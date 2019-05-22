package sample.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.models.PlayerModel;

import java.io.IOException;
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
    private final PlayerModel playerModel;
    private int points = 0;
    private boolean run = false;
    private final double gravity = 1;  // constant downward accelartion
    private final double flapping = -10;  // uppward acceleration
    private boolean isFlapping = false;
    private int dY = 0; // verticale speed
    private double pipes_distance = 200;
    private ArrayList<ArrayList<ImageView>> pipes = new ArrayList<ArrayList<ImageView>>();
    private DatabaseController databaseController = new DatabaseController();

    /**Background tasks, pipes movement, bird position updating, collision check*/
    private Timeline rewind;
    private Timeline jumping;
    private Timeline colission;
    private Timeline points_timeline;

    public Label name;
    private Scene scene;
    public ImageView bird;
    public Label point_field;
    public Label lost;

    @FXML
    public javafx.scene.layout.AnchorPane AnchorPane;
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public GameController(PlayerModel playerModel) {
        this.playerModel = playerModel;
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

        point_field.setViewOrder(-100);

        // z-order and set label's value
        name.setText(playerModel.getPlayer_name());
        name.setViewOrder(-100);

        lost.setViewOrder(-100);

    }

    @FXML
    private void startGame(){
        /** Rewind time line - moves pipes and loops it over,
         *  Array of 3 pipes which are reseted over whenever
         *  One reaches X = -50
         *
         *  Firstly, check if rewind was started, it would mean that player died and there's
         *  no point of creating timeline once again, just start it over.
         *  If timeline wasn't create - create one and start it.
         * */
        if(this.rewind == null){
            this.rewind = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    pipes.forEach(pair -> {
                        // move pipes
                        pair.forEach(pipe -> pipe.setX(pipe.getX()-1));

                        if(pair.get(0).getX() < -50){
                            shuffle_pipes(pair);
                            pair.forEach(pipe -> pipe.setX(550));
                            points++;
                        }
                    });
                }
            }));
            this.rewind.setCycleCount(Timeline.INDEFINITE);
            this.rewind.play();
        }
        else
            this.rewind.play();

        /*Jumping timeline - responsible for gravity force and flapping upward */
        if(this.jumping == null) {
            this.jumping = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (isFlapping) {
                        dY += gravity + flapping;
                        isFlapping = false;
                    } else
                        dY += gravity;

                    bird.setY(bird.getY() + dY);
                }
            }));
            this.jumping.setCycleCount(Timeline.INDEFINITE);
            this.jumping.play();
        }
        else
            this.jumping.play();
    }

    private void collisionCheck(){
        /*Collisiong timeline - checks if player touched any of pipes*/
        if(this.colission == null) {
            this.colission = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (bird.getY() <= 0 || bird.getY() >= 640) {
                        if(run) {
                            rewind.stop();
                            jumping.stop();
                            points_timeline.stop();
                            lost.setVisible(true);
                            run = false;

                            new Thread() {
                                public void run() {
                                    databaseController.insert(playerModel.getPlayer_name(), points);
                                }
                            }.start();
                        }
                    }

                    pipes.forEach(pair -> pair.forEach(pipe -> {
                        if (pipe.getBoundsInParent().intersects(bird.getBoundsInParent())) {
                            if(run) {
                                rewind.stop();
                                jumping.stop();
                                points_timeline.stop();
                                lost.setVisible(true);
                                run = false;

                                new Thread() {
                                    public void run() {
                                        databaseController.insert(playerModel.getPlayer_name(), points);
                                    }
                                }.start();
                            }
                        }
                    }));

                }
            }));
            this.colission.setCycleCount(Timeline.INDEFINITE);
            this.colission.play();
        }
        else
            this.colission.play();
    }

    private void pointsCheck(){
        if(this.points_timeline == null) {
            this.points_timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    point_field.setText(String.valueOf(points));
                }
            }));
            this.points_timeline.setCycleCount(Timeline.INDEFINITE);
            this.points_timeline.play();
        }
        else
            this.points_timeline.play();
    }

    @FXML
    private void keyPressed(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.SPACE){
            // start / restart
            if(!this.run){
                restart();
                run = true;
            }
            else
                isFlapping = true;
        }
        else if(event.getCode() == KeyCode.ESCAPE){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/home.fxml"));

            loader.setController(new HomeController(this.playerModel));

            Parent root = (Parent)loader.load();
            Scene scene = new Scene(root, 360, 640);

            scene.getRoot().requestFocus();
            scene.getStylesheets().add("sample/styles.css");
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            stage.setScene(scene);
        }
    }

    private void restart(){
        initiliaze_pipes();
        bird.setY(320);
        dY = 0;
        startGame();
        collisionCheck();
        pointsCheck();
        lost.setVisible(false);
        points = 0;
    }

    private void shuffle_pipes(ArrayList<ImageView> pipes){
        Random generator = new Random();
        double h = generator.nextInt(300) + 100;
        double gap = generator.nextInt(100) + 90;

        pipes.get(0).setY(640-h);
        pipes.get(1).setY(640-500-h-gap);
    }

}
