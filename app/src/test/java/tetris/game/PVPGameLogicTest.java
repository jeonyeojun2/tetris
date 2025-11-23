package tetris.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PVPGameLogic 클래스의 단위 테스트
 */
class PVPGameLogicTest {
    
    private PVPGameLogic gameLogic;
    
    @BeforeEach
    void setUp() {
        gameLogic = new PVPGameLogic();
    }
    
    @Test
    void testDetermineWinner_Win() {
        String result = gameLogic.determineWinner(1000, 500);
        assertEquals("WIN", result, "높은 점수일 때 WIN 반환");
    }
    
    @Test
    void testDetermineWinner_Lose() {
        String result = gameLogic.determineWinner(500, 1000);
        assertEquals("LOSE", result, "낮은 점수일 때 LOSE 반환");
    }
    
    @Test
    void testDetermineWinner_Draw() {
        String result = gameLogic.determineWinner(1000, 1000);
        assertEquals("DRAW", result, "동점일 때 DRAW 반환");
    }
    
    @Test
    void testFormatTime() {
        assertEquals("03:00", gameLogic.formatTime(180), "3분 포맷");
        assertEquals("02:30", gameLogic.formatTime(150), "2분 30초 포맷");
        assertEquals("00:45", gameLogic.formatTime(45), "45초 포맷");
        assertEquals("00:00", gameLogic.formatTime(0), "0초 포맷");
    }
    
    @Test
    void testCalculateRemainingTime() {
        long startTime = 1000_000_000_000L; // 1000초 시점
        long currentTime = 1030_000_000_000L; // 1030초 시점 (30초 경과)
        long duration = 180; // 3분
        
        long remaining = gameLogic.calculateRemainingTime(startTime, currentTime, duration);
        assertEquals(150, remaining, "180 - 30 = 150초 남음");
    }
    
    @Test
    void testCalculateRemainingTime_Negative() {
        long startTime = 1000_000_000_000L;
        long currentTime = 1200_000_000_000L; // 200초 경과
        long duration = 180; // 3분
        
        long remaining = gameLogic.calculateRemainingTime(startTime, currentTime, duration);
        assertEquals(-20, remaining, "시간 초과 시 음수 반환");
    }
    
    @Test
    void testIsTimeUp_NotYet() {
        long startTime = 1000_000_000_000L;
        long currentTime = 1100_000_000_000L; // 100초 경과
        long duration = 180; // 3분
        
        assertFalse(gameLogic.isTimeUp(startTime, currentTime, duration), 
                "아직 시간이 남았으면 false");
    }
    
    @Test
    void testIsTimeUp_Exact() {
        long startTime = 1000_000_000_000L;
        long currentTime = 1180_000_000_000L; // 정확히 180초 경과
        long duration = 180;
        
        assertTrue(gameLogic.isTimeUp(startTime, currentTime, duration), 
                "정확히 시간이 되면 true");
    }
    
    @Test
    void testIsTimeUp_Over() {
        long startTime = 1000_000_000_000L;
        long currentTime = 1200_000_000_000L; // 200초 경과
        long duration = 180;
        
        assertTrue(gameLogic.isTimeUp(startTime, currentTime, duration), 
                "시간 초과하면 true");
    }
    
    @Test
    void testCanSendAttack() {
        assertFalse(gameLogic.canSendAttack(0), "0줄 삭제 시 공격 불가");
        assertFalse(gameLogic.canSendAttack(1), "1줄 삭제 시 공격 불가");
        assertTrue(gameLogic.canSendAttack(2), "2줄 삭제 시 공격 가능");
        assertTrue(gameLogic.canSendAttack(3), "3줄 삭제 시 공격 가능");
        assertTrue(gameLogic.canSendAttack(4), "4줄 삭제 시 공격 가능");
    }
    
    @Test
    void testEvaluateNetworkStatus_Good() {
        assertEquals("GOOD", gameLogic.evaluateNetworkStatus(50), "50ms는 GOOD");
        assertEquals("GOOD", gameLogic.evaluateNetworkStatus(199), "199ms는 GOOD");
    }
    
