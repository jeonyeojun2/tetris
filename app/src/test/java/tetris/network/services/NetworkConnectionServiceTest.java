package tetris.network.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NetworkConnectionService 테스트
 */
class NetworkConnectionServiceTest {

    private NetworkConnectionService service;

    @BeforeEach
    void setUp() {
        service = new NetworkConnectionService();
    }

    // ========== 연속 에러 테스트 ==========

    @Test
    void testHasExceededMaxErrors_BelowThreshold() {
        assertFalse(service.hasExceededMaxErrors(3, 5));
    }

    @Test
    void testHasExceededMaxErrors_AtThreshold() {
        assertTrue(service.hasExceededMaxErrors(5, 5));
    }

    @Test
    void testHasExceededMaxErrors_AboveThreshold() {
        assertTrue(service.hasExceededMaxErrors(7, 5));
    }

    @Test
    void testHasExceededMaxErrors_DefaultThreshold() {
        assertFalse(service.hasExceededMaxErrors(4));
        assertTrue(service.hasExceededMaxErrors(5));
        assertTrue(service.hasExceededMaxErrors(10));
    }

    @Test
    void testHasExceededMaxErrors_Zero() {
        assertFalse(service.hasExceededMaxErrors(0, 5));
    }

    // ========== 연결 타임아웃 테스트 ==========

    @Test
    void testIsConnectionTimeout_NoTimeout() {
        long lastActivity = System.nanoTime();
        long current = lastActivity + 5_000_000_000L; // 5초 후
        long timeout = 10_000_000_000L; // 10초
        
        assertFalse(service.isConnectionTimeout(lastActivity, current, timeout));
    }

    @Test
    void testIsConnectionTimeout_Timeout() {
        long lastActivity = System.nanoTime();
        long current = lastActivity + 15_000_000_000L; // 15초 후
        long timeout = 10_000_000_000L; // 10초
        
        assertTrue(service.isConnectionTimeout(lastActivity, current, timeout));
    }

    @Test
    void testIsConnectionTimeout_NoActivity() {
        long current = System.nanoTime();
        long timeout = 10_000_000_000L;
        
        assertFalse(service.isConnectionTimeout(0, current, timeout));
    }

    @Test
    void testIsConnectionTimeout_DisabledTimeout() {
        long lastActivity = System.nanoTime();
        long current = lastActivity + 100_000_000_000L; // 100초 후
        
        assertFalse(service.isConnectionTimeout(lastActivity, current, 0));
        assertFalse(service.isConnectionTimeout(lastActivity, current, -1));
    }

    @Test
    void testIsConnectionTimeout_ExactThreshold() {
        long lastActivity = 1000_000_000_000L;
        long timeout = 10_000_000_000L;
        long current = lastActivity + timeout;
        
        assertFalse(service.isConnectionTimeout(lastActivity, current, timeout));
        
        current = lastActivity + timeout + 1;
        assertTrue(service.isConnectionTimeout(lastActivity, current, timeout));
    }

    @Test
    void testIsConnectionTimeout_DefaultTimeout() {
        long lastActivity = System.nanoTime();
        long current = lastActivity + 5_000_000_000L; // 5초
        
        assertFalse(service.isConnectionTimeout(lastActivity, current));
    }

    // ========== 연결 활성 상태 테스트 ==========

    @Test
    void testIsConnectionActive_Active() {
        assertTrue(service.isConnectionActive(true, false));
    }

    @Test
    void testIsConnectionActive_NotRunning() {
        assertFalse(service.isConnectionActive(false, false));
    }

    @Test
    void testIsConnectionActive_SocketClosed() {
        assertFalse(service.isConnectionActive(true, true));
    }

    @Test
    void testIsConnectionActive_Inactive() {
        assertFalse(service.isConnectionActive(false, true));
    }

    // ========== IOException 재시도 가능 여부 테스트 ==========

    @Test
    void testIsRetriableIOException_ConnectionReset() {
        assertTrue(service.isRetriableIOException("Connection reset by peer"));
    }

