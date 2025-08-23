package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.constants.GeneratedMediaConversionConstants;
import de.hybris.platform.mediaconversion.model.job.MediaConversionCronJobModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaConversionJobDao implements MediaConversionJobDao
{
    private FlexibleSearchService flexibleSearchService;
    public static final String CATALOGVERSION_PARAMETER = "catVersion";
    public static final String MEDIAFORMATS_PARAMETER = "formats";


    Map<String, Object> queryParams(MediaConversionCronJobModel cronJob)
    {
        Map<String, Object> params = new TreeMap<>();
        if(cronJob.getIncludedFormats() != null && !cronJob.getIncludedFormats().isEmpty())
        {
            params.put("formats", cronJob.getIncludedFormats());
        }
        if(cronJob.getCatalogVersion() != null)
        {
            params.put("catVersion", cronJob.getCatalogVersion());
        }
        return params;
    }


    public Collection<List<PK>> queryFormatsPerContainerToConvert(MediaConversionCronJobModel cronJob)
    {
        Map<String, Object> params = queryParams(cronJob);
        FlexibleSearchQuery query = new FlexibleSearchQuery(
                        "SELECT un.container, un.format FROM ( {{SELECT sub.container, sub.format FROM ({{SELECT {a.pk} AS container, {f.pk} AS format, {m.pk} AS media FROM {MediaContainer AS a LEFT JOIN " + GeneratedMediaConversionConstants.Relations.CONVERSIONGROUPTOFORMATREL
                                        + " AS tf ON {a.conversionGroup} = {tf.source} JOIN ConversionMediaFormat AS f ON ({a.conversionGroup} IS NULL OR {tf.target} = {f.pk}) LEFT JOIN Media AS m ON {m.mediaContainer} = {a.pk} AND {m.mediaFormat} = {f.pk} } WHERE 1 = 1 " + (params.containsKey(
                                        "formats") ? "AND {f.pk} IN (?formats) " : "") + (params.containsKey("catVersion") ? "AND {a.catalogVersion} = ?catVersion " : "") + "}}) sub WHERE sub.media IS NULL }} UNION ALL {{" + outdatedMediaQuery(params) + "}} ) un ORDER BY un.container", params);
        query.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class}));
        return getFlexibleSearchService().search(query).getResult();
    }


    private String outdatedMediaQuery(Map<String, Object> params)
    {
        return "SELECT {ua.pk} AS container, {cm.mediaFormat} AS format FROM {MediaContainer AS ua JOIN Media as cm ON {cm.mediaContainer} = {ua.pk} JOIN ConversionMediaFormat as cmf ON {cm.mediaFormat} = {cmf.pk} LEFT JOIN Media as om ON {om.mediaContainer} = {ua.pk} AND {om.pk} = {cm.original} } WHERE {cm.originalDataPK} IS NOT NULL AND ({om.dataPK} IS NULL OR {cm.originalDataPK} <> {om.dataPK}) "
                        + (params.containsKey("formats") ?
                        "AND {cm.mediaFormat} IN (?formats) " :
                        "") + (
                        params.containsKey("catVersion") ?
                                        "AND {ua.catalogVersion} = ?catVersion " :
                                        "");
    }


    public Collection<List<PK>> queryOutdatedMedias(MediaConversionCronJobModel cronJob, MediaContainerModel container)
    {
        Map<String, Object> params = queryParams(cronJob);
        params.put("currentContainer", container);
        FlexibleSearchQuery query = new FlexibleSearchQuery(outdatedMediaQuery(params) + "AND {ua.pk} = ?currentContainer", params);
        query.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class}));
        return getFlexibleSearchService().search(query).getResult();
    }


    public Collection<PK> queryConvertedMedias(MediaConversionCronJobModel cronJob)
    {
        Map<String, Object> params = queryParams(cronJob);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {m.pk} FROM {Media as m JOIN MediaContainer as a ON {m.mediaContainer} = {a.pk}} WHERE {m.original} IS NOT NULL " + (params.containsKey("formats") ? "AND {m.mediaFormat} in (?formats) " : "") + (params.containsKey("catVersion")
                        ? "AND {a.catalogVersion} = ?catVersion"
                        : ""), params);
        query.setResultClassList(Collections.singletonList(PK.class));
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
