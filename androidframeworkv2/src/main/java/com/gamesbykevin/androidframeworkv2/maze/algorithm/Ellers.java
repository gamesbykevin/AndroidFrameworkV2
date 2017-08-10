package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;

import java.util.Random;

/**
 * Ellers maze generation algorithm
 * @author GOD
 */
public class Ellers extends Maze
{
    //our current location
    private int col = 0, row = 0;
    
    public Ellers(final boolean hexagon, final int cols, final int rows) throws Exception
    {
        super(hexagon, cols, rows);
        
        //set the walls for each room
        super.populateRooms();
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
        
        //randomly choose to join adjacent cells
        if (random.nextBoolean())
        {
            //if the neighbor location is in bounds
            if (hasBounds(col + 1, row))
            {
                //create the horizontal path
                createHorizontalPath(getRoom(col, row), getRoom(col + 1, row));
            }
        }

        //increase the column
        col++;
        
        //increase the progress
        super.getProgress().increase();
        
        //if we are at the end of the row perform cleanup
        if (!hasBounds(col, row))
        {
            cleanupRow(random);
            
            //reset column back to beginning
            col = 0;
            
            //increase the row
            row++;
        }
    }
    
    /**
     * Perform cleanup on the current row
     * @param random Object used to make random decisions
     */
    private void cleanupRow(final Random random) throws Exception
    {
        //check if we are at the last row
        if (row < getRows() - 1)
        {
            //reset the column position back to the beginning and make vertical connections
            for (col = 0; col < getCols(); col++)
            {
                //decide at random if we are to create a vertical connection
                if (random.nextBoolean())
                {
                    //create the vertical path
                    createVerticalPath(getRoom(col, row), random);
                }
            }

            //now lets make sure each set in the current row has at least 1 matching set in the row below
            for (col = 0; col < getCols(); col++)
            {
                //if we don't have a matching set below, lets create it now
                if (!hasSet(getRoom(col, row), row + 1))
                {
                    //create the vertical path
                    createVerticalPath(getRoom(col, row), random);
                }
            }
        }
        else
        {
            //we are at the last row, any non-matching neighbors will be joined
            for (col = 0; col < getCols() - 1; col++)
            {
                //create the path
                createHorizontalPath(getRoom(col, row), getRoom(col + 1, row));
            }
        }
    }
    
    /**
     * Create a horizontal path joining the 2 rooms.<br>
     * If the rooms are already part of the same set, nothing will happen here
     * @param roomWest Room to the west
     * @param roomEast The neighbor room to the east
     */
    private void createHorizontalPath(final Room roomWest, final Room roomEast) throws Exception
    {
        //only check if the neighbor is part of a different set
        if (!roomEast.hasId(roomWest))
        {
            //make the neighbor part of the same set
            roomEast.setId(roomWest);

            //now remove the walls between the neighbors
            MazeHelper.joinRooms(isHexagon(), roomWest, roomEast);
        }
    }
    
    /**
     * Create a vertical path joining the north room and a room to the south.<br>
     * @param roomNorth Room to the north
     * @param random Object used to make random decisions
     */
    private void createVerticalPath(final Room roomNorth, final Random random) throws Exception
    {
        //a room below the current north room
        Room roomSouth;

        if (isHexagon()) {

            Room room1 = getRoomNeighbor(roomNorth, Room.Wall.SouthWest);
            Room room2 = getRoomNeighbor(roomNorth, Room.Wall.SouthEast);

            if (room1 != null && room2 == null) {
                roomSouth = room1;
            } else if (room1 == null && room2 != null) {
                roomSouth = room2;
            } else {
                roomSouth = random.nextBoolean() ? room1 : room2;
            }

        } else {

            roomSouth = getRoom(roomNorth.getCol(), roomNorth.getRow() + 1);
        }

        //make the neighbor part of the same set
        roomSouth.setId(roomNorth);

        //now remove the walls between the neighbors
        MazeHelper.joinRooms(isHexagon(), roomSouth, roomNorth);
    }
    
    /**
     * Do we have the set?
     * @param room Room containing the unique set (id)
     * @param row The row we want to check
     * @return true if at least 1 room in the specified row has a matching set
     */
    private boolean hasSet(final Room room, final int row)
    {
        for (int column = 0; column < getCols(); column++)
        {
            //if the set matches, return true
            if (room.hasId(getRoom(column, row)))
                return true;
        }
        
        //no matching set was found
        return false;
    }
}