package imat.data;

import imat.basket.FxBasket;
import imat.productlist.FxProductItem;
import io.vavr.Tuple2;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;
import se.chalmers.cse.dat216.project.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IMatData {
    private static IMatData INSTANCE;
    private IMatDataHandler imat;
    private List<Product> prods;
    private HashMap<Integer, FxProductItem> id2prodListItem;

    private CartHandler cartHandler;


    private @Nullable Integer cartId;

    public IMatData(FxBasket fxBasket){
        if(INSTANCE != null){
            throw new ExceptionInInitializerError("IMatData already exists");
        }
        imat = IMatDataHandler.getInstance();
        prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");

        cartHandler = new CartHandler(imat.imatDirectory()+"/exCartData.json");

        id2prodListItem = new HashMap<>();
        for(Product p : getAllProducts()){
            var newSp = new ShoppingItem(p);
            newSp.setAmount(0);
            FxProductItem fxProductItem = new FxProductItem(newSp, fxBasket, this);
            id2prodListItem.put(p.getProductId(), fxProductItem);
        }

        INSTANCE = this;
    }
    public List<NamedCart> getBoughtHistoryCarts(){
        return cartHandler.getNamedCarts().values()
                .stream().filter(x -> x.getIsBought() == true).toList();
    }
    public List<NamedCart> getSavedCarts(){
        return cartHandler.getNamedCarts().values()
                .stream().filter(x -> x.getIsBought() == false).toList();
    }
    public void orderAndClear(){
        cartHandler.orderCart(imat, true, null);
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
        cartHandler.write();
    }
}
