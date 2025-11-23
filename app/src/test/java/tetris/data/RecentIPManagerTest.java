package tetris.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RecentIPManager 테스트
 */
class RecentIPManagerTest {
    
    private RecentIPManager manager;
    
    @BeforeEach
    void setUp() {
        manager = RecentIPManager.getInstance();
    }
    
    @Test
    void testGetInstance() {
        RecentIPManager instance1 = RecentIPManager.getInstance();
        RecentIPManager instance2 = RecentIPManager.getInstance();
        
        assertSame(instance1, instance2);
    }
    
    @Test
    void testAddValidIP() {
        manager.addRecentIP("192.168.1.1");
        
        List<String> recentIPs = manager.getRecentIPs();
        assertTrue(recentIPs.contains("192.168.1.1"));
    }
    
    @Test
    void testAddInvalidIP() {
        int sizeBefore = manager.getRecentIPs().size();
        
        manager.addRecentIP("invalid.ip");
        manager.addRecentIP("999.999.999.999");
        manager.addRecentIP("");
        manager.addRecentIP(null);
        
        int sizeAfter = manager.getRecentIPs().size();
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    void testAddDuplicateIP() {
        manager.addRecentIP("192.168.1.100");
        manager.addRecentIP("192.168.1.101");
        manager.addRecentIP("192.168.1.100"); // 중복
        
        List<String> recentIPs = manager.getRecentIPs();
        long count = recentIPs.stream().filter(ip -> ip.equals("192.168.1.100")).count();
        
        assertEquals(1, count); // 중복 없이 하나만 있어야 함
        assertEquals("192.168.1.100", recentIPs.get(0)); // 맨 앞에 있어야 함
    }
    
    @Test
    void testMaxRecentIPs() {
        // 최대 개수(5개)를 초과하여 추가
        manager.clearRecentIPs();
        manager.addRecentIP("192.168.1.1");
        manager.addRecentIP("192.168.1.2");
        manager.addRecentIP("192.168.1.3");
        manager.addRecentIP("192.168.1.4");
        manager.addRecentIP("192.168.1.5");
        manager.addRecentIP("192.168.1.6"); // 6번째
        
        List<String> recentIPs = manager.getRecentIPs();
        
        assertTrue(recentIPs.size() <= 5); // 최대 5개
        assertEquals("192.168.1.6", recentIPs.get(0)); // 가장 최근 IP가 맨 앞
        assertFalse(recentIPs.contains("192.168.1.1")); // 가장 오래된 IP는 제거됨
    }
    
    @Test
    void testGetRecentIPsReturnsImmutableCopy() {
        manager.addRecentIP("192.168.1.10");
        
        List<String> recentIPs1 = manager.getRecentIPs();
        List<String> recentIPs2 = manager.getRecentIPs();
        
        assertNotSame(recentIPs1, recentIPs2); // 다른 인스턴스
    }
    
    @Test
    void testClearRecentIPs() {
        manager.addRecentIP("192.168.1.20");
        manager.clearRecentIPs();
        
        List<String> recentIPs = manager.getRecentIPs();
        assertTrue(recentIPs.isEmpty());
    }
    
    @Test
    void testLocalhostIP() {
        manager.addRecentIP("127.0.0.1");
        
        List<String> recentIPs = manager.getRecentIPs();
        assertTrue(recentIPs.contains("127.0.0.1"));
    }
    
    @Test
    void testIPWithLeadingTrailingSpaces() {
        String testIP = "  192.168.1.30  ";
        manager.addRecentIP(testIP);
        
        List<String> recentIPs = manager.getRecentIPs();
        // trim된 버전이거나 원본 그대로일 수 있음
        boolean found = recentIPs.contains("192.168.1.30") || recentIPs.contains(testIP.trim());
        assertTrue(found);
    }
    
    @Test
    void testMultipleOperations() {
        manager.clearRecentIPs();
        
        manager.addRecentIP("192.168.1.1");
        assertEquals(1, manager.getRecentIPs().size());
        
        manager.addRecentIP("192.168.1.2");
        assertEquals(2, manager.getRecentIPs().size());
        
        manager.clearRecentIPs();
        assertEquals(0, manager.getRecentIPs().size());
    }
}
