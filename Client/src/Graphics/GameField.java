package Graphics;

import Communication.AlternativeClient;
import GameFieldItems.LocationMark;
import GameFieldItems.Unit;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.*;

class GameField {
//  TODO Сделать alert о занятости клетки
//  TODO Переделать механизм синхронизации

    //core fields
    private Unit mainUnit;
    private AlternativeClient tc;

    //layout fields
    private VBox userChart = new VBox();
    private Scene scene;
    private Stage primaryStage;
    private SimpleBooleanProperty paused = new SimpleBooleanProperty(false);
    private Image map = new Image(getClass().getClassLoader().getResourceAsStream("imgs/Map.png"));
    private double mapWidth = map.getWidth() / 4;
    private double mapHeight = map.getHeight() / 4;
    private ArrayList<LocationMark> starts = new ArrayList<>();
    private Image imgPlay = new Image(getClass().getClassLoader().getResourceAsStream("imgs/play.png"));
    private Image imgPause = new Image(getClass().getClassLoader().getResourceAsStream("imgs/pause.png"));
    private AnchorPane mapPane = new AnchorPane();

    //fields for Unit movements
    private HashMap<Integer, LocationMark> locations = new HashMap<>();
    private ArrayList<Unit> playerList = new ArrayList<>();

    //offsets to move the window
    private static double xOffset;
    private static double yOffset;


    GameField(Stage stage, AlternativeClient tc, Unit unit) throws Exception {
        this.tc = tc;
        mainUnit = unit;
        this.primaryStage = stage;
        start(primaryStage);
    }


    //layout management


