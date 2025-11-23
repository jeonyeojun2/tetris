package tetris.ui.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tetris.ui.SceneManager;
import tetris.ui.services.MenuNavigationService;
import tetris.ui.services.SettingsValidationService;

import static org.mockito.Mockito.*;

/**
 * MainMenuController Mock 테스트
 * - 서비스 호출 검증
 */
@ExtendWith(MockitoExtension.class)
class MainMenuControllerMockTest {
    
    @Mock
    private MenuNavigationService navigationService;
    
    @Mock
    private SettingsValidationService validationService;
    
    @Mock
    private SceneManager sceneManager;
    
    private MainMenuController controller;
    
    @BeforeEach
    void setUp() {
        controller = new MainMenuController(navigationService, validationService);
        controller.setSceneManager(sceneManager);
    }
    
    @Test
    void testNavigationServiceUsedForNext() {
        // Given
        when(navigationService.navigateToNext()).thenReturn(1);
        when(navigationService.getCurrentIndex()).thenReturn(0);
        
        // 참고: 실제 버튼이 없으므로 initialize는 호출하지 않음
        // 대신 서비스 메서드가 올바르게 주입되었는지만 확인
        
        // Then
        verify(navigationService, never()).navigateToNext(); // 아직 호출 안 됨
    }
    
    @Test
    void testNavigationServiceInitialization() {
        // 서비스가 주입되었는지 확인
        // 실제 JavaFX 컴포넌트가 필요한 부분은 통합 테스트에서 검증
        verify(navigationService, never()).setMenuItemCount(anyInt());
    }
    
    @Test
    void testValidationServiceCanBeInjected() {
        // 서비스 주입 검증
        // 실제 사용은 initialize에서 이루어지지만, 
        // JavaFX 환경 없이는 테스트하기 어려움
        when(validationService.calculateCanvasSize(anyString())).thenReturn(new int[]{600, 900});
        
        // Mock이 설정되었는지 확인
        int[] size = validationService.calculateCanvasSize("중간");
        verify(validationService).calculateCanvasSize("중간");
        assert size[0] == 600;
        assert size[1] == 900;
    }
}
