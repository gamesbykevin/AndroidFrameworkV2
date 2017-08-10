package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.util.UtilityHelper;

import org.junit.Test;

import java.util.Random;

import static com.gamesbykevin.androidframeworkv2.maze.algorithm.MazeTest.COLS;
import static com.gamesbykevin.androidframeworkv2.maze.algorithm.MazeTest.HEXAGON;
import static com.gamesbykevin.androidframeworkv2.maze.algorithm.MazeTest.RANDOM;
import static com.gamesbykevin.androidframeworkv2.maze.algorithm.MazeTest.ROWS;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Kevin on 8/5/2017.
 */

public class GrowingTreeTest extends GrowingTree {

    public GrowingTreeTest() throws Exception {
        super(HEXAGON, COLS, ROWS);

        //flag debug true
        UtilityHelper.DEBUG = true;
    }

    @Test
    public void generate() {
        try {

            //create our maze
            GrowingTreeTest maze = new GrowingTreeTest();

            //keep generating until finished
            while (!maze.isGenerated()) {

                //assume not generated
                assertFalse(maze.isGenerated());

                UtilityHelper.logEvent("Maze generation progress: " + maze.getProgress().getProgress());

                //generate more
                maze.update(RANDOM);
            }

            //assume maze is generated
            assertTrue(maze.isGenerated());

        } catch (Exception e) {
            UtilityHelper.handleException(e);
        }
    }
}