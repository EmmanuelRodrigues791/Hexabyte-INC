import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Item item;

    @BeforeEach
    public void setUp() {
        item = new Item(1, "Milk", 5.99, 50, "Nelson");
    }

    @Test
    public void testGetId() {
        assertEquals(1, item.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Milk", item.getName());
    }

    @Test
    public void testGetPrice() {
        assertEquals(5.99, item.getPrice());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(50, item.getQuantity());
    }

    @Test
    public void testGetOrigin() {
        assertEquals("Nelson", item.getOrigin());
    }

    @Test
    public void testSetPrice() {
        item.setPrice(12.99);
        assertEquals(12.99, item.getPrice());
    }

    @Test
    public void testSetQuantity() {
        item.setQuantity(100);
        assertEquals(100, item.getQuantity());
    }

    @Test
    public void testSetPriceToZero() {
        item.setPrice(0.0);
        assertEquals(0.0, item.getPrice());
    }

    @Test
    public void testSetQuantityToZero() {
        item.setQuantity(0);
        assertEquals(0, item.getQuantity());
    }
}