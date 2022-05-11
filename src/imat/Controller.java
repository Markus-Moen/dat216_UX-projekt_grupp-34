package imat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import se.chalmers.cse.dat216.project.*;

import java.util.ResourceBundle;

public class Controller {
    public static IMatDataHandler imat;

    public static void initialize(){
        imat = IMatDataHandler.getInstance();
        var prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");
    }

    public static void makeServerAssets(){

    }
}
