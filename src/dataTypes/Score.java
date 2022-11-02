package dataTypes;

public class Score {
    public int id;
    public String playerName;
    public int score;

    public Score(String playerName, int score, int id) {
        this.playerName = playerName;
        this.score = score;
        this.id = id;
    }
}
