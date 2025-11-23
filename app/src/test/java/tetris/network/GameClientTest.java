package tetris.network;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

class GameClientTest {
    
    private GameClient client;
    
    @AfterEach
    void tearDown() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }
    
    @Test
    void testClientCreation() {
        client = new GameClient();
        
        assertNotNull(client);
        assertFalse(client.isClientRunning());
    }
    
    @Test
    void testIsConnectedInitially() {
        client = new GameClient();
        
        assertFalse(client.isConnected());
    }
    
    @Test
    void testSetMessageHandler() {
        client = new GameClient();
        
        assertFalse(client.hasMessageHandler());
        
        client.setMessageHandler(new GameClient.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {}
            @Override
            public void onConnected() {}
            @Override
            public void onDisconnected() {}
            @Override
            public void onError(Exception e) {}
            @Override
            public void onRttUpdate(long rtt) {}
        });
        
        assertTrue(client.hasMessageHandler());
    }
    
    @Test
    void testGetServerAddressWhenNotConnected() {
        client = new GameClient();
        
        assertNull(client.getServerAddress());
    }
    
    @Test
    void testGetServerPortWhenNotConnected() {
        client = new GameClient();
        
        assertEquals(-1, client.getServerPort());
    }
    
    @Test
    void testClientNotRunningInitially() {
        client = new GameClient();
        
        assertFalse(client.isClientRunning());
    }
    
    @Test
    void testCloseClient() {
        client = new GameClient();
        
        client.close();
        
        assertFalse(client.isConnected());
        assertFalse(client.isClientRunning());
    }
}
