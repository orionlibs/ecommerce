package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IndexerService
{
    void performFullIndex(FacetSearchConfig paramFacetSearchConfig) throws IndexerException;


    void performFullIndex(FacetSearchConfig paramFacetSearchConfig, Map<String, String> paramMap) throws IndexerException;


    void updateIndex(FacetSearchConfig paramFacetSearchConfig) throws IndexerException;


    void updateIndex(FacetSearchConfig paramFacetSearchConfig, Map<String, String> paramMap) throws IndexerException;


    void updateTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType) throws IndexerException;


    void updateTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Map<String, String> paramMap) throws IndexerException;


    void updateTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, List<PK> paramList) throws IndexerException;


    void updateTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, List<PK> paramList, Map<String, String> paramMap) throws IndexerException;


    void updatePartialTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Collection<IndexedProperty> paramCollection, List<PK> paramList) throws IndexerException;


    void updatePartialTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Collection<IndexedProperty> paramCollection, List<PK> paramList, Map<String, String> paramMap) throws IndexerException;


    void deleteFromIndex(FacetSearchConfig paramFacetSearchConfig) throws IndexerException;


    void deleteFromIndex(FacetSearchConfig paramFacetSearchConfig, Map<String, String> paramMap) throws IndexerException;


    void deleteTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType) throws IndexerException;


    void deleteTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Map<String, String> paramMap) throws IndexerException;


    void deleteTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, List<PK> paramList) throws IndexerException;


    void deleteTypeIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, List<PK> paramList, Map<String, String> paramMap) throws IndexerException;
}
