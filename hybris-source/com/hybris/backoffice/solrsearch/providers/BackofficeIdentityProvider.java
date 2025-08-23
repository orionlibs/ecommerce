package com.hybris.backoffice.solrsearch.providers;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import java.io.Serializable;

public class BackofficeIdentityProvider implements IdentityProvider<ItemModel>, Serializable
{
    public String getIdentifier(IndexConfig indexConfig, ItemModel model)
    {
        return model.getPk().getLongValueAsString();
    }
}