    @Test
    void testIsRetriableIOException_BrokenPipe() {
        assertTrue(service.isRetriableIOException("Broken pipe"));
    }

    @Test
    void testIsRetriableIOException_NetworkUnreachable() {
        assertTrue(service.isRetriableIOException("Network is unreachable"));
    }

    @Test
    void testIsRetriableIOException_Timeout() {
        assertTrue(service.isRetriableIOException("Connection timeout"));
    }

    @Test
    void testIsRetriableIOException_NonRetriable() {
        assertFalse(service.isRetriableIOException("Invalid format"));
        assertFalse(service.isRetriableIOException("Syntax error"));
    }

    @Test
    void testIsRetriableIOException_Null() {
        assertFalse(service.isRetriableIOException(null));
    }

    @Test
    void testIsRetriableIOException_CaseInsensitive() {
        assertTrue(service.isRetriableIOException("CONNECTION RESET"));
        assertTrue(service.isRetriableIOException("BROKEN PIPE"));
    }

    // ========== 심각한 에러 판단 테스트 ==========

    @Test
    void testIsCriticalError_ClassNotFound() {
        assertTrue(service.isCriticalError(ClassNotFoundException.class));
    }

    @Test
    void testIsCriticalError_SecurityException() {
        assertTrue(service.isCriticalError(SecurityException.class));
    }

    @Test
    void testIsCriticalError_NonCritical() {
        assertFalse(service.isCriticalError(RuntimeException.class));
        assertFalse(service.isCriticalError(IllegalArgumentException.class));
    }

    // ========== 포트 유효성 테스트 ==========

    @Test
    void testIsValidPort_Valid() {
        assertTrue(service.isValidPort(8080));
        assertTrue(service.isValidPort(1));
        assertTrue(service.isValidPort(65535));
    }

    @Test
    void testIsValidPort_Invalid() {
        assertFalse(service.isValidPort(0));
        assertFalse(service.isValidPort(-1));
        assertFalse(service.isValidPort(65536));
        assertFalse(service.isValidPort(100000));
    }

    @Test
    void testIsValidPort_CommonPorts() {
        assertTrue(service.isValidPort(80));   // HTTP
        assertTrue(service.isValidPort(443));  // HTTPS
        assertTrue(service.isValidPort(22));   // SSH
        assertTrue(service.isValidPort(3306)); // MySQL
    }

    // ========== IP 주소 유효성 테스트 ==========

    @Test
    void testIsValidIPAddress_Valid() {
        assertTrue(service.isValidIPAddress("192.168.0.1"));
        assertTrue(service.isValidIPAddress("127.0.0.1"));
        assertTrue(service.isValidIPAddress("10.0.0.1"));
        assertTrue(service.isValidIPAddress("255.255.255.255"));
    }

    @Test
    void testIsValidIPAddress_Localhost() {
        assertTrue(service.isValidIPAddress("localhost"));
        assertTrue(service.isValidIPAddress("LOCALHOST"));
    }

    @Test
    void testIsValidIPAddress_Invalid() {
        assertFalse(service.isValidIPAddress(null));
        assertFalse(service.isValidIPAddress(""));
        assertFalse(service.isValidIPAddress("   "));
        assertFalse(service.isValidIPAddress("256.1.1.1"));
        assertFalse(service.isValidIPAddress("192.168.0"));
        assertFalse(service.isValidIPAddress("192.168.0.1.1"));
        assertFalse(service.isValidIPAddress("abc.def.ghi.jkl"));
    }

    @Test
    void testIsValidIPAddress_EdgeCases() {
        assertTrue(service.isValidIPAddress("0.0.0.0"));
        assertFalse(service.isValidIPAddress("-1.0.0.0"));
        assertFalse(service.isValidIPAddress("192.168.-1.1"));
    }

    // ========== 연결 정보 검증 테스트 ==========

    @Test
    void testValidateConnection_Valid() {
        NetworkConnectionService.ConnectionValidationResult result = 
            service.validateConnection("192.168.0.1", 8080);
        
        assertTrue(result.isValid());
        assertEquals("연결 정보가 유효합니다.", result.getMessage());
    }

