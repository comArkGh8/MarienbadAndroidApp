package com.example.marienbad.computer_strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



public class RowOperations {

    /**
     *
     * @param stick_array - will already have zeros removed
     * @return
     */
    public static int getNumberNonZeroRows(List<Integer> stick_array){
        return stick_array.size();
    }


    /**
     *
     * @param rowSticksMap - input rows and sticks
     * @return sorted Array of non-zeros in ascending order
     */
    public static HashMap<Integer,Integer> sortedByIncreasingSticks(HashMap<Integer,Integer> rowSticksMap){

        HashMap<Integer, Integer> sortedSticksMap = rowSticksMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        HashMap<Integer, Integer> sortedMinusZeros = sortedSticksMap.entrySet().stream()
                .filter(x -> x.getValue() != 0)
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return sortedMinusZeros;
    }


    // returns (row or sticks) in kth order (from 1-number of non-zero)
    public static int getOrderAt(String rowOrSticks, int k, HashMap<Integer,Integer> rowSticksMap) {
        HashMap<Integer,Integer> sortedMap = sortedByIncreasingSticks(rowSticksMap);
        switch (rowOrSticks) {
            case "row":
                List<Integer> rows = new ArrayList<>(sortedMap.keySet());
                int kthOrderedRow = rows.get(k-1);
                return kthOrderedRow;

            case "sticks":
                List<Integer> sticks = new ArrayList<>(sortedMap.values());
                int kthOrderedSticks = sticks.get(k-1);
                return kthOrderedSticks;
        }

        return 0;
    }


    /**
     *
     * @param board
     * @return true id there are two rows with same number of sticks
     * also updates the twoSame MarienbadBoard class variable to contain those two rows
     */

    public static boolean hasRepeatedRow(MarienbadBoard board) {

        // make HashMap to count number of times (specific number of sticks) arises
        // has form number_of_sticks => how_many_times
        HashMap<Integer,Integer> counts = new HashMap<Integer,Integer>();
        // iterate over the row of sticks
        Iterator it = board.rowsOfSticks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (counts.containsKey(pair.getValue())) {
                int numberOccur = counts.get(pair.getValue());
                counts.put((Integer) pair.getValue(), numberOccur+1);
            }
            else {
                counts.put((Integer) pair.getValue(), 1);
            }
        }

        // just filter out the rows which are repeated
        HashMap<Integer, Integer> rowsSticksWhichRepeat = board.rowsOfSticks
                .entrySet()
                .stream()
                .filter(x -> counts.get(x.getValue()) >1 )
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        if (rowsSticksWhichRepeat.isEmpty()) {
            return false;
        }
        else {
            // sort the (repeated row map)
            HashMap<Integer,Integer> sortedRepeats = sortedByIncreasingSticks(rowsSticksWhichRepeat);
            // make list of keys
            List<Integer> sortedRows= new ArrayList<>(sortedRepeats.keySet());
            // set board.twoSame
            board.twoSame[0] = sortedRows.get(0);
            board.twoSame[1] = sortedRows.get(1);
            return true;

        }
    }

    // gives true or false when applied to the stick array (here, a list)
    public static boolean arrayHasRepeatedRow(List<Integer> arrayList) {
        Set<Integer> arraySet = new HashSet<>(arrayList);
        if (arraySet.size() < arrayList.size()) {
            return true;
        }
        return false;
    }


}
