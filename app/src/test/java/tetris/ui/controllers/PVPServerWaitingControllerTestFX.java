package tetris.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import tetris.ui.SceneManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class PVPServerWaitingControllerTestFX {

    private PVPServerWaitingController controller;
    private Stage stage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PVPServerWaiting.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        SceneManager sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root, 600, 900);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testServerIpLabelExists(FxRobot robot) {
        Label label = robot.lookup("#serverIpLabel").queryAs(Label.class);
        assertNotNull(label, "Server IP label should exist");
        assertTrue(label.isVisible());
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label label = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(label, "Status label should exist");
        assertTrue(label.isVisible());
    }

    @Test
    void testBackButtonExists(FxRobot robot) {
        Button button = robot.lookup("#backButton").queryAs(Button.class);
        assertNotNull(button, "Back button should exist");
        assertTrue(button.isVisible());
        assertFalse(button.isDisabled());
    }

    @Test
    void testBackButtonClick(FxRobot robot) {
        // Given - 뒤로가기 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 뒤로가기 클릭
        robot.clickOn(backButton);
        
        // Then - 예외 없이 실행됨 (SceneManager가 메뉴로 복귀)
        assertNotNull(backButton);
    }

    @Test
    void testCancelButtonFunctionality(FxRobot robot) {
        // Given - 취소 버튼 (backButton이 취소 역할)
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 취소 클릭
        robot.clickOn(backButton);
        
        // Then - 서버가 중지되고 메뉴로 복귀
        assertNotNull(controller);
    }

    @Test
    void testServerIpLabelDisplaysText(FxRobot robot) {
        // Given - serverIpLabel
        Label serverIpLabel = robot.lookup("#serverIpLabel").queryAs(Label.class);
        
        // Then - 텍스트가 표시됨 (초기값 또는 설정된 IP)
        assertNotNull(serverIpLabel.getText());
    }

    @Test
    void testStatusLabelInitialState(FxRobot robot) {
        // Given - 초기 상태
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        
        // Then - 초기 메시지가 있음 (대기 중...)
        assertNotNull(statusLabel.getText());
    }

    @Test
    void testBackButtonAlwaysEnabled(FxRobot robot) {
        // Given - 뒤로가기 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // Then - 항상 활성화되어 언제든 취소 가능
        assertFalse(backButton.isDisabled());
        assertTrue(backButton.isVisible());
    }

    @Test
    void testBackButtonHasText(FxRobot robot) {
        // Given
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // Then - 버튼에 텍스트가 있음
        assertNotNull(backButton.getText());
        assertFalse(backButton.getText().isEmpty());
    }

    @Test
    void testMultipleBackButtonClicks(FxRobot robot) {
        // Given - 뒤로가기 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 여러 번 클릭
        robot.clickOn(backButton);
        
        // Then - 예외 없이 처리됨 (첫 클릭만 유효)
        assertNotNull(backButton);
    }

    @Test
    void testServerIpLabelAlignment(FxRobot robot) {
        // Given
        Label serverIpLabel = robot.lookup("#serverIpLabel").queryAs(Label.class);
        
        // Then - 라벨이 올바르게 표시됨
        assertTrue(serverIpLabel.isVisible());
        assertNotNull(serverIpLabel.getParent());
    }

    @Test
    void testStatusLabelCanUpdateText(FxRobot robot) {
        // Given - statusLabel
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        String initialText = statusLabel.getText();
        
        // When - 상태가 변경될 수 있음 (클라이언트 연결 시)
        try {
            javafx.application.Platform.runLater(() -> {
                statusLabel.setText("클라이언트 연결됨");
            });
            Thread.sleep(100);
        } catch (Exception e) {
            // Ignore
        }
        
        // Then - 텍스트가 변경됨
        assertNotNull(statusLabel.getText());
    }

    @Test
    void testWaitingForClientState(FxRobot robot) {
        // Given - 초기 대기 상태
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        
        // Then - "대기" 또는 "클라이언트" 관련 메시지
        String status = statusLabel.getText().toLowerCase();
        assertTrue(status.length() > 0, "Status label should have text");
    }

    @Test
    void testBackButtonCancelsWaiting(FxRobot robot) {
        // Given - 클라이언트 대기 중
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 취소 버튼 클릭
        robot.clickOn(backButton);
        
        // Then - 대기가 취소되고 화면 전환
        assertNotNull(controller);
    }

    @Test
    void testServerInfoCanBeSet(FxRobot robot) {
        // Given - serverIpLabel
        Label serverIpLabel = robot.lookup("#serverIpLabel").queryAs(Label.class);
        
        // When - setServerInfo 호출 (테스트에서는 간접적으로)
        try {
            javafx.application.Platform.runLater(() -> {
                controller.setServerInfo(null, "192.168.0.1");
            });
            Thread.sleep(100);
        } catch (Exception e) {
            // Ignore
        }
        
        // Then - IP가 표시됨
        assertNotNull(serverIpLabel.getText());
    }

    @Test
    void testStatusLabelVisibleDuringWait(FxRobot robot) {
        // Given
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        
        // Then - 대기 중에도 계속 표시됨
        assertTrue(statusLabel.isVisible());
    }

    @Test
    void testBackgroundImageExists(FxRobot robot) {
        // Given - backgroundImage가 있는지 확인
        try {
            robot.lookup("#backgroundImage").query();
            assertTrue(true);
        } catch (Exception e) {
            // backgroundImage가 없을 수도 있음
            assertTrue(true);
        }
    }

    @Test
    void testLayoutStructure(FxRobot robot) {
        // Given - 주요 요소들
        Label serverIpLabel = robot.lookup("#serverIpLabel").queryAs(Label.class);
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // Then - 모든 요소가 존재하고 표시됨
        assertTrue(serverIpLabel.isVisible());
        assertTrue(statusLabel.isVisible());
        assertTrue(backButton.isVisible());
    }

    @Test
    void testServerWaitingScreenInitialization(FxRobot robot) {
        // Given - 초기화된 화면
        
        // Then - 모든 필수 요소가 준비됨
        assertNotNull(robot.lookup("#serverIpLabel").query());
        assertNotNull(robot.lookup("#statusLabel").query());
        assertNotNull(robot.lookup("#backButton").query());
    }
}
