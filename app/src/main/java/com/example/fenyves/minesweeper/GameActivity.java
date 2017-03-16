package com.example.fenyves.minesweeper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private List<Button> buttons = new ArrayList<>();
    private MineSweeper mineSweeper;

    private long startTime;
    private List<Gamer> gamers;

    TextView mineNums;

    private final static String SAVED = "saved";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);


        if (savedInstanceState != null) {
            mineSweeper = (MineSweeper) savedInstanceState.getSerializable(SAVED);
        } else {
            mineSweeper = new MineSweeper();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;

        int buttonSize;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            buttonSize = screenWidth / mineSweeper.getWidth();
            LinearLayout gameLayout = (LinearLayout) findViewById(R.id.game_layout);
            gameLayout.setOrientation(LinearLayout.VERTICAL);
        } else {
            buttonSize = (screenHeight - screenHeight / 5) / mineSweeper.getHigh();
            LinearLayout gameLayout = (LinearLayout) findViewById(R.id.game_layout);
            gameLayout.setOrientation(LinearLayout.HORIZONTAL);
        }

        //Create the UI, set onClickListener to the buttons to the game
        LinearLayout rowContainer = (LinearLayout) findViewById(R.id.rowsContainer);
        mineNums = (TextView) findViewById(R.id.mines_out);
        mineNums.setText(String.valueOf(mineSweeper.minesLeft()));
        for (int i = 0; i < mineSweeper.getHigh(); i++) {

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            row.setWeightSum((float) mineSweeper.getWidth());


            for (int j = 0; j < mineSweeper.getWidth(); j++) {
                Button button = new Button(this);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonSize, buttonSize);
                params.weight = 1;

                GradientDrawable gd = new GradientDrawable();
                gd.setColor(0xFFC0C0C0);
                gd.setCornerRadius(5);
                gd.setStroke(1, 0xFF000000);

                button.setLayoutParams(params);
                button.setBackground(gd);

                buttons.add(button);

                final int finalJ = j;
                final int finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mineSweeper.get_game_play_in_coordinates(finalJ, finalI) == MineSweeper.FLAG)
                            return;
                        if (mineSweeper.get_game_play_in_coordinates(finalJ, finalI) != MineSweeper.UNKNOWN)
                            return;


                        mineSweeper.discover(finalJ, finalI);

                        String play = "on";
                        if (mineSweeper.player_won()) {
                            mineSweeper.discover_all();
                            game_end(true);
                            play = "won";
                        } else if (mineSweeper.player_lost()) {
                            mineSweeper.discover_all();
                            game_end(false);
                            play = "lost";
                        }

                        redraw_buttons(play);
                    }
                });
                button.setLongClickable(true);
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mineSweeper.setFlag(finalJ, finalI);

                        mineNums.setText(String.valueOf(mineSweeper.minesLeft()));

                        redraw_buttons("on");
                        return true;
                    }
                });

                row.addView(button);

            }
            rowContainer.addView(row);
        }


        try {
            FileInputStream fis = getApplicationContext().openFileInput(MineSweeper.fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            gamers = (ArrayList<Gamer>) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Log.d("solution", "onCreate: " + mineSweeper.print_sol());
        startTime = System.currentTimeMillis();
        redraw_buttons("on");

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(SAVED, mineSweeper);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void redraw_buttons(String win) {
        //Revealed button
        GradientDrawable revealed = new GradientDrawable();
        revealed.setColor(0xFFFFFFFF);
        revealed.setCornerRadius(5);
        revealed.setStroke(1, 0xFF000000);

        //Mine Button
        GradientDrawable mineButton = new GradientDrawable();
        if (win.equals("lost")) {
            mineButton.setColor(0xFFFF0000);
        } else {
            mineButton.setColor(0xFF00FF00);

        }
        mineButton.setCornerRadius(5);
        mineButton.setStroke(1, 0xFF000000);

        for (int i = 0; i < mineSweeper.getHigh(); i++) {
            for (int j = 0; j < mineSweeper.getWidth(); j++) {
                int solution = mineSweeper.get_game_play_in_coordinates(j, i);

                switch (solution) {
                    case MineSweeper.FLAG:
                        buttons.get(i * mineSweeper.getWidth() + j).setText("X");
                        break;
                    case MineSweeper.MINE:
                        buttons.get(i * mineSweeper.getWidth() + j).setText("X");
                        buttons.get(i * mineSweeper.getWidth() + j).setClickable(false);
                        buttons.get(i * mineSweeper.getWidth() + j).setFocusable(false);
                        buttons.get(i * mineSweeper.getWidth() + j).setBackground(mineButton);
                        break;
                    case 0:
                        buttons.get(i * mineSweeper.getWidth() + j).setClickable(false);
                        buttons.get(i * mineSweeper.getWidth() + j).setFocusable(false);
                        buttons.get(i * mineSweeper.getWidth() + j).setBackground(revealed);
                    case MineSweeper.UNKNOWN:
                        buttons.get(i * mineSweeper.getWidth() + j).setText("");
                        break;
                    default:
                        buttons.get(i * mineSweeper.getWidth() + j).setText(String.valueOf(solution));
                        buttons.get(i * mineSweeper.getWidth() + j).setClickable(false);
                        buttons.get(i * mineSweeper.getWidth() + j).setFocusable(false);
                        buttons.get(i * mineSweeper.getWidth() + j).setBackground(revealed);
                        break;
                }


            }
        }
    }

    private void game_end(boolean win) {

        if (win) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

            LinearLayout end_layout = (LinearLayout) findViewById(R.id.endContainer);
            LinearLayout win_layout = (LinearLayout) inflater.inflate(R.layout.content_win, (ViewGroup) findViewById(R.id.win_id));

            end_layout.addView(win_layout);

            TextView time_out = (TextView) findViewById(R.id.yourTimeOut);
            final TextInputEditText name_in = (TextInputEditText) findViewById(R.id.name_input);
            Button submit = (Button) findViewById(R.id.submit_button);

            long endTime = System.currentTimeMillis();
            final long sec = (endTime - startTime) / 1000;

            String time = (sec / 60) + ":" + (sec % 60);

            time_out.setText(time);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(name_in.getText().equals(""))
                        return;
                    String name = String.valueOf(name_in.getText());
                    if (gamers == null)
                        gamers = new ArrayList<>();

                    gamers.add(new Gamer(name, sec));
                    onBackPressed();
                    try {
                        FileOutputStream fos = getApplicationContext().openFileOutput(MineSweeper.fileName, Context.MODE_PRIVATE);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(gamers);
                        os.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

            LinearLayout end_layout = (LinearLayout) findViewById(R.id.endContainer);
            LinearLayout lose_layout = (LinearLayout) inflater.inflate(R.layout.content_lose, (ViewGroup) findViewById(R.id.lose_id));

            end_layout.addView(lose_layout);

            Button backButton = (Button) findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }
}
