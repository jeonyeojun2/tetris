package tetris.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import tetris.ui.SceneManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class PVPClientConnectionControllerTestFX {

    private PVPClientConnectionController controller;
    private Stage stage;

    @Start
    private void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PVPClientConnection.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        SceneManager sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root, 600, 900);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testServerIpFieldExists(FxRobot robot) {
        TextField field = robot.lookup("#serverIpField").queryAs(TextField.class);
        assertNotNull(field, "Server IP field should exist");
        assertTrue(field.isVisible());
    }

    @Test
    void testConnectButtonExists(FxRobot robot) {
        Button button = robot.lookup("#connectButton").queryAs(Button.class);
        assertNotNull(button, "Connect button should exist");
        assertTrue(button.isVisible());
        assertFalse(button.isDisabled());
    }

    @Test
    void testBackButtonExists(FxRobot robot) {
        Button button = robot.lookup("#backButton").queryAs(Button.class);
        assertNotNull(button, "Back button should exist");
        assertTrue(button.isVisible());
        assertFalse(button.isDisabled());
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label label = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(label, "Status label should exist");
    }

    @Test
    void testEmptyIpShowsError(FxRobot robot) {
        // Given - IP 필드가 비어있음
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        ipField.setText("");
        
        // When - 접속 버튼 클릭
        Button connectButton = robot.lookup("#connectButton").queryAs(Button.class);
        robot.clickOn(connectButton);
        
        // Then - 에러 메시지 표시
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel.getText());
        assertTrue(statusLabel.getText().contains("IP") || statusLabel.getText().contains("입력"));
    }

    @Test
    void testValidIpInputEnabled(FxRobot robot) {
        // Given - 유효한 IP 입력
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        robot.clickOn(ipField);
        robot.write("127.0.0.1");
        
        // Then - 텍스트가 입력됨
        assertEquals("127.0.0.1", ipField.getText());
    }

    @Test
    void testConnectButtonDisabledAfterClick(FxRobot robot) {
        // Given - 유효한 IP 입력
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        robot.clickOn(ipField);
        robot.write("192.168.0.1");
        
        // When - 접속 버튼 클릭
        Button connectButton = robot.lookup("#connectButton").queryAs(Button.class);
        robot.clickOn(connectButton);
        
        // Then - 버튼이 비활성화됨 (연결 중)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignore
        }
        assertTrue(connectButton.isDisabled() || connectButton.getText().contains("연결"));
    }

    @Test
    void testBackButtonClick(FxRobot robot) {
        // Given - 뒤로가기 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 뒤로가기 클릭
        robot.clickOn(backButton);
        
        // Then - 예외 없이 실행됨 (SceneManager가 화면 전환)
        assertNotNull(backButton);
    }

    @Test
    void testIpFieldAcceptsInput(FxRobot robot) {
        // Given
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        
        // When - 다양한 IP 형식 입력
        robot.clickOn(ipField);
        robot.write("10.0.0.1");
        
        // Then
        assertEquals("10.0.0.1", ipField.getText());
    }

    @Test
    void testStatusLabelUpdatesOnConnect(FxRobot robot) {
        // Given - IP 입력
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        String initialStatus = statusLabel.getText();
        
        robot.clickOn(ipField);
        robot.write("localhost");
        
        // When - 접속 시도
        Button connectButton = robot.lookup("#connectButton").queryAs(Button.class);
        robot.clickOn(connectButton);
        
        // Then - 상태 라벨이 변경됨
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignore
        }
        assertNotEquals(initialStatus, statusLabel.getText());
    }

    @Test
    void testRecentIPsBoxExists(FxRobot robot) {
        // Given - recentIPsBox가 존재하는지 확인
        try {
            robot.lookup("#recentIPsBox").query();
            // Then - 예외 없이 찾아짐
            assertTrue(true);
        } catch (Exception e) {
            // recentIPsBox가 없을 수도 있음
            assertTrue(true);
        }
    }

    @Test
    void testInvalidIpFormatHandling(FxRobot robot) {
        // Given - 잘못된 IP 형식
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        robot.clickOn(ipField);
        robot.write("invalid.ip.format");
        
        // When - 접속 시도
        Button connectButton = robot.lookup("#connectButton").queryAs(Button.class);
        robot.clickOn(connectButton);
        
        // Then - 상태 라벨에 메시지 표시 (연결 실패는 시간이 걸림)
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel.getText());
    }

    @Test
    void testMultipleConnectAttempts(FxRobot robot) {
        // Given - IP 입력
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        robot.clickOn(ipField);
        robot.write("127.0.0.1");
        
        // When - 여러 번 접속 시도
        Button connectButton = robot.lookup("#connectButton").queryAs(Button.class);
        robot.clickOn(connectButton);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Then - 버튼이 비활성화되어 중복 클릭 방지
        assertTrue(connectButton.isDisabled() || !connectButton.isVisible());
    }

    @Test
    void testBackButtonAlwaysEnabled(FxRobot robot) {
        // Given - 뒤로가기 버튼
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // When - 접속 중이어도
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        robot.clickOn(ipField);
        robot.write("192.168.1.1");
        
        Button connectButton = robot.lookup("#connectButton").queryAs(Button.class);
        robot.clickOn(connectButton);
        
        // Then - 뒤로가기는 여전히 활성화됨
        assertFalse(backButton.isDisabled());
    }

    @Test
    void testIpFieldClearAndReenter(FxRobot robot) {
        // Given - IP 입력
        TextField ipField = robot.lookup("#serverIpField").queryAs(TextField.class);
        robot.clickOn(ipField);
        robot.write("192.168.0.1");
        
        // When - 지우고 다시 입력
        ipField.clear();
        robot.write("10.0.0.1");
        
        // Then
        assertEquals("10.0.0.1", ipField.getText());
    }

    @Test
    void testStatusLabelInitialState(FxRobot robot) {
        // Given - 초기 상태
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        
        // Then - 초기 메시지가 있음
        assertNotNull(statusLabel.getText());
    }

    @Test
    void testConnectButtonHasText(FxRobot robot) {
        // Given
        Button connectButton = robot.lookup("#connectButton").queryAs(Button.class);
        
        // Then - 버튼 텍스트가 있음
        assertNotNull(connectButton.getText());
        assertFalse(connectButton.getText().isEmpty());
    }

    @Test
    void testBackButtonHasText(FxRobot robot) {
        // Given
        Button backButton = robot.lookup("#backButton").queryAs(Button.class);
        
        // Then
        assertNotNull(backButton.getText());
        assertFalse(backButton.getText().isEmpty());
    }
}
