package com.example.marienbad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioGroup;

public class ComputerChoiceActivity extends AppCompatActivity {

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



    }
}
