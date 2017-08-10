package com.gamesbykevin.androidframeworkv2.maze.algorithm;

import com.gamesbykevin.androidframeworkv2.maze.Maze;
import com.gamesbykevin.androidframeworkv2.maze.MazeHelper;
import com.gamesbykevin.androidframeworkv2.maze.Room;
import com.gamesbykevin.androidframeworkv2.maze.Room.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Kruskal's maze generation algorithm
 * @author GOD
 */
public class Kruskals extends Maze
{
    public Kruskals(final boolean hexagon, final int cols, final int rows) throws Exception
    {
        super(hexagon, cols, rows);
        
        //set walls for each room
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
        
        //get the room with the lowest weight
        final Room room = getLowestWeight(random);
        
        //create a list of optional directions to randomly choose from
        List<Room.Wall> directions = new ArrayList<>();

        //check all neighbors
        for (int i = 0; i < Room.getAllWalls(isHexagon()).size(); i++) {
            Wall wall = Room.getAllWalls(isHexagon()).get(i);

            //get the neighbor room
            Room tmp = getRoom(room.getCol() + wall.getCol(), room.getRow() + wall.getRow());

            //if the room exists and is not part of the same set
            if (tmp != null && !tmp.hasId(room))
                directions.add(wall);
        }

        //now randomly join a neighboring room
        joinRooms(room, directions.get(random.nextInt(directions.size())));
        
        //update the progress
        super.getProgress().setCount(((getCols() * getRows()) - getUniqueSetCount()) + 1);
    }
    
    /**
     * Get the unique set count.<br>
     * As we generate our maze this number will decrease until we reach 1 set
     * @return The total number of unique sets
     */
    private int getUniqueSetCount()
    {
        //our list of unique sets
        List<UUID> sets = new ArrayList<UUID>();
        
        //check all rooms
        for (int col = 0; col < getCols(); col++)
        {
            for (int row = 0; row < getRows(); row++)
            {
                final Room room = getRoom(col, row);
                
                //do we already have a matching set
                boolean match = false;
                
                //check each set to see if we have a match
                for (int index = 0; index < sets.size(); index++)
                {
                    //if we already have this in our list
                    if (room.hasId(sets.get(index)))
                    {
                        //flag match
                        match = true;
                        
                        //no need to check the rest
                        break;
                    }
                }
                
                //if we didn't find it, add to the list
                if (!match)
                    sets.add(room.getId());
            }
        }
        
        //return the number of sets that we found
        return sets.size();
    }
    
    /**
     * Get the room with the lowest weight.
     * @param random Object used to make random decisions
     * @return The room with the fewest amount of matching sets, if no rooms are available return null
     */
    private Room getLowestWeight(final Random random)
    {
        //the weight to beat
        int lowestWeight = getCols() * getRows();
        
        //the list of the 
        List<Room> rooms = new ArrayList<Room>();
        
        //check all rooms
        for (int col = 0; col < getCols(); col++)
        {
            for (int row = 0; row < getRows(); row++)
            {
                //get the current room
                final Room room = getRoom(col, row);
                
                //count the total number of sets
                final int count = getSetCount(room);
                
                //if we have a new low weight
                if (count < lowestWeight)
                {
                    //we also want to make sure there are new neighbors we can connect to
                    if (getNeighborCount(room) > 0)
                    {
                        //remove all existing rooms
                        rooms.clear();

                        //add the new champion
                        rooms.add(room);

                        //set the new score to beat
                        lowestWeight = count;
                    }
                }
                else if (count == lowestWeight)
                {
                    //we also want to make sure there are new neighbors we can connect to
                    if (getNeighborCount(room) > 0)
                    {
                        //add the new champion
                        rooms.add(room);
                    }
                }
            }
        }
        
        //return a random room from our list
        return rooms.get(random.nextInt(rooms.size()));
    }
    
    /**
     * Get the neighbor count
     * @param room The room containing the set (id) we want to check
     * @return The total number of neighbors (north, south, east, west) that have a different set (id)
     */
    private int getNeighborCount(final Room room)
    {
        //keep track of the count
        int count = 0;

        //check all neighbors
        for (int i = 0; i < Room.getAllWalls(isHexagon()).size(); i++) {

            //get the current wall
            Wall wall = Room.getAllWalls(isHexagon()).get(i);

            //offset the direction to get the room
            Room tmp = getRoom(room.getCol() + wall.getCol(), room.getRow() + wall.getRow());

            //if the room exists and is not part of the same set
            if (tmp != null && !tmp.hasId(room))
                count++;
        }

        return count;
    }
    
