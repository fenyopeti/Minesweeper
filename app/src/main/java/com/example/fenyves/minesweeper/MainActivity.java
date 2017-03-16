package com.example.fenyves.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button newGameButton;
    Button highScoreButton;
    Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGameButton = (Button) findViewById(R.id.newGameButton);
        highScoreButton = (Button) findViewById(R.id.highScoreButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGameIntent = new Intent();
                newGameIntent.setClass(MainActivity.this, GameActivity.class);
                startActivity(newGameIntent);
            }
        });

        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent highScoreIntent = new Intent();
                highScoreIntent.setClass(MainActivity.this, HighScoreActivity.class);
                startActivity(highScoreIntent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutIntent = new Intent();
                aboutIntent.setClass(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
            }
        });

    }
}

