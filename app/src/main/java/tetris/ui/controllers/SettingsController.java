package tetris.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import tetris.data.ScoreManager;
import tetris.ui.SceneManager;
import tetris.ui.SettingsManager;
import tetris.ui.services.SettingsValidationService;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;

public class SettingsController implements Initializable {

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private CheckBox colorBlindModeCheckBox;

    @FXML
    private ComboBox<String> screenSizeComboBox;

    @FXML
    private TextField keyLeftField;

    @FXML
    private TextField keyRightField;

    @FXML
    private TextField keyDownField;

    @FXML
    private TextField keyRotateField;

    @FXML
    private TextField keyHardDropField;

    @FXML
    private TextField keyLeftFieldP2;

    @FXML
    private TextField keyRightFieldP2;

    @FXML
    private TextField keyDownFieldP2;

    @FXML
    private TextField keyRotateFieldP2;

    @FXML
    private TextField keyHardDropFieldP2;

    private SceneManager sceneManager;
    private SettingsManager settingsManager;
    private SettingsValidationService validationService;

    // 생성자 주입
    public SettingsController() {
        this(new SettingsValidationService());
    }
    
    // 테스트용 생성자
    public SettingsController(SettingsValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingsManager = SettingsManager.getInstance();
        setupDifficultyComboBox();
        setupKeyFields();
        loadSettings();
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private void setupDifficultyComboBox() {
        if (difficultyComboBox != null) {
            difficultyComboBox.getItems().addAll("Easy", "Normal", "Hard");
            difficultyComboBox.setValue("Normal");
        }
        if (screenSizeComboBox != null) {
            screenSizeComboBox.getItems().addAll("작게", "중간", "크게");
            screenSizeComboBox.setValue("중간");
        }
    }

    private void setupKeyFields() {
        // TextField에 한 글자만 입력되도록 제한
        setupSingleKeyField(keyLeftField);
        setupSingleKeyField(keyRightField);
        setupSingleKeyField(keyDownField);
        setupSingleKeyField(keyRotateField);
        setupSingleKeyField(keyHardDropField);
        setupSingleKeyField(keyLeftFieldP2);
        setupSingleKeyField(keyRightFieldP2);
        setupSingleKeyField(keyDownFieldP2);
        setupSingleKeyField(keyRotateFieldP2);
        setupSingleKeyField(keyHardDropFieldP2);
    }

    private void setupSingleKeyField(TextField field) {
        if (field == null) return;
        
        // 입력시 대문자로 변환하고 한 글자만 허용
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > 0) {
                String upper = newValue.toUpperCase();
                // SPACE, ENTER, UP, DOWN, LEFT, RIGHT는 예외로 허용
                if (upper.equals("SPACE") || upper.equals("ENTER") || 
                    upper.equals("UP") || upper.equals("DOWN") || 
                    upper.equals("LEFT") || upper.equals("RIGHT")) {
                    return;
                }
                if (upper.length() > 1) {
                    field.setText(upper.substring(0, 1));
                } else {
                    field.setText(upper);
                }
            }
        });
    }

    private void loadSettings() {
        // SettingsManager에서 설정 로드
        if (difficultyComboBox != null) {
            difficultyComboBox.setValue(settingsManager.getDifficulty());
        }
        if (colorBlindModeCheckBox != null) {
            colorBlindModeCheckBox.setSelected(settingsManager.isColorBlindModeEnabled());
        }
        if (screenSizeComboBox != null) {
            screenSizeComboBox.setValue(settingsManager.getScreenSize());
        }
        // 키 설정 불러오기
        if (keyLeftField != null) {
            keyLeftField.setText(settingsManager.getKeyLeft());
        }
        if (keyRightField != null) {
            keyRightField.setText(settingsManager.getKeyRight());
        }
        if (keyDownField != null) {
            keyDownField.setText(settingsManager.getKeyDown());
        }
        if (keyRotateField != null) {
            keyRotateField.setText(settingsManager.getKeyRotate());
        }
        if (keyHardDropField != null) {
            keyHardDropField.setText(settingsManager.getKeyHardDrop());
        }
        // Player2 키 설정 불러오기
        if (keyLeftFieldP2 != null) {
            keyLeftFieldP2.setText(settingsManager.getKeyLeftP2());
        }
        if (keyRightFieldP2 != null) {
            keyRightFieldP2.setText(settingsManager.getKeyRightP2());
        }
        if (keyDownFieldP2 != null) {
            keyDownFieldP2.setText(settingsManager.getKeyDownP2());
        }
        if (keyRotateFieldP2 != null) {
            keyRotateFieldP2.setText(settingsManager.getKeyRotateP2());
        }
        if (keyHardDropFieldP2 != null) {
            keyHardDropFieldP2.setText(settingsManager.getKeyHardDropP2());
        }
    }

    @FXML
    private void onSaveSettings() {
        // Player1과 Player2 키 중복 검사
        if (!validateKeySettings()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("키 설정 오류");
            alert.setHeaderText("키 중복 발견");
            alert.setContentText("Player1과 Player2가 같은 키를 사용할 수 없습니다. \n다른 키를 설정해주세요.");
            alert.showAndWait();
            return;
        }
        
        // 설정 저장 로직
        saveSettings();
        onBackToMenu();
    }

    @FXML
    private void onResetSettings() {
        // 설정 초기화
        settingsManager.resetToDefaults();
        loadSettings();
    }

    @FXML
    private void onBackToMenu() {
        if (sceneManager != null) {
            sceneManager.showMainMenu();
        }
    }

    private void saveSettings() {
        // UI에서 설정 값들을 SettingsManager에 저장
        if (difficultyComboBox != null) {
            settingsManager.setDifficulty(difficultyComboBox.getValue());
        }
        if (colorBlindModeCheckBox != null) {
            settingsManager.setColorBlindModeEnabled(colorBlindModeCheckBox.isSelected());
        }
        if (screenSizeComboBox != null) {
            String selectedSize = screenSizeComboBox.getValue();
            settingsManager.setScreenSize(selectedSize);
        }
        // 키 설정 저장
        if (keyLeftField != null && !keyLeftField.getText().isEmpty()) {
            settingsManager.setKeyLeft(keyLeftField.getText());
        }
        if (keyRightField != null && !keyRightField.getText().isEmpty()) {
            settingsManager.setKeyRight(keyRightField.getText());
        }
        if (keyDownField != null && !keyDownField.getText().isEmpty()) {
            settingsManager.setKeyDown(keyDownField.getText());
        }
        if (keyRotateField != null && !keyRotateField.getText().isEmpty()) {
            settingsManager.setKeyRotate(keyRotateField.getText());
        }
        if (keyHardDropField != null && !keyHardDropField.getText().isEmpty()) {
            settingsManager.setKeyHardDrop(keyHardDropField.getText());
        }
        // Player2 키 설정 저장
        if (keyLeftFieldP2 != null && !keyLeftFieldP2.getText().isEmpty()) {
            settingsManager.setKeyLeftP2(keyLeftFieldP2.getText());
        }
        if (keyRightFieldP2 != null && !keyRightFieldP2.getText().isEmpty()) {
            settingsManager.setKeyRightP2(keyRightFieldP2.getText());
        }
        if (keyDownFieldP2 != null && !keyDownFieldP2.getText().isEmpty()) {
            settingsManager.setKeyDownP2(keyDownFieldP2.getText());
        }
        if (keyRotateFieldP2 != null && !keyRotateFieldP2.getText().isEmpty()) {
            settingsManager.setKeyRotateP2(keyRotateFieldP2.getText());
        }
        if (keyHardDropFieldP2 != null && !keyHardDropFieldP2.getText().isEmpty()) {
            settingsManager.setKeyHardDropP2(keyHardDropFieldP2.getText());
        }
        // 파일에 저장
        settingsManager.saveToFile();
        System.out.println("Settings saved");
    }

    /**
     * Player1과 Player2의 키 설정이 중복되지 않는지 검사
     */
    private boolean validateKeySettings() {
        Map<String, String> p1Keys = new HashMap<>();
        if (keyLeftField != null && !keyLeftField.getText().isEmpty()) {
            p1Keys.put("left", keyLeftField.getText());
        }
        if (keyRightField != null && !keyRightField.getText().isEmpty()) {
            p1Keys.put("right", keyRightField.getText());
        }
        if (keyDownField != null && !keyDownField.getText().isEmpty()) {
            p1Keys.put("down", keyDownField.getText());
        }
        if (keyRotateField != null && !keyRotateField.getText().isEmpty()) {
            p1Keys.put("rotate", keyRotateField.getText());
        }
        if (keyHardDropField != null && !keyHardDropField.getText().isEmpty()) {
            p1Keys.put("hardDrop", keyHardDropField.getText());
        }

        Map<String, String> p2Keys = new HashMap<>();
        if (keyLeftFieldP2 != null && !keyLeftFieldP2.getText().isEmpty()) {
            p2Keys.put("left", keyLeftFieldP2.getText());
        }
        if (keyRightFieldP2 != null && !keyRightFieldP2.getText().isEmpty()) {
            p2Keys.put("right", keyRightFieldP2.getText());
        }
        if (keyDownFieldP2 != null && !keyDownFieldP2.getText().isEmpty()) {
            p2Keys.put("down", keyDownFieldP2.getText());
        }
        if (keyRotateFieldP2 != null && !keyRotateFieldP2.getText().isEmpty()) {
            p2Keys.put("rotate", keyRotateFieldP2.getText());
        }
        if (keyHardDropFieldP2 != null && !keyHardDropFieldP2.getText().isEmpty()) {
            p2Keys.put("hardDrop", keyHardDropFieldP2.getText());
        }
        
        // 서비스를 사용하여 중복 검사
        return validationService.findDuplicateKeys(p1Keys, p2Keys).isEmpty();
    }


    @FXML
    private void onClearScoreboard() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("스코어보드 초기화");
        alert.setHeaderText("모든 게임 모드의 점수 기록을 삭제하시겠습니까?");
        alert.setContentText("일반 모드와 아이템 모드의 모든 기록이 삭제됩니다. 이 작업은 되돌릴 수 없습니다.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ScoreManager.getInstance().clearScores("NORMAL");
            ScoreManager.getInstance().clearScores("ITEM");
            
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("초기화 완료");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("모든 스코어보드가 초기화되었습니다.");
            infoAlert.showAndWait();
        }
    }
}