package sample.models;

/**Used in main menu to show highest scores of all players*/
public class ScoreModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    private String name;
    private int points;

    public ScoreModel(String name, int points) {
        this.name = name;
        this.points = points;
    }
}
