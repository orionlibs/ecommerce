package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExFile;
import de.hybris.platform.impex.jalo.ImpExLogFilter;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.ImpExZip;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.security.ImportExportUserRightsHelper;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.logging.HybrisLogFilter;
import de.hybris.platform.util.logging.HybrisLogger;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class Exporter implements ImpExLogFilter.LocationProvider
{
    private ImpExExportWriter writer;
    private ImpExReader reader;
    private final CSVReader scriptReader;
    private ImpExMedia source = null;
    private HeaderDescriptor header = null;
    private int headerCount = 0;
    private final ImpExExportWriter scriptWriter;
    private final StringWriter script;
    private boolean lastSetTarget = false;
    private boolean lastExportItems = true;
    private boolean writeCurrentHeader = true;
    private ExportConfiguration config = null;
    private String includeLine = "";
    private static final Logger log = Logger.getLogger(Exporter.class.getName());
    public static final int DEFAULT_ITEM_DUMP_RANGE = 1000;
    private static final char IMPEX_IMPORT_SCRIPT_FIELD_SEPARATOR = ';';
    private long exportedItemsCount = 0L;
    private long lastLogExportedItemsCount = 0L;
    private long startTime = 0L;
    private long logTime = 0L;
    public static final long LOG_INTERVAL = 60000L;


    public Exporter(ExportConfiguration config) throws ImpExException
    {
        this.config = config;
        this.source = config.getSource();
        this.script = new StringWriter();
        this.scriptWriter = createScriptWriter(this.script);
        this.scriptReader = createScriptReader(this.source);
        this.startTime = System.currentTimeMillis();
        this.logTime = this.startTime;
    }


    protected void logProcess()
    {
        if(log.isInfoEnabled())
        {
            long curTime = System.currentTimeMillis();
            long timeDiff = curTime - this.logTime;
            if(timeDiff > 60000L)
            {
                this.logTime = curTime;
                long curExportedItemsCount = getExportedItemsCount();
                long itemsDiff = curExportedItemsCount - this.lastLogExportedItemsCount;
                long rate = itemsDiff * 1000L / timeDiff;
                log.info("Exported " + itemsDiff + " items in " + timeDiff / 1000L + " s (speed " + rate + " items/s).");
                log.info("Overall exported items are " + curExportedItemsCount + ".");
                this.lastLogExportedItemsCount = curExportedItemsCount;
            }
        }
    }


    public long getExportedItemsCount()
    {
        return this.exportedItemsCount;
    }


    public Exporter(ImpExMedia source, EnumerationValue headerValidationMode) throws ImpExException
    {
        this(new ExportConfiguration(source, headerValidationMode));
    }


    public Export export() throws ImpExException
    {
        this.reader = createImpExReader(this.scriptReader, this.config.getDocumentIDRegistry(), this.config.getHeaderValidationMode());
        this.writer = createExportWriter();
        ImpExLogFilter filter = new ImpExLogFilter();
        try
        {
            filter.registerLocationProvider(this);
            HybrisLogger.addFilter((HybrisLogFilter)filter);
            runScriptBasedExport();
        }
        catch(ImpExException e)
        {
            ImpExException newException = new ImpExException((Throwable)e, filter.extendMessage(e.getMessage()), e.getErrorCode());
            throw newException;
        }
        catch(Exception e)
        {
            ImpExException newException = new ImpExException(e, filter.extendMessage(e.getMessage()), -1);
            throw newException;
        }
        finally
        {
            this.config.deleteTempFiles();
            filter.unregisterLocationProvider();
            HybrisLogger.removeFilter((HybrisLogFilter)filter);
        }
        ImpExExportMedia impExExportMedia1 = this.config.getDataExportTarget();
        if(impExExportMedia1 != null && (!impExExportMedia1.hasData() || impExExportMedia1.getSize().longValue() == 0L))
        {
            try
            {
                impExExportMedia1.remove();
            }
            catch(ConsistencyCheckException e)
            {
                log.warn("Can not remove empty media " + impExExportMedia1.getCode());
            }
        }
        ImpExExportMedia impExExportMedia2 = this.config.getMediasExportTarget();
        if(impExExportMedia2 != null && (!impExExportMedia2.hasData() || impExExportMedia2.getSize().longValue() == 0L))
        {
            try
            {
                impExExportMedia2.remove();
            }
            catch(ConsistencyCheckException e)
            {
                log.warn("Can not remove empty media " + impExExportMedia2.getCode());
            }
        }
        Map<Object, Object> attributeValues = new HashMap<>();
        attributeValues.put("code", this.config.getSource().getCode());
        attributeValues.put("exportedData", this.config.getDataExportTarget());
        attributeValues.put("exportedMedias", this.config.getMediasExportTarget());
        attributeValues.put("exportScript", this.config.getSource());
        attributeValues.put("description", "ImpEx based export");
        return ImpExManager.getInstance().createExport(attributeValues);
    }


    protected void runScriptBasedExport() throws FileNotFoundException, JaloBusinessException
    {
        ImpExFile dataFile;
        this.writer.setColumnOffset(0);
        this.scriptWriter.setColumnOffset(0);
        exportInternal();
        try
        {
            dataFile = this.config.getDataFile();
            if(!this.config.isSingleFile() && dataFile instanceof ImpExZip)
            {
                ImpExZip zipDataFile = (ImpExZip)dataFile;
                zipDataFile.startNewEntry("importscript.impex");
                this.scriptWriter.close();
                if(log.isDebugEnabled())
                {
                    log.debug("Generated impex script is:\n" + this.script.getBuffer().toString());
                }
                MediaUtil.copy(new ByteArrayInputStream(this.script.getBuffer().toString().getBytes()), zipDataFile.getOutputStream(), false);
                ((CSVWriter)this.writer.getExportWriter()).getWriter().flush();
                zipDataFile.closeEntry();
            }
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
        this.header = null;
        try
        {
            if(this.reader != null)
            {
                this.reader.close();
            }
        }
        catch(IOException e1)
        {
            log.warn("Error while closing streams: " + e1.getMessage(), e1);
        }
        if(this.writer != null)
        {
            this.writer.close();
        }
        try
        {
            if(dataFile != null)
            {
                dataFile.close();
            }
        }
        catch(IOException e1)
        {
            log.warn("Error while closing streams: " + e1.getMessage(), e1);
        }
        if(this.config.getMediaFile() != null)
        {
            try
            {
                this.config.getMediaFile().close();
            }
            catch(IOException e)
            {
                log.warn("Error while closing streams: " + e.getMessage(), e);
            }
        }
        if(this.reader != null && this.reader.getDocumentIDRegistry().hasUnresolvedIDs())
        {
            StringBuilder buffer = new StringBuilder();
            for(String entry : this.reader.getDocumentIDRegistry().printUnresolvedIDs(","))
            {
                buffer.append(entry + " - ");
            }
            if(this.reader.getValidationMode().equals(ImpExManager.getExportReimportStrictMode()))
            {
                throw new ImpExException("Unresolved Document IDs were detected, so a re-import will not succeed. Assure that all items referenced using a document ID were exported too. The ID's referenced but not exported are: " + buffer
                                .toString());
            }
            log.warn("Unresolved Document IDs were detected, so a re-import will not succeed. Assure that all items referenced using a document ID were exported too. The ID's referenced but not exported are: " + buffer
                            .toString());
        }
        if(dataFile != null)
        {
            this.config.getDataExportTarget().setData(new DataInputStream(new FileInputStream(dataFile.getFile())), this.config
                            .getDataExportTarget().getCode() + "." + this.config.getDataExportTarget().getCode(), dataFile
                            .getMimeType());
        }
        if(this.config.getMediaFile().getFile() != null)
        {
            try
            {
                this.config.getMediasExportTarget().setData(new DataInputStream(new FileInputStream(this.config.getMediaFile().getFile())), this.config
                                                .getMediasExportTarget().getCode() + "." + this.config.getMediasExportTarget().getCode(),
                                this.config
                                                .getMediaFile().getMimeType());
            }
            catch(NullPointerException e)
            {
                log.error("Catched Nullpointer", e);
            }
        }
    }


    protected ImpExReader createImpExReader(CSVReader scriptReader, DocumentIDRegistry docIDRegistry, EnumerationValue headerValidationMode)
    {
        return (ImpExReader)new MyImpExReader(this, scriptReader, true, headerValidationMode, docIDRegistry);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ImpExExportWriter getExExportWriter()
    {
        return getImpExExportWriter();
    }


    public ImpExExportWriter getImpExExportWriter()
    {
        return this.writer;
    }


    protected ImpExExportWriter createExportWriter() throws ImpExException
    {
        ImpExCSVExportWriter impExCSVExportWriter;
        ImpExExportWriter ret = null;
        try
        {
            impExCSVExportWriter = new ImpExCSVExportWriter(new CSVWriter(this.config.getDataFile().getOutputStream(), this.config.getDataExportTarget().getEncoding().getCode()));
        }
        catch(UnsupportedEncodingException e)
        {
            throw new ImpExException(e);
        }
        if(this.config.getFieldSeparator().length() > 0)
        {
            ((CSVWriter)impExCSVExportWriter.getExportWriter()).setFieldseparator(this.config.getFieldSeparator().charAt(0));
        }
        else
        {
            throw new ImpExException("Currently field separator of length 1 only are supported");
        }
        if(this.config.getCommentCharacter().length() > 0)
        {
            ((CSVWriter)impExCSVExportWriter.getExportWriter()).setCommentchar(this.config.getCommentCharacter().charAt(0));
        }
        else
        {
            throw new ImpExException("Currently comment marker of length 1 only are supported");
        }
        if(this.config.getQuoteCharacter().length() > 0)
        {
            ((CSVWriter)impExCSVExportWriter.getExportWriter()).setTextseparator(this.config.getQuoteCharacter().charAt(0));
        }
        else
        {
            throw new ImpExException("Currently quote marker of length 1 only are supported");
        }
        return (ImpExExportWriter)impExCSVExportWriter;
    }


    private ImpExExportWriter createScriptWriter(Writer script) throws ImpExException
    {
        ImpExCSVExportWriter impExCSVExportWriter = new ImpExCSVExportWriter(new CSVWriter(script));
        if(this.config.getFieldSeparator().length() > 0)
        {
            ((CSVWriter)impExCSVExportWriter.getExportWriter()).setFieldseparator(this.config.getFieldSeparator().charAt(0));
        }
        else
        {
            throw new ImpExException("Currently field separator of length 1 only are supported");
        }
        if(this.config.getCommentCharacter().length() > 0)
        {
            ((CSVWriter)impExCSVExportWriter.getExportWriter()).setCommentchar(this.config.getCommentCharacter().charAt(0));
        }
        else
        {
            throw new ImpExException("Currently comment marker of length 1 only are supported");
        }
        if(this.config.getQuoteCharacter().length() > 0)
        {
            ((CSVWriter)impExCSVExportWriter.getExportWriter()).setTextseparator(this.config.getQuoteCharacter().charAt(0));
        }
        else
        {
            throw new ImpExException("Currently quote marker of length 1 only are supported");
        }
        impExCSVExportWriter.setColumnOffset(0);
        return (ImpExExportWriter)impExCSVExportWriter;
    }


    private CSVReader createScriptReader(ImpExMedia sourceMedia) throws ImpExException
    {
        StringBuilder scriptBuilder = ImpExUtils.getContent((Media)sourceMedia);
        if(scriptBuilder == null)
        {
            throw new ImpExException("Given media does not contain data");
        }
        CSVReader ret = null;
        try
        {
            ret = new CSVReader(new ByteArrayInputStream(scriptBuilder.toString().getBytes()), this.source.getEncoding().getCode());
        }
        catch(UnsupportedEncodingException e)
        {
            throw new ImpExException(e);
        }
        ret.setFieldSeparator(new char[] {this.source
                        .getFieldSeparatorAsPrimitive()});
        ret.setCommentOut(new char[] {this.source
                        .getCommentCharacterAsPrimitive()});
        ret.setTextSeparator(this.source.getQuoteCharacterAsPrimitive());
        ret.setLinesToSkip(this.source.getLinesToSkipAsPrimitive());
        return ret;
    }


    public boolean hasNextHeader()
    {
        return (this.header != null);
    }


    public HeaderDescriptor getNextHeader()
    {
        return this.header;
    }


    public int getCurrentHeaderCount()
    {
        return this.headerCount;
    }


    protected void exportInternal() throws ImpExException
    {
        try
        {
            while(true)
            {
                Object o = this.reader.readLine();
                if(log.isDebugEnabled())
                {
                    log.debug("line: " + o);
                }
                if(o != null)
                {
                    if(!this.lastExportItems)
                    {
                        exportItems(this.header.getTypeCode(), false);
                    }
                    this.lastExportItems = false;
                    if(!(o instanceof HeaderDescriptor))
                    {
                        throw new ImpExException("Have read unknown line type " + o.getClass().getName());
                    }
                    this.header = (HeaderDescriptor)o;
                    if(!this.config.isSingleFile() && !this.lastSetTarget)
                    {
                        setTargetFile(this.header.getTypeCode() + ".csv");
                    }
                    this.lastSetTarget = false;
                    this.writer.setCurrentHeader(this.header);
                    try
                    {
                        if(this.writeCurrentHeader)
                        {
                            this.writer.writeCurrentHeader(!this.config.isSingleFile());
                        }
                        if(!this.config.isSingleFile())
                        {
                            writeImpexImportScript();
                        }
                    }
                    catch(IOException e)
                    {
                        throw new ImpExException(e);
                    }
                    this.headerCount++;
                    continue;
                }
                break;
            }
            if(!this.lastExportItems)
            {
                exportItems(this.header.getTypeCode(), false);
            }
            return;
        }
        catch(ImpExException e)
        {
            if(this.config.getDataFile() != null)
            {
                try
                {
                    this.config.getDataFile().close();
                    this.config.getDataFile().getFile().delete();
                    this.config.getDataExportTarget().remove();
                }
                catch(IOException e1)
                {
                    log.error("Can not delete temporary data-target file! " + e1.getMessage());
                }
                catch(ConsistencyCheckException e1)
                {
                    log.error("Can not delete temporary data-target file! " + e1.getMessage());
                }
            }
            if(this.config.getMediaFile() != null)
            {
                try
                {
                    this.config.getMediaFile().close();
                    this.config.getMediaFile().getFile().delete();
                    this.config.getMediasExportTarget().remove();
                }
                catch(IOException e1)
                {
                    log.error("Can not delete temporary media-target file");
                }
                catch(ConsistencyCheckException e1)
                {
                    log.error("Can not delete temporary media-target file! " + e1.getMessage());
                }
            }
            throw e;
        }
    }


    private void writeImpexImportScript() throws IOException, ImpExException
    {
        char scriptWriterFieldSeparator = ((CSVWriter)this.scriptWriter.getExportWriter()).getFieldseparator();
        setScriptWriterSeparator(';');
        this.scriptWriter.writeSrcLine("");
        this.scriptWriter.writeComment("-----------------------------------------------------------");
        this.scriptWriter.setCurrentHeader(this.header);
        this.scriptWriter.writeCurrentHeader(false);
        this.scriptWriter.writeSrcLine(this.includeLine);
        this.scriptWriter.writeComment("-----------------------------------------------------------");
        setScriptWriterSeparator(scriptWriterFieldSeparator);
    }


    private void setScriptWriterSeparator(char separator)
    {
        ((CSVWriter)this.scriptWriter.getExportWriter()).setFieldseparator(separator);
    }


    public void exportAllHeader() throws ImpExException
    {
        if(this.reader == null)
        {
            throw new ImpExException("Given reader object is null");
        }
        try
        {
            while(hasNextHeader())
            {
                exportInternal();
            }
        }
        finally
        {
            this.config.deleteTempFiles();
        }
    }


    public Collection<String> getResultingFiles()
    {
        List<String> ret;
        if(this.header != null)
        {
            ret = Collections.EMPTY_LIST;
        }
        else
        {
            ret = new ArrayList<>();
            ret.add(this.config.getDataFile().getFile().getAbsolutePath());
            if(this.config.getMediaFile() != null)
            {
                ret.add(this.config.getMediaFile().getFile().getAbsolutePath());
            }
        }
        return ret;
    }


    protected void exportUserRights() throws ImpExException
    {
        CSVWriter csvWriter = (CSVWriter)this.writer.getExportWriter();
        try
        {
            this.scriptWriter.writeComment("-----------------------------------------------------------");
            this.scriptWriter.writeSrcLine(this.includeLine);
            this.scriptWriter.writeComment("-----------------------------------------------------------");
            csvWriter.writeSrcLine("$START_USERRIGHTS");
            StringWriter tempWriter = new StringWriter();
            ImportExportUserRightsHelper ieur = new ImportExportUserRightsHelper(tempWriter, ((CSVWriter)this.writer.getExportWriter()).getTextseparator(), ((CSVWriter)this.writer.getExportWriter()).getFieldseparator(), ',', true);
            ieur.exportSecurity();
            csvWriter.writeSrcLine(tempWriter.getBuffer().toString());
            csvWriter.writeSrcLine("$END_USERRIGHTS");
            csvWriter.getWriter().flush();
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
    }


    protected Collection<Item> getAllItems(String code, int start, int count, boolean inclSubtypes)
    {
        StringBuilder orderby = new StringBuilder();
        for(Object next : this.writer.getCurrentHeader().calculateUniqueAttributeColumns())
        {
            String qualifier = ((StandardColumnDescriptor)next).getQualifier().trim();
            if(qualifier.equalsIgnoreCase(Item.PK))
            {
                continue;
            }
            orderby.append("{").append(qualifier).append("} ASC, ");
        }
        orderby.append("{").append(Item.CREATION_TIME).append("} ASC, ");
        orderby.append("{").append(Item.PK).append("} ASC");
        return FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + code + (
                                                        inclSubtypes ? "" : "!") + "} ORDER BY " + orderby
                                                        .toString(), Collections.EMPTY_MAP,
                                        Collections.singletonList(Item.class), true, true, start, count)
                        .getResult();
    }


    public final void exportItems(String typecode) throws ImpExException
    {
        exportItems(typecode, 1000, false);
    }


    public final void exportItems(String typecode, boolean inclSubTypes) throws ImpExException
    {
        exportItems(typecode, 1000, inclSubTypes);
    }


    public void exportItems(String typecode, int count) throws ImpExException
    {
        exportItems(typecode, count, false);
    }


    public void exportItems(String typecode, int count, boolean inclSubTypes) throws ImpExException
    {
        if(typecode.equals(TypeManager.getInstance().getComposedType(UserRight.class).getCode()))
        {
            exportUserRights();
        }
        else
        {
            int start = 0;
            int range = count;
            Collection<Item> items = null;
            do
            {
                items = getAllItems(typecode, start, range, inclSubTypes);
                start += range;
                exportItems(items);
            }
            while(items != null && items.size() == range);
        }
    }


    public void setTargetFile(String filename, boolean writeHeader) throws ImpExException
    {
        setTargetFile(filename, writeHeader, 1, -1);
    }


    public void setTargetFile(String filename, boolean writeHeader, int linesToSkip, int offset) throws ImpExException
    {
        try
        {
            ImpExFile dataFile = this.config.getDataFile();
            if(dataFile instanceof ImpExZip)
            {
                ImpExZip zipDataFile = (ImpExZip)dataFile;
                zipDataFile.startNewEntry(filename);
                this.writer.setColumnOffset(offset);
                this
                                .includeLine =
                                "\"#% impex.includeExternalDataMedia( \"\"" + filename + "\"\" , \"\"" + this.config.getDataExportTarget().getEncoding().getCode() + "\"\", '" + ((CSVWriter)this.writer.getExportWriter()).getFieldseparator() + "',  " + linesToSkip + " , " + offset + " );\"";
                if(log.isDebugEnabled())
                {
                    log.debug("start writing to zip entry " + filename);
                }
            }
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
        this.lastSetTarget = true;
        this.writeCurrentHeader = writeHeader;
    }


    public void setTargetFile(String filename) throws ImpExException
    {
        setTargetFile(filename, true);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setRelaxedMode(boolean isRelaxedMode) throws ImpExException
    {
        log.warn("Use of deprecated method from BeanShell: Exporter#setRelaxedMode(boolean)");
        try
        {
            this.scriptWriter.writeComment("-----------------------------------------------------------");
            this.scriptWriter.writeSrcLine("\"#% impex.setRelaxedMode( " + isRelaxedMode + " );\"");
            this.scriptWriter.writeComment("-----------------------------------------------------------");
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
        this.reader.setValidationMode(ImpExManager.getImportRelaxedMode());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void exportItems(String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count) throws ImpExException
    {
        log.warn("Use of deprecated method from BeanShell: Exporter#exportItems(String,Map,List,boolean,boolean,int,int)");
        exportItemsFlexibleSearch(query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
    }


    public void exportItemsFlexibleSearch(String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count) throws ImpExException
    {
        if(this.writer.getCurrentHeader().getTypeCode()
                        .equals(TypeManager.getInstance().getComposedType(UserRight.class).getCode()))
        {
            exportUserRights();
        }
        else
        {
            Collection<Item> items = FlexibleSearch.getInstance().search(query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count).getResult();
            exportItems(items);
        }
    }


    public void exportItemsFlexibleSearch(String query) throws ImpExException
    {
        exportItemsFlexibleSearch(query, 1000);
    }


    public void exportItemsFlexibleSearch(String query, int count) throws ImpExException
    {
        if(this.writer.getCurrentHeader().getTypeCode()
                        .equals(TypeManager.getInstance().getComposedType(UserRight.class).getCode()))
        {
            exportUserRights();
        }
        else
        {
            Collection<Item> items;
            int start = 0;
            int range = count;
            do
            {
                items = FlexibleSearch.getInstance().search(query, Collections.EMPTY_MAP, Collections.singletonList(Item.class), true, true, start, range).getResult();
                start += range;
                exportItems(items);
            }
            while(items != null && items.size() == range);
        }
    }


    public void exportItems(String[] pklist) throws ImpExException
    {
        if(this.writer.getCurrentHeader().getTypeCode()
                        .equals(TypeManager.getInstance().getComposedType(UserRight.class).getCode()))
        {
            exportUserRights();
        }
        else
        {
            String[] pks = pklist;
            List<PK> pkList = new ArrayList<>(pks.length);
            for(String current : pks)
            {
                pkList.add(PK.parse(current));
            }
            JaloSession js = JaloSession.getCurrentSession();
            exportItems(js.getItems(js.getSessionContext(), pkList, true));
        }
    }


    public void exportItems(Collection<Item> items) throws ImpExException
    {
        if(log.isDebugEnabled())
        {
            log.debug("start exporting header " + this.header.getDefinitionSrc());
        }
        if(this.writer.getCurrentHeader().getTypeCode()
                        .equals(TypeManager.getInstance().getComposedType(UserRight.class).getCode()))
        {
            exportUserRights();
        }
        else
        {
            for(Item item : items)
            {
                this.writer.writeLine(item);
                this.exportedItemsCount++;
                logProcess();
            }
            try
            {
                ((CSVWriter)this.writer.getExportWriter()).getWriter().flush();
            }
            catch(IOException e)
            {
                throw new ImpExException(e);
            }
            this.lastExportItems = true;
        }
        if(log.isDebugEnabled())
        {
            log.debug("finished exporting header " + this.header.getDefinitionSrc());
        }
    }


    public void setValidationMode(String mode) throws ImpExException
    {
        try
        {
            this.scriptWriter.writeComment("-----------------------------------------------------------");
            this.scriptWriter.writeSrcLine("\"#% impex.setValidationMode( " + mode + " );\"");
            this.scriptWriter.writeComment("-----------------------------------------------------------");
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
        this.reader.setValidationMode(ImpExManager.getValidationMode(mode));
    }


    public void setLocale(Locale l) throws ImpExException
    {
        try
        {
            this.scriptWriter.writeComment("-----------------------------------------------------------");
            this.scriptWriter.writeSrcLine("\"#% impex.setLocale( new Locale( \"\"" + l
                            .getLanguage() + "\"\" , \"\"" + l.getCountry() + "\"\" ) );\"");
            this.scriptWriter.writeComment("-----------------------------------------------------------");
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
        this.reader.setLocale(l);
    }


    public String getCurrentLocation()
    {
        ImpExReader readerRef = this.reader;
        if(readerRef == null)
        {
            return "";
        }
        return readerRef.getCurrentLocation();
    }
}
