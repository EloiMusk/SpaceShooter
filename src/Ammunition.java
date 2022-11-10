import greenfoot.Actor;

/**
 * The ammunition for the player.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Ammunition extends Actor {
    /**
     * The current frame of the animation.
     */
    private int animationFrame = 0;

    /**
     * Creates a new instance of the ammunition and sets the first frame of the animation.
     */
    public Ammunition() {
        setImage("Ammunition/00.png");
    }

    /**
     * Act method of the ammunition. Calls the animation and moves the animation with a rotation towards the bottom simulating a falling movement.
     * When the bullet reaches the bottom of the screen it is removed from the world.
     */
    public void act() {
        animate();
        setLocation(getX(), getY() + 1);
        setRotation(getRotation() + 1);
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }

    /**
     * Animates the ammunition by changing the image every 2 tics.
     */
    private void animate() {
        if (Space.animationMilliSeconds % 2 == 0) {
            if (animationFrame >= 34) {
                animationFrame = 0;
            }
            setImage("Ammunition/" + String.format("%02d", animationFrame) + ".png");
            animationFrame++;
        }
    }
}
