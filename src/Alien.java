import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.util.List;
import java.util.Map;

/**
 * An alien Actor. This class is used to create an alien Actor that can be added to the world.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Alien extends Actor {
    /**
     * The current difficulty level of the alien.
     */
    private int difficulty = 1;
    /**
     * The current health of the alien.
     */
    public int health;
    /**
     * The maximum health of the alien.
     */
    public int maxHealth;
    /**
     * The variant of the alien. Determines the look of the alien.
     */
    public final int variant = Greenfoot.getRandomNumber(6) + 1;
    /**
     * The current frame of the animation.
     */
    private int animationFrame = 0;
    /**
     * Array of how many frames each variant has. Mapped according to the variants.
     */
    private final int[] maxAnimationFrames = {28, 35, 6, 3, 5, 11};
    /**
     * The playback speed of the animation. Mapped according to the variants.
     */
    private final double[] maxAnimationSpeed = {3, 2.5, 6, 10, 8, 4};
    /**
     * States if the alien has spawned false = not spawned true = spawned.
     */
    public boolean spawned = false;
    /**
     * The final Y position of the alien. Used for the spawn and reformat animation.
     */
    public int finalY;
    /**
     * The final X position of the alien. Used for the spawn and reformat animation.
     */
    public int finalX;
    /**
     * States wether the alien has died or not. Used for the death animation.
     *
     * @see #isHit()
     */
    public boolean hasDied = false;

    /**
     * Constructor for the alien class. Sets the image, the health and the maxHealth of the alien.
     */
    public Alien() {
        this.health = this.difficulty * 100;
        this.maxHealth = this.difficulty * 100;
        setImage(getFrame(0));
        getImage().scale(50, 50);
    }

    /**
     * Constructor for the alien class. Sets image, health, maxHealth, difficulty, finalY and finalX position of the alien.
     *
     * @param level The current level of the game.
     * @param y     The final Y position of the alien.
     * @param x     The final X position of the alien.
     */
    public Alien(int level, int y, int x) {
        finalY = y;
        finalX = x;
        this.difficulty = (int) (level * Math.round((Math.random() * 2) + 1));
        this.health = this.difficulty * 100;
        this.maxHealth = this.difficulty * 100;
        setImage(getFrame(0));
        getImage().scale(50, 50);
    }

    /**
     * Act method of the alien class. Triggers the animation, movement, shooting and death of the alien.
     */
    public void act() {
        if (!spawned) {
            slideIn();
        } else {
            shoot();
            isHit();
        }
        animation();
    }

    /**
     * Determines if the alien has been hit by a bullet. If the alien has been hit, the health of the alien is decreased by the damage of the bullet.
     */
    public void isHit() {
        Space space = (Space) getWorld();
        if (isTouching(Bullet.class)) {
            List<Bullet> bullets = getIntersectingObjects(Bullet.class);
            for (Bullet bullet : bullets) {
                if (bullet.isPlayerBullet) {
                    if (!bullet.isExploding) {
                        health -= bullet.damage;
                        bullet.startExplosion();
                    }
                    if (bullet.dealDamage) {
                        health -= bullet.damage;
                    }
                    if (isDead() && !hasDied) {
                        hasDied = true;
                        space.addScore(10 * difficulty);
                        spawnUpgrade();
                        space.addObject(new AlienDeath(variant), getX(), getY());
                        space.removeObject(this);
                    }
                }
            }
        }
    }

    /**
     * Spawns and upgrade with a chance according to the current difficulty level:
     * <ul>
     *     <li>>=1: 20%</li>
     *     <li>>=5: 50%</li>
     *     <li>>=10: 100%</li>
     * </ul>
     */
    public void spawnUpgrade() {
        try {
            if ((difficulty >= 1 && Greenfoot.getRandomNumber(100) < 20) || (difficulty >= 5 && Greenfoot.getRandomNumber(100) < 50) || difficulty >= 10) {
                getWorld().addObject(new Upgrade(), getX(), getY());
            }
        } catch (Exception e) {
//           Do nothing
        }
    }

    /**
     * Determines if the alien is dead.
     *
     * @return true if the health of the alien is less than or equal to 0.
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Shoots a bullet at the player. The fire rate is determined by the difficulty level.
     */
    private void shoot() {
        if (Space.animationMinutes % 30 == 0 && Greenfoot.getRandomNumber(200) < difficulty / 2) {
            getWorld().addObject(new Bullet(1, true), getX(), getY());
        }
    }

    /**
     * Smoothly slides the alien from its spawn position to its final position.
     */
    private void slideIn() {
        if (getY() - 10 >= finalY) {
            setLocation(getX(), getY() - 10);
        } else if (getY() - 1 >= finalY) {
            setLocation(getX(), getY() - 1);
        } else if (getY() + 10 <= finalY) {
            setLocation(getX(), getY() + 10);
        } else setLocation(getX(), Math.min(getY() + 1, finalY));

        if (getX() - 10 >= finalX) {
            setLocation(getX() - 10, getY());
        } else if (getX() - 1 >= finalX) {
            setLocation(getX() - 1, getY());
        } else if (getX() + 10 <= finalX) {
            setLocation(getX() + 10, getY());
        } else setLocation(Math.min(getX() + 1, finalX), getY());
        if (getX() == finalX && getY() == finalY) {
            spawned = true;
        }
    }

    /**
     * Animates the alien by setting the next frame of the animation.
     */
    private void animation() {
        if (Space.animationMilliSeconds % maxAnimationSpeed[variant - 1] == 0) {
            if (animationFrame >= maxAnimationFrames[variant - 1]) {
                animationFrame = 0;
            }
            setImage(getFrame(animationFrame));
            getImage().scale(50, 50);
            animationFrame++;
        }
    }

    /**
     * Returns the given frame of the animation and adds the health bar and power level to the alien.
     *
     * @param frame The current frame of the animation.
     * @return The next frame of the animation.
     */
    private GreenfootImage getFrame(int frame) {
        GreenfootImage image;
        if (maxAnimationFrames[variant - 1] >= 10) {
            image = new GreenfootImage("Alien/" + variant + "/" + String.format("%02d", frame) + ".png");
        } else {
            image = new GreenfootImage("Alien/" + variant + "/" + frame + ".png");
        }
        image.scale(400, 400);
        image.setColor(new Color(255, 255 - difficulty, 255 - difficulty));
        image.setFont(image.getFont().deriveFont(100));
        image.drawString("lvl." + difficulty, 100, 100);
        image.setColor(Color.WHITE);
        image.fillRect(120, 300, 150, 20);
        image.setColor(Color.RED);
        image.fillRect(120, 300, (int) (150 * ((double) health / (double) maxHealth)), 20);
        return image;
    }

}
