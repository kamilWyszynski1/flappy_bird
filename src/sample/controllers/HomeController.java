package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class HomeController {

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

    public HomeController() {
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
    private void play() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/game.fxml"));
        AnchorPane.getChildren().setAll(
                Collections.singleton(
                        loader.load()
                ));

    }

}