
package imat.history;

import imat.Anchorable;
//import imat.FxRoot;
import imat.basket.FxBasket;
import imat.data.NamedCart;
import javafx.fxml.FXML;
import imat.basket.FxBasket;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import static io.vavr.API.print;

public class FxHistory implements Anchorable, Initializable {
    private final AnchorPane anchorPane;
    private final FxBasket fxBasket;

    //public static IMatDataHandler imat;

    @FXML private AnchorPane historyPane;
    @FXML private AnchorPane historyListAP;
    @FXML private AnchorPane historyReceiptAP;

    @FXML private Text itemListText;
    @FXML private Text itemCostText;
    @FXML private Text itemTotalText;
    @FXML private TextArea receipt;

    @FXML private Text historyItemListText;
    @FXML private Text historyItemCostText;
    @FXML private Text historyItemTotalText;

    @FXML private FlowPane receiptList;
    @FXML public void openReceiptView() {
        historyReceiptAP.toFront();
    }
    @FXML public void closeReceiptView () {
        historyListAP.toFront();
    }

    //private List<FxReceipt> receiptArrayList = new ArrayList<>();

    public FxHistory(FxBasket fxBasket) {
        this.fxBasket = fxBasket;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("history.fxml"));
        fxmlLoader.setController(this);

        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        openHistory();
        //addReceiptToList();
    }
    public void openHistory(){
        historyPane.toFront();
        addReceiptToList();
    }

    @FXML protected void onButtonReturn(){
        fxBasket.focus();
    }

    public void addReceiptToList(){
        receiptList.getChildren().clear();
        List<NamedCart> receiptData = fxBasket.iMatData.getHistoryCarts();
        for (NamedCart cart : receiptData) {
            String name = cart.getName();
            addReceipt(cart, name);
        }
    }

    public void addReceipt(NamedCart cart, String name){
        //String[] order, String listName, String listDate
        Receipt receipt = new Receipt();

        Date currentDate = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MMM/yyyy");
        String dateOnly = dateFormat.format(currentDate);
        //receipt.setReceiptDate(dateOnly);

        receipt.setListName(name);

        Order order = cart.getOrder();
        //List<ShoppingItem> items = order.getItems();
        receipt.setReceiptDate(dateFormat.format(order.getDate()));
        receipt.setStringList(fxBasket.iMatData.receiptHistory(order));
        System.out.println(fxBasket.iMatData.receiptHistory(order)[0]);
        FxReceipt listReceipt = new FxReceipt(this, receipt);
        receiptList.getChildren().add(listReceipt.getAnchor());

        //receiptArrayList.add(listReceipt);
        historyItemListText.setText(receipt.getProductNames());
        historyItemCostText.setText(receipt.getCostValues());
        historyItemTotalText.setText(receipt.getTotal());
        //updateHistoryList();
    }

    public void updateHistoryList(){
        //receiptList.getChildren().clear();
        //for (FxReceipt receipt : receiptArrayList) {
        //    receiptList.getChildren().add(receipt.getAnchor());
        //}
    }    @FXML protected void onButtonBack(){
        fxBasket.focus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }
}
