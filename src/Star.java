import greenfoot.Actor;
import greenfoot.Greenfoot;

public class Star extends Actor {
    private int speed = 0;

    public Star() {
        setImage("Star/" + (Greenfoot.getRandomNumber(3) + 1) + ".png");
        getImage().scale((Greenfoot.getRandomNumber(20) + 5), (Greenfoot.getRandomNumber(20) + 5));
        speed = Greenfoot.getRandomNumber(3) + 1;
    }

    public Star(int Variation) {
        getImage().scale((Greenfoot.getRandomNumber(20) + 5), (Greenfoot.getRandomNumber(20) + 5));
        getImage().setTransparency(Variation);
    }

    private void animation() {
        if (Space.animationMilliSeconds % speed == 0) {
                setLocation(getX(), getY() + 3);
            }
    }

    public void act() {
        animation();
        if (isAtEdge()) {
            getWorld().addObject(new Star(), Greenfoot.getRandomNumber(800), 0);
            getWorld().removeObject(this);
        }
    }
}
