import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.util.List;

/**
 * A bullet. This is the projectile fired by the player's ship or the enemy's. It can have 4 different types with different mechanics.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Bullet extends Actor {
    /**
     * The delay is used to determine when the explosion damage should be applied.
     */
    private int delay;
    /**
     * The amount of damage the default damage is increased by.
     */
    public float damageBoost = 1;
    /**
     * The base damage of the bullet.
     */
    public int damage = 100;
    /**
     * The explosion damage of the bullet.
     */
    public int explosionDamage = 50;
    /**
     * The amount of speed the default speed is increased by.
     */
    public float speedBoost = 1;
    /**
     * The base speed of the bullet.
     */
    public int speed = 8;
    /**
     * The type of the bullet.
     */
    public int bulletType;
    /**
     * The factor by which the bullet's size is increased.
     */
    public float sizeBoost = 1;
    /**
     * The base size of the bullet.
     */
    public int size = 20;
    /**
     * The current frame of the animation of the bullet.
     */
    private int animationFrame = 0;
    /**
     * The current frame of the animation of the explosion.
     */
    private int explosionFrame = 0;
    /**
     * Weather or not the bullet is exploding or not.
     */
    public boolean exploded = false;
    /**
     * Weather the bullet is exploding or not.
     */
    public boolean isExploding = false;
    /**
     * The maximum explosion frames.
     */
    private int maxExplosionFrame = 6;
    /**
     * The maximum animation frames.
     */
    private int maxAnimationFrame = 4;
    /**
     * The size of the explosion.
     */
    private int explosionSize = 20;
    /**
     * States weather the bullet is dealing explosion damage or not.
     */
    public boolean dealDamage = false;
    /**
     * The direction the Bullet is facing.
     */
    private int bulletDirection = 1;
    /**
     * States if the bullet is a player bullet or not.
     */
    public boolean isPlayerBullet = true;
    /**
     * Sound-service for the blast sound
     */
    private final SoundService bulletSound = new SoundService();
    /**
     * Sound-service for the explosion sound
     */
    private final SoundService explosionSound = new SoundService();
    /**
     * States if the explosion sound has been played or not.
     */
    private boolean explosionSoundPlayed = false;

    /**
     * Creates a new bullet of the specified type.
     *
     * @param type The type of the bullet.
     */
    public Bullet(int type) {
        this.bulletType = type;
        init();
        setImage("Bullet/" + bulletType + "/0.png");
        getImage().scale(size, size);
        getImage().rotate(90);

    }

    /**
     * Creates a new bullet of the specified type and determines if it is a player bullet or not and sets the direction accordingly.
     *
     * @param type  The type of the bullet.
     * @param alien States if the bullet is a player bullet or not.
     */
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

    /**
     * Initializes the bullet and sets the values according to the bullet type.
     */
    private void init() {
        bulletSound.volumeOffset = BulletData.bullets[bulletType - 1].blastVolumeOffset;
        if (isPlayerBullet) {
            bulletSound.playSound("Bullet/" + bulletType + "/blast/" + (Greenfoot.getRandomNumber(3) + 1) + ".wav");
            explosionSound.setSound("Bullet/" + bulletType + "/explosion/1.wav");
        } else {
            bulletSound.playSound("Alien/blast/" + (Greenfoot.getRandomNumber(3) + 1) + ".wav");
        }
        setRotation(-90);
        damage = (int) (BulletData.bullets[bulletType - 1].damage / damageBoost);
        speed = (int) (BulletData.bullets[bulletType - 1].speed / speedBoost);
        size = (int) (BulletData.bullets[bulletType - 1].size / sizeBoost);
        explosionSize = BulletData.bullets[bulletType - 1].explosionSize;
        explosionDamage = (int) (BulletData.bullets[bulletType - 1].explosionDamage / damageBoost);
        maxExplosionFrame = BulletData.bullets[bulletType - 1].explosionAnimationFrameCount;
        delay = BulletData.bullets[bulletType - 1].delay;
        maxAnimationFrame = BulletData.bullets[bulletType - 1].maxAnimationFrames;
    }

    /**
     * Animates the bullet by setting the image to the next frame.
     */
    private void animation() {
        if ((Space.animationMilliSeconds * 10) % 30 == 0) {
            if (animationFrame >= maxAnimationFrame) {
                animationFrame = 0;
            }
            GreenfootImage frame = new GreenfootImage("Bullet/" + bulletType + "/" + animationFrame + ".png");
            frame.scale(size, size);
            frame.rotate(90);
            setImage(frame);
            animationFrame++;
        }
    }

    /**
     * Checks if the bullet is out of bounds and removes it if it is.
     */
    private void checkSurroundings() {
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }

    /**
     * Starts the explosion of the bullet.
     */
    public void startExplosion() {
        if (!explosionSoundPlayed && isPlayerBullet && bulletType != 4) {
            explosionSound.playSound();
            explosionSoundPlayed = true;
        }
        setLocation(getX(), getY() - (getImage().getHeight() / 2));
        damage = explosionDamage / maxExplosionFrame;
        isExploding = true;
    }

    /**
     * Animates the explosion of the bullet by setting the image to the next frame. Plays the explosion sound and removes the bullet when the animation is done.
     */
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
        if (dealDamage && !explosionSoundPlayed && isPlayerBullet && bulletType == 4) {
            explosionSound.playSound();
            explosionSoundPlayed = true;
        }
        explosionFrame++;
    }

    /**
     * Moves the bullet, animates it and checks if it is out of bounds, if it is not exploding.
     */
    public void act() {
        if (isExploding && (Space.animationMilliSeconds * 10) % 30 == 0) {
            explode();
        } else if (!isExploding) {
            moveOneStep();
            animation();
            checkSurroundings();
        }
    }

    /**
     * Moves the bullet one step in the direction it is facing.
     */
    private void moveOneStep() {
//        if the bullet is of type 4, it will turn the bullet towards the nearest alien
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
