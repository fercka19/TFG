package com.example.chatconinterfaztcp;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ItemController {

    @FXML
    private HBox mensajeColor;

    @FXML
    private Text txtMensaje;

    public void darTexto(String mensaje) {

        if (mensaje.length() < 20) {
            txtMensaje.setText(mensaje);
        } else {
            txtMensaje.setWrappingWidth(150);
            txtMensaje.setText(mensaje);
        }
    }

    public void colorTextoRecivido() {
        mensajeColor.getStyleClass().add("color");
    }

    public void colorTextoEnviado() {
        mensajeColor.getStyleClass().add("colorEnvio");
    }

    public void colorTextoPrivado() {
        mensajeColor.getStyleClass().add("colorPrivado");

    }

    public void colorTextoUsuario() {
        mensajeColor.getStyleClass().add("colorUsuario");
    }

}