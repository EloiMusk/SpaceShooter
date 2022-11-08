import greenfoot.Actor;
import greenfoot.Greenfoot;

import java.util.HashMap;
import java.util.Map;

public class SpaceShip extends Actor {
    private int animationFrame = 0;
    public int health = 3;
    public int shield = 0;
    public int ammunition = 50;
    private boolean isShooting = false;
    private int movementSpeed = 0;
    private int bulletCount = 1;
    private float bulletSpeedBoost = 1;
    private float bulletDamageBoost = 1;
    private float bulletSizeBoost = 1;
    private int bulletType = 1;
    private int bulletCoolDown = 0;
    private final SoundService heartBreakSound = new SoundService("HeartBreak/1.wav");
    private final SoundService shieldBreakSound = new SoundService("ShieldBreak/1.wav");
    private final SoundService powerUpSound = new SoundService("PowerUp/1.wav");
    public Map<UpgradeType, Boolean> activeUpgrades = new HashMap<>();

    public SpaceShip() {
        setImage("SpaceShip/SpaceShip0.png");
        getImage().scale(64, 64);
        init();
    }

    private void init() {
        UpgradeType[] upgradeTypes = UpgradeType.values();
        for (UpgradeType type :
                upgradeTypes) {
            activeUpgrades.put(type, false);
        }
    }

    private void animation() {
        if (Space.animationMilliSeconds == 1) {
            if (animationFrame > 2) {
                animationFrame = 0;
            }
            setImage("SpaceShip/SpaceShip" + animationFrame + ".png");
            getImage().scale(64, 64);
            animationFrame++;
        }
    }

    public void controls() {
        if (Greenfoot.isKeyDown("a")) {
            if (getX() > 0) {
                setLocation(getX() - (5 + movementSpeed), getY());
            }
        }
        if (Greenfoot.isKeyDown("d")) {
            if (getX() < getWorld().getWidth()) {
                setLocation(getX() + (5 + movementSpeed), getY());
            }
        }
        if (Greenfoot.isKeyDown("w")) {
            if (getY() > 0) {
                setLocation(getX(), getY() - (5 + movementSpeed));
            }
        }
        if (Greenfoot.isKeyDown("s")) {
            if (getY() < getWorld().getHeight()) {
                setLocation(getX(), getY() + (5 + movementSpeed));
            }
        }
//        TODO: Different shooting speed depending on bullet type
        if (Greenfoot.isKeyDown("space") && !isShooting) {
            if (this.ammunition > 0) {
                for (int i = 0; i < this.bulletCount; i++) {
                    Bullet bullet = new Bullet(bulletType);
                    bullet.speedBoost = this.bulletSpeedBoost;
                    bullet.damageBoost = this.bulletDamageBoost;
                    bullet.sizeBoost = this.bulletSizeBoost;
                    getWorld().addObject(bullet, getX(), getY() - 60 + (i * 20));
                }
                isShooting = true;
                this.ammunition--;
            } else {
//                TODO: Add effect for no ammunition
                System.out.println("Out of ammunition");
            }
        }
        if (!Greenfoot.isKeyDown("space") && isShooting) {
            isShooting = false;
        }
    }

    public void isHit() {
        if (isTouching(Alien.class)) {
            removeTouching(Alien.class);
            if (this.shield > 0) {
                shieldBreakSound.playSound();
                this.shield--;
            } else {
                heartBreakSound.playSound();
                this.health--;
            }
        }
        if (isTouching(Bullet.class)) {
            Bullet bullet = (Bullet) getOneIntersectingObject(Bullet.class);
            if (!bullet.isPlayerBullet) {
                if (!bullet.isExploding) {
                    bullet.startExplosion();
                    if (this.shield > 0) {
                        shieldBreakSound.playSound();
                        this.shield--;
                    } else {
                        heartBreakSound.playSound();
                        this.health--;
                    }
                }
            }
        }
        if (this.health <= 0) {
            Space space = (Space) getWorld();
            space.gameOver();
        }
    }