    /**
     * Get the set count
     * @param room The room containing the set (id) that we want to check
     * @return The total number of rooms that have the matching set
     */
    private int getSetCount(final Room room)
    {
        //count the number of matching sets
        int count = 0;
        
        //check all rooms
        for (int col = 0; col < getCols(); col++)
        {
            for (int row = 0; row < getRows(); row++)
            {
                //if the id's match
                if (getRoom(col, row).hasId(room))
                    count++;
            }
        }
        
        //return our result
        return count++;
    }
    
    /**
     * Join rooms
     * @param room The room we want to join
     * @param direction The direction of the neighbor we want to join
     * @throws Exception 
     */
    private void joinRooms(final Room room, final Wall direction) throws Exception
    {
        switch (direction)
        {
            case NorthEast:
                //remove the appropriate walls
                room.removeWall(Wall.NorthEast);
                getRoom(room.getCol() + 1, room.getRow() - 1).removeWall(Wall.SouthWest);
                break;

            case NorthWest:
                //remove the appropriate walls
                room.removeWall(Wall.NorthWest);
                getRoom(room.getCol() - 1, room.getRow() - 1).removeWall(Wall.SouthEast);
                break;

            case SouthEast:
                //remove the appropriate walls
                room.removeWall(Wall.SouthEast);
                getRoom(room.getCol() + 1, room.getRow() + 1).removeWall(Wall.NorthWest);
                break;

            case SouthWest:
                //remove the appropriate walls
                room.removeWall(Wall.SouthWest);
                getRoom(room.getCol() - 1, room.getRow() + 1).removeWall(Wall.NorthEast);
                break;

            case East:
                //remove the appropriate walls
                room.removeWall(Wall.East);
                getRoom(room.getCol() + 1, room.getRow()).removeWall(Wall.West);
                break;
                
            case West:
                //remove the appropriate walls
                room.removeWall(Wall.West);
                getRoom(room.getCol() - 1, room.getRow()).removeWall(Wall.East);
                break;
                
            case North:
                //remove the appropriate walls
                room.removeWall(Wall.North);
                getRoom(room.getCol(), room.getRow() - 1).removeWall(Wall.South);
                break;
                
            case South:
                //remove the appropriate walls
                room.removeWall(Wall.South);
                getRoom(room.getCol(), room.getRow() + 1).removeWall(Wall.North);
                break;
                
            default:
                throw new Exception("Direction not handled here " + direction.toString());
        }
        
        //now update the neighboring rooms that we can access to have the same set
        joinSets(room);
    }
    
    /**
     * Assign the rooms we can access with the same set.<br>
     * Starting at the specified source we will check all neighbors that we can access and assign the room
     * @param source The room containing the set we want to assign
     */
    private void joinSets(final Room source)
    {
        //mark all rooms as not visited
        MazeHelper.setVisitedAll(this, false);
        
        //now we want to check all rooms that border the current room and assign the same set
        List<Room> rooms = new ArrayList<Room>();
        
        //add the initial room
        rooms.add(source);
        
        //continue to check while rooms exist
        while (!rooms.isEmpty())
        {
            //get the first room
            final Room tmp = rooms.get(0);
            
            //assign the matching id
            tmp.setId(source);
            
            //mark as visited
            tmp.setVisited(true);

            for (int i = 0; i < Room.getAllWalls(isHexagon()).size(); i++) {
                Wall wall = Room.getAllWalls(isHexagon()).get(i);

                //if there is no wall and the neighboring room has not been visited yet
                if (!tmp.hasWall(wall) && !getRoom(tmp.getCol() + wall.getCol(), tmp.getRow() + wall.getRow()).hasVisited())
                    rooms.add(getRoom(tmp.getCol() + wall.getCol(), tmp.getRow() + wall.getRow()));
            }

            //remove room from list
            rooms.remove(0);
        }
    }
}