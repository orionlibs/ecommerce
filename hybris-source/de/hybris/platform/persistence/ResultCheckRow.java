package de.hybris.platform.persistence;

public class ResultCheckRow
{
    private final long currentHJMPTS;
    private final boolean isLocked;
    private final int numberOfRows;


    public ResultCheckRow(long currentHJMPTS, boolean isLocked, int numberOfRows)
    {
        this.currentHJMPTS = currentHJMPTS;
        this.isLocked = isLocked;
        this.numberOfRows = numberOfRows;
    }


    public ResultCheckRow(int numberOfRows)
    {
        this.currentHJMPTS = -1L;
        this.isLocked = false;
        this.numberOfRows = numberOfRows;
    }


    public long getCurrentHJMPTS()
    {
        return this.currentHJMPTS;
    }


    public boolean isLocked()
    {
        return this.isLocked;
    }


    public int getNumberOfRows()
    {
        return this.numberOfRows;
    }
}
