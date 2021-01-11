package com.github.gaijinkindred.AppointmentManager.Backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import com.github.gaijinkindred.AppointmentManager.Main;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DatabaseConnector {

    private static DatabaseConnector INSTANCE;
    public static DatabaseConnector getInstance() {
        return INSTANCE == null ? INSTANCE = new DatabaseConnector() : INSTANCE;
    }

    private Connection connection;

    public DatabaseConnector() {
        try {
            //Reading from a file now...
            File file = new File("src/main/java/config.txt");
            Scanner scan = new Scanner(file);
            String url = "";
            String tableName = "";
            String username = "";
            String password = "";
            String[] spl;
            while(scan.hasNextLine()) {
                spl = scan.nextLine().split(":");
                if(spl[0].toLowerCase().contains("url")) {
                    url = spl[1];
                } else if(spl[0].toLowerCase().contains("username")) {
                    username = spl[1];
                } else if(spl[0].toLowerCase().contains("table")) {
                    tableName = spl[1];
                } else if(spl[0].toLowerCase().contains("password")) {
                    password = spl[1];
                }
            }
            scan.close();
            connection = DriverManager.getConnection("jdbc:mysql://" + url + ":3306/" + tableName, username, password);
        } catch(SQLException | FileNotFoundException e) {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Erreur de connexion à la base de données", "En quittant l'application, veuillez rouvrir avec une connexion stable au serveur.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Error de conexión a la base de datos","Salida de la aplicación, vuelva a abrir con una conexión estable al servidor.");
            } else {
                Main.newError("Database connection error", "Application Exiting, please reopen with a stable connection to the server.");
            }
        	System.exit(-404);
        }
    }

    public ResultSet request(String reqStr) {
        if(connection != null) {
            try {
                Statement statement = connection.createStatement();
                if(reqStr.contains(";")) { //Over-glorified input validation
                   throw new SQLException("Unexpected character found.");
                }
                if(statement.execute(reqStr)) {
                    return statement.getResultSet();
                }
            } catch(SQLException ex) {
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Demande", "Erreur de demande d'application, réessayez plus tard.");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Solicitud de error", "Error de solicitud de aplicación, intente nuevamente más tarde.");
                } else {
                    Main.newError("Request Error", "Application request error, try request again later.");
                }
                ex.printStackTrace();
            }
        }
        return null;
    }
}
