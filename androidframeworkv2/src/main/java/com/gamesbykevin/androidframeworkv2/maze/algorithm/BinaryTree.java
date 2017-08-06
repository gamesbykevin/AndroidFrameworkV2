package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Binary Tree maze generation algorithm
 * @author GOD
 */
public class BinaryTree extends Maze
{
    //our current location
    private int col = 0, row = 0;
    
    //list of rooms
    private List<Room> tmp;
    
    /**
     * The different directions we can use to create passages
     */
    private enum Directions
    {
        NE, NW, SE, SW
    }
    
    //the direction we will use
    private Directions direction;
    
    public BinaryTree(final boolean hexagon, final int cols, final int rows) throws Exception
    {
        super(hexagon, cols, rows);
        
        //fill all walls
        super.populateRooms();
        
        //create a new list
        this.tmp = new ArrayList<Room>();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
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
        
        //if we don't have a direction selected
        if (direction == null)
        {
            //pick a random direction
            direction = Directions.values()[random.nextInt(Directions.values().length)];
        }
        
        //clear list
        tmp.clear();
        
        //our optional rooms to create passages
        final Room room1;
        final Room room2;
        
        switch (direction)
        {
            case NW:
                room1 = getRoom(col - 1, row);
                room2 = getRoom(col, row - 1);
                break;
                
            case NE:
                room1 = getRoom(col + 1, row);
                room2 = getRoom(col, row - 1);
                break;
                
            case SW:
                room1 = getRoom(col - 1, row);
                room2 = getRoom(col, row + 1);
                break;
                
            case SE:
                room1 = getRoom(col + 1, row);
                room2 = getRoom(col, row + 1);
                break;
                
            default:
                throw new Exception("Direction is not handled here " + direction.toString());
        }
        
        //if the room exists, add to the list
        if (room1 != null)
            tmp.add(room1);
        if (room2 != null)
            tmp.add(room2);
        
        //join the rooms if there is at least one in our list
        if (!tmp.isEmpty())
        {
            //get the current room
            final Room room = getRoom(col, row);

            //now join the current room to a random room from the list
            MazeHelper.joinRooms(isHexagon(), room, tmp.get(random.nextInt(tmp.size())));
        }
        
        //move to the next column
        col++;
        
        //if we past the last column
        if (col >= getCols())
        {
            //start at the first column
            col = 0;
            
            //move to the next row
            row++;
        }
        
        //increase the progress
        super.getProgress().increase();
    }
}
