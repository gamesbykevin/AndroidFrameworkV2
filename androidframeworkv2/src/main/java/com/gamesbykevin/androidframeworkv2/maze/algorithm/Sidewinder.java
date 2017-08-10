package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sidewinder maze generation algorithm
 * @author GOD
 */
public class Sidewinder extends Maze
{
    //temporary list of optional rooms
    private List<Room> options;
    
    //the current location
    private int currentCol, currentRow;
    
    public Sidewinder(final boolean hexagon, final int cols, final int rows) throws Exception
    {
        super(hexagon, cols, rows);
        
        //fill all walls
        super.populateRooms();
        
        //create new optional list
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
     */
    @Override
    public void update(final Random random) throws Exception
    {
        //if generated no need to continue
        if (isGenerated())
            return;
        
        //if we are just starting with the maze
        if (!MazeHelper.hasVisited(this))
        {
            this.currentCol = 0;
            this.currentRow = 0;
        }
        
        //add the current room to the list
        options.add(getRoom(currentCol, currentRow));
        
        //if chosen at random, or the first row
        if (random.nextBoolean() || currentRow == 0)
        {
            //create a random passage to the east
            createEastPassage();
        }
        else
        {
            //create a random passage to the north
            createNorthPassage(random);
            
            //clear list
            options.clear();
        }
        
        //move to the next column
        currentCol++;
        
        //if at the end of the column, move to the next row
        if (currentCol == getCols() - 1)
        {
            //add the current room to the list
            options.add(getRoom(currentCol, currentRow));
            
            //create a random passage to the north
            createNorthPassage(random);
            
            //reset back to the first column
            currentCol = 0;

            //move to the next row
            currentRow++;

            //clear our list
            options.clear();
        }
        
        //update the progress
        updateProgress();
    }
    
    /**
     * Create a passage east
     */
    private void createEastPassage() throws Exception
    {
        //the current room
        final Room room = getRoom(currentCol, currentRow);

        //the room to the east
        final Room east = getRoom(currentCol + 1, currentRow);

        //mark the rooms as visited
        room.setVisited(true);
        east.setVisited(true);

        //join the rooms
        MazeHelper.joinRooms(isHexagon(), room, east);
    }
    
    /**
     * Create a passage north using one of the existing rooms in our set
     * @param random Object used to make random decisions
     */
    private void createNorthPassage(final Random random) throws Exception
    {
        //we can't go north if in the first row
        if (currentRow == 0)
            return;
        
        //make sure we have options available and are not in the first row
        if (!options.isEmpty())
        {
            //pick a random room in our set
            final Room room = options.get(random.nextInt(options.size()));

            //the room to the north
            final Room north;

            //if hexagon we have 2 options for north
            if (isHexagon()) {

                //2 optional rooms
                Room room1 = getRoomNeighbor(room, Room.Wall.NorthWest);
                Room room2 = getRoomNeighbor(room, Room.Wall.NorthEast);

                if (room1 != null && room2 == null) {
                    north = room1;
                } else if (room1 == null && room2 != null) {
                    north = room2;
                } else {
                    north = random.nextBoolean() ? room1 : room2;
                }

            } else {
                //room directly above to the north
                north = getRoom(room.getCol(), room.getRow() - 1);
            }

            //mark the rooms as visited
            room.setVisited(true);
            north.setVisited(true);

            //join the rooms
            MazeHelper.joinRooms(isHexagon(), room, north);
        }
    }
}