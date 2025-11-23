package tetris.game;

/**
 * P2P 게임의 핵심 비즈니스 로직을 담당하는 클래스
 * UI와 독립적으로 테스트 가능하도록 설계됨
 */
public class PVPGameLogic {
    
    /**
     * 시간제한 모드에서 승자 결정
     * @param myScore 내 점수
     * @param opponentScore 상대방 점수
     * @return "WIN", "LOSE", "DRAW"
     */
    public String determineWinner(int myScore, int opponentScore) {
        if (myScore > opponentScore) {
            return "WIN";
        } else if (myScore < opponentScore) {
            return "LOSE";
        } else {
            return "DRAW";
        }
    }
    
    /**
     * 시간 포맷 (초 -> MM:SS)
     * @param seconds 초 단위 시간
     * @return "MM:SS" 형식의 문자열
     */
    public String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
    
    /**
     * 남은 시간 계산
     * @param gameStartTimeNanos 게임 시작 시각 (나노초)
     * @param currentTimeNanos 현재 시각 (나노초)
     * @param gameDurationSeconds 게임 총 시간 (초)
     * @return 남은 시간 (초), 음수면 시간 초과
     */
    public long calculateRemainingTime(long gameStartTimeNanos, long currentTimeNanos, long gameDurationSeconds) {
        long elapsedSeconds = (currentTimeNanos - gameStartTimeNanos) / 1_000_000_000L;
        return gameDurationSeconds - elapsedSeconds;
    }
    
    /**
     * 시간이 초과되었는지 확인
     * @param gameStartTimeNanos 게임 시작 시각 (나노초)
     * @param currentTimeNanos 현재 시각 (나노초)
     * @param gameDurationSeconds 게임 총 시간 (초)
     * @return 시간 초과 여부
     */
    public boolean isTimeUp(long gameStartTimeNanos, long currentTimeNanos, long gameDurationSeconds) {
        return calculateRemainingTime(gameStartTimeNanos, currentTimeNanos, gameDurationSeconds) <= 0;
    }
    
    /**
     * 공격 라인 수 계산
     * @param linesCleared 삭제한 줄 수
     * @return 공격 가능 여부 (2줄 이상이면 true)
     */
    public boolean canSendAttack(int linesCleared) {
        return linesCleared >= 2;
    }
    
    /**
     * RTT에 따른 네트워크 상태 평가
     * @param rttMillis RTT (밀리초)
     * @return "GOOD", "WARNING", "CRITICAL"
     */
    public String evaluateNetworkStatus(long rttMillis) {
        if (rttMillis < 200) {
            return "GOOD";
        } else if (rttMillis < 500) {
            return "WARNING";
        } else {
            return "CRITICAL";
        }
    }
    
    /**
     * 연결 타임아웃 여부 확인
     * @param lastActivityTimeNanos 마지막 활동 시각 (나노초)
     * @param currentTimeNanos 현재 시각 (나노초)
     * @param timeoutNanos 타임아웃 시간 (나노초)
     * @return 타임아웃 여부
     */
    public boolean isConnectionTimeout(long lastActivityTimeNanos, long currentTimeNanos, long timeoutNanos) {
        if (lastActivityTimeNanos == 0) {
            return false;
        }
        return (currentTimeNanos - lastActivityTimeNanos) > timeoutNanos;
    }
}