    public void isHitByUpgrade() {
        if (isTouching(Upgrade.class)) {
            if (!powerUpSound.isPlaying()) {
                powerUpSound.playSound();
            }
            Upgrade upgrade = (Upgrade) getOneIntersectingObject(Upgrade.class);
            switch (upgrade.upgradeType) {
                case HEALTH:
                    activeUpgrades.put(UpgradeType.HEALTH, true);
                    if (this.health < 4) {
                        this.health++;
                    }
                    break;
                case SHIELD:
                    activeUpgrades.put(UpgradeType.SHIELD, true);
                    if (this.shield < 3) {
                        this.shield++;
                    }
                    break;
                case MOVEMENT_SPEED:
                    activeUpgrades.put(UpgradeType.MOVEMENT_SPEED, true);
                    this.movementSpeed += 5;
                    break;
                case FIRE_RATE:
                    activeUpgrades.put(UpgradeType.FIRE_RATE, true);
                    this.bulletCount += 2;
                    break;
                case BULLET_SPEED:
                    activeUpgrades.put(UpgradeType.BULLET_SPEED, true);
                    this.bulletSpeedBoost += 10;
                    break;
                case BULLET_DAMAGE:
                    activeUpgrades.put(UpgradeType.BULLET_DAMAGE, true);
                    this.bulletDamageBoost += 100;
                    break;
                case BULLET_SIZE:
                    activeUpgrades.put(UpgradeType.BULLET_SIZE, true);
                    this.bulletSizeBoost += 10;
                    break;
                case ROCKET:
                    resetBulletType();
                    activeUpgrades.put(UpgradeType.ROCKET, true);
                    this.bulletType = 2;
                    this.bulletCoolDown = 0;
                    break;
                case BOMB:
                    resetBulletType();
                    activeUpgrades.put(UpgradeType.BOMB, true);
                    this.bulletType = 3;
                    this.bulletCoolDown = 0;
                    break;
                case MISSILE:
                    resetBulletType();
                    activeUpgrades.put(UpgradeType.MISSILE, true);
                    this.bulletType = 4;
                    this.bulletCoolDown = 0;
                    break;
                case NUKE:
                    resetBulletType();
                    activeUpgrades.put(UpgradeType.NUKE, true);
                    this.bulletType = 5;
                    this.bulletCoolDown = 0;
                    break;
            }
            getWorld().removeObject(upgrade);
        }
        if (isTouching(Ammunition.class)) {
            if ((this.ammunition + 5) <= 40) {
                this.ammunition += 5;
            } else {
                this.ammunition = 40;
            }
            getWorld().removeObject(getOneIntersectingObject(Ammunition.class));
        }
    }

    private void resetBulletType() {
        activeUpgrades.put(UpgradeType.ROCKET, false);
        activeUpgrades.put(UpgradeType.BOMB, false);
        activeUpgrades.put(UpgradeType.MISSILE, false);
        activeUpgrades.put(UpgradeType.NUKE, false);
    }

    private void coolDown() {
//      Every 1.6s and a bit seconds
        if (Space.animationSeconds % 100 == 0) {
            if (this.ammunition < 40) {
                this.ammunition += (Space.level / 2) + 1;
            }
        }
//        Every 500 or something milliseconds
        if (Space.animationSeconds % 40 == 0) {
            if (movementSpeed > 0) {
                movementSpeed--;
            } else {
                activeUpgrades.put(UpgradeType.MOVEMENT_SPEED, false);
            }
        }
        if (Space.animationSeconds % 50 == 0) {
            if (bulletCount > 1) {
                bulletCount -= 0.2;
            } else {
                activeUpgrades.put(UpgradeType.FIRE_RATE, false);
            }
            if (bulletSpeedBoost > 1) {
                bulletSpeedBoost -= (float) 1 / 3;
                System.out.println("Bullet speed reduced: " + bulletSpeedBoost);
            } else {
                activeUpgrades.put(UpgradeType.BULLET_SPEED, false);
            }
            if (bulletDamageBoost > 1) {
                bulletDamageBoost -= 10;
                System.out.println("Bullet damage reduced: " + bulletDamageBoost);
            } else {
                activeUpgrades.put(UpgradeType.BULLET_DAMAGE, false);
            }
            if (bulletSizeBoost > 1) {
                bulletSizeBoost -= (float) 1 / 3;
                System.out.println("Bullet size reduced: " + bulletSizeBoost);
            } else {
                activeUpgrades.put(UpgradeType.BULLET_SIZE, false);
            }
        }
        if (Space.animationSeconds % 60 == 0 && bulletType > 1) {
            if (bulletCoolDown >= BulletData.getBullet(bulletType).coolDown) {
                bulletCoolDown = 0;
                bulletType = 1;
                resetBulletType();
            } else {
                bulletCoolDown++;
            }
        }

    }

    public void act() {
        animation();
        controls();

        isHit();
        isHitByUpgrade();
        coolDown();
    }
}
