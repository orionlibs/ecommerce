package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexConfig;

public interface IdentityProvider<T>
{
    String getIdentifier(IndexConfig paramIndexConfig, T paramT);
}
