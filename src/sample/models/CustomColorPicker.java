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
import java.net.URL;

public class CustomColorPicker extends Pane {
    @FXML private ColorPicker picker;
    @FXML private Button submit;
    @FXML private ImageView bird;
    @FXML private Button save;
    BufferedImage image;
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
        image = ImageIO.read(new File(bird.getImage().getUrl().replace("file:", "")));

        picker.setLayoutX(77);
        picker.setLayoutY(250);
        submit.setLayoutX(140);
        submit.setLayoutY(290);
        submit.setText("Submit");
        bird.setLayoutX(77);
        bird.setLayoutY(290);
        birdURL = bird.getImage().getUrl().replace("file:", "");

    }

    @FXML
    private void changeColor() throws IOException {
        System.out.println(picker.getValue());

        int rgb = getRGB(picker.getValue());
        System.out.println(rgb);
        for (int i = 0; i < 12; i++) {
            image.setRGB(21 + i, 14, rgb);
            image.setRGB(21 + i, 15, rgb);
        }

        for (int i = 0; i < 10; i++) {
            image.setRGB(21 + i, 18, rgb);
            image.setRGB(21 + i, 19, rgb);
        }
        for (int i = 19; i < 22; i++) {
            image.setRGB(i, 16, rgb);
            image.setRGB(i, 17, rgb);
        }

        bird.setImage(SwingFXUtils.toFXImage(image, null));


    }

    private int getRGB(javafx.scene.paint.Color value) {
        int rgb = (int)(value.getRed() * 255);
        rgb = (rgb << 8) + (int)(value.getGreen()*255);
        rgb = (rgb << 8) + (int)(value.getBlue()*255);
        return rgb;
    }

    @FXML
    private void saveBird() throws IOException {
        File outputfile = new File(birdURL);
        ImageIO.write(image, "png", outputfile);
    }
}
