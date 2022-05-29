package imat.savedcarts;

import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.util.List;

public class SavedCart extends AnchorPane {
    private String name;
    private String date = "20/4";
    private List<ShoppingItem> shoppingItems;

    public SavedCart(List<ShoppingItem> shoppingItems, String name) {
        this.name = name;
        this.shoppingItems = shoppingItems;
    }

    public List<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
