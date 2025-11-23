package tetris;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 매우 단순한 테스트 - Gradle 테스트 실행 확인용
 */
class SimpleTest {
    
    @Test
    void testAddition() {
        assertEquals(4, 2 + 2);
    }
    
    @Test
    void testString() {
        String test = "hello";
        assertNotNull(test);
        assertEquals(5, test.length());
    }
}
