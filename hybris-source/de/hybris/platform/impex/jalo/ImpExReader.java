package de.hybris.platform.impex.jalo;

import bsh.EvalError;
import bsh.Interpreter;
import com.google.common.base.Stopwatch;
import com.google.common.base.Suppliers;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.BeanShellUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.jalo.util.CSVMap;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.security.ImportExportUserRightsHelper;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.exception.ScriptingException;
import de.hybris.platform.util.CSVCellDecorator;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ImpExReader
{
    private static final Logger LOG = Logger.getLogger(ImpExReader.class.getName());
    public static final Integer FIRST = Integer.valueOf(0);
    private final ReaderManager readerManager;
    private final ScriptCodeLineFactory codeLineFactory;
    private Locale locale = null;
    private Queue<Map<Integer, String>> insertedLines;
    private Map<String, String> definitions = new TreeMap<>((Comparator<? super String>)new Object(this));
    private final List<Object[]> replacementRules;
    private Map<Integer, CSVCellDecorator> decorators;
    private final Stack<AbstractCodeLine> ifStack = new Stack<>();
    private HeaderDescriptor currentHeader = null;
    private boolean skipValueLines = false;
    private HeaderDescriptor.AttributeConstraintFilter attributeConstraintFilter = null;
    private boolean userImport = false;
    private List<String> userRightsLines;
    private final StringBuilder userRightsLineEntry = new StringBuilder();
    private EnumerationValue headerValidationMode = null;
    private EnumerationValue strictModeCache;
    private Collection<ImpExMedia> externalDataMedias;
    private boolean enableExternalSyntaxParsingVariable = false;
    private boolean enableBeanShell = false;
    private boolean enableBeanShellForExternalData = false;
    private Interpreter bsh = null;
    private Boolean combinedSearchFlagCache = null;
    private AbstractCodeLine beforeEachExpr = null;
    private DocumentIDRegistry documentIDRegistry = null;
    private Connection dbCon = null;
    private String dbUrl = null;
    private String dbUser = null;
    private String dbPassword = null;
    private String dbClassName = null;
    private final boolean legacyScriptingEnabled;
    private InvalidHeaderPolicy invalidHeaderPolicy;
    private static final String NO_HEADER_FOR_VALUE_LINE = "# no current header for value line";
    private static final Supplier<Pattern> HEADER_LINE_PATTERN_SUPPLIER;

    static
    {
        HEADER_LINE_PATTERN_SUPPLIER = (Supplier<Pattern>)Suppliers.memoize(() -> {
            String regexp = "^\\s*(" + ImpExConstants.Syntax.Mode.INSERT_UPDATE + "|" + ImpExConstants.Syntax.Mode.INSERT + "|" + ImpExConstants.Syntax.Mode.UPDATE + "|" + ImpExConstants.Syntax.Mode.REMOVE + ")?\\s+\\w.*";
            return Pattern.compile(regexp);
        });
    }

    public ImpExReader(String fileName, String encoding, boolean skipValueLines) throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new CSVReader(fileName, encoding), skipValueLines);
    }


    public ImpExReader(InputStream input, String encoding) throws UnsupportedEncodingException
    {
        this(new CSVReader(input, encoding), false);
    }


    public ImpExReader(InputStream input, String encoding, boolean skipValueLines) throws UnsupportedEncodingException
    {
        this(new CSVReader(input, encoding), skipValueLines);
    }


    public ImpExReader(CSVReader reader, boolean skipValueLines)
    {
        this(reader, skipValueLines, ImpExManager.getImportStrictMode());
    }


    public ImpExReader(CSVReader reader, boolean skipValueLines, EnumerationValue headerValidationMode)
    {
        this(reader, skipValueLines, headerValidationMode, new DocumentIDRegistry());
    }


    public ImpExReader(CSVReader reader, boolean skipValueLines, EnumerationValue headerValidationMode, DocumentIDRegistry docIdRegistry)
    {
        this(reader, skipValueLines, headerValidationMode, docIdRegistry, InvalidHeaderPolicy.THROW_EXCEPTION);
    }


    public ImpExReader(CSVReader reader, boolean skipValueLines, EnumerationValue headerValidationMode, DocumentIDRegistry docIdRegistry, InvalidHeaderPolicy headerHandlingPolicy)
    {
        reader.setShowComments(true);
        reader.setMultiLineMode(true);
        reader.setLinesToSkip(0);
        this.readerManager = new ReaderManager(reader);
        this.skipValueLines = skipValueLines;
        this.headerValidationMode = headerValidationMode;
        this.documentIDRegistry = docIdRegistry;
        this.replacementRules = new ArrayList(getDefaultReplacements());
        ScriptingLanguagesService service = (ScriptingLanguagesService)Registry.getApplicationContext().getBean("scriptingLanguagesService", ScriptingLanguagesService.class);
        this.codeLineFactory = new ScriptCodeLineFactory(this.readerManager, service);
        this.legacyScriptingEnabled = Config.getBoolean("impex.legacy.scripting", true);
        this.invalidHeaderPolicy = headerHandlingPolicy;
    }


    public InvalidHeaderPolicy getInvalidHeaderPolicy()
    {
        return this.invalidHeaderPolicy;
    }


    protected void setInvalidHeaderPolicy(InvalidHeaderPolicy invalidHeaderPolicy)
    {
        this.invalidHeaderPolicy = invalidHeaderPolicy;
    }


    protected EnumerationValue getStrictMode()
    {
        if(this.strictModeCache == null)
        {
            this.strictModeCache = ImpExManager.getImportStrictMode();
        }
        return this.strictModeCache;
    }


    protected List<Object[]> getDefaultReplacements()
    {
        List<Object[]> rulesList = null;
        for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)Config.getAllParameters().entrySet())
        {
            if(((String)entry.getKey()).startsWith("impex.header.replacement"))
            {
                int prio;
                try
                {
                    prio = Integer.parseInt(((String)entry
                                    .getKey()).substring("impex.header.replacement".length() + 1));
                }
                catch(Exception e)
                {
                    prio = 0;
                }
                int delimiter = (entry.getValue() == null) ? -1 : ((String)entry.getValue()).indexOf("...");
                String srcPattern = (delimiter >= 0) ? ((String)entry.getValue()).substring(0, delimiter).trim() : null;
                String tgtPattern = (delimiter >= 0) ? ((String)entry.getValue()).substring(delimiter + "...".length()).trim() : null;
                if(srcPattern != null && srcPattern.length() > 0 && tgtPattern != null && tgtPattern.length() > 0)
                {
                    if(rulesList == null)
                    {
                        rulesList = new ArrayList();
                    }
                    rulesList.add(new Object[] {Integer.valueOf(prio), srcPattern, tgtPattern});
                }
            }
        }
        if(rulesList == null)
        {
            return Collections.EMPTY_LIST;
        }
        sortRulesList(rulesList);
        return rulesList;
    }


    protected void sortRulesList(List<Object[]> rulesList)
    {
        Collections.sort(rulesList, (Comparator<? super Object>)new Object(this));
    }


    public void addHeaderReplacementRule(String srcPattern, String tgtPattern, int prio)
    {
        this.replacementRules.add(new Object[] {Integer.valueOf(prio), srcPattern, tgtPattern});
        sortRulesList(this.replacementRules);
    }


    public void setAttributeConstraintFilter(HeaderDescriptor.AttributeConstraintFilter filter)
    {
        this.attributeConstraintFilter = filter;
    }


    public HeaderDescriptor.AttributeConstraintFilter getAttributeConstraintFilter()
    {
        return this.attributeConstraintFilter;
    }


    public static HeaderDescriptor parseHeader(String headerLine) throws ImpExException
    {
        return parseHeader(headerLine, new DocumentIDRegistry());
    }


    public static HeaderDescriptor parseHeader(String headerLine, DocumentIDRegistry docIdRegistry) throws ImpExException
    {
        return parseHeader(headerLine, ImpExManager.getImportStrictMode(), docIdRegistry);
    }


    public static HeaderDescriptor parseHeader(String headerLine, EnumerationValue mode) throws ImpExException
    {
        return parseHeader(headerLine, mode, new DocumentIDRegistry());
    }


    public static HeaderDescriptor parseHeader(String headerLine, EnumerationValue mode, DocumentIDRegistry docIdRegistry) throws ImpExException
    {
        ImpExReader reader = new ImpExReader(new CSVReader(new StringReader(headerLine)), true, mode, docIdRegistry);
        Object object = reader.readLine();
        if(!(object instanceof HeaderDescriptor))
        {
            throw new ImpExException("could not parse header from '" + headerLine + "' - got " + object + " instead", 0);
        }
        return (HeaderDescriptor)object;
    }


    public boolean isExternalSyntaxParsingEnabled()
    {
        return this.enableExternalSyntaxParsingVariable;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void enableExternalImpExSyntaxParsing(boolean isOn)
    {
        enableExternalSyntaxParsing(isOn);
    }


    public void enableExternalSyntaxParsing(boolean isOn)
    {
        this.enableExternalSyntaxParsingVariable = isOn;
    }


    public boolean isCombinedSearchEnabled()
    {
        if(this.combinedSearchFlagCache == null)
        {
            this.combinedSearchFlagCache = Boolean.valueOf(Config.getBoolean("impex.query.combined", true));
        }
        return Boolean.TRUE.equals(this.combinedSearchFlagCache);
    }


    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }


    public Locale getLocale()
    {
        return (this.locale == null) ? JaloSession.getCurrentSession().getSessionContext().getLocale() : this.locale;
    }


    public void close() throws IOException
    {
        this.readerManager.clear();
    }


    public CSVReader getCSVReader()
    {
        return this.readerManager.getMainReader();
    }


    public void addExternalDataMedia(ImpExMedia ext)
    {
        if(ext != null && (this.externalDataMedias == null || !this.externalDataMedias.contains(ext)))
        {
            if(this.externalDataMedias == null)
            {
                this.externalDataMedias = new ArrayList<>();
            }
            this.externalDataMedias.add(ext);
        }
    }


    public void addExternalDataMedias(Collection<ImpExMedia> toAdd)
    {
        if(toAdd != null && !toAdd.isEmpty())
        {
            if(this.externalDataMedias == null)
            {
                this.externalDataMedias = new ArrayList<>();
            }
            this.externalDataMedias.addAll(toAdd);
        }
    }


    public void removeExternalDataMedia(Media ext)
    {
        if(ext != null && this.externalDataMedias != null)
        {
            this.externalDataMedias.remove(ext);
        }
    }


    public void removeExternalDataMedias(Collection<Media> toRemove)
    {
        if(toRemove != null && !toRemove.isEmpty() && this.externalDataMedias != null)
        {
            this.externalDataMedias.removeAll(toRemove);
        }
    }


    protected Set<String> getAllExternalDataMediaCodes()
    {
        if(this.externalDataMedias == null || this.externalDataMedias.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<String> ret = new HashSet<>();
        for(Media m : this.externalDataMedias)
        {
            ret.add(m.getCode());
        }
        return ret;
    }


    public ImpExMedia findExternalDataMedia(String code) throws JaloBusinessException
    {
        if(this.externalDataMedias != null)
        {
            for(ImpExMedia m : this.externalDataMedias)
            {
                if(code.equalsIgnoreCase(m.getCode()))
                {
                    return m;
                }
            }
        }
        return null;
    }


    protected ImpExMedia findExternalDataMedia(String prefix, String code)
    {
        if(this.externalDataMedias != null)
        {
            for(ImpExMedia m : this.externalDataMedias)
            {
                String expectedMediaCode = prefix + prefix + code;
                if(expectedMediaCode.equalsIgnoreCase(m.getCode()))
                {
                    return m;
                }
            }
        }
        return null;
    }


    public boolean isIncludingExternalData()
    {
        return (this.readerManager.readerCount() > 1);
    }


    public void setCurrentHeader(String headerCSVDefinition) throws ImpExException
    {
        setCurrentHeader(parseHeader(headerCSVDefinition, getValidationMode(), this.documentIDRegistry));
    }


    public void setCurrentHeader(HeaderDescriptor header)
    {
        setBeforeEachCode(null);
        this.currentHeader = header;
        setCellDecorators(header);
    }


    public HeaderDescriptor getCurrentHeader()
    {
        return this.currentHeader;
    }


    private Connection getConnection()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("creating JDBC connection using 'url=" + this.dbUrl + "'");
        }
        try
        {
            if(this.dbCon == null || this.dbCon.isClosed())
            {
                if(this.dbUrl == null || this.dbClassName == null)
                {
                    throw new JaloSystemException("Missing Datasource Definition! Sample: initDatabase( \"jdbc:mysql://HOST/DATABASE?user=USER&password=PWD\", \"com.mysql.jdbc.Driver\" )", 1234);
                }
                Class.forName(this.dbClassName).newInstance();
                this.dbCon = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
            }
        }
        catch(Exception e)
        {
            LOG.error("error while getting database connection", e);
        }
        return this.dbCon;
    }


    public final void initDatabase(String url, String user, String password, String className)
    {
        if(url == null || className == null || (user == null && password != null))
        {
            throw new IllegalArgumentException("<null> not allowed! url:= " + url + ", className:= " + className);
        }
        this.dbUrl = url.trim();
        this.dbClassName = className.trim();
        this.dbUser = user;
        this.dbPassword = password;
        this.dbCon = null;
    }


    public void includeSQLData(String url, String user, String password, String className, String sqlStatement)
    {
        initDatabase(url, user, password, className);
        includeSQLData(sqlStatement);
    }


    public void includeSQLData(String sqlStatement)
    {
        includeSQLData(sqlStatement, 0, -1);
    }


    public void includeSQLData(String sqlStatement, int skip, int offset)
    {
        ResultSet resultSet = null;
        Statement stmt = null;
        Connection con = null;
        try
        {
            con = getConnection();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(sqlStatement);
            includeExternalData((CSVReader)new ResultSetCSVReader(resultSet, skip), offset);
        }
        catch(SQLException e)
        {
            LOG.error("Error for header '" + getCurrentHeader() + "' and statement '" + sqlStatement, e);
            if(resultSet != null)
            {
                try
                {
                    resultSet.close();
                }
                catch(SQLException ex)
                {
                    LOG.warn("Can not close result set: " + ex.getMessage(), ex);
                }
            }
            if(stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch(SQLException ex)
                {
                    LOG.warn("Can not close statement: " + ex.getMessage(), ex);
                }
            }
            try
            {
                con.close();
            }
            catch(SQLException ex)
            {
                LOG.warn("Can not close connection: " + ex.getMessage(), ex);
            }
        }
    }


    public void includeExternalData(File file, String encoding, int linesToSkip) throws UnsupportedEncodingException, FileNotFoundException
    {
        CSVReader csvReader = new CSVReader(file, encoding);
        csvReader.setLinesToSkip(linesToSkip);
        csvReader.setCommentOut(this.readerManager.getMainReader().getCommentOut());
        csvReader.setFieldSeparator(this.readerManager.getMainReader().getFieldSeparator());
        csvReader.setTextSeparator(this.readerManager.getMainReader().getTextSeparator());
        includeExternalData(csvReader, -1, file.getPath() + "/" + file.getPath());
    }


    public void includeExternalData(InputStream inputStream, String encoding, int linesToSkip) throws UnsupportedEncodingException
    {
        CSVReader csvReader = new CSVReader(inputStream, encoding);
        csvReader.setLinesToSkip(linesToSkip);
        csvReader.setCommentOut(this.readerManager.getMainReader().getCommentOut());
        csvReader.setFieldSeparator(this.readerManager.getMainReader().getFieldSeparator());
        csvReader.setTextSeparator(this.readerManager.getMainReader().getTextSeparator());
        includeExternalData(csvReader, -1, "InputStream");
    }


    public void includeExternalData(InputStream inputStream, String encoding, int linesToSkip, int columnOffset) throws UnsupportedEncodingException
    {
        CSVReader csvReader = new CSVReader(inputStream, encoding);
        csvReader.setLinesToSkip(linesToSkip);
        csvReader.setCommentOut(this.readerManager.getMainReader().getCommentOut());
        csvReader.setFieldSeparator(this.readerManager.getMainReader().getFieldSeparator());
        csvReader.setTextSeparator(this.readerManager.getMainReader().getTextSeparator());
        includeExternalData(csvReader, columnOffset, "InputStream");
    }


    public void includeExternalData(InputStream inputStream, String encoding, char[] delimiter, int linesToSkip, int columnOffset) throws UnsupportedEncodingException
    {
        CSVReader csvReader = new CSVReader(inputStream, encoding);
        csvReader.setLinesToSkip(linesToSkip);
        csvReader.setFieldSeparator(delimiter);
        csvReader.setCommentOut(this.readerManager.getMainReader().getCommentOut());
        csvReader.setTextSeparator(this.readerManager.getMainReader().getTextSeparator());
        includeExternalData(csvReader, columnOffset, "InputStream");
    }


    public void includeExternalData(String fileName, String fileEncoding, int linesToSkip) throws UnsupportedEncodingException, FileNotFoundException
    {
        CSVReader csvReader = new CSVReader(fileName, fileEncoding);
        csvReader.setLinesToSkip(linesToSkip);
        includeExternalData(csvReader, -1, fileName);
    }


    public void includeExternalData(CSVReader reader)
    {
        includeExternalData(reader, -1, String.valueOf(reader));
    }


    public void includeExternalData(CSVReader reader, int linesToSkip, int columnOffset)
    {
        includeExternalData(reader, columnOffset);
        reader.setLinesToSkip(linesToSkip);
    }


    public void includeExternalData(CSVReader reader, int columnOffset)
    {
        includeExternalData(reader, columnOffset, String.valueOf(reader));
    }


    public void includeExternalData(CSVReader reader, int columnOffset, String locationText)
    {
        reader.setMultiLineMode(isExternalSyntaxParsingEnabled());
        reader.setShowComments(true);
        this.readerManager.pushReader(reader, columnOffset, locationText);
    }


    public void includeExternalData(InputStream inputStream, String encoding, boolean setNewHeader) throws UnsupportedEncodingException, ImpExException
    {
        if(inputStream == null)
        {
            throw new ImpExException("Given stream is null");
        }
        CSVReader csvReader = new CSVReader(inputStream, encoding);
        if(setNewHeader)
        {
            Object obj = readLine();
            if(obj instanceof HeaderDescriptor)
            {
                setCurrentHeader((HeaderDescriptor)obj);
            }
            csvReader.setLinesToSkip(1);
        }
        else
        {
            csvReader.setLinesToSkip(0);
        }
        includeExternalData(csvReader, -1, "InputStream");
    }


    public void includeExternalDataMedia(String code) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = findExternalDataMedia(code);
            if(media == null)
            {
                throw new JaloInvalidParameterException("external data '" + code + "' doesn't match any attached media within " +
                                getAllExternalDataMediaCodes(), 0);
            }
            includeExternalDataMedia(media);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void includeExternalDataMedia(String code, int columnOffset) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = findExternalDataMedia(code);
            if(media == null)
            {
                throw new JaloInvalidParameterException("external data '" + code + "' doesn't match any attached media within " +
                                getAllExternalDataMediaCodes(), 0);
            }
            includeExternalDataMedia(media, columnOffset);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void includeExternalDataMedia(String code, int linesToSkip, int columnOffset) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = findExternalDataMedia(code);
            if(media == null)
            {
                throw new JaloInvalidParameterException("external data '" + code + "' doesn't match any attached media within " +
                                getAllExternalDataMediaCodes(), 0);
            }
            includeExternalDataMedia(media, Utilities.resolveEncoding(media.getEncoding()), media.getFieldSeparatorAsPrimitive(), linesToSkip, columnOffset);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void includeExternalDataMedia(String code, String encoding) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = findExternalDataMedia(code);
            if(media == null)
            {
                throw new JaloInvalidParameterException("external data '" + code + "' doesn't match any attached media within " +
                                getAllExternalDataMediaCodes(), 0);
            }
            includeExternalDataMedia(media, encoding, media.getFieldSeparatorAsPrimitive(), media.getLinesToSkipAsPrimitive(), -1);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void includeExternalDataMedia(String code, String encoding, int columnOffset) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = findExternalDataMedia(code);
            if(media == null)
            {
                throw new JaloInvalidParameterException("external data '" + code + "' doesn't match any attached media within " +
                                getAllExternalDataMediaCodes(), 0);
            }
            includeExternalDataMedia(media, encoding, media.getFieldSeparatorAsPrimitive(), media.getLinesToSkipAsPrimitive(), columnOffset);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void includeExternalDataMedia(String code, String encoding, int linesToSkip, int columnOffset) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = findExternalDataMedia(code);
            if(media == null)
            {
                throw new JaloInvalidParameterException("external data '" + code + "' doesn't match any attached media within " +
                                getAllExternalDataMediaCodes(), 0);
            }
            includeExternalDataMedia(media, encoding, media.getFieldSeparatorAsPrimitive(), linesToSkip, columnOffset);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void includeExternalDataMedia(String code, String encoding, char delimiter, int linesToSkip, int columnOffset) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = findExternalDataMedia(code);
            if(media == null)
            {
                throw new JaloInvalidParameterException("external data '" + code + "' doesn't match any attached media within " +
                                getAllExternalDataMediaCodes(), 0);
            }
            includeExternalDataMedia(media, encoding, delimiter, linesToSkip, columnOffset);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void includeExternalDataMedia(ImpExMedia media) throws UnsupportedEncodingException
    {
        includeExternalDataMedia(media, -1);
    }


    public void includeExternalDataMedia(ImpExMedia media, int columnOffset) throws UnsupportedEncodingException
    {
        if(media != null)
        {
            CSVReader csvReader;
            DataInputStream dataFromStreamSure = null;
            try
            {
                dataFromStreamSure = media.getDataFromStreamSure();
                csvReader = new CSVReader(dataFromStreamSure, Utilities.resolveEncoding(media.getEncoding()));
                csvReader.setFieldSeparator(new char[] {media
                                .getFieldSeparatorAsPrimitive()});
                csvReader.setLinesToSkip(media.getLinesToSkipAsPrimitive());
                csvReader.setCommentOut(new char[] {media
                                .getCommentCharacterAsPrimitive()});
                csvReader.setTextSeparator(media.getQuoteCharacterAsPrimitive());
            }
            catch(UnsupportedEncodingException e)
            {
                IOUtils.closeQuietly(dataFromStreamSure);
                throw e;
            }
            catch(JaloBusinessException e)
            {
                IOUtils.closeQuietly(dataFromStreamSure);
                throw new JaloSystemException(e);
            }
            includeExternalData(csvReader, columnOffset, media.getCode());
        }
        else
        {
            throw new JaloInvalidParameterException("external data is null", 0);
        }
    }


    public void includeExternalDataMedia(ImpExMedia media, String encoding, char delimiter, int linesToSkip, int columnOffset) throws UnsupportedEncodingException
    {
        if(media != null)
        {
            CSVReader csvReader;
            DataInputStream dataFromStreamSure = null;
            try
            {
                dataFromStreamSure = media.getDataFromStreamSure();
                csvReader = new CSVReader(dataFromStreamSure, encoding);
                csvReader.setFieldSeparator(new char[] {delimiter});
                csvReader.setLinesToSkip(linesToSkip);
            }
            catch(UnsupportedEncodingException e)
            {
                IOUtils.closeQuietly(dataFromStreamSure);
                throw e;
            }
            catch(JaloBusinessException e)
            {
                IOUtils.closeQuietly(dataFromStreamSure);
                throw new JaloSystemException(e);
            }
            includeExternalData(csvReader, columnOffset, media.getCode());
        }
        else
        {
            throw new JaloInvalidParameterException("external data is null", 0);
        }
    }


    public String getCurrentLocation()
    {
        return this.readerManager.getCurrentLocation();
    }


    protected CSVReader getCurrentReader()
    {
        return this.readerManager.peekReader();
    }


    public DocumentIDRegistry getDocumentIDRegistry()
    {
        return this.documentIDRegistry;
    }


    protected boolean gotInsertedLines()
    {
        return (this.insertedLines != null && !this.insertedLines.isEmpty());
    }


    public void insertLine(Map csvLine)
    {
        if(csvLine != null && !csvLine.isEmpty())
        {
            if(this.insertedLines == null)
            {
                this.insertedLines = new LinkedList<>();
            }
            this.insertedLines.offer(new CSVMap(csvLine));
        }
    }


    protected Map<Integer, String> readNextCSVLine()
    {
        Map<Integer, String> line = null;
        if(gotInsertedLines())
        {
            line = this.insertedLines.poll();
        }
        else
        {
            line = getCurrentReader().getLine();
        }
        return line;
    }


    protected Map<Integer, String> adjustLineIndexes(Map<Integer, String> line)
    {
        Map<Integer, String> ret = line;
        int offset = this.readerManager.getCurrentColumnOffset();
        if(offset != 0)
        {
            ret = new HashMap<>(line.size());
            for(Map.Entry<Integer, String> e : line.entrySet())
            {
                Integer index = e.getKey();
                ret.put(Integer.valueOf(index.intValue() - offset), e.getValue());
            }
        }
        return ret;
    }


    public Object readLine() throws ImpExException
    {
        Object ret = null;
        while(true)
        {
            if(!tryToReadNextLineForImporting())
            {
                return null;
            }
            Map<Integer, String> line = replaceDefinitions(readNextCSVLine());
            Map<Integer, String> adjustedLine = adjustLineIndexes(line);
            if(!isEmptyLine(line))
            {
                if(isCommentLine(line))
                {
                    if(isCodeLine(line))
                    {
                        processCodeLine(createCodeLine(line));
                    }
                }
                else if(isNotInInactiveIfBlock(null))
                {
                    if(isHeaderLine(adjustedLine))
                    {
                        ret = createAndSetAsCurrentHeader(adjustedLine);
                    }
                    else if(!this.userImport && isStartUserRights(line))
                    {
                        setBeforeEachCode(null);
                        this.userImport = true;
                        this.userRightsLines = new ArrayList<>();
                    }
                    else if(this.userImport && isEndUserRights(line))
                    {
                        this.userImport = false;
                        writeUserRightsLines();
                    }
                    else if(isDefinition(line))
                    {
                        addDefinition(line.get(FIRST));
                    }
                    else if(!this.skipValueLines)
                    {
                        ret = processValueLine(ret, adjustedLine);
                    }
                    else if(ImpExUtils.isExportMode(getValidationMode()))
                    {
                        throw new ImpExException("No valid line type found for " + line);
                    }
                }
            }
            if(ret != null)
            {
                return ret;
            }
        }
    }


    private HeaderDescriptor createAndSetAsCurrentHeader(Map<Integer, String> adjustedLine) throws HeaderValidationException
    {
        HeaderDescriptor header;
        if(getInvalidHeaderPolicy() == InvalidHeaderPolicy.DUMP_VALUE_LINES)
        {
            try
            {
                header = createNewHeader(adjustedLine);
            }
            catch(HeaderValidationException ex)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Invalid header in ImpEx: " + ex);
                }
                header = createInvalidHeader(adjustedLine, ex);
            }
        }
        else
        {
            header = createNewHeader(adjustedLine);
        }
        setCurrentHeader(header);
        return header;
    }


    private Object processValueLine(Object ret, Map<Integer, String> adjustedLine) throws ImpExException
    {
        if(this.userImport)
        {
            storeUserRightsLine(adjustedLine);
        }
        else if(getCurrentHeader() == null)
        {
            if(getInvalidHeaderPolicy() == InvalidHeaderPolicy.DUMP_VALUE_LINES)
            {
                addNoHeaderInfoAsComment(adjustedLine);
                ValueLine valueLine = createValueLine(null, adjustedLine);
                valueLine.markUnresolved();
                ret = valueLine;
            }
            else
            {
                throw new HeaderValidationException("no current header for value line: " + getCurrentReader().getSourceLine(), 0);
            }
        }
        else
        {
            if(getBeforeEachCode() != null)
            {
                execute(getBeforeEachCode(), adjustedLine, true);
            }
            if(hasCellDecorators())
            {
                adjustedLine = CSVReader.applyDecorators(getCellDecorators(), adjustedLine);
            }
            if(!isEmptyLine(adjustedLine))
            {
                ret = createValueLine(getCurrentHeader(), adjustedLine);
            }
        }
        return ret;
    }


    private boolean tryToReadNextLineForImporting() throws ImpExException
    {
        boolean gotOne = gotInsertedLines();
        while(!gotOne)
        {
            CSVReader csvReader = getCurrentReader();
            try
            {
                gotOne = csvReader.readNextLine();
            }
            catch(IllegalStateException e)
            {
                throw new ImpExException(e);
            }
            if(!gotOne)
            {
                if(isIncludingExternalData())
                {
                    this.readerManager.popReader();
                    continue;
                }
                if(this.userImport)
                {
                    LOG.warn("Userrights definition block is missing end tag");
                }
                if(!this.ifStack.isEmpty())
                {
                    LOG.warn("Not each 'if:' has a corresponding 'endif:'");
                    this.ifStack.clear();
                }
                return false;
            }
        }
        return true;
    }


    private void addNoHeaderInfoAsComment(Map<Integer, String> adjustedLine)
    {
        int lastIdx = adjustedLine.size();
        if(!((String)adjustedLine.get(Integer.valueOf(lastIdx - 1))).equals("# no current header for value line"))
        {
            adjustedLine.put(Integer.valueOf(lastIdx), "# no current header for value line");
        }
    }


    protected void addHeaderExceptionInfoAsComment(Map<Integer, String> adjustedLine, HeaderValidationException ex)
    {
        String errorComment = "# " + ex.getMessage();
        int lastIdx = adjustedLine.size();
        if(!((String)adjustedLine.get(Integer.valueOf(lastIdx - 1))).equals(errorComment))
        {
            adjustedLine.put(Integer.valueOf(lastIdx), errorComment);
        }
    }


    public HeaderDescriptor createInvalidHeader(Map<Integer, String> line, HeaderValidationException ex) throws HeaderValidationException
    {
        addHeaderExceptionInfoAsComment(line, ex);
        try
        {
            List<String> columns = applyHeaderReplacements(lineToList(line));
            String expr = columns.get(0);
            return new HeaderDescriptor(this, expr, columns.subList(1, columns.size()), getCurrentLocation(), this.documentIDRegistry, ex);
        }
        catch(Exception e)
        {
            LOG.warn("Exception in a header constructor: " + e.getMessage());
            return null;
        }
    }


    protected boolean hasCellDecorators()
    {
        return (this.decorators != null && !this.decorators.isEmpty());
    }


    protected Map<Integer, CSVCellDecorator> getCellDecorators()
    {
        return (this.decorators != null) ? this.decorators : Collections.EMPTY_MAP;
    }


    protected void setCellDecorators(HeaderDescriptor header)
    {
        this.decorators = null;
        if(isValidHeader(header))
        {
            this.decorators = new HashMap<>();
            for(Iterator<AbstractColumnDescriptor> iter = header.getColumns().iterator(); iter.hasNext(); )
            {
                AbstractColumnDescriptor columnDescriptor = iter.next();
                if(columnDescriptor != null && (!(columnDescriptor instanceof StandardColumnDescriptor) ||
                                !((StandardColumnDescriptor)columnDescriptor).isVirtual()) && columnDescriptor
                                .getCSVCellDecorator() != null)
                {
                    this.decorators.put(Integer.valueOf(columnDescriptor.getValuePosition()), columnDescriptor
                                    .getCSVCellDecorator());
                }
            }
        }
    }


    private boolean isValidHeader(HeaderDescriptor header)
    {
        return (header != null && header.getInvalidHeaderException() == null);
    }


    protected void storeUserRightsLine(Map line)
    {
        this.userRightsLineEntry.setLength(0);
        int count = line.size();
        for(int i = 0; i < count; i++)
        {
            this.userRightsLineEntry.append((String)line.get(Integer.valueOf(i)));
            if(i < count)
            {
                this.userRightsLineEntry.append(getCurrentReader().getFieldSeparator()[0]);
            }
        }
        this.userRightsLines.add(this.userRightsLineEntry.toString());
    }


    protected void writeUserRightsLines() throws ImpExException
    {
        if(this.userRightsLines == null || this.userRightsLines.isEmpty())
        {
            throw new ImpExException("userrights import - missing values!!!", 0);
        }
        StringBuilder buf = new StringBuilder();
        for(String text : this.userRightsLines)
        {
            buf.append(text + "\n");
        }
        ImportExportUserRightsHelper importer = new ImportExportUserRightsHelper(new StringReader(buf.toString()), getCurrentReader().getTextSeparator(), getCurrentReader().getFieldSeparator()[0], ',', false);
        importer.importSecurity();
    }


    protected void addDefinition(String cell)
    {
        if(this.definitions == null)
        {
            this.definitions = new HashMap<>();
        }
        String[] def = splitDefinitonCell(cell);
        this.definitions.put(def[0], def[1]);
    }


    protected String[] splitDefinitonCell(String cell)
    {
        int index = cell.indexOf('=');
        String[] ret = new String[2];
        ret[0] = (index > -1) ? cell.substring(0, index).trim() : cell;
        ret[1] = (index > -1) ? cell.substring(index + 1).trim() : "";
        return ret;
    }


    protected void checkDefinitonKey(String newKey, Map currentDefinitions) throws ImpExException
    {
        for(Iterator<Map.Entry> iter = currentDefinitions.entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry entry = iter.next();
            String existingKey = (String)entry.getKey();
            if(!existingKey.equals(newKey) && (existingKey.startsWith(newKey) || newKey.startsWith(existingKey)))
            {
                throw new ImpExException(getCurrentLocation() + ": definition clash between '" + getCurrentLocation() + "' and '" + existingKey + "' - definition keys cannot be prefixes of other keys");
            }
        }
    }


    protected Map<Integer, String> replaceDefinitions(Map<Integer, String> line)
    {
        if(isExternalSyntaxParsingEnabled() || !isIncludingExternalData())
        {
            if(line == null)
            {
                return null;
            }
            if(line.isEmpty() || this.definitions.isEmpty())
            {
                return line;
            }
            Map<Integer, String> ret = new HashMap<>(line);
            for(Map.Entry<Integer, String> e : ret.entrySet())
            {
                Integer pos = e.getKey();
                String vStr = e.getValue();
                if(FIRST.equals(pos) && isDefinition(line))
                {
                    String[] def = splitDefinitonCell(vStr);
                    e.setValue(def[0] + "=" + def[0]);
                    continue;
                }
                if(vStr != null)
                {
                    e.setValue(replaceDefinitionsInCell(vStr));
                }
            }
            return ret;
        }
        return line;
    }


    private String replaceDefinitionsInCell(String cell)
    {
        String ret = cell;
        for(Iterator<Map.Entry> it = this.definitions.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            String keyStr = (String)entry.getKey();
            String replacement = (String)entry.getValue();
            StringBuilder stringBuilder = null;
            int last = 0;
            int pos;
            for(pos = ret.indexOf(keyStr); pos >= last; pos = ret.indexOf(keyStr, last))
            {
                if(stringBuilder == null)
                {
                    stringBuilder = new StringBuilder();
                }
                if(pos > last)
                {
                    stringBuilder.append(ret.substring(last, pos));
                }
                stringBuilder.append(replacement);
                last = pos + keyStr.length();
            }
            if(last < ret.length() && stringBuilder != null)
            {
                stringBuilder.append(ret.substring(last));
            }
            if(stringBuilder != null)
            {
                ret = stringBuilder.toString();
            }
        }
        return ret;
    }


    protected String findMarker(String expr)
    {
        String expression = expr.toLowerCase(LocaleHelper.getPersistenceLocale());
        if(expression.startsWith("beforeEach:end".toLowerCase(LocaleHelper.getPersistenceLocale())))
        {
            return "beforeEach:end";
        }
        if(expression.startsWith("beforeEach:".toLowerCase(LocaleHelper.getPersistenceLocale())))
        {
            return "beforeEach:";
        }
        if(expression.startsWith("if:".toLowerCase(LocaleHelper.getPersistenceLocale())))
        {
            return "if:";
        }
        if(expression.startsWith("endif:".toLowerCase(LocaleHelper.getPersistenceLocale())))
        {
            return "endif:";
        }
        return null;
    }


    protected AbstractCodeLine createCodeLine(Map<Integer, String> line)
    {
        if(isLegacyScriptingEnabled())
        {
            return createCodeLineLegacyWay(line);
        }
        return createCodeLineModernWay(line);
    }


    protected AbstractCodeLine createCodeLineLegacyWay(Map<Integer, String> line)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String marker = null;
        List<Integer> keys = new ArrayList<>(line.keySet());
        Collections.sort(keys);
        for(Integer cellNr : keys)
        {
            String cell = line.get(cellNr);
            if(cell != null)
            {
                if(FIRST.equals(cellNr) && CSVUtils.lineStartsWith(cell, getCurrentReader().getCommentOut(), "%"))
                {
                    String expr = cell.substring(2).trim();
                    marker = findMarker(expr);
                    stringBuilder.append((marker != null) ? expr.substring(marker.length()) : expr);
                    continue;
                }
                stringBuilder.append(cell);
            }
        }
        return (AbstractCodeLine)new SimpleCodeLine(line, marker, stringBuilder.toString().trim(), getCurrentReader().getCurrentLineNumber(),
                        getCurrentLocation());
    }


    protected AbstractCodeLine createCodeLineModernWay(Map<Integer, String> line)
    {
        return this.codeLineFactory.createCodeLineFromLine(line);
    }


    protected HeaderDescriptor createNewHeader(Map<Integer, String> line) throws HeaderValidationException
    {
        List<String> columns = applyHeaderReplacements(lineToList(line));
        String expr = columns.get(0);
        HeaderDescriptor header = new HeaderDescriptor(this, expr, columns.subList(1, columns.size()), getCurrentLocation(), this.documentIDRegistry);
        header.validate();
        return header;
    }


    protected List<String> applyHeaderReplacements(List<String> columns)
    {
        if(!this.replacementRules.isEmpty())
        {
            for(int i = 0, s = columns.size(); i < s; i++)
            {
                boolean modified = false;
                String col = columns.get(i);
                if(i != 0 && col != null && col.length() != 0)
                {
                    for(Object[] rule : this.replacementRules)
                    {
                        try
                        {
                            String repl = col.replaceAll((String)rule[1], (String)rule[2]);
                            if(!repl.equals(col))
                            {
                                columns.set(i, repl);
                                modified = true;
                                break;
                            }
                        }
                        catch(Exception e)
                        {
                            LOG.error("invalid replacement rule " + rule + " - error " + e.getMessage());
                            LOG.error(Utilities.getStackTraceAsString(e));
                        }
                    }
                    if(modified)
                    {
                        columns.set(i, replaceDefinitionsInCell(columns.get(i)));
                    }
                }
            }
        }
        return columns;
    }


    protected int getLineSize(Map<Integer, String> line)
    {
        int maxIdx = -1;
        for(Map.Entry<Integer, String> e : line.entrySet())
        {
            maxIdx = Math.max(maxIdx, ((Integer)e.getKey()).intValue());
        }
        return maxIdx + 1;
    }


    protected List<String> lineToList(Map<Integer, String> line)
    {
        String[] columns = new String[getLineSize(line)];
        for(Map.Entry<Integer, String> e : line.entrySet())
        {
            columns[((Integer)e.getKey()).intValue()] = e.getValue();
        }
        return Arrays.asList(columns);
    }


    protected boolean isEmptyLine(Map<Integer, String> line)
    {
        if(line.isEmpty())
        {
            return true;
        }
        for(Iterator<Map.Entry> iter = line.entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry entry = iter.next();
            String value = (String)entry.getValue();
            if(value != null && value.length() > 0)
            {
                return false;
            }
        }
        return true;
    }


    public void setValidationMode(String mode)
    {
        setValidationMode(ImpExManager.getValidationMode(mode));
    }


    public void setValidationMode(EnumerationValue headerValidationMode)
    {
        this.headerValidationMode = headerValidationMode;
    }


    public EnumerationValue getValidationMode()
    {
        return this.headerValidationMode;
    }


    @Deprecated(since = "3.0", forRemoval = false)
    public void setRelaxedMode(boolean enable)
    {
        if(enable)
        {
            setValidationMode(ImpExManager.getImportRelaxedMode());
        }
        else
        {
            setValidationMode(getStrictMode());
        }
    }


    @Deprecated(since = "3.0", forRemoval = false)
    public void setRelaxedMode(String enable)
    {
        setRelaxedMode(Boolean.parseBoolean(enable));
    }


    protected boolean isCommentLine(Map<Integer, String> line)
    {
        if(isExternalSyntaxParsingEnabled() || !isIncludingExternalData())
        {
            String cellValue = line.get(FIRST);
            cellValue = (cellValue == null) ? null : cellValue.toUpperCase(LocaleHelper.getPersistenceLocale());
            return (cellValue != null && CSVUtils.lineStartsWith(cellValue, getCurrentReader().getCommentOut(), null));
        }
        return false;
    }


    protected boolean isCodeLine(Map<Integer, String> line)
    {
        if(isExternalSyntaxParsingEnabled() || !isIncludingExternalData())
        {
            String cellValue = line.get(FIRST);
            cellValue = (cellValue == null) ? null : cellValue.toUpperCase(LocaleHelper.getPersistenceLocale());
            return (cellValue != null && CSVUtils.lineStartsWith(cellValue, getCurrentReader().getCommentOut(), "%"));
        }
        return false;
    }


    protected boolean isHeaderLine(Map<Integer, String> line)
    {
        if(isExternalSyntaxParsingEnabled() || !isIncludingExternalData())
        {
            Optional<String> tmp = Optional.ofNullable(line.get(FIRST));
            return tmp.<String>map(s -> s.toLowerCase(LocaleHelper.getPersistenceLocale())).filter(((Pattern)HEADER_LINE_PATTERN_SUPPLIER.get()).asPredicate()).isPresent();
        }
        return false;
    }


    protected boolean isDefinition(Map<Integer, String> line)
    {
        if(isExternalSyntaxParsingEnabled() || !isIncludingExternalData())
        {
            String cellValue = line.get(FIRST);
            cellValue = (cellValue == null) ? null : cellValue.toUpperCase(LocaleHelper.getPersistenceLocale());
            return (cellValue != null && cellValue.startsWith("$"));
        }
        return false;
    }


    protected boolean isStartUserRights(Map<Integer, String> line)
    {
        if(isExternalSyntaxParsingEnabled() || !isIncludingExternalData())
        {
            String cellValue = line.get(FIRST);
            cellValue = (cellValue != null) ? cellValue.toUpperCase(LocaleHelper.getPersistenceLocale()) : null;
            return (cellValue != null && cellValue
                            .startsWith("$START_USERRIGHTS"));
        }
        return false;
    }


    protected boolean isEndUserRights(Map<Integer, String> line)
    {
        if(isExternalSyntaxParsingEnabled() || !isIncludingExternalData())
        {
            String cellValue = line.get(FIRST);
            cellValue = (cellValue != null) ? cellValue.toUpperCase(LocaleHelper.getPersistenceLocale()) : null;
            return (cellValue != null && cellValue
                            .startsWith("$END_USERRIGHTS"));
        }
        return false;
    }


    protected ValueLine createValueLine(HeaderDescriptor header, Map<Integer, String> line)
    {
        return new ValueLine(header, line.get(FIRST), line, getCurrentReader().getCurrentLineNumber(), getCurrentLocation());
    }


    public final boolean isCodeExecutionEnabled()
    {
        return this.enableBeanShell;
    }


    public void enableCodeExecution(boolean isOn)
    {
        this.enableBeanShell = isOn;
    }


    public final boolean isExternalCodeExecutionEnabled()
    {
        return this.enableBeanShellForExternalData;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void enableExternalDataCodeExecution(boolean isOn)
    {
        enableExternalCodeExecution(isOn);
    }


    public void enableExternalCodeExecution(boolean isOn)
    {
        if(isOn && !isExternalSyntaxParsingEnabled())
        {
            throw new IllegalStateException("ImpEx syntax is not active - cannot turn on external data code execution");
        }
        this.enableBeanShellForExternalData = isOn;
    }


    protected void setBeforeEachCode(AbstractCodeLine line)
    {
        if(line != null && !isCodeExecutionEnabled())
        {
            throw new IllegalStateException("code execution is not enabled");
        }
        this.beforeEachExpr = line;
    }


    public AbstractCodeLine getBeforeEachCode()
    {
        return this.beforeEachExpr;
    }


    public boolean enterIfBlock(AbstractCodeLine line)
    {
        if(!this.ifStack.isEmpty())
        {
            AbstractCodeLine prevLine = this.ifStack.peek();
            Object result = prevLine.getResult();
            if(!((Boolean)result).booleanValue())
            {
                line.setResult(Boolean.FALSE);
            }
        }
        this.ifStack.push(line);
        return (line.getResult() != Boolean.FALSE);
    }


    public void exitIfBlock()
    {
        this.ifStack.pop();
    }


    protected boolean isNotInInactiveIfBlock(AbstractCodeLine curLine)
    {
        if(this.ifStack.isEmpty())
        {
            return true;
        }
        AbstractCodeLine line = this.ifStack.peek();
        Object result = line.getResult();
        if(line == curLine)
        {
            return true;
        }
        if(!(result instanceof Boolean))
        {
            if(isCodeExecutionEnabled())
            {
                LOG.warn("Expression '" + line.getExecutableCode() + "' within 'if:' statement is not a valid expression, will interpret it as 'false'");
                result = Boolean.FALSE;
            }
            else
            {
                result = Boolean.TRUE;
            }
            line.setResult(result);
        }
        return ((Boolean)result).booleanValue();
    }


    protected boolean processMarkerCodeLine(AbstractCodeLine line) throws ImpExException
    {
        String marker = line.getMarker();
        if(marker.equalsIgnoreCase("beforeEach:end"))
        {
            setBeforeEachCode(null);
        }
        else if(marker.equalsIgnoreCase("beforeEach:"))
        {
            setBeforeEachCode(line);
        }
        else
        {
            if(marker.equalsIgnoreCase("if:"))
            {
                return enterIfBlock(line);
            }
            if(marker.equalsIgnoreCase("endif:"))
            {
                exitIfBlock();
            }
            else
            {
                throw new ImpExException("unknown code marker '" + marker + "' within line " + line);
            }
        }
        return false;
    }


    protected void processCodeLine(AbstractCodeLine line) throws ImpExException
    {
        if(isCodeExecutionEnabled())
        {
            if(!isExternalCodeExecutionEnabled() && isIncludingExternalData())
            {
                LOG.warn("blocked code execution from external data ( line: " + line.getLineNumber() + ")");
            }
            else if(line.getMarker() != null)
            {
                if(processMarkerCodeLine(line))
                {
                    execute(line, line.getSourceLine());
                }
            }
            else
            {
                execute(line, line.getSourceLine());
            }
        }
        else
        {
            LOG.warn("skipped code line " + line + " since bean shell is not enabled");
        }
    }


    protected void setBeanShellContext(Interpreter shell, Map<Integer, String> line) throws EvalError
    {
        shell.set("line", line);
        shell.set("currentLineNumber", Integer.valueOf(getCurrentReader().getCurrentLineNumber()));
        shell.set("currentLocation", getCurrentLocation());
    }


    protected Map<String, Object> getScriptExecutionContext(Map<Integer, String> line)
    {
        Map<String, Object> result = new HashMap<>(3);
        result.put("impex", this);
        result.put("line", line);
        result.put("currentLineNumber", Integer.valueOf(getCurrentReader().getCurrentLineNumber()));
        result.put("currentLocation", getCurrentLocation());
        return Collections.unmodifiableMap(result);
    }


    protected void addToBeanShellContext(String key, Object value) throws EvalError
    {
        if(this.bsh != null)
        {
            this.bsh.set(key, value);
        }
    }


    public Object getFromBeanShellContext(String key) throws EvalError
    {
        if(this.bsh != null)
        {
            return this.bsh.get(key);
        }
        return null;
    }


    protected void execute(AbstractCodeLine line, Map csvLine) throws ImpExException
    {
        execute(line, csvLine, false);
    }


    protected void execute(AbstractCodeLine line, Map<Integer, String> csvLine, boolean forEachMode) throws ImpExException
    {
        if(isCodeExecutionEnabled())
        {
            if(!forEachMode && !isExternalCodeExecutionEnabled() && isIncludingExternalData())
            {
                LOG.warn("Since external code execution is not enabled skipped execution of line: " + line);
            }
            else if(isNotInInactiveIfBlock(line))
            {
                if(isLegacyScriptingEnabled())
                {
                    legacyExecute(line, csvLine);
                }
                else
                {
                    modernExecute((AbstractScriptingEngineCodeLine)line, csvLine);
                }
            }
        }
        else
        {
            LOG.warn("Since code execution is not enabled skipped execution of line: " + line);
        }
    }


    protected void modernExecute(AbstractScriptingEngineCodeLine line, Map<Integer, String> csvLine) throws ImpExException
    {
        try
        {
            Stopwatch stopwatch = Stopwatch.createStarted();
            line.executeAndSetResult(getScriptExecutionContext(csvLine));
            stopwatch.stop();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Executed code line (modern way) at " + line.getLineNumber() + " in " + stopwatch + ", result: " + line
                                .getResult());
            }
        }
        catch(ScriptingException e)
        {
            throw new ImpExException(e, "error executing code line at " + line.getLineNumber() + " : " + e.getMessage(), 0);
        }
    }


    protected void legacyExecute(AbstractCodeLine line, Map<Integer, String> csvLine) throws ImpExException
    {
        assureBeanShellLoaded();
        try
        {
            synchronized(this.bsh)
            {
                Stopwatch stopwatch = Stopwatch.createStarted();
                setBeanShellContext(this.bsh, csvLine);
                Object result = this.bsh.eval(line.getExecutableCode());
                line.setResult(result);
                stopwatch.stop();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Executed code line (legacy way) at " + line.getLineNumber() + " in " + stopwatch + ", result: " + line
                                    .getResult());
                }
            }
        }
        catch(EvalError e)
        {
            Throwable throwable = (e.getCause() != null) ? e.getCause() : (Throwable)e;
            throw new ImpExException(throwable, "error executing code line at " + line
                            .getLineNumber() + " : " + throwable.getMessage(), 0);
        }
    }


    private boolean isLegacyScriptingEnabled()
    {
        return this.legacyScriptingEnabled;
    }


    protected void assureBeanShellLoaded() throws ImpExException
    {
        if(this.bsh == null)
        {
            this.bsh = BeanShellUtils.createInterpreter(Collections.singletonMap("impex", this));
            this.bsh.setOut(new PrintStream((OutputStream)new Object(this)));
            this.bsh.setErr(new PrintStream((OutputStream)new Object(this)));
        }
    }


    public boolean isDebugEnabled()
    {
        return LOG.isDebugEnabled();
    }


    public void debug(String msg, Throwable throwable)
    {
        LOG.debug(msg, throwable);
    }


    public void debug(String msg)
    {
        LOG.debug(msg);
    }


    public boolean isInfoEnabled()
    {
        return LOG.isInfoEnabled();
    }


    public void info(String msg)
    {
        LOG.info(msg);
    }


    public void info(String msg, Throwable throwable)
    {
        LOG.info(msg, throwable);
    }


    public void warn(String msg)
    {
        LOG.warn(msg);
    }


    public void warn(String msg, Throwable throwable)
    {
        LOG.warn(msg, throwable);
    }


    public void error(String msg)
    {
        LOG.error(msg);
    }


    public void error(String msg, Throwable throwable)
    {
        LOG.error(msg, throwable);
    }
}
