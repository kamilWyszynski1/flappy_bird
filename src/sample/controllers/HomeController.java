package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.models.PlayerModel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HomeController {
    private final PlayerModel playerModel;
    private ObservableMap<String, Integer> scores;
    private DatabaseController databaseController = new DatabaseController();

    public javafx.scene.layout.AnchorPane AnchorPane;
    @FXML
    private TextField input;
    @FXML
    private Button btn;
    @FXML
    private Label name;
    @FXML
    private Group main_menu;
    @FXML
    private Pane highscore;
    @FXML
    private Label error;
    @FXML
    private Pane name_menu;
    private TableView grid_score;
    private TableColumn name_column;
    private TableColumn point_column;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public HomeController(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    @FXML
    private void initialize(){
        this.name.setText(this.playerModel.getPlayer_name());

        new Thread(){
            public void run(){
                scores = databaseController.getScores();
            }
        }.start();
    }

    @FXML
    private void setLabelText(){
        if(input.getText().trim().isEmpty()){
            error.setText("Name can't be empty!");
        }
        else if(input.getText().trim().length() >= 20)
            error.setText("Name too long!");
        else {
            name.setText(input.getText());
            this.playerModel.setPlayer_name(input.getText());
            error.setText("");
        }
    }

    @FXML
    private void showPlayerName(){
        name_menu.setVisible(true);
        main_menu.setVisible(false);
    }

    @FXML
    private void showHighscore(){
        highscore.setVisible(true);
        main_menu.setVisible(false);
        System.out.println(scores);

        grid_score.setItems((ObservableList) scores);

    }

    @FXML
    private void back(){
        main_menu.setVisible(true);
        name_menu.setVisible(false);
        highscore.setVisible(false);
    }

    @FXML
    private void play(Event event) throws IOException {
        /**
         * Handler for Play button - switches fxml and controllers, passes playerModel to another controller.
         * */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/game.fxml"));

        loader.setController(new GameController(this.playerModel));

        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root, 360, 640);

        scene.getRoot().requestFocus();
        scene.getStylesheets().add("sample/styles.css");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);
    }

}
