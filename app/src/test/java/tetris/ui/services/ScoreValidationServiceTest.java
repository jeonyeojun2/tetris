package tetris.ui.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.data.ScoreManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ScoreValidationService 테스트
 * UI 없이 순수 로직만 테스트하므로 높은 커버리지 달성 가능
 */
class ScoreValidationServiceTest {

    private ScoreValidationService service;
    private ScoreManager scoreManager;

    @BeforeEach
    void setUp() {
        scoreManager = ScoreManager.getInstance();
        // 모든 게임 모드의 점수 초기화
        scoreManager.clearScores("NORMAL");
        scoreManager.clearScores("ITEM");
        service = new ScoreValidationService(scoreManager);
    }

    @Test
    void testCanSaveScore_EmptyScores() {
        // 점수가 없을 때는 항상 저장 가능
        assertTrue(service.canSaveScore(100, "NORMAL", "보통"));
        assertTrue(service.canSaveScore(0, "ITEM", ""));
    }

    @Test
    void testCanSaveScore_LessThan10Scores() {
        // 10개 미만일 때는 항상 저장 가능
        for (int i = 1; i <= 5; i++) {
            scoreManager.addScore("Player" + i, i * 100, "NORMAL", "보통");
        }
        
        assertTrue(service.canSaveScore(50, "NORMAL", "보통"));
        assertTrue(service.canSaveScore(600, "NORMAL", "보통"));
    }

    @Test
    void testCanSaveScore_MoreThan10Scores_HigherScore() {
        // 10개 이상일 때 더 높은 점수는 저장 가능
        for (int i = 1; i <= 10; i++) {
            scoreManager.addScore("Player" + i, i * 100, "NORMAL", "보통");
        }
        
        // 1000점보다 높은 점수는 저장 가능
        assertTrue(service.canSaveScore(1100, "NORMAL", "보통"));
        assertTrue(service.canSaveScore(1000, "NORMAL", "보통"));
    }

    @Test
    void testCanSaveScore_MoreThan10Scores_LowerScore() {
        // 10개 이상일 때 더 낮은 점수는 저장 불가
        for (int i = 1; i <= 10; i++) {
            scoreManager.addScore("Player" + i, (11 - i) * 100, "NORMAL", "보통"); // 높은 점수부터
        }
        
        // 10위 점수가 100점일 때
        assertFalse(service.canSaveScore(99, "NORMAL", "보통"));
        assertTrue(service.canSaveScore(100, "NORMAL", "보통")); // 동점은 저장 가능
    }

    @Test
    void testCanSaveScore_ItemMode() {
        // 아이템 모드 테스트
        for (int i = 1; i <= 10; i++) {
            scoreManager.addScore("Player" + i, (11 - i) * 200, "ITEM", "");
        }
        
        assertTrue(service.canSaveScore(2100, "ITEM", ""));
        assertTrue(service.canSaveScore(200, "ITEM", "")); // 10위와 동점
    }

    @Test
    void testParseScoreFromEntry_Valid() {
        // 정상적인 점수 문자열 파싱
        String entry = "1. Alice - 1500점 (2025-11-23)";
        assertEquals(1500, service.parseScoreFromEntry(entry));
        
        entry = "5. Bob - 800점 (2025-11-22)";
        assertEquals(800, service.parseScoreFromEntry(entry));
    }

    @Test
    void testParseScoreFromEntry_Invalid() {
        // 잘못된 형식의 문자열
        assertNull(service.parseScoreFromEntry("Invalid format"));
        assertNull(service.parseScoreFromEntry("1. Alice"));
        assertNull(service.parseScoreFromEntry(""));
    }

    @Test
    void testParseScoreFromEntry_EdgeCases() {
        // 엣지 케이스
        assertNull(service.parseScoreFromEntry(null));
        assertNull(service.parseScoreFromEntry("- 100점"));
        assertNull(service.parseScoreFromEntry("1. Name - abc점"));
    }

    @Test
    void testIsValidPlayerName_Valid() {
        // 유효한 이름
        assertTrue(service.isValidPlayerName("Alice"));
        assertTrue(service.isValidPlayerName("Player123"));
        assertTrue(service.isValidPlayerName("한글이름"));
        assertTrue(service.isValidPlayerName("A"));
    }

