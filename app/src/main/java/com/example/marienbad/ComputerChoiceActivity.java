package com.example.marienbad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ComputerChoiceActivity extends AppCompatActivity {



    /* Field for the start game Button */
    private Button mStartGameButton;



    // makes sure at least two player or against computer is checked
    private boolean choicesAreMade(){

        RadioGroup firstPlayerGroup = (RadioGroup) findViewById(R.id.player_choice_radio_group);
        if (firstPlayerGroup.getCheckedRadioButtonId() == -1){
            return false;
        }

        RadioGroup gameLevelGroup = (RadioGroup) findViewById(R.id.difficulty_level_radio_group);
        if (gameLevelGroup.getCheckedRadioButtonId() == -1){
            return false;
        }

        return true;
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_choice);


        // store the first move player from the radio buttons
        RadioGroup player_choice_rg = (RadioGroup) findViewById(R.id.player_choice_radio_group);

        player_choice_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

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

        // store the game_type from the radio buttons
        RadioGroup difficulty_level_rg = (RadioGroup) findViewById(R.id.difficulty_level_radio_group);

        difficulty_level_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.easy_button:
                        BoardActivity.setLevel(1);
                        break;
                    case R.id.moderate_button:
                        BoardActivity.setLevel(2);
                        break;
                    case R.id.expert_button:
                        BoardActivity.setLevel(3);
                        break;

                }
            }
        });


        // now to handle the start button
        mStartGameButton = (Button) findViewById(R.id.start_game_button);
        mStartGameButton.setOnClickListener(new View.OnClickListener() {

            /**
             * The onClick method is triggered when this button (mDoSomethingCoolButton) is clicked.
             *
             * @param v The view that is clicked. In this case, it's mDoSomethingCoolButton.
             */
            @Override
            public void onClick(View v) {


                if (!choicesAreMade()){
                    // make a toast with error message
                    Toast toast = Toast.makeText(getApplicationContext(),
                            " ",
                            Toast.LENGTH_SHORT);

                    toast.setText(R.string.no_first_or_level_choices_error);
                    toast.show();

                }
                else{
                    Class boardActivity = BoardActivity.class;
                    Intent startChildActivityIntent = new Intent(ComputerChoiceActivity.this, boardActivity);
                    startActivity(startChildActivityIntent);

                    BoardActivity.onStart = true;

                }


            }
        });



    }
}
