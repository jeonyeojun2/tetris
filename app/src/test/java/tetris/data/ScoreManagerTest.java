package tetris.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ScoreManagerTest {

    @BeforeEach
    void setUp() {
        // 테스트 전에 ScoreManager 인스턴스 초기화를 위해
        try {
            // 테스트용 임시 디렉토리 사용
            Path testDataDir = Paths.get(System.getProperty("java.io.tmpdir"), "TetrisTest");
            if (Files.exists(testDataDir)) {
                Files.list(testDataDir)
                    .filter(p -> p.toString().endsWith(".dat"))
                    .forEach(p -> {
                        try { Files.delete(p); } catch (Exception e) {}
                    });
            }
        } catch (Exception e) {
            // 무시
        }
    }

    @Test
    void testGetInstance() {
        ScoreManager instance1 = ScoreManager.getInstance();
        ScoreManager instance2 = ScoreManager.getInstance();
        
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    void testAddScore_Normal() {
        ScoreManager manager = ScoreManager.getInstance();
        
        boolean isTopTen = manager.addScore("Player1", 1000, "Normal", "NORMAL");
        assertTrue(isTopTen); // 첫 점수이므로 상위 10위 안
        
        assertEquals(1000, manager.getHighScore("NORMAL"));
    }

    @Test
    void testAddScore_Item() {
        ScoreManager manager = ScoreManager.getInstance();
        
        boolean isTopTen = manager.addScore("Player1", 500, "Normal", "ITEM");
        
        assertEquals(500, manager.getHighScore("ITEM"));
    }

    @Test
    void testGetFormattedScores() {
        ScoreManager manager = ScoreManager.getInstance();
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.addScore("Player2", 500, "Easy", "NORMAL");
        
        var scores = manager.getFormattedScores("NORMAL");
        assertNotNull(scores);
        assertTrue(scores.size() > 0);
    }

    @Test
    void testGetFormattedScoresByDifficulty() {
        ScoreManager manager = ScoreManager.getInstance();
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.addScore("Player2", 500, "Easy", "NORMAL");
        
        var scores = manager.getFormattedScoresByDifficulty("NORMAL", "Normal");
        assertNotNull(scores);
    }

    @Test
    void testClearScores() {
        ScoreManager manager = ScoreManager.getInstance();
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.clearScores("NORMAL");
        
        assertEquals(0, manager.getHighScore("NORMAL"));
    }

    @Test
    void testClearScoresByDifficulty() {
        ScoreManager manager = ScoreManager.getInstance();
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.addScore("Player2", 500, "Easy", "NORMAL");
        
        manager.clearScoresByDifficulty("NORMAL", "Easy");
    }
    
    @Test
    void testGetRank_FirstPlace() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.addScore("Player2", 800, "Normal", "NORMAL");
        
        int rank = manager.getRank(1200, "NORMAL");
        assertEquals(1, rank);
    }
    
    @Test
    void testGetRank_MiddlePlace() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.addScore("Player2", 800, "Normal", "NORMAL");
        manager.addScore("Player3", 600, "Normal", "NORMAL");
        
        int rank = manager.getRank(750, "NORMAL");
        assertEquals(3, rank);
    }
    
    @Test
    void testGetRank_LastPlace() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        // 10개 채우기
        for (int i = 0; i < 10; i++) {
            manager.addScore("Player" + i, (10 - i) * 100, "Normal", "NORMAL");
        }
        
        int rank = manager.getRank(50, "NORMAL");
        assertEquals(-1, rank); // 10개 꽉 찼고 최하위보다 낮으면 -1
    }
    
    @Test
    void testGetRank_EmptyScores() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        int rank = manager.getRank(500, "NORMAL");
        assertEquals(1, rank);
    }
    
    @Test
    void testGetRankByDifficulty_Normal() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.addScore("Player2", 800, "Easy", "NORMAL");
        manager.addScore("Player3", 600, "Normal", "NORMAL");
        
        int rank = manager.getRankByDifficulty(750, "NORMAL", "Normal");
        assertEquals(2, rank); // Normal 난이도 내에서 2위
    }
    
    @Test
    void testGetRankByDifficulty_ItemMode() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("ITEM");
        
        manager.addScore("Player1", 500, "Normal", "ITEM");
        
        int rank = manager.getRankByDifficulty(600, "ITEM", "Normal");
        assertEquals(1, rank); // ITEM 모드는 전체 순위 반환
    }
    
    @Test
    void testGetRankByDifficulty_EmptyDifficulty() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        manager.addScore("Player1", 1000, "Easy", "NORMAL");
        
        int rank = manager.getRankByDifficulty(500, "NORMAL", "Hard");
        assertEquals(1, rank); // Hard 난이도에 아무도 없으면 1위
    }
    
    @Test
    void testGetHighScore_Empty() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        assertEquals(0, manager.getHighScore("NORMAL"));
    }
    
    @Test
    void testGetHighScore_WithScores() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        manager.addScore("Player1", 500, "Normal", "NORMAL");
        manager.addScore("Player2", 1000, "Normal", "NORMAL");
        manager.addScore("Player3", 750, "Normal", "NORMAL");
        
        assertEquals(1000, manager.getHighScore("NORMAL"));
    }
    
    @Test
    void testAddScore_MoreThan10Scores() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        // 15개의 점수 추가 (같은 난이도)
        for (int i = 0; i < 15; i++) {
            manager.addScore("Player" + i, (15 - i) * 100, "Normal", "NORMAL");
        }
        
        var scores = manager.getFormattedScoresByDifficulty("NORMAL", "Normal");
        assertTrue(scores.size() <= 10); // 최대 10개만 유지
    }
    
    @Test
    void testAddScore_MultipleDifficulties() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        manager.addScore("Easy1", 500, "Easy", "NORMAL");
        manager.addScore("Normal1", 600, "Normal", "NORMAL");
        manager.addScore("Hard1", 700, "Hard", "NORMAL");
        
        var easyScores = manager.getFormattedScoresByDifficulty("NORMAL", "Easy");
        var normalScores = manager.getFormattedScoresByDifficulty("NORMAL", "Normal");
        var hardScores = manager.getFormattedScoresByDifficulty("NORMAL", "Hard");
        
        assertEquals(1, easyScores.size());
        assertEquals(1, normalScores.size());
        assertEquals(1, hardScores.size());
    }
    
    @Test
    void testGetFormattedScores_SameScore() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.clearScores("NORMAL");
        
        // 동점자들 추가
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        manager.addScore("Player2", 1000, "Normal", "NORMAL");
        manager.addScore("Player3", 800, "Normal", "NORMAL");
        
        var scores = manager.getFormattedScores("NORMAL");
        assertTrue(scores.get(0).startsWith("1. "));
        assertTrue(scores.get(1).startsWith("1. ")); // 동점이면 같은 순위
        assertTrue(scores.get(2).startsWith("3. ")); // 다음 순위는 3위
        
        assertEquals(1000, manager.getHighScore("NORMAL"));
    }

    @Test
    void testGetRank() {
        ScoreManager manager = ScoreManager.getInstance();
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        
        int rank = manager.getRank(500, "NORMAL");
        assertTrue(rank >= 1);
    }

    @Test
    void testGetRankByDifficulty() {
        ScoreManager manager = ScoreManager.getInstance();
        
        manager.addScore("Player1", 1000, "Normal", "NORMAL");
        
        int rank = manager.getRankByDifficulty(500, "NORMAL", "Normal");
        assertTrue(rank >= 1);
    }
}

