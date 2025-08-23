package de.hybris.platform.catalog.jalo.synchronization;

public class DefaultSyncStatusHolder implements SyncStatusHolder
{
    private volatile int lastLine = 0;
    private volatile int lastTurnDumpCount = -1;
    private volatile int lastDumpCount = -1;
    private volatile int turn = 1;
    private volatile boolean errorsOccured = false;


    synchronized void init(byte turn, int lastTurnDumpCount, int lastLine, int lastDumpCount, boolean errorsOccured)
    {
        this.turn = turn;
        this.lastTurnDumpCount = lastTurnDumpCount;
        this.lastLine = lastLine;
        this.lastDumpCount = lastDumpCount;
        this.errorsOccured = errorsOccured;
    }


    public int getLastLine()
    {
        return this.lastLine;
    }


    public int getLastTurnDumpCount()
    {
        return this.lastTurnDumpCount;
    }


    public int getLastLineDumpCount()
    {
        return this.lastDumpCount;
    }


    public int getTurn()
    {
        return this.turn;
    }


    public boolean isErrorsOccured()
    {
        return this.errorsOccured;
    }


    public void logBeginTurn(int turn)
    {
        this.turn = turn;
    }


    public synchronized void logFinishTurn(int turn, int turnDumpCount)
    {
        this.turn = turn;
        this.lastTurnDumpCount = turnDumpCount;
        this.lastLine = 0;
        this.lastDumpCount = 0;
    }


    public synchronized void logLine(int lastLine, int lastLineDumpCount, boolean errorsOccured)
    {
        this.lastLine = lastLine;
        this.lastDumpCount = lastLineDumpCount;
        this.errorsOccured = errorsOccured;
    }


    public void syncDone()
    {
    }


    public void syncAborted()
    {
    }


    public String toString()
    {
        return "SyncStatus( turn=" + getTurn() + ", prevDump=" + getLastTurnDumpCount() + ", lastLine=" + getLastLine() + ", lastLineDump=" +
                        getLastLineDumpCount() + ", errors=" + isErrorsOccured() + ")";
    }
}
