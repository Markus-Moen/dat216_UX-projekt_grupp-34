package imat.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingCart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExCart {
    private String exCartPath;
    private int exCartMin;
    private HashMap<Integer, String> cartId2Name;

    public ExCart(String exCartPath){
        this.exCartPath = exCartPath;

        Path cartDataFile = new File(exCartPath).toPath();
        String jsonInput = null;
        try {
            jsonInput = Files.readString(cartDataFile);
        } catch (IOException e) {
            jsonInput = "{}";
        }

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Integer>> typeRef = new TypeReference<>() {};
        try {
            var load = mapper.readValue(jsonInput, typeRef);
            cartId2Name = invert(load);
            exCartMin = cartId2Name.keySet().stream().min(Integer::compare).orElse(-1);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <K,V> HashMap<K,V> invert(HashMap<V, K> map){
        HashMap<K,V> out = new HashMap<>();
        for (Map.Entry<V, K> entry : map.entrySet()) {
            if(out.containsKey(entry.getValue())){
                throw new ArrayStoreException("Overlapping map");
            }
            out.put(entry.getValue(), entry.getKey());
        }
        return out;
    }

    public String getName(int id){
        return cartId2Name.get(id);
    }

    public void editSavedCart(int id, List<Order> orders){
        var order = orders.stream().filter(x -> x.getOrderNumber() == id).findFirst();
        if(order.isEmpty())
            throw new IndexOutOfBoundsException(id+" does not exist in orders");
    }

    public void write(){
        File cartDataFile = new File(exCartPath);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = null;
        try {
            var write = invert(cartId2Name);
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

    public Order cartAsOrder(ShoppingCart sc, String name){
        Order order = new Order();
        var items = sc.getItems();
        exCartMin--;
        order.setOrderNumber(exCartMin);
        order.setDate(new Date());
        order.setItems(items);
        cartId2Name.put(exCartMin, name);
        return order;
    }
}
