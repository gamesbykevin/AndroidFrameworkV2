package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.util.UtilityHelper;

import java.util.Random;

/**
 * Created by Kevin on 8/6/2017.
 */

public abstract class MazeTest {

    //object used to generate random events
    public static Random RANDOM = new Random();

    /**
     * Default list of scenarios for testing
     */
    public enum Scenario {

        Scenario1(true, 5, 7),
        Scenario2(false, 5, 7),
        Scenario3(true, 10, 10),
        Scenario4(false, 10, 10),
        Scenario5(false, 3, 3),
        Scenario6(true, 3, 3)
        ;

        private final boolean hexagon;
        private final int cols, rows;

        private Scenario(boolean hexagon, int cols, int rows) {
            this.hexagon = hexagon;
            this.cols = cols;
            this.rows = rows;
        }

        public boolean isHexagon() {
            return this.hexagon;
        }

        public int getCols() {
            return this.cols;
        }

        public int getRows() {
            return this.rows;
        }
    }

    public MazeTest() {
        UtilityHelper.DEBUG = true;
        UtilityHelper.UNIT_TEST = true;
    }
}