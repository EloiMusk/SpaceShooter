import dataTypes.Settings;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.sql.SQLException;

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
    private Button toggleMute = new Button("V", Color.BLACK, new Color(200, 200, 200, 100), this::toggleMute);
    public static int volume = 100;
    private boolean muted = false;

    private void toggleMute() {
        try {
            DbService.toggleMute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        score = 0;
        level = 1;
        addObject(new UI(), 400, 300);
        addObject(toggleMute, 50, 50);
        generateBackground();
        addObject(new SpaceShip(), 400, 500);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 10; c++) {
                if (Greenfoot.getRandomNumber(100) % 2 == 0) {
                    if (Greenfoot.getRandomNumber(100) % 2 == 0) {
                        addObject(new Alien(level, 100 + (r * 70), 100 + (c * 70)), 0, Greenfoot.getRandomNumber(getHeight()) / 3);
                    } else {
                        addObject(new Alien(level, 100 + (r * 70), 100 + (c * 70)), getWidth(), Greenfoot.getRandomNumber(getHeight()) / 3);
                    }
                } else {
                    addObject(new Alien(level, 100 + (r * 70), 100 + (c * 70)), Greenfoot.getRandomNumber(getWidth()), 0);
                }
            }
        }
    }

    public void addScore(int points) {
        score += points;
    }

    private void generateBackground() {
        setPaintOrder(Button.class, UI.class, SpaceShip.class, Bullet.class, Alien.class);
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
        try {
            Settings settings = DbService.getSettings();
            volume = settings.volume;
            if (volume <= 0 && !muted) {
                removeObject(toggleMute);
                toggleMute = new Button("\uD83D\uDD07", new Color(200, 200, 200, 100), Color.BLACK, this::toggleMute);
                addObject(toggleMute, 50, 50);
                muted = true;
            } else if (volume > 0 && muted) {
                removeObject(toggleMute);
                toggleMute = new Button("\uD83D\uDD0A", new Color(200, 200, 200, 100), Color.BLACK, this::toggleMute);
                addObject(toggleMute, 50, 50);
                muted = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getObjects(Alien.class).size() == 0) {
            level++;
            generateBackground();
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 10; c++) {
                    addObject(new Alien(level, 100 + (r * 70), 100 + (c * 70)), Greenfoot.getRandomNumber(getWidth()), Greenfoot.getRandomNumber(getHeight()) / 3);
                }
            }
        }
    }

    public void generateAmmunition() {
        if (Greenfoot.getRandomNumber(200) < level) {
            addObject(new Ammunition(), Greenfoot.getRandomNumber(800), 0);
        }
    }

    public void act() {
        refreshGameStats();
        runAnimationTimer();
        generateAmmunition();
    }
}
