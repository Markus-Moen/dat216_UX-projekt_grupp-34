package imat;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Either;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

public class Controller implements Initializable {
    public class ProductFilter {
        @Nullable ProductFilter then;

        private Either<String, HashSet<ProductCategory>> filter;

        ProductFilter(Either<String, List<ProductCategory>> filter){
            this(filter, null);
        }
        ProductFilter(Either<String, List<ProductCategory>> filter, ProductFilter then){
            this.then = then;
            this.filter = filter.map(HashSet::new);
        }

        static final int bestPossibleMatch = 2;
        static int howGoodStringMatch(String search, String src){ //this can be improved
            if(src.startsWith(search)) return bestPossibleMatch;
            if(src.contains(search)) return 1;
            else return -1;
        }
        int howGoodMatch(Product p){
            int matchScore;
            if(filter.isLeft()) {
                matchScore = howGoodStringMatch(p.getName(), filter.getLeft());
            } else {
                matchScore = filter.get().contains(p.getCategory()) ? 1 : -1;
            }
            if (then == null) return matchScore;
            int childMatch = then.howGoodMatch(p);
            return childMatch < 0 ? -1 : matchScore*(bestPossibleMatch+1)+childMatch;
        }
    }

    private static IMatDataHandler imat;
    private static List<Product> prods;
    private static HashMap<Integer, ProductListItem> id2prodListItem;

    public static List<Product> getAllProducts(){
        return prods;
    }
    public static HashMap<Integer, ProductListItem> getId2prodListItem(){
        return id2prodListItem;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imat = IMatDataHandler.getInstance();
        prods = imat.getProducts();
        System.out.println(prods.size()+" products loaded");

        for(Product p : Controller.getAllProducts()){
            ProductListItem productItem = new ProductListItem(p, this);
        }



    }
}
