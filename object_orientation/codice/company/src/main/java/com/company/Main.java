package com.company;

import com.company.GUI.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Login loginGUI = new Login();
        Scene loginScene = loginGUI.getScene();

        stage.setTitle("Azienda Dashboard");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();
        // controllo pull request
    }

    public static void main(String[] args) {
        launch(args);
    }
}