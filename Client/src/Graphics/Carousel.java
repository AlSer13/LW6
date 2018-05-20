package Graphics;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

// DisplayShelf
public class Carousel extends Application {
    int chosenCharacterID;
    String username;

    public Carousel(String username, Stage stage) throws Exception {
        this.username = username;
        start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String folder = "Client\\src\\Graphics\\imgs\\Faces";

        int[] index = {0};

        Unit[] images =
                Arrays.stream(new File(folder).listFiles())
                        .map(file -> file.toURI().toString())
                        .map(url -> new Unit(url, index[0]++))
                        .toArray(Unit[]::new);

        Group group = new Group();
        //group.setStyle("-fx-background-color:derive(darkred, 20%)");
        group.getChildren().addAll(images);

        Slider slider = new Slider(0, images.length - 1, 0);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);

        group.getChildren().add(slider);

        Scene scene = new Scene(group, 1000, 500, true);
        scene.setFill(Color.rgb(0x66,33,33));

        stage.setScene(scene);
        stage.getScene().setCamera(new PerspectiveCamera());
        stage.setResizable(false);
        stage.show();

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER: chosenCharacterID = (int)slider.getValue()+1;
                    try {
                        stage.close();
                        new GameField(username, chosenCharacterID, stage);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
            }
        });

        Label toChose = new Label("Press Enter to choose your hero");
        toChose.setFont(Font.font("Garamond", 20));
        toChose.setStyle("-fx-text-fill: aliceblue");
        toChose.setLayoutY(slider.getLayoutY() + 30);
        toChose.translateXProperty().bind(stage.widthProperty().divide(2).subtract(toChose.widthProperty().divide(2)));
        group.getChildren().add(toChose);

        /*Button choose = new Button("✓");
        choose.setOnAction(e -> {
            chosenCharacterID = slider.getValue();
            GameField gameField = new GameField();
            try {
                stage.close();
                gameField.start(stage);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
        group.getChildren().add(choose);
        choose.toFront();
        choose.translateXProperty().bind(stage.widthProperty().divide(2));
        choose.translateYProperty().bind(stage.heightProperty().subtract(40));*/

        slider.translateXProperty().bind(stage.widthProperty().divide(2).subtract(slider.widthProperty().divide(2)));
        slider.setTranslateY(10);
        slider.valueProperty().addListener((p, o, n) -> {
            if(n.doubleValue() == n.intValue())
                Stream.of(images).forEach(u -> {u.update(n.intValue(), stage.getWidth(), stage.getHeight());});
        });

        group.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if(e.getTarget() instanceof Unit)
                slider.setValue(((Unit)e.getTarget()).index);
        });

        Button close = new Button("X");
        close.setOnAction(e -> System.exit(0));
        close.getStyleClass().clear();
        close.setStyle("-fx-text-fill:white;-fx-font-size:15;-fx-font-weight:bold;-fx-font-family:'Comic Sans MS';");
        group.getChildren().add(close);
        close.translateXProperty().bind(stage.widthProperty().subtract(15));


        slider.setValue(4);
    }

    private static class Unit extends ImageView {
        final static Reflection reflection = new Reflection();
        final static Point3D  rotationAxis = new Point3D(0, 60, 0);

        static {
            reflection.setFraction(0.5);
        }

        final int index;
        final Rotate rotate = new Rotate(10, rotationAxis);
        final TranslateTransition transition = new TranslateTransition(Duration.millis(200), this);

        public Unit(String imageUrl, int index) {
            super(imageUrl);
            setEffect(reflection);
            setUserData(index);
            this.index = index;
            getTransforms().add(rotate);
        }

        public void update(int currentIndex, double width, double height) {
            int ef = index - currentIndex;
            double middle = width / 2 - 100;
            boolean b = ef < 0;

            setTranslateY(height/2 - getImage().getHeight()/2);
            double x,z, theta, pivot;

            if(ef == 0) {
                z = -300;
                x = middle;
                theta = 0;
                pivot = b ? 200 : 0;
            }
            else {
                x = middle + ef * 82 + (b ? -147 : 147);
                z = -78.588;
                pivot = b ? 200 : 0 ;
                theta = b ? 46 : -46;
            }
            rotate.setPivotX(pivot);
            rotate.setAngle(theta);

            transition.pause();
            transition.setToX(x);
            transition.setToZ(z);
            transition.play();
        }

    }

}