    @Test
    void testEvaluateNetworkStatus_Warning() {
        assertEquals("WARNING", gameLogic.evaluateNetworkStatus(200), "200ms는 WARNING");
        assertEquals("WARNING", gameLogic.evaluateNetworkStatus(350), "350ms는 WARNING");
        assertEquals("WARNING", gameLogic.evaluateNetworkStatus(499), "499ms는 WARNING");
    }
    
    @Test
    void testEvaluateNetworkStatus_Critical() {
        assertEquals("CRITICAL", gameLogic.evaluateNetworkStatus(500), "500ms는 CRITICAL");
        assertEquals("CRITICAL", gameLogic.evaluateNetworkStatus(1000), "1000ms는 CRITICAL");
    }
    
    @Test
    void testIsConnectionTimeout_NoTimeout() {
        long lastActivity = 1000_000_000_000L;
        long currentTime = 1005_000_000_000L; // 5초 경과
        long timeout = 10_000_000_000L; // 10초
        
        assertFalse(gameLogic.isConnectionTimeout(lastActivity, currentTime, timeout),
                "타임아웃 시간 내면 false");
    }
    
    @Test
    void testIsConnectionTimeout_Timeout() {
        long lastActivity = 1000_000_000_000L;
        long currentTime = 1011_000_000_000L; // 11초 경과
        long timeout = 10_000_000_000L; // 10초
        
        assertTrue(gameLogic.isConnectionTimeout(lastActivity, currentTime, timeout),
                "타임아웃 시간 초과하면 true");
    }
    
    @Test
    void testIsConnectionTimeout_NoActivity() {
        long lastActivity = 0; // 활동 기록 없음
        long currentTime = 1000_000_000_000L;
        long timeout = 10_000_000_000L;
        
        assertFalse(gameLogic.isConnectionTimeout(lastActivity, currentTime, timeout),
                "활동 기록이 없으면 false");
    }
    
    @Test
    void testCanSendAttackWithLines() {
        assertTrue(gameLogic.canSendAttack(1));
        assertTrue(gameLogic.canSendAttack(2));
        assertTrue(gameLogic.canSendAttack(4));
        assertFalse(gameLogic.canSendAttack(0));
    }
    
    @Test
    void testFormatTimeEdgeCases() {
        assertEquals("00:00", gameLogic.formatTime(-10));
        assertEquals("59:59", gameLogic.formatTime(3599));
        assertEquals("00:01", gameLogic.formatTime(1));
    }
    
    @Test
    void testDetermineWinnerZeroScores() {
        assertEquals("DRAW", gameLogic.determineWinner(0, 0));
    }
    
    @Test
    void testDetermineWinnerNegativeScores() {
        assertEquals("WIN", gameLogic.determineWinner(0, -100));
        assertEquals("LOSE", gameLogic.determineWinner(-100, 0));
    }
    
    @Test
    void testEvaluateNetworkStatusBoundaries() {
        assertEquals("GOOD", gameLogic.evaluateNetworkStatus(49));
        assertEquals("WARNING", gameLogic.evaluateNetworkStatus(50));
        assertEquals("WARNING", gameLogic.evaluateNetworkStatus(199));
        assertEquals("CRITICAL", gameLogic.evaluateNetworkStatus(200));
    }
    
    @Test
    void testCalculateRemainingTimeExactly() {
        long start = 1000_000_000_000L;
        long current = 1180_000_000_000L; // 정확히 180초 경과
        long duration = 180;
        
        assertEquals(0, gameLogic.calculateRemainingTime(start, current, duration));
    }
    
    @Test
    void testIsTimeUpBoundaryConditions() {
        long start = 1000_000_000_000L;
        long duration = 100;
        
        assertFalse(gameLogic.isTimeUp(start, 1099_999_999_999L, duration));
        assertTrue(gameLogic.isTimeUp(start, 1100_000_000_000L, duration));
    }
}
