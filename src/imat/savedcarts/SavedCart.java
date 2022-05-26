package imat.savedcarts;

import javafx.scene.layout.AnchorPane;

import java.util.List;

public class SavedCart extends AnchorPane {
    private String name;
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
