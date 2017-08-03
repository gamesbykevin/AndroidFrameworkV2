package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Prim's maze generation algorithm
 * @author GOD
 */
public class Prims extends Maze
{
    //list of rooms to check
    private List<Room> options;
    
    public Prims(final int cols, final int rows) throws Exception
    {
        super(cols, rows);
        
        //set 4 walls for each room
        super.populateRooms();
        
        //create a new list of optional rooms
        this.options = new ArrayList<Room>();
    }
    
    @Override
    public void dispose()
    {
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
    public void update(final Random random) throws Exception
    {
        //if generated no need to continue
        if (isGenerated())
            return;
        
        //our temporary room
        Room room;
        
        //if we don't have any visited rooms yet, we are just starting
        if (!MazeHelper.hasVisited(this))
        {
            //pick a random location
            final int col = random.nextInt(getCols());
            final int row = random.nextInt(getRows());
            
            //add this random room to the list of options
            options.add(getRoom(col, row));
        }
        
        //pick random position in our list
        final int index = random.nextInt(options.size());
        
        //pick random room from options
        room = options.get(index);
        
        //if we have started
        if (MazeHelper.hasVisited(this))
        {
            //check if any neighbors can be joined to the room
            final Room east = getRoom(room.getCol() + 1, room.getRow());
            final Room west = getRoom(room.getCol() - 1, room.getRow());
            final Room north = getRoom(room.getCol(), room.getRow() - 1);
            final Room south = getRoom(room.getCol(), room.getRow() + 1);
            
            //list of choices to move to
            final List<Room> choices = new ArrayList<Room>();
            
            //we only want to add the room that exists and is already visited to join with
            if (east != null && east.hasVisited())
                choices.add(east);
            if (west != null && west.hasVisited())
                choices.add(west);
            if (north != null && north.hasVisited())
                choices.add(north);
            if (south != null && south.hasVisited())
                choices.add(south);
            
            //the other room
            final Room other = choices.get(random.nextInt(choices.size()));
            
            //join the rooms
            MazeHelper.joinRooms(room, other);
        }
        
        //mark the room as visited
        room.setVisited(true);
        
        //check if any neighbors can be added to the list
        final Room east = getRoom(room.getCol() + 1, room.getRow());
        final Room west = getRoom(room.getCol() - 1, room.getRow());
        final Room north = getRoom(room.getCol(), room.getRow() - 1);
        final Room south = getRoom(room.getCol(), room.getRow() + 1);
        
        //add any optional directions that haven't been visited and don't already exist in the list
        if (east != null && !east.hasVisited() && !options.contains(east))
            options.add(east);
        if (west != null && !west.hasVisited() && !options.contains(west))
            options.add(west);
        if (north != null && !north.hasVisited() && !options.contains(north))
            options.add(north);
        if (south != null && !south.hasVisited() && !options.contains(south))
            options.add(south);
        
        //increase the progress
        super.getProgress().increase();
        
        //remove from the list
        options.remove(index);
    }
}