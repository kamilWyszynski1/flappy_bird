package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application  {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/home.fxml"));
        Parent root = (Parent)loader.load();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 360, 640);
        scene.getRoot().requestFocus();

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
