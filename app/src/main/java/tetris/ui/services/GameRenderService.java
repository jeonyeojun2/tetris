package tetris.ui.services;

import tetris.game.GameBoard;
import tetris.game.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * 게임 렌더링 관련 비즈니스 로직 서비스
 * JavaFX에 의존하지 않는 순수 계산 로직
 */
public class GameRenderService {
    
    /**
     * 화면 크기에 따른 블록 크기 계산
     */
    public int calculateBlockSize(String screenSize) {
        switch (screenSize) {
            case "작게":
                return 20;
            case "중간":
                return 25;
            case "크게":
                return 30;
            default:
                return 25;
        }
    }
    
    /**
     * 레벨에 따른 낙하 속도 계산 (나노초 단위)
     */
    public long calculateFallSpeed(int level) {
        return (long) (1_000_000_000 * Math.pow(0.9, level - 1));
    }
    
    /**
     * 캔버스 크기 계산
     */
    public int[] calculateCanvasSize(String screenSize) {
        int blockSize = calculateBlockSize(screenSize);
        int width = GameBoard.BOARD_WIDTH * blockSize;
        int height = GameBoard.BOARD_HEIGHT * blockSize;
        return new int[]{width, height};
    }
    
    /**
     * 다음 블록 캔버스 크기 계산 (4x4 그리드)
     */
    public int[] calculateNextPieceCanvasSize(String screenSize) {
        int blockSize = calculateBlockSize(screenSize);
        int size = 4 * blockSize;
        return new int[]{size, size};
    }
    
    /**
     * 게임 보드에서 가득 찬 줄 찾기
     */
    public List<Integer> findFullLines(int[][] board) {
        List<Integer> fullLines = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            boolean isFull = true;
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                fullLines.add(row);
            }
        }
        return fullLines;
    }
    
    /**
     * 블록 위치가 보드 내부인지 확인
     */
    public boolean isPositionInBounds(int row, int col) {
        return row >= 0 && row < GameBoard.BOARD_HEIGHT && 
               col >= 0 && col < GameBoard.BOARD_WIDTH;
    }
    
    /**
     * 피스 모양 배열에서 실제 블록이 있는 셀 개수 계산
     */
    public int countPieceBlocks(int[][] shape) {
        int count = 0;
        for (int[] row : shape) {
            for (int cell : row) {
                if (cell != 0) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * 아이템이 있는지 확인
     */
    public boolean hasItem(ItemType itemType) {
        return itemType != null && itemType != ItemType.NONE;
    }
    
    /**
     * 특정 행이 클리어 애니메이션 대상인지 확인
     */
    public boolean isRowBeingCleared(int row, List<Integer> clearingRows) {
        return clearingRows != null && clearingRows.contains(row);
    }
    
    /**
     * 애니메이션 진행률 계산 (0.0 ~ 1.0)
     */
    public double calculateAnimationProgress(long elapsedNano, long durationNano) {
        if (durationNano <= 0) return 1.0;
        double progress = (double) elapsedNano / durationNano;
        return Math.min(1.0, Math.max(0.0, progress));
    }
    
    /**
     * 점수를 포맷팅된 문자열로 변환
     */
    public String formatScore(int score) {
        return String.format("%,d", score);
    }
    
    /**
     * 레벨 표시 문자열 생성
     */
    public String formatLevel(int level) {
        return "Level " + level;
    }
    
    /**
     * 삭제된 줄 수 표시 문자열 생성
     */
    public String formatLines(int lines) {
        return lines + " Lines";
    }
    
    /**
     * 시간을 MM:SS 형식으로 포맷팅
     */
    public String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    /**
     * 남은 시간을 초 단위로 계산
     */
    public long calculateRemainingSeconds(long remainingNano) {
        return Math.max(0, remainingNano / 1_000_000_000);
    }
    
    /**
     * 블록 심볼 선택 (색약 모드용)
     */
    public String getBlockSymbol(int pieceType) {
        String[] symbols = {
            " ", // 0 - 빈칸
            "O", // 1 - I
            "●", // 2 - O
            "★", // 3 - T
            "▲", // 4 - S
            "■", // 5 - Z
            "◆", // 6 - J
            "◇", // 7 - L
            "▼", // 8 - WEIGHT
            "✸"  // 9 - BOMB
        };
        if (pieceType >= 0 && pieceType < symbols.length) {
            return symbols[pieceType];
        }
        return " ";
    }
    
    /**
     * 색상 인덱스 유효성 검사
     */
    public boolean isValidColorIndex(int colorIndex) {
        return colorIndex >= 0 && colorIndex <= 9;
    }
}
