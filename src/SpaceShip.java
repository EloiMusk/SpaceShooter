import greenfoot.Actor;
import greenfoot.Greenfoot;

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
    private int bulletType = 2;

    public SpaceShip() {

        setImage("SpaceShip/SpaceShip0.png");
        getImage().scale(64, 64);
    }

    private void animation() {
        if (Space.animationTimer == 1) {
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
            setLocation(getX() - (5 + movementSpeed), getY());
        }
        if (Greenfoot.isKeyDown("d")) {
            setLocation(getX() + (5 + movementSpeed), getY());
        }
        if (Greenfoot.isKeyDown("w")) {
            setLocation(getX(), getY() - (5 + movementSpeed));
        }
        if (Greenfoot.isKeyDown("s")) {
            setLocation(getX(), getY() + (5 + movementSpeed));
        }
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
                this.shield--;
            } else {
                this.health--;
            }
        }
        if (this.health <= 0) {
            Space.gameOver();
        }
    }

    public void isHitByUpgrade() {
        if (isTouching(Upgrade.class)) {
            Upgrade upgrade = (Upgrade) getOneIntersectingObject(Upgrade.class);
            switch (upgrade.upgradeType) {
                case HEALTH:
                    if (this.health < 4) {
                        this.health++;
                    }
                    break;
                case SHIELD:
                    if (this.shield < 3) {
                        this.shield++;
                    }
                    break;
                case MOVEMENT_SPEED:
                    this.movementSpeed += 5;
                    break;
                case FIRE_RATE:
                    this.bulletCount += 2;
                    break;
                case BULLET_SPEED:
                    this.bulletSpeedBoost += 5;
                    break;
                case BULLET_DAMAGE:
                    this.bulletDamageBoost += 100;
                    break;
                case BULLET_SIZE:
                    this.bulletSizeBoost += 5;
                    break;
                case ROCKET:
                    this.bulletType = 2;
                    break;
                case BOMB:
                    this.bulletType = 3;
                    break;
                case MISSILE:
                    this.bulletType = 4;
                    break;
                case NUKE:
                    this.bulletType = 5;
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

    private void coolDown() {
        if (Space.animationTimer == 0) {
            if (Greenfoot.getRandomNumber(100) < 20) {
                if (this.ammunition < 40) {
                    this.ammunition++;
                }
            }
            if (Greenfoot.getRandomNumber(100) % 2 == 0) {
                if (movementSpeed > 0) {
                    movementSpeed--;
                }
            }
            if (Greenfoot.getRandomNumber(100) < 10) {
                if (bulletCount > 1) {
                    bulletCount--;
                }
                if (bulletSpeedBoost > 1) {
                    bulletSpeedBoost -= 0.1;
                }
                if (bulletDamageBoost > 1) {
                    bulletDamageBoost -= 0.1;
                }
                if (bulletSizeBoost > 1) {
                    bulletSizeBoost -= 0.1;
                }
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
