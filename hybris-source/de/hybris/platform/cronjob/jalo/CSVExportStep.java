package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.zip.SafeZipEntry;
import java.io.BufferedWriter;
import java.io.File;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class CSVExportStep extends GeneratedCSVExportStep
{
    private static final Logger log = Logger.getLogger(CSVExportStep.class.getName());
    private static final String CSV_EXPORT_FILE_NAME = "CSVExport";
    private String fileName;


    protected boolean canUndo(CronJob cronJob)
    {
        return true;
    }


    protected void undoStep(CronJob cronJob)
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
    }


    protected JobMedia prepareMedia(CronJob cronJob)
    {
        ComposedType jobMediaType = getSession().getTypeManager().getComposedType(JobMedia.class);
        String dateString = (new SimpleDateFormat("yyyyMMdd_hhmmss")).format(new Date());
        this.fileName = "CSVExport" + dateString;
        JobMedia target = null;
        if(cronJob instanceof MediaProcessCronJob)
        {
            target = ((MediaProcessCronJob)cronJob).getJobMedia();
            if(target == null)
            {
                target = (JobMedia)getSession().getMediaManager().createMedia(null, this.fileName, jobMediaType);
            }
            try
            {
                target.setMime("application/zip");
                target.setRealFileName(this.fileName + ".zip");
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
            ((MediaProcessCronJob)cronJob).setJobMedia(target);
            cronJob.addLog("created new result media '" + target.getCode() + "'", (Step)this);
        }
        else
        {
            cronJob.addLog("can not create result media!", (Step)this);
        }
        return target;
    }


    protected void performStep(CronJob cronJob)
    {
        JobMedia target = prepareMedia(cronJob);
        try
        {
            target.setLocked(true);
            if(cronJob instanceof FlexibleSearchCronJob)
            {
                Collection result = ((FlexibleSearchCronJob)cronJob).getSearchResult();
                if(result != null && !result.isEmpty())
                {
                    File tempFile = null;
                    try
                    {
                        tempFile = File.createTempFile("CSVExport", null);
                        if(!tempFile.canWrite())
                        {
                            cronJob.addLog("cannot write to " + tempFile, (Step)this, cronJob.getErrorLogLevel());
                            return;
                        }
                        ZipOutputStream zos = null;
                        Writer writer = null;
                        try
                        {
                            zos = new ZipOutputStream(new FileOutputStream(tempFile));
                            writer = new BufferedWriter(new OutputStreamWriter(zos, "UTF-8"));
                            zos.putNextEntry((ZipEntry)new SafeZipEntry(this.fileName));
                            for(Iterator iter = result.iterator(); iter.hasNext(); )
                            {
                                Object entry = iter.next();
                                if(entry instanceof String)
                                {
                                    String str = (String)entry;
                                    writer.write("Code: " + str + "\n");
                                }
                                else if(entry instanceof Item)
                                {
                                    Item item = (Item)entry;
                                    writer.write("Item: " + item.getPK().toString() + "\n");
                                }
                                else
                                {
                                    writer.write("Object: " + entry.toString() + "\n");
                                }
                                writer.flush();
                            }
                            zos.closeEntry();
                        }
                        finally
                        {
                            IOUtils.closeQuietly(writer);
                            IOUtils.closeQuietly(zos);
                        }
                        target.setFile(tempFile);
                        cronJob.addLog("performStep finished without exceptions [STEP:" + this + "; CronJob: " + cronJob + "]", (Step)this);
                    }
                    finally
                    {
                        FileUtils.deleteQuietly(tempFile);
                    }
                }
                else
                {
                    cronJob.addLog("result list is empty [STEP:" + this + "; CronJob: " + cronJob + "]", (Step)this);
                }
            }
            else
            {
                cronJob.addLog("can not perform step [STEP:" + this + "; CronJob: " + cronJob + "]", (Step)this);
            }
        }
        catch(JaloBusinessException e)
        {
            throw new JaloInternalException(e);
        }
        catch(IOException e)
        {
            throw new JaloInternalException(e);
        }
        finally
        {
            target.setLocked(false);
        }
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable()
    {
        return false;
    }
}
