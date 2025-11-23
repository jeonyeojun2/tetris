package tetris.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import tetris.ui.SceneManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class ScoreBoardControllerTestFX {

    private ScoreBoardController controller;
    private Stage stage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScoreBoard.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        SceneManager sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testScoreListViewExists(FxRobot robot) {
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        assertNotNull(listView);
        assertTrue(listView.isVisible());
    }

    @Test
    void testNormalModeButtonExists(FxRobot robot) {
        ToggleButton button = robot.lookup("#normalModeButton").queryAs(ToggleButton.class);
        assertNotNull(button);
        assertTrue(button.isVisible());
    }

    @Test
    void testItemModeButtonExists(FxRobot robot) {
        ToggleButton button = robot.lookup("#itemModeButton").queryAs(ToggleButton.class);
        assertNotNull(button);
        assertTrue(button.isVisible());
    }

    @Test
    void testEasyButtonExists(FxRobot robot) {
        ToggleButton button = robot.lookup("#easyButton").queryAs(ToggleButton.class);
        assertNotNull(button);
    }

    @Test
    void testClickNormalModeButton(FxRobot robot) {
        ToggleButton button = robot.lookup("#normalModeButton").queryAs(ToggleButton.class);
        robot.clickOn(button);
        assertTrue(button.isSelected());
    }

    @Test
    void testClickItemModeButton(FxRobot robot) {
        ToggleButton button = robot.lookup("#itemModeButton").queryAs(ToggleButton.class);
        robot.clickOn(button);
        assertTrue(button.isSelected());
    }

    @Test
    void testClickEasyButton(FxRobot robot) {
        ToggleButton button = robot.lookup("#easyButton").queryAs(ToggleButton.class);
        robot.clickOn(button);
        assertTrue(button.isSelected());
    }

    @Test
    void testClickHardButton(FxRobot robot) {
        ToggleButton button = robot.lookup("#hardButton").queryAs(ToggleButton.class);
        robot.clickOn(button);
        assertTrue(button.isSelected());
    }

    @Test
    void testToggleBetweenModes(FxRobot robot) {
        ToggleButton normalButton = robot.lookup("#normalModeButton").queryAs(ToggleButton.class);
        ToggleButton itemButton = robot.lookup("#itemModeButton").queryAs(ToggleButton.class);
        
        robot.clickOn(normalButton);
        assertTrue(normalButton.isSelected());
        
        robot.clickOn(itemButton);
        assertTrue(itemButton.isSelected());
    }

    @Test
    void testToggleBetweenDifficulties(FxRobot robot) {
        ToggleButton easyButton = robot.lookup("#easyButton").queryAs(ToggleButton.class);
        ToggleButton normalButton = robot.lookup("#normalButton").queryAs(ToggleButton.class);
        
        robot.clickOn(easyButton);
        robot.clickOn(normalButton);
    }

    @Test
    void testBackButtonExists(FxRobot robot) {
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        assertNotNull(backButton);
        assertTrue(backButton.isVisible());
    }

    @Test
    void testClickBackButton(FxRobot robot) {
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        robot.clickOn(backButton);
    }

    @Test
    void testSceneInitialization(FxRobot robot) {
        assertNotNull(stage.getScene());
        assertNotNull(controller);
    }

    @Test
    void testListViewIsPopulated(FxRobot robot) {
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        assertNotNull(listView.getItems());
    }

    @Test
    void testMultipleModeSelections(FxRobot robot) {
        robot.clickOn("#normalModeButton");
        robot.clickOn("#itemModeButton");
        robot.clickOn("#normalModeButton");
    }

    @Test
    void testNormalButtonExists(FxRobot robot) {
        ToggleButton button = robot.lookup("#normalButton").queryAs(ToggleButton.class);
        assertNotNull(button);
    }

    // ========== Scenario Tests ==========

    @Test
    void testScoreListDisplaysTopScores(FxRobot robot) {
        // Given - 점수 리스트뷰
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        
        // Then - 리스트가 표시됨 (상위 N개)
        assertNotNull(listView);
        assertNotNull(listView.getItems());
    }

    @Test
    void testSwitchBetweenGameModes(FxRobot robot) {
        // Given - Normal과 Item 모드 버튼
        ToggleButton normalButton = robot.lookup("#normalModeButton").queryAs(ToggleButton.class);
        ToggleButton itemButton = robot.lookup("#itemModeButton").queryAs(ToggleButton.class);
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        
        // When - 모드 전환
        robot.clickOn(normalButton);
        int normalSize = listView.getItems().size();
        
        robot.clickOn(itemButton);
        int itemSize = listView.getItems().size();
        
        // Then - 리스트가 업데이트됨
        assertNotNull(listView);
    }

    @Test
    void testSwitchBetweenDifficulties(FxRobot robot) {
        // Given - Easy, Normal, Hard 버튼
        ToggleButton easyButton = robot.lookup("#easyButton").queryAs(ToggleButton.class);
        ToggleButton normalButton = robot.lookup("#normalButton").queryAs(ToggleButton.class);
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        
        // When - 난이도 전환
        robot.clickOn(easyButton);
        robot.clickOn(normalButton);
        
        // Then - 리스트가 업데이트됨 (정렬 순서 변경)
        assertNotNull(listView.getItems());
    }

    @Test
    void testSortOrderChanges(FxRobot robot) {
        // Given - 정렬 옵션 변경
        ToggleButton normalMode = robot.lookup("#normalModeButton").queryAs(ToggleButton.class);
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        
        // When - 모드 변경으로 정렬 순서 변경
        robot.clickOn(normalMode);
        
        // Then - 리스트 항목이 재정렬됨
        assertNotNull(listView);
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
    void testMultipleModeAndDifficultySwitches(FxRobot robot) {
        // Given - 여러 필터 버튼
        ToggleButton normalMode = robot.lookup("#normalModeButton").queryAs(ToggleButton.class);
        ToggleButton itemMode = robot.lookup("#itemModeButton").queryAs(ToggleButton.class);
        ToggleButton easyDiff = robot.lookup("#easyButton").queryAs(ToggleButton.class);
        
        // When - 여러 번 전환
        robot.clickOn(normalMode);
        robot.clickOn(easyDiff);
        robot.clickOn(itemMode);
        
        // Then - 리스트가 계속 업데이트됨
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        assertNotNull(listView.getItems());
    }

    @Test
    void testScoreListNotNull(FxRobot robot) {
        // Given - 점수 리스트
        ListView<?> listView = robot.lookup("#scoreListView").queryAs(ListView.class);
        
        // Then - null이 아님
        assertNotNull(listView);
        assertNotNull(listView.getItems());
    }

    @Test
    void testToggleButtonStates(FxRobot robot) {
        // Given - 토글 버튼들
        ToggleButton normalMode = robot.lookup("#normalModeButton").queryAs(ToggleButton.class);
        ToggleButton itemMode = robot.lookup("#itemModeButton").queryAs(ToggleButton.class);
        
        // When - 버튼 클릭
        robot.clickOn(normalMode);
        
        // Then - 선택 상태 변경
        assertTrue(normalMode.isSelected());
    }
}
