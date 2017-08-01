package com.gamesbykevin.androidframeworkv2.base;

/**
 * Created by Kevin on 7/31/2017.
 */

public class Cell
{
    //the location
    private double col, row;

    /**
     * Create a new cell with (0,0) location
     */
    public Cell()
    {
        this(0,0);
    }

    /**
     * Create a new cell of the specified location
     * @param col Column
     * @param row Row
     */
    public Cell(final double col, final double row)
    {
        this.col = col;
        this.row = row;
    }

    /**
     * Get the assigned column
     * @return The assigned column
     */
    public double getCol()
    {
        return this.col;
    }

    /**
     * Get the assigned row
     * @return The assigned row
     */
    public double getRow()
    {
        return this.row;
    }

    /**
     * Assign the column
     * @param cell Cell containing the desired column
     */
    public void setCol(final Cell cell)
    {
        setCol(cell.getCol());
    }

    /**
     * Assign the column
     * @param col
     */
    public void setCol(final double col)
    {
        this.col = col;
    }

    /**
     * Assign the row
     * @param cell Cell containing the desired row
     */
    public void setRow(final Cell cell)
    {
        setRow(cell.getRow());
    }

    /**
     * Assign the row
     * @param row
     */
    public void setRow(final double row)
    {
        this.row = row;
    }

    /**
     * Do we have this location?
     * @param cell Cell containing the (column, row)
     * @return true if the parameter Cell (column, row) matches the (column, row) of this Cell
     */
    public boolean hasLocation(final Cell cell)
    {
        return hasLocation(cell.getCol(), cell.getRow());
    }

    /**
     * Do we have this location?
     * @param col Column
     * @param row Row
     * @return true if the specified (column, row) match the (column, row) of this Cell
     */
    public boolean hasLocation(final double col, final double row)
    {
        return (getCol() == col && getRow() == row);
    }
}