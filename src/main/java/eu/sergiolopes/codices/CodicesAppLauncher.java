/*
 * Codices-fx, personal library manager (ebooks, audio & paper books).
 * Copyright (C) 2023  Sérgio Lopes, https:www.sergiolopes.eu, knitter.is@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.sergiolopes.codices;

import eu.sergiolopes.codices.view.ViewManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
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

    private String getSettingsFile() {
        return System.getProperty("user.home") + System.getProperty("file.separator") + ".codices.xml";
    }
}