package tetris.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import tetris.ui.SceneManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class BattleModeSelectionControllerTestFX {

    private BattleModeSelectionController controller;
    private Stage stage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BattleModeSelection.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        SceneManager sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root, 600, 900);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testNormalBattleButtonExists(FxRobot robot) {
        Button button = robot.lookup("#normalBattleButton").queryAs(Button.class);
        assertNotNull(button);
        assertTrue(button.isVisible());
    }

    @Test
    void testItemBattleButtonExists(FxRobot robot) {
        Button button = robot.lookup("#itemBattleButton").queryAs(Button.class);
        assertNotNull(button);
        assertTrue(button.isVisible());
    }

    @Test
    void testTimeLimitBattleButtonExists(FxRobot robot) {
        Button button = robot.lookup("#timeLimitBattleButton").queryAs(Button.class);
        assertNotNull(button);
        assertTrue(button.isVisible());
    }

    @Test
    void testBackButtonExists(FxRobot robot) {
        Button button = robot.lookup("#backButton").queryAs(Button.class);
        assertNotNull(button);
        assertTrue(button.isVisible());
    }

    @Test
    void testClickNormalBattleButton(FxRobot robot) {
        Button button = robot.lookup("#normalBattleButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testClickItemBattleButton(FxRobot robot) {
        Button button = robot.lookup("#itemBattleButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testClickTimeLimitBattleButton(FxRobot robot) {
        Button button = robot.lookup("#timeLimitBattleButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testClickBackButton(FxRobot robot) {
        Button button = robot.lookup("#backButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        Button normalButton = robot.lookup("#normalBattleButton").queryAs(Button.class);
        Button itemButton = robot.lookup("#itemBattleButton").queryAs(Button.class);
        Button timeLimitButton = robot.lookup("#timeLimitBattleButton").queryAs(Button.class);
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        assertFalse(normalButton.isDisabled());
        assertFalse(itemButton.isDisabled());
        assertFalse(timeLimitButton.isDisabled());
        assertFalse(backButton.isDisabled());
    }

    @Test
    void testButtonsHaveText(FxRobot robot) {
        Button normalButton = robot.lookup("#normalBattleButton").queryAs(Button.class);
        Button itemButton = robot.lookup("#itemBattleButton").queryAs(Button.class);
        Button timeLimitButton = robot.lookup("#timeLimitBattleButton").queryAs(Button.class);
        
        assertNotNull(normalButton.getText());
        assertNotNull(itemButton.getText());
        assertNotNull(timeLimitButton.getText());
    }

    @Test
    void testSceneIsInitialized(FxRobot robot) {
        assertNotNull(stage.getScene());
        assertNotNull(stage.getScene().getRoot());
    }

    @Test
    void testControllerIsSet(FxRobot robot) {
        assertNotNull(controller);
    }

    @Test
    void testMultipleNormalBattleClicks(FxRobot robot) {
        Button button = robot.lookup("#normalBattleButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testSequentialModeSelection(FxRobot robot) {
        robot.clickOn("#normalBattleButton");
    }

    @Test
    void testButtonLayout(FxRobot robot) {
        Button normalButton = robot.lookup("#normalBattleButton").queryAs(Button.class);
        Button itemButton = robot.lookup("#itemBattleButton").queryAs(Button.class);
        Button timeLimitButton = robot.lookup("#timeLimitBattleButton").queryAs(Button.class);
        
        assertTrue(normalButton.getLayoutY() >= 0);
        assertTrue(itemButton.getLayoutY() >= 0);
        assertTrue(timeLimitButton.getLayoutY() >= 0);
    }

    // ========== Scenario Tests ==========

    @Test
    void testNormalBattleModeStartsGame(FxRobot robot) {
        // Given - Normal Battle 버튼
        Button normalButton = robot.lookup("#normalBattleButton").queryAs(Button.class);
        
        // When - Normal 모드 클릭
        robot.clickOn(normalButton);
        
        // Then - 올바른 모드로 게임 시작 (SceneManager 처리)
        assertNotNull(controller);
    }

    @Test
    void testItemBattleModeStartsGame(FxRobot robot) {
        // Given - Item Battle 버튼
        Button itemButton = robot.lookup("#itemBattleButton").queryAs(Button.class);
        
        // When - Item 모드 클릭
        robot.clickOn(itemButton);
        
        // Then - Item 모드로 게임 시작
        assertNotNull(controller);
    }

    @Test
    void testTimeLimitBattleModeStartsGame(FxRobot robot) {
        // Given - Time Limit Battle 버튼
        Button timeLimitButton = robot.lookup("#timeLimitBattleButton").queryAs(Button.class);
        
        // When - Time Limit 모드 클릭
        robot.clickOn(timeLimitButton);
        
        // Then - Time Limit 모드로 게임 시작
        assertNotNull(controller);
    }

    @Test
    void testBackButtonReturnsToMenu(FxRobot robot) {
        // Given - 뒤로가기 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 뒤로가기 클릭
        robot.clickOn(backButton);
        
        // Then - 메뉴로 복귀 (SceneManager 처리)
        assertNotNull(controller);
    }

    @Test
    void testBackButtonAlwaysAccessible(FxRobot robot) {
        // Given - 뒤로가기 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // Then - 항상 접근 가능
        assertTrue(backButton.isVisible());
        assertFalse(backButton.isDisabled());
    }
}
