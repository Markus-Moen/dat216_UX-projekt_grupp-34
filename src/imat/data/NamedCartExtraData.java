package imat.data;

import se.chalmers.cse.dat216.project.Order;

import java.util.List;

public class NamedCartExtraData {
    private boolean isBought;
    private String name;
    private Integer id;

    public NamedCartExtraData(){
    }
    public NamedCartExtraData(NamedCartExtraData nc){
        this.isBought = nc.getIsBought();
        this.name = nc.getName();
        this.id = nc.getId();
    }
    public NamedCartExtraData(boolean isBought, String name, Integer id){
        this.isBought = isBought;
        this.name = name;
        this.id = id;
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
    public Order reverseGetOrder(List<Order> orders, int id){
        return orders.stream().filter(x -> x.getOrderNumber() == id).findFirst().orElseThrow();
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
