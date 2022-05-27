package imat.browse;

import imat.Anchorable;
import imat.IMatData;
import imat.ProductFilter;
import imat.basket.FxBasket;
import imat.productlist.FxProductItem;
import io.vavr.control.Either;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FxBrowse implements Anchorable, Initializable {
    FxBasket fxBasket;
    AnchorPane anchorPane;
    @FXML FlowPane productFlowPane;
    @FXML TextField searchTextBox;

    private Thread flowAppenderThread;
    private Task flowAppenderTask;

    private Map<Integer, FxProductItem> productListItemMap = new HashMap<Integer, FxProductItem>();

    public FxBrowse(FxBasket parentFx){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("browse.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.fxBasket = parentFx;
        onTextEdit();
    }

    private void updateProductList(@Nullable ProductFilter productFilter){
        if(flowAppenderThread != null && flowAppenderThread.isAlive()){
            flowAppenderTask.cancel();
            flowAppenderThread.interrupt();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        productFlowPane.getChildren().clear();

        flowAppenderTask = new Task() {
            @Override protected Void call() throws Exception {
                var ids = fxBasket.iMatData.getFilteredProductIds(productFilter);
                System.out.println("FOUND:"+ids.size());

                for(Integer i : ids) {
                    FxProductItem productListItem = fxBasket.iMatData.getProdListItem(i);
                    Thread.sleep(10);
                    Platform.runLater(() ->
                        productFlowPane.getChildren().add(productListItem.getAnchor())
                    );
                }
                return null;
            }
        };
        flowAppenderThread = new Thread(flowAppenderTask);
        flowAppenderThread.start();
    }

    public void openBrowse(){
        searchTextBox.requestFocus();
        onTextEdit();
    }

    @FXML protected void onButtonReturn(){
        fxBasket.updateBasket();
        fxBasket.focus();
    }
    @FXML protected void onTextEdit(){
        String search = searchTextBox.getText();
        System.out.println("SEARCH:"+search);
        if (search == null || search.length() == 0) {
            updateProductList(null);
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
