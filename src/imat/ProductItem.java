package imat;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ProductItem extends AnchorPane {
    private Controller parentController;
    private ShoppingItem shoppingItem;
    
    @FXML private ImageView productItemImage;
    @FXML private Label productItemName;
    @FXML private Label productItemCost;
    @FXML private TextField productItemAmount;

    public ProductItem(ShoppingItem shoppingItem, Controller controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingItem = shoppingItem;
        this.parentController = controller;

        Product product = shoppingItem.getProduct();
        Image img = new Image("file:" + product.getImageName());
        productItemImage.setImage(img);
        productItemName.setText(product.getName());
        productItemCost.setText(Double.toString(product.getPrice()) + " kr/kg");
        productItemAmount.setText(Double.toString(shoppingItem.getAmount()));
    }
}
