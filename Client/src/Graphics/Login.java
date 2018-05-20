package Graphics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Login extends Application{

    public String userName;
    private int numUsers = 5;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.initStyle(StageStyle.UNDECORATED);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 350, 220);
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (Login.class.getResource("/Graphics/style/Login.css").toExternalForm());
        primaryStage.show();

        Text title = new Text("Welcome");
        title.setId("welcome-text");
        title.setFont(Font.font("Garamond", FontWeight.NORMAL, 30));
        grid.add(title, 0,0,2,1);

        Label username = new Label("User:");
        username.setFont(Font.font("Garamond", 20));
        grid.add(username, 0,1);

        TextField userTF = new TextField("User" + numUsers++);

        grid.add(userTF, 1, 1);

        Button signInBtn = new Button("Sign in");
        HBox hbSignInBtn = new HBox(10);
        hbSignInBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbSignInBtn.getChildren().add(signInBtn);
        grid.add(hbSignInBtn, 1, 3);

        Button closeBtn = new Button("X");
        closeBtn.setOnAction(e -> System.exit(0));
        closeBtn.getStyleClass().clear();
        closeBtn.setId("close-label");

        HBox hbCloseBtn = new HBox(10);
        hbCloseBtn.getChildren().add(closeBtn);
        hbCloseBtn.setAlignment(Pos.TOP_RIGHT);
        grid.add(hbCloseBtn, 1, 0);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 0, 3, 2, 1);
        actionTarget.setId("pop-up-text");

        signInBtn.setOnAction(e -> {
            userName = userTF.getText();
            if (userName.length()>6) {
                userName = userName.substring(0,7);
            }
            actionTarget.setText("Signing in as " + userName);
            primaryStage.close();
            try {
                new Carousel(userName, primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                signInBtn.fire();
            }
        });

        grid.setBorder(new Border(new BorderStroke(Color.rgb(12,21,12), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
    }

}
