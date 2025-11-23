package tetris.ui.services;

/**
 * 게임 세션 관리 서비스
 * - 게임 상태 관리 (일시정지, 재개)
 * - 낙하 속도 계산
 * - 게임 진행 시간 관리
 */
public class GameSessionService {
    
    private boolean isPaused = false;
    private boolean isGameRunning = false;
    private long sessionStartTime = 0;
    private long pausedTime = 0;
    private long totalPausedDuration = 0;
    
    /**
     * 게임 세션 시작
     */
    public void startSession() {
        isGameRunning = true;
        isPaused = false;
        sessionStartTime = System.nanoTime();
        totalPausedDuration = 0;
    }
    
    /**
     * 게임 일시정지
     */
    public void pauseSession() {
        if (isGameRunning && !isPaused) {
            isPaused = true;
            pausedTime = System.nanoTime();
        }
    }
    
    /**
     * 게임 재개
     */
    public void resumeSession() {
        if (isGameRunning && isPaused) {
            isPaused = false;
            if (pausedTime > 0) {
                totalPausedDuration += (System.nanoTime() - pausedTime);
                pausedTime = 0;
            }
        }
    }
    
    /**
     * 게임 종료
     */
    public void endSession() {
        isGameRunning = false;
        isPaused = false;
    }
    
    /**
     * 레벨에 따른 낙하 속도 계산 (나노초 단위)
     * @param level 현재 레벨
     * @return 낙하 속도 (나노초)
     */
    public long calculateFallSpeed(int level) {
        if (level < 1) {
            level = 1;
        }
        // 레벨이 올라갈수록 0.9배씩 빨라짐
        return (long) (1_000_000_000 * Math.pow(0.9, level - 1));
    }
    
    /**
     * 실제 게임 진행 시간 계산 (일시정지 시간 제외)
     * @return 게임 진행 시간 (나노초)
     */
    public long getElapsedGameTime() {
        if (!isGameRunning) {
            return 0;
        }
        long currentTime = System.nanoTime();
        long elapsed = currentTime - sessionStartTime - totalPausedDuration;
        
        // 현재 일시정지 중이라면 현재 일시정지 시간도 제외
        if (isPaused && pausedTime > 0) {
            elapsed -= (currentTime - pausedTime);
        }
        
        return elapsed;
    }
    
    /**
     * 난이도에 따른 초기 낙하 속도 계산
     * @param difficulty 난이도 ("Easy", "Normal", "Hard")
     * @return 초기 낙하 속도 (나노초)
     */
    public long getInitialFallSpeedByDifficulty(String difficulty) {
        if (difficulty == null) {
            return 1_000_000_000L; // 기본값 Normal
        }
        switch (difficulty) {
            case "Easy":
                return 1_500_000_000L; // 1.5초
            case "Hard":
                return 700_000_000L;   // 0.7초
            case "Normal":
            default:
                return 1_000_000_000L; // 1초
        }
    }
    
    // Getters
    public boolean isPaused() {
        return isPaused;
    }
    
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    public long getSessionStartTime() {
        return sessionStartTime;
    }
    
    public long getTotalPausedDuration() {
        return totalPausedDuration;
    }
}
