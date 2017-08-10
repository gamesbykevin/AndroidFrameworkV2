package com.gamesbykevin.androidframeworkv2.maze;

import com.gamesbykevin.androidframeworkv2.base.Cell;
import com.gamesbykevin.androidframeworkv2.util.Progress;

import java.util.Random;

import static com.gamesbykevin.androidframeworkv2.maze.Room.Wall.NorthEast;
import static com.gamesbykevin.androidframeworkv2.maze.Room.Wall.NorthWest;
import static com.gamesbykevin.androidframeworkv2.maze.Room.Wall.SouthEast;
import static com.gamesbykevin.androidframeworkv2.maze.Room.Wall.SouthWest;

/**
 * The parent Maze class
 * @author GOD
 */
public abstract class Maze implements IMaze
{
    /**
     * The number of columns in this maze
     */
    private final int cols;
    
    /**
     * The number of rows in this maze
     */
    private final int rows;
    
    /**
     * The rooms that make up the maze
     */
    private Room[][] rooms;
    
    //the start and finish locations
    private Cell start, finish;
    
    //our maze generation progress
    private Progress progress;
    
    //default maze size
    protected static final int DEFAULT_MAZE_DIMENSION = 10;

    //is each room in  the maze a hexagon?
    private final boolean hexagon;

    //the start location to render the 2d maze, and the room dimension
    private int x = 0, y = 0, d = 32;
    
    /**
     * Create a new maze of specified size
     * @param hexagon Is each room in the maze a hexagon? if false we assume square
     * @param cols Total columns
     * @param rows Total rows
     * @throws Exception If the minimum required dimensions is not provided
     */
    protected Maze(boolean hexagon, final int cols, final int rows) throws Exception
    {
        //assign the shape of room
        this.hexagon = hexagon;

        //clear the cached list just in case
        Room.getAllWalls(isHexagon(), true);

        if (cols < 2)
            throw new Exception("The maze must contain at least 2 columns");
        if (rows < 2)
            throw new Exception("The maze must contain at least 2 rows");
        
        //store dimensions
        this.cols = cols;
        this.rows = rows;
        
        //create new progress object
        this.progress = new Progress(cols * rows);
        
        //create a new array of rooms 
        this.rooms = new Room[rows][cols];
        
        //now create our rooms
        createRooms();
        
        //create the start/finish locations
        this.start = new Cell();
        this.finish = new Cell();
    }

    /**
     * Is the maze a hexagon?
     * @return true if every room is shaped like a hexagon, false otherwise
     */
    public boolean isHexagon() {
        return this.hexagon;
    }
    
    /**
     * Assign the coordinate where the 2d maze will be rendered<br>
     * This is only for the generic maze render
     * @param x x-coordinate
     */
    public void setX(final int x)
    {
    	this.x = x;
    }
    
    /**
     * Assign the coordinate where the 2d maze will be rendered<br>
     * This is only for the generic maze render
     * @param y y-coordinate
     */
    public void setY(final int y)
    {
    	this.y = y;
    }
    
    /**
     * Assign the room dimension for the 2d maze<br>
     * This is only for the generic maze render
     * @param d The pixel dimension of a single room
     */
    public void setD(final int d)
    {
    	this.d = d;
    }
    
