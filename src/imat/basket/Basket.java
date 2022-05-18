package imat.basket;

import imat.Anchorable;
import imat.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;

//import imat.ProductItem;

public class Basket extends AnchorPane implements Anchorable, Initializable {
    private AnchorPane anchorPane;
    private Controller parentController;

    @FXML private Label basketName;
    @FXML private Button newBasketButton;
    @FXML private FlowPane productList;
    @FXML private Button mySavedBasketsButton;

    public Basket(Controller parentController) {
        this.parentController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("basket.fxml"));
        //fxmlLoader.setController(null);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        clearBasket();
    }

    private void setBasketName(String name){
        //basketName.setText(name);
    }
    private void openBasket(){ // need a name?
        Controller.getCart();
    }
    private void saveBasket(String name){
        setBasketName(name);
        //save shoppingcart somehow
    }
    public void addItemToBasket(ShoppingItem item){
        Controller.getCart().addItem(item);
        updateBasket();
    }
    public void removeItemFromBasket(ShoppingItem item){
        Controller.getCart().removeItem(item);
        updateBasket();
    }
    private void clearBasket(){
        //checks if user has saved and such
        Controller.getCart().clear();
        setBasketName("Ny varukorg");
    }
    public void updateBasket(){
        List<ShoppingItem> basketItems = Controller.getCart().getItems();
        productList.getChildren().clear();
        for (ShoppingItem product : basketItems) {
            int prodId = product.getProduct().getProductId();
            productList.getChildren().add(Controller.getProdListItem(prodId));
        }
    }

    @FXML protected void onButtonToCheckout(){
        parentController.checkoutPane.toFront();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
