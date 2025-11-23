package tetris.ui.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SettingsValidationServiceTest {
    
    private SettingsValidationService service;
    
    @BeforeEach
    void setUp() {
        service = new SettingsValidationService();
    }
    
    @Test
    void testFindDuplicateKeysNoDuplicates() {
        Map<String, String> p1Keys = new HashMap<>();
        p1Keys.put("left", "A");
        p1Keys.put("right", "D");
        
        Map<String, String> p2Keys = new HashMap<>();
        p2Keys.put("left", "J");
        p2Keys.put("right", "L");
        
        List<String> duplicates = service.findDuplicateKeys(p1Keys, p2Keys);
        
        assertTrue(duplicates.isEmpty());
    }
    
    @Test
    void testFindDuplicateKeysWithDuplicates() {
        Map<String, String> p1Keys = new HashMap<>();
        p1Keys.put("left", "A");
        p1Keys.put("right", "D");
        
        Map<String, String> p2Keys = new HashMap<>();
        p2Keys.put("left", "A"); // 중복
        p2Keys.put("right", "L");
        
        List<String> duplicates = service.findDuplicateKeys(p1Keys, p2Keys);
        
        assertEquals(1, duplicates.size());
        assertTrue(duplicates.contains("A"));
    }
    
    @Test
    void testFindDuplicateKeysIgnoresCase() {
        Map<String, String> p1Keys = new HashMap<>();
        p1Keys.put("left", "a");
        
        Map<String, String> p2Keys = new HashMap<>();
        p2Keys.put("left", "A");
        
        List<String> duplicates = service.findDuplicateKeys(p1Keys, p2Keys);
        
        assertEquals(1, duplicates.size());
    }
    
    @Test
    void testFindDuplicateWithinPlayerNoDuplicates() {
        Map<String, String> keys = new HashMap<>();
        keys.put("left", "A");
        keys.put("right", "D");
        keys.put("down", "S");
        
        List<String> duplicates = service.findDuplicateWithinPlayer(keys);
        
        assertTrue(duplicates.isEmpty());
    }
    
    @Test
    void testFindDuplicateWithinPlayerWithDuplicates() {
        Map<String, String> keys = new HashMap<>();
        keys.put("left", "A");
        keys.put("right", "A"); // 중복
        keys.put("down", "S");
        
        List<String> duplicates = service.findDuplicateWithinPlayer(keys);
        
        assertEquals(1, duplicates.size());
        assertTrue(duplicates.contains("A"));
    }
    
    @Test
    void testIsValidKeyValidSingleChar() {
        assertTrue(service.isValidKey("A"));
        assertTrue(service.isValidKey("Z"));
        assertTrue(service.isValidKey("5"));
    }
    
    @Test
    void testIsValidKeySpecialKeys() {
        assertTrue(service.isValidKey("SPACE"));
        assertTrue(service.isValidKey("ENTER"));
        assertTrue(service.isValidKey("UP"));
        assertTrue(service.isValidKey("DOWN"));
        assertTrue(service.isValidKey("LEFT"));
        assertTrue(service.isValidKey("RIGHT"));
    }
    
    @Test
    void testIsValidKeyInvalid() {
        assertFalse(service.isValidKey(null));
        assertFalse(service.isValidKey(""));
        assertFalse(service.isValidKey("   "));
        assertFalse(service.isValidKey("AB")); // 두 글자
        assertFalse(service.isValidKey("@")); // 특수문자
    }
    
    @Test
    void testIsValidScreenSize() {
        assertTrue(service.isValidScreenSize("작게"));
        assertTrue(service.isValidScreenSize("중간"));
        assertTrue(service.isValidScreenSize("크게"));
        assertFalse(service.isValidScreenSize("Invalid"));
        assertFalse(service.isValidScreenSize(null));
    }
    
    @Test
    void testIsValidDifficulty() {
        assertTrue(service.isValidDifficulty("Easy"));
        assertTrue(service.isValidDifficulty("Normal"));
        assertTrue(service.isValidDifficulty("Hard"));
        assertFalse(service.isValidDifficulty("Invalid"));
        assertFalse(service.isValidDifficulty(null));
    }
    
    @Test
    void testCalculateBlockSizeSmall() {
        assertEquals(20, service.calculateBlockSize("작게"));
    }
    
    @Test
    void testCalculateBlockSizeMedium() {
        assertEquals(25, service.calculateBlockSize("중간"));
    }
    
    @Test
    void testCalculateBlockSizeLarge() {
        assertEquals(30, service.calculateBlockSize("크게"));
    }
    
    @Test
    void testCalculateBlockSizeDefault() {
        assertEquals(25, service.calculateBlockSize("Invalid"));
        assertEquals(25, service.calculateBlockSize(null));
    }
    
    @Test
    void testCalculateCanvasSizeSmall() {
        int[] size = service.calculateCanvasSize("작게");
        assertArrayEquals(new int[]{480, 720}, size);
    }
    
    @Test
    void testCalculateCanvasSizeMedium() {
        int[] size = service.calculateCanvasSize("중간");
        assertArrayEquals(new int[]{600, 900}, size);
    }
    
    @Test
    void testCalculateCanvasSizeLarge() {
        int[] size = service.calculateCanvasSize("크게");
        assertArrayEquals(new int[]{720, 1080}, size);
    }
    
    @Test
    void testCalculateCanvasSizeDefault() {
        int[] size = service.calculateCanvasSize("Invalid");
        assertArrayEquals(new int[]{600, 900}, size);
    }
    
    @Test
    void testFindDuplicateKeysWithEmptyValues() {
        Map<String, String> p1Keys = new HashMap<>();
        p1Keys.put("left", "A");
        p1Keys.put("right", "");
        
        Map<String, String> p2Keys = new HashMap<>();
        p2Keys.put("left", "");
        p2Keys.put("right", "L");
        
        List<String> duplicates = service.findDuplicateKeys(p1Keys, p2Keys);
        
        assertTrue(duplicates.isEmpty()); // 빈 값은 무시됨
    }
    
    @Test
    void testFindDuplicateKeysWithNullValues() {
        Map<String, String> p1Keys = new HashMap<>();
        p1Keys.put("left", "A");
        p1Keys.put("right", null);
        
        Map<String, String> p2Keys = new HashMap<>();
        p2Keys.put("left", null);
        p2Keys.put("right", "L");
        
        List<String> duplicates = service.findDuplicateKeys(p1Keys, p2Keys);
        
        assertTrue(duplicates.isEmpty()); // null 값은 무시됨
    }
}
