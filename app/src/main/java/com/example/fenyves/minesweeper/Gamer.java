package com.example.fenyves.minesweeper;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Fenyves on 2016. 12. 11..
 */
public class Gamer implements Serializable {
    private String name;
    private long time;

    public Gamer(String name, long sec) {
        this.name = name;
        this.time = sec;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
