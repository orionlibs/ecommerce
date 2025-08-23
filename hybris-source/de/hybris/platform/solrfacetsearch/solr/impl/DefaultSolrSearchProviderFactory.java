package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrSearchProviderFactory implements SolrSearchProviderFactory
{
    private SolrStandaloneSearchProvider solrStandaloneSearchProvider;
    private SolrCloudSearchProvider solrCloudSearchProvider;
    private XmlExportSearchProvider xmlExportSearchProvider;


    public SolrSearchProvider getSearchProvider(FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws SolrServiceException
    {
        SolrServerMode mode = facetSearchConfig.getSolrConfig().getMode();
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$config$SolrServerMode[mode.ordinal()])
        {
            case 1:
                return (SolrSearchProvider)this.solrStandaloneSearchProvider;
            case 2:
                return (SolrSearchProvider)this.solrCloudSearchProvider;
            case 3:
                return (SolrSearchProvider)this.xmlExportSearchProvider;
        }
        throw new SolrServiceException("A Solr Server mode of: " + mode + " is not supported. Please specify standalone in the solr configuration.");
    }


    public SolrCloudSearchProvider getSolrCloudSearchProvider()
    {
        return this.solrCloudSearchProvider;
    }


    @Required
    public void setSolrCloudSearchProvider(SolrCloudSearchProvider solrCloudSearchProvider)
    {
        this.solrCloudSearchProvider = solrCloudSearchProvider;
    }


    public SolrStandaloneSearchProvider getSolrStandaloneSearchProvider()
    {
        return this.solrStandaloneSearchProvider;
    }


    @Required
    public void setSolrStandaloneSearchProvider(SolrStandaloneSearchProvider solrStandaloneSearchProvider)
    {
        this.solrStandaloneSearchProvider = solrStandaloneSearchProvider;
    }


    public XmlExportSearchProvider getXmlExportSearchProvider()
    {
        return this.xmlExportSearchProvider;
    }


    @Required
    public void setXmlExportSearchProvider(XmlExportSearchProvider xmlExportSearchProvider)
    {
        this.xmlExportSearchProvider = xmlExportSearchProvider;
    }
}
