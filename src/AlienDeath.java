import greenfoot.Actor;

/**
 * AlienDeath is a simple animation that plays when an alien is killed.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class AlienDeath extends Actor {
    /**
     * The variant of the alien.
     */
    private final int variant;
    /**
     * The current frame of the animation.
     */
    private int animationFrame = 1;

    /**
     * Constructor for AlienDeath. Sets the variant of the alien and plays the death sound.
     *
     * @param variation
     */
    public AlienDeath(int variation) {
        this.variant = variation;
        if (variant <= 3) {
            SoundService deathSound = new SoundService("DeathExplosion/AlienOrganic/1.wav");
            deathSound.volumeOffset = 10;
            deathSound.playSound();
        } else {
            new SoundService("DeathExplosion/AlienMetal/1.wav").playSound();
        }

    }

    /**
     * This method runs every frame. It checks if the animation is finished and if not, it plays the next frame. When the animation is finished, it removes itself.
     */
    public void act() {
        if (variant <= 3) {
            if (Space.animationSeconds % 4 == 0) {
                if (animationFrame <= 10) {
                    setImage("DeathExplosion/AlienOrganic/" + String.format("%02d", animationFrame) + ".png");
                    getImage().scale(100, 100);
                    animationFrame++;
                } else {
                    getWorld().removeObject(this);
                }
            }
        } else {
            if (Space.animationSeconds % 4 == 0) {
                if (animationFrame <= 12) {
                    setImage("DeathExplosion/AlienMetal/" + String.format("%02d", animationFrame) + ".png");
                    getImage().scale(64, 64);
                    animationFrame++;
                } else {
                    getWorld().removeObject(this);
                }
            }
        }
    }
}
