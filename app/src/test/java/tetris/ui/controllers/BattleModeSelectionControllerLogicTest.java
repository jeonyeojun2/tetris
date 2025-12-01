package tetris.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.ui.SceneManager;
import tetris.ui.SettingsManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BattleModeSelectionControllerLogicTest extends JavaFXTestBase {

    private BattleModeSelectionController controller;
    private Button normalButton;
    private Button itemButton;
    private Button timeLimitButton;
    private Button backButton;
    private ImageView backgroundImage;
    private SettingsManager settingsManager;
    private String originalScreenSize;

    @BeforeEach
    void setUp() {
        settingsManager = SettingsManager.getInstance();
        originalScreenSize = settingsManager.getScreenSize();

        runOnFxThreadAndWait(() -> {
            controller = new BattleModeSelectionController();
            injectUiSkeleton(controller);
            controller.initialize(null, null);
        });
    }

    @AfterEach
    void tearDown() {
        settingsManager.setScreenSize(originalScreenSize);
    }

    private void injectUiSkeleton(BattleModeSelectionController target) {
        Button localNormal = new Button();
        Button localItem = new Button();
        Button localTime = new Button();
        Button localBack = new Button();
        ImageView localBackground = new ImageView();

        setField(target, "normalBattleButton", localNormal);
        setField(target, "itemBattleButton", localItem);
        setField(target, "timeLimitBattleButton", localTime);
        setField(target, "backButton", localBack);
        setField(target, "backgroundImage", localBackground);

        if (target == controller) {
            normalButton = localNormal;
            itemButton = localItem;
            timeLimitButton = localTime;
            backButton = localBack;
            backgroundImage = localBackground;
        }
    }

    private void setField(BattleModeSelectionController target, String fieldName, Object value) {
        try {
            Field field = BattleModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            fail("Failed to inject field " + fieldName + ": " + e.getMessage());
        }
    }

    private Object getField(String fieldName) {
        try {
            Field field = BattleModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }

    private int getIntField(String fieldName) {
        try {
            Field field = BattleModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(controller);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }

    private void setIntField(String fieldName, int value) {
        try {
            Field field = BattleModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setInt(controller, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set field " + fieldName, e);
        }
    }

    private void invokePrivate(String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Method method = BattleModeSelectionController.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(controller, args);
        } catch (ReflectiveOperationException e) {
            fail("Failed to invoke " + methodName + ": " + e.getMessage());
        }
    }

    @Test
    void normalBattleModeNavigatesThroughSceneManager() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            invokePrivate("onNormalBattle", new Class<?>[]{});
        });

        verify(sceneManager).showBattleGameScreen("NORMAL");
    }

    @Test
    void itemBattleModeNavigatesThroughSceneManager() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            invokePrivate("onItemBattle", new Class<?>[]{});
        });

        verify(sceneManager).showBattleGameScreen("ITEM");
    }

    @Test
    void timeLimitBattleModeNavigatesThroughSceneManager() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            invokePrivate("onTimeLimitBattle", new Class<?>[]{});
        });

        verify(sceneManager).showBattleGameScreen("TIME_LIMIT");
    }

    @Test
    void backActionReturnsToMainMenu() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            invokePrivate("onBack", new Class<?>[]{});
        });

        verify(sceneManager).showMainMenu();
    }

    @Test
    void selectCurrentButtonFollowsCurrentIndex() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            setIntField("currentIndex", 3); // back button index
            invokePrivate("selectCurrentButton", new Class<?>[]{});
        });

        verify(sceneManager).showMainMenu();
    }

    @Test
    void navigateToPreviousButtonWrapsAround() {
        runOnFxThreadAndWait(() -> {
            setIntField("currentIndex", 0);
            invokePrivate("navigateToPreviousButton", new Class<?>[]{});
            assertEquals(3, getIntField("currentIndex"));
        });
    }

    @Test
    void selectButtonAppliesFocusStyle() {
        runOnFxThreadAndWait(() -> {
            invokePrivate("selectButton", new Class<?>[]{int.class}, 2);
            assertTrue(timeLimitButton.getStyleClass().contains("focused"));
            assertFalse(normalButton.getStyleClass().contains("focused"));
        });
    }

    @Test
    void initializeAdjustsBackgroundSizeFromSettings() {
        runOnFxThreadAndWait(() -> {
            settingsManager.setScreenSize("작게");
            BattleModeSelectionController tempController = new BattleModeSelectionController();
            injectUiSkeleton(tempController);
            tempController.initialize(null, null);
            ImageView tempBackground = (ImageView) getFieldFromTarget(tempController, "backgroundImage");
            assertEquals(480, tempBackground.getFitWidth());
            assertEquals(720, tempBackground.getFitHeight());
        });
    }

    private Object getFieldFromTarget(BattleModeSelectionController target, String fieldName) {
        try {
            Field field = BattleModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }
}
