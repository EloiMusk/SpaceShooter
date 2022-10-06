import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Bullet extends Actor {
    public int damage = 1;

    public Bullet() {
        damage = 1;
        setImage("Bullet/" + damage + "/0.png");
        getImage().scale(20, 20);
    }

    public Bullet(int damage) {
        this.damage = damage;
        setImage("Bullet/" + damage + "/0.png");
        getImage().scale(20, 20);
    }

    private int animationFrame = 0;

    private void animation() {
        if (Space.animationTimer == 1) {
            if (animationFrame > 2) {
                animationFrame = 0;
            }
            GreenfootImage frame = new GreenfootImage("Bullet/" + damage + "/" + animationFrame + ".png");
            frame.scale(20, 20);
            setImage(frame);
            animationFrame++;
        }
    }

    private void checkSurroundings() {
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }


    public void act() {
        setLocation(getX(), getY() - 8);
        animation();
        checkSurroundings();
    }
}
