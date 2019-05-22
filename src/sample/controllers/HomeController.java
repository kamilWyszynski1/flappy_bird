package sample.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.models.PlayerModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController {
    private final PlayerModel playerModel;

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

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public HomeController(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    @FXML
    private void initialize(){
        error.setText("Chuje!");
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
    private void back(){
        main_menu.setVisible(true);
        name_menu.setVisible(false);
        highscore.setVisible(false);
    }

    @FXML
    private void play(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/game.fxml"));
//        AnchorPane.getChildren().setAll(
//                Collections.singleton(
//                        loader.load()
//                ));

        GameController controller = loader.getController();
        controller.setPlayer(name.getText());

        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root, 360, 640);

        scene.getRoot().requestFocus();
        scene.getStylesheets().add("sample/styles.css");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);
    }

}
