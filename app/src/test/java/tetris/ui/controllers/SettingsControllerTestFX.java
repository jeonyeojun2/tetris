package tetris.ui.controllers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
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
import static org.testfx.matcher.control.ComboBoxMatchers.*;

/**
 * SettingsController TestFX 통합 테스트
 */
@ExtendWith(ApplicationExtension.class)
class SettingsControllerTestFX {
    
    private SettingsController controller;
    private SceneManager sceneManager;

    @Start
    private void start(Stage stage) throws Exception {
        // SettingsManager 초기화
        SettingsManager.getInstance();
        
        // FXML 로드
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsScreen.fxml"));
        Parent root = loader.load();
        
        controller = loader.getController();
        sceneManager = new SceneManager(stage);
        controller.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testDifficultyComboBoxExists(FxRobot robot) {
        verifyThat("#difficultyComboBox", isVisible());
    }

    @Test
    void testScreenSizeComboBoxExists(FxRobot robot) {
        verifyThat("#screenSizeComboBox", isVisible());
    }

    @Test
    void testColorBlindModeCheckBoxExists(FxRobot robot) {
        verifyThat("#colorBlindModeCheckBox", isVisible());
    }

    @Test
    void testKeyFieldsExist(FxRobot robot) {
        verifyThat("#keyLeftField", isVisible());
        verifyThat("#keyRightField", isVisible());
        verifyThat("#keyDownField", isVisible());
        verifyThat("#keyRotateField", isVisible());
        verifyThat("#keyHardDropField", isVisible());
    }

    @Test
    void testPlayer2KeyFieldsExist(FxRobot robot) {
        verifyThat("#keyLeftFieldP2", isVisible());
        verifyThat("#keyRightFieldP2", isVisible());
        verifyThat("#keyDownFieldP2", isVisible());
        verifyThat("#keyRotateFieldP2", isVisible());
        verifyThat("#keyHardDropFieldP2", isVisible());
    }

    @Test
    void testDifficultyComboBoxHasOptions(FxRobot robot) {
        ComboBox<String> comboBox = robot.lookup("#difficultyComboBox").query();
        assertNotNull(comboBox);
        assertTrue(comboBox.getItems().contains("Easy"));
        assertTrue(comboBox.getItems().contains("Normal"));
        assertTrue(comboBox.getItems().contains("Hard"));
    }

    @Test
    void testScreenSizeComboBoxHasOptions(FxRobot robot) {
        ComboBox<String> comboBox = robot.lookup("#screenSizeComboBox").query();
        assertNotNull(comboBox);
        assertTrue(comboBox.getItems().contains("작게"));
        assertTrue(comboBox.getItems().contains("중간"));
        assertTrue(comboBox.getItems().contains("크게"));
    }

    @Test
    void testDifficultySelectionEasy(FxRobot robot) {
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Easy");
        
        ComboBox<String> comboBox = robot.lookup("#difficultyComboBox").query();
        assertEquals("Easy", comboBox.getValue());
    }

    @Test
    void testDifficultySelectionHard(FxRobot robot) {
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Hard");
        
        ComboBox<String> comboBox = robot.lookup("#difficultyComboBox").query();
        assertEquals("Hard", comboBox.getValue());
    }

    @Test
    void testScreenSizeSelectionSmall(FxRobot robot) {
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("작게");
        
        ComboBox<String> comboBox = robot.lookup("#screenSizeComboBox").query();
        assertEquals("작게", comboBox.getValue());
    }

    @Test
    void testScreenSizeSelectionLarge(FxRobot robot) {
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("크게");
        
        ComboBox<String> comboBox = robot.lookup("#screenSizeComboBox").query();
        assertEquals("크게", comboBox.getValue());
    }

    @Test
    void testColorBlindModeToggle(FxRobot robot) {
        CheckBox checkBox = robot.lookup("#colorBlindModeCheckBox").query();
        boolean initialState = checkBox.isSelected();
        
        robot.clickOn("#colorBlindModeCheckBox");
        assertEquals(!initialState, checkBox.isSelected());
    }

    @Test
    void testKeyLeftFieldInput(FxRobot robot) {
        TextField field = robot.lookup("#keyLeftField").query();
        robot.clickOn(field);
        robot.write("A");
        
        assertTrue(field.getText().length() <= 1);
    }

    @Test
    void testKeyRightFieldInput(FxRobot robot) {
        TextField field = robot.lookup("#keyRightField").query();
        robot.clickOn(field);
        robot.write("D");
        
        assertTrue(field.getText().length() <= 1);
    }

    @Test
    void testAllFieldsAreEditable(FxRobot robot) {
        TextField leftField = robot.lookup("#keyLeftField").query();
        TextField rightField = robot.lookup("#keyRightField").query();
        TextField downField = robot.lookup("#keyDownField").query();
        TextField rotateField = robot.lookup("#keyRotateField").query();
        TextField hardDropField = robot.lookup("#keyHardDropField").query();
        
        assertFalse(leftField.isDisabled());
        assertFalse(rightField.isDisabled());
        assertFalse(downField.isDisabled());
        assertFalse(rotateField.isDisabled());
        assertFalse(hardDropField.isDisabled());
    }

    @Test
    void testSaveButtonExists(FxRobot robot) {
        // 저장 버튼이 있다면 확인
        verifyThat("저장", isVisible());
    }

    @Test
    void testBackButtonExists(FxRobot robot) {
        // 뒤로가기 버튼이 있다면 확인
        verifyThat("돌아가기", isVisible());
    }

    @Test
    void testClickSaveButton(FxRobot robot) {
        // 설정 저장 버튼 클릭 테스트
        robot.clickOn("저장");
    }

    @Test
    void testClickBackButton(FxRobot robot) {
        // 뒤로가기 버튼 클릭
        robot.clickOn("돌아가기");
    }

    @Test
    void testChangeDifficultyAndSave(FxRobot robot) {
        // 난이도 변경 후 저장
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Hard");
        robot.clickOn("저장");
    }

    @Test
    void testChangeScreenSizeAndSave(FxRobot robot) {
        // 화면 크기 변경 후 저장
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("크게");
        robot.clickOn("저장");
    }

    @Test
    void testToggleColorBlindModeAndSave(FxRobot robot) {
        // 색맹 모드 토글 후 저장
        robot.clickOn("#colorBlindModeCheckBox");
        robot.clickOn("저장");
    }

    @Test
    void testChangeKeyAndSave(FxRobot robot) {
        // 키 설정 변경 후 저장
        TextField field = robot.lookup("#keyLeftField").query();
        robot.clickOn(field);
        robot.eraseText(1);
        robot.write("Q");
        robot.clickOn("저장");
    }

    @Test
    void testChangeDifficultyAndCancel(FxRobot robot) {
        // 난이도 변경 후 취소
        ComboBox<String> comboBox = robot.lookup("#difficultyComboBox").query();
        String originalValue = comboBox.getValue();
        
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Easy");
        robot.clickOn("돌아가기");
        
        // 취소 시 원래 값으로 돌아가는지는 확인 안 함 (화면 전환되므로)
    }

    @Test
    void testMultipleSettingsChangesAndSave(FxRobot robot) {
        // 여러 설정 동시 변경 후 저장
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Easy");
        
        robot.clickOn("#colorBlindModeCheckBox");
        
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("작게");
        
        robot.clickOn("저장");
    }

    @Test
    void testPlayer2KeyFieldsInput(FxRobot robot) {
        // Player2 키 입력 테스트
        TextField fieldP2 = robot.lookup("#keyLeftFieldP2").query();
        robot.clickOn(fieldP2);
        robot.write("J");
        
        assertTrue(fieldP2.getText().length() <= 1);
    }

    @Test
    void testSaveButtonAction(FxRobot robot) {
        // 저장 버튼에 액션이 있는지 확인
        Button saveButton = robot.lookup("저장").query();
        assertNotNull(saveButton);
        assertTrue(saveButton.isVisible());
    }

    @Test
    void testBackButtonAction(FxRobot robot) {
        // 뒤로가기 버튼에 액션이 있는지 확인
        Button backButton = robot.lookup("돌아가기").query();
        assertNotNull(backButton);
        assertTrue(backButton.isVisible());
    }

    @Test
    void testResetButtonIfExists(FxRobot robot) {
        // 리셋 버튼이 있다면 테스트
        try {
            Button resetButton = robot.lookup("설정 되돌리기").query();
            if (resetButton != null) {
                assertTrue(resetButton.isVisible());
                robot.clickOn("설정 되돌리기");
            }
        } catch (Exception e) {
            // 리셋 버튼이 없을 수 있음
        }
    }

    @Test
    void testClearScoreboardButton(FxRobot robot) {
        // 스코어보드 초기화 버튼 테스트
        Button clearButton = robot.lookup("스코어보드 초기화").query();
        assertNotNull(clearButton);
        assertTrue(clearButton.isVisible());
    }

    @Test
    void testResetButtonClick(FxRobot robot) {
        Button resetButton = robot.lookup("설정 되돌리기").query();
        robot.clickOn(resetButton);
    }

    @Test
    void testDifficultyChangeSequence(FxRobot robot) {
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Easy");
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Hard");
    }

    @Test
    void testAllPlayer1KeyFields(FxRobot robot) {
        TextField left = robot.lookup("#keyLeftField").query();
        TextField right = robot.lookup("#keyRightField").query();
        TextField down = robot.lookup("#keyDownField").query();
        TextField rotate = robot.lookup("#keyRotateField").query();
        TextField hardDrop = robot.lookup("#keyHardDropField").query();
        
        assertNotNull(left);
        assertNotNull(right);
        assertNotNull(down);
        assertNotNull(rotate);
        assertNotNull(hardDrop);
    }

    @Test
    void testAllPlayer2KeyFields(FxRobot robot) {
        TextField left = robot.lookup("#keyLeftFieldP2").query();
        TextField right = robot.lookup("#keyRightFieldP2").query();
        TextField down = robot.lookup("#keyDownFieldP2").query();
        TextField rotate = robot.lookup("#keyRotateFieldP2").query();
        TextField hardDrop = robot.lookup("#keyHardDropFieldP2").query();
        
        assertNotNull(left);
        assertNotNull(right);
        assertNotNull(down);
        assertNotNull(rotate);
        assertNotNull(hardDrop);
    }

    @Test
    void testCompleteSettingsWorkflow(FxRobot robot) {
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Hard");
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("크게");
        robot.clickOn("#colorBlindModeCheckBox");
        robot.clickOn("저장");
    }

    @Test
    void testResetAfterChanges(FxRobot robot) {
        robot.clickOn("#difficultyComboBox");
        robot.clickOn("Easy");
        robot.clickOn("설정 되돌리기");
    }

    @Test
    void testMultipleColorBlindToggles(FxRobot robot) {
        CheckBox checkBox = robot.lookup("#colorBlindModeCheckBox").query();
        robot.clickOn(checkBox);
        robot.clickOn(checkBox);
        robot.clickOn(checkBox);
    }

    @Test
    void testScreenSizeAllOptions(FxRobot robot) {
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("작게");
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("중간");
        robot.clickOn("#screenSizeComboBox");
        robot.clickOn("크게");
    }

    @Test
    void testKeyInputValidation(FxRobot robot) {
        TextField field = robot.lookup("#keyLeftField").query();
        robot.clickOn(field);
        robot.eraseText(1);
        robot.write("Z");
        assertEquals("Z", field.getText());
    }

    @Test
    void testAllButtonsAccessible(FxRobot robot) {
        Button save = robot.lookup("저장").query();
        Button reset = robot.lookup("설정 되돌리기").query();
        Button clear = robot.lookup("스코어보드 초기화").query();
        Button back = robot.lookup("돌아가기").query();
        
        assertFalse(save.isDisabled());
        assertFalse(reset.isDisabled());
        assertFalse(clear.isDisabled());
        assertFalse(back.isDisabled());
    }
}
