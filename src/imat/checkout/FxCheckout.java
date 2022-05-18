package imat.checkout;

import imat.Anchorable;
import imat.FxRoot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;

public class FxCheckout extends AnchorPane implements Anchorable, Initializable {
    private AnchorPane anchorPane;
    private FxRoot parentFx;

    private int wizardPosition;
    private AnchorPane[] wizardArr;
    public static IMatDataHandler imat;
    private Customer customer;
    private CreditCard card;

    @FXML private AnchorPane addressPane;
    @FXML private AnchorPane deliveryPane;
    @FXML private AnchorPane paymentPane;
    @FXML private AnchorPane donePane;
    @FXML private TextField addressField;
    @FXML private TextField postCodeField;
    @FXML private TextField apartmentNumberField;
    @FXML private TextField cityField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField nameField;
    @FXML private TextField cardNumberField;
    @FXML private TextField cardMonthField;
    @FXML private TextField cardYearField;
    @FXML private TextField cardVerificationField;

    public FxCheckout(FxRoot parentFx) {
        this.parentFx = parentFx;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("checkout.fxml"));
        fxmlLoader.setController(this);

        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        imat = IMatDataHandler.getInstance();

        customer = imat.getCustomer();
        card = imat.getCreditCard();

        openCheckout();

        wizardArr = new AnchorPane[] {addressPane, deliveryPane, paymentPane, donePane};

        //nameField.setText("RuneSlayerF69420");
    }

    public void openCheckout(){
        fillFields();
        wizardPosition = 0;
        addressPane.toFront();
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
                // code block
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
        imat.placeOrder(true);
    }
    @FXML protected void pay(){

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
