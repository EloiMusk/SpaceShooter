import greenfoot.World;

public class Menu extends World {
    public Menu() {
        super(800, 600, 1);
    }
    public Menu(GameState gameState) {
        super(800, 600, 1);
        switch (gameState) {
            case GAME_OVER:
                showText("Game Over", 400, 300);
                showText("Your score: " + Space.score, 400, 350);
                showText("Press Enter to restart", 400, 400);
                break;
            case GAME_WON:
                break;
            case GAME_PAUSED:
                break;
            case GAME_RUNNING:
                break;
            case GAME_START:
                break;
        }
    }
}
