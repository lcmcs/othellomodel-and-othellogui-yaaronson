package edu.touro.cs.mcon364;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;



enum PlayerValue {
    NONE(" "), BLACK("\u25CF"), WHITE("\u25CB");
    private final String state;

    PlayerValue(String cellState) {

        state = cellState;
    }

    public PlayerValue opposite(){
        if(this.equals(PlayerValue.BLACK)){
            return PlayerValue.WHITE;
        }
        else if(this.equals(PlayerValue.WHITE)){
            return PlayerValue.BLACK;
        }
        else return PlayerValue.NONE;
    }

    public String toGUI(){
        if (this == PlayerValue.NONE){
            return " ";
        }
        if (this == PlayerValue.WHITE){
            return "\u25CB";
        }
        if (this == PlayerValue.BLACK){
            return "\u25CF";
        }
        return null;
    }

    @Override
    public String toString(){
        return state;
    }
}

public class OthelloModel {
    private PlayerValue [][] grid;
    private final int BOARD_SIZE;

    public OthelloModel(){
        BOARD_SIZE = 8;
        grid = new PlayerValue[BOARD_SIZE][BOARD_SIZE];
        initBoard();
    }

    public OthelloModel(int size){
        BOARD_SIZE = size;
        grid = new PlayerValue[BOARD_SIZE][BOARD_SIZE];
        initBoard();
    }

    public OthelloModel(PlayerValue[][] startingGrid){
        BOARD_SIZE = startingGrid.length;
        grid = startingGrid;
    }

    protected void initBoard(){
        int boardCenter = BOARD_SIZE/2;
        for(PlayerValue[] state : grid){
            Arrays.fill(state, PlayerValue.NONE);
        }
        grid[boardCenter - 1][boardCenter - 1] = grid[boardCenter][boardCenter] = PlayerValue.WHITE;
        grid[boardCenter - 1][boardCenter] = grid[boardCenter][boardCenter - 1] = PlayerValue.BLACK;
    }

    public boolean validMoveExists(PlayerValue state){
        for(int row = 0; row < grid.length; row++){
            for(int col = 0; col < grid[row].length; col++){
                if(grid[row][col] == PlayerValue.NONE && isValid(new Cell(row, col), state, false)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean indexInRange(Cell move){
        return move.row >= 0 && move.row < BOARD_SIZE && move.column >= 0 && move.column < BOARD_SIZE;
    }

    public ArrayList<Cell> getFlipCells(Cell move, PlayerValue state, boolean changeState){
        ArrayList<Cell> flipCellList = new ArrayList<>();
        if(grid[move.row][move.column] != PlayerValue.NONE){
            return flipCellList;
        }
        for(int offSetRows = -1; offSetRows <= 1; offSetRows++){
            for(int offSetCols = -1; offSetCols <= 1; offSetCols++){
                int neighboringRow = move.row + offSetRows;
                int neighboringCol = move.column + offSetCols;
                if(indexInRange(new Cell(neighboringRow, neighboringCol))) {
                    if (grid[neighboringRow][neighboringCol] == state.opposite()) {
                        for (int multiplier = 2; multiplier <= grid[move.row].length; multiplier++) {
                            int nextRow = move.row + (offSetRows * multiplier);
                            int nextCol = move.column + (offSetCols * multiplier);
                            if (indexInRange(new Cell(nextRow, nextCol))) {
                                if (grid[nextRow][nextCol] == PlayerValue.NONE) {
                                    break;
                                }
                                else if (grid[nextRow][nextCol] == state) {
                                    Cell flipCell = new Cell(nextRow, nextCol);
                                    flipCellList.add(flipCell);
                                    if(changeState){
                                        flipCells(move, flipCell, new Cell(offSetRows, offSetCols), state);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return flipCellList;
    }

    public boolean isValid(Cell move, PlayerValue state, boolean changeState){
        return (getFlipCells(move, state, changeState).size() > 0);
    }

    private void flipCells(Cell move, Cell flipCell, Cell offSetCell, PlayerValue state){
        while (flipCell.column != move.column || flipCell.row != move.row) {
            flipCell.column -= offSetCell.column;
            flipCell.row -= offSetCell.row;
            grid[flipCell.row][flipCell.column] = state;
        }
    }

    public PlayerValue getWinner(){
        int numCells = BOARD_SIZE * BOARD_SIZE;
        int black = 0, white = 0;

        for(PlayerValue[] row : grid){
            for(PlayerValue cell: row){
                if(cell.equals(PlayerValue.WHITE)){
                    white++;
                }
                else if(cell.equals(PlayerValue.BLACK)){
                    black++;
                }
            }
            if(black > numCells/2 + 1 || white > numCells/2 + 1){
                break;
            }
        }

        if(black == white)
            return PlayerValue.NONE;
        return black > white?PlayerValue.BLACK:PlayerValue.WHITE;
    }

    public ArrayList<Cell> getValidMoves(PlayerValue color){
        ArrayList<Cell> validCells = new ArrayList<>();
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                if(isValid(new Cell(row, col), color, false)){
                    validCells.add(new Cell(row, col));
                }
            }
        }
        return validCells;
    }

    public boolean isGameOver(){
        if(!validMoveExists(PlayerValue.WHITE) && !validMoveExists(PlayerValue.BLACK)){
            return true;
        }
        for(int row = 0; row < grid.length; row++){
            if(Arrays.asList(grid[row]).contains(PlayerValue.NONE)){
                return false;
            }
        }
        return true;
    }

    public boolean makeMove(Cell move, PlayerValue state){
        if(isValid(move, state, true)) {
            grid[move.row][move.column] = state;
            return true;
        }
        return false;
    }


    public ArrayList<Cell> computerMove(){
        int max = 0;
        Cell cell = new Cell(2,2);
        for (int i =0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                Cell currentCell  = new Cell(i,j);
                if (isValid(currentCell, PlayerValue.WHITE, false)){
                    int spaces = getFlipCells(currentCell,PlayerValue.WHITE, false).size();
                    if (spaces >= max){
                        max = spaces;
                        cell = currentCell;
                    }

                }
            }
        }
        ArrayList<Cell> places = getFlipCells(cell,PlayerValue.WHITE,true);
        for (int i = 0; i < places.size(); i++){
            Cell x = places.get(i);
            grid[x.row][x.column] = PlayerValue.WHITE;
        }
        return places;
    }

    public int getBoardSize(){
        return BOARD_SIZE;
    }

    public PlayerValue[][] toArray(){
        return Arrays.copyOf(grid, BOARD_SIZE);
    }

    @Override
    public String toString(){
        StringBuilder board = new StringBuilder();
        board.append("  _");
        for(int col = 0; col < grid[0].length; col++){
            board.append((char)('A' + col));
            board.append("__");
        }
        board.append('\n');
        for (int row = 0; row < grid.length; row++) {
            board.append(row + 1);
            board.append("|_");
            for (int col = 0; col < grid[row].length; col++) {
                board.append(grid[row][col]);
                board.append("__");
            }
            board.append('\n');
        }
        return board.toString();
    }

    public PlayerValue getCellValue(int x, int y) {
        return grid[x][y];
    }

    protected void setGrid(PlayerValue[][] initialGrid) {
        grid = initialGrid;
    }

    public static class Cell{
        int row, column, flips;

        public Cell(){
            flips = 0;
        }

        public Cell(int r, int c){
            row = r;
            column = c;
            flips = 0;
        }
    }
}
