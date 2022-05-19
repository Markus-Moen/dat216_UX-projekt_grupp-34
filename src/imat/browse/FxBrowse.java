package imat.browse;

import imat.Anchorable;
import imat.FxRoot;
import imat.ProductFilter;
import imat.basket.FxBasket;
import imat.productlist.FxProductItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FxBrowse extends AnchorPane implements Anchorable, Initializable {
    FxBasket parentFx;
    AnchorPane anchorPane;
    @FXML FlowPane productFlowPane;
    private Map<Integer, FxProductItem> productListItemMap = new HashMap<Integer, FxProductItem>();

    public FxBrowse(FxBasket parentFx){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("browse.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentFx = parentFx;
    }

    private void updateRecipeList(@Nullable ProductFilter productFilter){
        var ids = parentFx.parentFx.getFilteredProductIds(productFilter);

        productFlowPane.getChildren().clear();
        for(Integer i : ids){
            FxProductItem productListItem = parentFx.parentFx.getProdListItem(i);
            productFlowPane.getChildren().add(productListItem);
        }
    }

    @FXML protected void onButtonReturn(){
        parentFx.basketPane.toFront();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
