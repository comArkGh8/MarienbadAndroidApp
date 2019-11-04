package com.example.marienbad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

public class BoardActivity extends AppCompatActivity {

    private static int player; // 0 will be computer; 1 will be human
    private static List<int[]> rowSticksArray = new ArrayList<int[]>(); // of the form [[1,7], [2,5], [3,3], [4,1]]
    private static int[] select; // to keep track of selected sticks in rows
    private static boolean choiceIsInvalid;

    private static int[] zeroSelect = new int[4];

    /* Field to store our TextView */
    private TextView mWhoActionText;
    /* Field for the next Button */
    private Button mNextButton;

    public static boolean onStart;
    private static boolean nextButtonPressed;

    // method to set player
    public static void setPlayer(int playerInt){
        player = playerInt;
    }


    // make defensive copy of list
    public List<int[]> getRowSticksArray() {
        return Collections.unmodifiableList(this.rowSticksArray);
    }


    // TODO: I need replace this later with Java code!
    private int getNumberSticksInRow(int row){

        // find all checkBoxes with id of form row_stickId
        // and check how many are visible
        int number_in_row = 7 - 2*(row - 1);
        int number_visible = 0;
        for (int i = 1; i <= number_in_row; i++){
            // build id's
            String current_id = "checkBox" + row + "_" + i;
            int resID = getResources().getIdentifier(current_id, "id", getPackageName());
            CheckBox current_stick = (CheckBox) findViewById(resID);
            if (current_stick.getVisibility() == View.VISIBLE){
                number_visible++;
            }
        }

        return number_visible;
    }

    /**
     *
     * @param row
     * @param stickIndex
     * @param selection Must be a valid selection (at most one non-zero entry);
     * @return
     */
    private boolean stickIsSelected(int row, int stickIndex, int[] selection){
        // if all zeros return false
        if (noSticksSelected(selection)){
            return false;
        }

        // check if row corresponds to non-zero entry of selection
        if (selection[row-1] == 0){
            return false;
        }

        int numberSelected = selection[row-1];

        // first get rowSticksArray to check
        List<int[]> rowSticksCopy = getRowSticksArray();
        int mid_point = 5 - row;
        int number_in_row = rowSticksCopy.get(row - 1)[1];
        int left_hand = mid_point - number_in_row/2;
        int right_hand = left_hand + numberSelected - 1;

        if ( (stickIndex >= left_hand) && (stickIndex <= right_hand) ){
            return true;
        }


        return false;
    }



    public void setUpSticks(List<int[]> rowSticks, boolean fromNextButton, int[] selected){

        // make defensive copy; this is being way cautious
        List<int[]> rowSticksCopy = Collections.unmodifiableList(rowSticks);

        for (int rowIndex = 0; rowIndex < 4; rowIndex++){
            int row = rowSticksCopy.get(rowIndex)[0];
            int number_to_set = rowSticksCopy.get(rowIndex)[1];
            int number_in_row = 7 - 2*(row - 1);
            int mid_point = 5 - row;
            int left_hand = mid_point - number_to_set/2;
            int right_hand = left_hand + number_to_set -1;
            for (int i = 1; i <= number_in_row; i++){
                if (i < left_hand || i > right_hand){
                    // build id's
                    String current_id = "checkBox" + row + "_" + i;
                    int resID = getResources().getIdentifier(current_id, "id", getPackageName());
                    // get the stick
                    CheckBox current_stick = (CheckBox) findViewById(resID);
                    // set invisible
                    current_stick.setVisibility(View.INVISIBLE);
                    if (fromNextButton){
                        current_stick.setChecked(false);
                    }
                }
                else{
                    // a checked box may remain
                    // build id's
                    String current_id = "checkBox" + row + "_" + i;
                    int resID = getResources().getIdentifier(current_id, "id", getPackageName());
                    // get the stick
                    CheckBox current_stick = (CheckBox) findViewById(resID);
                    if (fromNextButton && (player == 1) ){
                        current_stick.setChecked(false);
                    }
                    else if ( (player == 0)) {
                        if (stickIsSelected(row, i, selected)){
                            current_stick.setChecked(true);
                        }
                        else{
                            current_stick.setChecked(false);
                        }
                    }



                }

            }

        }

        if (fromNextButton){
            nextButtonPressed = false;
            // reset select
            resetSelectionArray();
        }

    }

    private void initializeBoard(){
        int[] init1 = {1, 7};
        int[] init2 = {2, 5};
        int[] init3 = {3, 3};
        int[] init4 = {4, 1};

        rowSticksArray.add(init1);
        rowSticksArray.add(init2);
        rowSticksArray.add(init3);
        rowSticksArray.add(init4);

        // make defensive copy
        List<int[]> rowSticksCopy = getRowSticksArray();
        // no sticks selected
        setUpSticks(rowSticksCopy, true, zeroSelect);
    }

