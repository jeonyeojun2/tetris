package tetris.network.services;

/**
 * 네트워크 연결 상태 및 안정성 관리 서비스
 */
public class NetworkConnectionService {

    private static final int DEFAULT_MAX_CONSECUTIVE_ERRORS = 5;
    private static final long DEFAULT_TIMEOUT_NANOS = 10_000_000_000L; // 10초

    /**
     * 연속 에러 횟수가 임계값을 초과했는지 확인
     * @param consecutiveErrors 현재 연속 에러 횟수
     * @param maxErrors 최대 허용 에러 횟수
     * @return 임계값 초과 여부
     */
    public boolean hasExceededMaxErrors(int consecutiveErrors, int maxErrors) {
        return consecutiveErrors >= maxErrors;
    }

    /**
     * 기본 임계값으로 연속 에러 확인
     */
    public boolean hasExceededMaxErrors(int consecutiveErrors) {
        return hasExceededMaxErrors(consecutiveErrors, DEFAULT_MAX_CONSECUTIVE_ERRORS);
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
            return false; // 활동이 없었으면 타임아웃 아님
        }
        
        if (timeoutNanos <= 0) {
            return false; // 타임아웃 설정이 비활성화
        }
        
        long elapsedNanos = currentTimeNanos - lastActivityTimeNanos;
        return elapsedNanos > timeoutNanos;
    }

    /**
     * 기본 타임아웃으로 연결 확인
     */
    public boolean isConnectionTimeout(long lastActivityTimeNanos, long currentTimeNanos) {
        return isConnectionTimeout(lastActivityTimeNanos, currentTimeNanos, DEFAULT_TIMEOUT_NANOS);
    }

    /**
     * 연결이 활성 상태인지 확인
     * @param isRunning 실행 중 플래그
     * @param socketClosed 소켓 닫힘 여부
     * @return 연결 활성 상태
     */
    public boolean isConnectionActive(boolean isRunning, boolean socketClosed) {
        return isRunning && !socketClosed;
    }

    /**
     * 재시도 가능한 IOException인지 판단
     * @param exceptionMessage 예외 메시지
     * @return 재시도 가능 여부
     */
    public boolean isRetriableIOException(String exceptionMessage) {
        if (exceptionMessage == null) {
            return false;
        }
        
        // 일시적인 네트워크 문제로 재시도 가능한 경우
        String lowerMsg = exceptionMessage.toLowerCase();
        return lowerMsg.contains("connection reset") ||
               lowerMsg.contains("broken pipe") ||
               lowerMsg.contains("network is unreachable") ||
               lowerMsg.contains("timeout");
    }

    /**
     * 심각한 에러인지 판단 (ClassNotFoundException 등)
     * @param exceptionClass 예외 클래스
     * @return 심각한 에러 여부
     */
    public boolean isCriticalError(Class<? extends Exception> exceptionClass) {
        return ClassNotFoundException.class.isAssignableFrom(exceptionClass) ||
               SecurityException.class.isAssignableFrom(exceptionClass);
    }

    /**
     * 포트 번호 유효성 검증
     * @param port 포트 번호
     * @return 유효한 포트인지 여부
     */
    public boolean isValidPort(int port) {
        return port > 0 && port <= 65535;
    }

    /**
     * IP 주소 형식 검증 (간단한 검증)
     * @param ipAddress IP 주소 문자열
     * @return 유효한 IP 주소인지 여부
     */
    public boolean isValidIPAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            return false;
        }

        // localhost 허용
        if (ipAddress.equalsIgnoreCase("localhost")) {
            return true;
        }

        // IPv4 간단 검증
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        try {
            for (String part : parts) {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 연결 정보 유효성 검증
     * @param ipAddress IP 주소
     * @param port 포트 번호
     * @return 유효성 검증 결과
     */
    public ConnectionValidationResult validateConnection(String ipAddress, int port) {
        if (!isValidIPAddress(ipAddress)) {
            return new ConnectionValidationResult(false, "유효하지 않은 IP 주소입니다.");
        }
        
        if (!isValidPort(port)) {
            return new ConnectionValidationResult(false, "포트 번호는 1-65535 사이여야 합니다.");
        }
        
        return new ConnectionValidationResult(true, "연결 정보가 유효합니다.");
    }

    /**
     * 연결 검증 결과
     */
    public static class ConnectionValidationResult {
        private final boolean valid;
        private final String message;

        public ConnectionValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 재연결 대기 시간 계산 (exponential backoff)
     * @param attemptCount 재시도 횟수
     * @param baseDelayMillis 기본 대기 시간 (밀리초)
     * @return 대기 시간 (밀리초)
     */
    public long calculateReconnectDelay(int attemptCount, long baseDelayMillis) {
        if (attemptCount <= 0) {
            return baseDelayMillis;
        }
        
        // 최대 10번까지만 지수 증가
        int cappedAttempt = Math.min(attemptCount, 10);
        long delay = baseDelayMillis * (1L << (cappedAttempt - 1)); // 2^(n-1)
        
        // 최대 30초로 제한
        return Math.min(delay, 30000);
    }

    /**
     * 기본 대기 시간(1초)으로 재연결 대기 시간 계산
     */
    public long calculateReconnectDelay(int attemptCount) {
        return calculateReconnectDelay(attemptCount, 1000);
    }
}
