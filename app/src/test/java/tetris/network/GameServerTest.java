package tetris.network;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class GameServerTest {
    
    private GameServer server;
    
    @AfterEach
    void tearDown() {
        if (server != null) {
            try {
                server.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }
    
    @Test
    void testServerCreation() throws IOException {
        server = new GameServer(0); // Port 0 = 임의 포트
        
        assertNotNull(server);
        assertTrue(server.isServerRunning());
        assertTrue(server.getPort() > 0);
    }
    
    @Test
    void testIsClientConnectedInitially() throws IOException {
        server = new GameServer(0);
        
        assertFalse(server.isClientConnected());
    }
    
    @Test
    void testSetMessageHandler() throws IOException {
        server = new GameServer(0);
        
        assertFalse(server.hasMessageHandler());
        
        server.setMessageHandler(new GameServer.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {}
            @Override
            public void onClientConnected() {}
            @Override
            public void onClientDisconnected() {}
            @Override
            public void onError(Exception e) {}
            @Override
            public void onRttUpdate(long rtt) {}
        });
        
        assertTrue(server.hasMessageHandler());
    }
    
    @Test
    void testGetPortReturnsValidPort() throws IOException {
        server = new GameServer(0);
        int port = server.getPort();
        
        assertTrue(port > 0 && port <= 65535);
    }
    
    @Test
    void testServerRunningAfterCreation() throws IOException {
        server = new GameServer(0);
        
        assertTrue(server.isServerRunning());
    }
    
    @Test
    void testCloseServer() throws IOException {
        server = new GameServer(0);
        assertTrue(server.isServerRunning());
        
        server.close();
        
        assertFalse(server.isServerRunning());
    }
}
