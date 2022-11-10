import dataTypes.Score;
import dataTypes.Settings;

import java.sql.*;
import java.util.ArrayList;

/**
 * The DbService class is responsible for all database operations.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class DbService {
    /**
     * The DB URL to the db file on the local machine.
     */
    private static final String DB_URL = "jdbc:sqlite:db\\SpaceShooter.db";
    /**
     * Declaring the DB Driver.
     */
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    /**
     * Declaring the DB Connection.
     */
    private static Connection connection;

    /**
     * Starts the connection to the database.
     */
    public static void connect() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the database.
     */
    public static void close() throws SQLException {
        connection.close();
    }

    /**
     * Updates the settings in the database.
     *
     * @param settings The settings to be updated.
     * @return True if the update was successful, false otherwise.
     * @throws SQLException If the connection to the database is not established.
     */
    public static boolean updateSettings(Settings settings) throws SQLException {
        connect();
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
        close();
        return success;
    }

    /**
     * Toggles the volume in the database.
     *
     * @throws SQLException If the connection to the database is not established.
     */
    public static void toggleMute() throws SQLException {
        connect();
        Settings settings = getSettings();
        settings.volume = settings.volume == 0 ? 80 : 0;
        updateSettings(settings);
        close();
    }

    /**
     * Gets the settings from the database.
     *
     * @return The settings from the database.
     * @throws SQLException If the connection to the database is not established.
     */
    public static Settings getSettings() throws SQLException {
        connect();
        Settings settings = new Settings();
        String sql = "SELECT * FROM settings WHERE id = 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            settings.volume = resultSet.getInt("volume");
            settings.infinite = resultSet.getBoolean("infinite");
        }
        close();
        return settings;
    }

    /**
     * Updates the score in the database.
     *
     * @param score The score to be updated.
     * @return True if the update was successful, false otherwise.
     * @throws SQLException If the connection to the database is not established.
     */
    public static boolean addScore(String playerName, int score) throws SQLException {
        connect();
        boolean success;
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='scoreboard'";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        boolean tableExists = resultSet.next();
        if (!tableExists) {
            sql = "CREATE TABLE scoreboard (id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, player_name TEXT, score INTEGER)";
            connection.prepareStatement(sql).executeUpdate();
        }
        success = connection.prepareStatement("INSERT INTO scoreboard (player_name, score) VALUES('" + playerName + "','" + score + "')").execute();
        close();
        return success;
    }

    /**
     * Gets the scores from the database.
     *
     * @return The scores from the database.
     * @throws SQLException If the connection to the database is not established.
     */
    public static ArrayList<Score> getScores() throws SQLException {
        connect();
        String sql = "SELECT * FROM scoreboard";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        ArrayList<Score> scores = new ArrayList<>();
        while (resultSet.next()) {
            scores.add(new Score(resultSet.getString("player_name"), resultSet.getInt("score"), resultSet.getInt("id")));
        }
        close();
        return scores;
    }
}