    @Test
    void testIsValidPlayerName_Invalid() {
        // 유효하지 않은 이름
        assertFalse(service.isValidPlayerName(null));
        assertFalse(service.isValidPlayerName(""));
        assertFalse(service.isValidPlayerName("   "));
    }

    @Test
    void testIsValidPlayerName_TooLong() {
        // 너무 긴 이름 (20자 초과)
        String longName = "A".repeat(21);
        assertFalse(service.isValidPlayerName(longName));
        
        // 정확히 20자는 OK
        String exactName = "A".repeat(20);
        assertTrue(service.isValidPlayerName(exactName));
    }

    @Test
    void testIsValidPlayerName_WithWhitespace() {
        // 공백이 포함된 이름
        assertTrue(service.isValidPlayerName("  Alice  ")); // trim 후 유효
        assertFalse(service.isValidPlayerName("     ")); // trim 후 비어있음
    }

    @Test
    void testSaveScoreAndGetRank_Valid() {
        // 정상적인 점수 저장
        int rank1 = service.saveScoreAndGetRank("Alice", 1000, "NORMAL", "보통");
        assertTrue(rank1 >= 1); // 순위 확인
        
        int rank2 = service.saveScoreAndGetRank("Bob", 800, "NORMAL", "보통");
        assertTrue(rank2 >= rank1); // Bob이 Alice보다 낮거나 같은 순위
        
        int rank3 = service.saveScoreAndGetRank("Charlie", 1200, "NORMAL", "보통");
        assertTrue(rank3 <= rank1); // Charlie가 가장 높은 점수
    }

    @Test
    void testSaveScoreAndGetRank_InvalidName() {
        // 잘못된 이름으로 저장 시도
        int rank = service.saveScoreAndGetRank("", 1000, "NORMAL", "보통");
        assertEquals(-1, rank);
        
        rank = service.saveScoreAndGetRank(null, 1000, "NORMAL", "보통");
        assertEquals(-1, rank);
    }

    @Test
    void testSaveScoreAndGetRank_ItemMode() {
        // 아이템 모드에서 점수 저장
        int rank1 = service.saveScoreAndGetRank("Alice", 2000, "ITEM", "");
        assertTrue(rank1 >= 1);
        
        int rank2 = service.saveScoreAndGetRank("Bob", 1500, "ITEM", "");
        assertTrue(rank2 >= rank1); // Bob이 Alice보다 낮거나 같은 순위
    }

    @Test
    void testSaveScoreAndGetRank_WithWhitespace() {
        // 공백이 포함된 이름은 trim 후 저장
        int rank = service.saveScoreAndGetRank("  Alice  ", 1000, "NORMAL", "보통");
        assertTrue(rank >= 1);
        
        // 저장된 이름 확인
        var scores = scoreManager.getFormattedScoresByDifficulty("NORMAL", "보통");
        assertFalse(scores.isEmpty());
        assertTrue(scores.get(0).contains("Alice"));
    }

    @Test
    void testGetRankColorClass() {
        // 순위에 따른 색상 클래스
        assertEquals("rank-gold", service.getRankColorClass(1));
        assertEquals("rank-silver", service.getRankColorClass(2));
        assertEquals("rank-bronze", service.getRankColorClass(3));
        assertEquals("rank-top10", service.getRankColorClass(4));
        assertEquals("rank-top10", service.getRankColorClass(10));
        assertEquals("rank-normal", service.getRankColorClass(11));
        assertEquals("rank-normal", service.getRankColorClass(0));
    }

    @Test
    void testIntegration_FullScoreSaveFlow() {
        // 전체 플로우 테스트
        String playerName = "IntegrationTest";
        int score = 5000;
        String gameMode = "NORMAL";
        String difficulty = "어려움";
        
        // 1. 저장 가능 여부 확인
        assertTrue(service.canSaveScore(score, gameMode, difficulty));
        
        // 2. 이름 유효성 검증
        assertTrue(service.isValidPlayerName(playerName));
        
        // 3. 점수 저장 및 순위 확인
        int rank = service.saveScoreAndGetRank(playerName, score, gameMode, difficulty);
        assertEquals(1, rank);
        
        // 4. 순위 색상 클래스 확인
        String colorClass = service.getRankColorClass(rank);
        assertEquals("rank-gold", colorClass);
    }
}
