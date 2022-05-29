package imat.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class CartHandler {
    private String exCartPath;
    private int exCartMin;

    private HashMap<Integer, NamedCart> cartMap;

    public CartHandler(String exCartPath){
        this.exCartPath = exCartPath;
    }

    public void load(IMatDataHandler imat){
        Path cartDataFile = new File(exCartPath).toPath();
        String jsonInput = null;
        try {
            jsonInput = Files.readString(cartDataFile);
        } catch (IOException e) {
            jsonInput = "{}";
        }

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<NamedCartExtraData>> typeRef = new TypeReference<>() {};
        List<NamedCartExtraData> load;
        try {
            load = mapper.readValue(jsonInput, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var orders = imat.getOrders();
        cartMap = new HashMap<Integer, NamedCart>();
        for (var namc: load) {
            int id = namc.getId();
            var order = namc.getById(orders, id);
            cartMap.put(id, new NamedCart(order, namc));
        }
    }

    public void write(){
        File cartDataFile = new File(exCartPath);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = null;
        try {
            List<NamedCartExtraData> write = cartMap.values().stream().map(x -> (NamedCartExtraData)x).toList();
            jsonResult = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(write);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            cartDataFile.createNewFile();
            Files.writeString(cartDataFile.toPath(), jsonResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NamedCart orderCart(IMatDataHandler imat, boolean clearShoppingCart, @Nullable NamedCartExtraData exData){
        Order newOrder = imat.placeOrder(clearShoppingCart);

        if (exData == null){
            exData = new NamedCartExtraData(false, "", newOrder.getOrderNumber());
        }

        NamedCart nc = new NamedCart(newOrder, exData);
        cartMap.put(newOrder.getOrderNumber(), nc);
        return nc;
    }

    public HashMap<Integer, NamedCart> getNamedCarts(){
        return cartMap;
    }
}
