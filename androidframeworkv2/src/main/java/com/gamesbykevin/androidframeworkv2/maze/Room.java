package com.gamesbykevin.androidframeworkv2.maze;

import com.gamesbykevin.androidframeworkv2.base.Disposable;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

/**
 * A single room in a maze
 * @author GOD
 */
public class Room implements Disposable
{
    //have we visited this room
    private boolean visited = false;
    
    //assign a unique object to identify the room
    private UUID id;
    
    /**
     * All of the possible walls in a room
     */
    public enum Wall
    {
        North, South, West, East
    }
    
    /**
     * List of walls that this room has
     */
    private List<Wall> walls;
    
    /**
     * The lowest cost a room can have
     */
    public static final int COST_MINIMUM = 0;
    
    //the cost of this room, typically the number of rooms away from the start or finish etc....
    private int cost = COST_MINIMUM;
    
    //the location of the room
    private final int col, row;
    
    /**
     * Create a room
     * @param col Column
     * @param row Row
     */
    public Room(final int col, final int row)
    {
        this.col = col;
        this.row = row;
        
        //create a random unique identifier
        setId(UUID.randomUUID());
        
        //create a list for our walls
        this.walls = new ArrayList<Wall>();
    }
    
    /**
     * Get the column
     * @return The column assigned to this room
     */
    public int getCol()
    {
        return this.col;
    }
    
    /**
     * Get the row
     * @return The row assigned to this room
     */
    public int getRow()
    {
        return this.row;
    }
    
    @Override
    public void dispose()
    {
        this.walls.clear();
        this.walls = null;
    }
    
    /**
     * Get the id
     * @return The unique identifier generated for this room
     */
    public UUID getId()
    {
        return this.id;
    }
    
    /**
     * Does this room have the id?
     * @param room Room containing the unique identifier we want to check
     * @return true if the values are equal, false otherwise
     */
    public boolean hasId(final Room room)
    {
        return (hasId(room.getId()));
    }
    
    /**
     * Does this room have the id?
     * @param id The unique identifier we want to check
     * @return true if the values are equal, false otherwise
     */
    public boolean hasId(final UUID id)
    {
        return (getId().equals(id));
    }
    
    /**
     * Assign the id
     * @param room The room containing the unique identifier we want to assign
     */
    public final void setId(final Room room)
    {
        setId(room.getId());
    }
    
    /**
     * Assign the id
     * @param id The unique identifier we want to assign
     */
    public final void setId(final UUID id)
    {
        this.id = id;
    }
    
    /**
     * Add the wall to the room.
     * @param wall The wall we want to add
     * @return true if the wall was added, false otherwise
     */
    public boolean addWall(final Wall wall)
    {
        //if we already have it, it will not be added
        if (hasWall(wall))
            return false;
        
        //add the wall to the list
        getWalls().add(wall);
        
        //wall was successfully added
        return true;
    }
    
    /**
     * Do we have the wall?
     * @param wall The wall we want to check
     * @return true if the wall exists, false otherwise
     */
    public boolean hasWall(final Wall wall)
    {
        //we can't have the wall if our list is empty
        if (walls.isEmpty())
            return false;
        
        //check each wall in our list
        for (int index = 0; index < getWalls().size(); index++)
        {
            //if we have the wall return true
            if (getWalls().get(index) == wall)
                return true;
        }
        
        //we did not find the wall return false
        return false;
    }
    
    /**
     * Add all possible walls to the list
     */
    public void addAllWalls()
    {
        for (Wall wall : Wall.values())
        {
            addWall(wall);
        }
    }
    
    /**
     * Remove all walls from this room
     */
    public void removeAllWalls()
    {
        getWalls().clear();
    }
    
    /**
     * Remove a wall
     * @param wall The wall we want to remove
     * @return true if the wall was successfully removed, false otherwise
     */
    public boolean removeWall(final Wall wall)
    {
        //check all the existing walls in our list
        for (int index = 0; index < getWalls().size(); index++)
        {
            //if the walls match
            if (getWalls().get(index) == wall)
            {
                //remove the wall
                getWalls().remove(index);
                
                //a wall was removed return true;
                return true;
            }
        }
        
        //no walls were removed
        return false;
    }
    
    /**
     * Get the walls
     * @return The list of walls in this room
     */
    public List<Wall> getWalls()
    {
        return this.walls;
    }
    
    /**
     * Assign the cost
     * @param cost The desired cost of this room
     */
    public void setCost(final int cost)
    {
        this.cost = cost;
    }
    
    /**
     * Get the cost
     * @return The desired cost of this room.
     */
    public int getCost()
    {
        return this.cost;
    }
    
    /**
     * Assign the room visited
     * @param visited true = yes, false = no
     */
    public void setVisited(final boolean visited)
    {
        this.visited = visited;
    }
    
    /**
     * Has this room been visited?
     * @return true = yes, false = no
     */
    public boolean hasVisited()
    {
        return this.visited;
    }
    
    /**
     * Do we have this location?
     * @param room The room containing the location we want to check
     * @return true if the specified (col, row) match this room's (col, row). False otherwise
     */
    public boolean hasLocation(final Room room)
    {
        return hasLocation(room.getCol(), room.getRow());
    }
    
    /**
     * Do we have this location?
     * @param col Column
     * @param row Row
     * @return true if the specified (col, row) match this room's (col, row). False otherwise
     */
    public boolean hasLocation(final int col, final int row)
    {
        return (getCol() == col && getRow() == row);
    }
}