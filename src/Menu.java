import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.World;

import java.awt.*;
import java.util.function.Function;

public class Menu extends World {
    Object restart = new Object();

    public Menu() {
        super(800, 600, 1);
    }

    public Menu(GameState gameState) {
        super(800, 600, 1);
        switch (gameState) {
            case GAME_OVER:
                int x = ((getWidth() / 6 * 5)) / 3;
                showText("Game Over", x, 200);
                showText("Your score: " + Space.score, x, 250);
                showText("Press Enter to restart", x, 300);
                Button restartButton = new Button("Restart", Color.BLUE, Color.GRAY, this::onClick);
                addObject(restartButton, x, 350);
                addObject(new LeaderBoardComponent(getWidth() / 3, getHeight() / 100 * 80), (getWidth() / 6) * 5, getHeight() / 2);
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

    private void onClick() {
        Greenfoot.setWorld(new Space());
    }

    public void act() {
        if (Greenfoot.isKeyDown("enter")) {
            onClick();
        }
    }
}
