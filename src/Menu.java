import greenfoot.*;
import greenfoot.Color;

/**
 * World for the main menu.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Menu extends World {
    /**
     * The leaderboard component.
     */
    LeaderBoardComponent leaderboard;
    /**
     * The gameStart variable.
     */
    GameState gameState;

    /**
     * Constructor for the menu without a given game state.
     */
    public Menu() {
        super(800, 600, 1);
        this.gameState = GameState.GAME_START;
        init();
    }

    /**
     * Constructor for the menu with a given game state.
     *
     * @param gameState The game state.
     */
    public Menu(GameState gameState) {
        super(800, 600, 1);
        this.gameState = gameState;
        init();
    }

    /**
     * Checks if enter is pressed and starts the game.
     */
    public void act() {
        if (Greenfoot.isKeyDown("enter")) {
            onClick();
        }
    }

    /**
     * Initializes the menu and determines the game state and what to do.
     */
    private void init() {
        switch (gameState) {
            case GAME_OVER:
                generateGameOver();
                break;
            case GAME_WON:
                break;
            case GAME_PAUSED:
                break;
            case GAME_RUNNING:
                break;
            case GAME_START:
                generateGameStart();
                break;
        }
    }

    /**
     * Generates the game over menu.
     */
    private void generateGameOver() {
        GreenfootImage bg = getBackground();
        int x = bg.getWidth() / 2;
        GreenfootImage background = new GreenfootImage("Background/1.png");
        background.scale(bg.getWidth(), bg.getHeight());
        bg.drawImage(background, 0, 0);
        bg.setColor(new Color(255, 255, 255, 100));
        bg.fillRect(0, 0, bg.getWidth(), bg.getHeight());
        bg.setColor(Color.BLUE);
        bg.setFont(bg.getFont().deriveFont(20));
        GreenfootImage text = new GreenfootImage("Game Over", 25, Color.BLUE, new Color(0, 0, 0, 0));
        bg.drawImage(text, x - text.getWidth() / 2, 200);
        text = new GreenfootImage("Score: " + Space.score, 25, Color.BLUE, new Color(0, 0, 0, 0));
        bg.drawImage(text, x - text.getWidth() / 2, 250);
        addObject(new Button("Save", Color.BLUE, Color.GRAY, this::addScore), x + text.getWidth(), 260);
        text = new GreenfootImage("Press Enter to restart", 25, Color.BLUE, new Color(0, 0, 0, 0));
        bg.drawImage(text, x - text.getWidth() / 2, 300);
        Button restartButton = new Button("Restart", Color.BLUE, Color.GRAY, this::onClick);
        addObject(restartButton, x, 350);
        leaderboard = new LeaderBoardComponent(getWidth() / 3, getHeight() / 100 * 80);
        addObject(leaderboard, (getWidth() / 6) * 5, getHeight() / 2);
    }

    /**
     * Generates the game start menu.
     */
    private void generateGameStart() {
        GreenfootImage bg = getBackground();
        int x = bg.getWidth() / 2;
        GreenfootImage background = new GreenfootImage("Background/1.png");
        background.scale(bg.getWidth(), bg.getHeight());
        bg.drawImage(background, 0, 0);
        bg.setColor(new Color(255, 255, 255, 100));
        bg.fillRect(0, 0, bg.getWidth(), bg.getHeight());
        bg.setColor(Color.BLUE);
        bg.setFont(bg.getFont().deriveFont(20));
        GreenfootImage text = new GreenfootImage("Press Enter to start", 25, Color.BLUE, new Color(0, 0, 0, 0));
        bg.drawImage(text, x - text.getWidth() / 2, 300);
        Button startButton = new Button("Start", Color.BLUE, Color.GRAY, this::onClick);
        addObject(startButton, x, 350);
        leaderboard = new LeaderBoardComponent(getWidth() / 3, getHeight() / 100 * 80);
        addObject(leaderboard, (getWidth() / 6) * 5, getHeight() / 2);
    }

    /**
     * Refreshes the leaderboard.
     */
    public void refresh() {
        leaderboard.refresh();
        clearAll();
        init();
    }

    /**
     * Clears all objects from the world.
     */
    private void clearAll() {
        removeObjects(getObjects(null));
        getBackground().clear();
    }

    /**
     * Adds a score to the leaderboard.
     */
    private void addScore() {
        String name = Greenfoot.ask("Enter your name");
        if (name != null) {
            try {
                DbService.addScore(name, Space.score);
                refresh();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Click callback. Starts the game.
     */
    private void onClick() {
        Greenfoot.setWorld(new Space());
    }
}
