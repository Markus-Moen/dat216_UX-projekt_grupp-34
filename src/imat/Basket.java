package imat;

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

public class Basket extends AnchorPane{
    
    private Controller parentController;
    
    public static IMatDataHandler imat;
    private ShoppingCart basket;

    @FXML private Label basketName;
    @FXML private Button newBasketButton;
    @FXML private FlowPane productList;

    private Map<String, ProductItem> productListMap = new HashMap<String, ProductItem>();
    public Basket(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fx/basket.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        imat = IMatDataHandler.getInstance();
        for (Product product : imat.getProducts()) {
            ShoppingItem shoppingItem = new ShoppingItem(product, 0);
            ProductItem productItem = new ProductItem(shoppingItem, parentController);
            productListMap.put(product.getName(), productItem);
        }
        newBasket();
    }

    private void newBasket(){
        //checks if user has saved and such
        basket.clear();
        setBasketName("Ny varukorg");
    }
    private void setBasketName(String name){
        basketName.setText(name);
    } 
    private void openBasket(){ // need a name?
        basket = imat.getShoppingCart(); //donno what cart this gets?
    }
    private void saveBasket(String name){
        setBasketName(name);
        //save shoppingcart somehow
    }
    private void addItemToBasket(ShoppingItem item){
        basket.addItem(item);
        updateBasket();
    }
    private void removeItemFromBasket(ShoppingItem item){
        basket.removeItem(item);
        updateBasket();
    }
    private void changeAmount(ShoppingItem item, Double newAmount){
        item.setAmount(newAmount);
    }
    private void removeOne(ShoppingItem item){
        double amount = item.getAmount();
        changeAmount(item, amount - 1);
    }
    private void addOne(ShoppingItem item){
        double amount = item.getAmount();
        changeAmount(item, amount + 1);
    }
    private void updateBasket(){
        productList.getChildren().clear();
        List<ShoppingItem> products = basket.getItems();
        for (ShoppingItem product : products) {
            productList.getChildren().add(productListMap.get(product.getProduct()));
        }

    }

}
