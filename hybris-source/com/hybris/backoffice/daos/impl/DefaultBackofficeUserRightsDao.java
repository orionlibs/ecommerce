package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.daos.BackofficeUserRightsDao;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeUserRightsDao implements BackofficeUserRightsDao
{
    private transient FlexibleSearchService flexibleSearchService;
    private static final String FIND_USER_RIGHTS_BY_CODE = "select {PK} from {UserRight as u} where {u.code} = ?code";
    private static final String CODE = "code";


    public Collection<UserRightModel> findUserRightsByCode(String code)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {PK} from {UserRight as u} where {u.code} = ?code");
        query.addQueryParameter("code", code);
        SearchResult<UserRightModel> result = this.flexibleSearchService.search(query);
        return result.getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
