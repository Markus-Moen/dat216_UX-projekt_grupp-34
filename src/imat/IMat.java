package imat;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import net.lingala.zip4j.ZipFile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class IMat extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("imat/resources/IMat");

        maybeInitializeDB();

        Parent root = FXMLLoader.load(getClass().getResource("fx/imat.fxml"), bundle);

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        stage.show();
    }

    public void maybeInitializeDB() throws IOException {
        String userdir = System.getProperty("user.home");
        Path datDir = Path.of(userdir+"/.dat215/imat");
        var dbZip = Paths.get(datDir + "/db.zip");

        if (Files.exists(datDir)) return;

        var is = getClass().getResourceAsStream("resources/initial_database.zip");
        Files.createDirectories(datDir);
        Files.copy(is, dbZip);
        ZipFile zipFile = new ZipFile(dbZip.toFile());
        zipFile.extractAll(datDir.toString());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
