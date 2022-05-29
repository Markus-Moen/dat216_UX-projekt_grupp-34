package imat.savedcarts;

import imat.Anchorable;
import imat.basket.FxBasket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FxSavedCartItem extends AnchorPane implements Anchorable {
    private AnchorPane anchorPane;
    private SavedCart savedCart;
    private FxSavedCarts parentController;
    private FxBasket fxBasket;

    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private Button openButton;
    @FXML private Button removeButton;

    public FxSavedCartItem(SavedCart savedCart, FxSavedCarts parentController, FxBasket fxBasket) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("savedcartitem.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.savedCart = savedCart;
        this.fxBasket = fxBasket;
        this.parentController = parentController;
        nameLabel.setText(savedCart.getName());
        dateLabel.setText(savedCart.getDate());
    }

    @FXML
    protected void removeButtonPressed() {
        parentController.removeCart(this.savedCart);
    }

    @FXML protected void openButtonPressed() {
        fxBasket.loadShoppingItems(this.savedCart);
        fxBasket.focus();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }
}
