package de.hybris.platform.adaptivesearchsolr.strategies.impl;

import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearchsolr.strategies.SolrAsTypeMappingRegistry;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.search.BoostField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.InitializingBean;

public class DefaultSolrAsTypeMappingRegistry implements SolrAsTypeMappingRegistry, InitializingBean
{
    private final BidiMap<AsFacetType, FacetType> facetTypeMapping = (BidiMap<AsFacetType, FacetType>)new DualHashBidiMap();
    private final BidiMap<AsBoostOperator, SearchQuery.QueryOperator> boostOperatorMapping = (BidiMap<AsBoostOperator, SearchQuery.QueryOperator>)new DualHashBidiMap();
    private final BidiMap<AsBoostType, BoostField.BoostType> boostTypeMapping = (BidiMap<AsBoostType, BoostField.BoostType>)new DualHashBidiMap();


    public void afterPropertiesSet()
    {
        populateFacetTypeMapping();
        populateBoostOperatorMapping();
        populateBoostTypeMapping();
    }


    protected void populateFacetTypeMapping()
    {
        this.facetTypeMapping.put(AsFacetType.REFINE, FacetType.REFINE);
        this.facetTypeMapping.put(AsFacetType.MULTISELECT_OR, FacetType.MULTISELECTOR);
        this.facetTypeMapping.put(AsFacetType.MULTISELECT_AND, FacetType.MULTISELECTAND);
    }


    protected void populateBoostOperatorMapping()
    {
        this.boostOperatorMapping.put(AsBoostOperator.EQUAL, SearchQuery.QueryOperator.EQUAL_TO);
        this.boostOperatorMapping.put(AsBoostOperator.GREATER_THAN, SearchQuery.QueryOperator.GREATER_THAN);
        this.boostOperatorMapping.put(AsBoostOperator.GREATER_THAN_OR_EQUAL, SearchQuery.QueryOperator.GREATER_THAN_OR_EQUAL_TO);
        this.boostOperatorMapping.put(AsBoostOperator.LESS_THAN, SearchQuery.QueryOperator.LESS_THAN);
        this.boostOperatorMapping.put(AsBoostOperator.LESS_THAN_OR_EQUAL, SearchQuery.QueryOperator.LESS_THAN_OR_EQUAL_TO);
        this.boostOperatorMapping.put(AsBoostOperator.MATCH, SearchQuery.QueryOperator.MATCHES);
    }


    protected void populateBoostTypeMapping()
    {
        this.boostTypeMapping.put(AsBoostType.ADDITIVE, BoostField.BoostType.ADDITIVE);
        this.boostTypeMapping.put(AsBoostType.MULTIPLICATIVE, BoostField.BoostType.MULTIPLICATIVE);
    }


    protected BidiMap<AsFacetType, FacetType> getFacetTypeMapping()
    {
        return this.facetTypeMapping;
    }


    protected BidiMap<AsBoostOperator, SearchQuery.QueryOperator> getBoostOperatorMapping()
    {
        return this.boostOperatorMapping;
    }


    protected BidiMap<AsBoostType, BoostField.BoostType> getBoostTypeMapping()
    {
        return this.boostTypeMapping;
    }


    public FacetType toFacetType(AsFacetType asFacetType)
    {
        if(asFacetType == null)
        {
            return null;
        }
        return (FacetType)this.facetTypeMapping.get(asFacetType);
    }


    public AsFacetType toAsFacetType(FacetType facetType)
    {
        if(facetType == null)
        {
            return null;
        }
        return (AsFacetType)this.facetTypeMapping.inverseBidiMap().get(facetType);
    }


    public SearchQuery.QueryOperator toQueryOperator(AsBoostOperator asBoostOperator)
    {
        if(asBoostOperator == null)
        {
            return null;
        }
        return (SearchQuery.QueryOperator)this.boostOperatorMapping.get(asBoostOperator);
    }


    public AsBoostOperator toAsBoostOperator(SearchQuery.QueryOperator boostOperator)
    {
        if(boostOperator == null)
        {
            return null;
        }
        return (AsBoostOperator)this.boostOperatorMapping.inverseBidiMap().get(boostOperator);
    }


    public BoostField.BoostType toBoostType(AsBoostType asBoostType)
    {
        if(asBoostType == null)
        {
            return null;
        }
        return (BoostField.BoostType)this.boostTypeMapping.get(asBoostType);
    }


    public AsBoostType toAsBoostType(BoostField.BoostType boostType)
    {
        if(boostType == null)
        {
            return null;
        }
        return (AsBoostType)this.boostTypeMapping.inverseBidiMap().get(boostType);
    }
}
