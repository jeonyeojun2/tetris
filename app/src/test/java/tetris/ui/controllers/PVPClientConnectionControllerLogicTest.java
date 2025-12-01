package tetris.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.data.RecentIPManager;
import tetris.network.GameClient;
import tetris.network.NetworkMessage;
import tetris.ui.SceneManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PVPClientConnectionControllerLogicTest extends JavaFXTestBase {

    private PVPClientConnectionController controller;
    private TextField serverIpField;
    private VBox recentIPsBox;
    private Button connectButton;
    private Label statusLabel;
    private Button backButton;
    private RecentIPManager originalRecentIpManager;
    private RecentIPManager recentIpManagerMock;

    @BeforeEach
    void setUp() {
        originalRecentIpManager = getRecentIpManagerInstance();
        recentIpManagerMock = mock(RecentIPManager.class);
        when(recentIpManagerMock.getRecentIPs()).thenReturn(List.of());
        setRecentIpManagerInstance(recentIpManagerMock);

        runOnFxThreadAndWait(() -> {
            controller = new PVPClientConnectionController();
            injectUiSkeleton(controller);
            controller.initialize(null, null);
        });
    }

    @AfterEach
    void tearDown() {
        setRecentIpManagerInstance(originalRecentIpManager);
    }

    private void injectUiSkeleton(PVPClientConnectionController target) {
        serverIpField = new TextField();
        recentIPsBox = new VBox();
        recentIPsBox.getChildren().add(new Label("최근 접속"));
        connectButton = new Button();
        statusLabel = new Label();
        backButton = new Button();
        ImageView background = new ImageView();

        setField(target, "serverIpField", serverIpField);
        setField(target, "recentIPsBox", recentIPsBox);
        setField(target, "connectButton", connectButton);
        setField(target, "statusLabel", statusLabel);
        setField(target, "backButton", backButton);
        setField(target, "backgroundImage", background);
    }

    private void setField(Object target, String name, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            fail("Failed to inject field " + name + ": " + e.getMessage());
        }
    }

    private void invokePrivate(String name, Class<?>[] types, Object... args) {
        try {
            Method method = PVPClientConnectionController.class.getDeclaredMethod(name, types);
            method.setAccessible(true);
            method.invoke(controller, args);
        } catch (ReflectiveOperationException e) {
            fail("Failed to invoke " + name + ": " + e.getMessage());
        }
    }

    private RecentIPManager getRecentIpManagerInstance() {
        try {
            Field field = RecentIPManager.class.getDeclaredField("instance");
            field.setAccessible(true);
            return (RecentIPManager) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read RecentIPManager.instance", e);
        }
    }

    private void setRecentIpManagerInstance(RecentIPManager replacement) {
        try {
            Field field = RecentIPManager.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, replacement);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set RecentIPManager.instance", e);
        }
    }

    @Test
    void connectWithEmptyInputShowsValidationMessage() {
        runOnFxThreadAndWait(() -> {
            serverIpField.setText("   ");
            connectButton.setDisable(false);
            invokePrivate("onConnect", new Class<?>[]{});
            assertEquals("IP 주소를 입력하세요", statusLabel.getText());
            assertFalse(connectButton.isDisabled());
        });
    }

    @Test
    void loadRecentIpButtonsPopulateField() {
        when(recentIpManagerMock.getRecentIPs()).thenReturn(List.of("10.0.0.9"));

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            assertEquals(2, recentIPsBox.getChildren().size(), "One header and one entry expected");
            assertTrue(recentIPsBox.getChildren().get(0).isVisible(), "Header should be visible when IPs exist");

            HBox ipBox = (HBox) recentIPsBox.getChildren().get(1);
            Button ipButton = (Button) ipBox.getChildren().get(0);
            ipButton.fire();
            assertEquals("10.0.0.9", serverIpField.getText());
        });
    }

    @Test
    void handleConnectionAcceptedStoresIpAndOpensLobby() {
        SceneManager sceneManager = mock(SceneManager.class);
        GameClient gameClient = mock(GameClient.class);
        when(recentIpManagerMock.getRecentIPs()).thenReturn(List.of());

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            serverIpField.setText("192.168.0.7");
            setGameClient(gameClient);
            invokePrivate("handleMessage", new Class<?>[]{Object.class},
                new NetworkMessage(NetworkMessage.MessageType.CONNECTION_ACCEPTED, "ok"));
        });

        verify(recentIpManagerMock).addRecentIP("192.168.0.7");
        verify(sceneManager).showPVPLobby(null, gameClient, false);
        assertEquals("연결되었습니다! 로비로 이동 중...", statusLabel.getText());
    }

    @Test
    void validConnectDisablesButtonAndConfiguresClient() {
        AtomicReference<GameClient.MessageHandler> handlerRef = new AtomicReference<>();
        GameClient mockClient = mock(GameClient.class);
        doAnswer(inv -> {
            handlerRef.set(inv.getArgument(0));
            return null;
        }).when(mockClient).setMessageHandler(any());

        runOnFxThreadAndWait(() -> {
            controller.setGameClientFactory(() -> mockClient);
            serverIpField.setText("10.0.0.5");
            invokePrivate("onConnect", new Class<?>[]{});
            assertTrue(connectButton.isDisabled());
            assertEquals("연결 중...", statusLabel.getText());
        });

        verify(mockClient).setMessageHandler(any());
        assertNotNull(handlerRef.get(), "Message handler should be registered");

        runOnFxThreadAndWait(() -> handlerRef.get().onError(new RuntimeException("boom")));
        waitFor(50);
        assertEquals("연결 실패: boom", statusLabel.getText());
        assertFalse(connectButton.isDisabled());
    }

    @Test
    void backClosesClientAndReturnsToSelection() {
        SceneManager sceneManager = mock(SceneManager.class);
        GameClient client = mock(GameClient.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            setGameClient(client);
            invokePrivate("onBack", new Class<?>[]{});
        });

        verify(client).close();
        verify(sceneManager).showPVPModeSelection();
    }

    private void setGameClient(GameClient gameClient) {
        try {
            Field field = PVPClientConnectionController.class.getDeclaredField("gameClient");
            field.setAccessible(true);
            field.set(controller, gameClient);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set gameClient", e);
        }
    }
}
