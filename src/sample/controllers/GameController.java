package sample.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;

import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController{
    private boolean run = false;
    private final double gravity = 1;  // constant downward accelartion
    private final double flapping = -10;  // uppward acceleration
    private boolean isFlapping = false;
    private int dY = 0; // verticale speed

    private Timeline rewind;
    private Timeline jumping;
    private Timeline colission;

    private Scene scene;
    public ImageView bird;
    public ImageView down_pipe;
    public ImageView upper_pipe;

    @FXML
    public javafx.scene.layout.AnchorPane AnchorPane;
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public GameController() {
    }

    @FXML
    private void initialize() {
        int bird_y = 300;
        bird.setY(bird_y);


    }

    @FXML
    private void startGame(){
        // timeline rewinding pipes
        this.rewind = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                down_pipe.setX(down_pipe.getX()-1);
                upper_pipe.setX(upper_pipe.getX()-1);

            }
        }));
        this.rewind.setCycleCount(Timeline.INDEFINITE);
        this.rewind.play();

        // jumping
        this.jumping = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(isFlapping){
                    dY += gravity + flapping;
                    isFlapping = false;
                }
                else
                    dY += gravity;

                bird.setY(bird.getY()+dY);


            }
        }));
        this.jumping.setCycleCount(Timeline.INDEFINITE);
        this.jumping.play();
    }

    private void collisionCheck(){
        this.colission = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(bird.getY() <= 0 || bird.getY() >= 640) {
                    bird.setY(320);
                    rewind.stop();
                }
            }
        }));
        this.colission.setCycleCount(Timeline.INDEFINITE);
        this.colission.play();
    }

    @FXML
    private void keyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.SPACE){
            if(!this.run){
                startGame();
                collisionCheck();
                run = true;
            }
            else
                isFlapping = true;
        }
    }
}
