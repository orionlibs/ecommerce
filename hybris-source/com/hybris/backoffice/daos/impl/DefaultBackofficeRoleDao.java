package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.daos.BackofficeRoleDao;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeRoleDao implements BackofficeRoleDao
{
    private FlexibleSearchService flexibleSearchService;


    public Set<BackofficeRoleModel> findAllBackofficeRoles()
    {
        Set<BackofficeRoleModel> backOfficeRoles = new LinkedHashSet<>();
        String queryString = "SELECT {PK} FROM {BackofficeRole}";
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {BackofficeRole}");
        query.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {BackofficeRoleModel.class}));
        SearchResult<BackofficeRoleModel> search = this.flexibleSearchService.search(query);
        backOfficeRoles.addAll(search.getResult());
        return backOfficeRoles;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
