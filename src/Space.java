import dataTypes.Settings;
import greenfoot.*;
import javafx.scene.layout.Background;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Space extends World {
    public static int level = 1;
    public static int animationMilliSeconds;
    public static int animationSeconds;
    public static int animationMinutes;
    public static int score = 0;
    private Button toggleMute;
    public static int volume = 80;
    private boolean muted = false;
    public final SoundService backgroundMusic = new SoundService();
    private int backgroundScroll = 0;
    private GreenfootImage currentBackground;

    public Space() {
        super(800, 600, 1, false);
        startGame();
    }

    int currBg = 1;

    public void act() {
        setPaintOrder(Button.class, UI.class, SpaceShip.class, Bullet.class, Alien.class, Upgrade.class, Ammunition.class, Fog.class, Star.class);
        refreshBackground();
        refreshGameStats();
        runAnimationTimer();
        generateAmmunition();
        generateUpgrade();
        alienFormation();
    }

    private void setNewCurrentBackground() {
        currentBackground = new GreenfootImage("Background/" + (Greenfoot.getRandomNumber(13) + 1) + ".png");
        float scale = (float) getHeight() / (float) currentBackground.getHeight();
        currentBackground.scale((int) (scale * (float) currentBackground.getWidth()), getHeight());
        switch (Greenfoot.getRandomNumber(4) + 1) {
            case 1:
                this.currentBackground.rotate(90);
                break;
            case 2:
                this.currentBackground.rotate(180);
                break;
            case 3:
                this.currentBackground.rotate(270);
                break;
            default:
                break;
        }
    }

    public void startGame() {
        setNewCurrentBackground();
        try {
            Settings settings = DbService.getSettings();
            volume = settings.volume;
            if (volume == 0) {
                toggleMute = new Button("\uD83D\uDD07", new Color(200, 200, 200, 100), Color.BLACK, this::toggleMute);
            } else {
                toggleMute = new Button("\uD83D\uDD0A", new Color(200, 200, 200, 100), Color.BLACK, this::toggleMute);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        backgroundMusic.loop = true;
        backgroundMusic.volumeOffset = -45;
        playRandomBackgroundMusic();
        score = 0;
        level = 1;
        addObject(new UI(), 400, 300);
        addObject(toggleMute, 50, 50);
        addObject(new SpaceShip(), 400, 500);
        generateAliens();
        generateBackground();
    }

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

    public void addScore(int points) {
        score += points;
    }

    private void generateAliens() {
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

    private void generateBackground() {
        setPaintOrder(Button.class, UI.class, SpaceShip.class, Bullet.class, Alien.class, Upgrade.class, Fog.class, Star.class);
        for (int i = 0; i < 20; i++) {
            addObject(new Star(), Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
            addObject(new Fog(Greenfoot.getRandomNumber(800) + 1), Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
        }
    }

    private void refreshBackground() {
        GreenfootImage background = new GreenfootImage(getHeight(), getWidth());
        if (backgroundScroll > getHeight()) {
            backgroundScroll = 0;
        }
        background.drawImage(currentBackground, 0, backgroundScroll - currentBackground.getHeight());
        background.drawImage(currentBackground, 0, backgroundScroll);
        setBackground(background);
        if (animationSeconds % 3 == 0) {
            backgroundScroll++;
        }
    }

    private void playRandomBackgroundMusic() {
        String file = "Music/" + (Greenfoot.getRandomNumber(10) + 1) + ".mp3";
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stopSound();
        }
        backgroundMusic.playSound(file);
    }

    private void levelUp() {
        ArrayList<Fog> fogs = (ArrayList<Fog>) getObjects(Fog.class);
        getObjects(SpaceShip.class).get(0).maxAmmunition += level * 2;
        getObjects(SpaceShip.class).get(0).ammunition = getObjects(SpaceShip.class).get(0).maxAmmunition;
        SoundService levelUpSound = new SoundService();
        levelUpSound.playSound("LevelUp/1.wav");
        for (Upgrade upgrade : getObjects(Upgrade.class)) {
            removeObject(upgrade);
        }
        for (Ammunition ammunition : getObjects(Ammunition.class)) {
            removeObject(ammunition);
        }
        for (AlienDeath alienDeath : getObjects(AlienDeath.class)) {
            removeObject(alienDeath);
        }
        for (Bullet bullet : getObjects(Bullet.class)) {
            removeObject(bullet);
        }
        for (int i = 0; i < 30; i++) {
            for (Fog fog : fogs) {
                int setWidth = fog.getImage().getWidth();
                setWidth += ((int) (getWidth() * 1.5) - setWidth) / (30 - i);
                int setHeight = fog.getImage().getHeight();
                setHeight += ((int) (getWidth() * 1.5) - setHeight) / (30 - i);
                int transparency = fog.getImage().getTransparency();
                transparency += (255 - transparency) / (30 - i);
                fog.getImage().setTransparency(transparency);
                fog.getImage().scale(setWidth, setHeight);
                fog.moveOnce();
            }
            Greenfoot.delay(1);
        }
        for (int i = 0; i < 15; i++) {
            for (Fog fog : fogs) {
                fog.moveOnce();
            }
            Greenfoot.delay(1);
        }
        setNewCurrentBackground();
        setBackground(currentBackground);
        for (int i = 0; i < 30; i++) {
            for (Fog fog : fogs) {
                int setWidth = fog.getImage().getWidth();
                setWidth -= (setWidth - fog.width) / (30 - i);
                int setHeight = fog.getImage().getHeight();
                setHeight -= (setHeight - fog.height) / (30 - i);
                int transparency = fog.getImage().getTransparency();
                transparency -= (transparency - fog.transparency) / (30 - i);
                fog.getImage().setTransparency(transparency);
                fog.getImage().scale(setWidth, setHeight);
                fog.moveOnce();
            }
            Greenfoot.delay(1);
        }
        for (Fog fog : fogs) {
            fog.getImage().setTransparency(fog.transparency);
            fog.getImage().scale(fog.width, fog.height);
            fog.moveOnce();
        }
        level++;
        generateAliens();
        playRandomBackgroundMusic();
    }

    private void generateUpgrade() {
        if (animationMilliSeconds == 0) {
            if (level > 2 && Greenfoot.getRandomNumber(100) < 10) {
                addObject(new Upgrade(), Greenfoot.getRandomNumber(getWidth()), 0);
            }
            if (level > 5 && Greenfoot.getRandomNumber(100) < 30) {
                addObject(new Upgrade(), Greenfoot.getRandomNumber(getWidth()), 0);
            }
            if (level > 10 && Greenfoot.getRandomNumber(100) < 50) {
                addObject(new Upgrade(), Greenfoot.getRandomNumber(getWidth()), 0);
            }
        }
    }

    public void gameOver() {
        backgroundMusic.stopSound();
        new SoundService().playSound("GameOver/1.wav");
        Greenfoot.setWorld(new Menu(GameState.GAME_OVER));
    }

    public void refreshGameStats() {
        try {
            Settings settings = DbService.getSettings();
            volume = settings.volume;
            backgroundMusic.setVolume();
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
        ArrayList<Alien> aliens = (ArrayList<Alien>) getObjects(Alien.class);
        if (aliens.size() == 0) {
            levelUp();
        }
    }

    public void generateAmmunition() {
        if (Greenfoot.getRandomNumber(200) < level) {
            addObject(new Ammunition(), Greenfoot.getRandomNumber(800), 0);
        }
    }

    private void alienFormation() {
        if (animationMilliSeconds % 30 == 0) {
//        if (animationSeconds % 1 == 0) {
            ArrayList<Alien> aliens = (ArrayList<Alien>) getObjects(Alien.class);
            if (Greenfoot.getRandomNumber(100) < 50) {
                alienFormation1(aliens);
            } else if (Greenfoot.getRandomNumber(100) < 50) {
                alienFormation1(aliens);
            }
            alienFormation3(aliens);
        }

//        else if (level % 3 == 0) {
//            alienFormation2();
//        } else if (level % 5 == 0) {
//            alienFormation3();
//        } else {
//            alienFormation4();
//        }
    }

    //    A horizontal line of aliens
    private void alienFormation1(ArrayList<Alien> aliens) {
        for (Alien alien : aliens) {
            if (aliens.indexOf(alien) % 2 == 0) {
                alien.finalX = aliens.indexOf(alien) * ((getWidth() - (getWidth() / 2)) / aliens.size()) + alien.getImage().getWidth();
            } else {
                alien.finalX = getWidth() - (aliens.indexOf(alien) * ((getWidth() - (getWidth() / 2)) / aliens.size()) + alien.getImage().getWidth());
            }
            alien.finalY = ((getHeight() / 2) / aliens.size()) * aliens.indexOf(alien) + alien.getImage().getHeight();
        }
    }

    //    A U formation of aliens
    private void alienFormation2(ArrayList<Alien> aliens) {
        for (Alien alien : aliens) {
            if (aliens.indexOf(alien) % 4 == 0) {
                alien.finalX = aliens.indexOf(alien) * (getWidth() / aliens.size()) + alien.getImage().getWidth() / 2;
                alien.finalY = getHeight() / 10 + alien.getImage().getHeight();
            } else if (aliens.indexOf(alien) % 2 == 0) {
                alien.finalX = aliens.indexOf(alien) * (getWidth() / aliens.size()) + alien.getImage().getWidth() / 2;
                alien.finalY = getHeight() / 10 + alien.getImage().getHeight() * 2;
            } else {
                alien.finalX = aliens.indexOf(alien) * (getWidth() / aliens.size()) + alien.getImage().getWidth() / 2;
                alien.finalY = getHeight() / 10;
            }
            alien.spawned = alien.getY() == alien.finalY && alien.getX() == alien.finalX;
        }
    }

    // Set the finalX and finalY of each alien, so it results in a grid formation regardless of the number of aliens
    private void alienFormation3(ArrayList<Alien> aliens) {
        System.out.println("Formation 3");
        int alienWidth = aliens.get(1).getImage().getWidth();
        int alienHeight = aliens.get(1).getImage().getHeight();
        int alienSpacing = alienWidth / 2;
        int alienX = alienWidth / 2;
        int alienY = alienHeight / 2;
        for (Alien alien : aliens) {
            alienX += alienWidth + alienSpacing;
            if (alienX > getWidth() - getWidth() / 4) {
                alienX = alienWidth / 2 + getWidth() / 4;
                alienY += alienHeight + alienSpacing;
            }
            alien.finalX = alienX;
            alien.finalY = alienY;
        }
    }

    //    A random formation of aliens
    private void alienFormation4() {
        for (int i = 0; i < 10; i++) {
            addObject(new Alien(), Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(400));
        }
    }
}
