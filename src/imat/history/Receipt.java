package imat.history;

import javafx.scene.layout.AnchorPane;

public class Receipt extends AnchorPane {
    //private Order order;
    private String[] receipt;
    private String listName;
    private String date;

    public void setStringList(String[] list){
        this.receipt = list;
    }
    public void setReceiptDate(String buyDate){
        this.date = buyDate;
    }
    public void setListName(String name){
        this.listName = name;
    }
    public String getProductNames() {
        return receipt[0];
    }
    public String getCostValues(){
        return receipt[1];
    }
    public String getReceiptDate() {
        return date;
    }
    public String getReceiptListName() {
        return listName;
    }
    public String getTotal() {
        return receipt[2];
    }

}
