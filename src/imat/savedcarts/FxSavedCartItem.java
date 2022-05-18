package imat.savedcarts;

import imat.Anchorable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FxSavedCartItem extends AnchorPane implements Anchorable {
    private AnchorPane anchorPane;
    private SavedCart savedCart;

    @FXML private Label nameLabel;
    @FXML private Label dateLabel;
    @FXML private Button openButton;
    @FXML private Button removeButton;

    public FxSavedCartItem(SavedCart savedCart) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("savedcartitem.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.savedCart = savedCart;
        nameLabel.setText(savedCart.getName());
        dateLabel.setText(savedCart.getDate());
    }

    @Override
    public AnchorPane getAnchor() {
        return null;
    }
}
