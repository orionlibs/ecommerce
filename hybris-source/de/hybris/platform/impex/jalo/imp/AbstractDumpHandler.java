package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import java.io.File;

public abstract class AbstractDumpHandler implements DumpHandler
{
    private CSVReader readerOfLastDump;
    private CSVWriter writerOfCurrentDump;


    public CSVReader getReaderOfLastDump()
    {
        return this.readerOfLastDump;
    }


    public CSVWriter getWriterOfCurrentDump()
    {
        return this.writerOfCurrentDump;
    }


    protected void setWriterOfCurrentDump(CSVWriter writer)
    {
        this.writerOfCurrentDump = writer;
    }


    protected void setReaderOfLastDump(CSVReader reader)
    {
        this.readerOfLastDump = reader;
    }


    public File getDumpAsFile()
    {
        throw new UnsupportedOperationException("Returning dump as file is not supported from this handler");
    }


    public ImpExMedia getDumpAsMedia()
    {
        throw new UnsupportedOperationException("Returning dump as media is not supported from this handler");
    }


    public String getDumpAsString()
    {
        throw new UnsupportedOperationException("Returning dump as string is not supported from this handler");
    }
}
