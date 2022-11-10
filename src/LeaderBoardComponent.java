import dataTypes.Score;
import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.ArrayList;

/**
 * UI component for displaying the top 10 scores.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class LeaderBoardComponent extends Actor {

    /**
     * Constructor for LeaderBoardComponent that uses the world bounds to size the component.
     *
     * @param world The world to display the component in.
     */
    public LeaderBoardComponent(World world) {
        init(world.getWidth() / 100 * 80, world.getHeight() / 100 * 80);
    }

    /**
     * Constructor for LeaderBoardComponent that uses the given width and height to size the component.
     *
     * @param width  The width of the component.
     * @param height The height of the component.
     */
    public LeaderBoardComponent(int width, int height) {
        init(width, height);
    }

    /**
     * Initialises the component.
     *
     * @param x The x position of the component.
     * @param y The y position of the component.
     */
    private void init(int x, int y) {
//        Set the image of the border
        GreenfootImage image = new GreenfootImage("UI/border/1.png");
        image.scale(x, y);
        setImage(image);
        ArrayList<Score> scores = null;
//        Try to get the scores from the db
        try {
            scores = DbService.getScores();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
//        If there are scores, display them
        if (scores != null) {
            scores.sort((a, b) -> b.score - a.score);
            int yPosition = 50;
            int i = 0;
            GreenfootImage img = getImage();
            img.setFont(img.getFont().deriveFont(20));
            img.setColor(new Color(20, 0, 100));
            int xPos = img.getWidth() / 100;
            GreenfootImage text = new GreenfootImage("Leaderboard", 30, Color.BLUE, new Color(0, 0, 0, 0));
            getImage().drawImage(text, img.getWidth() / 2 - text.getWidth() / 2, (img.getHeight() / 100 * 15) - text.getHeight() / 2);
            getImage().drawString("#", xPos * 20, 100);
            getImage().drawString("Name", xPos * 40, 100);
            getImage().drawString("Score", xPos * 80, 100);
            for (Score score : scores) {
                if (i >= 5) {
                    break;
                }
                i++;
                getImage().drawString(String.valueOf(i), xPos * 20, 80 + yPosition);
                getImage().drawString(score.playerName, xPos * 40, 80 + yPosition);
                getImage().drawString(String.valueOf(score.score), xPos * 80, 80 + yPosition);
                yPosition += 50;
            }
        }
    }

    /**
     * refreshes the leaderboard with the given scores.
     */
    public void refresh() {
        int width = getImage().getWidth();
        int height = getImage().getHeight();
        getImage().clear();
        init(width, height);
    }
}
