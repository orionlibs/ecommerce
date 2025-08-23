package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.jalo.ErrorHandler;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import org.apache.log4j.Logger;

public class CronJobErrorHandler implements ErrorHandler
{
    private static final Logger LOG = Logger.getLogger(CronJobErrorHandler.class);
    private final CronJob cronjob;


    public CronJobErrorHandler(CronJob cronjob)
    {
        this.cronjob = cronjob;
    }


    public ErrorHandler.RESULT handleError(ImpExException impexException, ImpExReader reader)
    {
        EnumerationValue errorMode = this.cronjob.getErrorMode();
        if(errorMode != null && GeneratedCronJobConstants.Enumerations.ErrorMode.PAUSE.equals(errorMode.getCode()))
        {
            LOG.warn("Pause error mode is not supported, will fail");
            return ErrorHandler.RESULT.FAIL;
        }
        if(errorMode != null && GeneratedCronJobConstants.Enumerations.ErrorMode.IGNORE.equals(errorMode.getCode()))
        {
            if(impexException.getErrorCode() == 123)
            {
                LOG.error(impexException);
            }
            else
            {
                LOG.error("Exception ocurred, will ignore: " + impexException, (Throwable)impexException);
            }
            return ErrorHandler.RESULT.IGNORE;
        }
        return ErrorHandler.RESULT.FAIL;
    }
}
