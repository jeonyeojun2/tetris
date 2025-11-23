package tetris.ui.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionServiceTest {
    
    private GameSessionService service;
    
    @BeforeEach
    void setUp() {
        service = new GameSessionService();
    }
    
    @Test
    void testStartSession() {
        service.startSession();
        
        assertTrue(service.isGameRunning());
        assertFalse(service.isPaused());
        assertTrue(service.getSessionStartTime() > 0);
        assertEquals(0, service.getTotalPausedDuration());
    }
    
    @Test
    void testPauseSession() {
        service.startSession();
        service.pauseSession();
        
        assertTrue(service.isPaused());
        assertTrue(service.isGameRunning());
    }
    
    @Test
    void testResumeSession() {
        service.startSession();
        service.pauseSession();
        
        try {
            Thread.sleep(50); // 50ms 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        service.resumeSession();
        
        assertFalse(service.isPaused());
        assertTrue(service.isGameRunning());
        assertTrue(service.getTotalPausedDuration() > 0);
    }
    
    @Test
    void testEndSession() {
        service.startSession();
        service.endSession();
        
        assertFalse(service.isGameRunning());
        assertFalse(service.isPaused());
    }
    
    @Test
    void testCalculateFallSpeedLevel1() {
        long speed = service.calculateFallSpeed(1);
        assertEquals(1_000_000_000L, speed);
    }
    
    @Test
    void testCalculateFallSpeedLevel2() {
        long speed = service.calculateFallSpeed(2);
        assertTrue(speed < 1_000_000_000L);
        assertTrue(speed > 800_000_000L);
    }
    
    @Test
    void testCalculateFallSpeedLevel5() {
        long speed = service.calculateFallSpeed(5);
        assertTrue(speed < 700_000_000L);
    }
    
    @Test
    void testCalculateFallSpeedInvalidLevel() {
        long speed = service.calculateFallSpeed(0);
        assertEquals(1_000_000_000L, speed); // 레벨 0은 1로 처리
        
        speed = service.calculateFallSpeed(-5);
        assertEquals(1_000_000_000L, speed); // 음수도 1로 처리
    }
    
    @Test
    void testGetElapsedGameTimeWhenNotRunning() {
        long elapsed = service.getElapsedGameTime();
        assertEquals(0, elapsed);
    }
    
    @Test
    void testGetElapsedGameTimeWhenRunning() {
        service.startSession();
        
        try {
            Thread.sleep(100); // 100ms 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long elapsed = service.getElapsedGameTime();
        assertTrue(elapsed > 0);
        assertTrue(elapsed >= 100_000_000L); // 최소 100ms (나노초로)
    }
    
    @Test
    void testGetElapsedGameTimeWithPause() {
        service.startSession();
        
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        service.pauseSession();
        
        try {
            Thread.sleep(100); // 일시정지 중 100ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        service.resumeSession();
        
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long elapsed = service.getElapsedGameTime();
        // 일시정지 시간은 제외되어야 함 (약 100ms 제외)
        assertTrue(elapsed < 200_000_000L); // 200ms보다 작아야 함
    }
    
    @Test
    void testGetInitialFallSpeedByDifficultyEasy() {
        long speed = service.getInitialFallSpeedByDifficulty("Easy");
        assertEquals(1_500_000_000L, speed);
    }
    
    @Test
    void testGetInitialFallSpeedByDifficultyNormal() {
        long speed = service.getInitialFallSpeedByDifficulty("Normal");
        assertEquals(1_000_000_000L, speed);
    }
    
    @Test
    void testGetInitialFallSpeedByDifficultyHard() {
        long speed = service.getInitialFallSpeedByDifficulty("Hard");
        assertEquals(700_000_000L, speed);
    }
    
    @Test
    void testGetInitialFallSpeedByDifficultyInvalid() {
        long speed = service.getInitialFallSpeedByDifficulty("Invalid");
        assertEquals(1_000_000_000L, speed); // 기본값 Normal
    }
    
    @Test
    void testGetInitialFallSpeedByDifficultyNull() {
        long speed = service.getInitialFallSpeedByDifficulty(null);
        assertEquals(1_000_000_000L, speed); // 기본값 Normal
    }
    
    @Test
    void testMultiplePauseResumeCycles() {
        service.startSession();
        
        // 첫 번째 사이클
        service.pauseSession();
        try { Thread.sleep(20); } catch (InterruptedException e) {}
        service.resumeSession();
        
        long firstPauseDuration = service.getTotalPausedDuration();
        assertTrue(firstPauseDuration > 0);
        
        // 두 번째 사이클
        service.pauseSession();
        try { Thread.sleep(20); } catch (InterruptedException e) {}
        service.resumeSession();
        
        long secondPauseDuration = service.getTotalPausedDuration();
        assertTrue(secondPauseDuration > firstPauseDuration);
    }
    
    @Test
    void testPauseWhenAlreadyPaused() {
        service.startSession();
        service.pauseSession();
        long firstPauseTime = service.getTotalPausedDuration();
        
        service.pauseSession(); // 이미 일시정지된 상태에서 다시 호출
        
        // 상태는 변하지 않아야 함
        assertTrue(service.isPaused());
    }
    
    @Test
    void testResumeWhenNotPaused() {
        service.startSession();
        
        service.resumeSession(); // 일시정지되지 않은 상태에서 재개 시도
        
        // 상태는 변하지 않아야 함
        assertTrue(service.isGameRunning());
        assertFalse(service.isPaused());
    }
}
