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

import com.example.marienbad.computer_strategy.ChoiceOperations;
import com.example.marienbad.computer_strategy.MarienbadBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;

public class BoardActivity extends AppCompatActivity {

    private static MarienbadBoard gameBoard;

    private static int player; // 0 will be computer; 1 will be human
    private static String gameType; // either two_player or computer
    private static int gameLevel; // 1 = easy; 2 = moderate; 3 = difficult

    private static List<Integer> rowSticksArray = new ArrayList<Integer>(); // of the form [7, 5, 3, 1]

    private static int[] select; // to keep track of selected sticks in rows
    private static boolean choiceIsInvalid;

    private static int[] zeroSelect = new int[4];

    /* Field to store our TextView */
    private TextView mWhoActionText;
    /* Field for the next Button */
    private Button mNextButton;
    /* Field for Game Over annoucement */
    private TextView mGameOverText;

    public static boolean onStart;
    private static boolean nextButtonPressed;

    private static boolean endOfGame =  false;



    // method to set player
    public static void setPlayer(int playerInt){
        player = playerInt;
    }

    // method to set game type
    public static void setGamePlay (String type) { gameType = type; }

    // method to set game difficulty level
    public static void setLevel (int level) { gameLevel = level; }


    // make defensive copy of list
    public List<Integer> getRowSticksArray() {
        return Collections.unmodifiableList(this.rowSticksArray);
    }

    // probably should have only worked with Lists instead of additional
    // introduction of arrays, here is a (temp) work around

    private static int[] convertListIntegerToIntArray(List<Integer> list){
        // first get defensive copy of list
        List<Integer> listCopy = Collections.unmodifiableList(list);
        Integer[] stickArray = listCopy.toArray(new Integer[listCopy.size()]);
        int[] intStickArray = Arrays.stream(stickArray).mapToInt(Integer::intValue).toArray();
        return intStickArray;
    }

    private static boolean gameIsOver(List<Integer> stickList){
        int[] stickArray = convertListIntegerToIntArray(stickList);
        int totalSum = getSumOfSticks(stickArray);
        if (totalSum == 1){
            return true;
        }
        return false;
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
        List<Integer> rowSticksCopy = getRowSticksArray();
        int mid_point = 5 - row;
        int number_in_row = rowSticksCopy.get(row - 1);
        int left_hand = mid_point - number_in_row/2;
        int right_hand = left_hand + numberSelected - 1;

        if ( (stickIndex >= left_hand) && (stickIndex <= right_hand) ){
            return true;
        }


        return false;
    }

