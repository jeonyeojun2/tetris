package tetris.ui.services;

import tetris.data.ScoreManager;

import java.util.List;

/**
 * 점수 검증 및 처리를 담당하는 서비스 클래스
 * UI 컨트롤러에서 비즈니스 로직을 분리하여 테스트 가능하게 만듦
 */
public class ScoreValidationService {

    private final ScoreManager scoreManager;

    public ScoreValidationService() {
        this.scoreManager = ScoreManager.getInstance();
    }

    // 테스트를 위한 생성자
    public ScoreValidationService(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    /**
     * 점수가 상위 10위 안에 들어갈 수 있는지 확인
     * 
     * @param score 확인할 점수
     * @param gameMode 게임 모드
     * @param difficulty 난이도
     * @return 저장 가능하면 true
     */
    public boolean canSaveScore(int score, String gameMode, String difficulty) {
        List<String> scores = getScoresByMode(gameMode, difficulty);

        // 10개 미만이면 저장 가능
        if (scores.isEmpty() || scores.size() < 10) {
            return true;
        }

        // 10개 이상이면 마지막 점수와 비교
        String lastScoreEntry = scores.get(scores.size() - 1);
        Integer lastScore = parseScoreFromEntry(lastScoreEntry);
        
        if (lastScore == null) {
            return true; // 파싱 실패 시 일단 저장 가능하도록
        }
        
        return score >= lastScore;
    }

    /**
     * 게임 모드에 따라 적절한 점수 목록을 반환
     */
    private List<String> getScoresByMode(String gameMode, String difficulty) {
        if ("NORMAL".equals(gameMode)) {
            return scoreManager.getFormattedScoresByDifficulty(gameMode, difficulty);
        } else {
            return scoreManager.getFormattedScores(gameMode);
        }
    }

    /**
     * 점수 문자열에서 점수 값을 추출
     * 형식: "순위. 이름 - 점수점 (날짜)"
     * 
     * @param scoreEntry 점수 문자열
     * @return 파싱된 점수, 실패 시 null
     */
    public Integer parseScoreFromEntry(String scoreEntry) {
        try {
            int dashIndex = scoreEntry.indexOf(" - ");
            int pointIndex = scoreEntry.indexOf("점", dashIndex);
            
            if (dashIndex > 0 && pointIndex > dashIndex) {
                String scoreStr = scoreEntry.substring(dashIndex + 3, pointIndex).trim();
                return Integer.parseInt(scoreStr);
            }
        } catch (Exception e) {
            System.err.println("점수 파싱 오류: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * 플레이어 이름이 유효한지 검증
     * 
     * @param playerName 플레이어 이름
     * @return 유효하면 true
     */
    public boolean isValidPlayerName(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            return false;
        }
        
        // 이름 길이 제한 (예: 1-20자)
        String trimmed = playerName.trim();
        return trimmed.length() >= 1 && trimmed.length() <= 20;
    }

    /**
     * 점수를 저장하고 저장된 순위를 반환
     * 
     * @param playerName 플레이어 이름
     * @param score 점수
     * @param gameMode 게임 모드
     * @param difficulty 난이도
     * @return 저장된 순위 (1-10), 저장 실패 시 -1
     */
    public int saveScoreAndGetRank(String playerName, int score, String gameMode, String difficulty) {
        if (!isValidPlayerName(playerName)) {
            return -1;
        }

        if ("NORMAL".equals(gameMode)) {
            scoreManager.addScore(playerName.trim(), score, gameMode, difficulty);
            return scoreManager.getRankByDifficulty(score, gameMode, difficulty);
        } else {
            scoreManager.addScore(playerName.trim(), score, gameMode, "");
            return scoreManager.getRank(score, gameMode);
        }
    }

    /**
     * 특정 순위의 색상 클래스를 반환 (UI 스타일링용)
     * 
     * @param rank 순위 (1-10)
     * @return CSS 클래스 이름
     */
    public String getRankColorClass(int rank) {
        if (rank == 1) {
            return "rank-gold";
        } else if (rank == 2) {
            return "rank-silver";
        } else if (rank == 3) {
            return "rank-bronze";
        } else if (rank >= 1 && rank <= 10) {
            return "rank-top10";
        }
        return "rank-normal";
    }
}
