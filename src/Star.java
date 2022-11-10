import greenfoot.Actor;
import greenfoot.Greenfoot;

/**
 * Star is a simple actor that can be added to the world as a decoration.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Star extends Actor {
    /**
     * The movement speed of the star.
     */
    private int speed = 0;

    /**
     * Creates a default star.
     */
    public Star() {
        setImage("Star/" + (Greenfoot.getRandomNumber(3) + 1) + ".png");
        getImage().scale((Greenfoot.getRandomNumber(20) + 5), (Greenfoot.getRandomNumber(20) + 5));
        speed = Greenfoot.getRandomNumber(3) + 1;
    }

    /**
     * Creates a star with a given variation.
     *
     * @param variation The variation of the star.
     */
    public Star(int variation) {
        getImage().scale((Greenfoot.getRandomNumber(20) + 5), (Greenfoot.getRandomNumber(20) + 5));
        getImage().setTransparency(variation);
    }

    /**
     * Animate the star by moving it across the screen.
     */
    private void animation() {
        if (Space.animationMilliSeconds % speed == 0) {
            setLocation(getX(), getY() + 3);
        }
    }

    /**
     * Check if the star is at the edge of the world and remove it if it is.
     */
    public void act() {
        animation();
        if (isAtEdge()) {
            getWorld().addObject(new Star(), Greenfoot.getRandomNumber(800), 0);
            getWorld().removeObject(this);
        }
    }
}
