import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {

    private List<Product> products;
    private String[] columns = {"Name", "Price", "Quantity"};

    public ProductTableModel(List<Product> products) {
        this.products = products;
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product p = products.get(rowIndex);

        switch (columnIndex) {
            case 0: return p.getName();
            case 1: return p.getPrice();
            case 2: return p.getQuantity();
            default: return null;
        }
    }
}