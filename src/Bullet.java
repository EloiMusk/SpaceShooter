import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.util.List;

public class Bullet extends Actor {
    private int delay;
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

    public Bullet(int type) {
        this.bulletType = type;
        init();
        setImage("Bullet/" + bulletType + "/0.png");
        getImage().scale(size, size);
        getImage().rotate(90);

    }

    public Bullet(int type, boolean alien) {
        if (alien) {
            bulletDirection = -1;
            isPlayerBullet = false;
        }
        this.bulletType = type;
        init();
        setImage("Bullet/" + bulletType + "/0.png");
        getImage().scale(size, size);
        getImage().rotate(90);
    }

    private void init() {
//                TODO: Migrate to use int instead of Float for boosters
        if (isPlayerBullet) {
            new SoundService("Bullet/" + bulletType + "/blast/" + (Greenfoot.getRandomNumber(3) + 1) + ".wav").playSound();
        }else {
            new SoundService("Alien/blast/" + (Greenfoot.getRandomNumber(3) + 1) + ".wav").playSound();
        }
        setRotation(-90);
        damage = (int) (BulletData.bullets[bulletType - 1].damage / damageBoost);
        speed = (int) (BulletData.bullets[bulletType - 1].speed / speedBoost);
        size = (int) (BulletData.bullets[bulletType - 1].size / sizeBoost);
        explosionSize = BulletData.bullets[bulletType - 1].explosionSize;
        explosionDamage = (int) (BulletData.bullets[bulletType - 1].explosionDamage / damageBoost);
        maxExplosionFrame = BulletData.bullets[bulletType - 1].explosionAnimatonFrameCount;
        delay = BulletData.bullets[bulletType - 1].delay;
    }

    private void animation() {
        if ((Space.animationMilliSeconds * 10) % 30 == 0) {
            if (animationFrame > 2) {
                animationFrame = 0;
            }
            GreenfootImage frame = new GreenfootImage("Bullet/" + bulletType + "/" + animationFrame + ".png");
            frame.scale(size, size);
            frame.rotate(90);
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
        setLocation(getX(), getY() - (getImage().getHeight() / 2));
        damage = explosionDamage / maxExplosionFrame;
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
        dealDamage = explosionFrame >= (float) maxExplosionFrame / 100 * (float) delay;
        if (maxExplosionFrame >= 10) {
            frame = new GreenfootImage("Bullet/" + bulletType + "/explosion/" + String.format("%02d", explosionFrame) + ".png");
        } else {
            frame = new GreenfootImage("Bullet/" + bulletType + "/explosion/" + explosionFrame + ".png");
        }
        frame.scale(explosionSize, explosionSize);
        frame.rotate(90);
        setImage(frame);
        explosionFrame++;
    }


    public void act() {
        if (isExploding && (Space.animationMilliSeconds * 10) % 30 == 0) {
            explode();
        } else if (!isExploding) {
            moveOneStep();
            animation();
            checkSurroundings();
        }
    }

    private void moveOneStep() {
        if (bulletType == 4) {
            List<Alien> alienList = getNeighbours(200, false, Alien.class);
            if (alienList.size() > 0) {
                Alien alien = alienList.get(0);
                turnTowards(alien.getX(), alien.getY());
            }
            move(speed * bulletDirection);
        } else {
            if (isPlayerBullet) {
                move(speed * bulletDirection);
            } else {
                move((speed / 2) * bulletDirection);
            }
        }
    }
}
