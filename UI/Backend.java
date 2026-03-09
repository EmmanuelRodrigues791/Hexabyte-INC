import java.util.*;

import javax.swing.JTextField;

public class Backend {

    public static void addProduct(List<Product> products, Product p){
        boolean found = false;
        for (Product p2 : products){
            if (p2.getName().toLowerCase().equals(p.getName().toLowerCase())){
                p2.setQuantity(p2.getQuantity() + p.getQuantity());
                found = true;
            }
        }
        if (!found){
            products.add(p);
        }
    }
    
    public static void editQuantity(List<Product> products, JTextField nametxt, JTextField pricetxt){
        for (Product p : products){
            if (p.getName().toLowerCase().equals(nametxt.getText().toLowerCase())){
                p.setQuantity(Integer.parseInt(pricetxt.getText()));
            }
        }
    }

    public static void editPrice(List<Product> products, JTextField nametxt, JTextField pricetxt){
        for (Product p : products){
            if (p.getName().toLowerCase().equals(nametxt.getText().toLowerCase())){
                p.setPrice(Double.parseDouble(pricetxt.getText()));
            }
        }
    }
} 
