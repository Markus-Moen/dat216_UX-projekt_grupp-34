package imat.basket;

import imat.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;

//import imat.ProductItem;

public class Basket extends AnchorPane {
    
    private Controller parentController;

    @FXML private Label basketName;
    @FXML private Button newBasketButton;
    @FXML private FlowPane productList;
    @FXML private Button amountButtons;
    @FXML private Button mySavedBasketsButton;

    @FXML public void buttonToAmount() {
        amountButtons.toFront();
    }

    public Basket(Controller controller){
        parentController = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fx/basket.fxml"));
        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        newBasket();
    }

    private void newBasket(){
        //checks if user has saved and such
        parentController.getShoppingCart().clear();
        setBasketName("Ny varukorg");
    }
    private void setBasketName(String name){
        //basketName.setText(name);
    } 
    private void openBasket(){ // need a name?
        parentController.getShoppingCart(); //donno what cart this gets?
    }
    private void saveBasket(String name){
        setBasketName(name);
        //save shoppingcart somehow
    }
    private void addItemToBasket(ShoppingItem item){
        parentController.getShoppingCart().addItem(item);
        updateBasket();
    }
    public void updateBasket(){
        productList.getChildren().clear();
        List<ShoppingItem> basketItems = parentController.getShoppingCart().getItems();
        for (ShoppingItem product : basketItems) {
            int prodId = product.getProduct().getProductId();
            productList.getChildren().add(parentController.getProdListItem(prodId));
        }
    }

    private void mySavedBasketsButtonClicked() {

    }

}
