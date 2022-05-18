package imat;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;

public class Checkout extends AnchorPane{

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

    public Checkout(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fx/checkout.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
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

    protected void wizardNext(){
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

    protected void wizardBack(){
        wizardPosition--;
        wizardArr[wizardPosition].toFront();
    }

    private void customerData(){
        customer.setAddress(addressField.getText());
        customer.setPostCode(postCodeField.getText());
        customer.setPostAddress(apartmentNumberField.getText());
        //customer.setCity(cityField.getText());
        customer.setPhoneNumber(phoneNumberField.getText());
    }

    private void cardData(){
        card.setHoldersName(nameField.getText());
        card.setCardNumber(cardNumberField.getText());
        card.setValidMonth(Integer.parseInt(cardMonthField.getText()));
        card.setValidYear(Integer.parseInt(cardYearField.getText()));
        card.setVerificationCode(Integer.parseInt(cardVerificationField.getText()));
    }

    protected void buy(){
        imat.placeOrder(true);
    }
}
