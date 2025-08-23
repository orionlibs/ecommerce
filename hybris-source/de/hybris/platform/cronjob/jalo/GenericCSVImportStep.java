package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class GenericCSVImportStep extends GeneratedGenericCSVImportStep
{
    protected abstract boolean processObjects(Map paramMap, CronJob paramCronJob) throws JaloBusinessException;


    protected void finalizeStep(CronJob cronJob) throws JaloBusinessException
    {
    }


    protected boolean skipFirstLine()
    {
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected final boolean processLine(String line, CronJob cronJob) throws JaloBusinessException, AbortCronJobException
    {
        return false;
    }


    protected char[] getTokenCharacters()
    {
        return new char[] {'|'};
    }


    protected char[] getCommentOutCharacters()
    {
        return new char[] {'#', '%'};
    }


    protected CSVReader createCSVReader(InputStream inputStream, String encoding) throws UnsupportedEncodingException
    {
        return new CSVReader(inputStream, encoding);
    }


    protected void performStep(CronJob cronJob) throws AbortCronJobException, JaloInvalidParameterException
    {
        JobMedia media = prepareMedia(cronJob);
        CSVReader csvreader = null;
        boolean finished = false;
        boolean succeeded = false;
        boolean saveLineNumber = cronJob instanceof MediaProcessCronJob;
        int lastSuccessful = 0;
        int current = 0;
        Integer last = saveLineNumber ? ((MediaProcessCronJob)cronJob).getLastSuccessfulLine() : null;
        try
        {
            media.setLocked(true);
            EnumerationValue errorMode = cronJob.getErrorMode();
            if(errorMode == null)
            {
                errorMode = getErrorMode();
            }
            boolean ignoreErrors = (errorMode != null && GeneratedCronJobConstants.Enumerations.ErrorMode.IGNORE.equals(errorMode.getCode()));
            InputStream inputStream = null;
            try
            {
                inputStream = openMedia(media, cronJob);
                csvreader = createCSVReader(inputStream, getCharSet());
            }
            catch(UnsupportedEncodingException ex)
            {
                if(inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch(IOException e)
                    {
                        throw new JaloInternalException(e);
                    }
                }
                throw ex;
            }
            csvreader.setFieldSeparator(getTokenCharacters());
            csvreader.setCommentOut(getCommentOutCharacters());
            if(last != null)
            {
                csvreader.setLinesToSkip(last.intValue());
                lastSuccessful = current = last.intValue();
            }
            else if(skipFirstLine())
            {
                csvreader.setLinesToSkip(1);
                lastSuccessful = current = 1;
            }
            if(saveLineNumber && last != null && isInfoEnabled())
            {
                info("skipping first " + last.intValue() + " rows");
            }
            while(csvreader.readNextLine())
            {
                Map line = csvreader.getLine();
                current++;
                if(isDebugEnabled())
                {
                    debug("parsed " + line + " from '" + csvreader.getSourceLine() + "'");
                }
                checkAbort(cronJob);
                try
                {
                    succeeded = processObjects(line, cronJob);
                }
                catch(Exception e)
                {
                    if(isErrorEnabled())
                    {
                        error("error processing line " + current + " : " + e.getMessage() + " : " + Utilities.getStackTraceAsString(e));
                    }
                    if(!ignoreErrors)
                    {
                        throw new AbortCronJobException("abort due to line error : " + e.getMessage());
                    }
                }
                if(succeeded)
                {
                    lastSuccessful = current;
                }
            }
            finished = true;
            finalizeStep(cronJob);
            if(isInfoEnabled())
            {
                info("performStep finished without exceptions [STEP:" + this + "; CronJob: " + cronJob + "]");
            }
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JaloInternalException(e);
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
            if(saveLineNumber)
            {
                if(!finished)
                {
                    ((MediaProcessCronJob)cronJob).setCurrentLine(current);
                    ((MediaProcessCronJob)cronJob).setLastSuccessfulLine(lastSuccessful);
                }
                else
                {
                    ((MediaProcessCronJob)cronJob).setCurrentLine(null);
                    ((MediaProcessCronJob)cronJob).setLastSuccessfulLine(null);
                }
            }
            media.setLocked(false);
            try
            {
                if(csvreader != null)
                {
                    csvreader.close();
                }
            }
            catch(IOException e)
            {
                throw new JaloInternalException(e);
            }
        }
    }


    protected String getCharSet()
    {
        return "UTF-8";
    }


    protected InputStream openMedia(JobMedia media, CronJob cronJob) throws JaloBusinessException, IOException
    {
        return media.getDataFromStream();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected final Map tokenise(String line)
    {
        return null;
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable()
    {
        return true;
    }


    protected void checkAbort(CronJob cronJob) throws AbortCronJobException
    {
        if(cronJob.isRequestAbortAsPrimitive())
        {
            throw new AbortCronJobException("detected request of cronjob abort");
        }
        if(cronJob.isRequestAbortStepAsPrimitive())
        {
            cronJob.setRequestAbortStep(null);
            if(isWarnEnabled())
            {
                warn("abort step requested");
            }
            throw new AbortCronJobException("abort step requested");
        }
    }
}
