package com.example.marienbad;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class InstructionsActivity extends AppCompatActivity {


    /* Field to store our TextView */
    private TextView mDisplayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        /* Typical usage of findViewById... */
        mDisplayText = (TextView) findViewById(R.id.instructions_display);

        String instructText = "Instructions on how to play the game...";

        mDisplayText.setText(instructText);
    }

}
