package sample.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController{
    /**
     * Variables responsible for game and bird movement.
     * Bird gravity equation, smooth jumping:
     * dY += gravity + flapping
     * y += dY
     * */
    private int points = 0;
    private boolean run = false;
    private final double gravity = 1;  // constant downward accelartion
    private final double flapping = -10;  // uppward acceleration
    private boolean isFlapping = false;
    private int dY = 0; // verticale speed
    private double pipes_distance = 200;
    private ArrayList<ArrayList<ImageView>> pipes = new ArrayList<ArrayList<ImageView>>();

    /**Background tasks, pipes movement, bird position updating, collision check*/
    private Timeline rewind;
    private Timeline jumping;
    private Timeline colission;
    private Timeline points_timeline;

    private Scene scene;
    public Text point_field;
    public ImageView bird;

    @FXML
    public javafx.scene.layout.AnchorPane AnchorPane;
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public GameController() {
    }

    private void initiliaze_pipes(){
        for(int i=0; i<3;i++){
            for (ImageView pipe : pipes.get(i)) {
                pipe.setX(360+(pipes_distance*i));
            }
        }
    }

    @FXML
    private void initialize() {
        int bird_y = 300;
        bird.setY(bird_y);
        bird.setX(30);



        for(int i = 0; i < 3; i++) {
            ArrayList<ImageView> pipes_pair = new ArrayList<>();
            ImageView pipe = new ImageView(new Image("sample/assets/pipe-green.png"));
            ImageView pipe1 = new ImageView(new Image("sample/assets/pipe-green.png"));

            pipe1.setRotate(180.0);

            pipe.setFitHeight(500);
            pipe1.setFitHeight(500);

            // pair[0] - dolna
            pipes_pair.add(pipe);
            pipes_pair.add(pipe1);
            this.pipes.add(pipes_pair);
            shuffle_pipes(pipes_pair);
            AnchorPane.getChildren().addAll(pipes_pair);
        }
        initiliaze_pipes();

    }

    @FXML
    private void startGame(){
        // timeline rewinding pipes
        this.rewind = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pipes.forEach(pair -> {
                    // move pipes
                    pair.forEach(pipe -> pipe.setX(pipe.getX()-1));

                    if(pair.get(0).getX() < -50){
                        shuffle_pipes(pair);
                        pair.forEach(pipe -> pipe.setX(550));
                    }
                });
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

                pipes.forEach(pair -> pair.forEach(pipe -> {
                     if(pipe.getBoundsInParent().intersects(bird.getBoundsInParent())) {
                         initiliaze_pipes();
                     }
                }));

            }
        }));
        this.colission.setCycleCount(Timeline.INDEFINITE);
        this.colission.play();
    }

    private void pointsCheck(){
        this.points_timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                point_field.setText("0");
            }
        }));
    }

    @FXML
    private void keyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.SPACE){
            if(!this.run){
                startGame();
                collisionCheck();
                pointsCheck();
                run = true;
            }
            else
                isFlapping = true;
        }
    }

    private void shuffle_pipes(ArrayList<ImageView> pipes){
        Random generator = new Random();
        double h = generator.nextInt(300) + 100;
        double gap = generator.nextInt(100) + 90;

        pipes.get(0).setY(640-h);
        pipes.get(1).setY(640-500-h-gap);
    }
}
