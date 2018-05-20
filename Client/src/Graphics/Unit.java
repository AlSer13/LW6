package Graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;

class Unit extends ImageView {
    ImageView icon;
    GridPane menuIcon;
    String username;
    GameField.LocationMark currentLocation;
    int charId;

    Unit(String username, int charId) {
        this.username = username;
        this.charId = charId;
        this.setImage(new Image(new File("Client\\src\\Graphics\\imgs\\Faces\\Faces-0" + charId + ".png").toURI().toString()));

        this.icon = new ImageView();
        icon.setImage(this.getImage());
        icon.setFitHeight(this.getImage().getHeight()/2);
        icon.setFitWidth(this.getImage().getWidth()/2);

        menuIcon = new GridPane();
        menuIcon.add(icon,0,0);
        menuIcon.setAlignment(Pos.CENTER);
        Label userLabel = new Label(username);
        menuIcon.setVgap(20.0);
        menuIcon.setPrefWidth(250);
        userLabel.setAlignment(Pos.BASELINE_RIGHT);
        userLabel.setStyle("-fx-font-family: Garamond; -fx-font-size: 30; -fx-font-weight: bold;");
        userLabel.setPadding(new Insets(0,10,0,0));
        userLabel.setId("user-label");
        menuIcon.add(userLabel,1,0, 2, 1);

        this.setFitHeight(this.getImage().getHeight()/5);
        this.setFitWidth(this.getImage().getWidth()/5);

    }

    public boolean moveTo(GameField.LocationMark location) {
        if (location.transfer(this)) {
            if (currentLocation!=null) {
                currentLocation.leave(this);
            }
            if (currentLocation!=location) {
                location.getChildren().add(this);
                currentLocation = location;
            }
            return true;
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING,"Can't process the movement: location full.");
            a.showAndWait();
            return false;
        }
    }

}