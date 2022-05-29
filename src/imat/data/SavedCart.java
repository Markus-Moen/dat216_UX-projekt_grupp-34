package imat.data;

import se.chalmers.cse.dat216.project.Order;

import java.util.Date;

public class SavedCart {
    private boolean isSaved;
    private String name;
    private Order order;

    public Date setLastEdited(){
        return order.getDate();
    }
    public void setLastEdited(Date date){
        order.setDate(date);
    }
    public int getId(){
        return order.getOrderNumber();
    }


}
