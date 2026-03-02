import java.util.*;

class InventorySystem {
    private List<Item> items = new ArrayList<>();
    private List<String> log = new ArrayList<>();

    public void addItem(String name, double price, int quantity) {
        items.add(new Item(name, price, quantity));
        log.add("Added item: " + name);
    }

    public void removeItem(String name) {
        items.removeIf(item -> item.getName().equalsIgnoreCase(name));
        log.add("Removed item: " + name);
    }

    public void updatePrice(String name, double newPrice) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                item.setPrice(newPrice);
                log.add("Updated price for: " + name);
            }
        }
    }

    public void updateQuantity(String name, int newQuantity) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                item.setQuantity(newQuantity);
                log.add("Updated quantity for: " + name);
            }
        }
    }

    public void viewInventory() {
        for (Item item : items) {
            System.out.println(item);
        }
    }

    public void viewLog() {
        for (String entry : log) {
            System.out.println(entry);
        }
    }
}