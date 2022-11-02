import dataTypes.Score;
import dataTypes.Settings;

import java.sql.*;
import java.util.ArrayList;

public class DbService {
    private static final String DB_URL = "jdbc:sqlite:src\\db\\SpaceShooter.db";
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static Connection connection;

    public static void connect() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() throws SQLException {
        connection.close();
    }

    public static boolean updateSettings(Settings settings) throws SQLException {
        boolean success;
//        Check if table settings exists with sqlite syntax
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='settings'";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        boolean tableExists = resultSet.next();
        if (!tableExists) {
//            Create table settings if it doesn't exist
            sql = "CREATE TABLE settings (id INTEGER UNIQUE, volume INTEGER, infinite BOOLEAN)";
            connection.prepareStatement(sql).executeUpdate();
        }
        try {
            success = connection.prepareStatement("INSERT INTO settings (id, volume, infinite) values (1," + settings.volume + "," + settings.infinite + ")").execute();
        } catch (Exception e) {
            success = connection.prepareStatement("UPDATE settings SET volume = " + settings.volume + ", infinite = " + settings.infinite + " WHERE id = 1").execute();
        }
        return success;
    }

    public static Settings getSettings() throws SQLException {
        Settings settings = new Settings();
        String sql = "SELECT * FROM settings WHERE id = 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            settings.volume = resultSet.getInt("volume");
            settings.infinite = resultSet.getBoolean("infinite");
        }
        return settings;
    }

    public static boolean addScore(String playerName, int score) throws SQLException {
        boolean success;
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='scoreboard'";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        boolean tableExists = resultSet.next();
        if (!tableExists) {
            sql = "CREATE TABLE scoreboard (id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, player_name TEXT, score INTEGER)";
            connection.prepareStatement(sql).executeUpdate();
        }
        success = connection.prepareStatement("INSERT INTO scoreboard (player_name, score) VALUES('" + playerName + "','" + score + "')").execute();
        return success;
    }

    public static ArrayList<Score> getScoreBoard() throws SQLException {
        String sql = "SELECT * FROM scoreboard";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        ArrayList<Score> scores = new ArrayList<>();
        while (resultSet.next()) {
            scores.add(new Score(resultSet.getString("player_name"), resultSet.getInt("score"), resultSet.getInt("id")));
        }
        return scores;
    }
}
