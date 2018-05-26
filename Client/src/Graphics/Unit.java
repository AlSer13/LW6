package Graphics;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class Unit extends ImageView implements Comparable<Unit>, Serializable {
    private transient ImageView icon;
    transient GridPane menuIcon;
    private String username;
    transient GameField.LocationMark currentLocation;
    int locId;
    private int charId;
    boolean removed;

    Unit(String username, int charId) {
        this(username);
        assignCharacter(charId);
        assignMenuIcon();
    }

    Unit(String username) {
        this.username = username;
    }

    void assignCharacter(int charId) {
        this.charId = charId;
        this.setImage(new Image(new File("Client\\src\\Graphics\\imgs\\Faces\\Faces-0" + charId + ".png").toURI().toString()));
        this.setFitHeight(this.getImage().getHeight() / 5);
        this.setFitWidth(this.getImage().getWidth() / 5);
    }

    void assignMenuIcon() {
        if (this.getImage() != null) {
            this.icon = new ImageView();
            icon.setImage(this.getImage());
            icon.setFitHeight(this.getImage().getHeight() / 2);
            icon.setFitWidth(this.getImage().getWidth() / 2);

            menuIcon = new GridPane();
            menuIcon.add(icon, 0, 0);
            menuIcon.setAlignment(Pos.CENTER);
            Label userLabel = new Label(username);
            menuIcon.setVgap(20.0);
            menuIcon.setPrefWidth(250);
            userLabel.setAlignment(Pos.BASELINE_RIGHT);
            userLabel.setStyle("-fx-font-family: Garamond; -fx-font-size: 30; -fx-font-weight: bold;");
            userLabel.setPadding(new Insets(0, 10, 0, 0));
            userLabel.setId("user-label");
            menuIcon.add(userLabel, 1, 0, 2, 1);
        } else System.out.println("No image assigned to " + username);
    }

    void restore(HashMap<Integer, GameField.LocationMark> locations) {
        this.setImage(new Image(new File("Client\\src\\Graphics\\imgs\\Faces\\Faces-0" + charId + ".png").toURI().toString()));
        this.icon = new ImageView();
        icon.setImage(this.getImage());
        icon.setFitHeight(this.getImage().getHeight() / 2);
        icon.setFitWidth(this.getImage().getWidth() / 2);

        menuIcon = new GridPane();
        menuIcon.add(icon, 0, 0);
        menuIcon.setAlignment(Pos.CENTER);
        Label userLabel = new Label(username);
        menuIcon.setVgap(20.0);
        menuIcon.setPrefWidth(250);
        userLabel.setAlignment(Pos.BASELINE_RIGHT);
        userLabel.setStyle("-fx-font-family: Garamond; -fx-font-size: 30; -fx-font-weight: bold;");
        userLabel.setPadding(new Insets(0, 10, 0, 0));
        userLabel.setId("user-label");
        menuIcon.add(userLabel, 1, 0, 2, 1);
        this.setFitHeight(this.getImage().getHeight() / 5);
        this.setFitWidth(this.getImage().getWidth() / 5);

    }

    boolean moveTo(GameField.LocationMark location) throws NullPointerException {
        if (location.charPool.size() < location.limit) {
            if (!location.equals(currentLocation)) {
                this.leave();
                currentLocation = location;
                location.charPool.add(this);
                Platform.runLater(() -> location.getChildren().add(this));
                locId = currentLocation.id;
                return true;
            }
        }
        return false;
    }

    void leave() {
        if (currentLocation != null) {
            if (currentLocation.charPool.contains(this)) {
                currentLocation.charPool.remove(this);
                GameField.LocationMark locationMark = currentLocation;
                Platform.runLater(() -> locationMark.getChildren().remove(this));
                currentLocation = null;
                locId = 0;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        return username != null ? username.equals(unit.username) : unit.username == null;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    public String getName() {
        return username;
    }

    @Override
    public int compareTo(Unit u) {
        return (int) (u.getLayoutX() - this.getLayoutX());
    }
}