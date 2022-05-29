package imat.data;

import se.chalmers.cse.dat216.project.Order;

public class NamedCart extends NamedCartExtraData {
    public Order order;
    public NamedCart(Order order, NamedCartExtraData nc){
        super(nc.getIsBought(), nc.getName(), nc.getId());
        this.order = order;
    }

    public Order getOrder(){
        return order;
    }
}
