package de.hybris.platform.webservicescommons.oauth2.scope;

import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import java.util.List;

public interface OpenIDExternalScopesStrategy
{
    List<String> getExternalScopes(OAuthClientDetailsModel paramOAuthClientDetailsModel, String paramString);
}
