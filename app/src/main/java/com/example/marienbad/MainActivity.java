package com.example.marienbad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /* Field for the instructions Button */
    private Button mInstructButton;
    /* Field for the start game Button */
    private Button mStartButton;


    // makes sure at least two player or against computer is checked
    private boolean gameTypeChoiceIsMade(){

        RadioGroup compOrTwoGroup = (RadioGroup) findViewById(R.id.game_type_radio_group);
        if (compOrTwoGroup.getCheckedRadioButtonId() == -1){
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our Button from xml. This allows us to
         * do things like set the onClickListener which determines what happens when the button
         * is clicked.
         */
        mInstructButton = (Button) findViewById(R.id.instructions_button);

        /* Setting an OnClickListener allows us to do something when this button is clicked. */
        mInstructButton.setOnClickListener(new View.OnClickListener() {

            /**
             * The onClick method is triggered when this button (mDoSomethingCoolButton) is clicked.
             *
             * @param v The view that is clicked. In this case, it's mDoSomethingCoolButton.
             */
            @Override
            public void onClick(View v) {
                /*
                 * Storing the Context in a variable in this case is redundant since we could have
                 * just used "this" or "MainActivity.this" in the method call below. However, we
                 * wanted to demonstrate what parameter we were using "MainActivity.this" for as
                 * clear as possible.
                 */
                Context context = MainActivity.this;

                /* This is the class that we want to start (and open) when the button is clicked. */
                Class instructionActivity = InstructionsActivity.class;

                /*
                 * Here, we create the Intent that will start the Activity we specified above in
                 * the destinationActivity variable. The constructor for an Intent also requires a
                 * context, which we stored in the variable named "context".
                 */
                Intent startChildActivityIntent = new Intent(context, instructionActivity);

                /*
                 * Once the Intent has been created, we can use Activity's method, "startActivity"
                 * to start the ChildActivity.
                 */
                startActivity(startChildActivityIntent);
            }
        });



        // store the game_type from the radio buttons
        RadioGroup game_type_rg = (RadioGroup) findViewById(R.id.game_type_radio_group);

        game_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.play_computer_button:
                        BoardActivity.setGamePlay("computer");
                        break;
                    case R.id.two_player_button:
                        BoardActivity.setGamePlay("two_player");
                        break;

                }
            }
        });





        // now to handle the start button
        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {

            /**
             * The onClick method is triggered when this button (mDoSomethingCoolButton) is clicked.
             *
             * @param v The view that is clicked. In this case, it's mDoSomethingCoolButton.
             */
            @Override
            public void onClick(View v) {


                if (!gameTypeChoiceIsMade()){
                    // make a toast with error message
                    Toast toast = Toast.makeText(getApplicationContext(),
                            " ",
                            Toast.LENGTH_SHORT);

                    toast.setText(R.string.no_game_choice_error);
                    toast.show();

                }
                else{
                    Class childActivity = BoardActivity.class;

                    RadioButton compRadio = (RadioButton) findViewById(R.id.play_computer_button);
                    // find which type of game
                    boolean againstComputer = compRadio.isChecked();


                    if (againstComputer){
                        childActivity = ComputerChoiceActivity.class;
                    }
                    else{
                        BoardActivity.setPlayer(0);
                    }

                    Intent startChildActivityIntent = new Intent(MainActivity.this, childActivity);
                    startActivity(startChildActivityIntent);

                    BoardActivity.onStart = true;

                }

            }
        });








    }
}
