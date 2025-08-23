package de.hybris.platform.impex.jalo;

import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.DefaultDumpHandler;
import de.hybris.platform.impex.jalo.imp.DumpHandler;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.logging.HybrisLogFilter;
import de.hybris.platform.util.logging.HybrisLogger;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Importer implements ImpExLogFilter.LocationProvider
{
    private static final Logger LOG = Logger.getLogger(Importer.class.getName());
    private ImpExImportReader importReader = null;
    private STATE curState = STATE.INIT;
    private DumpHandler dumpHandler = null;
    private ErrorHandler errorHandler = null;
    private int curPass = 0;
    private int maxPass = -1;
    private int dumpedLineCountLastPass;
    private int lastCumulativeProcessedItemsCount = 0;
    private int lastCumulativeDumpedLineCount = 0;
    private int lastCumulativeResolvedItemsCount = 0;
    private long lastLogResolvedItems = 0L;
    private long startTime = 0L;
    private long passStartTime = -1L;
    private long logTime = 0L;
    public static final long LOG_INTERVAL = 60000L;
    private boolean error = false;
    private ImpExLogFilter filter;


    public Importer(CSVReader source)
    {
        this.importReader = createImportReader(source);
    }


    public Importer(ImpExImportReader importReader)
    {
        this.importReader = importReader;
    }


    protected void init() throws ImpExException
    {
        if(this.filter == null)
        {
            this.filter = new ImpExLogFilter();
            this.filter.registerLocationProvider(this);
        }
        if(this.dumpHandler == null)
        {
            this.dumpHandler = (DumpHandler)new DefaultDumpHandler();
        }
        if(this.errorHandler == null)
        {
            this.errorHandler = (ErrorHandler)new AlwaysFailErrorHandler();
        }
        try
        {
            this.dumpHandler.init();
            this.importReader.setCSVWriter(this.dumpHandler.getWriterOfCurrentDump());
        }
        catch(IOException e)
        {
            throw new ImpExException(e, "Can not create dump file", 0);
        }
        this.startTime = System.currentTimeMillis();
        this.logTime = this.startTime;
        this.curPass++;
        switchToState(STATE.RUNNING);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Starting import with pass " + getCurrentPass() + ((getMaxPass() > 0) ? (" of maximal " + getMaxPass()) : ""));
        }
    }


    protected void switchToState(STATE newState)
    {
        if(newState.ordinal() < this.curState.ordinal())
        {
            throw new IllegalStateException("Current state " + this.curState.name() + " can not be set to " + newState.name());
        }
        this.curState = newState;
    }


    protected void logProcess()
    {
        if(LOG.isInfoEnabled())
        {
            long curTime = System.currentTimeMillis();
            long timeDiff = curTime - this.logTime;
            if(timeDiff > 60000L)
            {
                this.logTime = curTime;
                long curResolvedItems = getResolvedItemsCountOverall();
                long itemsDiff = curResolvedItems - this.lastLogResolvedItems;
                long rate = itemsDiff * 1000L / timeDiff;
                LOG.info("Processed and resolved " + itemsDiff + " items in " + timeDiff / 1000L + " s (speed " + rate + " items/s). Overall processed and resolved items: " + curResolvedItems + ".");
                this.lastLogResolvedItems = curResolvedItems;
            }
        }
    }


    protected Item doImport() throws ImpExException
    {
        while(true)
        {
            try
            {
                return (Item)this.importReader.readLine();
            }
            catch(ImpExException e)
            {
                try
                {
                    if(this.errorHandler.handleError(e, (ImpExReader)this.importReader) == ErrorHandler.RESULT.FAIL)
                    {
                        throw e;
                    }
                    this.error = true;
                }
                catch(Exception excInErrorHandler)
                {
                    LOG.error("Exception '" + excInErrorHandler.getMessage() + "' in handling exception: " + e.getMessage());
                    throw e;
                }
            }
        }
    }


    protected boolean prepareNextPass() throws ImpExException
    {
        long passEndTime = System.currentTimeMillis();
        boolean ret = false;
        if(getDumpedLineCountPerPass() > 0)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Finished " + getCurrentPass() + " pass in " + Utilities.formatTime(passEndTime - this.passStartTime) + " - processed: " + this.importReader
                                .getProcessedItemsCount() + ", dumped: " + getDumpedLineCountPerPass() + " (last pass: " + this.dumpedLineCountLastPass + ")");
            }
            if((getMaxPass() < 0 || getCurrentPass() <= getMaxPass()) && (
                            getCurrentPass() == 1 || getDumpedLineCountPerPass() < this.dumpedLineCountLastPass))
            {
                close();
                this.dumpedLineCountLastPass = getDumpedLineCountPerPass();
                try
                {
                    this.dumpHandler.switchDump();
                }
                catch(ImpExException e)
                {
                    throw new ImpExException(e, "Error while switching dump file", -1);
                }
                this.lastCumulativeDumpedLineCount += this.importReader.getDumpedLineCount();
                this.lastCumulativeProcessedItemsCount += this.importReader.getProcessedItemsCount();
                this.lastCumulativeResolvedItemsCount += this.importReader.getResolvedItemsCount();
                this.importReader = createImportReaderForNextPass();
                this.curPass++;
                this.passStartTime = System.currentTimeMillis();
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Starting pass " + getCurrentPass() + ((getMaxPass() > 0) ? (" of maximal " + getMaxPass()) : ""));
                }
                ret = true;
            }
            else
            {
                StringBuilder errorMessage = new StringBuilder("Can not resolve any more lines ... ");
                if(getCurrentPass() == getMaxPass())
                {
                    errorMessage.append("No more passes left.");
                }
                else
                {
                    errorMessage.append("Aborting further passes (at pass ").append(getCurrentPass())
                                    .append(((getMaxPass() > 0) ? (" of " + getMaxPass()) : "") + ").");
                }
                errorMessage.append(" Finally could not import ").append(getDumpedLineCountPerPass()).append(" lines!");
                ImpExException impexException = new ImpExException(errorMessage.toString(), 123);
                if(this.errorHandler.handleError(impexException, (ImpExReader)this.importReader) == ErrorHandler.RESULT.FAIL)
                {
                    throw impexException;
                }
            }
        }
        else if(LOG.isInfoEnabled())
        {
            LOG.info("Finished " + getCurrentPass() + " pass in " + Utilities.formatTime(passEndTime - this.passStartTime) + " - processed: " + this.importReader
                            .getProcessedItemsCount() + ", no lines dumped (last pass " + this.dumpedLineCountLastPass + ")");
        }
        return ret;
    }


    protected void finishImport()
    {
        close();
        this.dumpHandler.finish((getDumpedLineCountPerPass() > 0));
        switchToState(STATE.FINISHED);
        if(this.error)
        {
            LOG.warn("Import finished with errors within " + Utilities.formatTime(System.currentTimeMillis() - this.startTime));
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Import finished successfully within " + Utilities.formatTime(System.currentTimeMillis() - this.startTime));
        }
    }


    public void abortImport()
    {
        if(!isAborted())
        {
            close();
            this.dumpHandler.finish((getDumpedLineCountPerPass() > 0));
            switchToState(STATE.ABORTED);
            LOG.warn("Import aborted after " + Utilities.formatTime(System.currentTimeMillis() - this.startTime));
        }
    }


    protected void assureStates(STATE... states)
    {
        boolean isOk = false;
        for(STATE state : states)
        {
            if(this.curState == state)
            {
                isOk = true;
            }
        }
        if(!isOk)
        {
            throw new IllegalStateException();
        }
    }


    protected ImpExImportReader createImportReader(CSVReader csvReader)
    {
        return new ImpExImportReader(csvReader);
    }


    protected ImpExImportReader createImportReaderForNextPass()
    {
        ImpExImportReader reader = new ImpExImportReader(this.dumpHandler.getReaderOfLastDump(), this.dumpHandler.getWriterOfCurrentDump(), this.importReader.getDocumentIDRegistry(), this.importReader.getImportProcessor(), this.importReader.getValidationMode(), this.importReader.isLegacyMode());
        reader.setIsSecondPass();
        reader.setLocale(this.importReader.getLocale());
        return reader;
    }


    public boolean isRunning()
    {
        return (this.curState == STATE.RUNNING);
    }


    public boolean isFinished()
    {
        return (this.curState.ordinal() >= STATE.FINISHED.ordinal());
    }


    public boolean isAborted()
    {
        return (this.curState == STATE.ABORTED);
    }


    public int getMaxPass()
    {
        return this.maxPass;
    }


    public int getCurrentPass()
    {
        return this.curPass;
    }


    public ImpExImportReader getReader()
    {
        return this.importReader;
    }


    public DumpHandler getDumpHandler()
    {
        return this.dumpHandler;
    }


    public ErrorHandler getErrorHandler()
    {
        return this.errorHandler;
    }


    public boolean hasUnresolvedLines()
    {
        return (getDumpedLineCountPerPass() > 0);
    }


    public int getValueLineCountPerPass()
    {
        return getReader().getValueLineCount();
    }


    public int getProcessedItemsCountPerHeader()
    {
        return getReader().getProcessedItemsCountPerHeader();
    }


    public int getProcessedItemsCountPerPass()
    {
        return getReader().getProcessedItemsCount();
    }


    public int getProcessedItemsCountOverall()
    {
        return this.lastCumulativeProcessedItemsCount + getProcessedItemsCountPerPass();
    }


    public int getDumpedLineCountPerHeader()
    {
        return getReader().getDumpedLineCountPerHeader();
    }


    public int getResolvedItemsCountPerPass()
    {
        return getReader().getResolvedItemsCount();
    }


    public int getResolvedItemsCountOverall()
    {
        return this.lastCumulativeResolvedItemsCount + getResolvedItemsCountPerPass();
    }


    public int getDumpedLineCountPerPass()
    {
        return getReader().getDumpedLineCount();
    }


    public int getDumpedLineCountOverall()
    {
        return this.lastCumulativeDumpedLineCount + getDumpedLineCountPerPass();
    }


    public int getCurrentLineNumber()
    {
        return getReader().getCSVReader().getCurrentLineNumber();
    }


    public int getLastImportedItemLineNumber()
    {
        return getReader().getLastImportedItemLineNumber();
    }


    public String getCurrentLocation()
    {
        ImpExImportReader impExImportReader = getReader();
        if(impExImportReader == null)
        {
            return null;
        }
        return impExImportReader.getCurrentLocation();
    }


    public HeaderDescriptor getCurrentHeader()
    {
        return getReader().getCurrentHeader();
    }


    public void importAll() throws ImpExException
    {
        if(this.curState == STATE.INIT)
        {
            init();
        }
        assureStates(new STATE[] {STATE.RUNNING});
        try
        {
            activateLogFilter();
            Item item = null;
            do
            {
                item = importNextInternal();
            }
            while(item != null);
        }
        finally
        {
            deactivateLogFilter();
        }
    }


    public final Item importNext() throws ImpExException
    {
        if(this.curState == STATE.INIT)
        {
            init();
        }
        assureStates(new STATE[] {STATE.RUNNING});
        try
        {
            activateLogFilter();
            return importNextInternal();
        }
        finally
        {
            deactivateLogFilter();
        }
    }


    protected Item importNextInternal() throws ImpExException
    {
        Item item = null;
        if(this.passStartTime < 0L)
        {
            this.passStartTime = System.currentTimeMillis();
        }
        else
        {
            try
            {
                do
                {
                    item = doImport();
                }
                while(item == null && prepareNextPass());
            }
            catch(ImpExException e)
            {
                ImpExException newException = new ImpExException((Throwable)e, this.filter.extendMessage(e.getMessage()), e.getErrorCode());
                abortImport();
                throw newException;
            }
            catch(RuntimeException e)
            {
                ImpExException newException = new ImpExException(e, this.filter.extendMessage(e.getMessage()), 0);
                abortImport();
                throw newException;
            }
            if(item == null)
            {
                finishImport();
            }
            else
            {
                logProcess();
            }
            return item;
        }
        while(true)
        {
            item = doImport();
        }
    }


    protected void activateLogFilter()
    {
        if(this.filter != null)
        {
            HybrisLogger.addFilter((HybrisLogFilter)this.filter);
        }
    }


    protected void deactivateLogFilter()
    {
        if(this.filter != null)
        {
            this.filter.unregisterLocationProvider();
            HybrisLogger.removeFilter((HybrisLogFilter)this.filter);
        }
    }


    public void close()
    {
        if(!isFinished())
        {
            try
            {
                this.importReader.close();
            }
            catch(IOException e)
            {
                LOG.warn("Error while closing import reader, will ignore");
            }
        }
    }


    public void setMaxPass(int maxPass)
    {
        assureStates(new STATE[] {STATE.INIT});
        if(maxPass < -1 && maxPass != 0)
        {
            LOG.warn("Max pass attribute of importer class can not be set to 0 or a value lower than -1. Given value was " + maxPass + ", will adjust it to -1.");
            this.maxPass = -1;
        }
        else
        {
            this.maxPass = maxPass;
        }
    }


    public void setDumpHandler(DumpHandler handler)
    {
        assureStates(new STATE[] {STATE.INIT});
        this.dumpHandler = handler;
    }


    public void setErrorHandler(ErrorHandler handler)
    {
        assureStates(new STATE[] {STATE.INIT});
        this.errorHandler = handler;
    }


    public void setLogFilter(ImpExLogFilter filter)
    {
        assureStates(new STATE[] {STATE.INIT});
        this.filter = filter;
    }


    public ImpExLogFilter getLogFilter()
    {
        return this.filter;
    }


    public boolean hadError()
    {
        assureStates(new STATE[] {STATE.FINISHED, STATE.ABORTED});
        return this.error;
    }
}
