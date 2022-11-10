import greenfoot.Actor;
import greenfoot.Greenfoot;

import java.util.HashMap;
import java.util.Map;

/**
 * A spaceship that can be controlled by the user.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class SpaceShip extends Actor {
    /**
     * The current animation frame.
     */
    private int animationFrame = 0;
    /**
     * The health of the spaceship.
     */
    public int health = 3;
    /**
     * The shield of the spaceship.
     */
    public int shield = 0;
    /**
     * The current ammunition of the spaceship.
     */
    public int ammunition = 50;
    /**
     * The maximum ammunition of the spaceship.
     */
    public int maxAmmunition = 40;
    /**
     * Whether the spaceship is currently firing.
     */
    private boolean isShooting = false;
    /**
     * The movement speed of the spaceship.
     */
    private int movementSpeed = 0;
    /**
     * The count of bullets fired at the same time.
     */
    private int bulletCount = 1;
    /**
     * The speed boost of the bullets.
     */
    private float bulletSpeedBoost = 1;
    /**
     * The damage boost of the bullets.
     */
    private float bulletDamageBoost = 1;
    /**
     * The base bullet damage boost of the bullets.
     */
    private int baseBulletDamageBoost;
    /**
     * The size boost of the bullets.
     */
    private float bulletSizeBoost = 1;
    /**
     * The bullet type of the bullets.
     */
    private int bulletType = 1;
    /**
     * The current state of the Bullet cool down.
     */
    private int bulletCoolDown = 0;
    /**
     * Instance of the SoundService for the heartBreak sound.
     */
    private final SoundService heartBreakSound = new SoundService("HeartBreak/1.mp3");
    /**
     * Instance of the SoundService for the shieldBreak sound.
     */
    private final SoundService shieldBreakSound = new SoundService("ShieldBreak/1.mp3");
    /**
     * Instance of the SoundService for the powerUp sound.
     */
    private final SoundService powerUpSound = new SoundService("PowerUp/1.mp3");
    /**
     * Hashmap which stores all Upgrade types and if they are active.
     */
    public Map<UpgradeType, Boolean> activeUpgrades = new HashMap<>();
    /**
     * Determines if the spaceship can currently shoot.
     */
    boolean canShoot = true;
    /**
     * Counter to control the shooting speed.
     */
    private int canShootDelay = 0;

    /**
     * Constructor for the SpaceShip class.
     */
    public SpaceShip() {
        setImage("SpaceShip/SpaceShip0.png");
        getImage().scale(64, 64);
        init();
    }

    /**
     * Initializes the SpaceShip.
     */
    private void init() {
        UpgradeType[] upgradeTypes = UpgradeType.values();
//        Sets the values for the activeUpgrades hashmap.
        for (UpgradeType type :
                upgradeTypes) {
            activeUpgrades.put(type, false);
        }
    }

    /**
     * Animates the spaceship by changing the image to the next frame.
     */
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

    /**
     * Controls of the spaceship.
     */
    public void controls() {
//        Moves the spaceship to the left.
        if (Greenfoot.isKeyDown("a")) {
            if (getX() > 0) {
                setLocation(getX() - (5 + movementSpeed), getY());
            }
        }
//        Moves the spaceship to the right.
        if (Greenfoot.isKeyDown("d")) {
            if (getX() < getWorld().getWidth()) {
                setLocation(getX() + (5 + movementSpeed), getY());
            }
        }
//        Moves the spaceship up.
        if (Greenfoot.isKeyDown("w")) {
            if (getY() > 0) {
                setLocation(getX(), getY() - (5 + movementSpeed));
            }
        }
//        Moves the spaceship down.
        if (Greenfoot.isKeyDown("s")) {
            if (getY() < getWorld().getHeight()) {
                setLocation(getX(), getY() + (5 + movementSpeed));
            }
        }
//        Shoots a bullet if the spaceship is not currently shooting and the bullet is not on cool down.
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
                new SoundService("Bullet/NoAmmo/1.mp3").playSound();
            }
        }
//        If space is not pressed, the spaceship is not shooting.
        if (!Greenfoot.isKeyDown("space") && isShooting) {
            isShooting = false;
        }
    }

    /**
     * Checks if the spaceship is colliding with an enemy. If so, the spaceship loses health.
     */
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

    /**
     * Plays the death animation of the spaceship.
     */
    private void playDeathAnimation() {
        new SoundService("DeathExplosion/SpaceShip/1.mp3").playSound();
        for (int i = 0; i <= 14; i++) {
            setImage("DeathExplosion/SpaceShip/" + String.format("%02d", i) + ".png");
            getImage().scale(100, 100);
            Greenfoot.delay(8);
        }
    }

    /**
     * Checks if the spaceship is colliding with a power up. If so, the spaceship gains the power up.
     */
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

    /**
     * Resets the activeUpgrades HashMap so that all bullets are set to false.
     */
    private void resetBulletType() {
        activeUpgrades.put(UpgradeType.ROCKET, false);
        activeUpgrades.put(UpgradeType.BOMB, false);
        activeUpgrades.put(UpgradeType.MISSILE, false);
        activeUpgrades.put(UpgradeType.NUKE, false);
    }

    /**
     * Cool down for the Upgrades and Bullets.
     */
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
//                System.out.println("Bullet speed reduced: " + bulletSpeedBoost);
            } else {
                activeUpgrades.put(UpgradeType.BULLET_SPEED, false);
            }
            if (bulletDamageBoost > baseBulletDamageBoost) {
                bulletDamageBoost -= 10;
//                System.out.println("Bullet damage reduced: " + bulletDamageBoost);
            } else {
                activeUpgrades.put(UpgradeType.BULLET_DAMAGE, false);
            }
            if (bulletSizeBoost > 1) {
                bulletSizeBoost -= (float) 1 / 3;
//                System.out.println("Bullet size reduced: " + bulletSizeBoost);
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
