package com.example.marienbad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    /* Field for the instructions Button */
    private Button mInstructButton;
    /* Field for the start game Button */
    private Button mStartButton;

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


        // store the first move player from the radio buttons
        RadioGroup rg = (RadioGroup) findViewById(R.id.player_choice_radio_group);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.computer_button:
                        BoardActivity.setPlayer(0);
                        break;
                    case R.id.person_button:
                        BoardActivity.setPlayer(1);
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
                Class boardActivity = BoardActivity.class;
                Intent startChildActivityIntent = new Intent(MainActivity.this, boardActivity);
                startActivity(startChildActivityIntent);

                // TODO: initialize board here with rowSticksArray
                BoardActivity.onStart = true;
            }
        });








    }
}
