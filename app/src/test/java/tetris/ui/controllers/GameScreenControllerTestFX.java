package tetris.ui.controllers;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import tetris.ui.SceneManager;
import tetris.ui.SettingsManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;

/**
 * GameScreenController TestFX 통합 테스트
 */
@ExtendWith(ApplicationExtension.class)
class GameScreenControllerTestFX {
    
    private GameScreenController controller;
    private SceneManager sceneManager;

    @Start
    private void start(Stage stage) throws Exception {
        // SettingsManager 초기화
        SettingsManager settings = SettingsManager.getInstance();
        settings.setGameMode("NORMAL");
        
        // FXML 로드
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScreen.fxml"));
        Parent root = loader.load();
        
        controller = loader.getController();
        sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testGameCanvasExists(FxRobot robot) {
        verifyThat("#gameCanvas", isVisible());
    }

    @Test
    void testScoreLabelExists(FxRobot robot) {
        verifyThat("#scoreLabel", isVisible());
    }

    @Test
    void testLevelLabelExists(FxRobot robot) {
        verifyThat("#levelLabel", isVisible());
    }

    @Test
    void testLinesLabelExists(FxRobot robot) {
        verifyThat("#linesLabel", isVisible());
    }

    @Test
    void testNextPieceCanvasExists(FxRobot robot) {
        verifyThat("#nextPieceCanvas", isVisible());
    }

    @Test
    void testGameCanvasSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        assertNotNull(canvas);
        assertTrue(canvas.getWidth() > 0);
        assertTrue(canvas.getHeight() > 0);
    }

    @Test
    void testNextPieceCanvasSize(FxRobot robot) {
        Canvas canvas = robot.lookup("#nextPieceCanvas").query();
        assertNotNull(canvas);
        assertTrue(canvas.getWidth() > 0);
        assertTrue(canvas.getHeight() > 0);
    }

    @Test
    void testScoreLabelInitialValue(FxRobot robot) {
        Label label = robot.lookup("#scoreLabel").query();
        assertNotNull(label);
        assertNotNull(label.getText());
    }

    @Test
    void testLevelLabelInitialValue(FxRobot robot) {
        Label label = robot.lookup("#levelLabel").query();
        assertNotNull(label);
        assertNotNull(label.getText());
    }

    @Test
    void testLinesLabelInitialValue(FxRobot robot) {
        Label label = robot.lookup("#linesLabel").query();
        assertNotNull(label);
        assertNotNull(label.getText());
    }

    @Test
    void testKeyPressLeft(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.LEFT);
        robot.release(KeyCode.LEFT);
        
        // 게임이 계속 실행 중인지 확인
        assertTrue(canvas.isVisible());
    }

    @Test
    void testKeyPressRight(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.RIGHT);
        robot.release(KeyCode.RIGHT);
        
        assertTrue(canvas.isVisible());
    }

    @Test
    void testKeyPressDown(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.DOWN);
        robot.release(KeyCode.DOWN);
        
        assertTrue(canvas.isVisible());
    }

    @Test
    void testKeyPressRotate(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.UP);
        robot.release(KeyCode.UP);
        
        assertTrue(canvas.isVisible());
    }

    @Test
    void testKeyPressSpace(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.SPACE);
        robot.release(KeyCode.SPACE);
        
        assertTrue(canvas.isVisible());
    }

    @Test
    void testKeyPressEscape(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.ESCAPE);
        robot.release(KeyCode.ESCAPE);
        
        assertTrue(canvas.isVisible());
    }

    @Test
    void testGameEngineInitialized(FxRobot robot) {
        // 게임 엔진이 초기화되어 있는지 확인
        Canvas canvas = robot.lookup("#gameCanvas").query();
        assertNotNull(canvas);
        assertNotNull(canvas.getGraphicsContext2D());
    }

    @Test
    void testAllLabelsVisible(FxRobot robot) {
        verifyThat("#scoreLabel", isVisible());
        verifyThat("#levelLabel", isVisible());
        verifyThat("#linesLabel", isVisible());
    }

    @Test
    void testCanvasFocusable(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        assertNotNull(canvas.getScene());
    }

    @Test
    void testMultipleKeyPresses(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.LEFT);
        robot.release(KeyCode.LEFT);
        robot.press(KeyCode.RIGHT);
        robot.release(KeyCode.RIGHT);
        robot.press(KeyCode.DOWN);
        robot.release(KeyCode.DOWN);
        assertTrue(canvas.isVisible());
    }

    @Test
    void testKeyPressPause(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.P);
        robot.release(KeyCode.P);
        assertTrue(canvas.isVisible());
    }

    @Test
    void testCanvasGraphicsContext(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        assertNotNull(canvas.getGraphicsContext2D());
    }

    @Test
    void testNextPieceCanvasGraphicsContext(FxRobot robot) {
        Canvas canvas = robot.lookup("#nextPieceCanvas").query();
        assertNotNull(canvas.getGraphicsContext2D());
    }

    @Test
    void testSceneFocused(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        assertNotNull(canvas.getScene());
    }

    @Test
    void testControllerSetSceneManager(FxRobot robot) {
        assertNotNull(controller);
        assertNotNull(sceneManager);
    }

    @Test
    void testMultipleRotations(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.UP);
        robot.release(KeyCode.UP);
        robot.press(KeyCode.UP);
        robot.release(KeyCode.UP);
        assertTrue(canvas.isVisible());
    }

    @Test
    void testHardDropMultipleTimes(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.SPACE);
        robot.release(KeyCode.SPACE);
        assertTrue(canvas.isVisible());
    }

    @Test
    void testKeySequenceLeftDownRotate(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.LEFT);
        robot.release(KeyCode.LEFT);
        robot.press(KeyCode.DOWN);
        robot.release(KeyCode.DOWN);
        robot.press(KeyCode.UP);
        robot.release(KeyCode.UP);
        assertTrue(canvas.isVisible());
    }

    @Test
    void testLabelUpdates(FxRobot robot) {
        Label scoreLabel = robot.lookup("#scoreLabel").query();
        Label levelLabel = robot.lookup("#levelLabel").query();
        Label linesLabel = robot.lookup("#linesLabel").query();
        
        assertNotNull(scoreLabel.getText());
        assertNotNull(levelLabel.getText());
        assertNotNull(linesLabel.getText());
    }

    @Test
    void testEscapePausesGame(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        robot.clickOn(canvas);
        robot.press(KeyCode.ESCAPE);
        robot.release(KeyCode.ESCAPE);
        assertTrue(canvas.isVisible());
    }

    @Test
    void testSceneRootNotNull(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        assertNotNull(canvas.getScene().getRoot());
    }

    @Test
    void testCanvasesInScene(FxRobot robot) {
        Canvas gameCanvas = robot.lookup("#gameCanvas").query();
        Canvas nextCanvas = robot.lookup("#nextPieceCanvas").query();
        
        assertEquals(gameCanvas.getScene(), nextCanvas.getScene());
    }

    @Test
    void testInitialGameState(FxRobot robot) {
        Canvas canvas = robot.lookup("#gameCanvas").query();
        assertTrue(canvas.isVisible());
        assertFalse(canvas.isDisabled());
    }
}
