import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;


public class Space extends World {
    public static int level = 1;
    public static int score = 0;
    public static int animationTimer = 10;

    public Space() {
        super(800, 600, 1);
        startGame();
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
    }

    private void generateBackground() {
        setPaintOrder(UI.class, SpaceShip.class, Alien.class, Bullet.class);
        GreenfootImage background = new GreenfootImage("Background/" + level + ".png");
        background.scale(800, 600);
        setBackground(background);
        for (int i = 0; i < 30; i++) {
            addObject(new Star(), Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
        }
    }

    private void runAnimationTimer() {
        if (animationTimer > 0) {
            animationTimer--;
        } else {
            animationTimer = 10;
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
