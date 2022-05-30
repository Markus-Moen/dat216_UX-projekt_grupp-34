package imat.basket;

import imat.data.IMatData;
import imat.browse.FxBrowse;
import imat.checkout.FxCheckout;
import imat.history.FxHistory;
import imat.productlist.FxProductItem;
import imat.savedcarts.FxSavedCarts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.ShoppingCartListener;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

//import imat.ProductItem;

public class FxBasket implements Initializable {
    public IMatData iMatData;
    private FxBrowse fxBrowse;
    private FxSavedCarts fxSavedCarts;
    private FxCheckout fxCheckout;
    private ShoppingCartListener shoppingCartListener;
    private boolean basketIsSaved = false;
    private boolean basketIsVirgin = true;

    private FxHistory fxHistory;

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
    @FXML private AnchorPane apNameBasket;
    @FXML private StackPane spSaveStack;

    @FXML private TextField basketNameInput;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane scrollContent;

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
    public void placeOrder(){
        System.out.println("SAVING CART");
        iMatData.orderAndClearActiveCart();
        iMatData.DEBUG_saveCarts();
    }

    private void unSavedWarning() {
        spSaveStack.toFront();
        apNewBasketSaveWarning.toFront();
    }

    public void moveSavedCartToActiveCart(int id) {
        String name = iMatData.moveSavedCartToActiveCart(id);
        basketName.setText(name);
    }
    private void clearBasket(){
        savedBasketLabel.setText("");
        //checks if user has saved and such
        iMatData.clearActiveCart();
        setBasketName("Ny varukorg");
    }

    public void updateBasket(){
        List<ShoppingItem> basketItems = iMatData.getActiveCart().getItems();
        System.out.println("UPDATE BASKET:"+basketItems.size());
        productList.getChildren().clear();
        for (ShoppingItem shoppingItem : basketItems) {
            int prodId = shoppingItem.getProduct().getProductId();
            FxProductItem fxProd = iMatData.getProdListItem(prodId);
            Platform.runLater(() -> {
                productList.getChildren().add(fxProd.getAnchor());
            });
        }
    }
    @FXML private void unsavedChangesCancelButtonPressed() {
        System.out.println("TODO");
    }
    @FXML private void unsavedChangesSaveButtonPressed() {
        if (basketIsVirgin) {
            apNameBasket.toFront();
        }
        else {
            saveButtonPressed();
        }
        spSaveStack.toBack();
        iMatData.clearActiveCart();
        basketName.setText("Ny varukorg");
        savedBasketLabel.setText("");
        basketIsVirgin = true;
        basketIsSaved = false;
    }
    @FXML private void unsavedChangesNoSaveButtonPressed() {
        spSaveStack.toBack();
        iMatData.clearActiveCart();
        basketName.setText("Ny varukorg");
        savedBasketLabel.setText("");
        basketIsVirgin = true;
        basketIsSaved = false;
    }

    @FXML public void saveAsConfirmButtonPressed() {
        String name = basketNameInput.getText();
        List<ShoppingItem> basketItems = new ArrayList<ShoppingItem>(iMatData.getActiveCart().getItems());

        iMatData.saveActiveCartAsNew(name, false);
        setBasketName(name);
        spSaveStack.toBack();

        System.out.println("New basket is saved!");
        savedBasketLabel.setText("Saved!");
        basketIsSaved = true;
        basketIsVirgin = false;
    }
    @FXML public void saveButtonPressed() {
        iMatData.saveCurrentyActiveCart();
        System.out.println("Saved!");
        savedBasketLabel.setText("Saved!");
    }
    @FXML public void saveAsButtonPressed() {
        System.out.println("Enter name");
        spSaveStack.toFront();
        apNameBasket.toFront();
    }
    @FXML public void newCartButtonPressed() {
        if (basketIsSaved) {
            spSaveStack.toBack();
            iMatData.clearActiveCart();
            basketName.setText("Ny varukorg");
            savedBasketLabel.setText("");
            basketIsVirgin = true;
            basketIsSaved = false;
        }
        else {
            unSavedWarning();
        }
    }
    @FXML protected void onButtonToCheckout(){
        fxCheckout.openCheckout();
        stackCheckout.toFront();
    }
    @FXML protected void onButtonToSavedCarts(){
        fxSavedCarts.openSavedCarts();
        stackSavedCarts.toFront();
    }
    @FXML protected void onButtonBrowse(){
        stackBrowse.toFront();
        fxBrowse.openBrowse();
    }
    @FXML protected void onButtonHistory(){
        fxHistory.openHistory();
        stackHistory.toFront();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iMatData = new IMatData(this);

        fxBrowse = new FxBrowse(this);
        browsePaneContent.getChildren().add(fxBrowse.getAnchor());
        fxSavedCarts = new FxSavedCarts(this);
        savedCartsPaneContent.getChildren().add(fxSavedCarts.getAnchor());
        fxCheckout = new FxCheckout(this);
        stackCheckout.getChildren().add(fxCheckout.getAnchor());
        fxHistory = new FxHistory(this);
        stackHistory.getChildren().add(fxHistory.getAnchor());

        scrollContent.prefWidthProperty().bind(scrollPane.widthProperty());

        basketIsSaved = true;
        basketIsVirgin = true;
        iMatData.getActiveCart().addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {
                basketIsSaved = false;
                basketIsVirgin = false;
                savedBasketLabel.setText("");
                updateBasket();
            }
        });

        clearBasket();
        System.out.println("IS IT DONE?");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutDown()));
    }

    //protected void addReceiptToHistory(){
    //    fxHistory.addReceipt();
    //}
    public void shutDown(){
        System.out.println("SHUT DOWN 1");
        iMatData.shutDown();
    }
}
