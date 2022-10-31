import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Bullet extends Actor {
    private static class BulletData {
        int speed;
        int damage;
        int size;
        int explosionSize;
        int explosionDamage;
        int explosionAnimatonFrameCount;

        public BulletData(int speed, int damage, int size, int explosionSize, int explosionDamage, int explosionAnimatonFrameCount) {
            this.speed = speed;
            this.damage = damage;
            this.size = size;
            this.explosionSize = explosionSize;
            this.explosionDamage = explosionDamage;
            this.explosionAnimatonFrameCount = explosionAnimatonFrameCount;
        }
    }

    public float damageBoost = 1;
    public int damage = 100;
    public int explosionDamage = 50;
    public float speedBoost = 1;
    public int speed = 8;
    public int bulletType;
    public float sizeBoost = 1;
    public int size = 20;
    private int animationFrame = 0;
    private int explosionFrame = 0;
    public boolean exploded = false;
    public boolean isExploding = false;
    private int maxExplosionFrame = 6;
    private int explosionSize = 20;
    public boolean dealDamage = false;
    private int bulletDirection = 1;
    public boolean isPlayerBullet = true;
    private final BulletData[] bulletData = {
//            Bullet type 1 (Default)
            new BulletData(8, 100, 15, 30, 50, 6),
//            Bullet type 2 (Rocket)
            new BulletData(10, 150, 50, 50, 100, 16),
//            Bullet type 3 (Bomb)
            new BulletData(7, 20, 20, 40, 300, 15),
//            Bullet type 4 (Missile)
            new BulletData(6, 20, 80, 80, 200, 30),
//            Bullet type 5 (Nuke)
            new BulletData(5, 20, 90, 90, 200, 23)
    };

//    public Bullet() {
//        setImage("Bullet/" + bulletType + "/0.png");
//        getImage().scale(size, size);
//        init();
//    }

    public Bullet(int type) {
        this.bulletType = type;
        setImage("Bullet/" + bulletType + "/0.png");
        getImage().scale(size, size);
        init();
    }
    public Bullet(int type, boolean alien) {
        if (alien) {
            bulletDirection = -1;
            isPlayerBullet = false;
        }
        this.bulletType = type;
        setImage("Bullet/" + bulletType + "/0.png");
        getImage().scale(size, size);
        init();
    }

    private void init() {
        damage = (int) (bulletData[bulletType - 1].damage * damageBoost);
        speed = (int) (bulletData[bulletType - 1].speed * speedBoost);
        size = (int) (bulletData[bulletType - 1].size * sizeBoost);
        explosionSize = bulletData[bulletType - 1].explosionSize;
        explosionDamage = (int) (bulletData[bulletType - 1].explosionDamage * damageBoost);
        maxExplosionFrame = bulletData[bulletType - 1].explosionAnimatonFrameCount;
    }

    private void animation() {
        if (Space.animationTimer == 1) {
            if (animationFrame > 2) {
                animationFrame = 0;
            }
            GreenfootImage frame = new GreenfootImage("Bullet/" + bulletType + "/" + animationFrame + ".png");
            frame.scale(size, size);
            setImage(frame);
            animationFrame++;
        }
    }

    private void checkSurroundings() {
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }

    public void startExplosion() {
        System.out.println("Explosion started: " + explosionDamage);
        damage = (explosionDamage / 100) * (50 / maxExplosionFrame);
        setLocation(getX(), getY() - speed);
        isExploding = true;
    }

    private void explode() {
        GreenfootImage frame;
        if (explosionFrame > maxExplosionFrame) {
            explosionFrame = 0;
            isExploding = false;
            exploded = true;
            getWorld().removeObject(this);
        }
        dealDamage = explosionFrame >= maxExplosionFrame / 2;
        if (maxExplosionFrame >= 10) {
            frame = new GreenfootImage("Bullet/" + bulletType + "/explosion/" + String.format("%02d", explosionFrame) + ".png");
        } else {
            frame = new GreenfootImage("Bullet/" + bulletType + "/explosion/" + explosionFrame + ".png");
        }
        frame.scale(explosionSize, explosionSize);
        setImage(frame);
        explosionFrame++;
    }


    public void act() {
        if (isExploding && (Space.animationTimer * 10) % 30 == 0) {
            explode();
        } else if (!isExploding) {
            setLocation(getX(), getY() - (speed * bulletDirection));
            animation();
            checkSurroundings();
        }
    }
}
