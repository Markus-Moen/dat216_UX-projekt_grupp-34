package imat.savedcarts;

import imat.Anchorable;
import imat.FxRoot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FxSavedCarts implements Anchorable, Initializable {
    AnchorPane anchorPane;
    FxRoot parentFx;

    @FXML private GridPane grid;

    @FXML private ScrollPane scroll;

    private List<SavedCart> savedCarts = new ArrayList<>();

    private List<SavedCart> getData() {
        List<SavedCart> carts = new ArrayList<>();
        SavedCart cart;

        for (int i=0; i<20; i++) {
            cart = new SavedCart();
            cart.setName("Varukorg " + i);
            cart.setDate("18/5-22");
            carts.add(cart);
        }

        return carts;
    }

    public FxSavedCarts(FxRoot parentFx){
        this.parentFx = parentFx;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("savedcarts.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        int column = 0;
        int row = 1;
        for (int i = 0; i < savedCarts.size(); i++) {
            FxSavedCartItem savedCartItem = new FxSavedCartItem(savedCarts.get(i));
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

    @FXML protected void onButtonReturn(){
        parentFx.basketPane.toFront();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }
}
