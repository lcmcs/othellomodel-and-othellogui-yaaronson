package edu.touro.cs.mcon364;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {


    OthelloModel game = new OthelloModel();

    @org.junit.jupiter.api.Test
    void ValidMoveTest() {

        game.makeMove(new OthelloModel.Cell(2, 3), PlayerValue.BLACK);
        game.makeMove(new OthelloModel.Cell(3, 2), PlayerValue.BLACK);
        game.makeMove(new OthelloModel.Cell(4, 5), PlayerValue.BLACK);
        game.makeMove(new OthelloModel.Cell(5, 4), PlayerValue.BLACK);
        game.makeMove(new OthelloModel.Cell(2, 4), PlayerValue.WHITE);
        game.makeMove(new OthelloModel.Cell(3, 5), PlayerValue.WHITE);
        game.makeMove(new OthelloModel.Cell(4, 2), PlayerValue.WHITE);
        assertFalse(game.validMoveExists(PlayerValue.WHITE));
    }

    @Test
    void computerMove() {
        game.initBoard();
        ArrayList<OthelloModel.Cell> result = game.computerMove();

        assertNotNull(result);

        assertFalse(result.isEmpty());

        for (OthelloModel.Cell cell : result) {
            assertFalse(game.isValid(cell, PlayerValue.WHITE, false));
        }

        for (OthelloModel.Cell cell : result) {
            assertEquals(PlayerValue.WHITE, game.getCellValue(cell.row,cell.column));
        }

    }

    @Test
    void makeMoveTest() {
        assertTrue(game.makeMove(new OthelloModel.Cell(3, 2), PlayerValue.BLACK));
        assertEquals(PlayerValue.BLACK, game.getCellValue(3, 2));
        assertFalse(game.makeMove(new OthelloModel.Cell(2, 3), PlayerValue.WHITE));
        assertFalse(game.makeMove(new OthelloModel.Cell(2, 3), PlayerValue.BLACK));
    }

    @Test
    void isGameOver() {
        game.makeMove(new OthelloModel.Cell(3, 2), PlayerValue.BLACK);
        assertFalse(game.isGameOver());
    }

    @Test
    void  getFlipCells() {
        game.makeMove(new OthelloModel.Cell(3, 4), PlayerValue.BLACK);
        game.makeMove(new OthelloModel.Cell(3, 3), PlayerValue.WHITE);
        game.makeMove(new OthelloModel.Cell(4, 4), PlayerValue.WHITE);
        game.makeMove(new OthelloModel.Cell(5, 4), PlayerValue.BLACK);
        ArrayList<OthelloModel.Cell> expected = new ArrayList<>();
        ArrayList<OthelloModel.Cell> actual = game.getFlipCells(new OthelloModel.Cell(6, 4), PlayerValue.BLACK, false);
        assertEquals(expected, actual);
    }

    @Test
    void getWinner() {
        PlayerValue[][] board = game.toArray();
        board[2][2] = PlayerValue.WHITE;
        board[3][3] = PlayerValue.WHITE;
        board[2][3] = PlayerValue.BLACK;
        board[3][2] = PlayerValue.BLACK;

        PlayerValue winner = game.getWinner();
        assertFalse(PlayerValue.NONE == winner);

    }

    @Test
    void getValidMoves() {
        PlayerValue[][] initialGrid = {
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },
                {PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE, PlayerValue.NONE,PlayerValue.NONE,PlayerValue.NONE, PlayerValue.NONE },

        };
        game.setGrid(initialGrid);

        boolean blackMoveExists = game.validMoveExists(PlayerValue.BLACK);
        assertFalse(blackMoveExists);

        boolean whiteMoveExists = game.validMoveExists(PlayerValue.WHITE);
        assertFalse(whiteMoveExists);

    }
}