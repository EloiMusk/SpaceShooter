import greenfoot.Actor;
import greenfoot.Greenfoot;

public class SpaceShip extends Actor {
    private int animationFrame = 0;
    private boolean isShooting = false;

    public SpaceShip() {
        setImage("SpaceShip/SpaceShip0.png");
    }

    private void animation() {
        if (Space.animationTimer == 1){
            if (animationFrame > 2) {
                animationFrame = 0;
            }
            setImage("SpaceShip/SpaceShip" + animationFrame + ".png");
            animationFrame++;
        }
    }

    public void controls() {
        if (Greenfoot.isKeyDown("a")) {
            setLocation(getX() - 5, getY());
        }
        if (Greenfoot.isKeyDown("d")) {
            setLocation(getX() + 5, getY());
        }
        if (Greenfoot.isKeyDown("w")) {
            setLocation(getX(), getY() - 5);
        }
        if (Greenfoot.isKeyDown("s")) {
            setLocation(getX(), getY() + 5);
        }
        if (Greenfoot.isKeyDown("space") && !isShooting) {
            getWorld().addObject(new Bullet(), getX(), getY() - 60);
            isShooting = true;
        }
        if (!Greenfoot.isKeyDown("space") && isShooting) {
            isShooting = false;
        }
    }

    public void act() {
        animation();
        controls();
    }
}
