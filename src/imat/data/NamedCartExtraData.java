package imat.data;

import se.chalmers.cse.dat216.project.Order;

import java.util.Date;
import java.util.List;

public class NamedCartExtraData {
    private boolean isBought;
    private String name;
    private Integer id;

    public NamedCartExtraData(boolean isBought, String name, Integer id){
        this.isBought = isBought;
        this.name = name;
        this.id = id;
    }
    public Date setLastEdited(List<Order> orders){
        return getById(orders, id).getDate();
    }
    public void setLastEdited(List<Order> orders, Date date){
        getById(orders, id).setDate(date);
    }
    public boolean getIsBought(){
        return isBought;
    }
    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }
    public Order getById(List<Order> orders, int id){
        return orders.stream().filter(x -> x.getOrderNumber() == id).findFirst().orElseThrow();
    }
}
