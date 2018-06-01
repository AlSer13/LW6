package Graphics;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Unit extends ImageView implements Comparable<Unit>, Serializable {
    private transient ImageView icon;
    transient GridPane menuIcon;
    private String username;
    private boolean kicked;
    transient GameField.LocationMark currentLocation;
    private int locIdVal;
    private int charIdVal;
    transient private MyIntProperty locId = new MyIntProperty(0);
    transient private MyIntProperty charId = new MyIntProperty(0);
    transient private SimpleStringProperty locName = new SimpleStringProperty();
    transient private SimpleStringProperty charName = new SimpleStringProperty();
    transient private SimpleBooleanProperty removed = new SimpleBooleanProperty(false);
    private boolean removedVal;
    public static ArrayList<String> LocNames = new ArrayList<>(Arrays.asList(
            "Not specified",
            "Left Start 2",
            "Left Start 3",
            "Left Start 4",
            "Bottom Start 1",
            "Bottom Start 2",
            "Bottom Start 3",
            "Bottom Start 4",
            "Desert",
            "Beach",
            "Island",
            "B-Tree",
            "Away",
            "Christopher's home",
            "Forest",
            "Left Start 1"
    ));

    public static ArrayList<String> CharNames = new ArrayList<>(Arrays.asList(
            "Not Specified",
            "Owl",
            "Tigger",
            "Eeyore",
            "Piglet",
            "Kanga",
            "Winnie-the-Pooh",
            "Little Roo",
            "Rabbit"
    ));

    public Unit(String username, int charId) {
        this(username);
        assignCharacter(charId);
        assignMenuIcon();
    }

    Unit(String username) {
        this.username = username;
    }

    void assignCharacter(int charId) {
        setCharId(charId);
        this.setImage(new Image(getClass().getClassLoader().getResourceAsStream("Graphics/imgs/Faces/Faces-0" + charId + ".png")));
        if (this.icon!=null) {
            this.icon.setImage(this.getImage());
        }
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

    void restore() {
        this.setLocId(locIdVal);
        this.setCharId(charIdVal);
        this.setImage(new Image(new File("Graphics.imgs.Faces\\Graphics.imgs.Faces-0" + getCharId() + ".png").toURI().toString()));
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
                setLocId(currentLocation.id);
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
                setLocId(0);
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



    public MyIntProperty locIdProperty() {
        return locId;
    }

    public int getLocId() {
        if (locId!=null)
        return locId.get();
        else
        {
            setLocId(locIdVal);
        }
        return locIdVal;
    }

    public void setLocId(int lid) {
        if (locId != null) {
            this.locId.set(lid);
            locIdVal = lid;
        } else {
            locId = new MyIntProperty(lid);
        }
    }



    public MyIntProperty charIdProperty() {
        return charId;
    }

    public int getCharId() {
        if (charId!=null)
            return charId.get();
        else
        {
            setCharId(charIdVal);
        }
        return charIdVal;
    }

    public void setCharId(int cid) {
        if (charId != null) {
            this.charId.set(cid);
            charIdVal = cid;
        } else {
            charId = new MyIntProperty(cid);
        }
    }



    public SimpleStringProperty locNameProperty(){
        setLocName(LocNames.get(getLocId()));
        return locName;
    }

    public String getLocName() {
        setLocName(LocNames.get(getLocId()));
        return locName.get();
    }

    public void setLocName(String lNm) {
        if (locName == null) locName = new SimpleStringProperty();
        locName.set(lNm);
    }



    public SimpleStringProperty charNameProperty() {
        setCharName(CharNames.get(getCharId()));
        return charName;
    }

    public String getCharName() {
        setCharName(CharNames.get(getCharId()));
        return charName.get();
    }

    public void setCharName(String cNm) {
        if (charName == null) charName = new SimpleStringProperty();
        charName.set(cNm);
    }

    public SimpleBooleanProperty removedProperty() {
        return removed;
    }

    public boolean isRemoved() {
        if (removed!=null)
            return removed.get();
        else
        {
            setRemoved(removedVal);
        }
        return removedVal;
    }


    public void setRemoved(boolean rmd) {
        if (removed != null) {
            this.removed.set(rmd);
            removedVal = rmd;
        } else {
            removed = new SimpleBooleanProperty(rmd);
        }
    }



    @Override
    public int compareTo(Unit u) {
        return (int) (u.getLayoutX() - this.getLayoutX());
    }

    public boolean isKicked() {
        return kicked;
    }

    public void setKicked(boolean kicked) {
        this.kicked = kicked;
    }


    public class MyIntProperty extends SimpleIntegerProperty implements Serializable {
        MyIntProperty(int initValue) {
            super(initValue);
        }

    }

}