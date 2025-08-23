package de.hybris.platform.webservicescommons.oauth2.scope.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDExternalScopesModel;
import de.hybris.platform.webservicescommons.oauth2.scope.ExternalScopesDao;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExternalScopesDao extends DefaultGenericDao<OpenIDExternalScopesModel> implements ExternalScopesDao
{
    private FlexibleSearchService flexibleSearchService;


    public DefaultExternalScopesDao()
    {
        super("OpenIDExternalScopesModel");
    }


    public List<OpenIDExternalScopesModel> findScopesByClientAndPrincipal(OAuthClientDetailsModel clientDetailsModel, PrincipalModel principal)
    {
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(String.format("select {pk} from {%s} where {%s}= ?clientDetails and {%s} LIKE ?expression ", new Object[] {"OpenIDExternalScopes", "clientDetailsId", "permittedPrincipals"}),
                        (Map)ImmutableMap.of("clientDetails", clientDetailsModel));
        flexibleSearchQuery.addQueryParameter("expression", "%," + principal.getPk().toString() + ",%");
        SearchResult<OpenIDExternalScopesModel> search = this.flexibleSearchService.search(flexibleSearchQuery);
        return search.getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
