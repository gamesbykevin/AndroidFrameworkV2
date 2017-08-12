package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Aldous-Broder maze generation algorithm
 * @author GOD
 */
public class AldousBroder extends Maze
{
    //track current location
    private int col, row;
    
    //keep track of failed count
    private int count = 0;
    
    /**
     * The maximum number of time allowed to fail finding an unvisited room
     */
    private int failedAttemptsLimit = 0;
    
    /**
     * Once progress of the maze passes this completion ratio<br>
     * It will become more difficult to locate an unvisited room.<br>
     * This will help speed up the process.
     */
    private static final float LOCATE_TARGET_PROGRESS_RATIO = .7f;
    
    //temporary object used to generate maze
    private List<Room> options;

    public AldousBroder(final boolean hexagon, final int cols, final int rows) throws Exception {

        super(hexagon, cols, rows);
        
        //add walls to each room
        super.populateRooms();
        
        //the limit will be determined by the size of the maze
        this.failedAttemptsLimit = ((cols * rows) / 2);
        
        //create new list(s)
        this.options = new ArrayList<>();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        options.clear();
        options = null;
    }
    
    /**
     * Create our maze
     * @param random Object used to make random decisions
     * @throws Exception
     */
    @Override
    public void update(final Random random) throws Exception {
        //if generated no need to continue
        if (isGenerated())
            return;
        
        //if we haven't visited any rooms, this is the first time pick a random location
        if (!MazeHelper.hasVisited(this)) {
            col = random.nextInt(getCols());
            row = random.nextInt(getRows());
        }
        
        //our temporary room
        Room room = getRoom(col, row);

        //make sure the list is empty
        options.clear();

        /**
         * If we are close to finishing the maze...
         * or if we have reached the number of failed attempts limit<br>
         *
         * Lets target any existing unvisited rooms, to help complete the maze
         */
        if (getProgress().getProgress() >= LOCATE_TARGET_PROGRESS_RATIO || count > failedAttemptsLimit) {

            //check all walls
            for (Room.Wall wall : Room.getAllWalls(isHexagon())) {

                //get the neighbor
                Room tmp = getRoomNeighbor(room, wall);

                if (tmp != null && !tmp.hasVisited())
                    options.add(tmp);
            }
        }

        //if we still don't have any options
        if (options.isEmpty())
        {
            //increase the count
            count++;

            //check all walls
            for (Room.Wall wall : Room.getAllWalls(isHexagon())) {

                Room tmp = getRoomNeighbor(col, row, wall);

                if (tmp != null)
                    options.add(tmp);
            }
        }
        
        //now pick a random room
        Room tmp = options.get(random.nextInt(options.size()));
        
        //assign the new location
        col = tmp.getCol();
        row = tmp.getRow();
        
        //if the room has not yet been visited we will join
        if (!tmp.hasVisited())
        {
            //reset the counter since we found an unvisited room
            count = 0;
            
            //mark the rooms as visited
            room.setVisited(true);
            tmp.setVisited(true);
            
            //join the rooms
            MazeHelper.joinRooms(isHexagon(), room, tmp);
        }
        else
        {
            //if we reached the limit of failed attempts
            if (count > failedAttemptsLimit)
            {
                //place at room next to unvisited room
                placeAtNeighbor(random);
            }
        }
        
        //update the maze progress
        updateProgress();
    }
    
    /**
     * Put our location at a room next to an unvisited room
     */
    private void placeAtNeighbor(final Random random)
    {
        //remove any existing objects
        options.clear();
        
        //check all rooms
        for (int col1 = 0; col1 < getCols(); col1++)
        {
            for (int row1 = 0; row1 < getRows(); row1++)
            {
                //if this room has not been visited
                if (!getRoom(col1, row1).hasVisited())
                {
                    //check all walls
                    for (Room.Wall wall : Room.getAllWalls(isHexagon())) {
                        Room tmp = getRoomNeighbor(col, row, wall);

                        if (tmp != null && tmp.hasVisited())
                            options.add(tmp);
                    }
                }
            }
        }
        
        //pick random room
        final Room room = options.get(random.nextInt(options.size()));
        
        //set our new location
        col = room.getCol();
        row = room.getRow();
        
        //remove any existing objects
        options.clear();
    }
}