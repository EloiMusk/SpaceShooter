import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

public class Space extends World {
    public Space() {
        super(800, 600, 1);
        startGame();
    }

    public static int level = 1;
    public static int animationMilliSeconds;
    public static int animationSeconds;
    public static int animationMinutes;
    public static int score = 0;

    private void decrementAnimationMilliseconds() {
        if (animationMilliSeconds > 0) {
            animationMilliSeconds--;
        } else {
            animationMilliSeconds = 60;
        }
    }

    private void decrementAnimationSeconds() {
        if (animationSeconds > 0) {
            animationSeconds--;
        } else {
            animationSeconds = 60 * 60;
        }
    }

    private void decrementAnimationMinutes() {
        if (animationMinutes > 0) {
            animationMinutes--;
        } else {
            animationMinutes = 60 * 60 * 60;
        }
    }

    private void runAnimationTimer() {
        decrementAnimationMilliseconds();
        decrementAnimationSeconds();
        decrementAnimationMinutes();
    }

    public void startGame() {
        addObject(new UI(), 400, 300);
        generateBackground();
        addObject(new SpaceShip(), 400, 500);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 10; c++) {
                addObject(new Alien(), 100 + (c * 70), 100 + (r * 70));
            }
        }
        score = 0;
    }

    public void addScore(int points) {
        score += points;
    }

    private void generateBackground() {
        setPaintOrder(UI.class, SpaceShip.class, Alien.class, Bullet.class);
//        GreenfootImage background = new GreenfootImage("Background/" + level + ".png");
        GreenfootImage background = new GreenfootImage("Background/" + (Greenfoot.getRandomNumber(1) + 1) + ".png");
        background.scale(800, 600);
        setBackground(background);
        for (int i = 0; i < 10; i++) {
            addObject(new Star(), Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
        }
    }

    public static void gameOver() {
        Greenfoot.setWorld(new Menu(GameState.GAME_OVER));
    }

    public void refreshGameStats() {
        if (getObjects(Alien.class).size() == 0) {
            level++;
            generateBackground();
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 10; c++) {
                    addObject(new Alien(), 100 + (c * 70), 100 + (r * 70));
                }
            }
        }
    }

    public void gemerateAmmunition() {
        if (Greenfoot.getRandomNumber(150) < 1) {
            addObject(new Ammunition(), Greenfoot.getRandomNumber(800), 0);
        }
    }

    public void act() {
        refreshGameStats();
        runAnimationTimer();
        gemerateAmmunition();
    }
}
