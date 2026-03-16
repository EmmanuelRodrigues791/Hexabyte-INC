// Item class with all 5 attributes matching the database
class Item {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String origin;

    // constructor method
    public Item(int id, String name, double price, int quantity, String origin) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.origin = origin;
    }
    // getter and setter methods
    public int getId() {return id;}
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getOrigin() { return origin; }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}