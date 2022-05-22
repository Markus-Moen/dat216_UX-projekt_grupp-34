package imat;

import javafx.scene.layout.AnchorPane;

import static javafx.scene.layout.AnchorPane.*;

public interface Anchorable {
    public AnchorPane getAnchor();
    public default AnchorPane fitAnchor(AnchorPane anchorPane){
        anchorPane.setTopAnchor(anchorPane, 0.0);
        setBottomAnchor(anchorPane, 0.0);
        setLeftAnchor(anchorPane, 0.0);
        setRightAnchor(anchorPane, 0.0);
        return anchorPane;
    }
}
