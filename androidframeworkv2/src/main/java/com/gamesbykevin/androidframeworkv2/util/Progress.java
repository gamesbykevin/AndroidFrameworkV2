package com.gamesbykevin.androidframeworkv2.util;

import com.gamesbykevin.androidframeworkv2.base.Disposable;

public class Progress implements Disposable
{
    //the goal will be the logic used to determine Progress isComplete()
    private int goal  = 0;
    
    //the count will be the logic used to track how close we are to goal
    private int count = 0;
    
    /**
     * Create new Progress tracker with the desired goal set.<br>
     * @param goal The goal we are trying to reach
     */
    public Progress(final int goal)
    {
        setGoal(goal);
    }
    
    @Override
    public void dispose()
    {
        //clean up anything here?
    }
    
    /**
     * Get the goal we have set
     * @return double The goal we are trying to reach
     */
    public int getGoal()
    {
        return goal;
    }
    
    /**
     * Set the goal we want.
     * @param goal The goal we are trying to reach
     */
    public final void setGoal(final int goal)
    {
        this.goal = goal;
    }
    
    /**
     * Where are we currently at in regards to our goal
     * @return double The number of updates made to this object
     */
    public int getCount()
    {
        return count;
    }
    
    /**
     * Increase the progress towards our goal by 1
     */
    public void increase()
    {
        setCount(getCount() + 1);
    }
    
    /**
     * Set the progress towards our goal
     * @param count The completed progress
     */
    public void setCount(final int count)
    {
        this.count = count;
    }
    
    /**
     * Make the progress 100% complete<br>
     * We do this my assigning the count the same value of the goal
     */
    public void markComplete()
    {
        setCount(getGoal());
    }
    
    /**
     * Get the progress towards reaching the goal in the form of a decimal.
     * @return double Progress typically ranging from 0.0 to 1.0
     */
    public double getProgress()
    {
        return ((double)getCount() / (double)getGoal());
    }
    
    /**
     * Has the count reached the goal
     * @return boolean
     */
    public boolean isComplete()
    {
        return (getCount() >= getGoal());
    }
    
    /**
     * Calculate the progress complete as a percentage. (not a decimal)
     * @return int Percent complete typically from 0% - 100%
     */
    private int getCompleteProgress()
    {
        int complete = (int)(getProgress() * 100);
        
        if (complete >= 100)
            complete = 100;
        
        return complete;
    }
}