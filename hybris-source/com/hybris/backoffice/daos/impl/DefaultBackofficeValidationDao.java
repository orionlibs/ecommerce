package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.daos.BackofficeValidationDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.DefaultFlexibleSearchService;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeValidationDao implements BackofficeValidationDao
{
    private DefaultFlexibleSearchService flexibleSearchService;


    @Required
    public void setFlexibleSearchService(DefaultFlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public DefaultFlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public Collection<ConstraintGroupModel> getConstraintGroups(Collection<String> groupsIds)
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT {PK} FROM {ConstraintGroup} WHERE {ID} IN (?ids)");
        fsq.addQueryParameter("ids", groupsIds);
        fsq.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {ConstraintGroupModel.class}));
        SearchResult<ConstraintGroupModel> resultSet = getFlexibleSearchService().search(fsq);
        return resultSet.getResult();
    }
}
