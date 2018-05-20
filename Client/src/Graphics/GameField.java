package Graphics;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class GameField extends Application {

    Image map = new Image(new File("Client\\src\\Graphics\\imgs\\Map.png").toURI().toString());
    double mapWidth = map.getWidth() / 4;
    double mapHeigth = map.getHeight() / 4;
    int limit = 8;
    Unit mainUser;
    HashSet<Unit> unitSet = new HashSet<>();

    public GameField(String username, int mainCharId, Stage stage) throws Exception {
        mainUser = new Unit(username, mainCharId);
        start(stage);
    }

    class LocationMark extends TilePane {
        String name;
        int limit;
        Label label;
        private ArrayList<Unit> charPool = new ArrayList<>();

        LocationMark(int limit) {
            this.setPrefSize(140,110);
            this.getStyleClass().add("location-mark");
            this.limit = limit;
            this.label = new Label();
            this.getChildren().add(label);
            this.setOnMouseClicked(s -> mainUser.moveTo(this));
        }

        LocationMark(double X, double Y, int limit) {
            this(limit);
            this.setLayoutX(X);
            this.setLayoutY(Y);
        }

        LocationMark(double X, double Y, int limit, String name) {
            this(X, Y, limit);
            this.name = name;
            setLabel();
        }

        void setLabel() {
            if (name == null || name.equals(""))
                name = "Unnamed area";
            this.label.setText(name);
            this.setAlignment(Pos.TOP_CENTER);
        }

        boolean leave(Unit unit) {
            if (charPool.contains(unit)){
                charPool.remove(unit);
                return true;
            }
            return false;

        }

        boolean transfer(Unit unit) {
            if (charPool.size()<limit) {
                charPool.add(unit);
                return true;
            }
            return isHere(unit);

        }

        boolean isHere(Unit unit) {
            return charPool.contains(unit);
        }

    }

    public void initMenu(HBox hbox) {

        MenuBar mainMenu = new MenuBar();
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open File");
        MenuItem exitApp = new MenuItem("Exit");
        file.getItems().addAll(openFile, exitApp);


        Menu edit = new Menu("Edit");
        MenuItem properties = new MenuItem("Properties");
        edit.getItems().add(properties);


        Menu help = new Menu("Help");
        MenuItem visitWebsite = new MenuItem("Visit Website");
        help.getItems().add(visitWebsite);

        mainMenu.getMenus().addAll(file, edit, help);


        MenuBar leftClose = new MenuBar();

        Menu close = new Menu("☓");
        MenuItem dummy = new MenuItem("");
        close.getItems().add(dummy);
        close.setId("close");

        close.setOnShowing(e -> System.exit(0));
        leftClose.getMenus().add(close);

        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");

        HBox.setHgrow(spacer, Priority.SOMETIMES);
        hbox.getChildren().addAll(mainMenu, spacer, leftClose);


    }

    public void setupLocationGenerator(Pane mapPane) {
        mapPane.setOnMouseClicked(e -> {

            LocationMark lm = new LocationMark(e.getX(), e.getY(), 1);
            mapPane.getChildren().add(lm);
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText("Enter the location name");
            TextField textField = new TextField();
            dialog.setGraphic(textField);
            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    lm.name = textField.getText();
                    lm.setLabel();
                    System.out.println("LocationMark " + lm.name + " = new LocationMark(" + e.getX() + ", " + e.getY() + ", \"" + lm.name + "\");");
                } else {
                    mapPane.getChildren().remove(lm);
                }
            });

        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane pane = new BorderPane();
        primaryStage.setResizable(true);

        //start
        //left start
        VBox leftStart = new VBox();

        LocationMark LStart1 = new LocationMark(1);
        LocationMark LStart2 = new LocationMark(1);
        LocationMark LStart3 = new LocationMark(1);
        LocationMark LStart4 = new LocationMark(1);

        leftStart.getChildren().addAll(LStart1, LStart2, LStart3, LStart4);
        leftStart.getChildren().forEach(e -> e.getStyleClass().add("left-start-pos"));


        //bottom start
        HBox botStart = new HBox();

        LocationMark BStart1 = new LocationMark(1);
        LocationMark BStart2 = new LocationMark(1);
        LocationMark BStart3 = new LocationMark(1);
        LocationMark BStart4 = new LocationMark(1);

        botStart.getChildren().addAll(BStart1, BStart2, BStart3, BStart4);
        botStart.getChildren().forEach(e -> e.getStyleClass().add("bot-start-pos"));

        //starts all together
        ArrayList<LocationMark> starts = new ArrayList<>();
        starts.addAll(Arrays.asList(LStart1, LStart2, LStart3, LStart4, BStart1, BStart2, BStart3, BStart4));

        //map pane
        AnchorPane mapPane = new AnchorPane();

        mapPane.setShape(new Rectangle(mapWidth, mapHeigth));
        mapPane.setMinSize(mapWidth, mapHeigth);
        mapPane.setMaxSize(mapWidth, mapHeigth);

        mapPane.getChildren().add(leftStart);
        mapPane.getChildren().add(botStart);

        leftStart.setSpacing(50.0);
        botStart.setSpacing(50.0);

        //50*6+141*4

        AnchorPane.setLeftAnchor(leftStart, 50.0);
        AnchorPane.setTopAnchor(leftStart, 70.0);
        AnchorPane.setBottomAnchor(botStart,50.0);
        AnchorPane.setRightAnchor(botStart, 100.0);

        mapPane.setId("map");
        pane.setCenter(mapPane);

        //places on map



        //top,bot

        HBox topPanel = new HBox();
        topPanel.setAlignment(Pos.TOP_LEFT);
        topPanel.setSpacing(0);
        topPanel.setPadding(new Insets(0,0,5,0));
        initMenu(topPanel);
        pane.setTop(topPanel);

        HBox botPanel = new HBox();
        botPanel.setAlignment(Pos.CENTER);
        botPanel.setSpacing(10);
        botPanel.setPadding(new Insets(0));
        pane.setBottom(botPanel);

        //right

        VBox rightPanel = new VBox();
        rightPanel.getStyleClass().add("side-bar");
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(0,5,10,5));
        pane.setRight(rightPanel);

        Button pickAgain = new Button("Pick again");
        pickAgain.setId("pick-again-btn");
        pickAgain.setOnAction(e -> {
            try {
                Carousel carousel = new Carousel(mainUser.username, primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });

        //rightPanel.setPrefWidth(220);
        VBox userChart = new VBox();
        VBox.setVgrow(userChart, Priority.SOMETIMES);

        rightPanel.getChildren().add(userChart);
        rightPanel.getChildren().add(pickAgain);

        //Add users


        mainUser.menuIcon.setStyle("-fx-border-color: firebrick;-fx-border-width: 3;");

        addUnit(mainUser);
        addUnit(new Unit("dasha", 2));
        addUnit(new Unit("dasha", 3));

        userChart.getChildren().addAll(unitSet.stream().map(u -> u.menuIcon).collect(Collectors.toList()));



        //initclick
        class InitClick implements EventHandler<MouseEvent> {
            public void handle(MouseEvent e) {
                if (e.getTarget() instanceof LocationMark) {
                    mainUser.moveTo((LocationMark) e.getTarget());
                    starts.forEach(s -> {
                        s.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                    });
                    play(mapPane);
                }
            }

        }
        InitClick initClick = new InitClick();
        starts.forEach(s -> s.addEventHandler(MouseEvent.MOUSE_CLICKED, initClick));


        Scene scene = new Scene(pane/*, mapWidth+300, mapHeigth+150*/);
        scene.getStylesheets()
                .add(Login.class.getResource("/Graphics/style/GameField.css").toExternalForm());

        //choose start point

        primaryStage.setScene(scene);
        primaryStage.show();

        //start
        Alert start = new Alert(Alert.AlertType.INFORMATION, "Chose your start position.");
        start.showAndWait();

        /*Duration animationDuration = new Duration(1500);

        Timeline timeline = new Timeline(
                //стартовые значения
                new KeyFrame( Duration.ZERO, new KeyValue(forest.translateXProperty(), 0)),
                new KeyFrame( Duration.ZERO, new KeyValue(forest.translateYProperty(), 0)),
                //конечные значения
                new KeyFrame( animationDuration, new KeyValue(forest.translateXProperty(), 100)),
                new KeyFrame( animationDuration, new KeyValue(forest.translateYProperty(), 100))
        );
        timeline.play();*/

    }

    void play(Pane mapPane){
        LocationMark desert = new LocationMark(208.0, 322.0, 3, "Desert");
        LocationMark beach = new LocationMark(242.0, 89.0, 2, "Beach");
        LocationMark island = new LocationMark(409.0, 188.0, 2,"Island");
        LocationMark bTree = new LocationMark(541.0, 70.0, 1,"B-Tree");
        LocationMark away = new LocationMark(731.0, 38.0, 1,"Away");
        LocationMark christopherHome = new LocationMark(645.0, 247.0,2, "Christopher's\nhome");
        LocationMark forest = new LocationMark(560.0, 440.0, 3,"Forest");
        mapPane.getChildren().addAll(desert, beach, island, bTree, away, christopherHome, forest);
    }

    void addUnit(Unit unit) {
        if (unitSet.size()<limit)
            unitSet.add(unit);
    }

}
