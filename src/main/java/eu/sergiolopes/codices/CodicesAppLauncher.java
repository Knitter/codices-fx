package eu.sergiolopes.codices;

import eu.sergiolopes.codices.view.ViewManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CodicesAppLauncher extends Application {

    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final String dbFile = System.getProperty("user.home") + System.getProperty("file.separator") + "codices.sqlite";
        //TODO: Copy base file or execute SQL to generate file
        //TODO: If not init already, ask for file location using wizard
        connection = connectSqliteFile(dbFile);
        if (connection == null) {
            //TODO: cleanup + user message & exit properly
            System.out.println("Could not load SQLite drivers or SQLite file");
            Platform.exit();
        }

        //TODO: is it worth DI?
        ViewManager manager = new ViewManager(connection);
        manager.showMainWindow();
    }

    @Override
    public void stop() throws Exception {
        if (!connection.isClosed()) {
            connection.close();
        }
        //TODO: cleanup
    }

    private Connection connectSqliteFile(String location) {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return DriverManager.getConnection("jdbc:sqlite:" + location);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(System.err);
        }

        return null;
    }
}