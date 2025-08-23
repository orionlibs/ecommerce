package de.hybris.platform.ruleengine.dao.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.ruleengine.dao.DroolsKIEModuleMediaDao;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleMediaModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Map;
import java.util.Optional;

public class DefaultDroolsKIEModuleMediaDao extends AbstractItemDao implements DroolsKIEModuleMediaDao
{
    private static final String FIND_KIEMODULE_MEDIA = "select {pk} from {DroolsKIEModuleMedia} where {kieModuleName} = ?kmname and {releaseId} = ?releaseId";


    public Optional<DroolsKIEModuleMediaModel> findKIEModuleMedia(String kieModuleName, String releaseId)
    {
        ImmutableMap immutableMap = ImmutableMap.of("kmname", kieModuleName, "releaseId", releaseId);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsKIEModuleMedia} where {kieModuleName} = ?kmname and {releaseId} = ?releaseId", (Map)immutableMap);
        SearchResult<DroolsKIEModuleMediaModel> searchResult = getFlexibleSearchService().search(query);
        return searchResult.getResult().stream().findFirst();
    }
}
