import greenfoot.Actor;
import greenfoot.Greenfoot;

public class Fog extends Actor {
    private int speed;
    private float speedModifier;
    private int direction;
    public int transparency;
    public int width;
    public int height;

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

    private void animation() {
        if (Space.animationMilliSeconds % speed == 0) {
            moveOnce();
        }
    }

    public void moveOnce(){
        setLocation(getX() + (int) (direction * speedModifier), getY() + (int) (speed * speedModifier));
    }

    public void act() {
        animation();
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
