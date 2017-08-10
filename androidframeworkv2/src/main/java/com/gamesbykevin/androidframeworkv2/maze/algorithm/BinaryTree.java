package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;

import java.util.Random;

/**
 * Binary Tree maze generation algorithm
 * @author GOD
 */
public class BinaryTree extends Maze
{
    //our current location
    private int col = 0, row = 0;
    
    /**
     * The different directions we can use to create passages.<br>
     * We will choose one randomly and use that for the entire maze generation
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
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
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
        
        //if we don't have a direction selected, pick a random direction
        if (direction == null)
            direction = Directions.values()[random.nextInt(Directions.values().length)];

        //our optional rooms to create passages
        final Room room1;
        final Room room2;

        //get our 2 rooms to choose between
        switch (direction)
        {
            case NW:
                if (isHexagon()) {
                    room1 = getRoomNeighbor(col, row, Room.Wall.West);
                    room2 = getRoomNeighbor(col, row, Room.Wall.NorthWest);
                } else {
                    room1 = getRoomNeighbor(col, row, Room.Wall.West);
                    room2 = getRoomNeighbor(col, row, Room.Wall.North);
                }
                break;
                
            case NE:
                if (isHexagon()) {
                    room1 = getRoomNeighbor(col, row, Room.Wall.East);
                    room2 = getRoomNeighbor(col, row, Room.Wall.NorthEast);
                } else {
                    room1 = getRoomNeighbor(col, row, Room.Wall.East);
                    room2 = getRoomNeighbor(col, row, Room.Wall.North);
                }
                break;
                
            case SW:
                if (isHexagon()) {
                    room1 = getRoomNeighbor(col, row, Room.Wall.West);
                    room2 = getRoomNeighbor(col, row, Room.Wall.SouthWest);
                } else {
                    room1 = getRoomNeighbor(col, row, Room.Wall.West);
                    room2 = getRoomNeighbor(col, row, Room.Wall.South);
                }
                break;
                
            case SE:
                if (isHexagon()) {
                    room1 = getRoomNeighbor(col, row, Room.Wall.East);
                    room2 = getRoomNeighbor(col, row, Room.Wall.SouthEast);
                } else {
                    room1 = getRoomNeighbor(col, row, Room.Wall.East);
                    room2 = getRoomNeighbor(col, row, Room.Wall.South);
                }
                break;
                
            default:
                throw new Exception("Direction is not handled here " + direction.toString());
        }

        //check our rooms to see what we can do
        if (room1 != null && room2 == null) {
            MazeHelper.joinRooms(isHexagon(), getRoom(col, row), room1);
        } else if (room1 == null && room2 != null) {
            MazeHelper.joinRooms(isHexagon(), getRoom(col, row), room2);
        } else if (room1 != null && room2 != null) {
            //if both rooms exist, pick one at random
            MazeHelper.joinRooms(isHexagon(), getRoom(col, row), random.nextBoolean() ? room1 : room2);
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