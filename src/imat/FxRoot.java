package imat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import imat.basket.FxBasket;
import imat.checkout.FxCheckout;
import imat.productlist.FxProductItem;
import imat.savedcarts.FxSavedCarts;
import io.vavr.Tuple2;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FxRoot implements Initializable {
    private static IMatDataHandler imat;
    private static List<Product> prods;
    private static HashMap<Integer, FxProductItem> id2prodListItem;
    private static HashMap<String, Integer> savedCartName2id;

    public static String cartNamePath;

    @FXML public AnchorPane basketPane;
    @FXML public AnchorPane savedBasketsPane;
    @FXML public AnchorPane checkoutPane;
    @FXML public AnchorPane historyPane;
    @FXML public StackPane stackPane;

    public FxBasket fxBasket;
    public FxCheckout fxCheckout;
    public FxSavedCarts fxSavedCarts;

    public static ShoppingCart getCart(){
        return imat.getShoppingCart();
    }
    public static List<Product> getAllProducts(){
        return prods;
    }
    public static FxProductItem getProdListItem(int i){
        return id2prodListItem.get(i);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imat = IMatDataHandler.getInstance();
        cartNamePath = imat.imatDirectory()+"/exCartData.json";
        prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");
        loadCartData();

        System.out.println(basketPane);
        fxBasket = new FxBasket(this);
        fxCheckout = new FxCheckout(this);
        fxSavedCarts = new FxSavedCarts(this);
        basketPane.getChildren().add(fxBasket.getAnchor());
        checkoutPane.getChildren().add(fxCheckout.getAnchor());
        savedBasketsPane.getChildren().add(fxSavedCarts.getAnchor());

        id2prodListItem = new HashMap<>();
        for(Product p : getAllProducts()){
            FxProductItem fxProductItem = new FxProductItem(new ShoppingItem(p), fxBasket);
            id2prodListItem.put(p.getProductId(), fxProductItem);
        }
    }

    public static void saveCartData(){
        File cartDataFile = new File(cartNamePath);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = null;
        try {
            jsonResult = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(savedCartName2id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            cartDataFile.createNewFile();
            Files.writeString(cartDataFile.toPath(), jsonResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadCartData(){
        Path cartDataFile = new File(cartNamePath).toPath();
        String jsonInput = null;
        try {
            jsonInput = Files.readString(cartDataFile);
        } catch (IOException e) {
            jsonInput = "{}";
        }

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Integer>> typeRef = new TypeReference<>() {};
        try {
            savedCartName2id = mapper.readValue(jsonInput, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
}