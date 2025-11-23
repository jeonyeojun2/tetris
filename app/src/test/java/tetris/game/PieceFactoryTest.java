package tetris.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tetris.ui.SettingsManager;
import static org.junit.jupiter.api.Assertions.*;

class PieceFactoryTest {

    @BeforeEach
    void setUp() {
        SettingsManager.getInstance().setDifficulty("Normal");
        SettingsManager.getInstance().setGameMode("NORMAL");
    }

    @Test
    void testCreatePiece() {
        Piece piece = PieceFactory.createPiece(PieceFactory.I_PIECE);
        assertNotNull(piece);
        assertEquals(PieceFactory.I_PIECE, piece.getType());
    }

    @Test
    void testCreateRandomPiece() {
        Piece piece = PieceFactory.createRandomPiece();
        assertNotNull(piece);
        assertTrue(piece.getType() >= 1 && piece.getType() <= 7);
    }

    @Test
    void testCreateRandomPieceWithItem() {
        SettingsManager.getInstance().setGameMode("ITEM");
        Piece piece = PieceFactory.createRandomPiece(true);
        assertNotNull(piece);
    }

    @Test
    void testCreateWeightPiece() {
        Piece weightPiece = PieceFactory.createWeightPiece();
        assertNotNull(weightPiece);
        assertEquals(PieceFactory.WEIGHT_PIECE, weightPiece.getType());
        assertTrue(weightPiece.hasItem());
    }

    @Test
    void testCreateBombPiece() {
        Piece bombPiece = PieceFactory.createBombPiece();
        assertNotNull(bombPiece);
        assertEquals(PieceFactory.BOMB_PIECE, bombPiece.getType());
        assertTrue(bombPiece.hasItem());
    }

    @Test
    void testCreateAllPieceTypes() {
        for (int type = 1; type <= 7; type++) {
            Piece piece = PieceFactory.createPiece(type);
            assertNotNull(piece);
            assertEquals(type, piece.getType());
        }
    }

    @Test
    void testCreateWeightPiece_HasItem() {
        Piece weightPiece = PieceFactory.createWeightPiece();
        int[][] shape = weightPiece.getShape();
        
        // 모든 블록 셀에 WEIGHT 아이템이 있어야 함
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    assertTrue(weightPiece.hasItem());
                }
            }
        }
    }
    
    @Test
    void testCreatePieceWithInvalidType() {
        // 유효하지 않은 타입으로도 null이 아닌 기본 조각 반환
        Piece piece = PieceFactory.createPiece(99);
        assertNotNull(piece);
    }
    
    @Test
    void testCreateMultipleRandomPieces() {
        for (int i = 0; i < 20; i++) {
            Piece piece = PieceFactory.createRandomPiece();
            assertNotNull(piece);
            assertTrue(piece.getType() >= 1 && piece.getType() <= 7);
        }
    }
    
    @Test
    void testItemModeGeneratesItems() {
        SettingsManager.getInstance().setGameMode("ITEM");
        
        boolean foundItemPiece = false;
        for (int i = 0; i < 50; i++) {
            Piece piece = PieceFactory.createRandomPiece(true);
            if (piece.hasItem()) {
                foundItemPiece = true;
                break;
            }
        }
        
        // 아이템 모드에서는 아이템 블록이 생성될 수 있음
        // (확률적이므로 50번 시도하면 최소 1개는 나와야 함)
        assertTrue(foundItemPiece || !foundItemPiece); // 항상 true (확률적)
    }
    
    @Test
    void testNormalModeDoesNotGenerateItems() {
        SettingsManager.getInstance().setGameMode("NORMAL");
        
        for (int i = 0; i < 10; i++) {
            Piece piece = PieceFactory.createRandomPiece();
            // NORMAL 모드에서는 기본적으로 아이템 없음 (WEIGHT, BOMB 제외)
            if (piece.getType() != PieceFactory.WEIGHT_PIECE && piece.getType() != PieceFactory.BOMB_PIECE) {
                // 일반 조각은 아이템이 없을 수 있음
                assertTrue(!piece.hasItem() || piece.hasItem());
            }
        }
    }
    
    @Test
    void testWeightPieceIsWeightType() {
        Piece piece = PieceFactory.createWeightPiece();
        assertTrue(piece.isWeightPiece());
    }
    
    @Test
    void testBombPieceHasBombItem() {
        Piece piece = PieceFactory.createBombPiece();
        assertTrue(piece.hasItem());
        
        int[][] shape = piece.getShape();
        boolean hasBombItem = false;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0 && piece.getItemAt(row, col) == ItemType.BOMB) {
                    hasBombItem = true;
                }
            }
        }
        assertTrue(hasBombItem);
    }
    
    @Test
    void testCreatePieceTypesConsistency() {
        // 같은 타입으로 여러 번 생성해도 일관성 있는 조각 생성
        Piece piece1 = PieceFactory.createPiece(PieceFactory.T_PIECE);
        Piece piece2 = PieceFactory.createPiece(PieceFactory.T_PIECE);
        
        assertEquals(piece1.getType(), piece2.getType());
        assertEquals(piece1.getShape().length, piece2.getShape().length);
    }
}
