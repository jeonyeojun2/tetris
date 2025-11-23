package tetris.ui.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BattleGameService 테스트
 */
class BattleGameServiceTest {
    
    private BattleGameService service;
    
    @BeforeEach
    void setUp() {
        service = new BattleGameService();
    }
    
    @Test
    void testCalculateAttackPower() {
        assertEquals(0, service.calculateAttackPower(0));
        assertEquals(0, service.calculateAttackPower(1));
        assertEquals(1, service.calculateAttackPower(2));
        assertEquals(2, service.calculateAttackPower(3));
        assertEquals(4, service.calculateAttackPower(4));
        assertEquals(0, service.calculateAttackPower(5));
    }
    
    @Test
    void testSelectEmptyColumn_Valid() {
        assertEquals(5, service.selectEmptyColumn(5, 10));
        assertEquals(0, service.selectEmptyColumn(0, 10));
        assertEquals(9, service.selectEmptyColumn(9, 10));
    }
    
    @Test
    void testSelectEmptyColumn_Invalid() {
        assertEquals(5, service.selectEmptyColumn(-1, 10));
        assertEquals(5, service.selectEmptyColumn(10, 10));
    }
    
    @Test
    void testLimitAttackLines() {
        assertEquals(3, service.limitAttackLines(3, 5));
        assertEquals(5, service.limitAttackLines(7, 5));
        assertEquals(0, service.limitAttackLines(0, 5));
    }
    
    @Test
    void testDetermineWinnerByScore() {
        assertEquals("Player 1", service.determineWinnerByScore(1000, 500));
        assertEquals("Player 2", service.determineWinnerByScore(500, 1000));
        assertEquals("Draw", service.determineWinnerByScore(1000, 1000));
    }
    
    @Test
    void testIsGameOver() {
        assertFalse(service.isGameOver(true, true));
        assertTrue(service.isGameOver(false, true));
        assertTrue(service.isGameOver(true, false));
        assertTrue(service.isGameOver(false, false));
    }
    
    @Test
    void testCalculateRemainingSeconds() {
        assertEquals(0, service.calculateRemainingSeconds(0));
        assertEquals(1, service.calculateRemainingSeconds(1_000_000_000));
        assertEquals(60, service.calculateRemainingSeconds(60_000_000_000L));
        assertEquals(0, service.calculateRemainingSeconds(-1000));
    }
    
    @Test
    void testIsTimeLimitReached() {
        assertTrue(service.isTimeLimitReached(0));
        assertTrue(service.isTimeLimitReached(-1000));
        assertFalse(service.isTimeLimitReached(1000));
    }
    
    @Test
    void testCreateVictoryMessage() {
        assertEquals("무승부!", service.createVictoryMessage("Draw", "시간 종료"));
        assertEquals("Player 1 승리! (상대 게임 오버)", 
                    service.createVictoryMessage("Player 1", "상대 게임 오버"));
        assertEquals("Player 2 승리! (높은 점수)", 
                    service.createVictoryMessage("Player 2", "높은 점수"));
    }
    
    @Test
    void testFormatPendingAttacks() {
        assertEquals("", service.formatPendingAttacks(0));
        assertEquals("↓ 1", service.formatPendingAttacks(1));
        assertEquals("↓ 5", service.formatPendingAttacks(5));
    }
    
    @Test
    void testCreateComboMessage() {
        assertEquals("", service.createComboMessage(0));
        assertEquals("", service.createComboMessage(1));
        assertEquals("Double!", service.createComboMessage(2));
        assertEquals("Triple!", service.createComboMessage(3));
        assertEquals("Tetris!", service.createComboMessage(4));
        assertEquals("Tetris!", service.createComboMessage(5));
    }
    
