package imat.productlist;

import imat.Anchorable;
import imat.basket.FxBasket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class FxProductItem extends AnchorPane implements Anchorable {
    private FxBasket fxBasket;
    private ShoppingItem shoppingItem;
    private AnchorPane anchorPane;
    
    @FXML private ImageView productItemImage;
    @FXML private Label productItemName;
    @FXML private Label productItemCost;
    @FXML private TextField productItemAmount;

    @FXML private AnchorPane addPane;
    @FXML private AnchorPane amountPane;

    public FxProductItem(ShoppingItem shoppingItem, FxBasket fxBasket){
        this.shoppingItem = shoppingItem;
        this.fxBasket = fxBasket;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        Product product = shoppingItem.getProduct();
        Image img = new Image("file:" + product.getImageName());
        productItemImage.setImage(img);
        productItemName.setText(product.getName());
        productItemCost.setText(Double.toString(product.getPrice()) + " kr/kg");
        productItemAmount.setText(Double.toString(shoppingItem.getAmount()));
    }
    private void removeItemFromBasket(){
        fxBasket.removeItemFromBasket(shoppingItem);
    }
    private double setAmount(Double newAmount){
        if(newAmount <= 0){
            addPane.toFront();
            newAmount = 0.0; //Ensures amount is never less than 0
        }
        shoppingItem.setAmount(newAmount);
        productItemAmount.setText(newAmount.toString());
        return newAmount;
    }
    @FXML protected void onButtonRemoveOne(){;
        double amount = shoppingItem.getAmount();
        setAmount(amount-1);
    }
    @FXML protected void onButtonAddOne(){
        double amount = shoppingItem.getAmount();
        setAmount( amount + 1);
    }
    @FXML protected void onButtonAdd(){
        amountPane.toFront();
        onButtonAddOne();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }
}
