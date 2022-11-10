import dataTypes.Settings;
import greenfoot.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Space is the world in which the game takes place.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Space extends World {
    /**
     * The level of the game.
     *
     * @default 1
     */
    public static int level = 1;
    /**
     * Current value of the animationMilliSeconds counter.
     *
     * @maxValue 60
     */
    public static int animationMilliSeconds;
    /**
     * Current value of the animationSeconds counter.
     *
     * @maxValue 3600
     */
    public static int animationSeconds;
    /**
     * Current value of the animationMinutes counter.
     *
     * @maxValue 216000
     */
    public static int animationMinutes;
    /**
     * The score of the game.
     */
    public static int score = 0;
    /**
     * Toggle Button for the sound.
     */
    private Button toggleMute;
    /**
     * The current volume of the game.
     */
    public static int volume = 80;
    /**
     * Whether the game is muted or not.
     */
    private boolean muted = false;
    /**
     * Instance of the SoundService for the background music.
     */
    public final SoundService backgroundMusic = new SoundService();
    /**
     * The current value of the scroll progress.
     */
    private int backgroundScroll = 0;
    /**
     * Instance of GreenfootImage for the background.
     */
    private GreenfootImage currentBackground;

    /**
     * Constructor for objects of class Space.
     */
    public Space() {
        super(800, 600, 1, false);
        startGame();
    }

    /**
     * Calls all the methods to run the game.
     */
    public void act() {
        setPaintOrder(Button.class, UI.class, SpaceShip.class, Bullet.class, Alien.class, Upgrade.class, Ammunition.class, Fog.class, Star.class);
        refreshBackground();
        refreshGameStats();
        runAnimationTimer();
        generateAmmunition();
        generateUpgrade();
        alienFormation();
    }

    /**
     * Starts the game. And builds the HUD.
     */
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

    /**
     * Generates a random background image.
     */
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

    /**
     * Toggle the mute of the game.
     */
    private void toggleMute() {
        try {
            DbService.toggleMute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * AnimationMilliSeconds counter. Decreases every act.
     * <p>1s = 60 increments</p>
     */
    private void decrementAnimationMilliseconds() {
        if (animationMilliSeconds > 0) {
            animationMilliSeconds--;
        } else {
            animationMilliSeconds = 60;
        }
    }

    /**
     * AnimationSeconds counter. Decreases every act.
     * <p>1m = 60 increments</p>
     */
    private void decrementAnimationSeconds() {
        if (animationSeconds > 0) {
            animationSeconds--;
        } else {
            animationSeconds = 60 * 60;
        }
    }

    /**
     * AnimationMinutes counter. Decreases every act.
     * <p>1h = 60 increments</p>
     */
    private void decrementAnimationMinutes() {
        if (animationMinutes > 0) {
            animationMinutes--;
        } else {
            animationMinutes = 60 * 60 * 60;
        }
    }

    /**
     * Runs the animation timers.
     */
    private void runAnimationTimer() {
        decrementAnimationMilliseconds();
        decrementAnimationSeconds();
        decrementAnimationMinutes();
    }

    /**
     * Adds the scored points to the score.
     *
     * @param points The points to add.
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Generates a grid of aliens.
     */
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

    /**
     * Generates a random background with stars and fog.
     */
    private void generateBackground() {
        setPaintOrder(Button.class, UI.class, SpaceShip.class, Bullet.class, Alien.class, Upgrade.class, Fog.class, Star.class);
        for (int i = 0; i < 20; i++) {
            addObject(new Star(), Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
            addObject(new Fog(Greenfoot.getRandomNumber(800) + 1), Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
        }
    }

    /**
     * Refreshes the background image to create a scrolling effect.
     */
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

    /**
     * Plays a random background music.
     */
    private void playRandomBackgroundMusic() {
        String file = "Music/" + (Greenfoot.getRandomNumber(10) + 1) + ".mp3";
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stopSound();
        }
        backgroundMusic.playSound(file);
    }

    /**
     * Called when the level is completed. Creates a transition to the next level with the fog.
     */
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

    /**
     * Generates random upgrades.
     */
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

    /**
     * Called when the player dies. Plays a sound and changes back to the Menu.
     */
    public void gameOver() {
        backgroundMusic.stopSound();
        new SoundService().playSound("GameOver/1.wav");
        Greenfoot.setWorld(new Menu(GameState.GAME_OVER));
    }

    /**
     * Refreshes the HUD with the current values.
     */
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

    /**
     * Generates random ammunition.
     */
    public void generateAmmunition() {
        if (Greenfoot.getRandomNumber(200) < level) {
            addObject(new Ammunition(), Greenfoot.getRandomNumber(800), 0);
        }
    }

    /**
     * Rearranges the aliens in a new formation.
     */
    private void alienFormation() {
        if (animationSeconds % 60 == 0) {
            ArrayList<Alien> aliens = (ArrayList<Alien>) getObjects(Alien.class);
            if (Greenfoot.getRandomNumber(100) < 30) {
                alienFormation2(aliens);
            } else if (Greenfoot.getRandomNumber(100) < 60) {
                alienFormation1(aliens);
            } else if (Greenfoot.getRandomNumber(100) < 100 && aliens.size() > 0) {
                alienFormation3(aliens);
            }
        }
    }

    /**
     * Generates a V formation.
     *
     * @param aliens The aliens to arrange.
     */
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

    /**
     * Zig Zag like formation over the full width of the screen
     *
     * @param aliens The aliens to arrange.
     */
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

    /**
     * Generates a Grid formation.
     *
     * @param aliens The aliens to arrange.
     */
    private void alienFormation3(ArrayList<Alien> aliens) {
        int alienWidth = aliens.get(0).getImage().getWidth();
        int alienHeight = aliens.get(0).getImage().getHeight();
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
}
