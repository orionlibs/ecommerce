package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.impex.constants.GeneratedImpExConstants;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.cronjob.ImpExExportCronJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExExportJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportJob;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.ExportConfiguration;
import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.Exporter;
import de.hybris.platform.impex.jalo.exp.HeaderLibrary;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.Utilities;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.log4j.Logger;

public class ImpExManager extends GeneratedImpExManager
{
    private static final Logger LOG = Logger.getLogger(ImpExManager.class.getName());


    public static ImpExManager getInstance()
    {
        return (ImpExManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("impex");
    }


    public ImpExExportCronJob createDefaultExportCronJob(ExportConfiguration config)
    {
        ImpExExportCronJob cronJob = createDefaultExportCronJob();
        cronJob.setDataExportTarget(config.getDataExportTarget());
        cronJob.setMediasExportTarget(config.getMediasExportTarget());
        cronJob.setJobMedia(config.getSource());
        cronJob.setMode(config.getHeaderValidationMode());
        cronJob
                        .setCommentCharacter((config.getCommentCharacter() == null || config.getCommentCharacter()
                                        .length() == 0) ? 35 :
                                        config.getCommentCharacter().charAt(0));
        cronJob
                        .setFieldSeparator((config.getFieldSeparator() == null || config.getFieldSeparator()
                                        .length() == 0) ? CSVConstants.DEFAULT_FIELD_SEPARATOR :
                                        config.getFieldSeparator().charAt(0));
        cronJob
                        .setQuoteCharacter((config.getQuoteCharacter() == null || config.getQuoteCharacter()
                                        .length() == 0) ? CSVConstants.DEFAULT_QUOTE_CHARACTER :
                                        config.getQuoteCharacter().charAt(0));
        cronJob.setSingleFile(config.isSingleFile());
        return cronJob;
    }


    public ImpExExportCronJob createDefaultExportCronJob()
    {
        try
        {
            return createDefaultExportCronJob(null, null);
        }
        catch(ImpExException e)
        {
            LOG.error(e);
            return null;
        }
    }


    public ImpExExportCronJob createDefaultExportCronJob(HeaderLibrary headerlibrary, Collection<Item> items) throws ImpExException
    {
        return createDefaultExportCronJob(getOrCreateImpExExportJob(), headerlibrary, items);
    }


    public ImpExExportCronJob createDefaultExportCronJob(ImpExExportJob impexExportJob, HeaderLibrary headerlibrary, Collection<Item> items) throws ImpExException
    {
        JaloSession curSession = JaloSession.getCurrentSession();
        ComposedType impexCronJob = curSession.getTypeManager().getComposedType(ImpExExportCronJob.class);
        Map<String, Object> values = new HashMap<>();
        values.put("code", CronJobManager.getInstance().getNextCronjobNumber() + "-ImpEx-Export");
        values.put("sessionUser", curSession.getSessionContext().getUser());
        values.put("sessionCurrency", curSession.getSessionContext().getCurrency());
        values.put("sessionLanguage", curSession.getSessionContext().getLanguage());
        values.put("active", Boolean.TRUE);
        values.put("singleExecutable", Boolean.TRUE);
        values.put("changeRecordingEnabled", Boolean.FALSE);
        values.put("errorMode", EnumerationManager.getInstance().getEnumerationValue("ErrorMode", GeneratedCronJobConstants.Enumerations.ErrorMode.FAIL));
        try
        {
            values.put("encoding", Utilities.resolveEncoding(CSVConstants.DEFAULT_ENCODING));
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.warn("Encoding not found! Unable to set default encoding to '" + CSVConstants.DEFAULT_ENCODING + "'", e);
        }
        ImpExExportJob job = (impexExportJob == null) ? null : getOrCreateImpExExportJob();
        values.put("job", job);
        if(headerlibrary != null && items != null && !items.isEmpty())
        {
            ImpExMedia script = ExportUtils.createExportScript(headerlibrary, items);
            values.put("exportTemplate", headerlibrary);
            values.put("jobMedia", script);
        }
        ImpExExportCronJob cronjob = null;
        try
        {
            cronjob = (ImpExExportCronJob)impexCronJob.newInstance(values);
        }
        catch(JaloGenericCreationException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        cronjob.resetItemsCounter();
        if(items != null)
        {
            cronjob.setItemsMaxCount(items.size());
        }
        return cronjob;
    }


    public ImpExExportJob getOrCreateImpExExportJob()
    {
        ImpExExportJob job = (ImpExExportJob)CronJobManager.getInstance().getJob("ImpEx-Export");
        if(job == null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("code", "ImpEx-Export");
            job = createImpExExportJob(map);
        }
        return job;
    }


    public ImpExImportCronJob createDefaultImpExImportCronJob()
    {
        return createDefaultImpExImportCronJob(getOrCreateImpExImportJob());
    }


    public ImpExImportCronJob createDefaultImpExImportCronJob(ImpExImportJob impexImportJob)
    {
        JaloSession curSession = JaloSession.getCurrentSession();
        ComposedType impexCronJob = curSession.getTypeManager().getComposedType(ImpExImportCronJob.class);
        Map<String, Object> values = new HashMap<>();
        try
        {
            ImpExImportJob job;
            values.put("code", CronJobManager.getInstance().getNextCronjobNumber() + "-ImpEx-Import");
            values.put("sessionUser", curSession.getSessionContext().getUser());
            values.put("sessionCurrency", curSession.getSessionContext().getCurrency());
            values.put("sessionLanguage", curSession.getSessionContext().getLanguage());
            values.put("active", Boolean.TRUE);
            values.put("singleExecutable", Boolean.TRUE);
            values.put("changeRecordingEnabled", Boolean.FALSE);
            values.put("errorMode", EnumerationManager.getInstance().getEnumerationValue("ErrorMode", GeneratedCronJobConstants.Enumerations.ErrorMode.FAIL));
            if(impexImportJob == null)
            {
                job = getOrCreateImpExImportJob();
            }
            else
            {
                job = impexImportJob;
            }
            values.put("job", job);
            ImpExImportCronJob cronjob = (ImpExImportCronJob)impexCronJob.newInstance(values);
            return cronjob;
        }
        catch(JaloGenericCreationException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public ImpExImportJob getOrCreateImpExImportJob()
    {
        ImpExImportJob job = (ImpExImportJob)CronJobManager.getInstance().getJob("ImpEx-Import");
        if(job == null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("code", "ImpEx-Import");
            job = createImpExImportJob(map);
        }
        return job;
    }


    public ImpExExportMedia createImpExExportMedia(String code)
    {
        if(code == null || code.trim().length() < 1)
        {
            throw new IllegalArgumentException("Missing media code!");
        }
        Map<Object, Object> attr = new HashMap<>();
        attr.put("code", code);
        return createImpExExportMedia(attr);
    }


    public ImpExMedia createImpExMedia(String code)
    {
        if(code == null || code.trim().length() < 1)
        {
            throw new IllegalArgumentException("Missing media code!");
        }
        Map<Object, Object> attr = new HashMap<>();
        attr.put("code", code);
        return createImpExMedia(attr);
    }


    public ImpExMedia createImpExMediaForCodeAndExtractionId(String code, String extractionId)
    {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(extractionId, "extractionId must not be null");
        Map<String, String> attrs = new HashMap<>();
        attrs.put("code", code);
        attrs.put("extractionId", extractionId);
        return createImpExMedia(attrs);
    }


    public ImpExMedia createImpExMedia(String code, EnumerationValue encoding)
    {
        ImpExMedia ret = createImpExMedia(code);
        ret.setEncoding(encoding);
        return ret;
    }


    public ImpExMedia createImpExMedia(String code, String encoding, InputStream content) throws UnsupportedEncodingException
    {
        try
        {
            ImpExMedia media = createImpExMedia(code, Utilities.resolveEncoding(encoding));
            media.setMime("impex");
            media.setData(new DataInputStream(content), code, ImpExConstants.File.MIME_TYPE_CSV);
            return media;
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public ImpExMedia createImpExMedia(String code, String encoding, String content) throws UnsupportedEncodingException
    {
        return createImpExMedia(code, encoding, new ByteArrayInputStream(content.getBytes()));
    }


    public ImpExMedia createImpExMedia(String code, String encoding) throws UnsupportedEncodingException
    {
        ImpExMedia ret = createImpExMedia(code);
        ret.setEncoding(Utilities.resolveEncoding(encoding));
        return ret;
    }


    public Item createItem(String type, String attributes, String values) throws ImpExException
    {
        if(type == null || type.length() == 0 || attributes == null || attributes.length() == 0 || values == null || values
                        .length() == 0)
        {
            throw new ImpExException("At least one of given strings is empty");
        }
        String script = ImpExConstants.Syntax.Mode.INSERT + " " + ImpExConstants.Syntax.Mode.INSERT + ";" + type + "\n;" + attributes;
        CSVReader reader = new CSVReader(script);
        reader.setFieldSeparator(new char[] {';'});
        reader.setTextSeparator('"');
        Importer importer = new Importer(reader);
        importer.getReader().setDumpingAllowed(false);
        Item res = importer.importNext();
        if(!importer.isFinished())
        {
            importer.close();
        }
        return res;
    }


    public ImpExExportCronJob exportData(ImpExExportCronJob cronJob, boolean synchronous)
    {
        String cronJobString = "cronjob with code=" + cronJob.getCode();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Starting export " + (synchronous ? "synchronous" : "asynchronous") + " using " + cronJobString);
        }
        cronJob.getJob().perform((CronJob)cronJob, synchronous);
        if(synchronous)
        {
            if(cronJob.getSuccessResult().equals(cronJob.getResult()))
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Export was successful (using " + cronJobString + ")");
                }
            }
            else
            {
                LOG.error("Export has caused an error, see logs of " + cronJobString + " for further details");
            }
        }
        return cronJob;
    }


    public Export exportData(ExportConfiguration config, boolean synchronous)
    {
        ImpExExportCronJob cronJob = createDefaultExportCronJob(config);
        Export export = exportData(cronJob, synchronous).getExport();
        return (export == null) ? null : export;
    }


    public Export exportData(ExportConfiguration config, HeaderLibrary library, Collection<Item> items, boolean synchronous) throws ImpExException
    {
        ImpExMedia scriptmedia = ExportUtils.createExportScript(library, items);
        config.setSource(scriptmedia);
        return exportData(config, synchronous);
    }


    public Export exportDataLight(ExportConfiguration config, HeaderLibrary library, Collection<Item> items) throws ImpExException
    {
        ImpExMedia scriptmedia = ExportUtils.createExportScript(library, items);
        config.setSource(scriptmedia);
        return exportDataLight(config);
    }


    public Export exportDataLight(ExportConfiguration config) throws ImpExException
    {
        if(config.getSource() == null)
        {
            throw new ImpExException("Missing export script file! You have to use: de.hybris.platform.impex.jalo.ExportConfiguration#setSource( ImpExExportMedia dest )");
        }
        MediaDataTranslator.setMediaDataHandler((MediaDataHandler)config.getMediaDataHandler());
        try
        {
            Exporter exporter = new Exporter(config);
            return exporter.export();
        }
        finally
        {
            MediaDataTranslator.unsetMediaDataHandler();
        }
    }


    public ImpExImportCronJob importData(ImpExImportCronJob cronJob, boolean synchronous, boolean removeOnSuccess)
    {
        String cronJobString = "cronjob with code=" + cronJob.getCode();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Starting import " + (synchronous ? "synchronous" : "asynchronous") + " using " + cronJobString);
        }
        cronJob.getJob().perform((CronJob)cronJob, synchronous);
        if(synchronous)
        {
            if(cronJob.getSuccessResult().equals(cronJob.getResult()))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Import was successful (using " + cronJobString + ")");
                }
                if(removeOnSuccess)
                {
                    try
                    {
                        cronJob.remove();
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Successful removed " + cronJobString);
                        }
                        return null;
                    }
                    catch(ConsistencyCheckException e)
                    {
                        LOG.warn("Can not delete used " + cronJobString, (Throwable)e);
                    }
                }
            }
            else
            {
                LOG.error("Import has caused an error, see logs of " + cronJobString + " for further details");
            }
        }
        else if(removeOnSuccess)
        {
            LOG.warn("Will ignore removeOnSuccess flag, because of asynchronous mode");
        }
        return cronJob;
    }


    public ImpExImportCronJob importData(ImpExMedia media, Collection<ImpExMedia> externalData, boolean codeexecution, boolean synchronous, boolean removeOnSuccess)
    {
        ImpExImportCronJob cronJob = getInstance().createDefaultImpExImportCronJob();
        cronJob.setEnableCodeExecution(codeexecution);
        cronJob.setJobMedia(media);
        cronJob.setExternalDataCollection(externalData);
        cronJob = importData(cronJob, synchronous, removeOnSuccess);
        return cronJob;
    }


    public ImpExImportCronJob importData(ImpExMedia media, ImpExMedia zipMedia, Collection<ImpExMedia> externalData, boolean codeexecution, boolean synchronous, boolean removeOnSuccess)
    {
        ImpExImportCronJob cronJob = getInstance().createDefaultImpExImportCronJob();
        cronJob.setEnableCodeExecution(codeexecution);
        cronJob.setJobMedia(media);
        cronJob.setExternalDataCollection(externalData);
        cronJob.setMediasMedia((Media)zipMedia);
        cronJob = importData(cronJob, synchronous, removeOnSuccess);
        return cronJob;
    }


    public ImpExImportCronJob importData(InputStream input, String encoding, char fieldSeparator, char quoteCharacter, boolean codeexecution)
    {
        ImpExImportCronJob cronJob = null;
        try
        {
            ImpExMedia jobMedia = createImpExMedia("generated impex media - " + UUID.randomUUID(), encoding);
            jobMedia.setFieldSeparator(fieldSeparator);
            jobMedia.setQuoteCharacter(quoteCharacter);
            jobMedia.setData(new DataInputStream(input), jobMedia.getCode() + ".csv", ImpExConstants.File.MIME_TYPE_CSV);
            jobMedia.setRemoveOnSuccess(true);
            cronJob = importData(jobMedia, null, codeexecution, true, true);
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.error("Given encoding is not supported", e);
        }
        return cronJob;
    }


    public ImpExImportCronJob importData(InputStream input, String encoding, boolean codeexecution)
    {
        return importData(input, encoding, CSVConstants.DEFAULT_FIELD_SEPARATOR, CSVConstants.DEFAULT_QUOTE_CHARACTER, codeexecution);
    }


    public ImpExImportCronJob importData(InputStream dataIs, InputStream mediaIs, String encoding, char fieldSeparator, char quoteCharacter, boolean codeexecution)
    {
        ImpExImportCronJob cronJob = null;
        try
        {
            ImpExMedia jobMedia = createImpExMedia("generated impex media", encoding);
            jobMedia.setFieldSeparator(fieldSeparator);
            jobMedia.setQuoteCharacter(quoteCharacter);
            jobMedia.setData(new DataInputStream(dataIs), jobMedia.getCode() + ".csv", ImpExConstants.File.MIME_TYPE_CSV);
            ImpExMedia zipMedia = createImpExMedia("generated impex zip media");
            zipMedia.setData(new DataInputStream(mediaIs), zipMedia.getCode() + ".zip", ImpExConstants.File.MIME_TYPE_ZIP);
            cronJob = importData(jobMedia, zipMedia, null, codeexecution, true, true);
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.error("Given encoding is not supported", e);
        }
        return cronJob;
    }


    public Importer importDataLight(InputStream input, String encoding, boolean codeexecution) throws ImpExException
    {
        if(input == null)
        {
            throw new ImpExException("Given input is null", -1);
        }
        try
        {
            CSVReader reader = new CSVReader(input, encoding);
            return importDataLight(reader, codeexecution);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new ImpExException(e);
        }
    }


    public Importer importDataLight(CSVReader reader, boolean codeexecution) throws ImpExException
    {
        return importDataLight(reader, codeexecution, -1);
    }


    public Importer importDataLight(CSVReader reader, boolean codeexecution, int passes) throws ImpExException
    {
        MediaDataTranslator.setMediaDataHandler((MediaDataHandler)new DefaultMediaDataHandler());
        try
        {
            Importer imp = new Importer(reader);
            imp.getReader().enableCodeExecution(codeexecution);
            imp.setMaxPass(passes);
            imp.importAll();
            return imp;
        }
        finally
        {
            MediaDataTranslator.unsetMediaDataHandler();
        }
    }


    public Boolean isSystemType(SessionContext ctx, ComposedType item)
    {
        Boolean check = super.isSystemType(ctx, item);
        if(check == null)
        {
            for(ComposedType st = item.getSuperType(); check == null && st != null; st = st.getSuperType())
            {
                check = super.isSystemType(ctx, st);
            }
        }
        return check;
    }


    public void createEssentialData(Map params, JspContext jspc) throws ConsistencyCheckException
    {
        try
        {
            EnumerationValue value = EnumerationManager.getInstance().getEnumerationValue(
                            EnumerationManager.getInstance().getEnumerationType("ExportConverterEnum"), "de_hybris_platform_impex_jalo_exp_ExcelConverter");
            value.remove();
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        try
        {
            ComposedType type = TypeManager.getInstance().getComposedType("ScriptGenerator");
            type.remove();
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        if(MediaManager.getInstance().getMediaFolderByQualifier("impex").size() < 1)
        {
            MediaManager.getInstance().createMediaFolder("impex", "impex");
        }
    }


    public MediaFolder getImpExMediaFolder()
    {
        Collection<MediaFolder> folders = MediaManager.getInstance().getMediaFolderByQualifier("impex");
        if(folders.isEmpty())
        {
            return null;
        }
        return folders.iterator().next();
    }


    public static EnumerationValue getImportStrictMode()
    {
        return getValidationMode(GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.IMPORT_STRICT);
    }


    public static EnumerationValue getImportRelaxedMode()
    {
        return getValidationMode(GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.IMPORT_RELAXED);
    }


    public static EnumerationValue getExportOnlyMode()
    {
        return getValidationMode(GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY);
    }


    public static EnumerationValue getExportReimportRelaxedMode()
    {
        return getValidationMode(GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_RELAXED);
    }


    public static EnumerationValue getExportReimportStrictMode()
    {
        return getValidationMode(GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT);
    }


    public static EnumerationValue getValidationMode(String code)
    {
        return EnumerationManager.getInstance().getEnumerationValue(
                        EnumerationManager.getInstance().getEnumerationType(GeneratedImpExConstants.TC.IMPEXVALIDATIONMODEENUM), code);
    }
}
