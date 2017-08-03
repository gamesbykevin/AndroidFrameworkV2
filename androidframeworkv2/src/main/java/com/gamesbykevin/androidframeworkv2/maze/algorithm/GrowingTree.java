package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Growing Tree maze generation algorithm
 * @author GOD
 */
public class GrowingTree extends Maze
{
    //temporary list of optional rooms
    private List<Room> options, tmp;
    
    public GrowingTree(final int cols, final int rows) throws Exception
    {
        super(cols, rows);
        
        //fill all walls
        super.populateRooms();
        
        //create new lists
        this.options = new ArrayList<Room>();
        this.tmp = new ArrayList<Room>();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        options.clear();
        options = null;
        
        tmp.clear();
        tmp = null;
    }
    
    /**
     * Create our maze
     * @param random Object used to make random decisions
     */
    @Override
    public void update(final Random random) throws Exception
    {
        //if generated no need to continue
        if (isGenerated())
            return;
        
        if(!MazeHelper.hasVisited(this))
        {
            //pick random location
            final int col = random.nextInt(getCols());
            final int row = random.nextInt(getRows());
            
            //add random room to list
            options.add(getRoom(col, row));
        }
        
        //random index from our list of cells
        final int index;
        
        /**
         * Choose at random whether to pick a random index from the list of the newest added
         */
        if (random.nextBoolean())
        {
            index = random.nextInt(options.size());
        }
        else
        {
            index = options.size() - 1;
        }
        
        //get that random room
        final Room room = options.get(index);
        
        //check neighbors
        final Room east = getRoom(room.getCol() + 1, room.getRow());
        final Room west = getRoom(room.getCol() - 1, room.getRow());
        final Room north = getRoom(room.getCol(), room.getRow() - 1);
        final Room south = getRoom(room.getCol(), room.getRow() + 1);
        
        //clear the list
        tmp.clear();
        
        //add any rooms that have not been visited to our list
        if (east != null && !east.hasVisited())
            tmp.add(east);
        if (west != null && !west.hasVisited())
            tmp.add(west);
        if (north != null && !north.hasVisited())
            tmp.add(north);
        if (south != null && !south.hasVisited())
            tmp.add(south);
        
        //if there are no unvisited neighbors
        if (tmp.isEmpty())
        {
            //remove it from the list
            options.remove(index);
        }
        else
        {
            //pick random room from our list
            final Room randomRoom = tmp.get(random.nextInt(tmp.size()));
            
            //join the rooms
            MazeHelper.joinRooms(room, randomRoom);
            
            //mark the rooms as visited
            room.setVisited(true);
            randomRoom.setVisited(true);
            
            //add the random room to the list
            options.add(randomRoom);
        }
        
        //update the progress
        updateProgress();
    }
}