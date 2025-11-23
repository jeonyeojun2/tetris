package tetris.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTypeTest {

    @Test
    void testGetDisplayChar() {
        assertEquals("L", ItemType.LINE_CLEAR.getDisplayChar());
        assertEquals("W", ItemType.WEIGHT.getDisplayChar());
        assertEquals("D", ItemType.DOUBLE_SCORE.getDisplayChar());
        assertEquals("+", ItemType.BOMB.getDisplayChar());
        assertEquals("S", ItemType.SKIP.getDisplayChar());
        assertEquals("", ItemType.NONE.getDisplayChar());
    }

    @Test
    void testItemTypeValues() {
        ItemType[] types = ItemType.values();
        assertTrue(types.length >= 6); // 최소 6개의 아이템 타입
    }
    
    @Test
    void testAllItemTypesHaveDisplayChar() {
        for (ItemType type : ItemType.values()) {
            assertNotNull(type.getDisplayChar());
        }
    }
    
    @Test
    void testItemTypeEnumConsistency() {
        assertEquals(ItemType.LINE_CLEAR, ItemType.valueOf("LINE_CLEAR"));
        assertEquals(ItemType.WEIGHT, ItemType.valueOf("WEIGHT"));
        assertEquals(ItemType.DOUBLE_SCORE, ItemType.valueOf("DOUBLE_SCORE"));
        assertEquals(ItemType.BOMB, ItemType.valueOf("BOMB"));
        assertEquals(ItemType.SKIP, ItemType.valueOf("SKIP"));
        assertEquals(ItemType.NONE, ItemType.valueOf("NONE"));
    }
    
    @Test
    void testItemTypeToString() {
        assertEquals("LINE_CLEAR", ItemType.LINE_CLEAR.toString());
        assertEquals("WEIGHT", ItemType.WEIGHT.toString());
        assertEquals("NONE", ItemType.NONE.toString());
    }
    
    @Test
    void testNoneItemHasEmptyChar() {
        assertEquals("", ItemType.NONE.getDisplayChar());
    }
}
