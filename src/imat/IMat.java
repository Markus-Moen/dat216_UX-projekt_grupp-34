package imat;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Stack;

import imat.basket.FxBasket;
import javafx.scene.text.Font;
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

    public static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("imat/resources/IMat");
        InputStream is = this.getClass().getResourceAsStream("./resources/font/Atkinson-Hyperlegible-Regular-102.ttf");
        Font font = Font.loadFont(is,50);
        Font.loadFont(this.getClass().getResourceAsStream("./resources/font/Atkinson-Hyperlegible-Bold-102.ttf"), 50);
        System.out.println("FONT="+font);

        maybeInitializeDB();

        Parent root = FXMLLoader.load(getClass().getResource("basket/basket.fxml"), bundle);

        Scene scene = new Scene(root, 1800, 900);
        stage.setMinHeight(900);
        stage.setMaxHeight(1080);
        stage.setMinWidth(1800);
        stage.setMaxWidth(1920);
        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        stage.show();

        this.stage = stage;
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
