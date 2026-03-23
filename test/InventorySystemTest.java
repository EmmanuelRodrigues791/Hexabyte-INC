import org.junit.jupiter.api.*;
import org.mockito.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventorySystemTest {

    private Connection mockConn;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private InventorySystem system;

    @BeforeEach
    public void setUp() throws Exception {
        mockConn = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);
        when(mockConn.createStatement()).thenReturn(mock(Statement.class));

        // Inject mock connection
        system = new InventorySystem(mockConn);
    }

    @Test
    public void testAddItemCallsInsert() throws Exception {
        system.addItem(1, "Milk", 5.99, 50, "Nelson");
        verify(mockPs, times(2)).executeUpdate(); // once for inventory, once for log
    }

    @Test
    public void testRemoveItemCallsDelete() throws Exception {
        system.removeItem("Milk");
        verify(mockConn, atLeastOnce()).prepareStatement(contains("DELETE"));
    }

    @Test
    public void testUpdatePriceCallsUpdate() throws Exception {
        system.updatePrice("Milk", 12.99);
        verify(mockConn, atLeastOnce()).prepareStatement(contains("UPDATE inventory SET price"));
    }

    @Test
    public void testUpdateQuantityCallsUpdate() throws Exception {
        system.updateQuantity("Milk", 100);
        verify(mockConn, atLeastOnce()).prepareStatement(contains("UPDATE inventory SET quantity"));
    }

    @Test
    public void testViewInventoryExecutesSelect() throws Exception {
        Statement mockStmt = mock(Statement.class);
        when(mockConn.createStatement()).thenReturn(mockStmt);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        system.viewInventory();
        verify(mockStmt).executeQuery(contains("SELECT * FROM inventory"));
    }

    @Test
    public void testViewLogExecutesSelect() throws Exception {
        Statement mockStmt = mock(Statement.class);
        when(mockConn.createStatement()).thenReturn(mockStmt);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        system.viewLog();
        verify(mockStmt).executeQuery(contains("SELECT entries"));
    }

    @Test
    public void testCloseClosesConnection() throws Exception {
        system.close();
        verify(mockConn).close();
    }
}