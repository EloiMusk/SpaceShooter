import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.util.List;

public class Alien extends Actor {
    private int animationFrame = 0;
    private int difficulty = 1;
    public int health;
    public int maxHealth;
    private final int variant = Greenfoot.getRandomNumber(6) + 1;
    private final int[] maxAnimationFrames = {28, 35, 6, 3, 5, 11};
    private final double[] maxAnimationSpeed = {3, 2.5, 6, 10, 8, 4};
    private boolean spawned = true;
    private int finalY;
    private int finalX;
    int ySteps;
    int xSteps;
    boolean stepsSet = false;
    int slideCounter = 0;

    public Alien() {
        this.health = this.difficulty * 100;
        this.maxHealth = this.difficulty * 100;
        setImage(getFrame(0));
        getImage().scale(50, 50);
    }

    public Alien(int level, int y, int x) {
        finalY = y;
        finalX = x;
        this.difficulty = (int) (level * Math.round((Math.random() * 2) + 1));
        this.health = this.difficulty * 100;
        this.maxHealth = this.difficulty * 100;
        setImage(getFrame(0));
        getImage().scale(50, 50);
    }

    private void slideIn() {
        if (getY() < finalY) {
            setLocation(getX(), getY() + ySteps);
        } else if (getY() > finalY) {
            setLocation(getX(), getY() - ySteps);
        }
        if (getX() < finalX) {
            setLocation(getX() + xSteps, getY());
        } else if (getX() > finalX) {
            setLocation(getX() - xSteps, getY());
        }
        if ((getY() < finalY && finalY > getY() + ySteps) || (getY() > finalY && finalY < getY() - ySteps)) {
            setLocation(getX(), finalY);
        }
        if ((getX() < finalX && finalX < getX() + xSteps) || (getX() > finalX && finalX > getX() - xSteps)) {
            setLocation(finalX, getY());
        }
        if (getX() == finalX && getY() == finalY) {
            spawned = false;
        }
    }

    private GreenfootImage getFrame(int frame) {
        GreenfootImage image;
        if (maxAnimationFrames[variant - 1] >= 10) {
            image = new GreenfootImage("Alien/" + variant + "/" + String.format("%02d", frame) + ".png");
        } else {
            image = new GreenfootImage("Alien/" + variant + "/" + frame + ".png");
        }
        image.scale(400, 400);
        image.setColor(new Color(255, 255 - difficulty, 255 - difficulty));
        image.setFont(image.getFont().deriveFont(100));
        image.drawString("lvl." + difficulty, 100, 100);
        image.setColor(Color.WHITE);
        image.fillRect(120, 300, 150, 20);
        image.setColor(Color.RED);
        image.fillRect(120, 300, (int) (150 * ((double) health / (double) maxHealth)), 20);
        return image;
    }

    private float lcm(int n1, int n2) {
        int lcm;
        lcm = Math.max(n1, n2);
        while (true) {
            if (lcm % n1 == 0 && lcm % n2 == 0) {
                return lcm;
            }
            ++lcm;
        }
    }

    private void animation() {
        if (Space.animationMilliSeconds % maxAnimationSpeed[variant - 1] == 0) {
            if (animationFrame >= maxAnimationFrames[variant - 1]) {
                animationFrame = 0;
            }
            setImage(getFrame(animationFrame));
            getImage().scale(50, 50);
            animationFrame++;
        }
    }

    public void spawnUpgrade() {
        try {
            if (Greenfoot.getRandomNumber(100) < ((double) (100 / 200) * difficulty) + 10 || true) {
                getWorld().addObject(new Upgrade(), getX(), getY());
            }
        } catch (Exception e) {
//           Do nothing
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void isHit() {
        Space space = (Space) getWorld();
        if (isTouching(Bullet.class)) {
            List<Bullet> bullets = getIntersectingObjects(Bullet.class);
            for (Bullet bullet : bullets) {
                if (bullet.isPlayerBullet) {
                    if (!bullet.isExploding) {
                        health -= bullet.damage;
                        bullet.startExplosion();
                    }
                    if (bullet.dealDamage) {
                        health -= bullet.damage;
                    }
                    if (isDead()) {
                        space.addScore(10 * difficulty);
                        spawnUpgrade();
                        space.removeObject(this);
                    }
                }
            }
        }
    }

    private void shoot() {
        if (Space.animationMilliSeconds % 3 == 0 && Greenfoot.getRandomNumber(1000) < difficulty) {
            getWorld().addObject(new Bullet(1, true), getX(), getY());
        }
    }

    public void act() {
        if (slideCounter >= 80) {
            setLocation(finalX, finalY);
            spawned = false;
        } else {
            slideCounter++;
        }
        if (!stepsSet) {
            ySteps = (getY() - finalY) / 60;
            xSteps = (getX() - finalX) / 60;
            if (ySteps < 0) {
                ySteps = -ySteps;
            }
            if (xSteps < 0) {
                xSteps = -xSteps;
            }
            stepsSet = true;
        }
        if (spawned) {
            slideIn();
        } else {
            shoot();
            isHit();
        }
        animation();
    }
}
