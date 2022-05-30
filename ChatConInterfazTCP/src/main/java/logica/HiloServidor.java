package logica;

import conexionDB.Conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class HiloServidor extends Thread {
    private static ArrayList<HiloServidor> hiloServidors = new ArrayList<>();
    private DataInputStream entrada;
    private DataOutputStream salida;
    private Socket socket;
    private String nombre;
    private String mensaje;
    private String respuesta;
    private String leer1[];
    private String leer[];

    public HiloServidor(Socket s, String nombre) throws IOException {
        this.socket = s;
        this.entrada = new DataInputStream(socket.getInputStream());
        this.salida = new DataOutputStream(socket.getOutputStream());
        this.nombre = nombre;
        hiloServidors.add(this);
        enviarClientes();
    }

    public void enviarClientes() {
        for (HiloServidor hiloServidor : hiloServidors) {
            try {
                System.out.println("envio del Hiloservidor : " + nombre);
                hiloServidor.salida.writeUTF("ONLINECLIENT : " + nombre);

            } catch (IOException e) {
                cerrar(socket, entrada, salida);
            }
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {

            try {
                respuesta = entrada.readUTF();

                String mensaje = respuesta;

                if (mensaje.charAt(0) == '/') {

                    leer = mensaje.split(" ", 2);
                    leer1 = leer[0].split("/");

                    for (HiloServidor hiloServidor : hiloServidors) {
                        try {
                            if (hiloServidor.nombre.equals(leer1[1])) {
                                hiloServidor.salida.writeUTF("/" + nombre + " :" + "\n" + leer[1]);
                            }
                        } catch (IOException e) {
                            cerrar(socket, entrada, salida);
                        }
                    }
                } else {
                    for (HiloServidor hiloServidor : hiloServidors) {
                        try {
                            if (!hiloServidor.nombre.equals(nombre)) {

                                hiloServidor.salida.writeUTF(nombre + " :" + "\n" + respuesta);
                            }
                        } catch (IOException e) {
                            cerrar(socket, entrada, salida);
                        }
                    }
                }
            } catch (IOException e) {
                cerrar(socket, entrada, salida);
                break;
            }

        }
    }

    public void eliminar() {
        Conexion conexion = new Conexion();
        conexion.online(0, nombre);
        for (HiloServidor hiloServidor : hiloServidors) {
            try {
                System.out.println("envio del Hiloservidor : " + nombre);
                hiloServidor.salida.writeUTF("ONLINECLIENT :" + nombre + " desconectado.");

            } catch (IOException e) {
            }
        }
        hiloServidors.remove(this);
    }

    public void cerrar(Socket socket, DataInputStream entrada, DataOutputStream salida) {
        eliminar();
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
