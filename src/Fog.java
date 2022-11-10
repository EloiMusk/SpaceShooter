import greenfoot.Actor;
import greenfoot.Greenfoot;

/**
 * Used to create fog in the background.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Fog extends Actor {
    /**
     * The speed of the fog
     */
    private int speed;
    /**
     * The speed modifier of the fog
     */
    private float speedModifier;
    /**
     * The direction of the fog
     */
    private int direction;
    /**
     * The transparency of the fog
     */
    public int transparency;
    /**
     * The width of the fog
     */
    public int width;
    /**
     * The height of the fog
     */
    public int height;

    /**
     * Creates a new fog object
     *
     * @param seed The seed of the fog used to place it in the world and deter,ome its direction
     */
    public Fog(int seed) {
        if (seed % 2 == 0) {
            direction = -1;
        } else {
            direction = 1;
        }
        setImage("Fog/" + (Greenfoot.getRandomNumber(3) + 1) + ".png");
        width = Greenfoot.getRandomNumber(250) + 250;
        height = (int) ((float) width / (float) getImage().getWidth() * (float) getImage().getHeight());
        getImage().scale(width, height);
        this.transparency = Greenfoot.getRandomNumber(50) + 10;
        getImage().setTransparency(transparency);
        speed = Greenfoot.getRandomNumber(3) + 2;
        speedModifier = ((float) (Greenfoot.getRandomNumber(1000) + 1) / 1000) + 1;
    }

    /**
     * Controls the speed of the fog
     */
    private void animation() {
        if (Space.animationMilliSeconds % speed == 0) {
            moveOnce();
        }
    }

    /**
     * Moves the fog once in the set direction
     */
    public void moveOnce() {
        setLocation(getX() + (int) (direction * speedModifier), getY() + (int) (speed * speedModifier));
    }

    public void act() {
//        Call the animation method
        animation();
//        Check if the fog is out of the world and remove it if it is
        if (getX() > getWorld().getWidth() + (getImage().getWidth() / 2) || (getX() < -(getImage().getWidth()) && getY() > getWorld().getHeight() / 2) || getY() > getWorld().getHeight() + (getImage().getHeight() / 2)) {
            int seed = Greenfoot.getRandomNumber(800) + 1;
            if (seed % 2 == 0) {
                getWorld().addObject(new Fog(seed), getWorld().getWidth() - 200, -500);
            } else {
                getWorld().addObject(new Fog(seed), 200, -500);
            }
            getWorld().removeObject(this);
        }
    }
}
