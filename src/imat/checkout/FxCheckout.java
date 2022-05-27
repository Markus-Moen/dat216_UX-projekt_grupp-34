package imat.checkout;

import imat.Anchorable;
import imat.IMatData;
import imat.basket.FxBasket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.CreditCard;

public class FxCheckout extends AnchorPane implements Anchorable, Initializable {
    private AnchorPane anchorPane;
    private FxBasket fxBasket;

    private int wizardPosition;
    private AnchorPane[] wizardArr;
    public static IMatDataHandler imat;
    private Customer customer;
    private CreditCard card;

    @FXML private AnchorPane addressPane;
    @FXML private AnchorPane deliveryPane;
    @FXML private AnchorPane paymentPane;
    @FXML private AnchorPane donePane;

    @FXML private Text itemListText;
    @FXML private Text itemCostText;
    @FXML private Text itemTotalText;
    @FXML private TextArea receipt;

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

    @FXML private ToggleGroup dateGroup;
    @FXML private GridPane deliveryGrid;
    @FXML private TextArea doneText;

    @FXML private AnchorPane imgPaneAddress;
    @FXML private ImageView imgAddress;
    @FXML private AnchorPane imgPaneDelivery;
    @FXML private ImageView imgDelivery;
    @FXML private AnchorPane imgPanePayment;
    @FXML private ImageView imgPayment;
    @FXML private AnchorPane imgPaneDone;
    @FXML private ImageView imgDone;

    public FxCheckout(FxBasket fxBasket) {
        this.fxBasket = fxBasket;
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
    }

    public void openCheckout(){
        fillFields();
        fillReceipt();
        wizardPosition = 0;
        addressPane.toFront();
    }

    private void fillReceipt(){
        String[] receipt = fxBasket.iMatData.receipt();
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
        if (dateGroup.getSelectedToggle() != null){
            dateGroup.getSelectedToggle().setSelected(false);
        }
        imat.placeOrder();
    }

    @FXML protected void dateSelector(){
        String output;
        String[] days = {"måndag", "tisdag", "onsdag", "torsdag", "fre", "lördag", "söndag"};
        String[] times = {"10:00", "10:30", "11:00", "11:30", "12:00"};

        for (Node node : deliveryGrid.getChildren()) {
            if (node instanceof ToggleButton){
                if (((ToggleButton)node).isSelected()){
                    output = days[deliveryGrid.getColumnIndex(node)] + " ";
                    output += times[deliveryGrid.getRowIndex(node)-1];
                    output = "Tack för din beställning!\n" +
                            "Dina kassar anländer " + output + ".\n" +
                            "Vi ringer en halvtimme innan leverans.";
                    doneText.setText(output);
                    break;
                }
            }
        }
    }

    @FXML protected void onButtonBack(){
        fxBasket.focus();
    }

    @Override
    public AnchorPane getAnchor() {
        return anchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
