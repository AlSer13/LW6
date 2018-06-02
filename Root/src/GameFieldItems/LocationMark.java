package GameFieldItems;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;

import java.util.HashSet;

public class LocationMark extends TilePane {
    public int id;
    private String name;
    int limit;
    private Label label;
    public HashSet<Unit> charPool = new HashSet<>();

    public LocationMark(int limit, int id) {
        this.setPrefSize(140, 110);
        this.getStyleClass().add("location-mark");
        this.limit = limit;
        this.label = new Label();
        this.getChildren().add(label);
        this.id = id;
    }

    public LocationMark(double X, double Y, int limit, String name, int id) {
        this(limit, id);
        this.setLayoutX(X);
        this.setLayoutY(Y);
        this.name = name;
        setLabel();
    }

    private void setLabel() {
        if (name == null || name.equals(""))
            name = "Unnamed area";
        this.label.setText(name);
        this.setAlignment(Pos.TOP_CENTER);
    }

}
