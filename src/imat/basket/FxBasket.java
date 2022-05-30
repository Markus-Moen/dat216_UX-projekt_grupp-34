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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.layout.HBox;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.ShoppingCartListener;
import se.chalmers.cse.dat216.project.ShoppingItem;

//import imat.ProductItem;

public class FxBasket implements Initializable {
    public IMatData iMatData;
    private FxBrowse fxBrowse;
    private FxSavedCarts fxSavedCarts;
    private FxCheckout fxCheckout;
    private ShoppingCartListener shoppingCartListener;


    private boolean basketHasUnsavedChanges;
    private boolean basketShouldPromptBeforeBeingOverwritten;

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
    @FXML private HBox hBoxSaveStack;

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

    public void unSavedWarning() {
        apNewBasketSaveWarning.toFront();
        hBoxSaveStack.toFront();
    }

    public enum IgnoreLocalChanges{
        YES, NO, CANCEL;
    }
    public IgnoreLocalChanges ignoreLocalChangesPrompt(){
        if(basketHasUnsavedChanges && basketShouldPromptBeforeBeingOverwritten){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Du har osparade ändringar");
            alert.setHeaderText("Du har osparade ändringar");
            alert.setContentText("Din varukorg har osparade ändringar, Vill du spara dessa?");

            ButtonType buttonTypeSave = new ButtonType("Ja, Spara");
            ButtonType buttonTypeNoSave = new ButtonType("Nej, Spara Inte");
            ButtonType buttonTypeCancel = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNoSave, buttonTypeCancel);

            Optional<ButtonType> option = alert.showAndWait();
            if(option.isEmpty()) return IgnoreLocalChanges.CANCEL;
            ButtonType bt = option.get();
            if(bt == buttonTypeCancel) return IgnoreLocalChanges.CANCEL;
            if(bt == buttonTypeNoSave) return IgnoreLocalChanges.YES;
            if(bt == buttonTypeSave) return IgnoreLocalChanges.NO;
        }
        return IgnoreLocalChanges.YES;
    }

    public void moveSavedCartToActiveCart(int id) {
        switch (ignoreLocalChangesPrompt()) {
            case YES -> {
            }
            case NO -> {
                saveButtonPressed();
            }
            case CANCEL -> {
                return;
            }
        }

        String name = iMatData.moveSavedCartToActiveCart(id);
        basketName.setText(name);

        basketHasUnsavedChanges = false;
        basketShouldPromptBeforeBeingOverwritten = true;
    }
    private void clearAndCreateNewBasket() {
        switch (ignoreLocalChangesPrompt()) {
            case YES -> {
            }
            case NO -> {
                saveButtonPressed();
            }
            case CANCEL -> {
                return;
            }
        }

        hBoxSaveStack.toBack();
        iMatData.clearActiveCart();
        basketName.setText("Ny varukorg");
        savedBasketLabel.setText("");

        basketHasUnsavedChanges = false;
        basketShouldPromptBeforeBeingOverwritten = false;
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
    @FXML private void cancelButtonPressed() {
        hBoxSaveStack.toBack();
    }
    @FXML private void warning_SaveButtonPressed() {
        if (basketHasUnsavedChanges) {
            apNameBasket.toFront();
        }
        else {
            saveButtonPressed();
        }
        clearAndCreateNewBasket();

    }
    @FXML private void warning_NoSaveButtonPressed() {
        clearAndCreateNewBasket();
    }

    @FXML public void saveAsConfirmButtonPressed() {
        String name = basketNameInput.getText();
        List<ShoppingItem> basketItems = new ArrayList<ShoppingItem>(iMatData.getActiveCart().getItems());

        iMatData.saveActiveCartAsNew(name, false);
        setBasketName(name);
        hBoxSaveStack.toBack();

        System.out.println("New basket is saved!");
        savedBasketLabel.setText("Saved!");
        basketHasUnsavedChanges = false;
        basketShouldPromptBeforeBeingOverwritten = true;
    }
    @FXML public void saveButtonPressed() {
        iMatData.saveCurrentyActiveCart();
        System.out.println("Saved!");
        savedBasketLabel.setText("Saved!");
    }
    @FXML public void saveAsButtonPressed() {
        System.out.println("Enter name");
        hBoxSaveStack.toFront();
        apNameBasket.toFront();
    }
    @FXML public void newCartButtonPressed() {
        clearAndCreateNewBasket();
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
        //fxHistory.openHistory();
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

        iMatData.getActiveCart().addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {
                basketHasUnsavedChanges = true;
                savedBasketLabel.setText("");
                updateBasket();
            }
        });

        clearAndCreateNewBasket();
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
