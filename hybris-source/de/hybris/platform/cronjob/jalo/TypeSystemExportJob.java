package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.JaloTools;
import de.hybris.platform.util.zip.SafeZipEntry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.XMLOutputter;

@Deprecated(since = "4.3", forRemoval = false)
public class TypeSystemExportJob extends GeneratedTypeSystemExportJob
{
    private static final Logger LOG = Logger.getLogger(TypeSystemExportJob.class.getName());


    protected boolean canUndo(CronJob cronJob)
    {
        return true;
    }


    public CronJob.CronJobResult undoCronJob(CronJob cronJob)
    {
        JobMedia result = ((MediaProcessCronJob)cronJob).getJobMedia();
        if(result != null)
        {
            try
            {
                result.remove();
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return cronJob.getUndoFinishedResult(true);
    }


    protected JobMedia prepareMedia(CronJob cronJob)
    {
        ComposedType jobMediaType = getSession().getTypeManager().getComposedType(JobMedia.class);
        String baseName = (new SimpleDateFormat("yyyyMMdd_hhmmss")).format(new Date());
        JobMedia target = ((MediaProcessCronJob)cronJob).getJobMedia();
        if(target == null)
        {
            target = (JobMedia)getSession().getMediaManager().createMedia(null, "TypeSystemDump_" + baseName, jobMediaType);
        }
        try
        {
            target.setMime("application/zip");
            target.setRealFileName(baseName + ".zip");
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
        ((MediaProcessCronJob)cronJob).setJobMedia(target);
        cronJob.addLog("created new result media '" + target.getCode() + "'");
        return target;
    }


    protected boolean canPerform(CronJob cronJob)
    {
        try
        {
            JobMedia media = prepareMedia(cronJob);
            if(cronJob.isRunningRestart())
            {
                return (media != null);
            }
            return (media != null && !media.isLockedAsPrimitive());
        }
        catch(JaloInvalidParameterException e)
        {
            cronJob.addLog(e.getMessage());
            return false;
        }
    }


    public CronJob.CronJobResult performCronJob(CronJob forCronJob)
    {
        try
        {
            MediaProcessCronJob cronJob = (MediaProcessCronJob)forCronJob;
            JobMedia target = cronJob.getJobMedia();
            File tempFile = File.createTempFile("typeSystemExport", null);
            if(!tempFile.canWrite())
            {
                cronJob.addLog("cannot write to " + tempFile, cronJob.getErrorLogLevel());
                return cronJob.getAbortResult();
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile));
            Writer bufferedwriter = new BufferedWriter(new OutputStreamWriter(zos, "UTF-8"));
            XMLOutputter xout = new XMLOutputter(bufferedwriter, "UTF-8");
            JaloSession jaloSession = getSession();
            Collection languages = jaloSession.getC2LManager().getAllLanguages();
            doExport(cronJob, jaloSession, zos, xout, null, languages);
            xout.reset();
            for(Iterator<Extension> it = jaloSession.getExtensionManager().getExtensions().iterator(); it.hasNext(); )
            {
                String extName = ((Extension)it.next()).getName();
                xout.reset(bufferedwriter, "UTF-8");
                doExport(cronJob, jaloSession, zos, xout, extName, languages);
                xout.reset();
            }
            bufferedwriter.close();
            zos.close();
            target.setFile(tempFile);
            tempFile.delete();
            return cronJob.getFinishedResult(true);
        }
        catch(FileNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected void doExport(MediaProcessCronJob cronJob, JaloSession jaloSession, ZipOutputStream zos, XMLOutputter xout, String extensionName, Collection languages)
    {
        try
        {
            String packageName = extensionName.replace('.', '_');
            if(LOG.isInfoEnabled())
            {
                LOG.info("exporting package '" + packageName + "'");
            }
            zos.putNextEntry((ZipEntry)new SafeZipEntry(packageName + "/items.xml"));
            JaloTools.exportSystem(jaloSession, xout, extensionName);
            xout.getWriter().flush();
            zos.closeEntry();
            Language oldOne = jaloSession.getSessionContext().getLanguage();
            for(Iterator<Language> it = languages.iterator(); it.hasNext(); )
            {
                Language lang = it.next();
                jaloSession.getSessionContext().setLanguage(lang);
                zos.putNextEntry((ZipEntry)new SafeZipEntry(packageName + "/localization/locales_" + packageName + ".properties"));
                JaloTools.exportTypeLocalizations(jaloSession, xout.getWriter(), extensionName);
                xout.getWriter().flush();
                zos.closeEntry();
            }
            jaloSession.getSessionContext().setLanguage(oldOne);
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
    }
}
