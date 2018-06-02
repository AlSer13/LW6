package Graphics;

import Communication.MultiClientThread;
import GameFieldItems.Unit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;


public class ServerRoom extends Application {
    //все взаимодействие с ServerRoom происходит через unitsOL

    private File saveFile;
    private HashMap<String, MultiClientThread> clientMap;

    private ObservableList<Unit> unitsOL;
    private ObservableList<String> users = FXCollections.observableArrayList();
    private ObservableList<String> characters = FXCollections.observableArrayList(Unit.CharNames);
    private ObservableList<String> locations = FXCollections.observableArrayList(Unit.LocNames);

    public ServerRoom(Stage primaryStage, ObservableList<Unit> unitsOL, File file, HashMap<String, MultiClientThread> clientMap) throws Exception {
        this.unitsOL = unitsOL;
        this.unitsOL.addListener((ListChangeListener<Unit>) c -> {
            c.next();
            c.getAddedSubList().forEach(u -> users.add(u.getName()));
        });
        this.saveFile = file;
        start(primaryStage);
        this.clientMap = clientMap;
    }

    public void start(Stage primaryStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        //main pane
        VBox pane = new VBox();
        pane.setSpacing(30);

        //general markup
        GridPane top = new GridPane();
        top.setVgap(20);
        top.setHgap(20);
        top.setAlignment(Pos.CENTER);

        Text bot = new Text();
        bot.setText("To perform a command on a unit, select it in the table.");

        pane.getChildren().addAll(top, bot);


        //filling top

        //add table
        TableView<Unit> table = new TableView<>();

        TableColumn<Unit, String> nameCol = new TableColumn<>("Owner");
        TableColumn<Unit, Integer> charIdCol = new TableColumn<>("Character ID");
        TableColumn<Unit, String> charNameCol = new TableColumn<>("Character name");
        TableColumn<Unit, Integer> locIdCol = new TableColumn<>("Location ID");
        TableColumn<Unit, String> locNameCol = new TableColumn<>("Location name");
        //TableColumn<Unit, Boolean> removedCol = new TableColumn<>("Removed");

        charIdCol.setCellValueFactory(new PropertyValueFactory<>("charId"));
        charNameCol.setCellValueFactory(new PropertyValueFactory<>("charName"));
        locIdCol.setCellValueFactory(new PropertyValueFactory<>("locId"));
        locNameCol.setCellValueFactory(new PropertyValueFactory<>("locName"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        //removedCol.setCellValueFactory(new PropertyValueFactory<>("removed"));

        table.getColumns().addAll(nameCol, charIdCol, charNameCol, locIdCol, locNameCol/*, removedCol*/);
        table.setItems(unitsOL);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        GridPane.setHgrow(table, Priority.SOMETIMES);
        top.setPadding(new Insets(20));

        top.add(table, 0, 0, 1, 3);

        //add play pause
        // HBox playPause = new HBox();
        //playPause.setSpacing(10);
        //playPause.setPadding(new Insets(20));
        ImageView imgPlay = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("imgs/play.png")));
        ImageView imgPause = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("imgs/pause.png")));
        Button playPause = new Button();
        playPause.setGraphic(imgPause);

        //playPause.getChildren().addAll(play, pause);

        top.add(playPause, 1, 0);

        //add commands
        ButtonBar commands = new ButtonBar();

        Button save = new Button("Save");
        Button load = new Button("Load");
        Button toStart = new Button("toStart");
        Button terminate = new Button("Terminate");

        commands.getButtons().addAll(save, load, toStart, terminate);

        top.add(commands, 0, 3);


        //filling middle

        VBox mid = new VBox();
        mid.setSpacing(20);
        mid.setAlignment(Pos.CENTER);
        top.add(mid, 1, 2, 2, 1);

        //left
        HBox left = new HBox();

        Button move = new Button("Move");
        ComboBox<String> locsBox = new ComboBox<>(locations);

        left.getChildren().addAll(move, locsBox);

        //center
        HBox center = new HBox();
        center.setSpacing(10);

        Button kick = new Button("Kick");
        Button remove = new Button("Remove");

        center.getChildren().addAll(kick, remove);

        //right
        HBox right = new HBox();

        /*Button add = new Button("Add");
        ComboBox<String> charBox = new ComboBox<>(characters);
        TextField newUserName = new TextField("username");

        right.getChildren().addAll(add, charBox, newUserName);*/

        mid.getChildren().addAll(left, center, right);

        Scene scene = new Scene(pane);
        /*scene.getStylesheets()
                .add(ServerRoom.class.getResource("/Graphics/style/GameField.css").toExternalForm());*/

        primaryStage.setScene(scene);

        //action
        kick.setOnAction(event -> {
            Unit selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.INFORMATION, "Select a unit from table first.").showAndWait();
            } else if (clientMap != null) {
                clientMap.get(selected.getName()).sendCmd("kick");
            }
            //TODO alerts for clientmap null
            //unitsOL.remove(selected);

        });

        remove.setOnAction(event -> {
            Unit selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.INFORMATION, "Select a unit from table first.").showAndWait();
            } else if (clientMap != null) {
                //TODO ограничения на нажатие кнопок
                if (!selected.isRemoved())
                    clientMap.get(selected.getName()).sendCmd("remove");
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("imgs/widmnd.jpg"))));
                    alert.showAndWait();
                }
            }
        });

        move.setOnAction(event -> {
            Unit selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.INFORMATION, "Select a unit from table first.").showAndWait();
            } else if (locsBox.getSelectionModel().getSelectedItem().equals("Not specified")) {
                new Alert(Alert.AlertType.ERROR, "Can't move to \"Not specified\".").showAndWait();
            } else if (selected.isRemoved()) {
                new Alert(Alert.AlertType.ERROR, "The unit is removed.").showAndWait();
            } else {
                clientMap.get(selected.getName()).sendCmd("move", Unit.LocNames.indexOf(locsBox.getSelectionModel().getSelectedItem()));
            }
        });

        /*add.setOnAction(event -> {
            unitsOL.add(new Unit(newUserName.getText(), Unit.CharNames.indexOf(charBox.getSelectionModel().getSelectedItem())));

        });*/

        save.setOnAction(event -> {
            try {
                save();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });

        load.setOnAction(event -> {
            try {
                new Alert(Alert.AlertType.WARNING, "Provides a view of a collection in current window.").showAndWait();
                load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientMap.forEach((s,t) -> t.sendCmd("load"));

        });

        toStart.setOnAction(event -> {
            int i = 1;
            for (Unit unit :
                    unitsOL) {
                if (!unit.isRemoved()) {
                    clientMap.get(unit.getName()).sendCmd("move", i);
                    i++;
                }
            }
        });

        terminate.setOnAction(event -> {

        });

        playPause.setOnAction(event -> {
            if (!(clientMap == null))
                if (playPause.getGraphic().equals(imgPause)) {
                    playPause.setGraphic(imgPlay);
                    System.out.println("pause");
                    clientMap.forEach((s, t) -> t.sendCmd("pause"));
                } else {
                    playPause.setGraphic(imgPause);
                    System.out.println("play");
                    clientMap.forEach((s, t) -> t.sendCmd("play"));
                }

        });

        terminate.setOnAction(event -> System.exit(0));


        primaryStage.show();
    }

    private void save() throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        PrintWriter pw = new PrintWriter(saveFile);
        String read = gson.toJson(UnitWrapper.wrapAll(unitsOL));
        pw.print(read);
        pw.close();

    }

    private void load() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        FileReader fr = new FileReader(saveFile);
        Scanner scan = new Scanner(fr);
        StringBuilder sb = new StringBuilder();
        while (scan.hasNextLine()) {
            sb.append(scan.nextLine());
        }
        String json = sb.toString();
        unitsOL.clear();
        ArrayList<Unit> unitArrayList = UnitWrapper.unwrapAll(gson.fromJson(json, new TypeToken<ArrayList<UnitWrapper>>() {
        }.getType()));
        unitsOL.addAll(unitArrayList);
        fr.close();
    }

    private static class UnitWrapper {
        int charId;
        int locId;
        String username;

        static UnitWrapper wrap(Unit unit) {
            UnitWrapper wrapper = new UnitWrapper();
            wrapper.charId = unit.getCharId();
            wrapper.locId = unit.getLocId();
            wrapper.username = unit.getName();
            return wrapper;
        }

        static Unit unwrap(UnitWrapper wrapper) {
            Unit unit = new Unit(wrapper.username, wrapper.charId);
            unit.setLocId(wrapper.locId);
            return unit;
        }

        static ArrayList<UnitWrapper> wrapAll(Collection<Unit> units) {
            ArrayList<UnitWrapper> wrappers = new ArrayList<>();
            units.forEach(u -> wrappers.add(wrap(u)));
            return wrappers;
        }

        static ArrayList<Unit> unwrapAll(ArrayList<UnitWrapper> wrappers) {
            ArrayList<Unit> units = new ArrayList<>();
            wrappers.forEach(w -> units.add(unwrap(w)));
            return units;
        }
    }
}
