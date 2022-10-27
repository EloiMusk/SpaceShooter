import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

public class Alien extends Actor {
    private int animationFrame = 0;
    private int difficulty = 20;
    public int health = 1;
    public int maxHealth = 1;
//    private int variant = Greenfoot.getRandomNumber(0);
    private int variant = 1;
    public Alien() {
        this.health = this.difficulty;
        this.maxHealth = this.difficulty;
        setImage(getFrame());
        getImage().scale(50, 50);
    }

    public Alien(int difficulty) {
        this.difficulty = difficulty;
        this.health = this.difficulty;
        this.maxHealth = this.difficulty;
        setImage(getFrame());
        getImage().scale(50, 50);
    }

    private GreenfootImage getFrame(int frame) {
        GreenfootImage image = new GreenfootImage("Alien/" + variant + "/" + String.format("%02d", frame) + ".png");
        image.setColor(new Color(255, 255-difficulty, 255-difficulty));
        image.setFont(image.getFont().deriveFont(100));
        image.drawString("lvl."+String.valueOf(difficulty), 100, 100);
        image.setColor(Color.WHITE);
        image.fillRect(120, 300, 150, 20);
        image.setColor(Color.RED);
        image.fillRect(120, 300, (int) (150 * ((double) health / (double) maxHealth)), 20);
        return image;
    }

    private GreenfootImage getFrame(){
        GreenfootImage image = new GreenfootImage("Alien/" + variant + "/00.png");
        image.setColor(new Color(255, 255-difficulty, 255-difficulty));
        image.setFont(image.getFont().deriveFont(100));
        image.drawString("lvl."+String.valueOf(difficulty), 100, 100);
        image.setColor(Color.WHITE);
        image.fillRect(120, 300, 150, 20);
        image.setColor(Color.RED);
        image.fillRect(120, 300, (int) (150 * ((double) health / (double) maxHealth)), 20);
        return image;
    }

    private void animation() {
        if (Space.animationTimer % 2 == 0) {
            if (animationFrame >= 29) {
                animationFrame = 0;
            }
            setImage(getFrame(animationFrame));
            getImage().scale(50, 50);
            animationFrame++;
        }
    }

    public void spawnUpgrade() {
        if (Greenfoot.getRandomNumber(100) < 30) {
            getWorld().addObject(new Upgrade(), getX(), getY());
        }
    }

    public boolean isDead() {
        if (health <= 0) {
            return true;
        }
        return false;
    }

    public void isHit() {
        Space space = (Space) getWorld();
        if (isTouching(Bullet.class)) {
            health -= ((Bullet)getOneIntersectingObject(Bullet.class)).damage;
            removeTouching(Bullet.class);
            if (isDead()) {
                space.addScore(10 * difficulty);
                space.removeObject(this);
                spawnUpgrade();
            }
        }
    }

    public void act() {
        animation();
        isHit();
    }
}
