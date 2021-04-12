package build;

public class Field {

    private final int numMines;
    private final int numRows;
    private final int numCols;
    private final Block[][] field;
    private final MineBlock[] mineCoords;

    public Field(int mines, int rows, int cols){
        numMines = mines;
        numRows = rows;
        numCols = cols;
        field = new Block[rows][cols];
        mineCoords = new MineBlock[mines]; // coordinates of mines

        generateField();
    }

    private void placeMines(){
        int count = 0;
        while(count != numMines){
            int r = (int) (Math.random() * numRows);
            int c = (int) (Math.random() * numCols);
            if (!(field[r][c] instanceof MineBlock)) { // not already a mine
                field[r][c] = new MineBlock(r, c);
                mineCoords[count] = (MineBlock) getBlock(r,c); // reference issue?
                count++;
            }
        }
    }

    private void generateNumbers(){
        for (int r = 0; r < numRows; r++){
            for (int c = 0; c < numCols; c++){
                if (!(field[r][c] instanceof MineBlock)) {
                    int countMines = countSurroundingMines(r, c);
                    field[r][c] = new NumBlock(countMines, r, c);
                }
            }
        }
    }

    // util method for generateNumbers
    private int countSurroundingMines(int r, int c) {
        int count = 0;
        int[][] indexes = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] pair : indexes) {
            if (isValidCoord(r + pair[0], c + pair[1])){
                if (field[r + pair[0]][c + pair[1]] != null && field[r + pair[0]][c + pair[1]].isMine())
                    count++;
            }
        }
        return count;
    }

    private void generateField(){
        placeMines();
        generateNumbers();
    }

    public void revealField(){
        for (Block[] r : field){
            for (Block s : r){
                s.forceReveal();
            }
        }
    }

    public Block getBlock(int row, int col){
        return field[row][col];
    }

    public MineBlock[] getMineCoords(){ return mineCoords; }

    public Block[][] getField(){
        return field;
    }

    public int getNumRows(){
        return numRows;
    }

    public int getNumCols(){
        return numCols;
    }

    public boolean isValidCoord(int row, int col){
        return 0 <= row && row <= getNumRows() - 1 && 0 <= col && col <= getNumCols() - 1;
    }


}
