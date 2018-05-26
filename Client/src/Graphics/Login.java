package Graphics;

import Communication.AlternativeClient;
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

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

import static javafx.application.Application.STYLESHEET_CASPIAN;
import static javafx.application.Application.setUserAgentStylesheet;


public class Login {


    private String userName;
    private AlternativeClient tc;
    private Unit unit;




    public Login(AlternativeClient tc, Stage stage) {

        this.tc = tc;
        tc.connectLocal();
        this.start(stage);

    }




    private void start(Stage primaryStage) {

        primaryStage.initStyle(StageStyle.UNDECORATED);
        setUserAgentStylesheet(STYLESHEET_CASPIAN);


        //make grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setBorder(new Border(new BorderStroke(Color.rgb(12,21,12), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));


        //make scene
        Scene scene = new Scene(grid, 350, 220);
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (Login.class.getResource("/Graphics/style/Login.css").toExternalForm());


        //make "Welcome" title
        Text title = new Text("Welcome");
        title.setId("welcome-text");
        title.setFont(Font.font("Garamond", FontWeight.NORMAL, 30));
        grid.add(title, 0,0,2,1);


        //make "User:" label and text field for it
        Label username = new Label("User:");
        username.setFont(Font.font("Garamond", 20));
        TextField userTF = new TextField("User" + tc.units.size());
        grid.add(username, 0,1);
        grid.add(userTF, 1, 1);


        //make Close button and a container for it
        Button closeBtn = new Button("X");
        closeBtn.setOnAction(e -> System.exit(0));
        closeBtn.getStyleClass().clear();
        closeBtn.setId("close-label");
        HBox hbCloseBtn = new HBox(10);
        hbCloseBtn.getChildren().add(closeBtn);
        hbCloseBtn.setAlignment(Pos.TOP_RIGHT);
        grid.add(hbCloseBtn, 1, 0);


        //make a pop-up text
        final Text actionTarget = new Text();
        grid.add(actionTarget, 0, 3, 2, 1);
        actionTarget.setId("pop-up-text");


        //make sign in button
        Button signInBtn = new Button("Sign in");
        HBox hbSignInBtn = new HBox(10);
        hbSignInBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbSignInBtn.getChildren().add(signInBtn);
        grid.add(hbSignInBtn, 1, 3);





        //actions
        signInBtn.setOnAction(e -> {
            userName = userTF.getText();
            try {
//                TODO Исправить ошибку с одновременной авторизацией
                tc.units = (Stack<Unit>) tc.ois.readObject();
                tc.oos.reset();
                tc.oos.writeObject(null);
                tc.oos.flush();
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            if (userName.length() <= 10) { //check if username is of appropriate size
                if (tc.units.stream()
                        .filter(Objects::nonNull)
                        .noneMatch(u -> Objects.equals(u.getName(), userName))) {
                    actionTarget.setText("Signing in as \n" + userName + ".");
                    unit = new Unit(userName);
                    try {
                        tc.units = (Stack<Unit>) tc.ois.readObject();
                        tc.oos.reset();
                        tc.oos.writeObject(unit);
                        tc.oos.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    primaryStage.close();
                    try {
                        new Carousel(primaryStage, tc, unit); //create a Carousel
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else
                    actionTarget.setText("Username exists");
            } else {
                actionTarget.setText("Username must not be\nlarger then 10 symbols.");
            }

        });

        //fire "sign in" on enter
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                signInBtn.fire();
            }
        });




        primaryStage.show();
    }

}