    private void setChecksOnSticks(int [] selectionArray){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        // if first time (from start) then setup with initial
        if (onStart) {
            initializeBoard();
            nextButtonPressed = true;
        }

        mWhoActionText = (TextView) findViewById(R.id.whose_action);
        mWhoActionText.setText(getString(R.string.player_announcement, player));

        mNextButton = (Button) findViewById(R.id.next_button);



        if (player == 0) {
            computerPlay();
        }
        else {
            humanPlay();
        }

        onStart = false;

        // TODO: finish; IM HERE!!!!
        // what happens when next_button is clicked
        // need to include the choiceIsInvalid variable
        mNextButton.setOnClickListener(new View.OnClickListener() {

            /**
             * The onClick method is triggered when this button (mDoSomethingCoolButton) is clicked.
             *
             * @param v The view that is clicked. In this case, it's mDoSomethingCoolButton.
             */
            @Override
            public void onClick(View v) {

                nextButtonPressed = true;


                // first check if all chosen sticks are in same row
                if (player == 1){
                    choiceIsInvalid = multipleRowsSelected() || noSticksSelected(select);
                }
                else {
                    choiceIsInvalid = false;
                }


                if (choiceIsInvalid) {

                    // make a toast with error message
                    Toast toast = Toast.makeText(getApplicationContext(),
                            " ",
                            Toast.LENGTH_SHORT);

                    if (multipleRowsSelected()) {
                        toast.setText(R.string.multiple_choice_error);
                    } else {
                        toast.setText(R.string.no_choice_error);
                    }

                    View view = toast.getView();

                    //Gets the TextView from the Toast so it can be edited
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.RED);

                    toast.show();

                    // setup screen again; reset selection array
                    resetSelectionArray();
                    // make defensive copy
                    List<int[]> rowSticksCopy = getRowSticksArray();
                    setUpSticks(rowSticksCopy, nextButtonPressed, zeroSelect);


                }
                else {
                    player = (player + 1) % 2;

                    // update screen
                    if (player == 0){
                        updateRowsSticksArray();
                        computerPlay();
                    }
                    else{
                        humanPlay();
                    }

                    // only reset if next button has been pressed
                    // because on create may be called from a rotation!
                    resetSelectionArray();

                }

                Class boardActivity = BoardActivity.class;
                //Intent startChildActivityIntent = new Intent(MainActivity.this, boardActivity);
                //startActivity(startChildActivityIntent);

            }
        });

    }


    //TODO: replace with code!!!!
    private int[] getComputerChoice() {
        int[] sampleArray = {1, 0, 0, 0};
        return sampleArray;
    }


    private void computerPlay(){

        mNextButton = (Button) findViewById(R.id.next_button);
        String buttonText;

        if (nextButtonPressed) {
            select = getComputerChoice();
        }

        nextButtonPressed = false;

        buttonText = "continue";
        mNextButton.setText(buttonText);

        // setup
        // make defensive copy
        List<int[]> rowSticksCopy = getRowSticksArray();
        setUpSticks(rowSticksCopy, false, select);

        updateRowsSticksArray();

    }


    private void humanPlay(){

        mNextButton = (Button) findViewById(R.id.next_button);
        String buttonText;

        // make defensive copy
        List<int[]> rowSticksCopy = getRowSticksArray();
        setUpSticks(rowSticksCopy, nextButtonPressed, zeroSelect);

        choiceIsInvalid = false;

        buttonText = "enter choice";
        mNextButton.setText(buttonText);
    }

    private void updateRowsSticksArray(){
        // get a defensive copy
        List<int[]> rowsAndSticks = this.getRowSticksArray();

        int rowChosenIndex = getRowSticksToRemove(select)[0] - 1;
        int numberSticksToRemove = getRowSticksToRemove(select)[1];

        // update
        int currentNumberSticks = rowsAndSticks.get(rowChosenIndex)[1];
        int updatedNumber = currentNumberSticks - numberSticksToRemove;

        // build new list
        rowSticksArray.get(rowChosenIndex)[1] = updatedNumber;

    }

    /**
     *
     * @param selection array of length 4 of form [sticks selected from row 1, from row 2, ..., from row 4]
     * @return array of form [row of selection, number of sticks selected]
     */
    private static int[] getRowSticksToRemove(int[] selection){
        int[] resultArray = new int[2];
        for (int i = 0; i < 4; i++){
            if (selection[i] > 0){
                resultArray[0] = i + 1;
                resultArray[1] = selection[i];
                return resultArray;
            }
        }

        return resultArray;
    }


    private boolean multipleRowsSelected(){
        boolean multiRow = false;
        int numRows = 0;

        for (int i = 0; i < 4; i++){
            if (select[i] > 0){
                numRows++;
            }
        }

        if (numRows > 1){
            multiRow = true;
        }

        return multiRow;
    }


    private boolean noSticksSelected(int[] selection){
        boolean noSticks = false;
        int totalSum = 0;

        for (int i = 0; i < 4; i++){
            totalSum += selection[i];
        }

        if (totalSum == 0){
            noSticks = true;
        }

        return noSticks;
    }

    private void incrementSelect(int rowIndex, boolean checked){
        if (checked) {
            select[rowIndex]++;
        }
        else{
            select[rowIndex]--;
        }
    }

    private void resetSelectionArray(){
        select = new int[4];
    }


    public void onRowOneCheckboxClicked(View cb) {
        boolean checked = ((CheckBox) cb).isChecked();
        incrementSelect(0, checked);

    }


    public void onRowTwoCheckboxClicked(View cb) {
        boolean checked = ((CheckBox) cb).isChecked();
        incrementSelect(1, checked);
    }


    public void onRowThreeCheckboxClicked(View cb) {
        boolean checked = ((CheckBox) cb).isChecked();
        incrementSelect(2, checked);
    }


    public void onRowFourCheckboxClicked(View cb) {
        boolean checked = ((CheckBox) cb).isChecked();
        incrementSelect(3, checked);
    }

}
