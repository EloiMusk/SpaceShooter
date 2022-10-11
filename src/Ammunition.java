import greenfoot.Actor;

public class Ammunition extends Actor {
    private int animationFrame = 0;

    public Ammunition() {
        setImage("Ammunition/00.png");
    }

    private void animate() {
        if (Space.animationTimer % 2 == 0) {
            if (animationFrame >= 34) {
                animationFrame = 0;
            }
            setImage("Ammunition/" + String.format("%02d", animationFrame) + ".png");
            animationFrame++;
        }
    }

    public void act() {
        animate();
        setLocation(getX(), getY() + 1);
        setRotation(getRotation() + 1);
        if (isAtEdge()){
            getWorld().removeObject(this);
        }
    }
}
