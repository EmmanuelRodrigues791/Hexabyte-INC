import java.io.*;
import java.util.*;

public class FileReader {
    final String FileName = "UserData.csv";
    public static List<Product> loadData(){
        List<Product> Data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new java.io.FileReader("UserData.csv"))){
            String Person = br.readLine();

            while((Person = br.readLine()) != null){
                String[] parts = Person.split(",", 3);
                Data.add(new Product(parts[0], Double.parseDouble(parts[1]), Integer.parseInt(parts[2])));
            }
        }
        catch (IOException e){
            System.out.println("Error loading file.");
        }
        return Data;
    }

    public static void saveData(List<Product> products) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("UserData.csv"))){
            pw.println("name, price, quantity");
            for (Product p : products) {
                pw.println(p.name + "," + p.price + "," + p.quantity);
            }
        }
        catch (IOException e){
            System.out.println("There was an error saving your data.");
        }
    }
} 