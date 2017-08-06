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

    //list of rooms for us to check on
    private List<Room> checking;

    public Prims(final boolean hexagon, final int cols, final int rows) throws Exception
    {
        super(hexagon, cols, rows);
        
        //set 4 walls for each room
        super.populateRooms();
        
        //create a new list of optional rooms
        this.options = new ArrayList<>();
        this.checking = new ArrayList<>();
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
            checking.clear();

            //check if any neighbors can be joined to the room
            if (isHexagon()) {

                checking.add(getRoom(room.getCol(), room.getRow() - 1));    //north
                checking.add(getRoom(room.getCol(), room.getRow() + 1));    //south
                checking.add(getRoom(room.getCol() + 1, room.getRow() - 1));//north east
                checking.add(getRoom(room.getCol() + 1, room.getRow() + 1));//south east
                checking.add(getRoom(room.getCol() - 1, room.getRow() - 1));//north west
                checking.add(getRoom(room.getCol() - 1, room.getRow() + 1));//south west

            } else {

                checking.add(getRoom(room.getCol() + 1, room.getRow()));    //east
                checking.add(getRoom(room.getCol() - 1, room.getRow()));    //west
                checking.add(getRoom(room.getCol(), room.getRow() - 1));    //north
                checking.add(getRoom(room.getCol(), room.getRow() + 1));    //south
            }

            //list of choices to move to
            final List<Room> choices = new ArrayList<Room>();

            //we only want to add the room that exists and is already visited to join with
            for (int i = 0; i < checking.size(); i++) {
                Room tmp = checking.get(i);

                if (tmp != null && tmp.hasVisited())
                    choices.add(tmp);
            }

            //the other room
            final Room other = choices.get(random.nextInt(choices.size()));
            
            //join the rooms
            MazeHelper.joinRooms(isHexagon(), room, other);
        }
        
        //mark the room as visited
        room.setVisited(true);


        checking.clear();

        //check if any neighbors can be joined to the room
        if (isHexagon()) {

            checking.add(getRoom(room.getCol(), room.getRow() - 1));    //north
            checking.add(getRoom(room.getCol(), room.getRow() + 1));    //south
            checking.add(getRoom(room.getCol() + 1, room.getRow() - 1));//north east
            checking.add(getRoom(room.getCol() + 1, room.getRow() + 1));//south east
            checking.add(getRoom(room.getCol() - 1, room.getRow() - 1));//north west
            checking.add(getRoom(room.getCol() - 1, room.getRow() + 1));//south west

        } else {

            checking.add(getRoom(room.getCol() + 1, room.getRow()));    //east
            checking.add(getRoom(room.getCol() - 1, room.getRow()));    //west
            checking.add(getRoom(room.getCol(), room.getRow() - 1));    //north
            checking.add(getRoom(room.getCol(), room.getRow() + 1));    //south
        }

        //add any optional directions that haven't been visited and don't already exist in the list
        for (int i = 0; i < checking.size(); i++) {
            Room tmp = checking.get(i);

            if (tmp != null && !tmp.hasVisited() && !options.contains(tmp))
                options.add(tmp);
        }

        //increase the progress
        super.getProgress().increase();
        
        //remove from the list
        options.remove(index);
    }
}