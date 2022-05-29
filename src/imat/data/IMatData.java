package imat.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import imat.basket.FxBasket;
import imat.productlist.FxProductItem;
import io.vavr.Tuple2;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;
import se.chalmers.cse.dat216.project.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IMatData {
    private static IMatData INSTANCE;
    private IMatDataHandler imat;
    private List<Product> prods;
    private HashMap<Integer, FxProductItem> id2prodListItem;

    private ExCart exCart;

    private List<Order> orderHistory;
    private List<SavedCart> savedCarts;

    private @Nullable Integer cartId;

    public IMatData(FxBasket fxBasket){
        if(INSTANCE != null){
            throw new ExceptionInInitializerError("IMatData already exists");
        }
        imat = IMatDataHandler.getInstance();
        prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");

        exCart = new ExCart(imat.imatDirectory()+"/exCartData.json");


        id2prodListItem = new HashMap<>();
        for(Product p : getAllProducts()){
            var newSp = new ShoppingItem(p);
            newSp.setAmount(0);
            FxProductItem fxProductItem = new FxProductItem(newSp, fxBasket, this);
            id2prodListItem.put(p.getProductId(), fxProductItem);
        }

        INSTANCE = this;
    }
    public List<Order> getOrders(){
        return imat.getOrders().stream().filter(x -> x.getOrderNumber() >= 0).toList();
    }
    public List<Order> getSavedCarts(){
        return imat.getOrders().stream().filter(x -> x.getOrderNumber() < 0).toList();
    }
    public void orderCart(){
        imat.placeOrder(true);
    }

    public void loadCart(int id){ //FORCE REPLACE
    }
    public void saveCart(int id){
        exCart.editSavedCart(id, imat.getOrders());
    }
    public int saveActiveCartAs(String name){
        Order x = exCart.cartAsOrder(imat.getShoppingCart(), name);
        imat.getOrders().add(x);
        return x.getOrderNumber();
    }

    public ShoppingCart getCart(){
        return imat.getShoppingCart();
    }
    public List<Product> getAllProducts(){
        return prods;
    }
    public Collection<FxProductItem> getAllProdListItems(){
        return id2prodListItem.values();
    }
    public FxProductItem getProdListItem(int i){
        if(id2prodListItem.containsKey(i) == false)
            throw new RuntimeException("Item does not exist");
        return id2prodListItem.get(i);
    }
    public Image getProductImage(Product prod){
        return imat.getFXImage(prod);
    }

    public List<Integer> getFilteredProductIds(@Nullable ProductFilter productOrder){
        var matchScoreFunc = new Function<Product, Tuple2<Integer, Product>>(){
            @Override
            public Tuple2<Integer, Product> apply(Product product) {
                if (productOrder == null){
                    return new Tuple2<>(1, product);
                } else {
                    return new Tuple2<>(productOrder.howGoodMatch(product), product);
                }
            }
        };
        Stream<Tuple2<Integer, Product>> scored = prods.stream().map(matchScoreFunc);
        Stream<Product> filteredAndOrdered = scored
                .filter(x -> x._1 > 0)
                .sorted((Comparator.comparingInt(
                        (Tuple2<Integer, Product> t) -> t._1)).reversed()
                )
                .map(x -> x._2);
        Stream<Integer> idOrdered = filteredAndOrdered.map(Product::getProductId);
        return idOrdered.collect(Collectors.toList());
    }

    public String[] receipt(){
        String[] output = new String[] {"", "", ""};
        ShoppingCart cart = getCart();
        List<ShoppingItem> items = cart.getItems();
        for (ShoppingItem item : items){
            output[0] += Math.round(item.getAmount()) + " " + item.getProduct().getName() + "\n";
            output[1] += item.getTotal() + "\n";
        }

        output[2] = Double.toString(cart.getTotal());

        return output;
    }

    public void shutDown() {
        System.out.println("SHUTTING DOWN");
        imat.shutDown();
        exCart.write();
    }
}
