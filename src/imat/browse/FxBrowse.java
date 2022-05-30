package imat.browse;

import imat.Anchorable;
import imat.data.ProductFilter;
import imat.basket.FxBasket;
import imat.productlist.FxProductItem;
import io.vavr.control.Either;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.Nullable;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/*
    POD,
    BREAD,
    BERRY,
    CITRUS_FRUIT,
    HOT_DRINKS,
    COLD_DRINKS,
    EXOTIC_FRUIT,
    FISH,
    VEGETABLE_FRUIT,
    CABBAGE,
    MEAT,
    DAIRIES,
    MELONS,
    FLOUR_SUGAR_SALT,
    NUTS_AND_SEEDS,
    PASTA,
    POTATO_RICE,
    ROOT_VEGETABLE,
    FRUIT,
    SWEET,
    HERB;
*/
public class FxBrowse implements Anchorable, Initializable {
    FxBasket fxBasket;
    AnchorPane anchorPane;
    @FXML FlowPane productFlowPane;
    @FXML TextField searchTextBox;
    @FXML AnchorPane scrollContent;
    @FXML ScrollPane scrollPane;
    @FXML Button lowReturnButton;

    @FXML ToggleButton togBread;
    @FXML ToggleButton togDrink;
    @FXML ToggleButton togFish;
    @FXML ToggleButton togFruit;
    @FXML ToggleButton togGreens;
    @FXML ToggleButton togMeat;
    @FXML ToggleButton togMilk;
    @FXML ToggleButton togSweet;
    @FXML ToggleButton togDry;

    private Thread flowAppenderThread;
    private Task flowAppenderTask;
    private List<ProductCategory> selectedCategories;

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
        scrollContent.prefWidthProperty().bind(scrollPane.widthProperty());
        onTextEdit();

        ToggleGroup toggleCategory = new ToggleGroup();
        togBread.setToggleGroup(toggleCategory);
        togDrink.setToggleGroup(toggleCategory);
        togFish.setToggleGroup(toggleCategory);
        togFruit.setToggleGroup(toggleCategory);
        togGreens.setToggleGroup(toggleCategory);
        togMeat.setToggleGroup(toggleCategory);
        togMilk.setToggleGroup(toggleCategory);
        togSweet.setToggleGroup(toggleCategory);
        togDry.setToggleGroup(toggleCategory);

        toggleCategory.selectedToggleProperty().addListener(
                (observable, oldToggle, newToggle) -> {
                    selectedCategories = new ArrayList<>();
                    if (newToggle == togBread) {
                        selectedCategories.add(ProductCategory.BREAD);
                    } else if (newToggle == togDrink) {
                        selectedCategories.add(ProductCategory.COLD_DRINKS);
                        selectedCategories.add(ProductCategory.HOT_DRINKS);
                    } else if (newToggle == togFish) {
                        selectedCategories.add(ProductCategory.FISH);
                    } else if (newToggle == togFruit) {
                        selectedCategories.add(ProductCategory.FRUIT);
                        selectedCategories.add(ProductCategory.BERRY);
                        selectedCategories.add(ProductCategory.NUTS_AND_SEEDS);
                    } else if (newToggle == togGreens) {
                        selectedCategories.add(ProductCategory.VEGETABLE_FRUIT);
                    } else if (newToggle == togMeat) {
                        selectedCategories.add(ProductCategory.MEAT);
                    } else if (newToggle == togMilk) {
                        selectedCategories.add(ProductCategory.DAIRIES);
                    } else if (newToggle == togSweet) {
                        selectedCategories.add(ProductCategory.SWEET);
                    } else if (newToggle == togDry) {
                        selectedCategories.add(ProductCategory.FLOUR_SUGAR_SALT);
                        selectedCategories.add(ProductCategory.PASTA);
                    } else {
                    }
                    System.out.println("TOGGLE GROUP ACTION:"+selectedCategories.size());
                    onTextEdit();
                }
        );
    }

    private void updateProductList(@Nullable ProductFilter productFilter){
        if(flowAppenderThread != null && flowAppenderThread.isAlive()){
            flowAppenderTask.cancel();
            flowAppenderThread.interrupt();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        productFlowPane.getChildren().clear();

        flowAppenderTask = new Task() {
            @Override protected Void call() throws Exception {
                var ids = fxBasket.iMatData.getFilteredProductIds(productFilter);

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
        fxBasket.focus();
    }
    @FXML protected void onTextEdit(){
        System.out.println("TEXEDIT");
        String search = searchTextBox.getText();
        if (search == null || search.length() == 0) {
            updateProductList(null);
        }
        var productFilter = new ProductFilter(Either.left(search));
        if(selectedCategories != null && selectedCategories.isEmpty() == false){
            productFilter = new ProductFilter(Either.right(selectedCategories), productFilter);
        }
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
