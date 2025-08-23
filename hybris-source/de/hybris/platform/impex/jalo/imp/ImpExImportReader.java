package de.hybris.platform.impex.jalo.imp;

import bsh.EvalError;
import bsh.Interpreter;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.impex.jalo.AbstractCodeLine;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.InvalidHeaderPolicy;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Config;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class ImpExImportReader extends ImpExReader
{
    private static final Logger LOG = Logger.getLogger(ImpExImportReader.class.getName());
    private static final EnumerationValue DEFAULT_IMPORT_VALIDATION_MODE = new EnumerationValue();
    private CSVWriter csvWriter = null;
    private final ImportProcessor defaultProcessor;
    private ImportProcessor customProcessor = null;
    private boolean createSavedValues = false;
    private boolean allowDumping = true;
    private int dumpCount = 0;
    private int processedItemsCount = 0;
    private int processedCountPerHeader = 0;
    private int dumpedCountPerHeader = 0;
    private ValueLine currentValueLine = null;
    private int valueLineCount = 0;
    private int skipValueLines = 0;
    private final boolean processing = false;
    private boolean secondPass = false;
    private ValueLine lastImportedLine = null;
    private Item lastItem = null;
    private AbstractCodeLine afterEachExpr = null;
    private boolean legacyMode = false;
    private boolean doDiscardNextLine = false;
    private boolean doDumpNextLine = false;
    private String dumpNextLineReason = null;
    private HeaderDescriptor lastDumpedLineHeader;


    public boolean isLegacyMode()
    {
        return this.legacyMode;
    }


    public ImpExImportReader(String lines)
    {
        this(new CSVReader(new StringReader(lines)));
    }


    public ImpExImportReader(CSVReader reader)
    {
        this(reader, null);
    }


    public ImpExImportReader(CSVReader reader, boolean legacyMode)
    {
        this(reader, null, (ImportProcessor)new DefaultImportProcessor(), legacyMode);
    }


    public ImpExImportReader(CSVReader reader, CSVWriter dumpWriter)
    {
        this(reader, dumpWriter, (ImportProcessor)new DefaultImportProcessor());
    }


    public ImpExImportReader(CSVReader reader, CSVWriter dumpWriter, ImportProcessor processor)
    {
        this(reader, dumpWriter, processor, Config.getBoolean("impex.legacy.mode", false));
    }


    public ImpExImportReader(CSVReader reader, CSVWriter dumpWriter, ImportProcessor processor, boolean legacyMode)
    {
        this(reader, dumpWriter, new DocumentIDRegistry(), processor, DEFAULT_IMPORT_VALIDATION_MODE, legacyMode);
    }


    public ImpExImportReader(CSVReader reader, CSVWriter dumpWriter, DocumentIDRegistry docIDRegistry, ImportProcessor processor, EnumerationValue mode)
    {
        this(reader, dumpWriter, docIDRegistry, processor, mode, Config.getBoolean("impex.legacy.mode", false));
    }


    public ImpExImportReader(CSVReader reader, CSVWriter dumpWriter, DocumentIDRegistry docIDRegistry, ImportProcessor processor, EnumerationValue mode, boolean legacyMode, InvalidHeaderPolicy policy)
    {
        super(reader, false, mode, docIDRegistry, policy);
        this.legacyMode = legacyMode;
        this.csvWriter = dumpWriter;
        if(ImpExImportReader.class.equals(getClass()) && processor instanceof MultiThreadedImportProcessor)
        {
            System.err.println("=========================================");
            Thread.dumpStack();
            System.err.println("=========================================");
        }
        this.defaultProcessor = processor;
        this.defaultProcessor.init(this);
    }


    public ImpExImportReader(CSVReader reader, CSVWriter dumpWriter, DocumentIDRegistry docIDRegistry, ImportProcessor processor, EnumerationValue mode, InvalidHeaderPolicy policy)
    {
        this(reader, dumpWriter, docIDRegistry, processor, mode, Config.getBoolean("impex.legacy.mode", false), policy);
    }


    public ImpExImportReader(CSVReader reader, CSVWriter dumpWriter, DocumentIDRegistry docIDRegistry, ImportProcessor processor, EnumerationValue mode, boolean legacyMode)
    {
        this(reader, dumpWriter, docIDRegistry, processor, mode, legacyMode, InvalidHeaderPolicy.DUMP_VALUE_LINES);
    }


    public final ImportProcessor getImportProcessor()
    {
        if(this.customProcessor != null)
        {
            return this.customProcessor;
        }
        return this.defaultProcessor;
    }


    protected String findMarker(String expr)
    {
        String marker = super.findMarker(expr);
        if(marker == null)
        {
            String expression = expr.toLowerCase(LocaleHelper.getPersistenceLocale());
            if(expression.startsWith("forEach:end".toLowerCase(LocaleHelper.getPersistenceLocale())))
            {
                marker = "forEach:end";
            }
            else if(expression.startsWith("afterEach:end".toLowerCase(LocaleHelper.getPersistenceLocale())))
            {
                marker = "afterEach:end";
            }
            else if(expression.startsWith("forEach:".toLowerCase(LocaleHelper.getPersistenceLocale())))
            {
                LOG.warn("Code marker 'forEach:' is deprecated, please use 'afterEach:' instead.");
                marker = "forEach:";
            }
            else if(expression.startsWith("afterEach:".toLowerCase(LocaleHelper.getPersistenceLocale())))
            {
                marker = "afterEach:";
            }
        }
        return marker;
    }


    protected boolean processMarkerCodeLine(AbstractCodeLine line) throws ImpExException
    {
        String marker = line.getMarker();
        if(isAfterEachEnd(marker))
        {
            setAfterEachCode(null);
            return false;
        }
        if(isAfterEach(marker))
        {
            setAfterEachCode(line);
            return false;
        }
        return super.processMarkerCodeLine(line);
    }


    private boolean isAfterEachEnd(String marker)
    {
        return (marker.equalsIgnoreCase("forEach:end") || marker
                        .equalsIgnoreCase("afterEach:end"));
    }


    private boolean isAfterEach(String marker)
    {
        return (marker.equalsIgnoreCase("forEach:") || marker
                        .equalsIgnoreCase("afterEach:"));
    }


    public void setCurrentHeader(HeaderDescriptor header)
    {
        if(getCurrentHeader() != header && (header == null || !header.equals(getCurrentHeader())))
        {
            setAfterEachCode(null);
            this.dumpedCountPerHeader = 0;
            this.processedCountPerHeader = 0;
            super.setCurrentHeader(header);
            this.customProcessor = null;
            try
            {
                this.customProcessor = getCustomImportProcessor(header);
            }
            catch(ImpExException e)
            {
                LOG.warn("Can not use custom processor because " + e.getMessage() + ". Will use default processor " + this.defaultProcessor
                                .getClass().getName(), (Throwable)e);
            }
            if(this.customProcessor != null)
            {
                this.customProcessor.init(this);
            }
        }
    }


    public ImportProcessor getCustomImportProcessor(HeaderDescriptor header) throws ImpExException
    {
        String proc = header.getDescriptorData().getModifier("processor");
        ImportProcessor result = null;
        if(proc != null)
        {
            try
            {
                Class<?> clazz = Class.forName(proc);
                result = (ImportProcessor)clazz.newInstance();
            }
            catch(Exception e)
            {
                throw new ImpExException(e, "Can't instantiate Processor " + proc + " because: " + e.getMessage(), 0);
            }
        }
        return result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void setForEachCode(AbstractCodeLine line)
    {
        setAfterEachCode(line);
    }


    protected void setAfterEachCode(AbstractCodeLine line)
    {
        if(line != null && !isCodeExecutionEnabled())
        {
            throw new IllegalStateException("code execution is not enabled");
        }
        this.afterEachExpr = line;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AbstractCodeLine getForEachCode()
    {
        return getAfterEachCode();
    }


    public AbstractCodeLine getAfterEachCode()
    {
        return this.afterEachExpr;
    }


    protected ValueLine createValueLine(HeaderDescriptor header, Map<Integer, String> line)
    {
        ValueLine ret = super.createValueLine(header, line);
        ret.setAfterEachCode(getAfterEachCode());
        return ret;
    }


    public void setValueLinesToSkip(int toSkip)
    {
        if(toSkip < 0)
        {
            throw new JaloInvalidParameterException("values to skip must be >= 0 ", 0);
        }
        this.skipValueLines = toSkip;
    }


    public int getValueLineCount()
    {
        return this.valueLineCount;
    }


    public int getProcessedItemsCount()
    {
        return this.processedItemsCount;
    }


    public int getProcessedItemsCountPerHeader()
    {
        return this.processedCountPerHeader;
    }


    public int getDumpedLineCount()
    {
        return this.dumpCount;
    }


    public int getDumpedLineCountPerHeader()
    {
        return this.dumpedCountPerHeader;
    }


    public int getResolvedItemsCount()
    {
        return this.processedItemsCount - this.dumpCount;
    }


    public ValueLine getCurrentValueLine()
    {
        return this.currentValueLine;
    }


    public CSVWriter getCSVWriter()
    {
        return this.csvWriter;
    }


    public void setCSVWriter(CSVWriter csvWriter)
    {
        this.csvWriter = csvWriter;
        this.lastDumpedLineHeader = null;
    }


    public void close() throws IOException
    {
        super.close();
        if(this.csvWriter != null)
        {
            this.csvWriter.close();
        }
    }


    public void readAll() throws ImpExException
    {
        for(Item i = (Item)readLine(); i != null; i = (Item)readLine())
            ;
    }


    public Object readLine() throws ImpExException
    {
        Item ret = null;
        Object object = null;
        do
        {
            this.doDiscardNextLine = false;
            this.doDumpNextLine = false;
            object = super.readLine();
            if(object == null)
            {
                break;
            }
            if(object instanceof ValueLine)
            {
                ValueLine valueLine = (ValueLine)object;
                PreProcessResult processResult = preProcessLine(valueLine);
                Exception error = null;
                try
                {
                    if(processResult == PreProcessResult.PROCESS && ensureValidHeaderOrMarkUnresolved(valueLine))
                    {
                        ret = processLine(this.currentValueLine);
                    }
                }
                catch(ImpExException | RuntimeException e)
                {
                    error = e;
                    throw e;
                }
                finally
                {
                    if(processResult != PreProcessResult.DISCARD)
                    {
                        postProcessValueLine(this.currentValueLine, ret, error);
                    }
                }
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("skipped non-value object " + object);
            }
        }
        while(ret == null);
        return ret;
    }


    protected boolean ensureValidHeaderOrMarkUnresolved(ValueLine valueLine)
    {
        if(getInvalidHeaderPolicy() == InvalidHeaderPolicy.DUMP_VALUE_LINES)
        {
            if(valueLine.getHeader() == null)
            {
                valueLine.markUnresolved();
                return false;
            }
            if(!valueLine.getHeader().isValid())
            {
                String reason = getCurrentHeader().getInvalidHeaderException().getLocalizedMessage();
                valueLine.markUnresolved(reason);
                return false;
            }
        }
        return true;
    }


    protected void incItemsCount(int toAdd)
    {
        this.processedItemsCount += toAdd;
        this.processedCountPerHeader += toAdd;
    }


    protected void incDumpCount(int toAdd)
    {
        this.dumpCount += toAdd;
        this.dumpedCountPerHeader += toAdd;
    }


    protected void postProcessValueLine(ValueLine currentValueLine, Item ret, Exception error) throws ImpExException
    {
        incItemsCount(1);
        if(error != null)
        {
            currentValueLine.markUnresolved((error.getMessage() != null) ? error.getMessage() : error.toString());
        }
        if(currentValueLine.isUnresolved() || currentValueLine.isUnrecoverable())
        {
            if(LOG.isDebugEnabled())
            {
                if(ret != null)
                {
                    LOG.debug(currentValueLine.getTypeCode() + "-->" + currentValueLine.getTypeCode() + "-->dumped");
                }
                else
                {
                    LOG.debug(currentValueLine.getTypeCode() + "-->dumped");
                }
            }
            if(isDumpingAllowed())
            {
                dumpUnresolvedLine(currentValueLine);
            }
            else if(getValidationMode().equals(getStrictMode()))
            {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("value line for header '");
                stringBuilder.append(currentValueLine.getHeader().getMode().getCode()).append(" ")
                                .append(currentValueLine.getHeader().getConfiguredComposedTypeCode());
                stringBuilder.append("' can not be imported completely because of: ");
                stringBuilder.append(currentValueLine.getUnresolvedReason());
                throw new UnresolvedValueException(stringBuilder.toString());
            }
        }
        else if(ret == null)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("no result provided for value line " + currentValueLine);
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug(currentValueLine.getTypeCode() + "-->" + currentValueLine.getTypeCode());
        }
        if(currentValueLine.hasHiddenLines())
        {
            for(ValueLine hiddenLine : currentValueLine.getHiddenLines())
            {
                incItemsCount(1);
                if(hiddenLine.isUnresolved())
                {
                    dumpUnresolvedLine(hiddenLine);
                }
            }
        }
        this.lastItem = ret;
        this.lastImportedLine = currentValueLine;
        if(currentValueLine.getAfterEachCode() != null && this.lastItem != null)
        {
            execute(currentValueLine.getAfterEachCode(), currentValueLine.getSource(), true);
        }
    }


    protected PreProcessResult preProcessLine(ValueLine valueLine)
    {
        this.valueLineCount++;
        this.currentValueLine = valueLine;
        if(this.skipValueLines > 0)
        {
            this.skipValueLines--;
            if(LOG.isDebugEnabled())
            {
                LOG.debug("skipped value line (toSkip=" + this.skipValueLines + ")" + valueLine);
            }
            return PreProcessResult.DISCARD;
        }
        if(this.doDiscardNextLine)
        {
            this.doDiscardNextLine = false;
            if(LOG.isDebugEnabled())
            {
                LOG.debug("discarded value line " + valueLine);
            }
            return PreProcessResult.DISCARD;
        }
        if(this.doDumpNextLine)
        {
            if(this.dumpNextLineReason == null)
            {
                valueLine.markUnresolved();
            }
            else
            {
                valueLine.markUnresolved(this.dumpNextLineReason);
            }
            this.doDumpNextLine = false;
            return PreProcessResult.DUMP;
        }
        return PreProcessResult.PROCESS;
    }


    protected Item processLine(ValueLine valueLine) throws ImpExException
    {
        try
        {
            return getImportProcessor().processItemData(valueLine);
        }
        catch(UnresolvedValueException e)
        {
            valueLine.markUnresolved(e.getMessage());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("marking line unresolved (reason: " + e.getMessage() + ")");
            }
            return null;
        }
        catch(ImpExException e)
        {
            throw e;
        }
        catch(IllegalStateException e)
        {
            if(getValidationMode().equals(ImpExManager.getImportRelaxedMode()) && valueLine.isUnresolved() && valueLine
                            .getHeader().isRemoveMode())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage());
                }
                for(AbstractColumnDescriptor scd : valueLine.getHeader().getColumns())
                {
                    valueLine.unresolveMissingEntry(scd.getValuePosition());
                }
                return null;
            }
            throw new ImpExException(e);
        }
        catch(Exception e)
        {
            throw new ImpExException(e);
        }
    }


    protected void setBeanShellContext(Interpreter bsh, Map csvLine) throws EvalError
    {
        super.setBeanShellContext(bsh, csvLine);
        bsh.set("lastItem", getLastImportedItem());
    }


    protected Map<String, Object> getScriptExecutionContext(Map<Integer, String> line)
    {
        Map<String, Object> result = new HashMap<>(super.getScriptExecutionContext(line));
        result.put("lastItem", getLastImportedItem());
        return Collections.unmodifiableMap(result);
    }


    public Item getLastImportedItem()
    {
        return this.lastItem;
    }


    public int getLastImportedItemLineNumber()
    {
        return (this.lastImportedLine != null) ? this.lastImportedLine.getLineNumber() : -1;
    }


    public ValueLine getLastImportedLine()
    {
        return this.lastImportedLine;
    }


    protected void dumpUnresolvedLine(ValueLine line) throws ImpExException
    {
        incDumpCount(1);
        if(this.csvWriter == null)
        {
            LOG.warn("no writer - cannot dump unresolved line " + line);
            return;
        }
        try
        {
            HeaderDescriptor lineHeader = line.getHeader();
            if(this.lastDumpedLineHeader == null || !this.lastDumpedLineHeader.equals(lineHeader))
            {
                this.csvWriter.write(Collections.EMPTY_MAP);
                if(lineHeader != null)
                {
                    this.csvWriter.write(lineHeader.dump());
                }
                this.lastDumpedLineHeader = lineHeader;
            }
            Map<Integer, String> dumpValueLine = line.dump();
            this.csvWriter.write(dumpValueLine);
            boolean logUnresolvedToConsole = Config.getBoolean("impex.log.unresolved.lines", true);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("dumped unresolved line " + line);
            }
            else if(isSecondPass() && logUnresolvedToConsole)
            {
                LOG.warn("dumped unresolved line " + line);
            }
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
    }


    public boolean isSecondPass()
    {
        return this.secondPass;
    }


    public void setIsSecondPass()
    {
        this.secondPass = true;
    }


    public boolean isDumpingAllowed()
    {
        return this.allowDumping;
    }


    public void setDumpingAllowed(boolean dumpingAllowed)
    {
        if(!dumpingAllowed && isDumpingAllowed() && getDumpedLineCount() > 0)
        {
            throw new IllegalStateException("cannot switch off dumping - " + getDumpedLineCount() + " lines have already been dumped");
        }
        this.allowDumping = dumpingAllowed;
    }


    public boolean isCreateHMCSavedValues()
    {
        return this.createSavedValues;
    }


    public void setCreateHMCSavedValues(boolean savedValueCreation)
    {
        this.createSavedValues = savedValueCreation;
    }


    public void discardNextLine()
    {
        this.doDiscardNextLine = true;
    }


    public void dumpNextLine()
    {
        dumpNextLine(null);
    }


    public void dumpNextLine(String reason)
    {
        this.doDumpNextLine = true;
        this.dumpNextLineReason = reason;
    }


    public EnumerationValue getValidationMode()
    {
        EnumerationValue candidate = super.getValidationMode();
        if(candidate != DEFAULT_IMPORT_VALIDATION_MODE)
        {
            return candidate;
        }
        return isLegacyMode() ? ImpExManager.getImportStrictMode() : ImpExManager.getImportRelaxedMode();
    }
}
