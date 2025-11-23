package tetris.network;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

/**
 * GameStateData 클래스의 단위 테스트
 */
class GameStateDataTest {
    
    @Test
    void testConstructorAndGetters() {
        int[][] board = new int[20][10];
        int[][] itemBoard = new int[20][10];
        board[0][0] = 1;
        itemBoard[0][0] = 2;
        
        int[][] currentShape = {{1, 1}, {1, 1}};
        int[][] nextShape = {{1, 1, 1, 1}};
        List<Integer> emptyCols = Arrays.asList(3, 5, 7);
        
        GameStateData data = new GameStateData(
            board, itemBoard, 1000, 5, 10, false,
            currentShape, 3, 4, 1,
            nextShape, 2, 3, emptyCols
        );
        
        assertEquals(1, data.getBoard()[0][0]);
        assertEquals(2, data.getItemBoard()[0][0]);
        assertEquals(1000, data.getScore());
        assertEquals(5, data.getLevel());
        assertEquals(10, data.getLinesCleared());
        assertFalse(data.isGameOver());
        assertEquals(3, data.getCurrentPieceX());
        assertEquals(4, data.getCurrentPieceY());
        assertEquals(1, data.getCurrentPieceType());
        assertEquals(2, data.getNextPieceType());
        assertEquals(3, data.getIncomingAttackLines());
        assertEquals(emptyCols, data.getIncomingAttackEmptyCols());
        assertTrue(data.getTimestamp() > 0);
    }
    
    @Test
    void testGameOver() {
        GameStateData gameOverData = new GameStateData(
            new int[20][10], new int[20][10], 5000, 10, 50, true,
            new int[0][0], 0, 0, 0,
            new int[0][0], 0, 0, Arrays.asList()
        );
        
        assertTrue(gameOverData.isGameOver());
    }
    
    @Test
    void testEmptyAttacks() {
        GameStateData data = new GameStateData(
            new int[20][10], new int[20][10], 100, 1, 0, false,
            new int[2][2], 0, 0, 0,
            new int[1][4], 0, 0, Arrays.asList()
        );
        
        assertEquals(0, data.getIncomingAttackLines());
        assertTrue(data.getIncomingAttackEmptyCols().isEmpty());
    }
    
    @Test
    void testMultipleAttacks() {
        List<Integer> emptyCols = Arrays.asList(1, 2, 3, 4, 5);
        
        GameStateData data = new GameStateData(
            new int[20][10], new int[20][10], 2000, 7, 25, false,
            new int[3][3], 2, 5, 3,
            new int[2][4], 4, 5, emptyCols
        );
        
        assertEquals(5, data.getIncomingAttackLines());
        assertEquals(5, data.getIncomingAttackEmptyCols().size());
        assertEquals(1, data.getIncomingAttackEmptyCols().get(0));
        assertEquals(5, data.getIncomingAttackEmptyCols().get(4));
    }
    
    @Test
    void testBoardDimensions() {
        int[][] board = new int[20][10];
        int[][] itemBoard = new int[20][10];
        
        GameStateData data = new GameStateData(
            board, itemBoard, 0, 1, 0, false,
            new int[0][0], 0, 0, 0,
            new int[0][0], 0, 0, Arrays.asList()
        );
        
        assertEquals(20, data.getBoard().length);
        assertEquals(10, data.getBoard()[0].length);
        assertEquals(20, data.getItemBoard().length);
        assertEquals(10, data.getItemBoard()[0].length);
    }
    
    @Test
    void testPieceShapes() {
        int[][] currentShape = {{1, 1}, {1, 0}};
        int[][] nextShape = {{1, 1, 1}, {0, 1, 0}};
        
        GameStateData data = new GameStateData(
            new int[20][10], new int[20][10], 500, 3, 5, false,
            currentShape, 5, 10, 2,
            nextShape, 5, 0, Arrays.asList()
        );
        
        assertArrayEquals(currentShape, data.getCurrentPieceShape());
        assertArrayEquals(nextShape, data.getNextPieceShape());
    }
    
    @Test
    void testTimestampIsRecent() {
        long before = System.currentTimeMillis();
        
        GameStateData data = new GameStateData(
            new int[20][10], new int[20][10], 0, 1, 0, false,
            new int[0][0], 0, 0, 0,
            new int[0][0], 0, 0, Arrays.asList()
        );
        
        long after = System.currentTimeMillis();
        
        assertTrue(data.getTimestamp() >= before);
        assertTrue(data.getTimestamp() <= after);
    }
}
