package com.company;

import com.company.GUI.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Login loginGUI = new Login();
        Scene scene = loginGUI.getScene();

        stage.setTitle("Azienda Dashboard");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}