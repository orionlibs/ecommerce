package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.impex.jalo.ErrorHandler;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.MultiThreadedImporter;
import de.hybris.platform.impex.jalo.imp.DumpHandler;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ImportProcessor;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImportProcessor;
import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.ThreadUtilities;
import de.hybris.platform.util.Utilities;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ImpExImportJob extends GeneratedImpExImportJob
{
    private static final Logger LOG = Logger.getLogger(ImpExImportJob.class.getName());


    protected boolean canUndo(CronJob cronJob)
    {
        return false;
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable(CronJob cronJob)
    {
        return true;
    }


    @ForceJALO(reason = "something else")
    public boolean isPerformable(CronJob cronJob)
    {
        boolean isPerformable = false;
        if(super.isPerformable(cronJob))
        {
            isPerformable = cronJob instanceof ImpExImportCronJob;
        }
        return isPerformable;
    }


    public CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        boolean success = false;
        ImpExImportCronJob importCronJob = null;
        if(!(cronJob instanceof ImpExImportCronJob))
        {
            throw new AbortCronJobException("Given cronjob is not instance of ImpExImportCronJob");
        }
        importCronJob = (ImpExImportCronJob)cronJob;
        Language sessionLanguage = null;
        try
        {
            sessionLanguage = getSession().getSessionContext().getLanguage();
            MediaDataTranslator.setMediaDataHandler((MediaDataHandler)new DefaultCronJobMediaDataHandler(importCronJob));
            success = performJob(importCronJob);
        }
        catch(AbortCronJobException e)
        {
            throw e;
        }
        catch(RuntimeException e)
        {
            LOG.error("Error while performing import: " + e.getMessage(), e);
            success = false;
        }
        catch(Exception e)
        {
            LOG.error("Error while performing import: " + e.getMessage());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while performing import: " + e.getMessage(), e);
            }
            success = false;
        }
        finally
        {
            try
            {
                getSession().getSessionContext().setLanguage(sessionLanguage);
            }
            catch(Exception e)
            {
                LOG.error("Exception while restoring previous session language: " + e.getMessage());
            }
            MediaDataTranslator.unsetMediaDataHandler();
        }
        if(success)
        {
            cleanup(importCronJob);
        }
        return importCronJob.getFinishedResult(success);
    }


    protected void cleanup(ImpExImportCronJob importCronJob)
    {
        ImpExMedia jobMedia = importCronJob.getJobMedia();
        if(jobMedia != null && jobMedia.isRemoveOnSuccessAsPrimitive())
        {
            try
            {
                importCronJob.getJobMedia().remove();
            }
            catch(ConsistencyCheckException e)
            {
                LOG.warn("Couldn't remove media " + importCronJob.getJobMedia().getCode() + " (" + importCronJob
                                .getJobMedia().getPK() + ")");
            }
            finally
            {
                importCronJob.setJobMedia(null);
            }
        }
        Collection<ImpExMedia> coll = importCronJob.getExternalDataCollection();
        Collection<ImpExMedia> newColl = new ArrayList<>(coll);
        if(coll != null && !coll.isEmpty())
        {
            for(ImpExMedia media : coll)
            {
                if(media != null && media.isRemoveOnSuccessAsPrimitive())
                {
                    try
                    {
                        media.remove();
                        newColl.remove(media);
                    }
                    catch(ConsistencyCheckException e)
                    {
                        LOG.warn("Couldn't remove media " + media.getCode() + " (" + media.getPK() + ")");
                    }
                }
            }
            importCronJob.setExternalDataCollection(newColl.isEmpty() ? null : newColl);
        }
    }


    protected boolean performJob(ImpExImportCronJob cronJob) throws AbortCronJobException
    {
        boolean success = false;
        try
        {
            CSVReader csvReader = createCSVReader(cronJob);
            csvReader = adjustCSVReader(csvReader, cronJob);
            ImportProcessor processor = createImportProcessor(cronJob);
            processor = adjustImportProcessor(processor, cronJob);
            ImpExImportReader importReader = createImportReader(cronJob, csvReader, processor);
            importReader = adjustImportReader(importReader, cronJob);
            Importer importer = createImporter(cronJob, importReader);
            importer = adjustImporter(importer, cronJob);
            if(LOG.isInfoEnabled())
            {
                if(importReader instanceof MultiThreadedImpExImportReader)
                {
                    LOG.info("Starting multi-threaded ImpEx cronjob \"" + getCode() + "\" (" + ((MultiThreadedImpExImportReader)importReader)
                                    .getMaxThreads() + " threads)");
                }
                else
                {
                    LOG.info("Starting ImpEx cronjob \"" + getCode() + "\"");
                }
            }
            success = doImport(cronJob, importer);
        }
        catch(IllegalStateException e)
        {
            LOG.error("Couldn't configure importer instance");
            success = false;
        }
        return success;
    }


    protected long getStatsInverval()
    {
        return 1000L * Config.getLong("impex.stats.interval", 60L);
    }


    protected boolean doImport(ImpExImportCronJob cronJob, Importer importer) throws AbortCronJobException
    {
        // Byte code:
        //   0: iconst_0
        //   1: istore_3
        //   2: aconst_null
        //   3: astore #4
        //   5: invokestatic currentTimeMillis : ()J
        //   8: lstore #5
        //   10: aload_0
        //   11: invokevirtual getStatsInverval : ()J
        //   14: lstore #7
        //   16: aload_1
        //   17: invokevirtual isRequestAbortAsPrimitive : ()Z
        //   20: ifeq -> 31
        //   23: aload_0
        //   24: aload_1
        //   25: invokevirtual abort : (Lde/hybris/platform/cronjob/jalo/CronJob;)V
        //   28: goto -> 76
        //   31: aload_2
        //   32: invokevirtual importNext : ()Lde/hybris/platform/jalo/Item;
        //   35: astore #4
        //   37: lload #7
        //   39: lconst_0
        //   40: lcmp
        //   41: ifle -> 76
        //   44: aload_2
        //   45: invokevirtual isRunning : ()Z
        //   48: ifeq -> 76
        //   51: invokestatic currentTimeMillis : ()J
        //   54: dup2
        //   55: lstore #9
        //   57: lload #5
        //   59: lsub
        //   60: lload #7
        //   62: lcmp
        //   63: ifle -> 76
        //   66: aload_0
        //   67: aload_1
        //   68: aload_2
        //   69: invokevirtual saveStats : (Lde/hybris/platform/impex/jalo/cronjob/ImpExImportCronJob;Lde/hybris/platform/impex/jalo/Importer;)V
        //   72: lload #9
        //   74: lstore #5
        //   76: aload #4
        //   78: ifnonnull -> 16
        //   81: iconst_1
        //   82: istore_3
        //   83: iload_3
        //   84: ifne -> 91
        //   87: aload_2
        //   88: invokevirtual abortImport : ()V
        //   91: aload_0
        //   92: aload_1
        //   93: aload_2
        //   94: invokevirtual saveStats : (Lde/hybris/platform/impex/jalo/cronjob/ImpExImportCronJob;Lde/hybris/platform/impex/jalo/Importer;)V
        //   97: goto -> 212
        //   100: astore #4
        //   102: aload #4
        //   104: athrow
        //   105: astore #4
        //   107: aload #4
        //   109: invokevirtual getErrorCode : ()I
        //   112: bipush #123
        //   114: if_icmpne -> 131
        //   117: getstatic de/hybris/platform/impex/jalo/cronjob/ImpExImportJob.LOG : Lorg/apache/log4j/Logger;
        //   120: aload #4
        //   122: invokevirtual getMessage : ()Ljava/lang/String;
        //   125: invokevirtual error : (Ljava/lang/Object;)V
        //   128: goto -> 144
        //   131: getstatic de/hybris/platform/impex/jalo/cronjob/ImpExImportJob.LOG : Lorg/apache/log4j/Logger;
        //   134: aload #4
        //   136: invokevirtual getMessage : ()Ljava/lang/String;
        //   139: aload #4
        //   141: invokevirtual error : (Ljava/lang/Object;Ljava/lang/Throwable;)V
        //   144: iload_3
        //   145: ifne -> 152
        //   148: aload_2
        //   149: invokevirtual abortImport : ()V
        //   152: aload_0
        //   153: aload_1
        //   154: aload_2
        //   155: invokevirtual saveStats : (Lde/hybris/platform/impex/jalo/cronjob/ImpExImportCronJob;Lde/hybris/platform/impex/jalo/Importer;)V
        //   158: goto -> 212
        //   161: astore #4
        //   163: getstatic de/hybris/platform/impex/jalo/cronjob/ImpExImportJob.LOG : Lorg/apache/log4j/Logger;
        //   166: aload #4
        //   168: invokevirtual getMessage : ()Ljava/lang/String;
        //   171: aload #4
        //   173: invokevirtual error : (Ljava/lang/Object;Ljava/lang/Throwable;)V
        //   176: iload_3
        //   177: ifne -> 184
        //   180: aload_2
        //   181: invokevirtual abortImport : ()V
        //   184: aload_0
        //   185: aload_1
        //   186: aload_2
        //   187: invokevirtual saveStats : (Lde/hybris/platform/impex/jalo/cronjob/ImpExImportCronJob;Lde/hybris/platform/impex/jalo/Importer;)V
        //   190: goto -> 212
        //   193: astore #11
        //   195: iload_3
        //   196: ifne -> 203
        //   199: aload_2
        //   200: invokevirtual abortImport : ()V
        //   203: aload_0
        //   204: aload_1
        //   205: aload_2
        //   206: invokevirtual saveStats : (Lde/hybris/platform/impex/jalo/cronjob/ImpExImportCronJob;Lde/hybris/platform/impex/jalo/Importer;)V
        //   209: aload #11
        //   211: athrow
        //   212: iload_3
        //   213: ifeq -> 234
        //   216: aload_2
        //   217: invokevirtual hadError : ()Z
        //   220: ifne -> 234
        //   223: aload_2
        //   224: invokevirtual hasUnresolvedLines : ()Z
        //   227: ifne -> 234
        //   230: iconst_1
        //   231: goto -> 235
        //   234: iconst_0
        //   235: ireturn
        // Line number table:
        //   Java source line number -> byte code offset
        //   #267	-> 0
        //   #270	-> 2
        //   #271	-> 5
        //   #272	-> 10
        //   #275	-> 16
        //   #277	-> 23
        //   #281	-> 31
        //   #283	-> 37
        //   #285	-> 66
        //   #286	-> 72
        //   #290	-> 76
        //   #291	-> 81
        //   #317	-> 83
        //   #319	-> 87
        //   #321	-> 91
        //   #322	-> 97
        //   #294	-> 100
        //   #296	-> 102
        //   #299	-> 105
        //   #301	-> 107
        //   #303	-> 117
        //   #307	-> 131
        //   #317	-> 144
        //   #319	-> 148
        //   #321	-> 152
        //   #322	-> 158
        //   #311	-> 161
        //   #313	-> 163
        //   #317	-> 176
        //   #319	-> 180
        //   #321	-> 184
        //   #322	-> 190
        //   #317	-> 193
        //   #319	-> 199
        //   #321	-> 203
        //   #322	-> 209
        //   #323	-> 212
        // Local variable table:
        //   start	length	slot	name	descriptor
        //   57	19	9	now	J
        //   5	78	4	item	Lde/hybris/platform/jalo/Item;
        //   10	73	5	statsTS	J
        //   16	67	7	interval	J
        //   102	3	4	e	Lde/hybris/platform/cronjob/jalo/AbortCronJobException;
        //   107	37	4	e	Lde/hybris/platform/impex/jalo/ImpExException;
        //   163	13	4	e	Ljava/lang/RuntimeException;
        //   0	236	0	this	Lde/hybris/platform/impex/jalo/cronjob/ImpExImportJob;
        //   0	236	1	cronJob	Lde/hybris/platform/impex/jalo/cronjob/ImpExImportCronJob;
        //   0	236	2	importer	Lde/hybris/platform/impex/jalo/Importer;
        //   2	234	3	allProcessed	Z
        // Exception table:
        //   from	to	target	type
        //   2	83	100	de/hybris/platform/cronjob/jalo/AbortCronJobException
        //   2	83	105	de/hybris/platform/impex/jalo/ImpExException
        //   2	83	161	java/lang/RuntimeException
        //   2	83	193	finally
        //   100	144	193	finally
        //   161	176	193	finally
        //   193	195	193	finally
    }


    protected CSVReader createCSVReader(ImpExImportCronJob cronJob)
    {
        InputStream dataFromStreamSure = null;
        try
        {
            CSVReader ret = null;
            ImpExMedia media = prepareMedia(cronJob);
            dataFromStreamSure = media.getDataFromStreamSure();
            ret = new CSVReader(dataFromStreamSure, Utilities.resolveEncoding(media.getEncoding()));
            ret.setTextSeparator(media.getQuoteCharacterAsPrimitive());
            ret.setFieldSeparator(new char[] {media
                            .getFieldSeparatorAsPrimitive()});
            ret.setCommentOut(new char[] {media
                            .getCommentCharacterAsPrimitive()});
            return ret;
        }
        catch(JaloInvalidParameterException | java.io.UnsupportedEncodingException | de.hybris.platform.jalo.JaloBusinessException e)
        {
            IOUtils.closeQuietly(dataFromStreamSure);
            throw new JaloSystemException(e);
        }
    }


    protected ImportProcessor createImportProcessor(ImpExImportCronJob cronJob)
    {
        int threads = getMaxThreads4Run(cronJob);
        return (threads > 1) ? (ImportProcessor)new MyMultiThreadedImportProcessor(cronJob.getCode()) : (ImportProcessor)new MyImportProcessor(cronJob.getCode());
    }


    protected ImpExImportReader createImportReader(ImpExImportCronJob cronJob, CSVReader csvReader, ImportProcessor processor)
    {
        String mediaPrefix = cronJob.getCode() + "-";
        int threads = getMaxThreads4Run(cronJob);
        boolean legacyMode = LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.IMPEX, cronJob
                        .isLegacyMode());
        if(processor == null)
        {
            return (threads > 1) ? (ImpExImportReader)new MyMultiThreadedImpExImportReader(csvReader, legacyMode, mediaPrefix) :
                            (ImpExImportReader)new MyImpExImportReader(csvReader, legacyMode, mediaPrefix);
        }
        return (threads > 1) ? (ImpExImportReader)new MyMultiThreadedImpExImportReader(csvReader, null, (MultiThreadedImportProcessor)processor, legacyMode, mediaPrefix) :
                        (ImpExImportReader)new MyImpExImportReader(csvReader, null, processor, legacyMode, mediaPrefix);
    }


    protected Importer createImporter(ImpExImportCronJob cronJob, ImpExImportReader reader)
    {
        int threads = getMaxThreads4Run(cronJob);
        return (threads > 1) ? (Importer)new MultiThreadedImporter((MultiThreadedImpExImportReader)reader, threads) : new Importer(reader);
    }


    protected CSVReader adjustCSVReader(CSVReader reader, ImpExImportCronJob cronJob)
    {
        return reader;
    }


    protected ImportProcessor adjustImportProcessor(ImportProcessor processor, ImpExImportCronJob cronJob)
    {
        return processor;
    }


    protected ImpExImportReader adjustImportReader(ImpExImportReader reader, ImpExImportCronJob cronJob)
    {
        reader.enableCodeExecution(cronJob.isEnableCodeExecutionAsPrimitive());
        if(cronJob.isEnableExternalSyntaxParsingAsPrimitive())
        {
            reader.enableExternalSyntaxParsing(true);
            reader.enableExternalCodeExecution(cronJob.isEnableExternalCodeExecutionAsPrimitive());
        }
        else
        {
            reader.enableExternalSyntaxParsing(false);
            reader.enableExternalCodeExecution(false);
        }
        reader.addExternalDataMedias(cronJob.getExternalDataCollection());
        reader.setLocale(prepareLocale(cronJob));
        reader.setCreateHMCSavedValues(cronJob.isEnableHmcSavedValuesAsPrimitive());
        EnumerationValue mode = cronJob.getMode();
        if(mode != null)
        {
            reader.setValidationMode(mode);
        }
        reader.setDumpingAllowed(cronJob.isDumpingAllowedAsPrimitive());
        return reader;
    }


    protected Importer adjustImporter(Importer importer, ImpExImportCronJob cronJob)
    {
        CronJobDumpHandler dumpHandler = new CronJobDumpHandler(cronJob);
        importer.setDumpHandler((DumpHandler)dumpHandler);
        importer.setErrorHandler((ErrorHandler)new CronJobErrorHandler((CronJob)cronJob));
        importer.getReader().setValueLinesToSkip(cronJob.getValueCountAsPrimitive());
        return importer;
    }


    protected Locale prepareLocale(ImpExImportCronJob cronJob)
    {
        Locale usedLocale = null;
        Language language = cronJob.getSessionLanguage();
        if(language != null)
        {
            usedLocale = language.getLocale();
        }
        if(usedLocale == null)
        {
            usedLocale = getSession().getSessionContext().getLocale();
        }
        String localeString = cronJob.getLocale();
        if(localeString == null)
        {
            LOG.warn("Locale is null, using " + usedLocale.toString());
        }
        else
        {
            String[] localeParts = localeString.split("_");
            switch(localeParts.length)
            {
                case 1:
                    usedLocale = new Locale(localeParts[0]);
                    return usedLocale;
                case 2:
                    usedLocale = new Locale(localeParts[0], localeParts[1]);
                    return usedLocale;
                case 3:
                    usedLocale = new Locale(localeParts[0], localeParts[1], localeParts[2]);
                    return usedLocale;
            }
            LOG.warn("Locale is empty, using " + usedLocale.toString());
        }
        return usedLocale;
    }


    protected ImpExMedia prepareMedia(ImpExImportCronJob cronJob) throws ImpExException
    {
        ImpExMedia workMedia = cronJob.getWorkMedia();
        if(workMedia == null)
        {
            cronJob.extractZip();
            ImpExMedia jobMedia = cronJob.getJobMedia();
            if(jobMedia == null)
            {
                throw new ImpExException("No job media set at import cronjob \"" + cronJob.getCode() + "\"");
            }
            workMedia = cronJob.createWorkMedia();
            cronJob.setWorkMedia(workMedia);
            workMedia.setData((Media)jobMedia);
            jobMedia.copySettings(workMedia);
        }
        return workMedia;
    }


    protected void saveStats(ImpExImportCronJob cronJob, Importer importer)
    {
        JaloPropertyContainer cont = getSession().createPropertyContainer();
        cont.setProperty("lastSuccessfulLine", Integer.valueOf(importer.getLastImportedItemLineNumber()));
        cont.setProperty("valueCount", Integer.valueOf(importer.getResolvedItemsCountOverall()));
        try
        {
            cronJob.setAllProperties(cont);
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected void abort(CronJob cronJob) throws AbortCronJobException
    {
        cronJob.setRequestAbort(null);
        throw new AbortCronJobException("Abort requested by user");
    }


    protected int getMaxThreads4Run(ImpExImportCronJob cronjob)
    {
        int ret = cronjob.getMaxThreadsAsPrimitive();
        if(ret < 1)
        {
            ret = getMaxThreadsAsPrimitive();
        }
        ret = Math.max(ret, 1);
        if(ret > 1 && Transaction.current().isRunning())
        {
            LOG.warn("ImpEx import \"" + cronjob
                            .getCode() + "\" cannot use multi-threaded (" + ret + ") mode inside transaction - using single mode. Please check if it is intended to import within transaction. If so, configure single threaded mode.");
            ret = 1;
        }
        return ret;
    }


    @ForceJALO(reason = "something else")
    public void setMaxThreads(SessionContext ctx, Integer integer)
    {
        Integer toSet = (integer != null) ? ((integer.intValue() > 0) ? integer : Integer.valueOf(1)) : Integer.valueOf(getDefaultMaxThreads(getTenant()));
        super.setMaxThreads(ctx, toSet);
    }


    @ForceJALO(reason = "something else")
    public Integer getMaxThreads(SessionContext ctx)
    {
        Integer integer = super.getMaxThreads(ctx);
        return (integer != null && integer.intValue() > 0) ? integer : Integer.valueOf(getDefaultMaxThreads(getTenant()));
    }


    public static int getDefaultMaxThreads(Tenant tenant)
    {
        String dbName = tenant.getDataSource().getDatabaseName().toLowerCase(LocaleHelper.getPersistenceLocale());
        String expression = Config.getParameter("impex.import.workers." + dbName);
        if(StringUtils.isBlank(expression))
        {
            expression = Config.getParameter("impex.import.workers");
        }
        return ThreadUtilities.getNumberOfThreadsFromExpression(expression, 2);
    }
}