    @Test
    void testValidateConnection_InvalidIP() {
        NetworkConnectionService.ConnectionValidationResult result = 
            service.validateConnection("999.999.999.999", 8080);
        
        assertFalse(result.isValid());
        assertEquals("유효하지 않은 IP 주소입니다.", result.getMessage());
    }

    @Test
    void testValidateConnection_InvalidPort() {
        NetworkConnectionService.ConnectionValidationResult result = 
            service.validateConnection("192.168.0.1", 99999);
        
        assertFalse(result.isValid());
        assertEquals("포트 번호는 1-65535 사이여야 합니다.", result.getMessage());
    }

    @Test
    void testValidateConnection_BothInvalid() {
        NetworkConnectionService.ConnectionValidationResult result = 
            service.validateConnection("invalid", -1);
        
        assertFalse(result.isValid());
        // IP가 먼저 검증되므로 IP 오류 메시지
        assertEquals("유효하지 않은 IP 주소입니다.", result.getMessage());
    }

    // ========== 재연결 대기 시간 계산 테스트 ==========

    @Test
    void testCalculateReconnectDelay_FirstAttempt() {
        assertEquals(1000L, service.calculateReconnectDelay(1, 1000L));
    }

    @Test
    void testCalculateReconnectDelay_ExponentialBackoff() {
        assertEquals(1000L, service.calculateReconnectDelay(1, 1000L));
        assertEquals(2000L, service.calculateReconnectDelay(2, 1000L));
        assertEquals(4000L, service.calculateReconnectDelay(3, 1000L));
        assertEquals(8000L, service.calculateReconnectDelay(4, 1000L));
        assertEquals(16000L, service.calculateReconnectDelay(5, 1000L));
    }

    @Test
    void testCalculateReconnectDelay_MaxCap() {
        // 10번 이상 시도해도 exponential이 멈춤
        long delay10 = service.calculateReconnectDelay(10, 1000L);
        long delay15 = service.calculateReconnectDelay(15, 1000L);
        assertEquals(delay10, delay15);
    }

    @Test
    void testCalculateReconnectDelay_MaxDelayCap() {
        // 30초 이상은 안 됨
        long delay = service.calculateReconnectDelay(20, 1000L);
        assertTrue(delay <= 30000L);
    }

    @Test
    void testCalculateReconnectDelay_ZeroOrNegativeAttempt() {
        assertEquals(1000L, service.calculateReconnectDelay(0, 1000L));
        assertEquals(500L, service.calculateReconnectDelay(-1, 500L));
    }

    @Test
    void testCalculateReconnectDelay_DefaultBaseDelay() {
        assertEquals(1000L, service.calculateReconnectDelay(1));
        assertEquals(2000L, service.calculateReconnectDelay(2));
    }

    // ========== 통합 시나리오 테스트 ==========

    @Test
    void testConnectionLifecycle() {
        // 1. 연결 정보 검증
        NetworkConnectionService.ConnectionValidationResult validation = 
            service.validateConnection("localhost", 8080);
        assertTrue(validation.isValid());
        
        // 2. 연결 활성 상태 확인
        assertTrue(service.isConnectionActive(true, false));
        
        // 3. 타임아웃 확인
        long start = System.nanoTime();
        long current = start + 5_000_000_000L;
        assertFalse(service.isConnectionTimeout(start, current));
        
        // 4. 에러 발생 시
        assertFalse(service.hasExceededMaxErrors(3));
    }

    @Test
    void testErrorHandlingScenario() {
        // 1. 재시도 가능한 에러
        assertTrue(service.isRetriableIOException("Connection reset"));
        
        // 2. 에러 카운트 증가
        int errorCount = 1;
        assertFalse(service.hasExceededMaxErrors(errorCount));
        
        // 3. 재연결 대기 시간 계산
        long delay = service.calculateReconnectDelay(errorCount);
        assertEquals(1000L, delay);
        
        // 4. 최대 에러 도달
        errorCount = 5;
        assertTrue(service.hasExceededMaxErrors(errorCount));
    }
}
