package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.base.Entity;
import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;
import com.gamesbykevin.androidframeworkv2.maze.Room.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Wilson's maze generation algorithm
 * @author GOD
 */
public class Wilsons extends Maze
{
    //temporary list for rooms
    private List<Room> options;
    
    //list of directions
    private List<Target> directions;
    
    //temporary list of directions
    private List<Wall> tmp;
    
    //our current location
    private int col, row;
    
    //the start location while generating the maze
    private int startCol, startRow;
    
    //count the number of moves made before meeting a room part of the maze
    private int count = 0;
    
    public Wilsons(final boolean hexagon, final int cols, final int rows) throws Exception
    {
        super(hexagon, cols, rows);
        
        //fill each room with walls
        super.populateRooms();
        
        //create new list
        this.options = new ArrayList<>();
        
        //create new list
        this.directions = new ArrayList<>();
        
        //create new list
        this.tmp = new ArrayList<>();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        options.clear();
        options = null;
        
        tmp.clear();
        tmp = null;
        
        directions.clear();
        directions = null;
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
        
        //we are just creating the maze, mark the first room as visited
        if (!MazeHelper.hasVisited(this))
            getRandomRoom(random).setVisited(true);

        //if we have no steps currently
        if (directions.isEmpty()) {

            //pick random room to start
            final Room room = getRandomRoom(random);
            
            //assign the start location
            startCol = room.getCol();
            startRow = room.getRow();
            
            //assign the current location
            col = startCol;
            row = startRow;
            
            //reset the count
            count = 0;
        }
        
        //clear objects in list
        tmp.clear();
        
        /**
         * If the number of attempts has exceeded half the size of the maze we will locate the closest visited room.
         * This is to help shorten the time of maze creation.
         */
        if (count >= (getCols() * getRows()) / 2)
            calculateShortestPath();

        //if the list is empty, locate optional directions
        if (tmp.isEmpty())
        {
            for (Wall wall : Room.getAllWalls(isHexagon())) {

                //get the neighbor
                Room neighbor = getRoomNeighbor(col, row, wall);

                //if the neighbor exists, add to list
                if (neighbor != null)
                    tmp.add(wall);
            }
        }
        
        //pick a random direction from the list
        final Wall direction = tmp.get(random.nextInt(tmp.size()));
        
        //add direction to list
        directions.add(new Target(direction, col, row));

        //get the neighbor
        Room neighbor = getRoomNeighbor(col, row, direction);

        //update the location based on our random direction neighbor
        col = neighbor.getCol();
        row = neighbor.getRow();

        /**
         * If this room was visited (a.k.a. part of the maze)<br>
         * We will now create the path and from the start to this location and make it part of the maze
         */
        if (getRoom(col, row).hasVisited())
        {
            //continue until the start location gets to the finish
            while (startCol != col || startRow != row)
            {
                //get the room at the start location
                Room room1 = getRoom(startCol, startRow);
                
                //next room
                Room room2 = null;

                //get the target at this specific location to determine the next move
                for (int index = directions.size() - 1; index >= 0; index--)
                {
                    if (directions.get(index).hasTarget(room1))
                    {
                        //get the next room in our steps
                        room2 = getRoomNeighbor(startCol, startRow, directions.get(index).direction);

                        //now remove this target
                        directions.remove(index);
                        
                        //exit the for loop
                        break;
                    }
                }
                
                //mark both rooms visited
                room1.setVisited(true);
                room2.setVisited(true);
                
                //join the rooms together
                MazeHelper.joinRooms(isHexagon(), room1, room2);
                
                //now update the start location to the current position
                startCol = room2.getCol();
                startRow = room2.getRow();
            }
            
            //make sure we clear the list
            directions.clear();
        }
        else
        {
            //increase the count
            count++;
        }
        
        //update the progress
        updateProgress();
    }
    
