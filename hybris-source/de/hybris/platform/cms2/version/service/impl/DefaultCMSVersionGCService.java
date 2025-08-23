package de.hybris.platform.cms2.version.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSVersionGCDao;
import de.hybris.platform.cms2.version.service.CMSVersionGCService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionGCService implements CMSVersionGCService
{
    private TimeService timeService;
    private CMSVersionGCDao cmsVersionGCDao;


    public List<CMSVersionModel> getRetainableVersions(int maxAgeDays, int maxNumberVersions)
    {
        Date maxAgeDate = (new DateTime(getTimeService().getCurrentTime())).minusDays(maxAgeDays).toDate();
        List<CMSVersionModel> allRetainableVersions = getCmsVersionGCDao().findRetainableVersions((maxAgeDays > 0) ? maxAgeDate : null);
        Map<CatalogVersionModel, Map<String, List<CMSVersionModel>>> groupedVersions = (Map<CatalogVersionModel, Map<String, List<CMSVersionModel>>>)allRetainableVersions.stream()
                        .collect(Collectors.groupingBy(CMSVersionModel::getItemCatalogVersion, Collectors.groupingBy(CMSVersionModel::getItemUid)));
        Stream<Map<String, List<CMSVersionModel>>> groupedVersionsStream = groupedVersions.values().stream();
        Stream<List<CMSVersionModel>> versionsStream = groupedVersionsStream.flatMap(map -> map.values().stream());
        return (List<CMSVersionModel>)versionsStream.flatMap(cmsVersionModels -> (maxNumberVersions > 0) ? filterByRetainAndLimitRemaining(cmsVersionModels, maxNumberVersions) : cmsVersionModels.stream())
                        .collect(Collectors.toList());
    }


    protected Stream<CMSVersionModel> filterByRetainAndLimitRemaining(List<CMSVersionModel> cmsVersionModels, int maxNumberVersions)
    {
        return Stream.concat(cmsVersionModels
                        .stream()
                        .filter(CMSVersionModel::getRetain), cmsVersionModels
                        .stream()
                        .filter(cmsVersionModel -> !cmsVersionModel.getRetain().booleanValue())
                        .sorted(Comparator.<CMSVersionModel, Comparable>comparing(ItemModel::getCreationtime).reversed())
                        .limit(maxNumberVersions));
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    protected CMSVersionGCDao getCmsVersionGCDao()
    {
        return this.cmsVersionGCDao;
    }


    @Required
    public void setCmsVersionGCDao(CMSVersionGCDao cmsVersionGCDao)
    {
        this.cmsVersionGCDao = cmsVersionGCDao;
    }
}
