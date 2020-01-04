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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.models.PlayerModel;
import sample.models.TimelineObserver;
import sample.models.TimelinesObservator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * Handles all bird actions, detection etc.
 * Contains variables responsible for game and bird movement.
 *      Bird gravity equation, smooth jumping:
 *      dY += gravity + flapping
 *      y += dY
 *
 * Additionally creates background tasks for these operations.
 * */
public class GameController{
    private final PlayerModel playerModel;
    private int points = 0;
    private boolean run = false;
    private final double gravity = 0.1;  // constant downward accelartion
    private final double flapping = -4;  // uppward acceleration
    private boolean isFlapping = false;
    private double dY = 0; // verticale speed
    private double pipes_distance = 200;
    private ArrayList<ArrayList<ImageView>> pipes = new ArrayList<ArrayList<ImageView>>();
    private DatabaseController databaseController = new DatabaseController();
    private Image[] birdImages;
    private double[] streakArray = new double[10];
    private ArrayList<Circle> circles = new ArrayList<>();

    private Timeline rewind;
    private Timeline jumping;
    private Timeline collision;
    private Timeline pointsTimeline;
    private Timeline birdChange;

    private TimelinesObservator observator = new TimelinesObservator();

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
        birdImages = new Image[3];

        birdImages[0] = new Image("sample/assets/yellowbird-downflap.png");
        birdImages[1] = new Image("sample/assets/yellowbird-midflap.png");
        birdImages[2] = new Image("sample/assets/yellowbird-upflap.png");

        for (double streak : streakArray) {
            streak = 300;
        }

    }

    private void initiliaze_pipes(){
        for(int i=0; i<3;i++){
            for (ImageView pipe : pipes.get(i)) {
                pipe.setX(360+(pipes_distance*i));
            }
        }
    }

    /**Initialization of bird position, pipes' and bird's images. */
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
            pipes.add(pipes_pair);
            shuffle_pipes(pipes_pair);
            AnchorPane.getChildren().addAll(pipes_pair);
        }
        initiliaze_pipes();

        point_field.setViewOrder(-100);

        // z-order and set label's value
        name.setText(playerModel.getPlayer_name());
        name.setViewOrder(-100);

        lost.setViewOrder(-100);

        Circle circle = new Circle();
        circle.setCenterX(bird.getY());
        circle.setCenterX(30.0);
        circle.setRadius(1);
        circle.setFill(Color.RED);
        circle.setViewOrder(-100);

        AnchorPane.getChildren().add(circle);
    }

    /**Creates rewind timeline - moves pipes and loops them over,
     * array of 3 pipes which are reseted over whenever one reaches x=-50
     *
     * Additionally creates jumping timeline which handles gravitaion
     * and bird's acceleration.*/
    @FXML
    private void startGame(){
        if(rewind == null) {
            rewind = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    pipes.forEach(pair -> {
                        // move pipes
                        pair.forEach(pipe -> pipe.setX(pipe.getX() - 1));

                        if (pair.get(0).getX() < -50) {
                            shuffle_pipes(pair);
                            pair.forEach(pipe -> pipe.setX(550));
                            points++;
                        }
                    });
                }
            }));
            rewind.setCycleCount(Timeline.INDEFINITE);
            observator.attach(new TimelineObserver(observator, rewind));

        }

        /*Jumping timeline - responsible for gravity force and flapping upward */
        if(jumping == null) {
            jumping = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
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
            jumping.setCycleCount(Timeline.INDEFINITE);
            observator.attach(new TimelineObserver(observator, jumping));
        }
    }

    /**Collision timeline - checks if TimelinesObservator touched any of pipes, if he did
     * timeline stops rest of timelines which is basically stopping whole game and
     * creates net Thread to insert user's score to db.*/
    private void collisionCheck(){
        if(collision == null) {
            collision = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (bird.getY() <= 0 || bird.getY() >= 640) {
                        if (run) {
                            rewind.stop();
                            jumping.stop();
                            pointsTimeline.stop();
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
                            if (run) {
                                observator.setRunning(false);
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
            collision.setCycleCount(Timeline.INDEFINITE);
            observator.attach(new TimelineObserver(observator, collision));
        }
    }

    private void timelinesStop() {
        rewind.stop();
        jumping.stop();
        pointsTimeline.stop();
        birdChange.stop();
    }

    /**TimelineObserver to increment user's points - whenever pipe is reseted point is added.*/
    private void pointsCheck(){
        if(pointsTimeline == null) {
            pointsTimeline = new Timeline(new KeyFrame(Duration.seconds(0.01), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    point_field.setText(String.valueOf(points));
                }
            }));
            pointsTimeline.setCycleCount(Timeline.INDEFINITE);
            observator.attach(new TimelineObserver(observator, pointsTimeline));
        }
    }

    /**TimelineObserver handles bird's image change.*/
    private void changeBird(){
        if (birdChange == null) {
            birdChange = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
                int i = 0;

                @Override
                public void handle(ActionEvent actionEvent) {
                    bird.setImage(birdImages[i]);

                    i = (i + 1) % birdImages.length;
                }
            }));
            birdChange.setCycleCount(Timeline.INDEFINITE);
            observator.attach(new TimelineObserver(observator, birdChange));
        }
        
    }
    
    
    @FXML
    private void keyPressed(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.SPACE){
            // start / restart
            if(!run){
                restart();
                run = true;
            }
            else
                isFlapping = true;
        }
        else if(event.getCode() == KeyCode.ESCAPE){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/home.fxml"));

            loader.setController(new HomeController(playerModel));

            Parent root = (Parent)loader.load();
            Scene scene = new Scene(root, 360, 640);

            scene.getRoot().requestFocus();
            scene.getStylesheets().add("sample/styles.css");
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            stage.setScene(scene);
        }
    }

    /** Responsible for starting/restarting game
     * Creates all the timelines responsible for:
     *      Collision Detection
     *      Bird movement
     *      Pipes movement
     *      Points incrementing
     *      Bird image change
     * */
    private void restart(){
        initiliaze_pipes();
        bird.setY(320);
        dY = 0;
        startGame();
        collisionCheck();
        pointsCheck();
        changeBird();
        observator.setRunning(true);
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
