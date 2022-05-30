package conexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Conexion {

    private final String base = "ususarios";
    private final String user = "prueba";
    private final String password = "1234";

    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet;

    private String usuario;
    private String pass;

    private int online;

    public Connection conexion() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/usuarios", user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
        System.out.println(this.usuario);
        System.out.println(usuario);
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public boolean insertar() {
        if (!controlRegistro()) {

            try {
                Connection con = conexion();

                preparedStatement = con.prepareStatement("INSERT INTO usuario(nombre,contraseña,onilne) VALUES (?,?,?)");
                this.online = 0;
                System.out.println(this.usuario);
                System.out.println(usuario);
                preparedStatement.setString(1, this.usuario);
                preparedStatement.setString(2, this.pass);
                preparedStatement.setInt(3, this.online);
                preparedStatement.execute();
                liberar(resultSet, preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

    public void online(int online, String usuario) {

        Connection con = conexion();
        try {
            System.out.println(usuario);
            preparedStatement = con.prepareStatement("UPDATE usuario SET onilne = ? WHERE nombre = ?");
            preparedStatement.setInt(1, online);
            preparedStatement.setString(2, usuario);

            preparedStatement.executeUpdate();
            liberar(resultSet, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Conexion> usuariosOnline() {

        ArrayList<Conexion> conectados = new ArrayList<>();

        try {
            Connection con = conexion();
            preparedStatement = con.prepareStatement("SELECT nombre ,onilne FROM  usuario WHERE onilne = ?");
            preparedStatement.setInt(1, 1);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Conexion conexion = new Conexion();
                conexion.setUsuario(resultSet.getString(1));
                conexion.setOnline(resultSet.getInt(2));
                conectados.add(conexion);
            }
            liberar(resultSet, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conectados;
    }

    public boolean controlRegistro() {
        List<String> comprobar = new ArrayList<String>();
        try {
            Connection con = conexion();
            preparedStatement = con.prepareStatement("SELECT * FROM  usuario WHERE nombre= ? ");

            preparedStatement.setString(1, usuario);


            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                comprobar.add(resultSet.getString(2));
            }
            System.out.println(comprobar);
            liberar(resultSet, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (comprobar.size() >= 1) {

            return true;
        } else {
            return false;
        }
    }

    public Boolean login() throws SQLException {

        List<String> comprobar = new ArrayList<String>();
        try {
            Connection con = conexion();
            preparedStatement = con.prepareStatement("SELECT * FROM  usuario WHERE nombre= ? AND contraseña = ?");

            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, pass);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                comprobar.add(resultSet.getString(2));
                comprobar.add(resultSet.getString(3));
            }
            System.out.println(comprobar);
            liberar(resultSet, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (comprobar.size() >= 1) {

            return true;
        } else {
            return false;
        }
    }

    public void liberar(ResultSet resultSet, PreparedStatement preparedStatement) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
