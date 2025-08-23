package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.constants.GeneratedImpExConstants;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedImpExImportCronJob extends CronJob
{
    public static final String JOBMEDIA = "jobMedia";
    public static final String WORKMEDIA = "workMedia";
    public static final String LASTSUCCESSFULLINE = "lastSuccessfulLine";
    public static final String MEDIASMEDIA = "mediasMedia";
    public static final String EXTERNALDATACOLLECTION = "externalDataCollection";
    public static final String LOCALE = "locale";
    public static final String DUMPFILEENCODING = "dumpFileEncoding";
    public static final String ENABLECODEEXECUTION = "enableCodeExecution";
    public static final String ENABLEEXTERNALCODEEXECUTION = "enableExternalCodeExecution";
    public static final String ENABLEEXTERNALSYNTAXPARSING = "enableExternalSyntaxParsing";
    public static final String ENABLEHMCSAVEDVALUES = "enableHmcSavedValues";
    public static final String MEDIASTARGET = "mediasTarget";
    public static final String VALUECOUNT = "valueCount";
    public static final String UNRESOLVEDDATASTORE = "unresolvedDataStore";
    public static final String ZIPENTRY = "zipentry";
    public static final String MODE = "mode";
    public static final String DUMPINGALLOWED = "dumpingAllowed";
    public static final String UNZIPMEDIASMEDIA = "unzipMediasMedia";
    public static final String MAXTHREADS = "maxThreads";
    public static final String LEGACYMODE = "legacyMode";
    protected static final BidirectionalOneToManyHandler<GeneratedImpExImportCronJob> JOBHANDLER = new BidirectionalOneToManyHandler(GeneratedImpExConstants.TC.IMPEXIMPORTJOB, false, "job", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("jobMedia", Item.AttributeMode.INITIAL);
        tmp.put("workMedia", Item.AttributeMode.INITIAL);
        tmp.put("lastSuccessfulLine", Item.AttributeMode.INITIAL);
        tmp.put("mediasMedia", Item.AttributeMode.INITIAL);
        tmp.put("externalDataCollection", Item.AttributeMode.INITIAL);
        tmp.put("locale", Item.AttributeMode.INITIAL);
        tmp.put("dumpFileEncoding", Item.AttributeMode.INITIAL);
        tmp.put("enableCodeExecution", Item.AttributeMode.INITIAL);
        tmp.put("enableExternalCodeExecution", Item.AttributeMode.INITIAL);
        tmp.put("enableExternalSyntaxParsing", Item.AttributeMode.INITIAL);
        tmp.put("enableHmcSavedValues", Item.AttributeMode.INITIAL);
        tmp.put("mediasTarget", Item.AttributeMode.INITIAL);
        tmp.put("valueCount", Item.AttributeMode.INITIAL);
        tmp.put("unresolvedDataStore", Item.AttributeMode.INITIAL);
        tmp.put("mode", Item.AttributeMode.INITIAL);
        tmp.put("dumpingAllowed", Item.AttributeMode.INITIAL);
        tmp.put("unzipMediasMedia", Item.AttributeMode.INITIAL);
        tmp.put("maxThreads", Item.AttributeMode.INITIAL);
        tmp.put("legacyMode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public EnumerationValue getDumpFileEncoding(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "dumpFileEncoding");
    }


    public EnumerationValue getDumpFileEncoding()
    {
        return getDumpFileEncoding(getSession().getSessionContext());
    }


    public void setDumpFileEncoding(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "dumpFileEncoding", value);
    }


    public void setDumpFileEncoding(EnumerationValue value)
    {
        setDumpFileEncoding(getSession().getSessionContext(), value);
    }


    public Boolean isDumpingAllowed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "dumpingAllowed");
    }


    public Boolean isDumpingAllowed()
    {
        return isDumpingAllowed(getSession().getSessionContext());
    }


    public boolean isDumpingAllowedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDumpingAllowed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDumpingAllowedAsPrimitive()
    {
        return isDumpingAllowedAsPrimitive(getSession().getSessionContext());
    }


    public void setDumpingAllowed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "dumpingAllowed", value);
    }


    public void setDumpingAllowed(Boolean value)
    {
        setDumpingAllowed(getSession().getSessionContext(), value);
    }


    public void setDumpingAllowed(SessionContext ctx, boolean value)
    {
        setDumpingAllowed(ctx, Boolean.valueOf(value));
    }


    public void setDumpingAllowed(boolean value)
    {
        setDumpingAllowed(getSession().getSessionContext(), value);
    }


    public Boolean isEnableCodeExecution(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enableCodeExecution");
    }


    public Boolean isEnableCodeExecution()
    {
        return isEnableCodeExecution(getSession().getSessionContext());
    }


    public boolean isEnableCodeExecutionAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnableCodeExecution(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableCodeExecutionAsPrimitive()
    {
        return isEnableCodeExecutionAsPrimitive(getSession().getSessionContext());
    }


    public void setEnableCodeExecution(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enableCodeExecution", value);
    }


    public void setEnableCodeExecution(Boolean value)
    {
        setEnableCodeExecution(getSession().getSessionContext(), value);
    }


    public void setEnableCodeExecution(SessionContext ctx, boolean value)
    {
        setEnableCodeExecution(ctx, Boolean.valueOf(value));
    }


    public void setEnableCodeExecution(boolean value)
    {
        setEnableCodeExecution(getSession().getSessionContext(), value);
    }


    public Boolean isEnableExternalCodeExecution(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enableExternalCodeExecution");
    }


    public Boolean isEnableExternalCodeExecution()
    {
        return isEnableExternalCodeExecution(getSession().getSessionContext());
    }


    public boolean isEnableExternalCodeExecutionAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnableExternalCodeExecution(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableExternalCodeExecutionAsPrimitive()
    {
        return isEnableExternalCodeExecutionAsPrimitive(getSession().getSessionContext());
    }


    public void setEnableExternalCodeExecution(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enableExternalCodeExecution", value);
    }


    public void setEnableExternalCodeExecution(Boolean value)
    {
        setEnableExternalCodeExecution(getSession().getSessionContext(), value);
    }


    public void setEnableExternalCodeExecution(SessionContext ctx, boolean value)
    {
        setEnableExternalCodeExecution(ctx, Boolean.valueOf(value));
    }


    public void setEnableExternalCodeExecution(boolean value)
    {
        setEnableExternalCodeExecution(getSession().getSessionContext(), value);
    }


    public Boolean isEnableExternalSyntaxParsing(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enableExternalSyntaxParsing");
    }


    public Boolean isEnableExternalSyntaxParsing()
    {
        return isEnableExternalSyntaxParsing(getSession().getSessionContext());
    }


    public boolean isEnableExternalSyntaxParsingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnableExternalSyntaxParsing(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableExternalSyntaxParsingAsPrimitive()
    {
        return isEnableExternalSyntaxParsingAsPrimitive(getSession().getSessionContext());
    }


    public void setEnableExternalSyntaxParsing(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enableExternalSyntaxParsing", value);
    }


    public void setEnableExternalSyntaxParsing(Boolean value)
    {
        setEnableExternalSyntaxParsing(getSession().getSessionContext(), value);
    }


    public void setEnableExternalSyntaxParsing(SessionContext ctx, boolean value)
    {
        setEnableExternalSyntaxParsing(ctx, Boolean.valueOf(value));
    }


    public void setEnableExternalSyntaxParsing(boolean value)
    {
        setEnableExternalSyntaxParsing(getSession().getSessionContext(), value);
    }


    public Boolean isEnableHmcSavedValues(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enableHmcSavedValues");
    }


    public Boolean isEnableHmcSavedValues()
    {
        return isEnableHmcSavedValues(getSession().getSessionContext());
    }


    public boolean isEnableHmcSavedValuesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnableHmcSavedValues(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableHmcSavedValuesAsPrimitive()
    {
        return isEnableHmcSavedValuesAsPrimitive(getSession().getSessionContext());
    }


    public void setEnableHmcSavedValues(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enableHmcSavedValues", value);
    }


    public void setEnableHmcSavedValues(Boolean value)
    {
        setEnableHmcSavedValues(getSession().getSessionContext(), value);
    }


    public void setEnableHmcSavedValues(SessionContext ctx, boolean value)
    {
        setEnableHmcSavedValues(ctx, Boolean.valueOf(value));
    }


    public void setEnableHmcSavedValues(boolean value)
    {
        setEnableHmcSavedValues(getSession().getSessionContext(), value);
    }


    public Collection<ImpExMedia> getExternalDataCollection(SessionContext ctx)
    {
        Collection<ImpExMedia> coll = (Collection<ImpExMedia>)getProperty(ctx, "externalDataCollection");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<ImpExMedia> getExternalDataCollection()
    {
        return getExternalDataCollection(getSession().getSessionContext());
    }


    public void setExternalDataCollection(SessionContext ctx, Collection<ImpExMedia> value)
    {
        setProperty(ctx, "externalDataCollection", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setExternalDataCollection(Collection<ImpExMedia> value)
    {
        setExternalDataCollection(getSession().getSessionContext(), value);
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


    public Integer getLastSuccessfulLine(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "lastSuccessfulLine");
    }


    public Integer getLastSuccessfulLine()
    {
        return getLastSuccessfulLine(getSession().getSessionContext());
    }


    public int getLastSuccessfulLineAsPrimitive(SessionContext ctx)
    {
        Integer value = getLastSuccessfulLine(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getLastSuccessfulLineAsPrimitive()
    {
        return getLastSuccessfulLineAsPrimitive(getSession().getSessionContext());
    }


    public void setLastSuccessfulLine(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "lastSuccessfulLine", value);
    }


    public void setLastSuccessfulLine(Integer value)
    {
        setLastSuccessfulLine(getSession().getSessionContext(), value);
    }


    public void setLastSuccessfulLine(SessionContext ctx, int value)
    {
        setLastSuccessfulLine(ctx, Integer.valueOf(value));
    }


    public void setLastSuccessfulLine(int value)
    {
        setLastSuccessfulLine(getSession().getSessionContext(), value);
    }


    public Boolean isLegacyMode(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "legacyMode");
    }


    public Boolean isLegacyMode()
    {
        return isLegacyMode(getSession().getSessionContext());
    }


    public boolean isLegacyModeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLegacyMode(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLegacyModeAsPrimitive()
    {
        return isLegacyModeAsPrimitive(getSession().getSessionContext());
    }


    public void setLegacyMode(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "legacyMode", value);
    }


    public void setLegacyMode(Boolean value)
    {
        setLegacyMode(getSession().getSessionContext(), value);
    }


    public void setLegacyMode(SessionContext ctx, boolean value)
    {
        setLegacyMode(ctx, Boolean.valueOf(value));
    }


    public void setLegacyMode(boolean value)
    {
        setLegacyMode(getSession().getSessionContext(), value);
    }


    public String getLocale(SessionContext ctx)
    {
        return (String)getProperty(ctx, "locale");
    }


    public String getLocale()
    {
        return getLocale(getSession().getSessionContext());
    }


    public void setLocale(SessionContext ctx, String value)
    {
        setProperty(ctx, "locale", value);
    }


    public void setLocale(String value)
    {
        setLocale(getSession().getSessionContext(), value);
    }


    public Integer getMaxThreads(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxThreads");
    }


    public Integer getMaxThreads()
    {
        return getMaxThreads(getSession().getSessionContext());
    }


    public int getMaxThreadsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxThreads(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxThreadsAsPrimitive()
    {
        return getMaxThreadsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxThreads(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxThreads", value);
    }


    public void setMaxThreads(Integer value)
    {
        setMaxThreads(getSession().getSessionContext(), value);
    }


    public void setMaxThreads(SessionContext ctx, int value)
    {
        setMaxThreads(ctx, Integer.valueOf(value));
    }


    public void setMaxThreads(int value)
    {
        setMaxThreads(getSession().getSessionContext(), value);
    }


    public Media getMediasMedia(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "mediasMedia");
    }


    public Media getMediasMedia()
    {
        return getMediasMedia(getSession().getSessionContext());
    }


    public void setMediasMedia(SessionContext ctx, Media value)
    {
        setProperty(ctx, "mediasMedia", value);
    }


    public void setMediasMedia(Media value)
    {
        setMediasMedia(getSession().getSessionContext(), value);
    }


    public String getMediasTarget(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mediasTarget");
    }


    public String getMediasTarget()
    {
        return getMediasTarget(getSession().getSessionContext());
    }


    public void setMediasTarget(SessionContext ctx, String value)
    {
        setProperty(ctx, "mediasTarget", value);
    }


    public void setMediasTarget(String value)
    {
        setMediasTarget(getSession().getSessionContext(), value);
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


    public ImpExMedia getUnresolvedDataStore(SessionContext ctx)
    {
        return (ImpExMedia)getProperty(ctx, "unresolvedDataStore");
    }


    public ImpExMedia getUnresolvedDataStore()
    {
        return getUnresolvedDataStore(getSession().getSessionContext());
    }


    public void setUnresolvedDataStore(SessionContext ctx, ImpExMedia value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setUnresolvedDataStore(ImpExMedia value)
    {
        setUnresolvedDataStore(getSession().getSessionContext(), value);
    }


    public Boolean isUnzipMediasMedia(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "unzipMediasMedia");
    }


    public Boolean isUnzipMediasMedia()
    {
        return isUnzipMediasMedia(getSession().getSessionContext());
    }


    public boolean isUnzipMediasMediaAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUnzipMediasMedia(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUnzipMediasMediaAsPrimitive()
    {
        return isUnzipMediasMediaAsPrimitive(getSession().getSessionContext());
    }


    public void setUnzipMediasMedia(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "unzipMediasMedia", value);
    }


    public void setUnzipMediasMedia(Boolean value)
    {
        setUnzipMediasMedia(getSession().getSessionContext(), value);
    }


    public void setUnzipMediasMedia(SessionContext ctx, boolean value)
    {
        setUnzipMediasMedia(ctx, Boolean.valueOf(value));
    }


    public void setUnzipMediasMedia(boolean value)
    {
        setUnzipMediasMedia(getSession().getSessionContext(), value);
    }


    public Integer getValueCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "valueCount");
    }


    public Integer getValueCount()
    {
        return getValueCount(getSession().getSessionContext());
    }


    public int getValueCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getValueCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getValueCountAsPrimitive()
    {
        return getValueCountAsPrimitive(getSession().getSessionContext());
    }


    public void setValueCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "valueCount", value);
    }


    public void setValueCount(Integer value)
    {
        setValueCount(getSession().getSessionContext(), value);
    }


    public void setValueCount(SessionContext ctx, int value)
    {
        setValueCount(ctx, Integer.valueOf(value));
    }


    public void setValueCount(int value)
    {
        setValueCount(getSession().getSessionContext(), value);
    }


    public ImpExMedia getWorkMedia(SessionContext ctx)
    {
        return (ImpExMedia)getProperty(ctx, "workMedia");
    }


    public ImpExMedia getWorkMedia()
    {
        return getWorkMedia(getSession().getSessionContext());
    }


    public void setWorkMedia(SessionContext ctx, ImpExMedia value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setWorkMedia(ImpExMedia value)
    {
        setWorkMedia(getSession().getSessionContext(), value);
    }


    public String getZipentry()
    {
        return getZipentry(getSession().getSessionContext());
    }


    public void setZipentry(String value)
    {
        setZipentry(getSession().getSessionContext(), value);
    }


    public abstract String getZipentry(SessionContext paramSessionContext);


    public abstract void setZipentry(SessionContext paramSessionContext, String paramString);
}
