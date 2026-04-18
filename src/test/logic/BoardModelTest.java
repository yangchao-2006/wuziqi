package test.logic;

import main.gobang.logic.BoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardModelTest {
    private BoardModel model;

    @BeforeEach
    void setUp() {
        model = new BoardModel();
    }

    @Test
    void testInitialBoardIsEmpty() {
        for (int i = 0; i < BoardModel.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardModel.BOARD_SIZE; j++) {
                assertEquals(0, model.getPiece(i, j));
            }
        }
        assertFalse(model.isFull());
    }

    @Test
    void testPlacePieceSuccess() {
        assertTrue(model.placePiece(0, 0, 1));
        assertEquals(1, model.getPiece(0, 0));
    }

    @Test
    void testPlacePieceOnOccupiedCellFails() {
        model.placePiece(5, 5, 2);
        assertFalse(model.placePiece(5, 5, 1));
        assertEquals(2, model.getPiece(5, 5));
    }

    @Test
    void testPlacePieceOutOfBoundsFails() {
        assertFalse(model.placePiece(-1, 0, 1));
        assertFalse(model.placePiece(BoardModel.BOARD_SIZE, 0, 1));
    }

    @Test
    void testRemovePiece() {
        model.placePiece(3, 3, 1);
        model.removePiece(3, 3);
        assertEquals(0, model.getPiece(3, 3));
    }

    @Test
    void testIsFull() {
        for (int i = 0; i < BoardModel.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardModel.BOARD_SIZE; j++) {
                model.placePiece(i, j, 1);
            }
        }
        assertTrue(model.isFull());
    }

    @Test
    void testClear() {
        model.placePiece(7, 7, 2);
        model.clear();
        assertEquals(0, model.getPiece(7, 7));
        assertFalse(model.isFull());
    }
}