package sample.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**Contains player's points and name.
 * Passed between two controllers while scene is changed.*/
public class PlayerModel {

    private final StringProperty player_name = new SimpleStringProperty("Anonymous");
    private int points;


    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;

    }

    public String getPlayer_name() {
        return player_name.get();
    }

    public StringProperty player_nameProperty() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name.set(player_name);
    }
}
