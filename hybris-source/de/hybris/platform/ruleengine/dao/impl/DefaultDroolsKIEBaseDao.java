package de.hybris.platform.ruleengine.dao.impl;

import de.hybris.platform.ruleengine.dao.DroolsKIEBaseDao;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;

public class DefaultDroolsKIEBaseDao extends AbstractItemDao implements DroolsKIEBaseDao
{
    private static final String FIND_ALL_KIEBASES = "select {pk} from {DroolsKIEBase}";


    public List<DroolsKIEBaseModel> findAllKIEBases()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DroolsKIEBase}");
        SearchResult<DroolsKIEBaseModel> searchResult = getFlexibleSearchService().search(query);
        return searchResult.getResult();
    }
}
