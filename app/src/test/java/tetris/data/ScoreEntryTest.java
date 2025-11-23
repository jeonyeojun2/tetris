package tetris.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class ScoreEntryTest {

    @Test
    void testScoreEntryCreation() {
        ScoreEntry entry = new ScoreEntry("Player1", 1000, "Normal", "NORMAL");
        
        assertEquals("Player1", entry.getPlayerName());
        assertEquals(1000, entry.getScore());
        assertEquals("Normal", entry.getDifficulty());
        assertEquals("NORMAL", entry.getGameMode());
        assertNotNull(entry.getDate());
    }

    @Test
    void testGetFormattedDate() {
        ScoreEntry entry = new ScoreEntry("Player1", 1000, "Normal", "NORMAL");
        String formatted = entry.getFormattedDate();
        
        assertNotNull(formatted);
        assertTrue(formatted.length() > 0);
    }

    @Test
    void testCompareTo() {
        ScoreEntry entry1 = new ScoreEntry("Player1", 1000, "Normal", "NORMAL");
        ScoreEntry entry2 = new ScoreEntry("Player2", 500, "Normal", "NORMAL");
        ScoreEntry entry3 = new ScoreEntry("Player3", 1000, "Normal", "NORMAL");
        
        assertTrue(entry2.compareTo(entry1) > 0); // entry2의 점수가 더 낮으므로 compareTo는 양수
        assertEquals(0, entry1.compareTo(entry3)); // 같은 점수
    }

    @Test
    void testToString() {
        ScoreEntry entry = new ScoreEntry("Player1", 1234, "Normal", "NORMAL");
        String str = entry.toString();
        
        assertTrue(str.contains("Player1"));
        assertTrue(str.contains("1234"));
    }
    
    @Test
    void testCompareToWithDifferentScores() {
        ScoreEntry high = new ScoreEntry("High", 2000, "Normal", "NORMAL");
        ScoreEntry low = new ScoreEntry("Low", 100, "Normal", "NORMAL");
        
        assertTrue(high.compareTo(low) < 0); // 높은 점수가 더 앞
        assertTrue(low.compareTo(high) > 0);
    }
    
    @Test
    void testGetDateReturnsNonNull() {
        ScoreEntry entry = new ScoreEntry("Test", 500, "Easy", "ITEM");
        assertNotNull(entry.getDate());
        assertTrue(entry.getDate() instanceof LocalDateTime);
    }
    
    @Test
    void testDifferentDifficulties() {
        ScoreEntry easy = new ScoreEntry("Player", 100, "Easy", "NORMAL");
        ScoreEntry normal = new ScoreEntry("Player", 100, "Normal", "NORMAL");
        ScoreEntry hard = new ScoreEntry("Player", 100, "Hard", "NORMAL");
        
        assertEquals("Easy", easy.getDifficulty());
        assertEquals("Normal", normal.getDifficulty());
        assertEquals("Hard", hard.getDifficulty());
    }
    
    @Test
    void testDifferentGameModes() {
        ScoreEntry normalMode = new ScoreEntry("Player", 100, "Normal", "NORMAL");
        ScoreEntry itemMode = new ScoreEntry("Player", 100, "Normal", "ITEM");
        
        assertEquals("NORMAL", normalMode.getGameMode());
        assertEquals("ITEM", itemMode.getGameMode());
    }
    
    @Test
    void testEqualsScoreComparison() {
        ScoreEntry entry1 = new ScoreEntry("Player1", 1500, "Normal", "NORMAL");
        ScoreEntry entry2 = new ScoreEntry("Player2", 1500, "Normal", "NORMAL");
        
        assertEquals(0, entry1.compareTo(entry2));
    }
}
