package com.gamesbykevin.androidframeworkv2.maze;

import com.gamesbykevin.androidframeworkv2.maze.Room.Wall;

import java.util.ArrayList;
import java.util.List;

/**
 * Maze Helper methods
 * @author GOD
 */
public class MazeHelper 
{
    /**
     * Join the rooms.<br>
     * We will remove the wall from both rooms to create a passage.
     * @param room1 Room 1
     * @param room2 Room 2
     * @throws Exception If the rooms are not neighbors
     */
    public static void joinRooms(final boolean hexagon, final Room room1, final Room room2) throws Exception
    {
        //calculate how distant the rooms are from one another
        int colDiff = (room1.getCol() > room2.getCol()) ? room1.getCol() - room2.getCol() : room2.getCol() - room1.getCol();
        int rowDiff = (room1.getRow() > room2.getRow()) ? room1.getRow() - room2.getRow() : room2.getRow() - room1.getRow();

        //make sure the distance isn't too far apart
        if (colDiff > 1 || colDiff < -1 || rowDiff > 1 || rowDiff < -1)
            throw new Exception("The rooms are not neighbors");

        if (hexagon) {

            //north east
            if (room1.getRow() > room2.getRow()) {
                if (room1.getRow() % 2 == 0) {
                    if (room1.getCol() == room2.getCol()) {
                        room1.removeWall(Wall.NorthEast);
                        room2.removeWall(Wall.SouthWest);
                    }
                } else {
                    if (room1.getCol() < room2.getCol()) {
                        room1.removeWall(Wall.NorthEast);
                        room2.removeWall(Wall.SouthWest);
                    }
                }
            }

            //south east
            if (room1.getRow() < room2.getRow()) {
                if (room1.getRow() % 2 == 0) {
                    if (room1.getCol() == room2.getCol()) {
                        room1.removeWall(Wall.SouthEast);
                        room2.removeWall(Wall.NorthWest);
                    }
                } else {
                    if (room1.getCol() < room2.getCol()) {
                        room1.removeWall(Wall.SouthEast);
                        room2.removeWall(Wall.NorthWest);
                    }
                }
            }

            //west
            if (room1.getRow() == room2.getRow() && room1.getCol() > room2.getCol()) {
                room1.removeWall(Wall.West);
                room2.removeWall(Wall.East);
            }

            //east
            if (room1.getRow() == room2.getRow() && room1.getCol() < room2.getCol()) {
                room1.removeWall(Wall.East);
                room2.removeWall(Wall.West);
            }

            //north west
            if (room1.getRow() > room2.getRow()) {
                if (room1.getRow() % 2 == 0) {
                    if (room1.getCol() > room2.getCol()) {
                        room1.removeWall(Wall.NorthWest);
                        room2.removeWall(Wall.SouthEast);
                    }
                } else {
                    if (room1.getCol() == room2.getCol()) {
                        room1.removeWall(Wall.NorthWest);
                        room2.removeWall(Wall.SouthEast);
                    }
                }
            }

            //south west
            if (room1.getRow() < room2.getRow()) {
                if (room1.getRow() % 2 == 0) {
                    if (room1.getCol() > room2.getCol()) {
                        room1.removeWall(Wall.SouthWest);
                        room2.removeWall(Wall.NorthEast);
                    }
                } else {
                    if (room1.getCol() == room2.getCol()) {
                        room1.removeWall(Wall.SouthWest);
                        room2.removeWall(Wall.NorthEast);
                    }
                }
            }

        } else {

            //if the columns are not equal they are horizontal neighbors
            if (colDiff != 0) {
                if (room1.getCol() > room2.getCol()) {
                    room1.removeWall(Room.Wall.West);
                    room2.removeWall(Room.Wall.East);
                } else {
                    room1.removeWall(Room.Wall.East);
                    room2.removeWall(Room.Wall.West);
                }
            } else {

                //they are vertical neighbors
                if (room1.getRow() > room2.getRow()) {
                    room1.removeWall(Room.Wall.North);
                    room2.removeWall(Room.Wall.South);
                } else {
                    room1.removeWall(Room.Wall.South);
                    room2.removeWall(Room.Wall.North);
                }
            }
        }
    }
    
