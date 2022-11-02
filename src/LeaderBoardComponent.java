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
            img.setColor(Color.BLUE);
            GreenfootImage text = new GreenfootImage("Leaderboard", 30, Color.BLUE, new Color(0, 0, 0, 0));
            getImage().drawImage(text, img.getWidth() / 2 - text.getWidth() / 2, img.getHeight() / 100 * 15 - text.getHeight() / 2);
            getImage().drawString("#", img.getWidth() / 100 * 60 / 5, 100);
            getImage().drawString("Name", img.getWidth() / 100 * 20, 100);
            getImage().drawString("Score", img.getWidth() / 100 * 60, 100);
            for (Score score : scores) {
                if (i >= 5) {
                    break;
                }
                i++;
                getImage().drawString(String.valueOf(i), (img.getWidth() / 100 * 60) / 5, 80 + yPosition);
                getImage().drawString(score.playerName, ((img.getWidth() / 100 * 60) / 5) + 20, 80 + yPosition);
                getImage().drawString(String.valueOf(score.score), ((img.getWidth() / 100 * 80) / 5) + 100, 80 + yPosition);
                yPosition += 50;
            }
        }
    }
}
