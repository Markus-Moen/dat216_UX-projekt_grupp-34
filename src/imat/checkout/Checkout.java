package imat.checkout;

import imat.Anchorable;
import imat.Controller;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;

public class Checkout extends AnchorPane implements Anchorable, Initializable {
    private AnchorPane anchorPane;
    private Controller parentController;

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

    public Checkout(Controller parentController) {
        this.parentController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("checkout.fxml"));
        //fxmlLoader.setController(null);
        //fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        imat = IMatDataHandler.getInstance();

        wizardArr = new AnchorPane[] {addressPane, deliveryPane, paymentPane, donePane};
        wizardPosition = 0;

        customer = imat.getCustomer();
        card = imat.getCreditCard();

        nameField.setText("RuneSlayerF69420");
    }

    private void fillFields(){
        addressField.setText(customer.getAddress());
        postCodeField.setText(customer.getPostCode());
        apartmentNumberField.setText(customer.getPostAddress());
        phoneNumberField.setText(customer.getPhoneNumber());

        nameField.setText(card.getHoldersName());
        cardNumberField.setText(card.getCardNumber());
        cardMonthField.setText(Integer.toString(card.getValidMonth()));
        cardYearField.setText(Integer.toString(card.getValidYear()));
        cardVerificationField.setText(Integer.toString(card.getVerificationCode()));
    }

    @FXML protected void wizardNext(){
        switch(wizardPosition) {
            case 2:
                buy();
                break;
            case 1:
                // code block
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
        card.setValidMonth(Integer.parseInt(cardMonthField.getText()));
        card.setValidYear(Integer.parseInt(cardYearField.getText()));
        card.setVerificationCode(Integer.parseInt(cardVerificationField.getText()));
    }

    @FXML protected void buy(){
        imat.placeOrder(true);
    }
    @FXML protected void pay(){

    }

    @FXML protected void onButtonBack(){
        parentController.basketPane.toFront();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
