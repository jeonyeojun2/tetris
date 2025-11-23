package tetris.ui.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameRenderService 테스트
 */
class GameRenderServiceTest {
    
    private GameRenderService service;
    
    @BeforeEach
    void setUp() {
        service = new GameRenderService();
    }
    
    @Test
    void testCalculateBlockSize_Small() {
        assertEquals(20, service.calculateBlockSize("작게"));
    }
    
    @Test
    void testCalculateBlockSize_Medium() {
        assertEquals(25, service.calculateBlockSize("중간"));
    }
    
    @Test
    void testCalculateBlockSize_Large() {
        assertEquals(30, service.calculateBlockSize("크게"));
    }
    
    @Test
    void testCalculateBlockSize_Default() {
        assertEquals(25, service.calculateBlockSize("invalid"));
        assertEquals(25, service.calculateBlockSize(null));
    }
    
    @Test
    void testCalculateFallSpeed_Level1() {
        long expected = 1_000_000_000L;
        assertEquals(expected, service.calculateFallSpeed(1));
    }
    
    @Test
    void testCalculateFallSpeed_Level2() {
        long expected = (long) (1_000_000_000 * 0.9);
        assertEquals(expected, service.calculateFallSpeed(2));
    }
    
    @Test
    void testCalculateFallSpeed_Level5() {
        long expected = (long) (1_000_000_000 * Math.pow(0.9, 4));
        assertEquals(expected, service.calculateFallSpeed(5));
    }
    
    @Test
    void testCalculateCanvasSize_Medium() {
        int[] size = service.calculateCanvasSize("중간");
        assertEquals(2, size.length);
        assertEquals(10 * 25, size[0]); // width
        assertEquals(20 * 25, size[1]); // height
    }
    
    @Test
    void testCalculateCanvasSize_Large() {
        int[] size = service.calculateCanvasSize("크게");
        assertEquals(10 * 30, size[0]);
        assertEquals(20 * 30, size[1]);
    }
    
    @Test
    void testCalculateNextPieceCanvasSize() {
        int[] size = service.calculateNextPieceCanvasSize("중간");
        assertEquals(4 * 25, size[0]);
        assertEquals(4 * 25, size[1]);
    }
    
    @Test
    void testFindFullLines_EmptyBoard() {
        int[][] board = new int[20][10];
        List<Integer> fullLines = service.findFullLines(board);
        assertTrue(fullLines.isEmpty());
    }
    
    @Test
    void testFindFullLines_OneFullLine() {
        int[][] board = new int[20][10];
        // 마지막 줄을 가득 채움
        for (int col = 0; col < 10; col++) {
            board[19][col] = 1;
        }
        List<Integer> fullLines = service.findFullLines(board);
        assertEquals(1, fullLines.size());
        assertEquals(19, fullLines.get(0));
    }
    
    @Test
    void testFindFullLines_MultipleFullLines() {
        int[][] board = new int[20][10];
        // 여러 줄을 가득 채움
        for (int col = 0; col < 10; col++) {
            board[18][col] = 1;
            board[19][col] = 2;
        }
        List<Integer> fullLines = service.findFullLines(board);
        assertEquals(2, fullLines.size());
        assertTrue(fullLines.contains(18));
        assertTrue(fullLines.contains(19));
    }
    
    @Test
    void testIsPositionInBounds_Valid() {
        assertTrue(service.isPositionInBounds(0, 0));
        assertTrue(service.isPositionInBounds(10, 5));
        assertTrue(service.isPositionInBounds(19, 9));
    }
    
    @Test
    void testIsPositionInBounds_Invalid() {
        assertFalse(service.isPositionInBounds(-1, 0));
        assertFalse(service.isPositionInBounds(0, -1));
        assertFalse(service.isPositionInBounds(20, 0));
        assertFalse(service.isPositionInBounds(0, 10));
    }
    
    @Test
    void testCountPieceBlocks_EmptyShape() {
        int[][] shape = {{0, 0}, {0, 0}};
        assertEquals(0, service.countPieceBlocks(shape));
    }
    
