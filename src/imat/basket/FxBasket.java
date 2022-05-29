package imat.basket;

import imat.data.IMatData;
import imat.browse.FxBrowse;
import imat.checkout.FxCheckout;
import imat.productlist.FxProductItem;
import imat.savedcarts.FxSavedCarts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import se.chalmers.cse.dat216.project.ShoppingItem;

//import imat.ProductItem;

public class FxBasket implements Initializable {
    public static FxBasket INSTANCE;

    public IMatData iMatData;
    private FxBrowse fxBrowse;
    private FxSavedCarts fxSavedCarts;
    private FxCheckout fxCheckout;

    @FXML private AnchorPane stackBasket;
    @FXML private AnchorPane stackBrowse;
    @FXML private AnchorPane stackSavedCarts;
    @FXML private AnchorPane stackCheckout;
    @FXML private AnchorPane stackHistory;

    @FXML private AnchorPane browsePaneContent;
    @FXML private AnchorPane savedCartsPaneContent;

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

    public void focus(){
        stackBasket.toFront();
    }
    private void setBasketName(String name){
        basketName.setText(name);
    }
    @FXML private void openBasket(){ // need a name?
        savedBasketLabel.setText("Saved!");
        iMatData.getCart();
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
        iMatData.getCart().addItem(item);
        updateBasket();
    }
    public void removeItemFromBasket(ShoppingItem item){
        savedBasketLabel.setText("");
        iMatData.getCart().removeItem(item);
        updateBasket();
    }
    @FXML private void clearBasket(){
        savedBasketLabel.setText("");
        //checks if user has saved and such
        iMatData.getCart().clear();
        setBasketName("Ny varukorg");
    }
    public void updateBasket(){
        List<ShoppingItem> basketItems = iMatData.getCart().getItems();
        System.out.println("UPDATE BASKET:"+basketItems.size());
        productList.getChildren().clear();
        for (ShoppingItem shoppingItem : basketItems) {
            int prodId = shoppingItem.getProduct().getProductId();
            FxProductItem fxProd = iMatData.getProdListItem(prodId);
            productList.getChildren().add(fxProd.getAnchor());
        }
    }

    @FXML protected void onButtonToCheckout(){
        fxCheckout.openCheckout();
        stackCheckout.toFront();
    }
    @FXML protected void onButtonToSavedCarts(){
        stackSavedCarts.toFront();
    }
    @FXML protected void onButtonBrowse(){
        fxBrowse.openBrowse();
        stackBrowse.toFront();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(INSTANCE != null){
            throw new ExceptionInInitializerError("FxBasket already exits");
        }
        INSTANCE = this;
        iMatData = new IMatData(this);

        fxBrowse = new FxBrowse(this);
        browsePaneContent.getChildren().add(fxBrowse.getAnchor());
        fxSavedCarts = new FxSavedCarts(this);
        savedCartsPaneContent.getChildren().add(fxSavedCarts.getAnchor());
        fxCheckout = new FxCheckout(this);
        stackCheckout.getChildren().add(fxCheckout.getAnchor());

        clearBasket();
        System.out.println("IS IT DONE?");
    }

    public void shutDown(){
        iMatData.shutDown();
    }
}
