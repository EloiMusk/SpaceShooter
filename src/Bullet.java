import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Bullet extends Actor {
    public int damage = 1;
    public int speed = 8;
    public int bulletType = 1;
    public int size = 20;
    private int animationFrame = 0;
    private int explosionFrame = 0;
    private boolean exploded = false;
    private boolean isExploding = false;
    private int maxExplosionFrame = 10;
    public Bullet() {
        setImage("Bullet/" + bulletType + "/0.png");
        getImage().scale(size, size);
    }

    public Bullet(int type) {
        this.bulletType = type;
        setImage("Bullet/" + bulletType + "/0.png");
        getImage().scale(size, size);
    }
    private void animation() {
        if (Space.animationTimer == 1) {
            if (animationFrame > 2) {
                animationFrame = 0;
            }
            GreenfootImage frame = new GreenfootImage("Bullet/" + bulletType + "/" + animationFrame + ".png");
            frame.scale(size, size);
            setImage(frame);
            animationFrame++;
        }
    }

    private void checkSurroundings() {
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }

    private void explode(){
        if (Space.animationTimer == 1) {
            if (explosionFrame > maxExplosionFrame) {
                explosionFrame = 0;
                isExploding = false;
                exploded = true;
            }
            GreenfootImage frame = new GreenfootImage("Bullet/" + bulletType + "/explosion/" + explosionFrame + ".png");
            frame.scale(size, size);
            setImage(frame);
            explosionFrame++;
        }
    }


    public void act() {
        setLocation(getX(), getY() - speed);
        animation();
        checkSurroundings();
    }
}
