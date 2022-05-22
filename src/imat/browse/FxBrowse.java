package imat.browse;

import imat.Anchorable;
import imat.FxRoot;
import imat.ProductFilter;
import imat.basket.FxBasket;
import imat.productlist.FxProductItem;
import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.Nullable;

import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FxBrowse extends AnchorPane implements Anchorable, Initializable {
    FxBasket parentFx;
    AnchorPane anchorPane;
    @FXML FlowPane productFlowPane;
    @FXML TextField searchTextBox;

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

    private void updateProductList(@Nullable ProductFilter productFilter){
        var ids = FxRoot.getFilteredProductIds(productFilter);
        System.out.println("FOUND:"+ids.size());

        productFlowPane.getChildren().clear();
        for(Integer i : ids){
            FxProductItem productListItem = FxRoot.getProdListItem(i);
            productFlowPane.getChildren().add(productListItem.getAnchor());
        }
    }

    @FXML protected void onButtonReturn(){
        parentFx.basketPane.toFront();
    }
    @FXML protected void onTextEdit(){
        String search = searchTextBox.getText();
        System.out.println("SEARCH:"+search);
        if (search == null || search.length() == 0) {
            return; //TODO: nosearch default
        }
        var productFilter = new ProductFilter(Either.left(search));
        updateProductList(productFilter);
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
