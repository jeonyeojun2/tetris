package tetris.network;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NetworkMessage 클래스의 단위 테스트
 */
class NetworkMessageTest {
    
    @Test
    void testConstructor() {
        String testData = "test data";
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_START, testData);
        
        assertEquals(NetworkMessage.MessageType.GAME_START, message.getType());
        assertEquals(testData, message.getData());
        assertTrue(message.getTimestamp() > 0);
    }
    
    @Test
    void testTimestamp() {
        long before = System.currentTimeMillis();
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.PING, null);
        long after = System.currentTimeMillis();
        
        assertTrue(message.getTimestamp() >= before);
        assertTrue(message.getTimestamp() <= after);
    }
    
    @Test
    void testAllMessageTypes() {
        NetworkMessage.MessageType[] types = NetworkMessage.MessageType.values();
        assertTrue(types.length >= 18, "18개 이상의 메시지 타입이 있어야 함");
        
        // 각 메시지 타입으로 메시지 생성 테스트
        for (NetworkMessage.MessageType type : types) {
            NetworkMessage message = new NetworkMessage(type, "test");
            assertEquals(type, message.getType());
        }
    }
    
    @Test
    void testWithNullData() {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.DISCONNECT, null);
        
        assertEquals(NetworkMessage.MessageType.DISCONNECT, message.getType());
        assertNull(message.getData());
    }
    
    @Test
    void testWithMapData() {
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("score", 1000);
        data.put("level", 5);
        
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_STATE_UPDATE, data);
        
        assertEquals(NetworkMessage.MessageType.GAME_STATE_UPDATE, message.getType());
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> retrievedData = (java.util.Map<String, Object>) message.getData();
        assertEquals(1000, retrievedData.get("score"));
        assertEquals(5, retrievedData.get("level"));
    }
}
