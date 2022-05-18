package imat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ItemCartController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Button openButton;

    @FXML
    private Button removeButton;

    private ItemCart itemCart;

    public void setData(ItemCart itemCart) {
        this.itemCart = itemCart;

        nameLabel.setText(itemCart.getName());
        dateLabel.setText(itemCart.getDate());

    }


}
