package imat.cartmenu;

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

public class CartsMenuController implements Initializable {
    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    private List<ItemCart> carts = new ArrayList<>();

    private List<ItemCart> getData() {
        List<ItemCart> carts = new ArrayList<>();
        ItemCart cart;

        for (int i=0; i<20; i++) {
            cart = new ItemCart();
            cart.setName("Varukorg " + i);
            cart.setDate("18/5-22");
            carts.add(cart);
        }

        return carts;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carts.addAll(getData());
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < carts.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item_cart.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();


                ItemCartController itemCartController = fxmlLoader.getController();
                itemCartController.setData(carts.get(i));

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