    /**
     * We will assign the cost of each cell.<br>
     * The starting point will have a cost of 0.<br>
     * Each neighboring cell will contain the cost of the previous location + 1.<br>
     */
    public static void calculateCost(final Maze maze) throws Exception
    {
        //mark all rooms as not visited so we know which ones to check
        for (int row = 0; row < maze.getRows(); row++)
        {
            for (int col = 0; col < maze.getCols(); col++)
            {
                maze.getRoom(col, row).setVisited(false);
            }
        }
        
        //create empty list of optional walls
        List<Room> options = new ArrayList<>();
        
        //get the room at the starting point 
        Room room = maze.getRoom(maze.getStartCol(), maze.getStartRow());
        
        //the starting point will have a cost of 0
        room.setCost(0);
        
        //add the room to our options list
        options.add(room);
        
        //continue as long as we have rooms to check
        while (!options.isEmpty())
        {
            //get the current room
            room = options.get(0);
            
            //flag the current room as visited
            room.setVisited(true);

            //check if the room in the specified direction can be added to the options list
            for (Wall wall : Room.getAllWalls(maze.isHexagon())) {
                performRoomCheck(maze, room, wall, options);
            }

            //now that we have checked the current room, remove from our list
            options.remove(0);
        }
    }
    
    /**
     * Check if we can add the neighbor room to the existing options list.<br>
     * If so we will add to the options list.
     * @param current The current room we are in
     * @param direction The direction we want to check from the current room
     * @param options List of existing rooms we can add to
     * @throws Exception If the specified direction is not handled
     */
    private static void performRoomCheck(final Maze maze, final Room current, final Room.Wall direction, final List<Room> options) throws Exception
    {
        //get the neighbor room
        Room neighbor = maze.getRoomNeighbor(current, direction);

        //make sure room exists and we haven't already visited
        if (neighbor != null && !neighbor.hasVisited())
        {
            //if there isn't a wall blocking the current room
            if (!current.hasWall(direction))
            {
                //assign the cost
                neighbor.setCost(current.getCost() + 1);
                
                //mark if as visited
                neighbor.setVisited(true);
                
                //add it to our list of rooms to check
                options.add(neighbor);
            }
        }
    }
    
    /**
     * Locate the finish position automatically.<br>
     * We will assign the cost of reach room.<br>
     * Then the room with the highest cost (room furthest from starting point) will be the finish.
     */
    public static void locateFinish(final Maze maze) throws Exception
    {
        //calculate the cost of each cell
        calculateCost(maze);
        
        //the cost to beat
        int cost = 0;
        
        for (int row = 0; row < maze.getRows(); row++)
        {
            for (int col = 0; col < maze.getCols(); col++)
            {
                final Room room = maze.getRoom(col, row);
                
                //if the cost is higher than our record
                if (room.getCost() > cost)
                {
                    //assign the winning cost
                    cost = room.getCost();
                    
                    //assign the finish location
                    maze.setFinishLocation(col, row);
                }
            }
        }
    }
    
    /**
     * Do we have a visited room?<br>
     * This can be used to determine if we have started to create our maze
     * @return true if at least 1 room has been visited, false otherwise
     */
    public static boolean hasVisited(final Maze maze)
    {
        for (int row = 0; row < maze.getRows(); row++)
        {
            for (int col = 0; col < maze.getCols(); col++)
            {
                //if this room has been visited return true
                if (maze.getRoom(col, row).hasVisited())
                    return true;
            }
        }
        
        //no rooms have been visited
        return false;
    }
    
    /**
     * Update all room as visited/un-visited
     * @param visited True if we want the rooms visited, false otherwise
     */
    public static void setVisitedAll(final Maze maze, final boolean visited)
    {
        for (int row = 0; row < maze.getRows(); row++)
        {
            for (int col = 0; col < maze.getCols(); col++)
            {
                maze.getRoom(col, row).setVisited(visited);
            }
        }
    }
}