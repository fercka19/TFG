package com.example.chatconinterfaztcp;

import controllers.CargaServidorController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioServidor extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        CargaServidorController cargaServidorController = new CargaServidorController();
        cargaServidorController.iniciarServer();
    }
}
