package sample.models;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomColorPicker extends Pane {
    @FXML private ColorPicker picker;
    @FXML private Button submit;
    @FXML private ImageView bird;
    @FXML private Button save;
    ArrayList<BufferedImage> birds = new ArrayList<>();
    String birdURL;

    public CustomColorPicker() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "../scenes/color_picker.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        birdURL = bird.getImage().getUrl().replace("file:", "");

        birds.add(ImageIO.read(new File(birdURL)));
        birds.add(ImageIO.read(new File(birdURL.replace("down", "mid"))));
        birds.add(ImageIO.read(new File(birdURL.replace("down", "up"))));

        picker.setLayoutX(77);
        picker.setLayoutY(250);
        submit.setLayoutX(140);
        submit.setLayoutY(290);
        submit.setText("Submit");
        bird.setLayoutX(77);
        bird.setLayoutY(290);
    }

    @FXML
    private void changeColor() throws IOException {
        System.out.println(picker.getValue());

        int rgb = getRGB(picker.getValue());
        for (BufferedImage image: birds) {
            for (int i = 0; i < 12; i++) {
                image.setRGB(20 + i, 14, rgb);
                image.setRGB(20 + i, 15, rgb);
            }

            for (int i = 0; i < 10; i++) {
                image.setRGB(20 + i, 18, rgb);
                image.setRGB(20 + i, 19, rgb);
            }
            for (int i = 18; i < 21; i++) {
                image.setRGB(i, 16, rgb);
                image.setRGB(i, 17, rgb);
            }
        }


        bird.setImage(SwingFXUtils.toFXImage(birds.get(0), null));

    }

    private int getRGB(javafx.scene.paint.Color value) {
       return  ((0xFF) << 24) |
                (((int)(value.getRed()*255) & 0xFF) << 16) |
                (((int)(value.getGreen()*255) & 0xFF) << 8)  |
                (((int) (value.getBlue() * 255) & 0xFF));
    }

    @FXML
    private void saveBird() throws IOException {
        File down = new File(birdURL);
        File mid = new File(birdURL.replace("down", "mid"));
        File up = new File(birdURL.replace("down", "up"));

        ImageIO.write(birds.get(0), "png", down);
        ImageIO.write(birds.get(1), "png", mid);
        ImageIO.write(birds.get(2), "png", up);
    }
}
