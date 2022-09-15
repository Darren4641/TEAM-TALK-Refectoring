package com.goindol.teamtalk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    private static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        System.setProperty("prism.lcdtext","false");
        Font.loadFont(getClass().getResourceAsStream("font/양진체v0.9_ttf.ttf"), 14);
        Parent root = FXMLLoader.load(getClass().getResource("views/InitialView.fxml"));
        stage.setTitle("Team Talk");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}