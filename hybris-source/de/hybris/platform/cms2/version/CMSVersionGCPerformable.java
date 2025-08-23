package de.hybris.platform.cms2.version;

import de.hybris.platform.cms2.version.strategies.CMSVersionCollectRetainableStrategy;
import de.hybris.platform.cms2.version.strategies.CMSVersionDeleteStrategy;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMSVersionGCPerformable extends AbstractJobPerformable<CronJobModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(CMSVersionGCPerformable.class);
    private final CMSVersionCollectRetainableStrategy collectRetainableStrategy;
    private final CMSVersionDeleteStrategy versionDeleteStrategy;


    public CMSVersionGCPerformable(CMSVersionCollectRetainableStrategy collectRetainableStrategy, CMSVersionDeleteStrategy versionDeleteStrategy)
    {
        this.collectRetainableStrategy = collectRetainableStrategy;
        this.versionDeleteStrategy = versionDeleteStrategy;
    }


    public boolean isAbortable()
    {
        return true;
    }


    public PerformResult perform(CronJobModel cronJobModel)
    {
        try
        {
            Set<PK> retainablePKs = this.collectRetainableStrategy.fetchRetainableVersions();
            if(clearAbortRequestedIfNeeded(cronJobModel))
            {
                return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
            }
            Optional<PerformResult> performResult = deleteObsoleteVersions(cronJobModel, retainablePKs);
            if(performResult.isPresent())
            {
                return performResult.get();
            }
        }
        catch(Exception e)
        {
            LOG.error("CMS Version Garbage Collection failed.", e);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.UNKNOWN);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    private Optional<PerformResult> deleteObsoleteVersions(CronJobModel cronJobModel, Set<PK> retainablePKs) throws Exception
    {
        FlexibleSearchQuery versionsToDelete = new FlexibleSearchQuery("SELECT {v:pk} FROM {CMSVersion AS v} ORDER BY {v:pk} DESC");
        versionsToDelete.setResultClassList(Collections.singletonList(PK.class));
        SearchResult<PK> result = this.flexibleSearchService.search(versionsToDelete);
        if(clearAbortRequestedIfNeeded(cronJobModel))
        {
            return Optional.of(new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED));
        }
        List<PK> toDelete = new ArrayList<>(result.getResult());
        toDelete.removeAll(retainablePKs);
        int numDelete = toDelete.size();
        int pageSize = (cronJobModel.getQueryCount() > 0) ? cronJobModel.getQueryCount() : 1000;
        int i;
        for(i = 0; i < numDelete; i += pageSize)
        {
            int endIdx = Math.min(i + pageSize, numDelete);
            List<PK> batchToDelete = toDelete.subList(i, endIdx);
            this.versionDeleteStrategy.deleteVersions(batchToDelete);
            LOG.debug("Deleted {} / {}", Integer.valueOf(i + batchToDelete.size()), Integer.valueOf(numDelete));
            if(clearAbortRequestedIfNeeded(cronJobModel))
            {
                return Optional.of(new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED));
            }
        }
        LOG.info("{} obsolete versions deleted; {} versions retained", Integer.valueOf(numDelete), Integer.valueOf(retainablePKs.size()));
        return Optional.empty();
    }
}
