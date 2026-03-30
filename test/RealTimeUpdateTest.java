import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RealTimeUpdateTest {

    @Test
    public void testListenerIsNotNull() {
        // RED - InventoryUpdateListener doesn't exist yet
        InventoryUpdateListener listener = new InventoryUpdateListener();
        assertNotNull(listener);
    }

    @Test
    public void testListenerReceivesAddEvent() {
        InventoryUpdateListener listener = new InventoryUpdateListener();
        listener.onItemAdded("Milk");
        assertEquals("Milk", listener.getLastEvent());
    }

    @Test
    public void testListenerReceivesRemoveEvent() {
        InventoryUpdateListener listener = new InventoryUpdateListener();
        listener.onItemRemoved("Eggs");
        assertEquals("Eggs", listener.getLastEvent());
    }

    @Test
    public void testListenerReceivesPriceUpdate() {
        InventoryUpdateListener listener = new InventoryUpdateListener();
        listener.onPriceUpdated("Milk", 12.99);
        assertEquals("Milk", listener.getLastEvent());
    }

    @Test
    public void testListenerReceivesQuantityUpdate() {
        InventoryUpdateListener listener = new InventoryUpdateListener();
        listener.onQuantityUpdated("Milk", 100);
        assertEquals("Milk", listener.getLastEvent());
    }
}