    @Test
    void testCountPieceBlocks_TwoBlocks() {
        int[][] shape = {{1, 0}, {1, 0}};
        assertEquals(2, service.countPieceBlocks(shape));
    }
    
    @Test
    void testCountPieceBlocks_FourBlocks() {
        int[][] shape = {{1, 1}, {1, 1}};
        assertEquals(4, service.countPieceBlocks(shape));
    }
    
    @Test
    void testHasItem_Null() {
        assertFalse(service.hasItem(null));
    }
    
    @Test
    void testIsRowBeingCleared_Null() {
        assertFalse(service.isRowBeingCleared(0, null));
    }
    
    @Test
    void testIsRowBeingCleared_NotInList() {
        List<Integer> clearingRows = Arrays.asList(5, 10, 15);
        assertFalse(service.isRowBeingCleared(0, clearingRows));
    }
    
    @Test
    void testIsRowBeingCleared_InList() {
        List<Integer> clearingRows = Arrays.asList(5, 10, 15);
        assertTrue(service.isRowBeingCleared(10, clearingRows));
    }
    
    @Test
    void testCalculateAnimationProgress_Zero() {
        assertEquals(0.0, service.calculateAnimationProgress(0, 1000));
    }
    
    @Test
    void testCalculateAnimationProgress_Half() {
        assertEquals(0.5, service.calculateAnimationProgress(500, 1000), 0.001);
    }
    
    @Test
    void testCalculateAnimationProgress_Complete() {
        assertEquals(1.0, service.calculateAnimationProgress(1000, 1000));
    }
    
    @Test
    void testCalculateAnimationProgress_OverComplete() {
        assertEquals(1.0, service.calculateAnimationProgress(1500, 1000));
    }
    
    @Test
    void testCalculateAnimationProgress_ZeroDuration() {
        assertEquals(1.0, service.calculateAnimationProgress(100, 0));
    }
    
    @Test
    void testFormatScore() {
        assertEquals("0", service.formatScore(0));
        assertEquals("1,234", service.formatScore(1234));
        assertEquals("1,000,000", service.formatScore(1000000));
    }
    
    @Test
    void testFormatLevel() {
        assertEquals("Level 1", service.formatLevel(1));
        assertEquals("Level 10", service.formatLevel(10));
    }
    
    @Test
    void testFormatLines() {
        assertEquals("0 Lines", service.formatLines(0));
        assertEquals("42 Lines", service.formatLines(42));
    }
    
    @Test
    void testFormatTime() {
        assertEquals("00:00", service.formatTime(0));
        assertEquals("00:30", service.formatTime(30));
        assertEquals("01:00", service.formatTime(60));
        assertEquals("02:05", service.formatTime(125));
        assertEquals("10:45", service.formatTime(645));
    }
    
    @Test
    void testCalculateRemainingSeconds() {
        assertEquals(0, service.calculateRemainingSeconds(0));
        assertEquals(1, service.calculateRemainingSeconds(1_000_000_000));
        assertEquals(30, service.calculateRemainingSeconds(30_000_000_000L));
        assertEquals(0, service.calculateRemainingSeconds(-1000));
    }
    
    @Test
    void testGetBlockSymbol_Valid() {
        assertEquals(" ", service.getBlockSymbol(0));
        assertEquals("O", service.getBlockSymbol(1));
        assertEquals("●", service.getBlockSymbol(2));
        assertEquals("★", service.getBlockSymbol(3));
        assertEquals("▼", service.getBlockSymbol(8));
        assertEquals("✸", service.getBlockSymbol(9));
    }
    
    @Test
    void testGetBlockSymbol_Invalid() {
        assertEquals(" ", service.getBlockSymbol(-1));
        assertEquals(" ", service.getBlockSymbol(10));
    }
    
    @Test
    void testIsValidColorIndex() {
        assertTrue(service.isValidColorIndex(0));
        assertTrue(service.isValidColorIndex(5));
        assertTrue(service.isValidColorIndex(9));
        assertFalse(service.isValidColorIndex(-1));
        assertFalse(service.isValidColorIndex(10));
    }
}
