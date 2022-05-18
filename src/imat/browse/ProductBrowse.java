package imat.browse;

import imat.Controller;
import imat.ProductFilter;
import imat.productlist.ProductItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductBrowse extends AnchorPane {
    Controller parentController;
    @FXML FlowPane productFlowPane;
    private Map<Integer, ProductItem> productListItemMap = new HashMap<Integer, ProductItem>();

    public ProductBrowse(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fx/productbrowse.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void updateRecipeList(@Nullable ProductFilter productFilter){
        var ids = parentController.getFilteredProductIds(productFilter);

        productFlowPane.getChildren().clear();
        for(Integer i : ids){
            ProductItem productListItem = parentController.getProdListItem(i);
            productFlowPane.getChildren().add(productListItem);
        }
    }
}