    static void initMenu(Stage primaryStage, HBox hbox) {
        //TODO Make working menu items

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
        visitWebsite.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "https://se.ifmo.ru/");
            alert.showAndWait();
        });
        help.getItems().add(visitWebsite);

        mainMenu.getMenus().addAll(file, edit, help);

        properties.setOnAction(event -> new Alert(Alert.AlertType.WARNING, "Under construction").showAndWait());
        openFile.setOnAction(event -> new Alert(Alert.AlertType.WARNING, "Under construction").showAndWait());
        exitApp.setOnAction(event -> new Alert(Alert.AlertType.WARNING, "Under construction").showAndWait());
        properties.setOnAction(event -> new Alert(Alert.AlertType.WARNING, "Under construction").showAndWait());


        MenuBar leftClose = new MenuBar();

        Menu close = new Menu("☓");
        MenuItem dummy = new MenuItem("");
        close.getItems().add(dummy);
        close.setId("close");

        close.setOnShowing(e -> {
            primaryStage.close();
            System.exit(0);
        });
        leftClose.getMenus().add(close);

        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");

        HBox.setHgrow(spacer, Priority.SOMETIMES);
        hbox.getChildren().addAll(mainMenu, spacer, leftClose);

        //action

        //make the window draggable
        hbox.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        hbox.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });


    }


    private void start(Stage primaryStage) throws Exception {

        BorderPane pane = new BorderPane();

        scene = new Scene(pane/*, mapWidth+300, mapHeight+150*/);
        scene.getStylesheets().add(GameField.class.getResource("/style/GameField.css").toExternalForm());

        primaryStage.setResizable(true);
        primaryStage.setScene(scene);


        //middle map panel
        mapPane = new AnchorPane();
        mapPane.setShape(new Rectangle(mapWidth, mapHeight));
        mapPane.setMinSize(mapWidth, mapHeight);
        mapPane.setMaxSize(mapWidth, mapHeight);
        mapPane.setId("map");

        //playpause

        ImageView playPause = new ImageView(imgPlay);
        AnchorPane.setLeftAnchor(playPause, 0.5);
        AnchorPane.setTopAnchor(playPause, 0.5);
        paused.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                playPause.setImage(imgPause);
            } else
                playPause.setImage(imgPlay);
        });
        mapPane.getChildren().add(playPause);

        //starts

        //left start
        VBox leftStart = new VBox();
        leftStart.setSpacing(50.0);

        LocationMark LStart1 = new LocationMark(1, 15);
        LocationMark LStart2 = new LocationMark(1, 1);
        LocationMark LStart3 = new LocationMark(1, 2);
        LocationMark LStart4 = new LocationMark(1, 3);

        leftStart.getChildren().addAll(LStart1, LStart2, LStart3, LStart4);
        leftStart.getChildren().forEach(e -> e.getStyleClass().add("left-start-pos"));


        //bottom start
        HBox botStart = new HBox();
        botStart.setSpacing(50.0);

        LocationMark BStart1 = new LocationMark(1, 4);
        LocationMark BStart2 = new LocationMark(1, 5);
        LocationMark BStart3 = new LocationMark(1, 6);
        LocationMark BStart4 = new LocationMark(1, 7);

        botStart.getChildren().addAll(BStart1, BStart2, BStart3, BStart4);
        botStart.getChildren().forEach(e -> e.getStyleClass().add("bot-start-pos"));

        //starts all together
        starts.addAll(Arrays.asList(LStart1, LStart2, LStart3, LStart4, BStart1, BStart2, BStart3, BStart4));

        //locations
        LocationMark desert = new LocationMark(208.0, 322.0, 3, "Desert", 8);
        LocationMark beach = new LocationMark(242.0, 89.0, 2, "Beach", 9);
        LocationMark island = new LocationMark(409.0, 188.0, 2, "Island", 10);
        LocationMark bTree = new LocationMark(541.0, 70.0, 1, "B-Tree", 11);
        LocationMark away = new LocationMark(731.0, 38.0, 1, "Away", 12);
        LocationMark christopherHome = new LocationMark(645.0, 247.0, 2, "Christopher's\nhome", 13);
        LocationMark forest = new LocationMark(560.0, 440.0, 3, "Forest", 14);
        locations.put(beach.id, beach);
        locations.put(desert.id, desert);
        locations.put(island.id, island);
        locations.put(bTree.id, bTree);
        locations.put(away.id, away);
        locations.put(christopherHome.id, christopherHome);
        locations.put(forest.id, forest);
        locations.forEach((k, v) -> v.setVisible(false));
        locations.put(LStart1.id, LStart1);
        locations.put(BStart1.id, BStart1);
        locations.put(LStart2.id, LStart2);
        locations.put(BStart2.id, BStart2);
        locations.put(LStart3.id, LStart3);
        locations.put(BStart3.id, BStart3);
        locations.put(LStart4.id, LStart4);
        locations.put(BStart4.id, BStart4);
        locations.forEach((k,v) -> v.setOnMouseClicked(s -> {
            if (!paused.get()) {
                if (!mainUnit.isRemoved()) {
                    mainUnit.moveTo(v);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Sorry, you have been removed. Watch and cry.").showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Paused");
                alert.setGraphic(new ImageView(imgPause));
                alert.showAndWait();
            }
        }));

        mapPane.getChildren().addAll(desert, beach, island, bTree, away, christopherHome, forest);
        mapPane.getChildren().add(leftStart);
        mapPane.getChildren().add(botStart);
        AnchorPane.setLeftAnchor(leftStart, 50.0);
        AnchorPane.setTopAnchor(leftStart, 70.0);
        AnchorPane.setBottomAnchor(botStart, 50.0);
        AnchorPane.setRightAnchor(botStart, 100.0);

        pane.setCenter(mapPane);


        //top, bot panels
        HBox topPanel = new HBox();
        topPanel.setAlignment(Pos.TOP_LEFT);
        topPanel.setSpacing(0);
        topPanel.setPadding(new Insets(0, 0, 5, 0));
        initMenu(primaryStage, topPanel);
        pane.setTop(topPanel);

        HBox botPanel = new HBox();
        botPanel.setAlignment(Pos.CENTER);
        botPanel.setSpacing(10);
        botPanel.setPadding(new Insets(0));
        pane.setBottom(botPanel);


        //right panel
        VBox rightPanel = new VBox();
        rightPanel.getStyleClass().add("side-bar");
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(0, 5, 10, 5));
        rightPanel.setPrefWidth(300);

        //pick again button
        Button pickAgain = new Button("Pick again");
        pickAgain.setId("pick-again-btn");

        //add children
        rightPanel.getChildren().add(userChart);
        rightPanel.getChildren().add(pickAgain);
        VBox.setVgrow(userChart, Priority.SOMETIMES);
        pane.setRight(rightPanel);


        mainUnit.menuIcon.setStyle("-fx-border-color: firebrick;-fx-border-width: 3;");


        //action

        ShutdownHook shutdownHook = new ShutdownHook(() -> removeUnit(mainUnit));
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        //InitClick
        class InitClick implements EventHandler<MouseEvent> {
            public void handle(MouseEvent e) {
                if (e.getTarget() instanceof LocationMark) {
                    if (mainUnit.moveTo((LocationMark) e.getTarget())) {
                        starts.forEach(s -> s.removeEventHandler(MouseEvent.MOUSE_CLICKED, this));
                        locations.forEach((k, v) -> v.setVisible(true));
                        addUnit(mainUnit);
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Location occupied by" + ((LocationMark) e.getTarget()).charPool.iterator().next().getName()).showAndWait();
                    }
                }
            }

        }
        InitClick initClick = new InitClick();
        starts.forEach(s -> s.addEventHandler(MouseEvent.MOUSE_CLICKED, initClick));

        //pick again button pressed
        pickAgain.setOnAction(e -> {
            try {
                new Carousel(primaryStage, tc, mainUnit, this, scene.getWidth());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });

        //unit exit

        //TODO Отображение окна одновременно с отображением карты
        new Alert(Alert.AlertType.INFORMATION, "Now you wil choose your start position.").showAndWait();
        primaryStage.show();
        service.start();

    }


    private Service<Boolean> service = new Service<Boolean>() {

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                boolean listening = true;

                @Override
                protected Boolean call() {
                    do {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                        if (!sync()) {
                            removeUnit(mainUnit);
                            mainUnit.setKicked(true);
                            Platform.runLater(() -> {
                                new Alert(Alert.AlertType.WARNING, "You've been kicked.").showAndWait();
                                System.exit(0);
                            });
                            break;
                        }
                        /*try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    } while (listening);
                    return true;
                }
            };
        }

    };


    private boolean sync() {
        try {

            String cmd = (String) tc.ois.readObject();
            tc.units = (Stack<Unit>) tc.ois.readObject();
            if (cmd != null)
                processCmd(cmd);
            if (tc.units.contains(mainUnit)) {
                playerList.stream().filter(unit -> (!tc.units.contains(unit) || (tc.units.get(tc.units.indexOf(unit)).isRemoved() && !unit.isRemoved())))
                        .forEach(this::removeUnit);
                tc.units.stream().filter(unit -> !unit.equals(mainUnit) && !unit.isRemoved() && unit.getLocId() != 0)
                        .forEach(this::addUnit);
                tc.oos.reset();
                tc.oos.writeObject(mainUnit);
                tc.oos.flush();
                return true;
            } else {
                return false;
            }

        } catch (EOFException e) {
            return false;
        } catch (SocketException e) {
            Platform.runLater(() -> {
                new Alert(Alert.AlertType.ERROR, "Server unavailable.").showAndWait();
                System.exit(1);
            });
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //do nothing (кидает исключение когда прилетает строка)
        }
        return true;
    }


    private void addUnit(Unit unit) {
        if (unit.currentLocation == null) unit.restore();
        if (!playerList.contains(unit)) {
            //int limit = 8;
            //if (tc.units.size() < limit) {
            playerList.add(unit);
            unit.moveTo(locations.get(unit.getLocId()));
            addToChart(unit);
            // }
        } else playerList.get(playerList.indexOf(unit)).moveTo(locations.get(unit.getLocId()));
    }


    private void removeUnit(Unit unit) {
        //TODO исправить ошибку с id локации
        Platform.runLater(() -> userChart.getChildren().remove(unit.menuIcon));
        unit.setLocId(0);
        unit.setCharId(0);
        unit.setRemoved(true);
        if (!unit.isKicked())
            try {
                tc.oos.reset();
                tc.oos.writeObject(unit);
                tc.oos.flush();
            } catch (IOException e) {
                //do nothing
            }
        unit.leave();
        System.out.println(unit.getName() + " removed.");
        System.out.close();
    }


    private void addToChart(Unit unit) {
        Platform.runLater(() ->
                userChart.getChildren().add(unit.menuIcon));
    }

    void pickAgain() {
//        TODO Испраить ошибку с иконкой в меню
        primaryStage.setScene(scene);
    }

    private void processCmd(String s) {
        switch (s) {
            case "pause": {
                paused.set(true);
            }
            break;
            case "play":
                paused.set(false);
                break;
            case "move":
                mainUnit.moveTo(locations.get(tc.units.get(tc.units.indexOf(mainUnit)).getLocId()));
                break;

            case "remove":
                System.out.println("Received remove command.");
                removeUnit(mainUnit);
                break;

            case "load":
                mainUnit.moveTo(locations.get(tc.units.get(tc.units.indexOf(mainUnit)).getLocId()));
                mainUnit.assignCharacter(tc.units.get(tc.units.indexOf(mainUnit)).getCharId());
                break;
        }
    }

}





/*-*******************OLD CODE********************/


/*
public void setupLocationGenerator(Pane mapPane) {
        mapPane.setOnMouseClicked(e -> {

            LocationMark lm = new LocationMark(e.getX(), e.getY(), 1, 1000);
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
 */