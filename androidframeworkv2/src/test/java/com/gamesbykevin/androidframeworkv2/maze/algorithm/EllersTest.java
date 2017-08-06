package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.util.UtilityHelper;

import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Kevin on 8/5/2017.
 */

public class EllersTest extends Ellers {

    private static final int COLS = 10;
    private static final int ROWS = 10;

    public static boolean hexagon = true;

    public static Random RANDOM = new Random();

    public EllersTest() throws Exception {
        super(hexagon, COLS, ROWS);

        //flag debug true
        UtilityHelper.DEBUG = true;
    }

    @Test
    public void generate() {

        try {

            //create our maze
            EllersTest maze = new EllersTest();

            //keep generating until finished
            while (!maze.isGenerated()) {

                //assume not generated
                assertFalse(maze.isGenerated());

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