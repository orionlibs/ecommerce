package de.hybris.platform.servicelayer.search.paginated.impl;

import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.AbstractQueryHelper;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultAbstractQueryHelper implements AbstractQueryHelper
{
    public <T extends FlexibleSearchQuery> T getUpdatedFlexibleSearchQuery(FlexibleSearchQuery original, String query)
    {
        ServicesUtil.validateParameterNotNull(original, "original must not be null!");
        ServicesUtil.validateParameterNotNull(query, "query must not be null!");
        FlexibleSearchQuery updatedQuery = new FlexibleSearchQuery(query);
        copyAttributes(original, updatedQuery);
        updatedQuery.addQueryParameters(original.getQueryParameters());
        return (T)updatedQuery;
    }


    public <T extends GenericSearchQuery> T getUpdatedGenericSearchQuery(GenericSearchQuery original, GenericQuery query)
    {
        ServicesUtil.validateParameterNotNull(original, "original must not be null!");
        ServicesUtil.validateParameterNotNull(query, "query must not be null!");
        GenericSearchQuery updatedQuery = new GenericSearchQuery(query);
        copyAttributes(original, updatedQuery);
        return (T)updatedQuery;
    }


    protected <T extends de.hybris.platform.servicelayer.search.AbstractQuery> void copyAttributes(T original, T target)
    {
        target.setCount(original.getCount());
        target.setStart(original.getStart());
        target.setNeedTotal(original.isNeedTotal());
        target.setCatalogVersions(original.getCatalogVersions());
        target.setDisableCaching(original.isDisableCaching());
        target.setDisableSearchRestrictions(original.isDisableSearchRestrictions());
        target.setDisableSpecificDbLimitSupport(original.isDisableSpecificDbLimitSupport());
        target.setFailOnUnknownFields(original.isFailOnUnknownFields());
        target.setLocale(original.getLocale());
        target.setResultClassList(original.getResultClassList());
        target.setSessionSearchRestrictions(original.getSessionSearchRestrictions());
        target.setUser(original.getUser());
    }
}
