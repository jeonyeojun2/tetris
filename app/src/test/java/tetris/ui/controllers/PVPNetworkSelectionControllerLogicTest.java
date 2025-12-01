package tetris.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.network.GameClient;
import tetris.network.GameServer;
import tetris.network.NetworkMessage;
import tetris.ui.SceneManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PVPNetworkSelectionControllerLogicTest extends JavaFXTestBase {

    private PVPNetworkSelectionController controller;
    private Label gameModeLabel;
    private VBox modeSelectionBox;
    private VBox serverBox;
    private VBox clientBox;
    private Label connectionStatusLabel;
    private Label clientStatusLabel;
    private TextField serverIpField;
    private Button connectButton;

    @BeforeEach
    void setUp() {
        runOnFxThreadAndWait(() -> {
            controller = new PVPNetworkSelectionController();
            injectUiSkeleton();
        });
    }

    private void injectUiSkeleton() {
        gameModeLabel = new Label();
        modeSelectionBox = new VBox();
        serverBox = new VBox();
        clientBox = new VBox();
        connectionStatusLabel = new Label();
        clientStatusLabel = new Label();
        serverIpField = new TextField();
        connectButton = new Button();

        setField("gameModeLabel", gameModeLabel);
        setField("modeSelectionBox", modeSelectionBox);
        setField("serverBox", serverBox);
        setField("clientBox", clientBox);
        setField("connectionStatusLabel", connectionStatusLabel);
        setField("clientStatusLabel", clientStatusLabel);
        setField("serverIpField", serverIpField);
        setField("connectButton", connectButton);
    }

    private void setField(String fieldName, Object value) {
        try {
            Field field = PVPNetworkSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (ReflectiveOperationException e) {
            fail("Failed to inject field " + fieldName + ": " + e.getMessage());
        }
    }

    private Object getField(String fieldName) {
        try {
            Field field = PVPNetworkSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }

    private void invokePrivate(String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Method method = PVPNetworkSelectionController.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(controller, args);
        } catch (ReflectiveOperationException e) {
            fail("Failed to invoke " + methodName + ": " + e.getMessage());
        }
    }

    @Test
    void setGameModeUpdatesLabel() {
        runOnFxThreadAndWait(() -> {
            controller.setGameMode("TIME_LIMIT");
            assertEquals("PVP 대전 - 시간제한 모드", gameModeLabel.getText());
        });
    }

    @Test
    void clientModeShowsClientPanel() {
        runOnFxThreadAndWait(() -> {
            modeSelectionBox.setVisible(true);
            modeSelectionBox.setManaged(true);
            clientBox.setVisible(false);
            clientBox.setManaged(false);

            invokePrivate("onClientMode", new Class<?>[]{});

            assertFalse(modeSelectionBox.isVisible());
            assertTrue(clientBox.isVisible());
        });
    }

    @Test
    void connectWithoutIpShowsValidationMessage() {
        runOnFxThreadAndWait(() -> {
            serverIpField.setText("   ");
            connectButton.setDisable(false);

            invokePrivate("onConnect", new Class<?>[]{});

            assertEquals("Please enter IP address", clientStatusLabel.getText());
            assertTrue(clientStatusLabel.getStyle().contains("#ff0000"));
            assertFalse(connectButton.isDisabled());
        });
    }

    @Test
    void handleClientMessageStartsGameWhenAccepted() {
        SceneManager sceneManager = mock(SceneManager.class);
        GameClient client = mock(GameClient.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            setField("gameClient", client);
            setField("gameMode", "NORMAL");
            NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.CONNECTION_ACCEPTED, "ITEM");
            invokePrivate("handleClientMessage", new Class<?>[]{Object.class}, message);
        });

        verify(sceneManager).showPVPGameScreen("ITEM", null, client, false);
    }

    @Test
    void startGameAsServerUsesSceneManager() {
        SceneManager sceneManager = mock(SceneManager.class);
        GameServer server = mock(GameServer.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            setField("isServer", true);
            setField("gameMode", "ITEM");
            setField("gameServer", server);
            invokePrivate("startGame", new Class<?>[]{});
        });

        verify(sceneManager).showPVPGameScreen("ITEM", server, null, true);
    }

    @Test
    void cleanupClosesNetworkObjects() {
        GameServer server = mock(GameServer.class);
        GameClient client = mock(GameClient.class);

        runOnFxThreadAndWait(() -> {
            setField("gameServer", server);
            setField("gameClient", client);
            invokePrivate("cleanup", new Class<?>[]{});
        });

        verify(server).close();
        verify(client).close();
        assertNull(getField("gameServer"));
        assertNull(getField("gameClient"));
    }

    @Test
    void onBackTriggersCleanupAndNavigation() {
        SceneManager sceneManager = mock(SceneManager.class);
        GameServer server = mock(GameServer.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            setField("gameServer", server);
            invokePrivate("onBack", new Class<?>[]{});
        });

        verify(server).close();
        verify(sceneManager).showPVPModeSelection();
    }
}
