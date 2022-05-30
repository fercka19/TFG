package controllers;

import conexionDB.Conexion;
import com.example.chatconinterfaztcp.InicioApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

public class CargaUsuarioController {
    private Stage stage;
    private Stage stageRegis;
    private String usuario, pass;

    @FXML
    private TextField txtNick;

    @FXML
    private PasswordField txtPassLog;

    @FXML
    private PasswordField txtPass;

    @FXML
    private TextField txtUsuario;

    @FXML
    private void mostrarAlertError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    @FXML
    void btnRegistrar(ActionEvent event) {
        asignarValoresRegistro();
        Conexion conexion = new Conexion();
        conexion.setUsuario(usuario);
        conexion.setPass(pass);
        //boolean controlalert =conexion.insertar();
        if (conexion.insertar()) {
            mostrarAlertError("Error usuario ya registrado");
        }
        limpiar();

    }

    @FXML
    void irRegistro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(InicioApplication.class.getResource("registro.fxml"));
        Scene scene = new Scene(loader.load(), 210, 400);
        this.stageRegis = new Stage();
        this.stageRegis.setResizable(false);
        this.stageRegis.setScene(scene);
        this.stageRegis.show();

    }

    @FXML
    void irChat(ActionEvent event) throws Exception {

        if (!txtNick.getText().equals("")&& !txtPassLog.getText().equals("")) {

                Conexion conexion = new Conexion();
                conexion.setUsuario(txtNick.getText());
                conexion.setPass(txtPassLog.getText());
                Boolean log = conexion.login();
                if (conexion.login()) {
                    conexion.online(1, txtNick.getText());
                    mostrarChat(log);
                } else {
                    mostrarAlertError("Error usuario no Registrado");
                }

            }

    }

    public void mostrarChat(Boolean log) {
        try {
            Stage stage1 = new Stage();
            if (log.equals(true)) {

                FXMLLoader loader = new FXMLLoader(InicioApplication.class.getResource("hello-view.fxml"));
                Scene scene = null;
                scene = new Scene(loader.load(), 400, 400);

                ClienteController controller = loader.getController();


                stage1.setResizable(false);
                stage1.setScene(scene);
                stage1.show();
                controller.init(txtNick.getText());

                this.stage.close();
                //cerrar apli
                stage1.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void asignarValoresRegistro() {
        usuario = txtUsuario.getText();
        pass = txtPass.getText();
    }

    public void limpiar() {
        txtUsuario.setText("");
        txtPass.setText("");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
