package com.example.marienbad.computer_strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.System.out;


public class ChoiceOperations {

    /**
     *
     */
    public static List<Integer> randomChoice (MarienbadBoard gameBoard){
        List<Integer> choice = new ArrayList<>();
        HashMap<Integer,Integer> rowSticksMap = gameBoard.rowsOfSticks;
        HashMap<Integer,Integer> sortedMap = RowOperations.sortedByIncreasingSticks(rowSticksMap);
        List<Integer> sortedStickList = new ArrayList<>(sortedMap.values());
        List<Integer> sortedRowList = new ArrayList<>(sortedMap.keySet());

        int numberOfRows = RowOperations.getNumberNonZeroRows(sortedStickList);
        Random r = new Random();
        int randomRowOrderedIndex = r.nextInt(numberOfRows);
        int randomRow = sortedRowList.get(randomRowOrderedIndex);

        // now from row choose a random number of sticks
        int numberSticksInRandomRow = sortedStickList.get(randomRowOrderedIndex);
        int randomSticks = r.nextInt(numberSticksInRandomRow) + 1;

        choice = Arrays.asList(randomRow,randomSticks);

        return choice;

    }

    /**
     *
     * @param gameBoard - assumes not lost yet (i.e. board is not 1000)
     * @return - best choice if not 1
     */
    public static List<Integer> bestChoice (MarienbadBoard gameBoard){

        List<Integer> choice = new ArrayList<>();


        HashMap<Integer,Integer> rowSticksMap = gameBoard.rowsOfSticks;
        HashMap<Integer,Integer> sortedMap = RowOperations.sortedByIncreasingSticks(rowSticksMap);
        List<Integer> sortedList = new ArrayList<>(sortedMap.values());

        List<Integer> compareList = Arrays.asList(1,3,5,7);
        if (sortedList.equals(compareList)) {
            Random r = new Random();
            int randomRow = r.nextInt(4) + 1;
            int numberSticks = 1;

            choice = Arrays.asList(randomRow,numberSticks);
            return choice;
        }

        int numberOfRows = RowOperations.getNumberNonZeroRows(sortedList);
        // get the row with the maximum number of sticks
        int maxRow = RowOperations.getOrderAt("row", numberOfRows, sortedMap);

        if (LosingSituations.stickArrayIsLoser(sortedList)) {
            choice = Arrays.asList(maxRow,1);
            return choice;
        }

        // check if removing a row produces a loser
        // from sorted map, create a new list with an index removed (filter)
        // if a loser, return that index
        Set<Integer> sortedMapRows = sortedMap.keySet();
        for (int index : sortedMapRows) {
            HashMap<Integer, Integer> sortedMinusIndex = sortedMap.entrySet().stream()
                    .filter(x -> x.getKey() != index)
                    .collect(
                            Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            List<Integer> sortedMinusIndexList = new ArrayList<>(sortedMinusIndex.values());
            if (LosingSituations.stickArrayIsLoser(sortedMinusIndexList)) {
                choice = Arrays.asList(index,sortedMap.get(index));
                return choice;
            }

        }

        // otherwise go through case by case according to number of non-zero rows
        switch (numberOfRows) {

            case 1:
                // obvious: remove all but one
                List<Integer> row = new ArrayList<>(sortedMap.keySet());
                int oneRow = row.get(0);
                List<Integer> stick = new ArrayList<>(sortedMap.values());
                int oneSticks = stick.get(0);

                choice = Arrays.asList(oneRow, oneSticks - 1);
                return choice;

            case 2:
                // choose max - min
                int minSticks = RowOperations.getOrderAt("sticks", 1, sortedMap);
                choice = Arrays.asList(maxRow, sortedMap.get(maxRow) - minSticks);
                return choice;

            case 3:
                // check case by case according to first two in array
                List<Integer> firstTwoInStickList = new ArrayList<>();
                firstTwoInStickList.add(sortedList.get(0));
                firstTwoInStickList.add(sortedList.get(1));
                int maxSticks = RowOperations.getOrderAt("sticks",3,sortedMap);

                if (firstTwoInStickList.equals(Arrays.asList(1, 1))) {
                    choice = Arrays.asList(maxRow, maxSticks -1);
                    return choice;
                }
                else if (firstTwoInStickList.equals(Arrays.asList(1, 2))) {
                    if (maxSticks > 3) {
                        choice = Arrays.asList(maxRow, maxSticks - 3);
                        return choice;
                    }
                }
                else if (firstTwoInStickList.equals(Arrays.asList(1, 3))) {
                    choice = Arrays.asList(maxRow, maxSticks - 2);
                    return choice;
                }
                else if (firstTwoInStickList.equals(Arrays.asList(1, 4))) {
                    choice = Arrays.asList(maxRow, maxSticks - 5);
                    return choice;
                }
                else if (firstTwoInStickList.equals(Arrays.asList(1, 5))) {
                    choice = Arrays.asList(maxRow, maxSticks - 4);
                    return choice;
                }
                else if (firstTwoInStickList.equals(Arrays.asList(2, 3))) {
                    choice = Arrays.asList(maxRow, maxSticks - 1);
                    return choice;
                }
                break;

            case 4:
                // check for double rows and change to min min k k
                if (RowOperations.hasRepeatedRow(gameBoard)) {
                    // first get other rows
                    Integer allRows[] = {1, 2, 3, 4};
                    Set<Integer> otherRowsSet = new HashSet<>(Arrays.asList(allRows));
                    Set<Integer> twoSameSet = new HashSet<>(Arrays.asList(gameBoard.twoSame));
                    otherRowsSet.removeAll(twoSameSet);

                    // from sorted map, just keep the other rows
                    HashMap<Integer, Integer> sortedOthers = sortedMap.entrySet().stream()
                            .filter(x -> otherRowsSet.contains(x.getKey()))
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                    List<Integer> othersSticks = new ArrayList<>(sortedOthers.values());
                    int minOthers = othersSticks.get(0);
                    int maxOthers = othersSticks.get(1);
                    List<Integer> othersRows = new ArrayList<>(sortedOthers.keySet());
                    int maxRowOthers = othersRows.get(1);

                    choice = Arrays.asList(maxRowOthers, maxOthers - minOthers);
                    return choice;

                }
        }

        // the rest are isolated cases
        int minRow = RowOperations.getOrderAt("row", 1, sortedMap);
        int midRow = RowOperations.getOrderAt("row", 2, sortedMap);

        if (sortedList.equals(Arrays.asList(2, 4, 5))) {
            choice = Arrays.asList(minRow, 1);
            return choice;
        }
        else if (sortedList.equals(Arrays.asList(2, 4, 7))) {
            choice = Arrays.asList(maxRow, 1);
            return choice;
        }
        else if (sortedList.equals(Arrays.asList(2, 5, 6))) {
            choice = Arrays.asList(midRow, 1);
            return choice;
        }
        else if (sortedList.equals(Arrays.asList(3, 4, 5))) {
            choice = Arrays.asList(minRow, 2);
            return choice;
        }
        else if (sortedList.equals(Arrays.asList(3, 4, 6))) {
            choice = Arrays.asList(minRow, 1);
            return choice;
        }
        else if (sortedList.equals(Arrays.asList(3, 5, 7))) {
            choice = Arrays.asList(minRow, 1);
            return choice;
        }
        else if (sortedList.equals(Arrays.asList(1, 3, 4, 7))) {
            choice = Arrays.asList(maxRow, 1);
            return choice;
        }

        choice = Arrays.asList(maxRow, 1);
        return choice;
    }

}
