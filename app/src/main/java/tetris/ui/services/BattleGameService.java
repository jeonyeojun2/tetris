package tetris.ui.services;

import java.util.ArrayList;
import java.util.List;

/**
 * 대전 게임 관련 비즈니스 로직 서비스
 * 점수 계산, 공격력 계산, 승리 조건 판정 등
 */
public class BattleGameService {
    
    /**
     * 삭제한 줄 수에 따른 공격력 계산
     */
    public int calculateAttackPower(int clearedLines) {
        switch (clearedLines) {
            case 1:
                return 0; // 1줄 삭제는 공격 없음
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 4; // 테트리스!
            default:
                return 0;
        }
    }
    
    /**
     * 공격 블록이 추가될 때 빈 열 선택
     * @param lastPlacedCol 마지막으로 블록이 배치된 열
     * @param boardWidth 보드 너비
     * @return 빈 열 인덱스
     */
    public int selectEmptyColumn(int lastPlacedCol, int boardWidth) {
        // 마지막 배치 위치를 빈 칸으로 사용
        if (lastPlacedCol >= 0 && lastPlacedCol < boardWidth) {
            return lastPlacedCol;
        }
        // 기본값: 중앙
        return boardWidth / 2;
    }
    
    /**
     * 공격 블록 줄 수 제한 확인
     */
    public int limitAttackLines(int attackLines, int maxLines) {
        return Math.min(attackLines, maxLines);
    }
    
    /**
     * 시간 제한 모드에서 승자 결정
     * @param score1 플레이어 1 점수
     * @param score2 플레이어 2 점수
     * @return "Player 1", "Player 2", 또는 "Draw"
     */
    public String determineWinnerByScore(int score1, int score2) {
        if (score1 > score2) {
            return "Player 1";
        } else if (score2 > score1) {
            return "Player 2";
        } else {
            return "Draw";
        }
    }
    
    /**
     * 게임 오버 상태 확인 (일반 모드)
     * @param engine1Running 플레이어 1 엔진 실행 상태
     * @param engine2Running 플레이어 2 엔진 실행 상태
     * @return 게임 오버 여부
     */
    public boolean isGameOver(boolean engine1Running, boolean engine2Running) {
        return !engine1Running || !engine2Running;
    }
    
    /**
     * 남은 시간 계산 (나노초 → 초)
     */
    public long calculateRemainingSeconds(long remainingNano) {
        return Math.max(0, remainingNano / 1_000_000_000);
    }
    
    /**
     * 시간 제한 도달 여부 확인
     */
    public boolean isTimeLimitReached(long remainingNano) {
        return remainingNano <= 0;
    }
    
    /**
     * 승리 메시지 생성
     */
    public String createVictoryMessage(String winner, String reason) {
        if ("Draw".equals(winner)) {
            return "무승부!";
        }
        return winner + " 승리! (" + reason + ")";
    }
    
    /**
     * 대기 중인 공격 수 표시 문자열 생성
     */
    public String formatPendingAttacks(int attackCount) {
        if (attackCount == 0) {
            return "";
        }
        return "↓ " + attackCount;
    }
    
    /**
     * 콤보 메시지 생성
     */
    public String createComboMessage(int comboCount) {
        if (comboCount <= 1) {
            return "";
        } else if (comboCount == 2) {
            return "Double!";
        } else if (comboCount == 3) {
            return "Triple!";
        } else if (comboCount >= 4) {
            return "Tetris!";
        }
        return "";
    }
    
    /**
     * 게임 모드 표시 문자열 생성
     */
    public String getGameModeDisplayName(String mode) {
        switch (mode) {
            case "NORMAL":
                return "일반 대전";
            case "ITEM":
                return "아이템 대전";
            case "TIME_LIMIT":
                return "시간제한 대전";
            default:
                return mode;
        }
    }
    
    /**
     * 플레이어 이름 생성
     */
    public String getPlayerName(int playerNumber) {
        return "Player " + playerNumber;
    }
    
    /**
     * 공격 블록 추가 가능 여부 확인
     */
    public boolean canAddAttackLines(int currentHeight, int attackLines, int maxHeight) {
        return currentHeight + attackLines <= maxHeight;
    }
    
    /**
     * 레벨 차이에 따른 핸디캡 계산
     */
    public double calculateHandicap(int level1, int level2) {
        int diff = Math.abs(level1 - level2);
        if (diff <= 1) {
            return 1.0; // 핸디캡 없음
        } else if (diff <= 3) {
            return 0.9; // 10% 감소
        } else {
            return 0.8; // 20% 감소
        }
    }
    
    /**
     * 연속 공격 보너스 계산
     */
    public int calculateComboBonus(int comboCount) {
        if (comboCount <= 1) {
            return 0;
        }
        return (comboCount - 1) * 50;
    }
    
    /**
     * 공격 우선순위 결정 (동시 공격 시)
     */
    public int determinePriority(int attackPower1, int attackPower2) {
        if (attackPower1 > attackPower2) {
            return 1; // 플레이어 1 우선
        } else if (attackPower2 > attackPower1) {
            return 2; // 플레이어 2 우선
        } else {
            return 0; // 동시 적용
        }
    }
    
    /**
     * 점수 차이 계산
     */
    public int calculateScoreDifference(int score1, int score2) {
        return Math.abs(score1 - score2);
    }
    
    /**
     * 역전 가능 여부 판단
     */
    public boolean isComebackPossible(int scoreDiff, long remainingSeconds) {
        // 남은 시간이 30초 이상이고 점수 차이가 5000점 이하면 역전 가능
        return remainingSeconds >= 30 && scoreDiff <= 5000;
    }
    
    /**
     * 게임 난이도 자동 조정
     */
    public double adjustDifficulty(int winStreak) {
        if (winStreak >= 5) {
            return 1.2; // 20% 어려워짐
        } else if (winStreak >= 3) {
            return 1.1; // 10% 어려워짐
        } else if (winStreak <= -3) {
            return 0.9; // 10% 쉬워짐
        }
        return 1.0; // 변화 없음
    }
    
    /**
     * 공격 효율성 계산 (공격한 줄 / 받은 공격)
     */
    public double calculateAttackEfficiency(int attacksSent, int attacksReceived) {
        if (attacksReceived == 0) {
            return attacksSent > 0 ? Double.POSITIVE_INFINITY : 0.0;
        }
        return (double) attacksSent / attacksReceived;
    }
    
    /**
     * MVP 결정 (여러 통계 기반)
     */
    public String determineMVP(int score1, int score2, int attacks1, int attacks2) {
        int totalPoints1 = score1 + (attacks1 * 100);
        int totalPoints2 = score2 + (attacks2 * 100);
        
        if (totalPoints1 > totalPoints2) {
            return "Player 1";
        } else if (totalPoints2 > totalPoints1) {
            return "Player 2";
        } else {
            return "Both";
        }
    }
}
