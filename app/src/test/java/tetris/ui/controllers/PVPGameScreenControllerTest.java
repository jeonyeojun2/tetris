package tetris.ui.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.testfx.util.WaitForAsyncUtils;
import tetris.game.BattleGameEngine;
import tetris.game.GameBoard;
import tetris.game.GameEngine;
import tetris.network.GameClient;
import tetris.network.GameStateData;
import tetris.network.GameServer;
import tetris.network.NetworkMessage;
import tetris.ui.SettingsManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PVPGameScreenControllerTest extends JavaFXTestBase {

    private PVPGameScreenController controller;
    private Canvas myCanvas;
    private Canvas opponentCanvas;
    private Canvas myNextCanvas;
    private Canvas opponentNextCanvas;
    private Canvas myIncomingCanvas;
    private Canvas opponentIncomingCanvas;
    private Label statusLabel;
    private VBox gameOverBox;
    private Label latencyLabel;
    private Label lagWarningLabel;
    private Label timerLabel;
    private Label myScoreLabel;
    private Label opponentScoreLabel;
    private Label myLevelLabel;
    private Label opponentLevelLabel;
    private Label myLinesLabel;
    private Label opponentLinesLabel;
    private Label myPlayerLabel;
    private Label opponentPlayerLabel;
    private Label gameModeLabel;
    private boolean originalColorBlind;

    @BeforeEach
    void setUp() {
        originalColorBlind = SettingsManager.getInstance().isColorBlindModeEnabled();
        runOnFxThreadAndWait(() -> {
            controller = new PVPGameScreenController();
            injectBasicUiStructure();
        });
    }

    @AfterEach
    void tearDown() {
        SettingsManager.getInstance().setColorBlindModeEnabled(originalColorBlind);
    }

    private void injectBasicUiStructure() {
        myCanvas = new Canvas();
        opponentCanvas = new Canvas();
        myNextCanvas = new Canvas();
        opponentNextCanvas = new Canvas();
        myIncomingCanvas = new Canvas();
        opponentIncomingCanvas = new Canvas();
        statusLabel = new Label();
        latencyLabel = new Label();
        lagWarningLabel = new Label();
        timerLabel = new Label();
        myScoreLabel = new Label();
        opponentScoreLabel = new Label();
        myLevelLabel = new Label();
        opponentLevelLabel = new Label();
        myLinesLabel = new Label();
        opponentLinesLabel = new Label();
        myPlayerLabel = new Label();
        opponentPlayerLabel = new Label();
        gameOverBox = new VBox();
        gameOverBox.setVisible(false);
        gameOverBox.setManaged(false);
        gameModeLabel = new Label();

        setField("myCanvas", myCanvas);
        setField("opponentCanvas", opponentCanvas);
        setField("myNextCanvas", myNextCanvas);
        setField("opponentNextCanvas", opponentNextCanvas);
        setField("myIncomingCanvas", myIncomingCanvas);
        setField("opponentIncomingCanvas", opponentIncomingCanvas);
        setField("statusLabel", statusLabel);
        setField("latencyLabel", latencyLabel);
        setField("lagWarningLabel", lagWarningLabel);
        setField("timerLabel", timerLabel);
        setField("myScoreLabel", myScoreLabel);
        setField("opponentScoreLabel", opponentScoreLabel);
        setField("myLevelLabel", myLevelLabel);
        setField("opponentLevelLabel", opponentLevelLabel);
        setField("myLinesLabel", myLinesLabel);
        setField("opponentLinesLabel", opponentLinesLabel);
        setField("myPlayerLabel", myPlayerLabel);
        setField("opponentPlayerLabel", opponentPlayerLabel);
        setField("gameOverBox", gameOverBox);
        setField("gameModeLabel", gameModeLabel);
        setField("battleEngine", new BattleGameEngine("NORMAL"));
    }

    private void setField(String fieldName, Object value) {
        try {
            Field field = PVPGameScreenController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (ReflectiveOperationException e) {
            fail("Failed to set field " + fieldName + ": " + e.getMessage());
        }
    }

    private Object getField(String fieldName) {
        try {
            Field field = PVPGameScreenController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }

    private void invokeSetStatusMessage(String message, String color) {
        try {
            Method method = PVPGameScreenController.class.getDeclaredMethod("setStatusMessage", String.class, String.class);
            method.setAccessible(true);
            method.invoke(controller, message, color);
        } catch (ReflectiveOperationException e) {
            fail("Failed to invoke setStatusMessage: " + e.getMessage());
        }
    }

    private void waitForFxEvents() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    private GameStateData createGameStateData(int score, int incomingLines) {
        int[][] board = new int[GameBoard.BOARD_HEIGHT][GameBoard.BOARD_WIDTH];
        int[][] itemBoard = new int[GameBoard.BOARD_HEIGHT][GameBoard.BOARD_WIDTH];
        return new GameStateData(
            board,
            itemBoard,
            score,
            2,
            4,
            false,
            new int[][]{{1}},
            0,
            0,
            1,
            new int[][]{{2}},
            2,
            incomingLines,
            Arrays.asList(0, 1)
        );
    }

    private void setEngineScore(GameEngine engine, int score) {
        try {
            Field scoreField = GameEngine.class.getDeclaredField("score");
            scoreField.setAccessible(true);
            scoreField.setInt(engine, score);
        } catch (ReflectiveOperationException e) {
            fail("Failed to set engine score: " + e.getMessage());
        }
    }

    private static class TestAnimationTimer extends AnimationTimer {
        private boolean stopped;

        @Override
        public void handle(long now) {
            // no-op for tests
        }

        @Override
        public void stop() {
            stopped = true;
        }

        boolean isStopped() {
            return stopped;
        }
    }

    @Test
    void renderingMethodsHandleColorBlindBoards() {
        runOnFxThreadAndWait(() -> {
            SettingsManager.getInstance().setColorBlindModeEnabled(true);
            controller.setupCanvasSize();
            BattleGameEngine engine = new BattleGameEngine("NORMAL");
            setField("battleEngine", engine);
            setField("isServer", true);

            GameEngine myEngine = engine.getPlayer1Engine();
            int[][] board = myEngine.getGameBoard().getBoard();
            board[0][0] = 1;
            board[1][1] = 2;

            setField("playerLinesToClear", Arrays.asList(0));
            setField("isAnimatingClear", true);

            GameStateData opponent = createGameStateData(900, 2);
            setField("opponentState", opponent);
            setField("opponentIncomingLines", 2);

            engine.addAttackToPlayer1(3, 4);

            controller.renderMyBoard();
            controller.renderOpponentBoard();
            controller.renderNextPieces();
            controller.renderIncomingLines();

            assertEquals(3, engine.getPendingAttacksToPlayer1());
        });
    }

    @Test
    void setupCanvasSizeConfiguresEveryCanvas() {
        runOnFxThreadAndWait(() -> controller.setupCanvasSize());

        assertEquals(GameBoard.BOARD_WIDTH * 25, myCanvas.getWidth());
        assertEquals(GameBoard.BOARD_HEIGHT * 25, myCanvas.getHeight());
        assertEquals(GameBoard.BOARD_WIDTH * 25, opponentCanvas.getWidth());
        assertEquals(GameBoard.BOARD_HEIGHT * 25, opponentCanvas.getHeight());
        assertEquals(6 * 25, myNextCanvas.getWidth());
        assertEquals(5 * 25, myNextCanvas.getHeight());
        assertEquals(6 * 25, opponentNextCanvas.getWidth());
        assertEquals(5 * 25, opponentNextCanvas.getHeight());
        assertEquals(6 * 25, myIncomingCanvas.getWidth());
        assertEquals(5 * 25, myIncomingCanvas.getHeight());
        assertEquals(6 * 25, opponentIncomingCanvas.getWidth());
        assertEquals(5 * 25, opponentIncomingCanvas.getHeight());
    }

    @Test
    void receiveAttackQueuesLinesForServerPlayer() {
        runOnFxThreadAndWait(() -> {
            setField("battleEngine", new BattleGameEngine("NORMAL"));
            setField("isServer", true);
            Map<String, Object> data = new HashMap<>();
            data.put("lines", 3);
            data.put("emptyCol", 4);
            controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.ATTACK, data));
        });

        waitForFxEvents();

        BattleGameEngine engine = (BattleGameEngine) getField("battleEngine");
        assertEquals(3, engine.getPendingAttacksToPlayer1());
    }

    @Test
    void receiveAttackQueuesLinesForClientPlayer() {
        runOnFxThreadAndWait(() -> {
            setField("battleEngine", new BattleGameEngine("NORMAL"));
            setField("isServer", false);
            Map<String, Object> data = new HashMap<>();
            data.put("lines", 2);
            data.put("emptyCol", 1);
            controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.ATTACK, data));
        });

        waitForFxEvents();

        BattleGameEngine engine = (BattleGameEngine) getField("battleEngine");
        assertEquals(2, engine.getPendingAttacksToPlayer2());
    }

    @Test
    void receiveGameOverMessageShowsVictoryUi() {
        TestAnimationTimer timer = new TestAnimationTimer();
        runOnFxThreadAndWait(() -> {
            setField("gameLoop", timer);
            Map<String, Object> payload = new HashMap<>();
            payload.put("isGameOver", true);
            controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.GAME_OVER, payload));
        });

        waitForFxEvents();

        assertEquals("승리!", statusLabel.getText());
        assertTrue(gameOverBox.isVisible());
        assertTrue(timer.isStopped(), "Game loop should be stopped when victory arrives");
    }

    @Test
    void gameStateUpdateStoresIncomingStateData() {
        GameStateData stateData = createGameStateData(1200, 4);

        runOnFxThreadAndWait(() ->
            controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.GAME_STATE_UPDATE, stateData))
        );

        waitForFxEvents();

        assertSame(stateData, getField("opponentState"));
        assertEquals(4, ((Integer) getField("opponentIncomingLines")).intValue());
    }

    @Test
    void receivePauseMessageTogglesStatusText() {
        runOnFxThreadAndWait(() -> {
            BattleGameEngine engine = new BattleGameEngine("NORMAL");
            engine.startGame();
            setField("battleEngine", engine);
            controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.PAUSE, true));
        });
        waitForFxEvents();
        assertEquals("일시 정지 (상대방)", statusLabel.getText());

        runOnFxThreadAndWait(() -> controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.PAUSE, false)));
        waitForFxEvents();
        assertEquals("", statusLabel.getText());
    }

    @Test
    void disconnectMessageStopsLoopAndShowsStatus() {
        TestAnimationTimer timer = new TestAnimationTimer();
        runOnFxThreadAndWait(() -> {
            setField("gameLoop", timer);
            controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.DISCONNECT, null));
        });

        waitForFxEvents();

        assertEquals("Opponent Left", statusLabel.getText());
        assertTrue(timer.isStopped());
    }

    @Test
    void updateLatencyDisplayAppliesThresholdColors() {
        runOnFxThreadAndWait(() -> {
            setField("currentRTT", 50L);
            controller.updateLatencyDisplay();
        });
        assertTrue(latencyLabel.getStyle().contains("#00ff00"));

        runOnFxThreadAndWait(() -> {
            setField("currentRTT", 250L);
            controller.updateLatencyDisplay();
        });
        assertTrue(latencyLabel.getStyle().contains("#ffaa00"));

        runOnFxThreadAndWait(() -> {
            setField("currentRTT", 600L);
            controller.updateLatencyDisplay();
        });
        assertTrue(latencyLabel.getStyle().contains("#ff0000"));
    }

    @Test
    void updateUIRefreshesTimerAndLagWarnings() {
        runOnFxThreadAndWait(() -> {
            BattleGameEngine engine = new BattleGameEngine("TIME_LIMIT");
            engine.startGame();
            setField("battleEngine", engine);
            setField("isServer", true);
            setField("isTimeLimitMode", true);
            setField("opponentState", createGameStateData(777, 0));
            setField("currentRTT", 600L);
            setField("lastRTTUpdateTime", System.nanoTime());

            controller.updateUI();

            assertEquals(String.valueOf(engine.getPlayer1Engine().getScore()), myScoreLabel.getText());
            assertEquals("777", opponentScoreLabel.getText());
            assertTrue(lagWarningLabel.isVisible());
            assertFalse(timerLabel.getText().isEmpty());
        });
    }


    @Test
    void sendAttackUsesClientTransport() throws Exception {
        GameClient client = mock(GameClient.class);

        runOnFxThreadAndWait(() -> {
            setField("isServer", false);
            setField("gameClient", client);
            controller.sendAttack(2, 5);
        });

        ArgumentCaptor<NetworkMessage> captor = ArgumentCaptor.forClass(NetworkMessage.class);
        verify(client).sendMessage(captor.capture());
        NetworkMessage sent = captor.getValue();
        assertEquals(NetworkMessage.MessageType.ATTACK, sent.getType());
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) sent.getData();
        assertEquals(2, payload.get("lines"));
        assertEquals(5, payload.get("emptyCol"));
    }

    @Test
    void setStatusMessageAdjustsFont() {
        runOnFxThreadAndWait(() -> {
            invokeSetStatusMessage("WIN", "#ffffff");
            assertTrue(statusLabel.getStyle().contains("24px"));

            invokeSetStatusMessage("이 메시지는 상당히 길어서 작은 폰트를 사용해야 합니다", "#ffffff");
            assertTrue(statusLabel.getStyle().contains("16px"));
        });
    }

    @Test
    void sendMyStateUsesServerTransport() throws Exception {
        GameServer server = mock(GameServer.class);

        runOnFxThreadAndWait(() -> {
            BattleGameEngine engine = new BattleGameEngine("NORMAL");
            engine.startGame();
            setField("battleEngine", engine);
            setField("isServer", true);
            setField("gameServer", server);
            controller.sendMyState();
        });

        ArgumentCaptor<NetworkMessage> captor = ArgumentCaptor.forClass(NetworkMessage.class);
        verify(server).sendMessage(captor.capture());
        NetworkMessage sent = captor.getValue();
        assertEquals(NetworkMessage.MessageType.GAME_STATE_UPDATE, sent.getType());
        assertTrue(sent.getData() instanceof GameStateData);
    }

    @Test
    void timeUpMessageStopsGameAndShowsResult() {
        TestAnimationTimer timer = new TestAnimationTimer();
        runOnFxThreadAndWait(() -> {
            BattleGameEngine engine = new BattleGameEngine("NORMAL");
            engine.startGame();
            setField("battleEngine", engine);
            setField("isServer", true);
            setField("gameLoop", timer);
            setEngineScore(engine.getPlayer1Engine(), 800);
            Map<String, Object> payload = new HashMap<>();
            payload.put("myScore", 600);
            controller.receiveNetworkMessage(new NetworkMessage(NetworkMessage.MessageType.TIME_UP, payload));
        });

        waitForFxEvents();

        assertEquals("시간 종료! 승리!", statusLabel.getText());
        assertTrue(gameOverBox.isVisible());
        assertTrue(timer.isStopped());
    }

    @Test
    void setGameModeUpdatesLabelWithLocalizedText() {
        runOnFxThreadAndWait(() -> controller.setGameMode("ITEM"));
        assertEquals("아이템 모드", gameModeLabel.getText());

        runOnFxThreadAndWait(() -> controller.setGameMode("TIME_LIMIT"));
        assertEquals("시간제한 모드", gameModeLabel.getText());
    }
}