    /**
     * Create a room for every (column, row) in our maze
     */
    private void createRooms()
    {
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                this.rooms[row][col] = new Room(col, row);
            }
        }
    }

    /**
     * Get the finish location
     * @return The finish (column, row)
     */
    public Cell getFinish()
    {
        return this.finish;
    }
    
    /**
     * Get the start location
     * @return The start (column, row)
     */
    public Cell getStart()
    {
        return this.start;
    }
    
    /**
     * Get the start column
     * @return The start column
     */
    public int getStartCol()
    {
        return (int)start.getCol();
    }
    
    /**
     * Get the start row
     * @return The start row
     */
    public int getStartRow()
    {
        return (int)start.getRow();
    }
    
    /**
     * Assign the starting location
     * @param col The start column
     * @param row The start row
     */
    public void setStartLocation(final int col, final int row)
    {
        this.start.setCol(col);
        this.start.setRow(row);
    }
    
    /**
     * Get the finish column
     * @return The finish column
     */
    public int getFinishCol()
    {
        return (int)finish.getCol();
    }
    
    /**
     * Get the finish row
     * @return The finish row
     */
    public int getFinishRow()
    {
        return (int)finish.getRow();
    }
    
    /**
     * Assign the finish location
     * @param col The finish column
     * @param row The finish row
     */
    public void setFinishLocation(final int col, final int row)
    {
        this.finish.setCol(col);
        this.finish.setRow(row);
    }
    
    /**
     * Fill each room with walls accordingly
     */
    protected void populateRooms()
    {
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                final Room room = getRoom(col, row);
                
                //make sure the room exists
                if (room != null)
                    getRoom(col, row).addAllWalls(isHexagon());
            }
        }
    }
    
    /**
     * Is this location within the bounds of this maze?
     * @param col Column
     * @param row Row
     * @return true = yes, false = no
     */
    @Override
    public boolean hasBounds(final int col, final int row)
    {
        return (col >= 0 && col < getCols() && row >= 0 && row < getRows());
    }
    
    @Override
    public void dispose()
    {
        if (getRooms() != null)
        {
            for (int row = 0; row < getRows(); row++)
            {
                for (int col = 0; col < getCols(); col++)
                {
                    this.rooms[row][col].dispose();
                    this.rooms[row][col] = null;
                }
            }
            
            this.rooms = null;
        }
    }

    protected Room getRoomNeighbor(final Room room, Room.Wall direction) {
        return getRoomNeighbor(room.getCol(), room.getRow(), direction);
    }

    protected Room getRoomNeighbor(final int col, final int row, Room.Wall direction) {

        int tmpCol = direction.getCol();
        int tmpRow = direction.getRow();

        //need to verify neighbors
        if (isHexagon()) {
            switch (direction) {

                case NorthWest:
                    if (row % 2 == 0) {
                        tmpCol = -1;
                    } else {
                        tmpCol = 0;
                    }
                    break;

                case NorthEast:
                    if (row % 2 == 0) {
                        tmpCol = 0;
                    } else {
                        tmpCol = 1;
                    }
                    break;

                case SouthWest:
                    if (row % 2 == 0) {
                        tmpCol = -1;
                    } else {
                        tmpCol = 0;
                    }
                    break;

                case SouthEast:
                    if (row % 2 == 0) {
                        tmpCol = 0;
                    } else {
                        tmpCol = 1;
                    }
                    break;
            }
        }

        //return the neighbor room
        return getRoom(col + tmpCol, row + tmpRow);
    }

    /**
     * Get the room at the specified location
     * @param col Column
     * @param row Row
     * @return The room at the specified location, if the location is out of bounds, null is returned
     */
    public Room getRoom(final int col, final int row)
    {
        //if out of bounds return null
        if (!hasBounds(col, row))
            return null;
        
        return getRooms()[row][col];
    }
    
    /**
     * Get the rooms.
     * @return The array of rooms that make up the maze
     */
    public Room[][] getRooms()
    {
        return this.rooms;
    }
    
    /**
     * Get the columns
     * @return The total number of columns in this maze
     */
    public int getCols()
    {
        return this.cols;
    }
    
    /**
     * Get the rows
     * @return The total number of rows in this maze
     */
    public int getRows()
    {
        return this.rows;
    }
    
    public boolean isGenerated()
    {
    	return (getProgress().isComplete());
    }
    
    @Override
    public Progress getProgress()
    {
        return this.progress;
    }
    
    /**
     * Update the progress of our maze creation.<br>
     * Here we track the progress by the number of visited rooms.
     */
    protected void updateProgress()
    {
        int count = 0;
        
        //check all rooms
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                //keep track of number of visited rooms
                if (getRoom(col, row).hasVisited())
                    count++;
            }
        }
        
        //update the progress
        getProgress().setCount(count);
    }
    
    /**
     * Each child maze needs to have logic to generate
     * @param random Object used to make random decisions
     * @throws Exception 
     */
    @Override
    public abstract void update(final Random random) throws Exception;
}