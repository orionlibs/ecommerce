package de.hybris.platform.catalog.jalo.synchronization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RAFileSyncStatusWriter extends DefaultSyncStatusHolder
{
    private static final long POS_TURN = 0L;
    private static final long POS_LAST_TURN_DUMP_COUNT = 4L;
    private static final long POS_LAST_LINE = 8L;
    private static final long POS_LAST_LINE_DUMP_COUNT = 12L;
    private static final long POS_ERRORS = 16L;
    private static final long FILE_SIZE = 14L;
    private boolean closed = false;
    private final File dataFile;
    private final RandomAccessFile raFile;


    RAFileSyncStatusWriter(String fileName)
    {
        try
        {
            this.raFile = new RandomAccessFile(this.dataFile = new File(fileName), "rwd");
            this.closed = false;
        }
        catch(FileNotFoundException e)
        {
            throw new IllegalStateException("cannot open or create sync status file", e);
        }
        try
        {
            if(this.raFile.length() == 0L)
            {
                initNewRAFile(fileName);
            }
            else
            {
                if(this.raFile.length() != 14L)
                {
                    throw new IllegalStateException("wrong sync status file size - expected 14 but was " + this.raFile
                                    .length());
                }
                initFromRAFile(fileName);
            }
        }
        catch(IOException e)
        {
            throw new IllegalStateException("could not read sync status file", e);
        }
    }


    private final void initNewRAFile(String fileName) throws IOException
    {
        this.raFile.writeByte(getTurn());
        this.raFile.writeInt(getLastTurnDumpCount());
        this.raFile.writeInt(getLastLine());
        this.raFile.writeInt(getLastLineDumpCount());
        this.raFile.writeBoolean(isErrorsOccured());
        System.err.println("created new SyncStatusWriter(fileName:" + fileName + ",turn:" + getTurn() + ",lastDumpCount:" +
                        getLastTurnDumpCount() + ",lastLine:" + getLastLine() + ",lastLineDump:" + getLastLineDumpCount() + ",errors:" +
                        isErrorsOccured());
    }


    private final void initFromRAFile(String fileName) throws IOException
    {
        byte turn = this.raFile.readByte();
        int lastTurnDumpCount = this.raFile.readInt();
        int lastLine = this.raFile.readInt();
        int lastDumpCount = this.raFile.readInt();
        boolean errorsOccured = this.raFile.readBoolean();
        init(turn, lastTurnDumpCount, lastLine, lastDumpCount, errorsOccured);
        System.err.println("read new SyncStatusWriter(fileName:" + fileName + ",turn:" + getTurn() + ",lastDumpCount:" +
                        getLastTurnDumpCount() + ",lastLine:" + getLastLine() + ",lastLineDump:" + getLastLineDumpCount() + ",errors:" +
                        isErrorsOccured());
    }


    public synchronized void logBeginTurn(int turn)
    {
        if(!this.closed)
        {
            try
            {
                if(getTurn() != turn)
                {
                    super.logBeginTurn(turn);
                    this.raFile.seek(0L);
                    this.raFile.writeInt(turn);
                }
            }
            catch(IOException e)
            {
                throw new RuntimeException("error writing turn begin status", e);
            }
        }
    }


    public synchronized void logFinishTurn(int turn, int turnDumpCount)
    {
        if(!this.closed)
        {
            try
            {
                super.logFinishTurn(turn, turnDumpCount);
                this.raFile.seek(0L);
                this.raFile.writeByte(getTurn());
                this.raFile.seek(4L);
                this.raFile.writeInt(getLastTurnDumpCount());
                this.raFile.seek(8L);
                this.raFile.writeInt(getLastLine());
                this.raFile.seek(12L);
                this.raFile.writeInt(getLastLineDumpCount());
            }
            catch(IOException e)
            {
                throw new RuntimeException("error writing turn begin status", e);
            }
        }
    }


    public synchronized void logLine(int lastLine, int lastLineDumpCount, boolean errorsOccured)
    {
        if(!this.closed)
        {
            try
            {
                boolean lastLineChanged = (getLastLine() != lastLine);
                boolean lastLineDumpCountChanged = (getLastLineDumpCount() != lastLineDumpCount);
                boolean errorChanged = (isErrorsOccured() != errorsOccured);
                super.logLine(lastLine, lastLineDumpCount, errorsOccured);
                if(lastLineChanged)
                {
                    this.raFile.seek(8L);
                    this.raFile.writeInt(getLastLine());
                }
                if(lastLineDumpCountChanged)
                {
                    this.raFile.seek(12L);
                    this.raFile.writeInt(getLastLineDumpCount());
                }
                if(errorChanged)
                {
                    this.raFile.seek(16L);
                    this.raFile.writeBoolean(isErrorsOccured());
                }
            }
            catch(IOException e)
            {
                throw new RuntimeException("error writing line status", e);
            }
        }
    }


    public synchronized void syncDone()
    {
        if(!this.closed)
        {
            try
            {
                super.syncDone();
                this.closed = true;
                this.raFile.close();
                this.dataFile.delete();
            }
            catch(IOException iOException)
            {
            }
        }
    }


    public synchronized void syncAborted()
    {
        if(!this.closed)
        {
            try
            {
                this.closed = true;
                this.raFile.close();
            }
            catch(IOException iOException)
            {
            }
        }
    }
}
