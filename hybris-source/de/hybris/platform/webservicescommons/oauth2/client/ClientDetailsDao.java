package de.hybris.platform.webservicescommons.oauth2.client;

import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

public interface ClientDetailsDao
{
    OAuthClientDetailsModel findClientById(String paramString);
}
