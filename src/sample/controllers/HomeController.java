package sample.controllers;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.models.CustomColorPicker;
import sample.models.PlayerModel;
import sample.models.ScoreModel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**Handles main menu*/
public class HomeController {
    private final PlayerModel playerModel;
    private HashMap<String, Integer> scores;
    private DatabaseController databaseController = new DatabaseController();

    @FXML
    private javafx.scene.layout.AnchorPane AnchorPane;
    @FXML
    private TextField input;
    @FXML
    private Button btn;
    @FXML
    private Label name;
    @FXML
    private Pane main_menu;
    @FXML
    private Pane highscore;
    @FXML
    private CustomColorPicker colorPicker;
    @FXML
    private Label error;
    @FXML
    private Pane name_menu;
    @FXML
    private TableView scoreTable;
    @FXML
    private TableColumn<ScoreModel, String> colName;
    @FXML
    private TableColumn<ScoreModel, Integer> colPoints;

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

        colName.setCellValueFactory(new PropertyValueFactory<ScoreModel, String>("name"));
        colName.setResizable(false);
        colPoints.setCellValueFactory(new PropertyValueFactory<ScoreModel, Integer>("points"));
        colPoints.setResizable(false);

        new Thread(() -> {
            ObservableList<ScoreModel> list = databaseController.getScores();
            scoreTable.setItems(list);
        }).start();
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
    }

    @FXML
    private void showColorPicker() {
        colorPicker.setVisible(true);
        main_menu.setVisible(false);
    }

    @FXML
    private void back(){
        main_menu.setVisible(true);
        name_menu.setVisible(false);
        highscore.setVisible(false);
        colorPicker.setVisible(false);
    }

    /**Handles play button - switches fxml and controllers, passes playerModel to GameController.*/
    @FXML
    private void play(Event event) throws IOException {
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
