package de.hybris.platform.assertions;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.assertj.core.api.AbstractAssert;

public class CronJobResultAssert extends AbstractAssert<CronJobResultAssert, PerformResult>
{
    public CronJobResultAssert(PerformResult actual)
    {
        super(actual, CronJobResultAssert.class);
    }


    public static CronJobResultAssert assertThat(PerformResult actual)
    {
        return new CronJobResultAssert(actual);
    }


    public CronJobResultAssert failed()
    {
        isNotNull();
        if(!CronJobResult.FAILURE.equals(((PerformResult)this.actual).getResult()))
        {
            failWithMessage("Expected cron job result should be <%s> but was <%s>", new Object[] {CronJobResult.FAILURE, ((PerformResult)this.actual).getResult()});
        }
        return this;
    }


    public CronJobResultAssert succeded()
    {
        isNotNull();
        if(!CronJobResult.SUCCESS.equals(((PerformResult)this.actual).getResult()))
        {
            failWithMessage("Expected cron job result should be <%s> but was <%s>", new Object[] {CronJobResult.SUCCESS, ((PerformResult)this.actual).getResult()});
        }
        return this;
    }


    public CronJobResultAssert aborted()
    {
        isNotNull();
        if(!CronJobStatus.ABORTED.equals(((PerformResult)this.actual).getStatus()))
        {
            failWithMessage("Expected cron job status should be <%s> but was <%s>", new Object[] {CronJobStatus.ABORTED, ((PerformResult)this.actual).getStatus()});
        }
        return this;
    }


    public CronJobResultAssert finished()
    {
        isNotNull();
        if(!CronJobStatus.FINISHED.equals(((PerformResult)this.actual).getStatus()))
        {
            failWithMessage("Expected cron job status should be <%s> but was <%s>", new Object[] {CronJobStatus.FINISHED, ((PerformResult)this.actual).getStatus()});
        }
        return this;
    }
}
