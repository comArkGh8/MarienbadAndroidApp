package com.example.marienbad.computer_strategy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 # this class of methods looks at all the possible losing hands
 # in the game of Marienbad.  grouped according to
 # number of non-zero rows, they are:
 # row = 1: 1
 #
 # case row = 2:
 #   if two are same (and not 1), then loses, else wins
 #
 # case row = 3:
 #   if smallest = 1: next are sum == 3 or 6
 #   1,1,1
 #   3,2,1
 #   next two are separate special cases
 #   5,4,1
 #   6,4,2
 #   next are sum = 14
 #   7,5,2
 #   7,4,3
 #   6,5,3
 #
 # case row = 4:
 #   n,n,1,1 (sum = 2*low + 2*high)
 #   next are covered by sum == 14 or 16
 #   7,4,2,1
 #   6,5,2,1
 #   6,4,3,1
 #   7,5,3,1
 */

public class LosingSituations {

    /**
     *
     * @param stick_array - has to be ordered in increasing number of sticks
     * @return
     */

    public static boolean stickArrayIsLoser(List<Integer> stick_array) {

        int sumOfSticks = stick_array.stream()
                .reduce(0, Integer::sum);
        if (sumOfSticks == 1) {
            return true;
        }

        // else we handle the situation in cases
        // the next belongs to 3 or 4 rows
        if ((sumOfSticks == 14 && (!RowOperations.arrayHasRepeatedRow(stick_array))) || (sumOfSticks == 16)) {
            return true;
        }

        int numberNonZero = RowOperations.getNumberNonZeroRows(stick_array);

        switch (numberNonZero) {

            case 2:
                if ((stick_array.get(0) > 1) && (RowOperations.arrayHasRepeatedRow(stick_array))) {
                    return true;
                }
                else {
                    return false;
                }

            case 3:
                // express stick_array as set and compare to set(1,4,5) and set(2,4,6)
                Set<Integer> stickSet = new HashSet<>(stick_array);

                Set<Integer> compareSetOne = new HashSet<Integer>();
                compareSetOne.addAll( Arrays.asList( 1, 4, 5));

                Set<Integer> compareSetTwo = new HashSet<Integer>();
                compareSetTwo.addAll( Arrays.asList( 2, 4, 6));

                if (stickSet.equals(compareSetOne) || stickSet.equals(compareSetTwo)) {
                    return true;
                }
                else if ( (stick_array.get(0) == 1) && (sumOfSticks == 3)   || (sumOfSticks == 6) ) {
                    return true;
                }
                else {
                    return false;
                }


            case 4:
                if (sumOfSticks == (2*stick_array.get(0) + 2*stick_array.get(2))) {
                    return true;
                }
                else {
                    return false;
                }
        }


        return false;

    }




}
