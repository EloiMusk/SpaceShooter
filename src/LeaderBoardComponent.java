import dataTypes.Score;
import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.ArrayList;

public class LeaderBoardComponent extends Actor {

    public LeaderBoardComponent(World world) {
        init(world.getWidth() / 100 * 80, world.getHeight() / 100 * 80);
    }

    public LeaderBoardComponent(int width, int height) {
        init(width, height);
    }

    public void refresh() {
        int width = getImage().getWidth();
        int height = getImage().getHeight();
        getImage().clear();
        init(width, height);
    }

    private void init(int x, int y) {
        GreenfootImage image = new GreenfootImage("UI/border/1.png");
        image.scale(x, y);
        setImage(image);
        ArrayList<Score> scores = null;
        try {
            scores = DbService.getScores();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
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
}
