package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import android.util.Log;

import com.gamesbykevin.androidframeworkv2.util.DebugHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Kevin on 8/5/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class RecursiveBacktrackingTest extends MazeTest {

    @Test
    public void generate() throws Exception {

        PowerMockito.mockStatic(Log.class);

        for (MazeTest.Scenario scenario : MazeTest.Scenario.values()) {

            //create our maze
            RecursiveBacktracking maze = new RecursiveBacktracking(scenario.isHexagon(), scenario.getCols(), scenario.getRows());

            //keep generating until finished
            while (!maze.isGenerated()) {

                //assume not generated
                assertFalse(maze.isGenerated());

                DebugHelper.logEvent("Maze generation progress: RecursiveBacktracking " + maze.getProgress().getProgress());

                //generate more
                maze.update(RANDOM);
            }

            //assume maze is generated
            assertTrue(maze.isGenerated());
        }
    }
}