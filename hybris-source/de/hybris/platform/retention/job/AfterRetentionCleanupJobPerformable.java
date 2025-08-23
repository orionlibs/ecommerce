package de.hybris.platform.retention.job;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.RetentionCleanupAction;
import de.hybris.platform.retention.RetentionItemsProvider;
import de.hybris.platform.retention.RetentionItemsProviderFactory;
import de.hybris.platform.retention.RetentionRequestParams;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.internal.model.RetentionJobModel;
import de.hybris.platform.tx.Transaction;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class AfterRetentionCleanupJobPerformable extends AbstractJobPerformable<CronJobModel>
{
    private static final Logger LOG = Logger.getLogger(AfterRetentionCleanupJobPerformable.class);
    private RetentionItemsProviderFactory retentionItemsProviderFactory;


    public PerformResult perform(CronJobModel cronJob)
    {
        return performInternal(cronJob);
    }


    private PerformResult performInternal(CronJobModel cronJobModel)
    {
        RetentionJobModel retentionJob = (RetentionJobModel)cronJobModel.getJob();
        Integer batchSize = retentionJob.getBatchSize();
        Objects.requireNonNull(batchSize);
        boolean caughtException = false;
        boolean failure = false;
        try
        {
            LOG.info("Starting AfterRetentionCleanupJob");
            AbstractRetentionRuleModel afterRetentionCleanupRule = retentionJob.getRetentionRule();
            RetentionRequestParams retentionParams = RetentionRequestParams.builder().withRuleModel(afterRetentionCleanupRule).withBatchSize(batchSize).build();
            RetentionItemsProvider itemsProvider = this.retentionItemsProviderFactory.create(retentionParams);
            RetentionCleanupAction retentionCleanupAction = getRetentionCleanupAction(afterRetentionCleanupRule);
            List<ItemToCleanup> queryResult = null;
            LOG.info("Selecting items for clean up using " + afterRetentionCleanupRule.getCode());
            long batchNumber = 0L;
            while(true)
            {
                if(cronJobModel.getPk() != null && clearAbortRequestedIfNeeded(cronJobModel))
                {
                    return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
                }
                Transaction tx = Transaction.current();
                tx.begin();
                boolean transactionSuccessfull = false;
                ItemToCleanup item = null;
                try
                {
                    queryResult = itemsProvider.nextItemsForCleanup();
                    if(queryResult != null)
                    {
                        LOG.info("Sending " + queryResult.size() + " items for clean up, using " + afterRetentionCleanupRule
                                        .getActionReference());
                        for(ItemToCleanup ite : queryResult)
                        {
                            item = ite;
                            retentionCleanupAction.cleanup(this, afterRetentionCleanupRule, ite);
                        }
                    }
                    transactionSuccessfull = true;
                }
                catch(Exception ex)
                {
                    transactionSuccessfull = false;
                    failure = true;
                    LOG.error(buildErrMsg(batchNumber, item), ex);
                }
                finally
                {
                    batchNumber++;
                    if(transactionSuccessfull)
                    {
                        tx.commit();
                    }
                    else
                    {
                        tx.rollback();
                    }
                }
                if(CollectionUtils.isEmpty(queryResult))
                {
                    caughtException = false;
                    return new PerformResult(determineCronJobResult(failure, caughtException), CronJobStatus.FINISHED);
                }
            }
        }
        catch(Exception ex)
        {
            caughtException = true;
            LOG.error("Caught exception during AfterRetentionCleanupJob ", ex);
        }
        return new PerformResult(determineCronJobResult(failure, caughtException), CronJobStatus.FINISHED);
    }


    private String buildErrMsg(long batchNumber, ItemToCleanup item)
    {
        StringBuilder buff = new StringBuilder(String.format("Caught exception during processing batch number %s", new Object[] {Long.valueOf(batchNumber)}));
        if(item != null)
        {
            buff.append(String.format(" for item %s", new Object[] {item}));
        }
        return buff.toString();
    }


    private CronJobResult determineCronJobResult(boolean failure, boolean caughtException)
    {
        CronJobResult jobResult = failure ? CronJobResult.FAILURE : CronJobResult.SUCCESS;
        return caughtException ? CronJobResult.ERROR : jobResult;
    }


    private RetentionCleanupAction getRetentionCleanupAction(AbstractRetentionRuleModel afterRetentionCleanupRule)
    {
        String retentionCleanupRuleBean = afterRetentionCleanupRule.getActionReference();
        return (RetentionCleanupAction)Registry.getApplicationContext().getBean(retentionCleanupRuleBean, RetentionCleanupAction.class);
    }


    @Required
    public void setRetentionItemsProviderFactory(RetentionItemsProviderFactory retentionItemsProviderFactory)
    {
        this.retentionItemsProviderFactory = retentionItemsProviderFactory;
    }
}
