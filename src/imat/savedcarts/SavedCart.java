package imat.savedcarts;

import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;

import java.util.List;

public class SavedCart extends AnchorPane {
    private String name;
    private Order order;
    private String date;

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
