package imat.basket;

import imat.Anchorable;
import imat.FxRoot;
import imat.browse.FxBrowse;
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
    public FxRoot parentFx;
    private FxBrowse browse;

    @FXML public AnchorPane basketPane;
    @FXML private AnchorPane browseLightBox;
    @FXML private AnchorPane browsepane;
    @FXML private Label basketName;
    @FXML private Button newBasketButton;
    @FXML private FlowPane productList;
    @FXML private Button mySavedBasketsButton;
    @FXML private Label savedBasketLabel;
    @FXML private AnchorPane apNewBasketSaveWarning;

    @FXML public void openSaveView () {
        apNewBasketSaveWarning.toFront();
    }
    @FXML public void closeSaveView () {
        apNewBasketSaveWarning.toBack();
    }

    public FxBasket(FxRoot parentFx) {
        this.parentFx = parentFx;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("basket.fxml"));
        fxmlLoader.setController(this);

        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        browse = new FxBrowse(this);
        browsepane.getChildren().add(browse.getAnchor());

        clearBasket();
    }
    private void setBasketName(String name){
        basketName.setText(name);
    }
    @FXML private void openBasket(){ // need a name?
        savedBasketLabel.setText("Saved!");
        FxRoot.getCart();
    }
    @FXML private void saveBasket(){
        savedBasketLabel.setText("Saved!");
        String name = "hej";
        setBasketName(name);
        closeSaveView();
        //save shoppingcart somehow
    }
    public void addItemToBasket(ShoppingItem item){
        savedBasketLabel.setText("");
        FxRoot.getCart().addItem(item);
        updateBasket();
    }
    public void removeItemFromBasket(ShoppingItem item){
        savedBasketLabel.setText("");
        FxRoot.getCart().removeItem(item);
        updateBasket();
    }
    @FXML private void clearBasket(){
        savedBasketLabel.setText("");
        //checks if user has saved and such
        FxRoot.getCart().clear();
        setBasketName("Ny varukorg");
    }
    public void updateBasket(){
        List<ShoppingItem> basketItems = FxRoot.getCart().getItems();
        productList.getChildren().clear();
        for (ShoppingItem product : basketItems) {
            int prodId = product.getProduct().getProductId(                                                                            );
            productList.getChildren().add(FxRoot.getProdListItem(prodId));
        }
    }

    @FXML protected void onButtonToCheckout(){
        parentFx.fxCheckout.openCheckout();
        parentFx.checkoutPane.toFront();
    }
    @FXML protected void onButtonToSavedCarts(){
        parentFx.savedBasketsPane.toFront();
    }
    @FXML protected void onButtonBrowse(){
        browseLightBox.toFront();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
