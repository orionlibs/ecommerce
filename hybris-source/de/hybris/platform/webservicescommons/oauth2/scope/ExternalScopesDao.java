package de.hybris.platform.webservicescommons.oauth2.scope;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDExternalScopesModel;
import java.util.List;

public interface ExternalScopesDao
{
    List<OpenIDExternalScopesModel> findScopesByClientAndPrincipal(OAuthClientDetailsModel paramOAuthClientDetailsModel, PrincipalModel paramPrincipalModel);
}
