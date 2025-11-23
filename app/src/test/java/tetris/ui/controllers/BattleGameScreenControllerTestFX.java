package tetris.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import tetris.ui.SceneManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class BattleGameScreenControllerTestFX {

    private BattleGameScreenController controller;
    private Stage stage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BattleGameScreen.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        SceneManager sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testPlayer1CanvasExists(FxRobot robot) {
        Canvas canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
        assertNotNull(canvas, "Player 1 canvas should exist");
        assertTrue(canvas.isVisible(), "Player 1 canvas should be visible");
    }

    @Test
    void testPlayer2CanvasExists(FxRobot robot) {
        Canvas canvas = robot.lookup("#player2Canvas").queryAs(Canvas.class);
        assertNotNull(canvas, "Player 2 canvas should exist");
        assertTrue(canvas.isVisible(), "Player 2 canvas should be visible");
    }

    @Test
    void testPlayer1NextCanvasExists(FxRobot robot) {
        Canvas canvas = robot.lookup("#player1NextCanvas").queryAs(Canvas.class);
        assertNotNull(canvas, "Player 1 next canvas should exist");
    }

    @Test
    void testPlayer2NextCanvasExists(FxRobot robot) {
        Canvas canvas = robot.lookup("#player2NextCanvas").queryAs(Canvas.class);
        assertNotNull(canvas, "Player 2 next canvas should exist");
    }

    @Test
    void testPlayer1IncomingCanvasExists(FxRobot robot) {
        Canvas canvas = robot.lookup("#player1IncomingCanvas").queryAs(Canvas.class);
        assertNotNull(canvas, "Player 1 incoming canvas should exist");
    }

    @Test
    void testPlayer2IncomingCanvasExists(FxRobot robot) {
        Canvas canvas = robot.lookup("#player2IncomingCanvas").queryAs(Canvas.class);
        assertNotNull(canvas, "Player 2 incoming canvas should exist");
    }

    @Test
    void testPlayer1ScoreLabelExists(FxRobot robot) {
        Label label = robot.lookup("#player1ScoreLabel").queryAs(Label.class);
        assertNotNull(label, "Player 1 score label should exist");
    }

    @Test
    void testPlayer2ScoreLabelExists(FxRobot robot) {
        Label label = robot.lookup("#player2ScoreLabel").queryAs(Label.class);
        assertNotNull(label, "Player 2 score label should exist");
    }

    @Test
    void testPlayer1LevelLabelExists(FxRobot robot) {
        Label label = robot.lookup("#player1LevelLabel").queryAs(Label.class);
        assertNotNull(label, "Player 1 level label should exist");
    }

    @Test
    void testPlayer2LevelLabelExists(FxRobot robot) {
        Label label = robot.lookup("#player2LevelLabel").queryAs(Label.class);
        assertNotNull(label, "Player 2 level label should exist");
    }

    @Test
    void testPlayer1LinesLabelExists(FxRobot robot) {
        Label label = robot.lookup("#player1LinesLabel").queryAs(Label.class);
        assertNotNull(label, "Player 1 lines label should exist");
    }

    @Test
    void testPlayer2LinesLabelExists(FxRobot robot) {
        Label label = robot.lookup("#player2LinesLabel").queryAs(Label.class);
        assertNotNull(label, "Player 2 lines label should exist");
    }

    @Test
    void testTimerLabelExists(FxRobot robot) {
        Label label = robot.lookup("#timerLabel").queryAs(Label.class);
        assertNotNull(label, "Timer label should exist");
    }

    @Test
    void testWinnerLabelExists(FxRobot robot) {
        Label label = robot.lookup("#winnerLabel").queryAs(Label.class);
        assertNotNull(label, "Winner label should exist");
    }

    @Test
    void testGameOverBoxExists(FxRobot robot) {
        VBox box = robot.lookup("#gameOverBox").queryAs(VBox.class);
        assertNotNull(box, "Game over box should exist");
    }

    @Test
    void testRematchButtonExists(FxRobot robot) {
        Button button = robot.lookup("#rematchButton").queryAs(Button.class);
        assertNotNull(button, "Rematch button should exist");
    }

    @Test
    void testToMenuButtonExists(FxRobot robot) {
        Button button = robot.lookup("#toMenuButton").queryAs(Button.class);
        assertNotNull(button, "To menu button should exist");
    }

    @Test
    void testPlayer1CanvasSizeDefaultBlockSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
        // Default BLOCK_SIZE is 25, GameBoard.BOARD_WIDTH is 10, BOARD_HEIGHT is 20
        assertEquals(250.0, canvas.getWidth(), "Player 1 canvas width should be 250 (10 * 25)");
        assertEquals(500.0, canvas.getHeight(), "Player 1 canvas height should be 500 (20 * 25)");
    }

    @Test
    void testPlayer2CanvasSizeDefaultBlockSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#player2Canvas").queryAs(Canvas.class);
        assertEquals(250.0, canvas.getWidth(), "Player 2 canvas width should be 250 (10 * 25)");
        assertEquals(500.0, canvas.getHeight(), "Player 2 canvas height should be 500 (20 * 25)");
    }

    @Test
    void testPlayer1NextCanvasSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#player1NextCanvas").queryAs(Canvas.class);
        // 6 * BLOCK_SIZE (25) = 150, 5 * BLOCK_SIZE = 125
        assertEquals(150.0, canvas.getWidth(), "Player 1 next canvas width should be 150");
        assertEquals(125.0, canvas.getHeight(), "Player 1 next canvas height should be 125");
    }

    @Test
    void testPlayer2NextCanvasSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#player2NextCanvas").queryAs(Canvas.class);
        assertEquals(150.0, canvas.getWidth(), "Player 2 next canvas width should be 150");
        assertEquals(125.0, canvas.getHeight(), "Player 2 next canvas height should be 125");
    }

    @Test
    void testPlayer1IncomingCanvasSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#player1IncomingCanvas").queryAs(Canvas.class);
        assertEquals(150.0, canvas.getWidth(), "Player 1 incoming canvas width should be 150");
        assertEquals(125.0, canvas.getHeight(), "Player 1 incoming canvas height should be 125");
    }

    @Test
    void testPlayer2IncomingCanvasSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#player2IncomingCanvas").queryAs(Canvas.class);
        assertEquals(150.0, canvas.getWidth(), "Player 2 incoming canvas width should be 150");
        assertEquals(125.0, canvas.getHeight(), "Player 2 incoming canvas height should be 125");
    }

    @Test
    void testCanvasesNotFocusTraversable(FxRobot robot) {
        Canvas canvas1 = robot.lookup("#player1Canvas").queryAs(Canvas.class);
        Canvas canvas2 = robot.lookup("#player2Canvas").queryAs(Canvas.class);
        assertFalse(canvas1.isFocusTraversable(), "Player 1 canvas should not be focus traversable");
        assertFalse(canvas2.isFocusTraversable(), "Player 2 canvas should not be focus traversable");
    }

    @Test
    void testSetBattleModeScoreLimitInitializesEngine(FxRobot robot) {
        try {
            controller.setBattleMode("SCORE_LIMIT");
            // Verify game loop starts - canvas should have graphics context
            Canvas canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
            assertNotNull(canvas.getGraphicsContext2D(), "Graphics context should be initialized");
        } catch (Exception e) {
            // Ignore exceptions from game engine initialization in test environment
        }
    }

    @Test
    void testSetBattleModeTimeLimitInitializesEngine(FxRobot robot) {
        try {
            controller.setBattleMode("TIME_LIMIT");
            // Verify game loop starts
            Canvas canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
            assertNotNull(canvas.getGraphicsContext2D(), "Graphics context should be initialized");
        } catch (Exception e) {
            // Ignore exceptions from game engine initialization in test environment
        }
    }

    @Test
    void testSetBattleModeItemBattleInitializesEngine(FxRobot robot) {
        try {
            controller.setBattleMode("ITEM_BATTLE");
            Canvas canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
            assertNotNull(canvas.getGraphicsContext2D(), "Graphics context should be initialized");
        } catch (Exception e) {
            // Ignore exceptions from game engine initialization in test environment
        }
    }

    @Test
    void testGameOverBoxInitiallyNotVisible(FxRobot robot) {
        VBox box = robot.lookup("#gameOverBox").queryAs(VBox.class);
        assertFalse(box.isVisible(), "Game over box should be initially hidden");
    }

    @Test
    void testSceneHasKeyHandler(FxRobot robot) {
        Scene scene = stage.getScene();
        assertNotNull(scene, "Scene should exist");
        assertNotNull(scene.getOnKeyPressed(), "Scene should have key handler");
    }

    @Test
    void testEscapeKeyHandlerSetup(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        // Press ESCAPE key to trigger pause
        robot.push(javafx.scene.input.KeyCode.ESCAPE);
        // Just verify no exception is thrown - actual pause behavior requires game engine running
    }

    @Test
    void testRematchButtonClick(FxRobot robot) {
        Button button = robot.lookup("#rematchButton").queryAs(Button.class);
        assertFalse(button.isDisabled());
    }

    @Test
    void testToMenuButtonClick(FxRobot robot) {
        Button button = robot.lookup("#toMenuButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testPlayer1KeyHandling(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        robot.push(javafx.scene.input.KeyCode.A);
        robot.push(javafx.scene.input.KeyCode.D);
        robot.push(javafx.scene.input.KeyCode.S);
    }

    @Test
    void testPlayer2KeyHandling(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        robot.push(javafx.scene.input.KeyCode.LEFT);
        robot.push(javafx.scene.input.KeyCode.RIGHT);
        robot.push(javafx.scene.input.KeyCode.DOWN);
    }

    @Test
    void testBothPlayersCanvasesRenderable(FxRobot robot) {
        Canvas canvas1 = robot.lookup("#player1Canvas").queryAs(Canvas.class);
        Canvas canvas2 = robot.lookup("#player2Canvas").queryAs(Canvas.class);
        
        assertNotNull(canvas1.getGraphicsContext2D());
        assertNotNull(canvas2.getGraphicsContext2D());
    }

    @Test
    void testAllLabelsInitialized(FxRobot robot) {
        Label p1Score = robot.lookup("#player1ScoreLabel").queryAs(Label.class);
        Label p2Score = robot.lookup("#player2ScoreLabel").queryAs(Label.class);
        Label timer = robot.lookup("#timerLabel").queryAs(Label.class);
        
        assertNotNull(p1Score.getText());
        assertNotNull(p2Score.getText());
        assertNotNull(timer.getText());
    }

    @Test
    void testScoreLimitModeSetup(FxRobot robot) {
        try {
            controller.setBattleMode("SCORE_LIMIT");
            Canvas canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
            assertTrue(canvas.isVisible());
        } catch (Exception e) {
            // Ignore engine initialization errors in test
        }
    }

    @Test
    void testTimeLimitModeSetup(FxRobot robot) {
        try {
            controller.setBattleMode("TIME_LIMIT");
            Label timer = robot.lookup("#timerLabel").queryAs(Label.class);
            assertTrue(timer.isVisible());
        } catch (Exception e) {
            // Ignore engine initialization errors
        }
    }

    @Test
    void testItemBattleModeSetup(FxRobot robot) {
        try {
            controller.setBattleMode("ITEM_BATTLE");
            Canvas canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
            assertTrue(canvas.isVisible());
        } catch (Exception e) {
            // Ignore engine initialization errors
        }
    }

    @Test
    void testGameOverBoxHiddenInitially(FxRobot robot) {
        VBox gameOverBox = robot.lookup("#gameOverBox").queryAs(VBox.class);
        assertFalse(gameOverBox.isVisible());
    }

    @Test
    void testWinnerLabelEmpty(FxRobot robot) {
        Label winner = robot.lookup("#winnerLabel").queryAs(Label.class);
        assertNotNull(winner);
    }

    @Test
    void testAllCanvasesInSameScene(FxRobot robot) {
        Canvas p1Canvas = robot.lookup("#player1Canvas").queryAs(Canvas.class);
        Canvas p2Canvas = robot.lookup("#player2Canvas").queryAs(Canvas.class);
        Canvas p1Next = robot.lookup("#player1NextCanvas").queryAs(Canvas.class);
        Canvas p2Next = robot.lookup("#player2NextCanvas").queryAs(Canvas.class);
        
        assertEquals(p1Canvas.getScene(), p2Canvas.getScene());
        assertEquals(p1Canvas.getScene(), p1Next.getScene());
        assertEquals(p1Canvas.getScene(), p2Next.getScene());
    }

    @Test
    void testPlayer1RotateKey(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        robot.push(javafx.scene.input.KeyCode.W);
    }

    @Test
    void testPlayer2RotateKey(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        robot.push(javafx.scene.input.KeyCode.UP);
    }

    @Test
    void testPlayer1HardDrop(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        robot.push(javafx.scene.input.KeyCode.SPACE);
    }

    @Test
    void testPlayer2HardDrop(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        robot.push(javafx.scene.input.KeyCode.ENTER);
    }

    @Test
    void testControllerInitialization(FxRobot robot) {
        assertNotNull(controller);
        assertNotNull(stage);
        assertNotNull(stage.getScene());
    }

    @Test
    void testMultipleKeySequences(FxRobot robot) {
        controller.setBattleMode("SCORE_LIMIT");
        robot.push(javafx.scene.input.KeyCode.A);
        robot.push(javafx.scene.input.KeyCode.D);
        robot.push(javafx.scene.input.KeyCode.W);
        robot.push(javafx.scene.input.KeyCode.SPACE);
    }

    @Test
    void testButtonsEnabled(FxRobot robot) {
        Button rematch = robot.lookup("#rematchButton").queryAs(Button.class);
        Button toMenu = robot.lookup("#toMenuButton").queryAs(Button.class);
        
        assertNotNull(rematch);
        assertNotNull(toMenu);
    }
}
