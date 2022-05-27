package imat.history;

import imat.Anchorable;
import imat.FxRoot;
import javafx.fxml.FXML;
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
import java.util.ResourceBundle;

import javafx.scene.text.Text;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;

import static io.vavr.API.print;

public class FxHistory extends AnchorPane implements Anchorable, Initializable {
    private AnchorPane anchorPane;
    private FxRoot parentFx;

    public static IMatDataHandler imat;

    @FXML private AnchorPane historyListAP;
    @FXML private AnchorPane historyRecieptAP;

    @FXML private Text itemListText;
    @FXML private Text itemCostText;
    @FXML private Text itemTotalText;
    @FXML private TextArea receipt;

    @FXML private FlowPane recieptList;


    public FxHistory(FxRoot parentFx) {
        this.parentFx = parentFx;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("checkout.fxml"));
        fxmlLoader.setController(this);

        try {
            anchorPane = fitAnchor(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        imgAddress.fitWidthProperty().bind(imgPaneAddress.widthProperty());
        imgDelivery.fitWidthProperty().bind(imgPaneDelivery.widthProperty());
        imgPayment.fitWidthProperty().bind(imgPanePayment.widthProperty());
        imgDone.fitWidthProperty().bind(imgPaneDone.widthProperty());

        imat = IMatDataHandler.getInstance();
        customer = imat.getCustomer();
        card = imat.getCreditCard();

        openCheckout();

        wizardArr = new AnchorPane[] {addressPane, deliveryPane, paymentPane, donePane};

        //nameField.setText("RuneSlayerF69420");
    }

    public void openCheckout(){
        fillFields();
        fillReceipt();
        wizardPosition = 0;
        addressPane.toFront();
    }

    private void fillReceipt(){
        String[] receipt = parentFx.receipt();
        itemListText.setText(receipt[0]);
        itemCostText.setText(receipt[1]);
        itemTotalText.setText(receipt[2]);
    }

    private void fillFields(){
        addressField.setText(customer.getAddress());
        postCodeField.setText(customer.getPostCode());
        apartmentNumberField.setText(customer.getPostAddress());
        phoneNumberField.setText(customer.getPhoneNumber());

        nameField.setText(card.getHoldersName());
        cardNumberField.setText(card.getCardNumber());

        int temp;
        temp = card.getValidMonth();
        if (temp != -1){
            cardMonthField.setText(Integer.toString(temp));
        }

        temp = card.getValidYear();
        if (temp != -1){
            cardYearField.setText(Integer.toString(temp));
        }

        temp = card.getVerificationCode();
        if (temp != -1){
            cardVerificationField.setText(Integer.toString(temp));
        }
    }

    @FXML protected void wizardNext(){
        switch(wizardPosition) {
            case 0:
                customerData();
                break;
            case 1:
                dateSelector();
                break;
            case 2:
                cardData();
                buy();
                break;
            default:
                // code block
        }
        wizardPosition++;
        wizardArr[wizardPosition].toFront();
    }

    @FXML protected void wizardBack(){
        wizardPosition--;
        wizardArr[wizardPosition].toFront();
    }

    @FXML private void customerData(){
        customer.setAddress(addressField.getText());
        customer.setPostCode(postCodeField.getText());
        customer.setPostAddress(apartmentNumberField.getText());
        //customer.setCity(cityField.getText());
        customer.setPhoneNumber(phoneNumberField.getText());
    }

    @FXML private void cardData(){
        card.setHoldersName(nameField.getText());
        card.setCardNumber(cardNumberField.getText());
        card.setValidMonth(maybeParseInt(cardMonthField.getText()));
        card.setValidYear(maybeParseInt(cardYearField.getText()));
        card.setVerificationCode(maybeParseInt(cardVerificationField.getText()));
    }

    public static int maybeParseInt(String str) {
        try {
            int i = Integer.parseInt(str);
            return i;
        } catch(NumberFormatException e){
            return -1;
        }
    }

    @FXML protected void buy(){
        imat.placeOrder();
    }

    @FXML protected void dateSelector(){
        print(dateGroup.getSelectedToggle().getProperties().values().toArray()[0].toString());
    }

    @FXML protected void onButtonBack(){
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
