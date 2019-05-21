package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerNameController {

    @FXML
    public TextField input;
    @FXML
    public Button btn;
    @FXML
    public Label name;
    @FXML
    public Group main_menu;
    @FXML
    private Pane name_menu;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public PlayerNameController() {
    }

    @FXML
    private void initialize(){

    }

    @FXML
    private void setLabelText(){
        name.setText(input.getText());
    }

    @FXML
    private void showPlayerName(){
        name_menu.setVisible(true);
        main_menu.setVisible(false);
    }

    @FXML
    private void back(){
        main_menu.setVisible(true);
        name_menu.setVisible(false);
    }
}
