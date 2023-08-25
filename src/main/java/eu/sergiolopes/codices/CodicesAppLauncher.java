/**
 * MIT License
 * <p>
 * Copyright (c) 2023 Sérgio Lopes
 * https:www.sergiolopes.eu, knitter.is@gmail.com
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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