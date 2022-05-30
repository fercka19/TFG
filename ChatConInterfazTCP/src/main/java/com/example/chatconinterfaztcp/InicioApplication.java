package com.example.chatconinterfaztcp;

import controllers.CargaUsuarioController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InicioApplication.class.getResource("carga-usuario.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),210,400);
        stage.setTitle("chat!");
        stage.setResizable(false);
        stage.setScene(scene);
        CargaUsuarioController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}