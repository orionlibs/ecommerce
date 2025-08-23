package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class DefaultDumpHandler extends AbstractDumpHandler
{
    private static final Logger log = Logger.getLogger(DefaultDumpHandler.class.getName());
    private File curDumpFile;
    private File lastDumpFile;


    public void init() throws IOException
    {
        setWriterOfCurrentDump(createWriter());
    }


    public void finish(boolean saveCurrentDump)
    {
        deleteLastDump();
        if(saveCurrentDump)
        {
            log.info("Dump file is located at " + this.curDumpFile.getAbsolutePath());
        }
        else
        {
            deleteCurrentDump();
        }
    }


    public void switchDump() throws ImpExException
    {
        deleteLastDump();
        switchCurrentToLastDump();
        try
        {
            setReaderOfLastDump(createReader());
            setWriterOfCurrentDump(createWriter());
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
    }


    public File getDumpAsFile()
    {
        return this.curDumpFile;
    }


    public ImpExMedia getDumpAsMedia()
    {
        return ImpExManager.getInstance().createImpExMedia("unresolved_lines");
    }


    protected void deleteLastDump()
    {
        if(this.lastDumpFile != null)
        {
            if(!FileUtils.deleteQuietly(this.lastDumpFile))
            {
                log.warn("Can not delete temp file: " + this.lastDumpFile.getAbsolutePath());
            }
        }
    }


    protected void deleteCurrentDump()
    {
        if(this.curDumpFile != null)
        {
            if(!FileUtils.deleteQuietly(this.curDumpFile))
            {
                log.warn("Can not delete temp file: " + this.curDumpFile.getAbsolutePath());
            }
            this.curDumpFile = null;
        }
    }


    protected void switchCurrentToLastDump()
    {
        this.lastDumpFile = this.curDumpFile;
    }


    protected CSVReader createReader() throws IOException
    {
        return new CSVReader(this.lastDumpFile, CSVConstants.DEFAULT_ENCODING);
    }


    protected CSVWriter createWriter() throws IOException
    {
        this.curDumpFile = File.createTempFile("dumpfile", ".tmp");
        try
        {
            return new CSVWriter(this.curDumpFile, CSVConstants.DEFAULT_ENCODING);
        }
        catch(IOException e)
        {
            deleteCurrentDump();
            throw e;
        }
    }
}