    public void setUpSticks(List<Integer> rowSticks, boolean fromNextButton, int[] selected){

        // make defensive copy; this is being way cautious
        List<Integer> rowSticksCopy = Collections.unmodifiableList(rowSticks);

        for (int rowIndex = 0; rowIndex < 4; rowIndex++){
            int row = rowIndex+1;
            int number_to_set = rowSticksCopy.get(rowIndex);
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
        rowSticksArray.add(7);
        rowSticksArray.add(5);
        rowSticksArray.add(3);
        rowSticksArray.add(1);

        gameBoard = new MarienbadBoard();

        // make defensive copy
        List<Integer> rowSticksCopy = getRowSticksArray();
        // no sticks selected
        setUpSticks(rowSticksCopy, true, zeroSelect);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntArray("selection", select);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);


        // if reloaded just need select array
        if (savedInstanceState != null) {
            select = savedInstanceState.getIntArray("selection");
        }
        else{
            resetSelectionArray();
        }


        // if first time (from start) then setup with initial
        if (onStart) {
            initializeBoard();
            nextButtonPressed = true;
        }

        mWhoActionText = (TextView) findViewById(R.id.whose_action);
        mWhoActionText.setText(getString(R.string.player_announcement, player));

        mNextButton = (Button) findViewById(R.id.next_button);

        mGameOverText= (TextView) findViewById(R.id.game_over_text);


        if (endOfGame){
            // setup sticks
            List<Integer> rowSticksCopy = getRowSticksArray();
            setUpSticks(rowSticksCopy, nextButtonPressed, zeroSelect);
            // hide next button and set game over text
            mNextButton.setVisibility(View.INVISIBLE);
            mGameOverText.setText(R.string.game_over_announcement);
        }

        nextButtonPressed = false;

        if (gameType.equals("computer")){
            if (player == 0) {
                computerPlay();
            }
            else {
                humanPlay();
            }
        }
        else if (gameType.equals("two_player")){
            humanPlay();
        }



        onStart = false;

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
                if (gameType.equals("computer")){
                    if (player == 1){
                        choiceIsInvalid = multipleRowsSelected() || noSticksSelected(select);
                    }
                    else {
                        choiceIsInvalid = false;
                    }
                }
                else if (gameType.equals("two_player")){
                    choiceIsInvalid = multipleRowsSelected() || noSticksSelected(select);
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
                    List<Integer> rowSticksCopy = getRowSticksArray();
                    setUpSticks(rowSticksCopy, nextButtonPressed, zeroSelect);


                }
                else {
                    // update board view
                    updateRowsSticksArray();
                    // update Marienbad gameBoard
                    int rowChosen= getRowSticksToRemove(select)[0];
                    int numberSticksToRemove = getRowSticksToRemove(select)[1];
                    gameBoard.change(rowChosen, numberSticksToRemove);

                    List<Integer> stickRowList = getRowSticksArray();
                    endOfGame = gameIsOver(stickRowList);
                    if (endOfGame){
                        // setup sticks
                        List<Integer> rowSticksCopy = getRowSticksArray();
                        setUpSticks(rowSticksCopy, nextButtonPressed, zeroSelect);
                        // hide next button and set game over text
                        mNextButton.setVisibility(View.INVISIBLE);
                        mGameOverText.setText(R.string.game_over_announcement);
                    }
                    else{
                        player = (player + 1) % 2;
                        if (gameType.equals("computer")){
                            if (player == 0){
                                computerPlay();
                            }
                            else{
                                humanPlay();
                            }
                        }
                        else if (gameType.equals("two_player")){
                            humanPlay();
                        }
                    }








                }

                Class boardActivity = BoardActivity.class;
                //Intent startChildActivityIntent = new Intent(MainActivity.this, boardActivity);
                //startActivity(startChildActivityIntent);

            }
        });

    }


    // This can be cleaned up
    private int[] getComputerChoice() {
        int[] choiceArray = new int[4];
        List<Integer> choiceList = new ArrayList<>();
        if (gameLevel == 1){
            // easy level; get random choice 8 times out of 10
            Random r = new Random();
            int randomValue = r.nextInt(10);
            if (randomValue < 8 ){
                // get random choice
                choiceList = ChoiceOperations.randomChoice(gameBoard);
            }
            else{
                choiceList = ChoiceOperations.bestChoice(gameBoard);
            }
            int rowIndex = choiceList.get(0) - 1;
            int numberSticks = choiceList.get(1);
            choiceArray[rowIndex] = numberSticks;
        }
        else if (gameLevel == 2){
            // moderate level; get random choice 3 times out of 10
            Random r = new Random();
            int randomValue = r.nextInt(10);
            if (randomValue < 3 ){
                // get random choice
                choiceList = ChoiceOperations.randomChoice(gameBoard);
            }
            else{
                choiceList = ChoiceOperations.bestChoice(gameBoard);
            }
            int rowIndex = choiceList.get(0) - 1;
            int numberSticks = choiceList.get(1);
            choiceArray[rowIndex] = numberSticks;
        }
        else if (gameLevel == 3){
            // expert level
            choiceList = ChoiceOperations.bestChoice(gameBoard);

            int rowIndex = choiceList.get(0) - 1;
            int numberSticks = choiceList.get(1);
            choiceArray[rowIndex] = numberSticks;
        }

        return choiceArray;
    }


    private void computerPlay(){

        mNextButton = (Button) findViewById(R.id.next_button);
        String buttonText;

        if (nextButtonPressed || onStart) {
            select = getComputerChoice();
        }

        buttonText = "continue";
        mNextButton.setText(buttonText);

        // setup
        // make defensive copy
        List<Integer> rowSticksCopy = getRowSticksArray();
        setUpSticks(rowSticksCopy, false, select);


    }


    private void humanPlay(){

        mNextButton = (Button) findViewById(R.id.next_button);
        String buttonText;

        // make defensive copy
        List<Integer> rowSticksCopy = getRowSticksArray();
        setUpSticks(rowSticksCopy, nextButtonPressed, zeroSelect);

        choiceIsInvalid = false;

        buttonText = "enter choice";
        mNextButton.setText(buttonText);
    }

    private void updateRowsSticksArray(){
        // get a defensive copy
        List<Integer> rowsAndSticks = getRowSticksArray();

        int rowChosenIndex = getRowSticksToRemove(select)[0] - 1;
        int numberSticksToRemove = getRowSticksToRemove(select)[1];

        // update
        int currentNumberSticks = rowsAndSticks.get(rowChosenIndex);
        int updatedNumber = currentNumberSticks - numberSticksToRemove;

        // build new list
        rowSticksArray.set(rowChosenIndex, updatedNumber);


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

    // note this method is also in RowOperations
    // here it is applied with an array as input
    private static int getSumOfSticks(int[] selection){
        int totalSum = 0;

        for (int i = 0; i < 4; i++){
            totalSum += selection[i];
        }
        return totalSum;
    }


    private boolean noSticksSelected(int[] selection){
        boolean noSticks = false;
        int totalSum = getSumOfSticks(selection);

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
