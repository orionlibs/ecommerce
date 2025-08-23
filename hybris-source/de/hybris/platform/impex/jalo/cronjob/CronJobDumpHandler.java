package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.imp.AbstractDumpHandler;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Utilities;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class CronJobDumpHandler extends AbstractDumpHandler
{
    private static final Logger LOG = Logger.getLogger(CronJobDumpHandler.class.getName());
    private final ImpExImportCronJob cronjob;
    private File tempDumpFile;


    public CronJobDumpHandler(ImpExImportCronJob cronjob)
    {
        this.cronjob = cronjob;
    }


    public void init() throws IOException
    {
        setWriterOfCurrentDump(createWriter());
    }


    public void finish(boolean saveCurrentDump)
    {
        if(saveCurrentDump)
        {
            saveAndDeleteTempDump();
        }
        else
        {
            deleteTempDump();
        }
    }


    public void switchDump() throws ImpExException
    {
        saveAndDeleteTempDump();
        try
        {
            switchCurrentToLastDump();
        }
        catch(JaloBusinessException e)
        {
            throw new ImpExException(e);
        }
        try
        {
            setReaderOfLastDump(createReader());
            setWriterOfCurrentDump(createWriter());
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
    }


    public ImpExMedia getDumpAsMedia()
    {
        return this.cronjob.getUnresolvedDataStore();
    }


    protected CSVWriter createWriter() throws IOException
    {
        File tempFile = File.createTempFile(this.cronjob.getPK().toString() + "-", ".impex");
        try
        {
            setTempDump(tempFile);
            CSVWriter writer = new CSVWriter(new FileOutputStream(tempFile), Utilities.resolveEncoding(this.cronjob.getDumpFileEncoding()));
            ImpExMedia jobMedia = this.cronjob.getJobMedia();
            writer.setCommentchar(jobMedia.getCommentCharacterAsPrimitive());
            writer.setFieldseparator(jobMedia.getFieldSeparatorAsPrimitive());
            writer.setTextseparator(jobMedia.getQuoteCharacterAsPrimitive());
            return writer;
        }
        catch(IOException e)
        {
            deleteTempDump();
            throw e;
        }
    }


    protected CSVReader createReader() throws IOException
    {
        DataInputStream dataFromStreamSure = null;
        try
        {
            ImpExMedia media = this.cronjob.getWorkMedia();
            String encoding = Utilities.resolveEncoding(this.cronjob.getDumpFileEncoding());
            dataFromStreamSure = media.getDataFromStreamSure();
            CSVReader reader = new CSVReader(dataFromStreamSure, encoding);
            reader.setFieldSeparator(new char[] {media
                            .getFieldSeparatorAsPrimitive()});
            reader.setTextSeparator(media.getQuoteCharacterAsPrimitive());
            reader.setCommentOut(new char[] {media
                            .getCommentCharacterAsPrimitive()});
            return reader;
        }
        catch(UnsupportedEncodingException e)
        {
            if(dataFromStreamSure != null)
            {
                try
                {
                    dataFromStreamSure.close();
                }
                catch(IOException ex)
                {
                    throw new JaloSystemException(ex);
                }
            }
            throw e;
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected void switchCurrentToLastDump() throws JaloBusinessException
    {
        ImpExMedia workMedia = this.cronjob.getWorkMedia();
        if(workMedia == null)
        {
            workMedia = this.cronjob.createWorkMedia();
            this.cronjob.setWorkMedia(workMedia);
        }
        ImpExMedia unresolved = this.cronjob.getUnresolvedDataStore();
        workMedia.setData((Media)unresolved);
        unresolved.copySettings(workMedia);
        this.cronjob.setUnresolvedDataStore(null);
    }


    protected void deleteTempDump()
    {
        try
        {
            File dumpFile = getTempDump();
            if(!FileUtils.deleteQuietly(dumpFile))
            {
                LOG.warn("Can not delete temp file: " + dumpFile.getAbsolutePath());
            }
        }
        finally
        {
            this.tempDumpFile = null;
        }
    }


    protected File getTempDump()
    {
        return this.tempDumpFile;
    }


    protected void setTempDump(File tempFile)
    {
        this.tempDumpFile = tempFile;
    }


    protected void saveAndDeleteTempDump()
    {
        DataInputStream inputStream = null;
        try
        {
            ImpExMedia impexMedia = this.cronjob.getUnresolvedDataStore();
            if(impexMedia == null)
            {
                impexMedia = this.cronjob.createUnresolvedDataStore();
                this.cronjob.getJobMedia().copySettings(impexMedia);
                impexMedia.setEncoding(this.cronjob.getDumpFileEncoding());
                this.cronjob.setUnresolvedDataStore(impexMedia);
            }
            if(getWriterOfCurrentDump() != null)
            {
                CSVWriter csvWriter = getWriterOfCurrentDump();
                IOUtils.closeQuietly((Closeable)csvWriter);
                setWriterOfCurrentDump(null);
            }
            File dumpFile = getTempDump();
            inputStream = new DataInputStream(new FileInputStream(dumpFile));
            impexMedia.setData(inputStream, impexMedia.getRealFileName(), impexMedia.getMime());
            inputStream = null;
        }
        catch(FileNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
            deleteTempDump();
        }
    }
}
