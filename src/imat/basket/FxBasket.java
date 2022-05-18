package imat.basket;

import imat.Anchorable;
import imat.FxRoot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import se.chalmers.cse.dat216.project.ShoppingItem;

//import imat.ProductItem;

public class FxBasket extends AnchorPane implements Anchorable, Initializable {
    private AnchorPane anchorPane;
    private FxRoot parentFx;

    @FXML private Label basketName;
    @FXML private Button newBasketButton;
    @FXML private FlowPane productList;
    @FXML private Button mySavedBasketsButton;

    public FxBasket(FxRoot parentFx) {
        this.parentFx = parentFx;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("basket.fxml"));
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
        FxRoot.getCart();
    }
    private void saveBasket(String name){
        setBasketName(name);
        //save shoppingcart somehow
    }
    public void addItemToBasket(ShoppingItem item){
        FxRoot.getCart().addItem(item);
        updateBasket();
    }
    public void removeItemFromBasket(ShoppingItem item){
        FxRoot.getCart().removeItem(item);
        updateBasket();
    }
    private void clearBasket(){
        //checks if user has saved and such
        FxRoot.getCart().clear();
        setBasketName("Ny varukorg");
    }
    public void updateBasket(){
        List<ShoppingItem> basketItems = FxRoot.getCart().getItems();
        productList.getChildren().clear();
        for (ShoppingItem product : basketItems) {
            int prodId = product.getProduct().getProductId();
            productList.getChildren().add(FxRoot.getProdListItem(prodId));
        }
    }

    @FXML protected void onButtonToCheckout(){
        parentFx.checkoutPane.toFront();
    }
    @FXML protected void onButtonToSavedCarts(){
        parentFx.savedBasketsPane.toFront();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
