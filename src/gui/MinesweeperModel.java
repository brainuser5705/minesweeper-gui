package gui;

import build.*;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperModel extends Field{

    private int flagsLeft;

    private int gameOverMode;

    private boolean isLoser = false;

    private List<Observer<MinesweeperModel, String>> observers = new ArrayList<Observer<MinesweeperModel, String>>();

    private int[][] INDEXES = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    public MinesweeperModel(int numMines, int numRows, int numCols, int gameOverMode){
        super(numMines, numRows, numCols);
        flagsLeft = numMines;
        this.gameOverMode = gameOverMode;
    }

    public void setGameOverMode(int mode){
        this.gameOverMode = mode;
        notifyObservers(null);
    }

    public void isGameOver(){

        boolean isWinner = switch(gameOverMode){
            case 1 -> checkWinningGame1();
            case 2 -> checkWinningGame2();
            case 3 -> checkWinningGame3();
            default -> throw new IllegalStateException("Unexpected value: " + gameOverMode);
        };

        if (isWinner){
            revealField();
            notifyObservers("winner");
        }else if (isLoser){
            revealField();
            notifyObservers("loser");
        }

    }

    private boolean checkWinningGame1(){
        Block[][] field = getField();
        for (Block[] row : field){
            for (Block block : row){
                if ((block.isMine() && !block.isFlag()) || (!block.isMine() && !block.isReveal())){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWinningGame2(){
        boolean allMinesFlagged = true;
        MineBlock[] mineCoords = getMineCoords();
        for (MineBlock mine : mineCoords){
            if (!mine.isFlag()) {
                allMinesFlagged = false;
                break;
            }
        }

        return getFlagsLeft() == 0 && allMinesFlagged;
    }

    private boolean checkWinningGame3(){
        Block[][] field = getField();
        for (Block[] row : field){
            for (Block block : row){
                if (!block.isMine() && !block.isReveal()) return false;
            }
        }
        return true;
    }

    public void reveal(Block block){

        try{
            block.reveal();

            if (!block.isFlag()){
                if (block.isMine()) {
                    isLoser = true;
                } else {
                    ArrayList<Block> revealedBlocks = new ArrayList<>();
                    revealedBlocks.add(block);
                    revealSurroundingBlanks(block, revealedBlocks);
                }
            }

            notifyObservers(null);
        }catch (StackOverflowError e){
            System.out.println("Dimension size is too large.");
        }

    }

    public void toggleFlag(Block block){
        if (block.isFlag()){
            block.unflag();
            flagsLeft++;
        }else if (!block.isFlag()){
            block.flag();
            if (flagsLeft > 0 && !block.isReveal()) // not already revealed
                flagsLeft--;
        }
        notifyObservers(null);
    }

    //util
    private void revealSurroundingBlanks(Block s, ArrayList<Block> revealedSquares){

        for (int[] pair : INDEXES) {

            if (isValidCoord(s.getRow()+pair[0], s.getCol()+pair[1])){
                Block adjacentBlock = getBlock(s.getRow()+pair[0], s.getCol()+pair[1]);
                if (!revealedSquares.contains(adjacentBlock) && adjacentBlock.isBlankBlock()){ // is already revealed, then skip - add this function
                    adjacentBlock.reveal();
                    revealedSquares.add(adjacentBlock);
                    revealSurroundingBlanks(adjacentBlock, revealedSquares);
                }
            }
        }

        for (Block revealedBlock : revealedSquares){
            if (!revealedBlock.equals(s)) // skip the clicked block
                revealAdjacentBlock(revealedBlock);
        }

    }

    // util method for reveal surrounding blocks
    private void revealAdjacentBlock(Block s){

        for (int[] pair : INDEXES) {
            if (isValidCoord(s.getRow()+pair[0], s.getCol()+pair[1])){
                Block adjacentSquare = getBlock(s.getRow()+pair[0], s.getCol()+pair[1]);
                if (!adjacentSquare.isMine()){
                    adjacentSquare.reveal();
                }
            }
        }

    }

    public int getFlagsLeft(){
        return flagsLeft;
    }

    public void addObserver(Observer<MinesweeperModel, String> observer){
        this.observers.add(observer);
    }

    public void notifyObservers(String args){
        for (Observer<MinesweeperModel, String> observer : observers){
            observer.update(this, args);
        }
    }


}