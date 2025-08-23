package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.HeaderLibrary;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.impex.jalo.exp.Report;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedImpExExportCronJob extends CronJob
{
    public static final String ENCODING = "encoding";
    public static final String MODE = "mode";
    public static final String DATAEXPORTTARGET = "dataExportTarget";
    public static final String MEDIASEXPORTTARGET = "mediasExportTarget";
    public static final String EXPORTTEMPLATE = "exportTemplate";
    public static final String EXPORT = "export";
    public static final String ITEMSEXPORTED = "itemsExported";
    public static final String ITEMSMAXCOUNT = "itemsMaxCount";
    public static final String ITEMSSKIPPED = "itemsSkipped";
    public static final String JOBMEDIA = "jobMedia";
    public static final String FIELDSEPARATOR = "fieldSeparator";
    public static final String QUOTECHARACTER = "quoteCharacter";
    public static final String COMMENTCHARACTER = "commentCharacter";
    public static final String DATAEXPORTMEDIACODE = "dataExportMediaCode";
    public static final String MEDIASEXPORTMEDIACODE = "mediasExportMediaCode";
    public static final String REPORT = "report";
    public static final String CONVERTERCLASS = "converterClass";
    public static final String SINGLEFILE = "singleFile";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("encoding", Item.AttributeMode.INITIAL);
        tmp.put("mode", Item.AttributeMode.INITIAL);
        tmp.put("dataExportTarget", Item.AttributeMode.INITIAL);
        tmp.put("mediasExportTarget", Item.AttributeMode.INITIAL);
        tmp.put("exportTemplate", Item.AttributeMode.INITIAL);
        tmp.put("export", Item.AttributeMode.INITIAL);
        tmp.put("itemsExported", Item.AttributeMode.INITIAL);
        tmp.put("itemsMaxCount", Item.AttributeMode.INITIAL);
        tmp.put("itemsSkipped", Item.AttributeMode.INITIAL);
        tmp.put("jobMedia", Item.AttributeMode.INITIAL);
        tmp.put("fieldSeparator", Item.AttributeMode.INITIAL);
        tmp.put("quoteCharacter", Item.AttributeMode.INITIAL);
        tmp.put("commentCharacter", Item.AttributeMode.INITIAL);
        tmp.put("dataExportMediaCode", Item.AttributeMode.INITIAL);
        tmp.put("mediasExportMediaCode", Item.AttributeMode.INITIAL);
        tmp.put("report", Item.AttributeMode.INITIAL);
        tmp.put("converterClass", Item.AttributeMode.INITIAL);
        tmp.put("singleFile", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Character getCommentCharacter(SessionContext ctx)
    {
        return (Character)getProperty(ctx, "commentCharacter");
    }


    public Character getCommentCharacter()
    {
        return getCommentCharacter(getSession().getSessionContext());
    }


    public char getCommentCharacterAsPrimitive(SessionContext ctx)
    {
        Character value = getCommentCharacter(ctx);
        return (value != null) ? value.charValue() : Character.MIN_VALUE;
    }


    public char getCommentCharacterAsPrimitive()
    {
        return getCommentCharacterAsPrimitive(getSession().getSessionContext());
    }


    public void setCommentCharacter(SessionContext ctx, Character value)
    {
        setProperty(ctx, "commentCharacter", value);
    }


    public void setCommentCharacter(Character value)
    {
        setCommentCharacter(getSession().getSessionContext(), value);
    }


    public void setCommentCharacter(SessionContext ctx, char value)
    {
        setCommentCharacter(ctx, Character.valueOf(value));
    }


    public void setCommentCharacter(char value)
    {
        setCommentCharacter(getSession().getSessionContext(), value);
    }


    public EnumerationValue getConverterClass(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "converterClass");
    }


    public EnumerationValue getConverterClass()
    {
        return getConverterClass(getSession().getSessionContext());
    }


    public void setConverterClass(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "converterClass", value);
    }


    public void setConverterClass(EnumerationValue value)
    {
        setConverterClass(getSession().getSessionContext(), value);
    }


    public String getDataExportMediaCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dataExportMediaCode");
    }


    public String getDataExportMediaCode()
    {
        return getDataExportMediaCode(getSession().getSessionContext());
    }


    public void setDataExportMediaCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "dataExportMediaCode", value);
    }


    public void setDataExportMediaCode(String value)
    {
        setDataExportMediaCode(getSession().getSessionContext(), value);
    }


    public ImpExExportMedia getDataExportTarget(SessionContext ctx)
    {
        return (ImpExExportMedia)getProperty(ctx, "dataExportTarget");
    }


    public ImpExExportMedia getDataExportTarget()
    {
        return getDataExportTarget(getSession().getSessionContext());
    }


    public void setDataExportTarget(SessionContext ctx, ImpExExportMedia value)
    {
        setProperty(ctx, "dataExportTarget", value);
    }


    public void setDataExportTarget(ImpExExportMedia value)
    {
        setDataExportTarget(getSession().getSessionContext(), value);
    }


    public EnumerationValue getEncoding(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "encoding");
    }


    public EnumerationValue getEncoding()
    {
        return getEncoding(getSession().getSessionContext());
    }


    public void setEncoding(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "encoding", value);
    }


    public void setEncoding(EnumerationValue value)
    {
        setEncoding(getSession().getSessionContext(), value);
    }


    public Export getExport(SessionContext ctx)
    {
        return (Export)getProperty(ctx, "export");
    }


    public Export getExport()
    {
        return getExport(getSession().getSessionContext());
    }


    public void setExport(SessionContext ctx, Export value)
    {
        setProperty(ctx, "export", value);
    }


    public void setExport(Export value)
    {
        setExport(getSession().getSessionContext(), value);
    }


    public HeaderLibrary getExportTemplate(SessionContext ctx)
    {
        return (HeaderLibrary)getProperty(ctx, "exportTemplate");
    }


    public HeaderLibrary getExportTemplate()
    {
        return getExportTemplate(getSession().getSessionContext());
    }


    public void setExportTemplate(SessionContext ctx, HeaderLibrary value)
    {
        setProperty(ctx, "exportTemplate", value);
    }


    public void setExportTemplate(HeaderLibrary value)
    {
        setExportTemplate(getSession().getSessionContext(), value);
    }


    public Character getFieldSeparator(SessionContext ctx)
    {
        return (Character)getProperty(ctx, "fieldSeparator");
    }


    public Character getFieldSeparator()
    {
        return getFieldSeparator(getSession().getSessionContext());
    }


    public char getFieldSeparatorAsPrimitive(SessionContext ctx)
    {
        Character value = getFieldSeparator(ctx);
        return (value != null) ? value.charValue() : Character.MIN_VALUE;
    }


    public char getFieldSeparatorAsPrimitive()
    {
        return getFieldSeparatorAsPrimitive(getSession().getSessionContext());
    }


    public void setFieldSeparator(SessionContext ctx, Character value)
    {
        setProperty(ctx, "fieldSeparator", value);
    }


    public void setFieldSeparator(Character value)
    {
        setFieldSeparator(getSession().getSessionContext(), value);
    }


    public void setFieldSeparator(SessionContext ctx, char value)
    {
        setFieldSeparator(ctx, Character.valueOf(value));
    }


    public void setFieldSeparator(char value)
    {
        setFieldSeparator(getSession().getSessionContext(), value);
    }


    public Integer getItemsExported(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "itemsExported");
    }


    public Integer getItemsExported()
    {
        return getItemsExported(getSession().getSessionContext());
    }


    public int getItemsExportedAsPrimitive(SessionContext ctx)
    {
        Integer value = getItemsExported(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getItemsExportedAsPrimitive()
    {
        return getItemsExportedAsPrimitive(getSession().getSessionContext());
    }


    public Integer getItemsMaxCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "itemsMaxCount");
    }


    public Integer getItemsMaxCount()
    {
        return getItemsMaxCount(getSession().getSessionContext());
    }


    public int getItemsMaxCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getItemsMaxCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getItemsMaxCountAsPrimitive()
    {
        return getItemsMaxCountAsPrimitive(getSession().getSessionContext());
    }


    public Integer getItemsSkipped(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "itemsSkipped");
    }


    public Integer getItemsSkipped()
    {
        return getItemsSkipped(getSession().getSessionContext());
    }


    public int getItemsSkippedAsPrimitive(SessionContext ctx)
    {
        Integer value = getItemsSkipped(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getItemsSkippedAsPrimitive()
    {
        return getItemsSkippedAsPrimitive(getSession().getSessionContext());
    }


    public ImpExMedia getJobMedia(SessionContext ctx)
    {
        return (ImpExMedia)getProperty(ctx, "jobMedia");
    }


    public ImpExMedia getJobMedia()
    {
        return getJobMedia(getSession().getSessionContext());
    }


    public void setJobMedia(SessionContext ctx, ImpExMedia value)
    {
        setProperty(ctx, "jobMedia", value);
    }


    public void setJobMedia(ImpExMedia value)
    {
        setJobMedia(getSession().getSessionContext(), value);
    }


    public String getMediasExportMediaCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mediasExportMediaCode");
    }


    public String getMediasExportMediaCode()
    {
        return getMediasExportMediaCode(getSession().getSessionContext());
    }


    public void setMediasExportMediaCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "mediasExportMediaCode", value);
    }


    public void setMediasExportMediaCode(String value)
    {
        setMediasExportMediaCode(getSession().getSessionContext(), value);
    }


    public ImpExExportMedia getMediasExportTarget(SessionContext ctx)
    {
        return (ImpExExportMedia)getProperty(ctx, "mediasExportTarget");
    }


    public ImpExExportMedia getMediasExportTarget()
    {
        return getMediasExportTarget(getSession().getSessionContext());
    }


    public void setMediasExportTarget(SessionContext ctx, ImpExExportMedia value)
    {
        setProperty(ctx, "mediasExportTarget", value);
    }


    public void setMediasExportTarget(ImpExExportMedia value)
    {
        setMediasExportTarget(getSession().getSessionContext(), value);
    }


    public EnumerationValue getMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "mode");
    }


    public EnumerationValue getMode()
    {
        return getMode(getSession().getSessionContext());
    }


    public void setMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "mode", value);
    }


    public void setMode(EnumerationValue value)
    {
        setMode(getSession().getSessionContext(), value);
    }


    public Character getQuoteCharacter(SessionContext ctx)
    {
        return (Character)getProperty(ctx, "quoteCharacter");
    }


    public Character getQuoteCharacter()
    {
        return getQuoteCharacter(getSession().getSessionContext());
    }


    public char getQuoteCharacterAsPrimitive(SessionContext ctx)
    {
        Character value = getQuoteCharacter(ctx);
        return (value != null) ? value.charValue() : Character.MIN_VALUE;
    }


    public char getQuoteCharacterAsPrimitive()
    {
        return getQuoteCharacterAsPrimitive(getSession().getSessionContext());
    }


    public void setQuoteCharacter(SessionContext ctx, Character value)
    {
        setProperty(ctx, "quoteCharacter", value);
    }


    public void setQuoteCharacter(Character value)
    {
        setQuoteCharacter(getSession().getSessionContext(), value);
    }


    public void setQuoteCharacter(SessionContext ctx, char value)
    {
        setQuoteCharacter(ctx, Character.valueOf(value));
    }


    public void setQuoteCharacter(char value)
    {
        setQuoteCharacter(getSession().getSessionContext(), value);
    }


    public Report getReport(SessionContext ctx)
    {
        return (Report)getProperty(ctx, "report");
    }


    public Report getReport()
    {
        return getReport(getSession().getSessionContext());
    }


    public void setReport(SessionContext ctx, Report value)
    {
        setProperty(ctx, "report", value);
    }


    public void setReport(Report value)
    {
        setReport(getSession().getSessionContext(), value);
    }


    public Boolean isSingleFile(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "singleFile");
    }


    public Boolean isSingleFile()
    {
        return isSingleFile(getSession().getSessionContext());
    }


    public boolean isSingleFileAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSingleFile(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSingleFileAsPrimitive()
    {
        return isSingleFileAsPrimitive(getSession().getSessionContext());
    }


    public void setSingleFile(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "singleFile", value);
    }


    public void setSingleFile(Boolean value)
    {
        setSingleFile(getSession().getSessionContext(), value);
    }


    public void setSingleFile(SessionContext ctx, boolean value)
    {
        setSingleFile(ctx, Boolean.valueOf(value));
    }


    public void setSingleFile(boolean value)
    {
        setSingleFile(getSession().getSessionContext(), value);
    }
}
