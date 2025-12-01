package tetris.ui.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.data.ScoreManager;
import tetris.ui.SceneManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreBoardControllerLogicTest extends JavaFXTestBase {

    private ScoreBoardController controller;
    private ScoreManager mockScoreManager;
    private ListView<String> scoreListView;
    private ToggleButton normalModeButton;
    private ToggleButton itemModeButton;
    private ToggleButton easyButton;
    private ToggleButton normalButton;
    private ToggleButton hardButton;
    private ImageView backgroundImageView;

    @BeforeEach
    void setUp() {
        mockScoreManager = mock(ScoreManager.class);
        runOnFxThreadAndWait(() -> {
            controller = new ScoreBoardController();
            controller.setScoreManager(mockScoreManager);
            injectUiSkeleton(controller);
        });
    }

    private void injectUiSkeleton(ScoreBoardController target) {
        ListView<String> localList = new ListView<>();
        ToggleButton localNormalMode = new ToggleButton();
        ToggleButton localItemMode = new ToggleButton();
        ToggleButton localEasy = new ToggleButton();
        ToggleButton localNormal = new ToggleButton();
        ToggleButton localHard = new ToggleButton();
        ImageView localBackground = new ImageView();

        setField(target, "scoreListView", localList);
        setField(target, "normalModeButton", localNormalMode);
        setField(target, "itemModeButton", localItemMode);
        setField(target, "easyButton", localEasy);
        setField(target, "normalButton", localNormal);
        setField(target, "hardButton", localHard);
        setField(target, "backgroundImageView", localBackground);

        if (target == controller) {
            scoreListView = localList;
            normalModeButton = localNormalMode;
            itemModeButton = localItemMode;
            easyButton = localEasy;
            normalButton = localNormal;
            hardButton = localHard;
            backgroundImageView = localBackground;
        }
    }

    private void setField(ScoreBoardController target, String name, Object value) {
        try {
            Field field = ScoreBoardController.class.getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            fail("Unable to set " + name + ": " + e.getMessage());
        }
    }

    @Test
    void initializeLoadsNormalScoresFromManager() {
        List<String> normalScores = List.of("1. AAA - 100");
        when(mockScoreManager.getFormattedScoresByDifficulty("NORMAL", "Normal")).thenReturn(normalScores);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            assertEquals(normalScores, scoreListView.getItems());
        });
        verify(mockScoreManager).getFormattedScoresByDifficulty("NORMAL", "Normal");
    }

    @Test
    void switchingToItemModeHidesDifficultyButtonsAndReloadsScores() {
        List<String> normalScores = List.of("1. AAA - 100");
        List<String> itemScores = List.of("1. ITEM - 200");
        when(mockScoreManager.getFormattedScoresByDifficulty("NORMAL", "Normal")).thenReturn(normalScores);
        when(mockScoreManager.getFormattedScores("ITEM")).thenReturn(itemScores);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            itemModeButton.fire();
            assertEquals(itemScores, scoreListView.getItems());
            assertFalse(easyButton.isManaged());
            assertFalse(normalButton.isManaged());
            assertFalse(hardButton.isManaged());
        });
        verify(mockScoreManager).getFormattedScores("ITEM");
    }

    @Test
    void changingDifficultyReloadsFilteredScores() {
        List<String> normalScores = List.of("1. AAA - 100");
        List<String> easyScores = List.of("1. BBB - 90");
        when(mockScoreManager.getFormattedScoresByDifficulty("NORMAL", "Normal")).thenReturn(normalScores);
        when(mockScoreManager.getFormattedScoresByDifficulty("NORMAL", "Easy")).thenReturn(easyScores);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            easyButton.fire();
            assertEquals(easyScores, scoreListView.getItems());
        });
        verify(mockScoreManager).getFormattedScoresByDifficulty("NORMAL", "Easy");
    }

    @Test
    void clearScoresInNormalModeDelegatesToScoreManager() {
        List<String> normalScores = List.of();
        when(mockScoreManager.getFormattedScoresByDifficulty("NORMAL", "Normal")).thenReturn(normalScores);

        runOnFxThreadAndWait(() -> {
            configureAlertFactory(Optional.of(ButtonType.OK));
            controller.initialize(null, null);
            invokePrivate("onClearScores", new Class<?>[]{});
        });

        verify(mockScoreManager).clearScoresByDifficulty("NORMAL", "Normal");
    }

    @Test
    void backButtonReturnsToMainMenu() {
        SceneManager sceneManager = mock(SceneManager.class);
        when(mockScoreManager.getFormattedScoresByDifficulty("NORMAL", "Normal")).thenReturn(List.of());

        runOnFxThreadAndWait(() -> {
            configureAlertFactory(Optional.empty());
            controller.initialize(null, null);
            controller.setSceneManager(sceneManager);
            invokePrivate("onBackToMenu", new Class<?>[]{});
        });

        verify(sceneManager).showMainMenu();
    }

    private void configureAlertFactory(Optional<ButtonType> confirmationResult) {
        Alert confirmationAlert = mock(Alert.class);
        when(confirmationAlert.showAndWait()).thenReturn(confirmationResult);
        Alert infoAlert = mock(Alert.class);
        when(infoAlert.showAndWait()).thenReturn(Optional.empty());

        controller.setAlertFactory(type ->
            type == Alert.AlertType.CONFIRMATION ? confirmationAlert : infoAlert
        );
    }

    private void invokePrivate(String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            var method = ScoreBoardController.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(controller, args);
        } catch (ReflectiveOperationException e) {
            fail("Failed to invoke " + methodName + ": " + e.getMessage());
        }
    }
}