    @Test
    void testGetGameModeDisplayName() {
        assertEquals("일반 대전", service.getGameModeDisplayName("NORMAL"));
        assertEquals("아이템 대전", service.getGameModeDisplayName("ITEM"));
        assertEquals("시간제한 대전", service.getGameModeDisplayName("TIME_LIMIT"));
        assertEquals("CUSTOM", service.getGameModeDisplayName("CUSTOM"));
    }
    
    @Test
    void testGetPlayerName() {
        assertEquals("Player 1", service.getPlayerName(1));
        assertEquals("Player 2", service.getPlayerName(2));
    }
    
    @Test
    void testCanAddAttackLines() {
        assertTrue(service.canAddAttackLines(10, 5, 20));
        assertTrue(service.canAddAttackLines(15, 5, 20));
        assertFalse(service.canAddAttackLines(16, 5, 20));
        assertFalse(service.canAddAttackLines(20, 1, 20));
    }
    
    @Test
    void testCalculateHandicap() {
        assertEquals(1.0, service.calculateHandicap(5, 5));
        assertEquals(1.0, service.calculateHandicap(5, 6));
        assertEquals(0.9, service.calculateHandicap(5, 7));
        assertEquals(0.9, service.calculateHandicap(5, 8));
        assertEquals(0.8, service.calculateHandicap(5, 9));
    }
    
    @Test
    void testCalculateComboBonus() {
        assertEquals(0, service.calculateComboBonus(0));
        assertEquals(0, service.calculateComboBonus(1));
        assertEquals(50, service.calculateComboBonus(2));
        assertEquals(100, service.calculateComboBonus(3));
        assertEquals(150, service.calculateComboBonus(4));
    }
    
    @Test
    void testDeterminePriority() {
        assertEquals(1, service.determinePriority(4, 2));
        assertEquals(2, service.determinePriority(1, 3));
        assertEquals(0, service.determinePriority(2, 2));
    }
    
    @Test
    void testCalculateScoreDifference() {
        assertEquals(500, service.calculateScoreDifference(1000, 500));
        assertEquals(500, service.calculateScoreDifference(500, 1000));
        assertEquals(0, service.calculateScoreDifference(1000, 1000));
    }
    
    @Test
    void testIsComebackPossible() {
        assertTrue(service.isComebackPossible(4000, 60));
        assertTrue(service.isComebackPossible(5000, 30));
        assertFalse(service.isComebackPossible(6000, 60));
        assertFalse(service.isComebackPossible(4000, 29));
        assertFalse(service.isComebackPossible(5001, 30));
    }
    
    @Test
    void testAdjustDifficulty() {
        assertEquals(1.0, service.adjustDifficulty(0));
        assertEquals(1.0, service.adjustDifficulty(1));
        assertEquals(1.0, service.adjustDifficulty(2));
        assertEquals(1.1, service.adjustDifficulty(3));
        assertEquals(1.1, service.adjustDifficulty(4));
        assertEquals(1.2, service.adjustDifficulty(5));
        assertEquals(1.2, service.adjustDifficulty(10));
        assertEquals(0.9, service.adjustDifficulty(-3));
        assertEquals(0.9, service.adjustDifficulty(-5));
    }
    
    @Test
    void testCalculateAttackEfficiency() {
        assertEquals(2.0, service.calculateAttackEfficiency(10, 5));
        assertEquals(0.5, service.calculateAttackEfficiency(5, 10));
        assertEquals(1.0, service.calculateAttackEfficiency(10, 10));
        assertEquals(Double.POSITIVE_INFINITY, service.calculateAttackEfficiency(10, 0));
        assertEquals(0.0, service.calculateAttackEfficiency(0, 0));
    }
    
    @Test
    void testDetermineMVP() {
        assertEquals("Player 1", service.determineMVP(1000, 500, 5, 3));
        assertEquals("Player 2", service.determineMVP(500, 1000, 3, 5));
        assertEquals("Player 2", service.determineMVP(1000, 900, 0, 2)); // 공격 보너스로 역전
        assertEquals("Both", service.determineMVP(1000, 1000, 0, 0));
    }
}
