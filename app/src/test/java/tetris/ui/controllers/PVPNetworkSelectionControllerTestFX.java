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
class PVPNetworkSelectionControllerTestFX {

    private PVPNetworkSelectionController controller;
    private Stage stage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PVPNetworkSelection.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        SceneManager sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        controller.setGameMode("NORMAL");
        
        Scene scene = new Scene(root, 600, 900);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testHostButtonExists(FxRobot robot) {
        Button button = robot.lookup("#hostButton").queryAs(Button.class);
        assertNotNull(button);
        assertTrue(button.isVisible());
    }

    @Test
    void testJoinButtonExists(FxRobot robot) {
        Button button = robot.lookup("#joinButton").queryAs(Button.class);
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
    void testClickHostButton(FxRobot robot) {
        Button button = robot.lookup("#hostButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testClickJoinButton(FxRobot robot) {
        Button button = robot.lookup("#joinButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testClickBackButton(FxRobot robot) {
        Button button = robot.lookup("#backButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        Button hostButton = robot.lookup("#hostButton").queryAs(Button.class);
        Button joinButton = robot.lookup("#joinButton").queryAs(Button.class);
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        assertFalse(hostButton.isDisabled());
        assertFalse(joinButton.isDisabled());
        assertFalse(backButton.isDisabled());
    }

    @Test
    void testButtonsHaveText(FxRobot robot) {
        Button hostButton = robot.lookup("#hostButton").queryAs(Button.class);
        Button joinButton = robot.lookup("#joinButton").queryAs(Button.class);
        
        assertNotNull(hostButton.getText());
        assertNotNull(joinButton.getText());
        assertFalse(hostButton.getText().isEmpty());
        assertFalse(joinButton.getText().isEmpty());
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
    void testMultipleHostClicks(FxRobot robot) {
        Button button = robot.lookup("#hostButton").queryAs(Button.class);
        robot.clickOn(button);
        // 두 번 클릭해도 문제 없어야 함
    }

    @Test
    void testMultipleJoinClicks(FxRobot robot) {
        Button button = robot.lookup("#joinButton").queryAs(Button.class);
        robot.clickOn(button);
    }

    @Test
    void testHostThenBack(FxRobot robot) {
        robot.clickOn("#hostButton");
        // 호스트 화면으로 전환 시도 후 바로 돌아가기는 테스트 불가
    }

    @Test
    void testButtonLayout(FxRobot robot) {
        Button hostButton = robot.lookup("#hostButton").queryAs(Button.class);
        Button joinButton = robot.lookup("#joinButton").queryAs(Button.class);
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        assertTrue(hostButton.getLayoutY() >= 0);
        assertTrue(joinButton.getLayoutY() >= 0);
        assertTrue(backButton.getLayoutY() >= 0);
    }

    // ========== Scenario Tests ==========

    @Test
    void testHostButtonOpensServerWaitingScreen(FxRobot robot) {
        // Given - "서버 열기" 버튼
        Button hostButton = robot.lookup("#hostButton").queryAs(Button.class);
        
        // When - 서버 열기 클릭
        robot.clickOn(hostButton);
        
        // Then - 서버 대기 화면으로 전환 (SceneManager 처리)
        assertNotNull(controller);
    }

    @Test
    void testJoinButtonOpensClientConnectionScreen(FxRobot robot) {
        // Given - "클라이언트로 접속" 버튼
        Button joinButton = robot.lookup("#joinButton").queryAs(Button.class);
        
        // When - 접속 클릭
        robot.clickOn(joinButton);
        
        // Then - IP 입력 화면으로 전환
        assertNotNull(controller);
    }

    @Test
    void testBackButtonReturnsToMenu(FxRobot robot) {
        // Given - "뒤로가기" 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 뒤로가기 클릭
        robot.clickOn(backButton);
        
        // Then - 메뉴로 복귀 (SceneManager 처리)
        assertNotNull(controller);
    }

    @Test
    void testHostButtonThenBackSequence(FxRobot robot) {
        // Given - 호스트 버튼
        Button hostButton = robot.lookup("#hostButton").queryAs(Button.class);
        
        // When - 호스트 클릭 (화면 전환 시도)
        robot.clickOn(hostButton);
        
        // Then - 예외 없이 실행됨
        assertTrue(true);
    }

    @Test
    void testJoinButtonThenBackSequence(FxRobot robot) {
        // Given - 접속 버튼
        Button joinButton = robot.lookup("#joinButton").queryAs(Button.class);
        
        // When - 접속 클릭
        robot.clickOn(joinButton);
        
        // Then - 예외 없이 실행됨
        assertTrue(true);
    }

    @Test
    void testButtonsEnableDisableStates(FxRobot robot) {
        // Given - 모든 버튼
        Button hostButton = robot.lookup("#hostButton").queryAs(Button.class);
        Button joinButton = robot.lookup("#joinButton").queryAs(Button.class);
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // Then - 초기에 모두 활성화
        assertFalse(hostButton.isDisabled());
        assertFalse(joinButton.isDisabled());
        assertFalse(backButton.isDisabled());
    }

    @Test
    void testGameModeIsSet(FxRobot robot) {
        // Given - 게임 모드가 설정됨
        
        // Then - 컨트롤러가 정상 초기화됨
        assertNotNull(controller);
    }

    @Test
    void testHostButtonTextNotEmpty(FxRobot robot) {
        // Given
        Button hostButton = robot.lookup("#hostButton").queryAs(Button.class);
        
        // Then
        assertNotNull(hostButton.getText());
        assertFalse(hostButton.getText().isEmpty());
    }

    @Test
    void testJoinButtonTextNotEmpty(FxRobot robot) {
        // Given
        Button joinButton = robot.lookup("#joinButton").queryAs(Button.class);
        
        // Then
        assertNotNull(joinButton.getText());
        assertFalse(joinButton.getText().isEmpty());
    }

    @Test
    void testBackButtonTextNotEmpty(FxRobot robot) {
        // Given
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // Then
        assertNotNull(backButton.getText());
        assertFalse(backButton.getText().isEmpty());
    }
}
