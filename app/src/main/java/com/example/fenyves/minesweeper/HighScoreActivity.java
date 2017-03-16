package com.example.fenyves.minesweeper;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    List<Gamer> gamers;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

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

        Collections.sort(gamers, new Comparator<Gamer>() {
            @Override
            public int compare(Gamer g1, Gamer g2) {
                return (int) (g1.getTime() - g2.getTime());
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GamerAdapter((ArrayList<Gamer>) gamers);
        recyclerView.setAdapter(adapter);
    }

}
