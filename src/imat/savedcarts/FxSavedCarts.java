package imat.savedcarts;

import imat.Anchorable;
import imat.basket.FxBasket;
import imat.data.NamedCart;
import imat.data.NamedCartExtraData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class FxSavedCarts implements Anchorable, Initializable {
    AnchorPane anchorPane;
    FxBasket fxBasket;

    @FXML private GridPane grid;
    @FXML private ScrollPane scroll;

    private List<NamedCart> savedCarts = new ArrayList<>();
    public void setSavedCarts(List<NamedCart> savedCarts) {
        this.savedCarts = savedCarts;
    }

    public List<NamedCart> getSavedCarts() {
        return savedCarts;
    }

    public FxSavedCarts(FxBasket fxBasket){
        this.fxBasket = fxBasket;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("savedcarts.fxml"));
        fxmlLoader.setController(this);

        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        openSavedCarts();
    }

    @FXML protected void onButtonReturn(){
        fxBasket.focus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    public void openSavedCarts(){
        savedCarts = new ArrayList<>();
        for(var x : fxBasket.iMatData.getSavedCarts()){
            savedCarts.add(x);
        }
        updateCartList();
    }

    public void removeCart(NamedCart savedCart) {
        clearCartList();

        System.out.println("Before: " + savedCarts.size());
        this.savedCarts.remove(savedCart);
        System.out.println("After: " + savedCarts.size());

        updateCartList();
    }

    public void addNewCart(NamedCart savedCart) {
        savedCarts.add(savedCart);
        updateCartList();
    }

    private void clearCartList() {
        grid.getChildren().clear();
    }

    public void updateCartList() {
        int column = 0;
        int row = 1;
        System.out.println("upfatyer " + savedCarts.size());
        for (int i = 0; i < savedCarts.size(); i++) {
            FxSavedCartItem savedCartItem = new FxSavedCartItem(savedCarts.get(i), this, fxBasket);
            AnchorPane itemAnchorPane = savedCartItem.getAnchor();

            if (column == 3) {
                column = 0;
                row++;
            }


            grid.add(itemAnchorPane, column++, row); //(child,column,row)
            //set grid width
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);

            //set grid height
            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(itemAnchorPane, new Insets(10));
        }
    }
}
