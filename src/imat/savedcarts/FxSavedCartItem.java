package imat.savedcarts;

import imat.Anchorable;
import imat.basket.FxBasket;
import imat.data.NamedCart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FxSavedCartItem extends AnchorPane implements Anchorable {
    private AnchorPane anchorPane;
    private NamedCart namedCart;
    private FxSavedCarts fxSavedCarts;
    private FxBasket fxBasket;

    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private Button openButton;
    @FXML private Button removeButton;

    public FxSavedCartItem(NamedCart namedCart, FxSavedCarts fxSavedCarts, FxBasket fxBasket) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("savedcartitem.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.namedCart = namedCart;
        this.fxBasket = fxBasket;
        this.fxSavedCarts = fxSavedCarts;
        nameLabel.setText(namedCart.getName());
        dateLabel.setText(namedCart.order.getDate().toString());
    }

    @FXML
    protected void removeButtonPressed() {
        fxSavedCarts.removeCart(namedCart);
    }

    @FXML protected void openButtonPressed() {
        fxBasket.moveSavedCartToActiveCart(namedCart.getId());
        fxBasket.focus();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }
}
