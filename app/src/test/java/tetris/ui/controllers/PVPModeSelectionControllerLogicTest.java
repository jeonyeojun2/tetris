package tetris.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import tetris.network.GameServer;
import tetris.ui.SceneManager;
import tetris.ui.SettingsManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PVPModeSelectionControllerLogicTest extends JavaFXTestBase {

    private PVPModeSelectionController controller;
    private Button serverButton;
    private Button clientButton;
    private Button backButton;
    private ImageView backgroundImage;
    private SettingsManager settingsManager;
    private String originalScreenSize;

    @BeforeEach
    void setUp() {
        settingsManager = SettingsManager.getInstance();
        originalScreenSize = settingsManager.getScreenSize();

        runOnFxThreadAndWait(() -> {
            controller = new PVPModeSelectionController();
            injectUiSkeleton(controller);
            controller.initialize(null, null);
        });
    }

    @AfterEach
    void tearDown() {
        settingsManager.setScreenSize(originalScreenSize);
    }

    private void injectUiSkeleton(PVPModeSelectionController target) {
        Button localServer = new Button();
        Button localClient = new Button();
        Button localBack = new Button();
        ImageView localBackground = new ImageView();

        setField(target, "serverButton", localServer);
        setField(target, "clientButton", localClient);
        setField(target, "backButton", localBack);
        setField(target, "backgroundImage", localBackground);

        if (target == controller) {
            serverButton = localServer;
            clientButton = localClient;
            backButton = localBack;
            backgroundImage = localBackground;
        }
    }

    private void setField(PVPModeSelectionController target, String fieldName, Object value) {
        try {
            Field field = PVPModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            fail("Failed to inject field " + fieldName + ": " + e.getMessage());
        }
    }

    private void setIntField(String fieldName, int value) {
        try {
            Field field = PVPModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setInt(controller, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set field " + fieldName, e);
        }
    }

    private int getIntField(String fieldName) {
        try {
            Field field = PVPModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(controller);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }

    private void invokePrivate(String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Method method = PVPModeSelectionController.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(controller, args);
        } catch (ReflectiveOperationException e) {
            fail("Failed to invoke " + methodName + ": " + e.getMessage());
        }
    }

    @Test
    void clientModeNavigatesToConnectionScreen() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            invokePrivate("onClientMode", new Class<?>[]{});
        });

        verify(sceneManager).showPVPClientConnection();
    }

    @Test
    void backButtonReturnsToMainMenu() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            invokePrivate("onBack", new Class<?>[]{});
        });

        verify(sceneManager).showMainMenu();
    }

    @Test
    void selectCurrentButtonUsesCurrentIndex() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            controller.setSceneManager(sceneManager);
            setIntField("currentIndex", 1); // client button
            invokePrivate("selectCurrentButton", new Class<?>[]{});
        });

        verify(sceneManager).showPVPClientConnection();
    }

    @Test
    void navigateToNextButtonCyclesThroughButtons() {
        runOnFxThreadAndWait(() -> {
            setIntField("currentIndex", 0);
            invokePrivate("navigateToNextButton", new Class<?>[]{});
            assertEquals(1, getIntField("currentIndex"));
            invokePrivate("navigateToNextButton", new Class<?>[]{});
            assertEquals(2, getIntField("currentIndex"));
            invokePrivate("navigateToNextButton", new Class<?>[]{});
            assertEquals(0, getIntField("currentIndex"));
        });
    }

    @Test
    void selectButtonAppliesFocusStyle() {
        runOnFxThreadAndWait(() -> {
            invokePrivate("selectButton", new Class<?>[]{int.class}, 2);
            assertTrue(backButton.getStyleClass().contains("focused"));
            assertFalse(clientButton.getStyleClass().contains("focused"));
        });
    }

    @Test
    void initializeAdjustsBackgroundForLargeScreens() {
        runOnFxThreadAndWait(() -> {
            settingsManager.setScreenSize("크게");
            PVPModeSelectionController tempController = new PVPModeSelectionController();
            injectUiSkeleton(tempController);
            tempController.initialize(null, null);
            ImageView tempBackground = (ImageView) getFieldFromTarget(tempController, "backgroundImage");
            assertEquals(720, tempBackground.getFitWidth());
            assertEquals(1080, tempBackground.getFitHeight());
        });
    }

    @Test
    void serverModeStartsServerAndShowsWaitingScreen() {
        SceneManager sceneManager = mock(SceneManager.class);

        runOnFxThreadAndWait(() -> {
            try (MockedConstruction<GameServer> mocked = mockConstruction(GameServer.class, (mock, context) -> {
                when(mock.getServerIP()).thenReturn("10.0.0.5");
            })) {
                controller.setSceneManager(sceneManager);
                invokePrivate("onServerMode", new Class<?>[]{});

                assertFalse(mocked.constructed().isEmpty(), "GameServer should be constructed");
                GameServer mockServer = mocked.constructed().get(0);
                verify(mockServer).start();
                verify(sceneManager).showPVPServerWaiting(mockServer, "10.0.0.5");
            } catch (Exception e) {
                fail("Server mode should not throw: " + e.getMessage());
            }
        });
    }

    private Object getFieldFromTarget(PVPModeSelectionController target, String fieldName) {
        try {
            Field field = PVPModeSelectionController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read field " + fieldName, e);
        }
    }
}
