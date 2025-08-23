package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.mediaconversion.model.job.ExtractMediaMetaDataCronJobModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExtractMediaMetaDataJobDao implements ExtractMediaMetaDataJobDao
{
    private FlexibleSearchService flexibleSearchService;


    public Collection<PK> findMetaDataUpdates(ExtractMediaMetaDataCronJobModel cronJob)
    {
        Map<String, Object> params = new TreeMap<>();
        if(cronJob.getCatalogVersion() != null)
        {
            params.put("catalogVers", cronJob.getCatalogVersion());
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery(
                        "SELECT {pk} FROM {Media} WHERE ({dataPK} <> {metaDataDataPK} OR {metaDataDataPK} IS NULL) AND {dataPK} IS NOT NULL " + ((cronJob.getCatalogVersion() != null) ? "AND {catalogVersion} = ?catalogVers " : "") + (!Boolean.TRUE.equals(cronJob.getIncludeConverted())
                                        ? "AND {original} IS NULL "
                                        : "") + (Boolean.TRUE.equals(cronJob.getContainerMediasOnly()) ? "AND {mediaContainer} IS NOT NULL " : ""), params);
        query.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class}));
        SearchResult<PK> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
