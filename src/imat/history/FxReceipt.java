package imat.history;

import imat.Anchorable;
import imat.savedcarts.SavedCart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class FxReceipt implements Anchorable {
    private AnchorPane anchorPane;
    private FxHistory parentController;
    private Receipt receipt;

    @FXML private Text receiptDateText;
    @FXML private Text receiptNameText;
    @FXML private Text receiptPriceText;



    public FxReceipt(FxHistory parentController, Receipt receipt) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("receiptItem.fxml"));
        fxmlLoader.setController(this);
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.receipt = receipt;
        this.parentController = parentController;

        //System.out.println(receipt.getReceiptDate());
        //System.out.println(receipt.getReceiptListName());
        //System.out.println(receipt.getTotal());
        receiptDateText.setText(receipt.getReceiptDate());
        receiptNameText.setText(receipt.getReceiptListName());
        receiptPriceText.setText(receipt.getTotal());
    }

    @FXML public void openReceipt(){
        parentController.openReceiptView();
    }
    @FXML public void closeReceipt(){
        parentController.closeReceiptView();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }
}
