package imat.browse;

import imat.FxRoot;
import imat.ProductFilter;
import imat.productlist.FxProductItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductBrowse extends AnchorPane {
    FxRoot parentFx;
    @FXML FlowPane productFlowPane;
    private Map<Integer, FxProductItem> productListItemMap = new HashMap<Integer, FxProductItem>();

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
        var ids = parentFx.getFilteredProductIds(productFilter);

        productFlowPane.getChildren().clear();
        for(Integer i : ids){
            FxProductItem productListItem = parentFx.getProdListItem(i);
            productFlowPane.getChildren().add(productListItem);
        }
    }
}
