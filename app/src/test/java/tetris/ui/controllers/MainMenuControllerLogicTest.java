package tetris.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.ui.SceneManager;
import tetris.ui.SettingsManager;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MainMenuControllerLogicTest extends JavaFXTestBase {

    private MainMenuController controller;
    private SettingsManager settingsManager;
    private String originalGameMode;
    private String originalScreenSize;

    @BeforeEach
    void setUp() {
        settingsManager = SettingsManager.getInstance();
        originalGameMode = settingsManager.getGameMode();
        originalScreenSize = settingsManager.getScreenSize();

        runOnFxThreadAndWait(() -> {
            controller = new MainMenuController();
            injectButtonsAndBackground();
        });
    }

    @AfterEach
    void tearDown() {
        settingsManager.setGameMode(originalGameMode);
        settingsManager.setScreenSize(originalScreenSize);
    }

    private void injectButtonsAndBackground() {
        setField("normalModeButton", new Button());
        setField("itemModeButton", new Button());
        setField("battleModeButton", new Button());
        setField("pvpModeButton", new Button());
        setField("settingsButton", new Button());
        setField("scoreBoardButton", new Button());
        setField("exitButton", new Button());
        setField("backgroundImage", new ImageView());
    }

    private void setField(String fieldName, Object value) {
        try {
            Field field = MainMenuController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (ReflectiveOperationException e) {
            fail("Failed to inject field " + fieldName + ": " + e.getMessage());
        }
    }

    private void invokePrivate(String methodName, Class<?>... parameterTypes) {
        try {
            var method = MainMenuController.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(controller);
        } catch (ReflectiveOperationException e) {
            fail("Failed to invoke " + methodName + ": " + e.getMessage());
        }
    }

    @Test
    void startNormalModeSavesSettingsAndNavigates() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            controller.setSceneManager(sceneManager);
            settingsManager.setGameMode("ITEM");
            invokePrivate("onStartNormalMode");
        });

        verify(sceneManager).showGameScreen();
        assertEquals("NORMAL", settingsManager.getGameMode());
    }

    @Test
    void startItemModeSetsItemRulesAndNavigates() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            controller.setSceneManager(sceneManager);
            settingsManager.setGameMode("NORMAL");
            invokePrivate("onStartItemMode");
        });

        verify(sceneManager).showGameScreen();
        assertEquals("ITEM", settingsManager.getGameMode());
    }

    @Test
    void startBattleModeOpensBattleSelection() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            controller.setSceneManager(sceneManager);
            invokePrivate("onStartBattleMode");
        });

        verify(sceneManager).showBattleModeSelection();
    }

    @Test
    void startPvpModeOpensPvpSelection() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            controller.setSceneManager(sceneManager);
            invokePrivate("onStartPVPMode");
        });

        verify(sceneManager).showPVPModeSelection();
    }

    @Test
    void settingsButtonOpensSettingsScreen() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            controller.setSceneManager(sceneManager);
            invokePrivate("onSettings");
        });

        verify(sceneManager).showSettingsScreen();
    }

    @Test
    void scoreBoardButtonOpensScoreBoard() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.initialize(null, null);
            controller.setSceneManager(sceneManager);
            invokePrivate("onScoreBoard");
        });

        verify(sceneManager).showScoreBoard();
    }

    @Test
    void initializeAdjustsBackgroundSizeForSmallScreens() {
        runOnFxThreadAndWait(() -> {
            settingsManager.setScreenSize("작게");
            controller.initialize(null, null);
            ImageView background = (ImageView) getPrivateField("backgroundImage");
            assertEquals(480, background.getFitWidth());
            assertEquals(720, background.getFitHeight());
        });
    }

    private Object getPrivateField(String fieldName) {
        try {
            Field field = MainMenuController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }
}
