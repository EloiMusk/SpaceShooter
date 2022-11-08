import greenfoot.*;
import greenfoot.Color;

public class Menu extends World {
    LeaderBoardComponent leaderboard;
    GameState gameState;

    public Menu() {
        super(800, 600, 1);
        this.gameState = GameState.GAME_START;
        init();
    }

    public Menu(GameState gameState) {
        super(800, 600, 1);
        this.gameState = gameState;
        init();
    }

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

    private void onClick() {
        Greenfoot.setWorld(new Space());
    }

    private void clearAll() {
        removeObjects(getObjects(null));
        getBackground().clear();
    }

    public void refresh() {
        leaderboard.refresh();
        clearAll();
        init();
    }

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

    public void act() {
        if (Greenfoot.isKeyDown("enter")) {
            onClick();
        }
    }
}
