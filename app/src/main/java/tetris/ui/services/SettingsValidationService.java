package tetris.ui.services;

import java.util.*;

/**
 * 설정 검증 서비스
 * - 키 중복 검사
 * - 설정 값 유효성 검증
 */
public class SettingsValidationService {
    
    /**
     * Player1과 Player2의 키 설정이 중복되는지 검사
     * @param p1Keys Player1 키 설정 맵 (keyName -> keyValue)
     * @param p2Keys Player2 키 설정 맵 (keyName -> keyValue)
     * @return 중복된 키 목록 (비어있으면 중복 없음)
     */
    public List<String> findDuplicateKeys(Map<String, String> p1Keys, Map<String, String> p2Keys) {
        List<String> duplicates = new ArrayList<>();
        
        for (Map.Entry<String, String> p1Entry : p1Keys.entrySet()) {
            String p1Key = p1Entry.getValue();
            if (p1Key == null || p1Key.trim().isEmpty()) {
                continue;
            }
            
            for (Map.Entry<String, String> p2Entry : p2Keys.entrySet()) {
                String p2Key = p2Entry.getValue();
                if (p2Key != null && p1Key.equalsIgnoreCase(p2Key)) {
                    duplicates.add(p1Key);
                    break;
                }
            }
        }
        
        return duplicates;
    }
    
    /**
     * Player1 또는 Player2 내에서 키가 중복되는지 검사
     * @param keys 키 설정 맵
     * @return 중복된 키 목록
     */
    public List<String> findDuplicateWithinPlayer(Map<String, String> keys) {
        List<String> duplicates = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        
        for (Map.Entry<String, String> entry : keys.entrySet()) {
            String key = entry.getValue();
            if (key == null || key.trim().isEmpty()) {
                continue;
            }
            
            String upperKey = key.toUpperCase();
            if (seen.contains(upperKey)) {
                if (!duplicates.contains(upperKey)) {
                    duplicates.add(upperKey);
                }
            } else {
                seen.add(upperKey);
            }
        }
        
        return duplicates;
    }
    
    /**
     * 키 설정이 유효한지 검증
     * @param key 키 값
     * @return 유효하면 true
     */
    public boolean isValidKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        
        String upper = key.toUpperCase().trim();
        
        // 특수 키는 길이 제한 없음
        List<String> specialKeys = Arrays.asList("SPACE", "ENTER", "UP", "DOWN", "LEFT", "RIGHT", 
                                                  "ESCAPE", "TAB", "SHIFT", "CONTROL", "ALT");
        if (specialKeys.contains(upper)) {
            return true;
        }
        
        // 일반 키는 한 글자만 허용
        return upper.length() == 1 && Character.isLetterOrDigit(upper.charAt(0));
    }
    
    /**
     * 화면 크기 설정이 유효한지 검증
     * @param screenSize 화면 크기 값
     * @return 유효하면 true
     */
    public boolean isValidScreenSize(String screenSize) {
        if (screenSize == null) {
            return false;
        }
        List<String> validSizes = Arrays.asList("작게", "중간", "크게");
        return validSizes.contains(screenSize);
    }
    
    /**
     * 난이도 설정이 유효한지 검증
     * @param difficulty 난이도 값
     * @return 유효하면 true
     */
    public boolean isValidDifficulty(String difficulty) {
        if (difficulty == null) {
            return false;
        }
        List<String> validDifficulties = Arrays.asList("Easy", "Normal", "Hard");
        return validDifficulties.contains(difficulty);
    }
    
    /**
     * 블록 크기 계산
     * @param screenSize 화면 크기
     * @return 블록 크기 (픽셀)
     */
    public int calculateBlockSize(String screenSize) {
        if (screenSize == null) {
            return 25; // 기본값
        }
        switch (screenSize) {
            case "작게":
                return 20;
            case "크게":
                return 30;
            case "중간":
            default:
                return 25;
        }
    }
    
    /**
     * 화면 크기에 따른 캔버스 크기 계산
     * @param screenSize 화면 크기
     * @return [width, height] 배열
     */
    public int[] calculateCanvasSize(String screenSize) {
        if (screenSize == null) {
            return new int[]{600, 900}; // 기본값
        }
        switch (screenSize) {
            case "작게":
                return new int[]{480, 720};
            case "크게":
                return new int[]{720, 1080};
            case "중간":
            default:
                return new int[]{600, 900};
        }
    }
}
