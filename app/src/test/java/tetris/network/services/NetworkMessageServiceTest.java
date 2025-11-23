package tetris.network.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.network.NetworkMessage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NetworkMessageService 테스트
 */
class NetworkMessageServiceTest {

    private NetworkMessageService service;

    @BeforeEach
    void setUp() {
        service = new NetworkMessageService();
    }

    // ========== 메시지 타입 확인 테스트 ==========

    @Test
    void testIsPingMessage_WithPing() {
        NetworkMessage ping = new NetworkMessage(NetworkMessage.MessageType.PING, 12345L);
        assertTrue(service.isPingMessage(ping));
    }

    @Test
    void testIsPingMessage_WithPong() {
        NetworkMessage pong = new NetworkMessage(NetworkMessage.MessageType.PONG, 12345L);
        assertFalse(service.isPingMessage(pong));
    }

    @Test
    void testIsPingMessage_WithNull() {
        assertFalse(service.isPingMessage(null));
    }

    @Test
    void testIsPingMessage_WithNonNetworkMessage() {
        assertFalse(service.isPingMessage("Not a NetworkMessage"));
    }

    @Test
    void testIsPongMessage_WithPong() {
        NetworkMessage pong = new NetworkMessage(NetworkMessage.MessageType.PONG, 12345L);
        assertTrue(service.isPongMessage(pong));
    }

    @Test
    void testIsPongMessage_WithPing() {
        NetworkMessage ping = new NetworkMessage(NetworkMessage.MessageType.PING, 12345L);
        assertFalse(service.isPongMessage(ping));
    }

    // ========== RTT 계산 테스트 ==========

    @Test
    void testCalculateRTT() {
        long sendTime = 1000L;
        long receiveTime = 1250L;
        assertEquals(250L, service.calculateRTT(sendTime, receiveTime));
    }

    @Test
    void testCalculateRTT_ZeroDelay() {
        long time = 1000L;
        assertEquals(0L, service.calculateRTT(time, time));
    }

    @Test
    void testCalculateRTT_LargeDelay() {
        long sendTime = 1000L;
        long receiveTime = 6000L;
        assertEquals(5000L, service.calculateRTT(sendTime, receiveTime));
    }

    @Test
    void testExtractRTTFromPong_Valid() {
        long sendTime = 1000L;
        long currentTime = 1150L;
        NetworkMessage pong = new NetworkMessage(NetworkMessage.MessageType.PONG, sendTime);
        
        assertEquals(150L, service.extractRTTFromPong(pong, currentTime));
    }

    @Test
    void testExtractRTTFromPong_InvalidType() {
        NetworkMessage ping = new NetworkMessage(NetworkMessage.MessageType.PING, 1000L);
        assertEquals(-1L, service.extractRTTFromPong(ping, 1150L));
    }

    @Test
    void testExtractRTTFromPong_NullMessage() {
        assertEquals(-1L, service.extractRTTFromPong(null, 1150L));
    }

    @Test
    void testExtractRTTFromPong_InvalidDataType() {
        NetworkMessage pong = new NetworkMessage(NetworkMessage.MessageType.PONG, "NotALong");
        assertEquals(-1L, service.extractRTTFromPong(pong, 1150L));
    }

    // ========== 네트워크 품질 평가 테스트 ==========

    @Test
    void testEvaluateNetworkQuality_Excellent() {
        assertEquals(NetworkMessageService.NetworkQuality.EXCELLENT, 
                     service.evaluateNetworkQuality(30L));
    }

    @Test
    void testEvaluateNetworkQuality_Good() {
        assertEquals(NetworkMessageService.NetworkQuality.GOOD, 
                     service.evaluateNetworkQuality(100L));
    }

    @Test
    void testEvaluateNetworkQuality_Fair() {
        assertEquals(NetworkMessageService.NetworkQuality.FAIR, 
                     service.evaluateNetworkQuality(200L));
    }

    @Test
    void testEvaluateNetworkQuality_Poor() {
        assertEquals(NetworkMessageService.NetworkQuality.POOR, 
                     service.evaluateNetworkQuality(400L));
    }

    @Test
    void testEvaluateNetworkQuality_Terrible() {
        assertEquals(NetworkMessageService.NetworkQuality.TERRIBLE, 
                     service.evaluateNetworkQuality(600L));
    }

    @Test
    void testEvaluateNetworkQuality_Negative() {
        assertEquals(NetworkMessageService.NetworkQuality.UNKNOWN, 
                     service.evaluateNetworkQuality(-1L));
    }

