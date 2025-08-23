package de.hybris.platform.directpersistence.impl;

public class SLDResultCheckRow
{
    private final long currentHJMPTS;
    private final boolean isLocked;
    private final int numberOfRows;


    public SLDResultCheckRow(long currentHJMPTS, boolean isLocked, int numberOfRows)
    {
        this.currentHJMPTS = currentHJMPTS;
        this.isLocked = isLocked;
        this.numberOfRows = numberOfRows;
    }


    public SLDResultCheckRow(int numberOfRows)
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