    /**
     * Calculate the shortest path.<br>
     * What we will do is find the direction of the nearest visited room.
     */
    private void calculateShortestPath()
    {
        //the highest distance
        double distance = (getCols() * getRows());
        
        //our temp room
        Room room = null;
        
        //check all rooms
        for (int row1 = 0; row1 < getRows(); row1++)
        {
            for (int col1 = 0; col1 < getCols(); col1++)
            {
                //don't check the same location
                if (col == col1 && row == row1)
                    continue;
                
                //don't check a room that hasn't been visited
                if (!getRoom(col1, row1).hasVisited())
                    continue;
                
                //calculate the distance
                final double temp = Entity.getDistance(col1, row1, col, row);
                
                //if we found a shorter distance
                if (temp < distance)
                {
                    //set this as the one to beat
                    distance = temp;
                    
                    //assign the shortest room
                    room = getRoom(col1, row1);
                }
            }
        }
        
        //if the winning room was found
        if (room != null)
        {
            /**
             * Compare the room to the current location. 
             * So we know which direction to head in
             */
            for (Wall wall : Room.getAllWalls(isHexagon())) {

                switch (wall) {

                    case North:

                        if (room.getRow() < row)
                            tmp.add(wall);
                        break;

                    case South:

                        if (room.getRow() > row)
                            tmp.add(wall);
                        break;

                    case West:

                        if (isHexagon()) {
                            if (room.getCol() < col && room.getRow() == row)
                                tmp.add(wall);
                        } else {
                            if (room.getCol() < col)
                                tmp.add(wall);
                        }
                        break;

                    case East:

                        if (isHexagon()) {
                            if (room.getCol() > col && room.getRow() == row)
                                tmp.add(wall);
                        } else {
                            if (room.getCol() > col)
                                tmp.add(wall);
                        }
                        break;

                    case NorthWest:

                        //if the winning room is above s
                        if (room.getRow() < row) {
                            if (room.getRow() % 2 == 0) {
                                if (room.getCol() < col)
                                    tmp.add(wall);
                            } else {
                                if (room.getCol() == col)
                                    tmp.add(wall);
                            }
                        }
                        break;

                    case NorthEast:

                        //if the winning room is above us
                        if (room.getRow() < row) {
                            if (room.getRow() % 2 == 0) {
                                if (room.getCol() == col)
                                    tmp.add(wall);
                            } else {
                                if (room.getCol() > col)
                                    tmp.add(wall);
                            }
                        }
                        break;

                    case SouthWest:

                        //if the winning room is below us
                        if (room.getRow() > row) {
                            if (room.getRow() % 2 == 0) {
                                if (room.getCol() < col)
                                    tmp.add(wall);
                            } else {
                                if (room.getCol() == col)
                                    tmp.add(wall);
                            }
                        }
                        break;

                    case SouthEast:

                        //if the winning room is below us
                        if (room.getRow() > row) {
                            if (room.getRow() % 2 == 0) {
                                if (room.getCol() == col)
                                    tmp.add(wall);
                            } else {
                                if (room.getCol() > col)
                                    tmp.add(wall);
                            }
                        }
                        break;

                    default:
                        throw new RuntimeException("Wall not defined: " + wall.toString());
                }
            }
        }
    }
    
    /**
     * Get a random room that has not been visited
     * @param random Object used to make random decisions
     * @return A random room that has not been visited yet
     */
    private Room getRandomRoom(final Random random)
    {
        //clear list
        options.clear();
        
        //check all rooms
        for (int row1 = 0; row1 < getRows(); row1++)
        {
            for (int col1 = 0; col1 < getCols(); col1++)
            {
                //get the room
                final Room room = getRoom(col1, row1);
                
                //add to list if not already visited
                if (!room.hasVisited())
                    options.add(room);
            }
        }
        
        //return a random room
        return options.get(random.nextInt(options.size()));
    }
    
    /**
     * Private class for each target to the destination
     */
    private class Target
    {
        //the direction to head in
        private final Wall direction;
        
        //the current location where this direction is
        private final int col, row;
        
        private Target(final Wall direction, final Room room)
        {
            this(direction, room.getCol(), room.getRow());
        }
        
        private Target(final Wall direction, final int col, final int row)
        {
            this.direction = direction;
            this.col = col;
            this.row = row;
        }
        
        /**
         * Determine if the col, row matches that of the room
         * @param room Room containing the position we want to check for
         * @return true if the room (col,row) matches this target (col,row)
         */
        private boolean hasTarget(final Room room)
        {
            return (col == room.getCol() && row == room.getRow());
        }
    }
}