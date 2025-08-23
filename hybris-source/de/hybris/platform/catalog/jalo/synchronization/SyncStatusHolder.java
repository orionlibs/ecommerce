package de.hybris.platform.catalog.jalo.synchronization;

public interface SyncStatusHolder
{
    int getLastLine();


    int getLastTurnDumpCount();


    int getLastLineDumpCount();


    int getTurn();


    boolean isErrorsOccured();


    void logBeginTurn(int paramInt);


    void logFinishTurn(int paramInt1, int paramInt2);


    void logLine(int paramInt1, int paramInt2, boolean paramBoolean);


    void syncDone();


    void syncAborted();
}
