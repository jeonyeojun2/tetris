package tetris.network;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 네트워크 접속 과정을 검증하는 통합 테스트
 * 서버-클라이언트 연결, 메시지 송수신을 자동으로 검증
 * 
 * 주의: 통합 테스트는 CI 환경에서 타임아웃이 발생할 수 있어 임시로 비활성화
 */
@Disabled("통합 테스트는 로컬 환경에서만 실행")
class NetworkConnectionIntegrationTest {
    
    private static final int TEST_PORT = 17778;
    private static final int TIMEOUT_SECONDS = 10;
    
    private GameServer gameServer;
    private GameClient gameClient;
    
    @BeforeEach
    void setUp() {
        gameServer = null;
        gameClient = null;
    }
    
    @AfterEach
    void tearDown() {
        if (gameClient != null) {
            try {
                gameClient.close();
            } catch (Exception e) {
                // Ignore
            }
        }
        if (gameServer != null) {
            try {
                gameServer.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }
    
    /**
     * 테스트 1: 서버 시작과 클라이언트 연결 확인
     */
    @Test
    @Timeout(value = TIMEOUT_SECONDS)
    void testServerStartupAndClientConnection() throws IOException, InterruptedException {
        CountDownLatch serverStarted = new CountDownLatch(1);
        
        gameServer = new GameServer(TEST_PORT);
        gameServer.setMessageHandler(new GameServer.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {}
            
            @Override
            public void onClientConnected() {
                serverStarted.countDown();
            }
            
            @Override
            public void onClientDisconnected() {}
            
            @Override
            public void onError(Exception e) {}
            
            @Override
            public void onRttUpdate(long rttMillis) {}
        });
        
        gameClient = new GameClient();
        gameClient.setMessageHandler(new GameClient.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {}
            
            @Override
            public void onConnected() {}
            
            @Override
            public void onDisconnected() {}
            
            @Override
            public void onError(Exception e) {}
            
            @Override
            public void onRttUpdate(long rttMillis) {}
        });
        gameClient.connect("localhost", TEST_PORT);
        
        boolean connected = serverStarted.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertTrue(connected, "클라이언트가 서버에 연결되어야 함");
    }
    
    /**
     * 테스트 2: 메시지 송수신
     */
    @Test
    @Timeout(value = TIMEOUT_SECONDS)
    void testMessageExchange() throws IOException, InterruptedException {
        CountDownLatch clientConnected = new CountDownLatch(1);
        CountDownLatch messageReceived = new CountDownLatch(1);
        
        AtomicReference<NetworkMessage> receivedMsg = new AtomicReference<>();
        
        gameServer = new GameServer(TEST_PORT);
        gameServer.setMessageHandler(new GameServer.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {
                if (message instanceof NetworkMessage) {
                    receivedMsg.set((NetworkMessage) message);
                    messageReceived.countDown();
                }
            }
            
            @Override
            public void onClientConnected() {
                clientConnected.countDown();
            }
            
            @Override
            public void onClientDisconnected() {}
            
            @Override
            public void onError(Exception e) {}
            
            @Override
            public void onRttUpdate(long rttMillis) {}
        });
        
        gameClient = new GameClient();
        gameClient.setMessageHandler(new GameClient.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {}
            
            @Override
            public void onConnected() {}
            
            @Override
            public void onDisconnected() {}
            
            @Override
            public void onError(Exception e) {}
            
            @Override
            public void onRttUpdate(long rttMillis) {}
        });
        gameClient.connect("localhost", TEST_PORT);
        
        assertTrue(clientConnected.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        
        NetworkMessage testMsg = new NetworkMessage(
            NetworkMessage.MessageType.ATTACK, 
            "Test"
        );
        gameClient.sendMessage(testMsg);
        
        assertTrue(messageReceived.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        assertNotNull(receivedMsg.get());
        assertEquals(NetworkMessage.MessageType.ATTACK, receivedMsg.get().getType());
    }
    
    /**
     * 테스트 3: GameStateData 전송
     */
    @Test
    @Timeout(value = TIMEOUT_SECONDS)
    void testGameStateDataTransfer() throws IOException, InterruptedException {
        CountDownLatch clientConnected = new CountDownLatch(1);
        CountDownLatch dataReceived = new CountDownLatch(1);
        
        AtomicReference<GameStateData> receivedState = new AtomicReference<>();
        
        gameServer = new GameServer(TEST_PORT);
        gameServer.setMessageHandler(new GameServer.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {
                if (message instanceof NetworkMessage) {
                    NetworkMessage netMsg = (NetworkMessage) message;
                    if (netMsg.getType() == NetworkMessage.MessageType.GAME_STATE_UPDATE) {
                        receivedState.set((GameStateData) netMsg.getData());
                        dataReceived.countDown();
                    }
                }
            }
            
            @Override
            public void onClientConnected() {
                clientConnected.countDown();
            }
            
            @Override
            public void onClientDisconnected() {}
            
            @Override
            public void onError(Exception e) {}
            
            @Override
            public void onRttUpdate(long rttMillis) {}
        });
        
        gameClient = new GameClient();
        gameClient.setMessageHandler(new GameClient.MessageHandler() {
            @Override
            public void onMessageReceived(Object message) {}
            
            @Override
            public void onConnected() {}
            
            @Override
            public void onDisconnected() {}
            
            @Override
            public void onError(Exception e) {}
            
            @Override
            public void onRttUpdate(long rttMillis) {}
        });
        gameClient.connect("localhost", TEST_PORT);
        
        assertTrue(clientConnected.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        
        int[][] board = new int[20][10];
        int[][] itemBoard = new int[20][10];
        board[19][5] = 1;
        
        GameStateData testState = new GameStateData(
            board, itemBoard, 1000, 5, 10, false,
            new int[][]{{1,1},{1,1}}, 0, 0, 1,
            new int[][]{{1,0},{1,1},{0,1}}, 2, 3, Arrays.asList(1, 3, 5)
        );
        
        NetworkMessage stateMessage = new NetworkMessage(
            NetworkMessage.MessageType.GAME_STATE_UPDATE, 
            testState
        );
        gameClient.sendMessage(stateMessage);
        
        assertTrue(dataReceived.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        assertNotNull(receivedState.get());
        assertEquals(1000, receivedState.get().getScore());
        assertEquals(5, receivedState.get().getLevel());
        assertEquals(1, receivedState.get().getBoard()[19][5]);
    }
}
