package Graphics;

import Communication.AlternativeClient;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Stream;

// DisplayShelf
class Carousel {
    private AlternativeClient tc;
    private Unit unit;
    private GameField gf;
    private double width;

    Carousel(Stage stage, AlternativeClient tc, Unit unit) throws Exception {
        this.unit = unit;
        this.width = 1213;
        this.tc = tc;
        start(stage);
    }

    Carousel(Stage stage, AlternativeClient tc, Unit unit, GameField gf, double width) throws Exception {
        this.unit = unit;
        this.tc = tc;
        this.gf = gf;
        this.width = width;
        start(stage);
    }
    //TODO Remove по крестику
    //TODO Реализовать невозможность выбрать одинакового персонажа

    private void start(Stage stage) throws Exception {
        String folder = "Graphics/imgs/Faces";
        File[] files = null;

        try {
             files = new File(getClass().getClassLoader().getResource(folder).getPath()).listFiles();
        } catch (NullPointerException e) {
            System.err.println("No such path as " + folder);
        }

        CarouselItem[] images;

        int[] index = {0};

        if (files != null) {
            images =
                    Arrays.stream(files)
                            .map(file -> file.toURI().toString())
                            .map(url -> new CarouselItem(url, index[0]++))
                            .toArray(CarouselItem[]::new);
        } else {
            throw new IOException("There are no files in the folder Faces");
        }

        Group group = new Group();
        group.getChildren().addAll(images);

        Scene scene = new Scene(group, width, 700, true);
        scene.getStylesheets().add(Carousel.class.getResource("/Graphics/style/GameField.css").toExternalForm());
        scene.setFill(Color.rgb(0x66, 33, 33));
        stage.setScene(scene);
        stage.getScene().setCamera(new PerspectiveCamera());
        stage.setResizable(false);

        HBox menu = new HBox();
        menu.setLayoutY(0);
        menu.setLayoutX(0);
        menu.setPrefWidth(scene.getWidth());
        GameField.initMenu(stage, menu);
        group.getChildren().add(menu);

        Slider slider = new Slider(0, images.length - 1, 0);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.translateXProperty().bind(stage.widthProperty().divide(2).subtract(slider.widthProperty().divide(2)));
        slider.translateYProperty().bind(menu.heightProperty().divide(2).subtract(slider.heightProperty().divide(2)));
        group.getChildren().add(slider);

        Label toChose = new Label("Press Enter to choose your hero");
        toChose.setFont(Font.font("Garamond", 20));
        toChose.setStyle("-fx-text-fill: aliceblue");
        toChose.setLayoutY(slider.getLayoutY() + 50);
        toChose.translateXProperty().bind(stage.widthProperty().divide(2).subtract(toChose.widthProperty().divide(2)));
        group.getChildren().add(toChose);


        //actions
        slider.valueProperty().addListener((p, o, n) -> {
            if (n.doubleValue() == n.intValue())
                Stream.of(images).forEach(u -> u.update(n.intValue(), stage.getWidth(), stage.getHeight()));
        });


        group.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() instanceof CarouselItem)
                slider.setValue(((CarouselItem) e.getTarget()).index);
        });


        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    unit.assignCharacter((int) slider.getValue() + 1);
                    unit.assignMenuIcon();
                    try {
                        if (gf==null) {
                            stage.close();
                            gf = new GameField(stage, tc, unit);
                        } else {
                            gf.pickAgain();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
            }
        });

        ShutdownHook shutdownHook = new ShutdownHook(()->unit.setRemoved(true));
        Runtime.getRuntime().addShutdownHook(shutdownHook);


        stage.show();
        slider.setValue(4);

    }

    private static class CarouselItem extends ImageView {
        final static Reflection reflection = new Reflection();
        final static Point3D rotationAxis = new Point3D(0, 60, 0);

        static {
            reflection.setFraction(0.5);
        }

        final int index;
        final Rotate rotate = new Rotate(10, rotationAxis);
        final TranslateTransition transition = new TranslateTransition(Duration.millis(200), this);

        CarouselItem(String imageUrl, int index) {
            super(imageUrl);
            setEffect(reflection);
            setUserData(index);
            this.index = index;
            getTransforms().add(rotate);
        }

        private void update(int currentIndex, double width, double height) {
            int ef = index - currentIndex;
            double middle = width / 2 - 100;
            boolean b = ef < 0;

            setTranslateY(height / 2 - getImage().getHeight() / 2);
            double x, z, angle, pivot;

            if (ef == 0) {
                z = -300;
                x = middle;
                angle = 0;
                pivot = b ? 200 : 0;
            } else {
                x = middle + ef * 82 + (b ? -147 : 147);
                z = -78.588;
                pivot = b ? 200 : 0;
                angle = b ? 50 : -50;
            }
            rotate.setPivotX(pivot);
            rotate.setAngle(angle);

            transition.pause();
            transition.setToX(x);
            transition.setToZ(z);
            transition.play();
        }

    }

}


class ShutdownHook extends Thread {
    private Runnable r;

    ShutdownHook(Runnable r) {
        this.r = r;
    }

    public void run() {
        try {
            r.run();
        } catch (NullPointerException e) {
            System.out.println("No file");
        }
    }
}