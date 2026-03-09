import java.util.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

public class Frontend {
    
    public static void viewList(List<Product> products){
        System.out.println();
        for (Product p : products){
            System.out.println(p.name + " " + p.price + " " + p.quantity);
        }
        System.out.println();
    }
    
    public static void addPersonDialog(List<Product> Data, Scanner myInput) {
        
    }

    public static void editPersonDialog(List<Product> Data, Scanner myInput, boolean loop, String id){
        
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hexabyte Inventory Manager");
        frame.setLayout(new BorderLayout());
        frame.setSize(870,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Scanner myInput = new Scanner(System.in);
        
        List<Product> Data = FileReader.loadData();
        ProductTableModel model = new ProductTableModel(Data);
        JTable table = new JTable(model);
        Font font = new Font("Arial", Font.PLAIN, 15);
        table.setFont(font);
        table.setRowHeight(16);
        table.getTableHeader().setFont(font);
        JScrollPane scrl = new JScrollPane(table);
        frame.add(scrl, BorderLayout.LINE_START);

        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel addPane = new JPanel();
        addPane.setLayout(new GridLayout(5,2));
        
        addPane.add(new JLabel("Name of Product "));
        JTextField nameProduct = new JTextField();
        addPane.add(nameProduct);
        addPane.add(new JLabel("Price of Product "));
        JTextField priceProduct = new JTextField();
        addPane.add(priceProduct);
        addPane.add(new JLabel("Quantity of Product "));
        JTextField quantityProduct = new JTextField();
        addPane.add(quantityProduct);
        JButton addButton = new JButton("Add Product");
        addPane.add(addButton);

        JPanel editPane = new JPanel();
        editPane.setLayout(new GridLayout(4,2));

        editPane.add(new JLabel("Name of Product "));
        JTextField nameProductEdit = new JTextField();
        editPane.add(nameProductEdit);
        editPane.add(new JLabel("Price of Product "));
        JTextField priceProductEdit = new JTextField();
        editPane.add(priceProductEdit);
        JButton saleButton = new JButton("Change Price of Item");
        editPane.add(saleButton);

        JPanel editPane2 = new JPanel();
        editPane2.setLayout(new GridLayout(4,2));

        editPane2.add(new JLabel("Name of Product "));
        JTextField nameProductEdit2 = new JTextField();
        editPane2.add(nameProductEdit2);
        editPane2.add(new JLabel("Quantity of Product "));
        JTextField priceProductEdit2 = new JTextField();
        editPane2.add(priceProductEdit2);
        JButton ChangeQuantity = new JButton("Change Quantity of Item");
        editPane2.add(ChangeQuantity);

        JButton saveButton = new JButton("Save & Quit");

        pane.add(addPane);
        pane.add(editPane);
        pane.add(editPane2);
        pane.add(saveButton);
        frame.add(pane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            Backend.addProduct(Data, new Product(nameProduct.getText(), Double.parseDouble(priceProduct.getText()), Integer.parseInt(quantityProduct.getText())));
            model.fireTableDataChanged();
        });

        saleButton.addActionListener(e ->{
            Backend.editPrice(Data, nameProductEdit, priceProductEdit);
            model.fireTableDataChanged();
        });

        ChangeQuantity.addActionListener(e -> {
            Backend.editQuantity(Data, nameProductEdit2, priceProductEdit2);
            model.fireTableDataChanged();
        });

        saveButton.addActionListener(e -> {
            FileReader.saveData(Data);
            System.exit(0);
        });
        
        frame.setVisible(true);
        myInput.close();
    }
}
