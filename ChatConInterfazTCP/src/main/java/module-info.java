module com.example.chatconinterfaztcp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.chatconinterfaztcp to javafx.fxml;
    exports com.example.chatconinterfaztcp;
    exports controllers;
    opens controllers to javafx.fxml;
    exports conexionDB;
    opens conexionDB to javafx.fxml;
}