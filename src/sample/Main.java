package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.HomeController;
import sample.models.NeuralNetwork;
import sample.models.PlayerModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Main extends Application  {

    private void initialize() {
        String url = "jdbc:sqlite:highscore.sqlite";
        String sql = "CREATE TABLE IF NOT EXISTS scores (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + " points integer DEFAULT 0"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        PlayerModel playerModel = new PlayerModel();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/home.fxml"));
        loader.setController(new HomeController(playerModel));

        Parent root = (Parent) loader.load();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 360, 640);
        scene.getRoot().requestFocus();

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        initialize();

        NeuralNetwork neuralNetwork = new NeuralNetwork(2,6);
        Vector<Double> inputs = new Vector<>();
        inputs.add(2.0);
        inputs.add(4.0);
        System.out.println(neuralNetwork.predict(inputs));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
