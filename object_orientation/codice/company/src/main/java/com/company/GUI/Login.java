package com.company.GUI;

import com.company.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Login {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginGUI.fxml"));

    public Scene getScene()  {
        try {
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
