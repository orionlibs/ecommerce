package de.hybris.y2ysync.task.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.logging.Logs;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.media.ConsumeMarkerMediaModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class Y2YSyncDAO
{
    private static final Logger LOG = Logger.getLogger(Y2YSyncDAO.class);
    private FlexibleSearchService flexibleSearchService;


    public List<SyncImpExMediaModel> findSyncMediasBySyncCronJob(String syncCronJobCode)
    {
        String queryString = "SELECT {m.pk} FROM {SyncImpExMedia AS m JOIN Y2YSyncCronJob AS c ON {m.exportCronJob}={c.pk}} WHERE {c.code}=?code";
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {m.pk} FROM {SyncImpExMedia AS m JOIN Y2YSyncCronJob AS c ON {m.exportCronJob}={c.pk}} WHERE {c.code}=?code");
        query.addQueryParameter("code", syncCronJobCode);
        SearchResult<SyncImpExMediaModel> searchResult = this.flexibleSearchService.search(query);
        Logs.debug(LOG, () -> "Found medias for syncExecutionID: " + syncCronJobCode + " >>> " + searchResult.getCount() + ", query was >>> SELECT {m.pk} FROM {SyncImpExMedia AS m JOIN Y2YSyncCronJob AS c ON {m.exportCronJob}={c.pk}} WHERE {c.code}=?code");
        return searchResult.getResult();
    }


    public Y2YSyncCronJobModel findSyncCronJobByCode(String syncCronJobCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Y2YSyncCronJob} WHERE {code} = ?code");
        query.addQueryParameter("code", syncCronJobCode);
        return (Y2YSyncCronJobModel)this.flexibleSearchService.searchUnique(query);
    }


    public List<ConsumeMarkerMediaModel> findConsumeMarkerMedias(String syncExecutionId)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {ConsumeMarkerMedia} WHERE {syncExecutionID}=?syncExecutionId");
        query.addQueryParameter("syncExecutionId", syncExecutionId);
        SearchResult<ConsumeMarkerMediaModel> searchResult = this.flexibleSearchService.search(query);
        return searchResult.getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
