package de.hybris.platform.webservicescommons.oauth2.client.impl;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import java.util.Collections;
import java.util.List;

public class DefaultClientDetailsDao extends DefaultGenericDao<OAuthClientDetailsModel> implements ClientDetailsDao
{
    public DefaultClientDetailsDao()
    {
        super("OAuthClientDetails");
    }


    public OAuthClientDetailsModel findClientById(String clientId)
    {
        List<OAuthClientDetailsModel> resList = find(Collections.singletonMap("clientId", clientId));
        if(resList.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + resList.size() + " clients with the unique clientId '" + clientId + "'");
        }
        return resList.isEmpty() ? null : resList.get(0);
    }
}
