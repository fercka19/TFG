package controllers;

import logica.HiloServidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CargaServidorController {

    private ServerSocket servidor;
    private Socket cliente;
    private DataInputStream entrada;
    private DataOutputStream salida;


    public void iniciarServer() throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        servidor = new ServerSocket(6000);
                        System.out.println("Servidor iniciado...");
                        while (!servidor.isClosed()) {
                            cliente = servidor.accept();//esperando al cliente
                            entrada = new DataInputStream(cliente.getInputStream());
                            salida = new DataOutputStream(cliente.getOutputStream());
                            String nombre = entrada.readUTF();

                            HiloServidor hiloServidor = new HiloServidor(cliente, nombre);

                            Thread thread = new Thread(hiloServidor);
                            thread.start();
                        }

                } catch (IOException e) {
                    System.out.println("servidor detenido");
                }
            }
        }).start();

    }


}
