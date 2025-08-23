package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexOperationIdGenerator;
import de.hybris.platform.solrfacetsearch.solr.Index;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexOperationIdGenerator implements IndexOperationIdGenerator
{
    private KeyGenerator indexOperationIdKeyGenerator;


    public KeyGenerator getIndexOperationIdKeyGenerator()
    {
        return this.indexOperationIdKeyGenerator;
    }


    @Required
    public void setIndexOperationIdKeyGenerator(KeyGenerator indexOperationIdKeyGenerator)
    {
        this.indexOperationIdKeyGenerator = indexOperationIdKeyGenerator;
    }


    public long generate(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Index index)
    {
        long timestamp = System.currentTimeMillis();
        long sequence = Long.parseLong((String)this.indexOperationIdKeyGenerator.generate());
        return (timestamp << 16L) + sequence;
    }
}
