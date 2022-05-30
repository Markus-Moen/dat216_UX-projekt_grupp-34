package imat.data;

import imat.basket.FxBasket;
import imat.productlist.FxProductItem;
import io.vavr.Tuple2;
import javafx.application.Platform;
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

    private @Nullable Integer openCartId;

    public IMatData(FxBasket fxBasket){
        if(INSTANCE != null){
            throw new ExceptionInInitializerError("IMatData already exists");
        }
        imat = IMatDataHandler.getInstance();
        prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");

        cartHandler = new CartHandler(imat.imatDirectory()+"/exCartData.json");
        cartHandler.load(imat);

        id2prodListItem = new HashMap<>();
        for(Product p : getAllProducts()){
            var newSp = new ShoppingItem(p);
            newSp.setAmount(0);
            FxProductItem fxProductItem = new FxProductItem(newSp, fxBasket, this);
            id2prodListItem.put(p.getProductId(), fxProductItem);
        }

        INSTANCE = this;
    }

    // CART METHODS
    public List<NamedCart> getHistoryCarts(){
        return cartHandler.getNamedCarts().values()
                .stream().filter(x -> x.getIsBought() == true).toList();
    }
    public List<NamedCart> getSavedCarts(){
        return cartHandler.getNamedCarts().values()
                .stream().filter(x -> x.getIsBought() == false).toList();
    }
    public void saveActiveCartAsNew(String name, boolean isBuy){
        NamedCartExtraData namedCartExtraData = new NamedCartExtraData(isBuy, name, null);
        boolean doClearCart = isBuy;
        NamedCart o = cartHandler.orderCart(imat, doClearCart, namedCartExtraData);
        openCartId = o.getId();
    }
    public void orderAndClearActiveCart(){
        String newName;
        if(openCartId == null){
            newName = "Köp "+new Date();
        } else {
            newName = cartHandler.getNamedCarts().get(openCartId).getName();
        }
        saveActiveCartAsNew(newName, true);
        clearActiveCart();
    }
    public String moveSavedCartToActiveCart(int cartId){
        NamedCart nc = cartHandler.getNamedCarts().get(cartId);
        openCartId = nc.getId();
        imat.getShoppingCart().clear();
        for (var x : nc.getOrder().getItems()) {
            var getFxProdItem = id2prodListItem.get(x.getProduct().getProductId());
            getFxProdItem.getShoppingItem().setAmount(x.getAmount());
            imat.getShoppingCart().addItem(getFxProdItem.getShoppingItem());
            getFxProdItem.refreshGraphics();
        }
        return nc.getName();
    }
    public void clearActiveCart(){
        openCartId = null;
        imat.getShoppingCart().clear();
    }
    public ShoppingCart getActiveCart(){
        return imat.getShoppingCart();
    }
    public void saveCurrentyActiveCart(){
        if(openCartId == null ){
            throw new RuntimeException("IMPOSSIBLE ERROR, CAN NOT SAVE UNACTIVE CART");
        }
        NamedCart nc = cartHandler.getNamedCarts().get(openCartId);

        List<ShoppingItem> shoppingItems = new ArrayList<>();
        for (var e : imat.getShoppingCart().getItems()){
            shoppingItems.add(e);
        }
        nc.getOrder().setItems(shoppingItems);
        nc.getOrder().setDate(new Date());
    }

    // IMATDATA GETTERS
    public Customer getCustomer(){
        return imat.getCustomer();
    }
    public CreditCard getCreditCard(){
        return imat.getCreditCard();
    }
    public Image getProductImage(Product prod){
        return imat.getFXImage(prod);
    }

    // IMATDATA GETTERS
    public List<Product> getAllProducts(){
        return prods;
    }
    public Collection<FxProductItem> getAllProdListItems(){
        return id2prodListItem.values();
    }
    public FxProductItem getProdListItem(int i){
        if(id2prodListItem.containsKey(i) == false)
            throw new IndexOutOfBoundsException("Item does not exist");
        return id2prodListItem.get(i);
    }

    // FILTERING
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
        ShoppingCart cart = getActiveCart();
        List<ShoppingItem> items = cart.getItems();
        for (ShoppingItem item : items){
            output[0] += Math.round(item.getAmount()) + " " + item.getProduct().getName() + "\n";
            output[1] += item.getTotal() + " kr" + "\n";
        }

        output[2] = Double.toString(cart.getTotal()) + " kr";

        return output;
    }

    public void DEBUG_saveCarts(){
        cartHandler.write();
    }
    public void shutDown() {
        System.out.println("SHUTTING DOWN");
        cartHandler.write();
        imat.shutDown();
    }
}
