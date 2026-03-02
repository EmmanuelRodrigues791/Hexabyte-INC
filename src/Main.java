import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventorySystem system = new InventorySystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("CartPilot System)");
        System.out.print("Login as (owner or manager or employee): ");
        String role = scanner.nextLine();

        while (true) {
            System.out.println("\n----Menu----");

            if (role.equals("owner") || role.equals("manager")) {
                System.out.println("1. Add Item");
                System.out.println("2. Remove Item");
                System.out.println("3. Update Price");
            }

            System.out.println("4. Update Quantity");
            System.out.println("5. View Inventory");
            System.out.println("6. View Log");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (role.equals("owner") || role.equals("manager")) {
                        System.out.print("Item Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Quantity: ");
                        int qty = scanner.nextInt();
                        system.addItem(name, price, qty);
                    }
                    break;

                case 2:
                    if (role.equals("owner") || role.equals("manager")) {
                        System.out.print("Item Name to Remove: ");
                        system.removeItem(scanner.nextLine());
                    }
                    break;

                case 3:
                    if (role.equals("owner") || role.equals("manager")) {
                        System.out.print("Item Name: ");
                        String name = scanner.nextLine();
                        System.out.print("New Price: ");
                        double price = scanner.nextDouble();
                        system.updatePrice(name, price);
                    }
                    break;

                case 4:
                    System.out.print("Item Name: ");
                    String name = scanner.nextLine();
                    System.out.print("New Quantity: ");
                    int qty = scanner.nextInt();
                    system.updateQuantity(name, qty);
                    break;

                case 5:
                    system.viewInventory();
                    break;

                case 6:
                    system.viewLog();
                    break;

                case 0:
                    System.out.println("Exiting CartPilot...");
                    return;
            }
        }
    }
}