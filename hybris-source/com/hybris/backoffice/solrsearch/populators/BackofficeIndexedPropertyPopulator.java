package com.hybris.backoffice.solrsearch.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

public class BackofficeIndexedPropertyPopulator implements Populator<SolrIndexedPropertyModel, IndexedProperty>
{
    public void populate(SolrIndexedPropertyModel solrIndexedPropertyModel, IndexedProperty indexedProperty) throws ConversionException
    {
        indexedProperty.setBackofficeDisplayName(solrIndexedPropertyModel.getBackofficeDisplayName());
        indexedProperty.setClassAttributeAssignment(solrIndexedPropertyModel.getClassAttributeAssignment());
    }
}