    @Test
    void testEvaluateNetworkQuality_BoundaryValues() {
        // Boundary tests
        assertEquals(NetworkMessageService.NetworkQuality.EXCELLENT, 
                     service.evaluateNetworkQuality(49L));
        assertEquals(NetworkMessageService.NetworkQuality.GOOD, 
                     service.evaluateNetworkQuality(50L));
        assertEquals(NetworkMessageService.NetworkQuality.GOOD, 
                     service.evaluateNetworkQuality(149L));
        assertEquals(NetworkMessageService.NetworkQuality.FAIR, 
                     service.evaluateNetworkQuality(150L));
    }

    // ========== 메시지 생성 테스트 ==========

    @Test
    void testCreatePingMessage() {
        long timestamp = 12345L;
        NetworkMessage ping = service.createPingMessage(timestamp);
        
        assertNotNull(ping);
        assertEquals(NetworkMessage.MessageType.PING, ping.getType());
        assertEquals(timestamp, ping.getData());
    }

    @Test
    void testCreatePongMessage() {
        long originalTimestamp = 12345L;
        NetworkMessage pong = service.createPongMessage(originalTimestamp);
        
        assertNotNull(pong);
        assertEquals(NetworkMessage.MessageType.PONG, pong.getType());
        assertEquals(originalTimestamp, pong.getData());
    }

    // ========== 메시지 검증 테스트 ==========

    @Test
    void testIsValidMessageType_Valid() {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_STATE, null);
        assertTrue(service.isValidMessageType(message));
    }

    @Test
    void testIsValidMessageType_Null() {
        assertFalse(service.isValidMessageType(null));
    }

    @Test
    void testIsGameStateMessage_True() {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_STATE, null);
        assertTrue(service.isGameStateMessage(message));
    }

    @Test
    void testIsGameStateMessage_False() {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.PING, null);
        assertFalse(service.isGameStateMessage(message));
    }

    @Test
    void testIsGameStartMessage_True() {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_START, null);
        assertTrue(service.isGameStartMessage(message));
    }

    @Test
    void testIsGameStartMessage_False() {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_OVER, null);
        assertFalse(service.isGameStartMessage(message));
    }

    @Test
    void testIsGameOverMessage_True() {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_OVER, null);
        assertTrue(service.isGameOverMessage(message));
    }

    @Test
    void testIsGameOverMessage_WithNonNetworkMessage() {
        assertFalse(service.isGameOverMessage("Not a message"));
    }

    // ========== NetworkQuality Enum 테스트 ==========

    @Test
    void testNetworkQuality_DisplayNames() {
        assertEquals("매우 좋음", NetworkMessageService.NetworkQuality.EXCELLENT.getDisplayName());
        assertEquals("좋음", NetworkMessageService.NetworkQuality.GOOD.getDisplayName());
        assertEquals("보통", NetworkMessageService.NetworkQuality.FAIR.getDisplayName());
        assertEquals("나쁨", NetworkMessageService.NetworkQuality.POOR.getDisplayName());
        assertEquals("매우 나쁨", NetworkMessageService.NetworkQuality.TERRIBLE.getDisplayName());
        assertEquals("알 수 없음", NetworkMessageService.NetworkQuality.UNKNOWN.getDisplayName());
    }

    @Test
    void testNetworkQuality_ColorCodes() {
        assertEquals("#00ff00", NetworkMessageService.NetworkQuality.EXCELLENT.getColorCode());
        assertEquals("#90ee90", NetworkMessageService.NetworkQuality.GOOD.getColorCode());
        assertEquals("#ffff00", NetworkMessageService.NetworkQuality.FAIR.getColorCode());
        assertEquals("#ffa500", NetworkMessageService.NetworkQuality.POOR.getColorCode());
        assertEquals("#ff0000", NetworkMessageService.NetworkQuality.TERRIBLE.getColorCode());
        assertEquals("#808080", NetworkMessageService.NetworkQuality.UNKNOWN.getColorCode());
    }

    // ========== 통합 시나리오 테스트 ==========

    @Test
    void testPingPongFlow() {
        // 1. PING 생성
        long sendTime = System.currentTimeMillis();
        NetworkMessage ping = service.createPingMessage(sendTime);
        assertTrue(service.isPingMessage(ping));
        
        // 2. PONG 응답 생성
        NetworkMessage pong = service.createPongMessage(ping.getData());
        assertTrue(service.isPongMessage(pong));
        
        // 3. RTT 계산
        long receiveTime = sendTime + 100; // 100ms 후
        long rtt = service.extractRTTFromPong(pong, receiveTime);
        assertEquals(100L, rtt);
        
        // 4. 네트워크 품질 평가
        NetworkMessageService.NetworkQuality quality = service.evaluateNetworkQuality(rtt);
        assertEquals(NetworkMessageService.NetworkQuality.GOOD, quality);
    }
}
