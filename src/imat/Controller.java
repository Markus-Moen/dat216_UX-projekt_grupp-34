package imat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import imat.basket.Basket;
import imat.checkout.Checkout;
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

public class Controller implements Initializable {
    private static IMatDataHandler imat;
    private static List<Product> prods;
    private static HashMap<Integer, ProductItem> id2prodListItem;
    private static HashMap<String, Integer> savedCartName2id;

    public static String cartNamePath;


    @FXML public AnchorPane basketPane;
    @FXML public AnchorPane savedBasketsPane;
    @FXML public AnchorPane checkoutPane;
    @FXML public AnchorPane historyPane;

    @FXML public StackPane stackPane;


    public List<Product> getAllProducts(){
        return prods;
    }
    public ProductItem getProdListItem(int i){
        return id2prodListItem.get(i);
    }

    public void saveCartData(){
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

    public void loadCartData(){
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

    public ShoppingCart getShoppingCart() {
        return imat.getShoppingCart();
    }
    public List<Integer> getFilteredProductIds(ProductFilter productOrder){
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imat = IMatDataHandler.getInstance();
        cartNamePath = imat.imatDirectory()+"/exCartData.json";
        prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");
        loadCartData();

        for(Product p : getAllProducts()){
            ProductItem productItem = new ProductItem(new ShoppingItem(p), this);
        }

        System.out.println(basketPane);
        basketPane = new Basket(this);
        checkoutPane = new Checkout(this);

        stackPane.getChildren().add(basketPane);
        basketPane.toFront();
        System.out.println(basketPane);
    }
}
