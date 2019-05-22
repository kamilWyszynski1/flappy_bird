package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.util.HashMap;

public class DatabaseController {
    /**
     * Connect to SQLITE database and perform inserting data
     * */



    private Connection connect(){
        String url = "jdbc:sqlite:highscore.sqlite";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    public void insert(String name, int points) {
        String sql = "INSERT INTO scores(name, points) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, points);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ObservableMap<String, Integer> getScores(){
        String sql = "SELECT name, points FROM scores ORDER BY points LIMIT 6";
        ObservableMap<String, Integer> scores = FXCollections.observableHashMap();

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                scores.put(rs.getString("name"), rs.getInt("points"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return scores;
    }
}

