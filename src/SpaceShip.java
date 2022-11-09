import greenfoot.Actor;
import greenfoot.Greenfoot;

import java.util.HashMap;
import java.util.Map;

public class SpaceShip extends Actor {
    private int animationFrame = 0;
    public int health = 3;
    public int shield = 0;
    public int ammunition = 50;
    public int maxAmmunition = 40;
    private boolean isShooting = false;
    private int movementSpeed = 0;
    private int bulletCount = 1;
    private float bulletSpeedBoost = 1;
    private float bulletDamageBoost = 1;
    private int baseBulletDamageBoost;
    private float bulletSizeBoost = 1;
    private int bulletType = 1;
    private int bulletCoolDown = 0;
    private final SoundService heartBreakSound = new SoundService("HeartBreak/1.wav");
    private final SoundService shieldBreakSound = new SoundService("ShieldBreak/1.wav");
    private final SoundService powerUpSound = new SoundService("PowerUp/1.wav");
    public Map<UpgradeType, Boolean> activeUpgrades = new HashMap<>();
    boolean canShoot = true;
    private int canShootDelay = 0;

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
        if (Greenfoot.isKeyDown("space") && !isShooting && canShoot) {
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
                canShoot = false;
                canShootDelay = 0;
            } else {
                new SoundService("Bullet/NoAmmo/1.wav").playSound();
            }
        }
        if (!Greenfoot.isKeyDown("space") && isShooting) {
            isShooting = false;
        }
    }

    public void isHit() {
        if (isTouching(Alien.class)) {
            Alien alien = (Alien) getOneIntersectingObject(Alien.class);
            if (alien.spawned) {
                alien.hasDied = true;
                getWorld().addObject(new AlienDeath(alien.variant), alien.getX(), alien.getY());
                getWorld().removeObject(alien);
                if (this.shield > 0) {
                    shieldBreakSound.playSound();
                    this.shield--;
                } else {
                    heartBreakSound.playSound();
                    this.health--;
                }
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
            playDeathAnimation();
            Space space = (Space) getWorld();
            space.gameOver();
        }
    }

    private void playDeathAnimation() {
        new SoundService("DeathExplosion/SpaceShip/1.wav").playSound();
        for (int i = 0; i <= 14; i++) {
            setImage("DeathExplosion/SpaceShip/" + String.format("%02d", i) + ".png");
            getImage().scale(100, 100);
            Greenfoot.delay(8);
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
                    this.baseBulletDamageBoost += Space.level;
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
            if ((this.ammunition + 5) <= maxAmmunition) {
                this.ammunition += 5;
            } else {
                this.ammunition = maxAmmunition;
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
        if (Space.animationSeconds % (60 / 1.5) == 0) {
            if (this.ammunition < maxAmmunition) {
                this.ammunition += (Space.level / 2) + 1;
            }
        }

        if (Space.animationSeconds % (60 / 0.5) == 0) {
            if (movementSpeed > 0) {
                movementSpeed--;
            } else {
                activeUpgrades.put(UpgradeType.MOVEMENT_SPEED, false);
            }
        }

        if (Space.animationSeconds % (60 / 0.8) == 0) {
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
            if (bulletDamageBoost > baseBulletDamageBoost) {
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

        if (bulletType > 1 && BulletData.getBullet(bulletType).coolDown * 60 > bulletCoolDown) {
            bulletCoolDown++;
        } else {
            bulletType = 1;
            resetBulletType();
            bulletCoolDown = 0;
        }
        if (canShootDelay < 60 / BulletData.getBullet(bulletType).fireRate && !canShoot) {
            canShootDelay++;
        } else {
            canShoot = true;
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
