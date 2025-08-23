package de.hybris.platform.solrfacetsearch.indexer.spi;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import java.util.Collection;
import org.apache.solr.common.SolrInputDocument;

public interface Exporter
{
    void exportToUpdateIndex(Collection<SolrInputDocument> paramCollection, FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType) throws ExporterException;


    void exportToDeleteFromIndex(Collection<String> paramCollection, FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType) throws ExporterException;
}
