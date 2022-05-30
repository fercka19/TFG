
package controllers;

import conexionDB.Conexion;
import com.example.chatconinterfaztcp.InicioApplication;
import com.example.chatconinterfaztcp.ItemController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteController {
    private static String[] usuariosList = new String[20];

    private CargaUsuarioController controllerCarga;
    private Stage stage;
    private String nombre;
    private String mensaje;
    private final String Host = "127.0.0.1";
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private String respuesta;
    @FXML
    private ScrollPane scroll;
    @FXML
    private VBox mostrarMensajes;
    @FXML
    private VBox mostrarUsuarios;
    @FXML
    private Label tvNickUsuario;

    @FXML
    private TextField txtEnviar;


    @FXML
    void enviarMensaje(ActionEvent event) {
        try {
            mensaje = txtEnviar.getText();

            System.out.println(mensaje);
            if (!mensaje.equals("")) {
                txtEnviar.setText("");
                salida.writeUTF(mensaje);
                System.out.println(mensaje);
                pintarMensaje();
            }
        } catch (IOException e) {

            cerrar(socket, entrada, salida);
        }
    }


    public HBox diseñoMensaje(String cadena, String posicion, boolean pintarColorUsu) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(InicioApplication.class.getResource("item-mensaje.fxml"));
        HBox itemHbox = fxmlLoader.load();
        ItemController itemController = fxmlLoader.getController();
        itemController.darTexto(cadena);
        HBox hBox = new HBox();
        //enviados
        if (posicion.equals("right")) {
            privado(cadena, hBox, itemController);
            hBox.setAlignment(Pos.BASELINE_RIGHT);
            itemController.colorTextoEnviado();
        } else {
            //usuarios
            if (pintarColorUsu) {
                hBox.setAlignment(Pos.BASELINE_CENTER);
                itemController.colorTextoUsuario();
                //privados y recibidos
            } else {
                privado(cadena, hBox, itemController);
                itemController.colorTextoRecivido();
            }
        }
        //añadir
        hBox.getChildren().add(itemHbox);

        return hBox;
    }

    public void privado(String cadena, HBox hBox, ItemController itemController) {
        if (cadena.charAt(0) == ('/')) {
            itemController.colorTextoPrivado();
        }
    }

    public void init(String text) throws IOException {
        this.tvNickUsuario.setText(text);
        try {
            socket = new Socket(Host, 6000);
            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
            nombre = tvNickUsuario.getText();

            salida.writeUTF(nombre);
            escuchar();
        } catch (Exception e) {

        }

    }

    public void escuchar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        mostrarUsuarios.setSpacing(2);
                        mostrarMensajes.setSpacing(2);
                        respuesta = entrada.readUTF();

                        if (respuesta.contains("ONLINECLIENT :")) {
                            Conexion conexion = new Conexion();
                            ArrayList<Conexion> online = conexion.usuariosOnline();
                            System.out.println(online.size());
                            Platform.runLater(() -> {

                                mostrarUsuarios.getChildren().clear();
                            });

                            int i = 0;
                            for (Conexion onlineCliente : online) {
                                String nombreUsu = online.get(i).getUsuario();
                                i++;
                                Platform.runLater(() -> {

                                    try {

                                        mostrarUsuarios.getChildren().add(diseñoMensaje(nombreUsu, "", true));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                        pintarRespuesta();

                    } catch (IOException e) {

                        cerrar(socket, entrada, salida);
                    }

                }
            }
        }).start();
    }

    public void pintarMensaje() throws IOException {
        mostrarMensajes.getChildren().add(diseñoMensaje(mensaje, "right", false));
        scroll.vvalueProperty().bind(mostrarMensajes.heightProperty());
    }

    public void pintarRespuesta() throws IOException {


        Platform.runLater(() -> {
            try {
                System.out.println(respuesta + " en pintar respuesta");
                mostrarMensajes.getChildren().add(diseñoMensaje(respuesta, "", false));
            } catch (IOException e) {
                e.printStackTrace();
            }
            scroll.vvalueProperty().bind(mostrarMensajes.heightProperty());
        });


    }

    public static void cerrar(Socket socket, DataInputStream entrada, DataOutputStream salida) {
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}