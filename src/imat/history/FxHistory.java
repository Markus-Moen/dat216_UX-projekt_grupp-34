package imat.history;

import imat.Anchorable;
//import imat.FxRoot;
import imat.IMat;
import imat.IMatData;
import imat.productlist.FxProductItem;
import imat.savedcarts.FxSavedCartItem;
import imat.savedcarts.SavedCart;
import javafx.fxml.FXML;
import imat.basket.FxBasket;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.text.Text;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;
import se.chalmers.cse.dat216.project.ShoppingItem;

import static io.vavr.API.print;

public class FxHistory implements Anchorable, Initializable {
    private final AnchorPane anchorPane;
    //private IMat imat;
    private final FxBasket fxBasket;

    //public static IMatDataHandler imat;

    @FXML private AnchorPane historyPane;
    @FXML private AnchorPane historyListAP;
    @FXML private AnchorPane historyRecieptAP;

    @FXML private Text itemListText;
    @FXML private Text itemCostText;
    @FXML private Text itemTotalText;
    @FXML private TextArea receipt;

    @FXML private FlowPane recieptList;
    @FXML public void openReceiptView () {
        historyRecieptAP.toFront();
    }
    @FXML public void closeReceiptView () {
        historyListAP.toFront();
    }

    private List<Receipt> receipts = new ArrayList<>();

    public FxHistory(FxBasket fxBasket) {
        this.fxBasket = fxBasket;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("history.fxml"));
        fxmlLoader.setController(this);

        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        //imat = IMatDataHandler.getInstance();
        //customer = imat.getCustomer();
        //card = imat.getCreditCard();

        openHistory();
        addReceipt();
    }
    public void openHistory(){
        historyPane.toFront();
    }

    @FXML protected void onButtonReturn(){
        fxBasket.focus();
    }

    private void addReceipt(){
        //String[] order, String listName, String listDate
        Receipt receipt = new Receipt();
        receipt.setReceiptDate("20/20");
        receipt.setListName("Hejsan");
        String[] lista = new String[3];
        lista[0] = "melon";
        lista[1] = "20";
        lista[2] = "200";
        receipt.setStringList(lista);
        FxReceipt listReceipt = new FxReceipt(this, receipt);
        AnchorPane receiptAnchorPane = listReceipt.getAnchor();
        recieptList.getChildren().add(listReceipt.getAnchor());
    }

    @FXML protected void onButtonBack(){
        fxBasket.focus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }
}
