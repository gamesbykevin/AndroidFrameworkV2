package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;

import java.util.Random;

/**
 * Created by Kevin on 8/6/2017.
 */

public abstract class MazeTest extends Maze {

    //size of maze
    public static final int COLS = 5;
    public static final int ROWS = 10;

    //is maze hexagon
    public static final boolean HEXAGON = false;

    //object used to generate random events
    public static Random RANDOM = new Random();

    public MazeTest() throws Exception {
        super(HEXAGON, COLS, ROWS);
    }

    public abstract void update(Random random);
}
