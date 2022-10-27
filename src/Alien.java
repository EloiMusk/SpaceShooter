import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

public class Alien extends Actor {
    private int animationFrame = 0;
    private int difficulty = 2;
    public int health;
    public int maxHealth;
//    private int variant = Greenfoot.getRandomNumber(0);
    private int variant = 1;
    public Alien() {
        this.health = this.difficulty * 100;
        this.maxHealth = this.difficulty * 100;
        setImage(getFrame());
        getImage().scale(50, 50);
    }

    public Alien(int difficulty) {
        this.difficulty = difficulty;
        this.health = this.difficulty * 100;
        this.maxHealth = this.difficulty * 100;
        setImage(getFrame());
        getImage().scale(50, 50);
    }

    private GreenfootImage getFrame(int frame) {
        GreenfootImage image = new GreenfootImage("Alien/" + variant + "/" + String.format("%02d", frame) + ".png");
        image.setColor(new Color(255, 255-difficulty, 255-difficulty));
        image.setFont(image.getFont().deriveFont(100));
        image.drawString("lvl."+difficulty, 100, 100);
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
        image.drawString("lvl."+difficulty, 100, 100);
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
        return health <= 0;
    }

    public void isHit() {
        Space space = (Space) getWorld();
        if (isTouching(Bullet.class)) {
            Bullet bullet = (Bullet) getOneIntersectingObject(Bullet.class);
            if (!bullet.isExploding) {
                System.out.println("deal initial damage: " + bullet.damage);
                health -= bullet.damage;
                bullet.startExplosion();
            }
            if (bullet.dealDamage){
                System.out.println("deal explosion damage: " + bullet.damage);
                health -= bullet.damage;
            }
            if (isDead()) {
                space.addScore(10 * difficulty);
                spawnUpgrade();
                space.removeObject(this);
            }
        }
    }

    public void act() {
        animation();
        isHit();
    }
}
