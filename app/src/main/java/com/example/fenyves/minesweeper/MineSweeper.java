package com.example.fenyves.minesweeper;

import android.util.Log;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Fenyves on 2016. 12. 08..
 */
public class MineSweeper implements Serializable{
    public static final int MINE = -1;
    public static final int FLAG = -2;
    public static final int UNKNOWN = -3;

    public static final String fileName = "scores";


    private int[][] solution;
    private int[][] game_play;

    private int high, width, mines, flags;


    public MineSweeper() {
        high = 9;
        width = 9;
        mines = 9;
        flags = 0;
        solution = new int[high][width];
        game_play = new int[high][width];

        for (int i = 0; i < high; i++) {
            for (int j = 0; j < width; j++) {
                game_play[j][i] = UNKNOWN;
            }
        }

        bomb_placing();
        numbers_placing();
    }

    //region Getter/Setter
    public int getHigh() {
        return high;
    }

    public int getWidth() {
        return width;
    }

    public int minesLeft() {
        return mines - flags;
    }

    public int get_game_play_in_coordinates(int x, int y) {
        return game_play[y][x];
    }

    public int[][] getGame_play() {
        return game_play;
    }

    public void setFlag(int x, int y) {
        if (game_play[y][x] == FLAG) {
            game_play[y][x] = UNKNOWN;
            flags--;
        } else if (game_play[y][x] == UNKNOWN) {
            flags++;
            game_play[y][x] = FLAG;
        }
    }
    //endregion

    //region private methods

    private void bomb_placing() { //The bomb has been planted, terrorists win
        Random rand = new Random();
        int x, y;
        for (int i = 0; i < mines; i++) {
            x = rand.nextInt(width - 1);
            y = rand.nextInt(high - 1);

            if (solution[y][x] == MINE)
                i--;
            else
                solution[y][x] = MINE;
        }
    }

    private void numbers_placing() {
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < width; j++) {
                if (solution[j][i] != MINE) {
                    solution[j][i] = bombs_neighboring(j, i);
                }
            }
        }
    }

    private int bombs_neighboring(int x, int y) {
        int neighboring_bombs = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((!out_of_bound(i, j)) && solution[i][j] == MINE) {
                    neighboring_bombs++;
                }
            }
        }
        return neighboring_bombs;
    }

    private boolean out_of_bound(int x, int y) {
        return x < 0 || x >= width || y < 0 || y >= high;
    }
    //endregion


    public void discover(int x, int y) {
        if (out_of_bound(x, y))
            return;
        if (solution[y][x] != 0) {
            if (solution[y][x] == MINE)
                Log.d("asd", "akna vót gecó");
            game_play[y][x] = solution[y][x];
            return;
        }
        if (game_play[y][x] == FLAG)
            return;
        if (game_play[y][x] != UNKNOWN)
            return;

        game_play[y][x] = solution[y][x];

        discover(x - 1, y);
        discover(x - 1, y - 1);
        discover(x - 1, y + 1);
        discover(x + 1, y);
        discover(x + 1, y - 1);
        discover(x + 1, y + 1);
        discover(x, y + 1);
        discover(x, y - 1);
    }

    public boolean player_won() {
        boolean equals = true;
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < width; j++) {
                if (solution[j][i] != MINE && solution[j][i] != game_play[j][i])
                    equals = false;
            }
        }

        return equals;
    }

    public void discover_all() {
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < width; j++) {
                game_play[j][i] = solution[j][i];
            }
        }
    }

    //region debug help
    public String print_sol() {
        String sol = "";
        for (int i = 0; i < getHigh(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                sol += Integer.toString(solution[j][i]);
            }
            sol += "\n";
        }
        return sol;
    }

    public String print_game() {
        String sol = "";
        for (int i = 0; i < getHigh(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                sol += Integer.toString(game_play[j][i]);
            }
            sol += "\n";
        }
        return sol;
    }

    public boolean player_lost() {
        boolean has_mine = false;
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < width; j++) {
                if (game_play[j][i] == MINE)
                    has_mine = true;
            }
        }

        return has_mine;
    }


    //endregion
}
