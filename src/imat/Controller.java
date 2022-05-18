package imat;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.chalmers.cse.dat216.project.*;

import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private static IMatDataHandler imat;
    private static List<Product> prods;
    private static HashMap<Integer, ProductItem> id2prodListItem;


    @FXML private AnchorPane basketPane;
    @FXML private AnchorPane savedBasketsPane;
    @FXML private AnchorPane checkoutPane;
    @FXML private AnchorPane historyPane;


    public static List<Product> getAllProducts(){
        return prods;
    }
    public static ProductItem getProdListItem(int i){
        return id2prodListItem.get(i);
    }

    public static ShoppingCart getShoppingCart() {
        return imat.getShoppingCart();
    }
    public static ShoppingCart loadShoppingCart(String name){
        System.out.println("NOT IMPLEMENTED");
        return imat.getShoppingCart();
    }
    public static void saveShoppingCart(String name){
        System.out.println("NOT IMPLEMENTED");

    }

    public static List<Integer> getFilteredProductIds(ProductFilter productOrder){
        var matchFunc = new Function<Product, Tuple2<Integer, Product>>(){
            @Override
            public Tuple2<Integer, Product> apply(Product product) {
                if (productOrder == null){
                    return new Tuple2<>(1, product);
                } else {
                    return new Tuple2<>(productOrder.howGoodMatch(product), product);
                }
            }
        };
        Stream<Tuple2<Integer, Product>> scored = prods.stream().map(matchFunc);
        Stream<Product> filteredAndOrdered = scored
                .filter(x -> x._1 > 0)
                .sorted(Comparator.comparingInt(o -> o._1))
                .map(x -> x._2);
        Stream<Integer> intOrder = filteredAndOrdered.map(Product::getProductId);
        return intOrder.collect(Collectors.toList());
    }

    //@Override
    public Controller(ResourceBundle resourceBundle) {
        imat = IMatDataHandler.getInstance();
        prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");

        for(Product p : Controller.getAllProducts()){
            ProductItem productItem = new ProductItem(new ShoppingItem(p), this);
        }

        AnchorPane basket = new Basket(this);
        basketPane.getChildren().add(basket);
        basketPane.toFront();

        AnchorPane checkout = new Checkout(this);
        checkoutPane.getChildren().add(checkout);
    }
}
