import greenfoot.Actor;
import greenfoot.Greenfoot;

public class Alien extends Actor {
    private int animationFrame = 0;
    private int difficulty = 1;

    public Alien() {
        setImage("Alien/"+ difficulty + "/00.png");
        getImage().scale(50, 50);
    }

    public Alien(int difficulty){
        this.difficulty = difficulty;
        setImage("Alien/"+ difficulty +"/00.png");
        getImage().scale(50, 50);
    }

    private void animation(){
        if (Space.animationTimer%2 == 0){
            if (animationFrame >= 29) {
                animationFrame = 0;
            }
            setImage("Alien/"+ difficulty + "/" + String.format("%02d", animationFrame) + ".png");
            getImage().scale(50, 50);
            animationFrame++;
        }
    }

    public void spawnUpgrade() {
        if (Greenfoot.getRandomNumber(100) < 10) {
            getWorld().addObject(new Upgrade(), getX(), getY());
        }
    }

    public void isHit(){
        Space space = (Space) getWorld();
        if (isTouching(Bullet.class)){
            removeTouching(Bullet.class);
            spawnUpgrade();
            space.addScore(10);
            space.removeObject(this);
        }
    }

    public void act() {
        animation();
        isHit();
    }
}
