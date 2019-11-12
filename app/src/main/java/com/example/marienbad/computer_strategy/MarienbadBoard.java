package com.example.marienbad.computer_strategy;


import java.util.HashMap;


public class MarienbadBoard {

    public HashMap<Integer, Integer> rowsOfSticks;
    public Integer[] twoSame;


    /* Rep invariant:
     *    rowsOfSticks.length =4
     */

    /**
     * Make an initial board
     * here 1 => 7, 2 => 5, 3 => 3, 4 => 1
     */
    public MarienbadBoard() {
        this.rowsOfSticks  = new HashMap<Integer, Integer>() {{
            put(1, 7);
            put(2, 5);
            put(3, 3);
            put(4, 1);
        }};
        this.twoSame = new Integer[2];
    }

    /**
     * Make a Board.
     * input are the no. of sticks in the four rows (as a list)
     */

    public MarienbadBoard(HashMap<Integer, Integer> stickList) {
        this.rowsOfSticks = stickList;
        this.twoSame = new Integer[2];
    }


    public void change(int row, int sticks){
        HashMap<Integer, Integer> sticksMap = this.rowsOfSticks;
        int originalSticks = sticksMap.get(row);
        this.rowsOfSticks.put(row, originalSticks -sticks);
        this.twoSame = new Integer[2];
    }



}
