package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

public class DefaultFacetSearchConfigPopulator implements Populator<SolrFacetSearchConfigModel, FacetSearchConfig>
{
    private Converter<SolrSearchConfigModel, SearchConfig> solrSearchConfigConverter;
    private Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter;
    private Converter<SolrFacetSearchConfigModel, IndexConfig> indexConfigConverter;


    public void populate(SolrFacetSearchConfigModel source, FacetSearchConfig target)
    {
        try
        {
            target.setDescription(source.getDescription());
            target.setIndexConfig(getIndexConfigFromItems(source));
            target.setName(source.getName());
            target.setSearchConfig(getSearchConfig(source.getSolrSearchConfig()));
            target.setSolrConfig(getSolrConfigFromItems(source.getSolrServerConfig()));
        }
        catch(Exception ex)
        {
            throw new ConversionException("Cannot convert facet search config", ex);
        }
    }


    protected SearchConfig getSearchConfig(SolrSearchConfigModel searchConfigModel)
    {
        return (SearchConfig)this.solrSearchConfigConverter.convert(searchConfigModel);
    }


    protected SolrConfig getSolrConfigFromItems(SolrServerConfigModel itemConfig)
    {
        return (SolrConfig)this.solrServerConfigConverter.convert(itemConfig);
    }


    protected IndexConfig getIndexConfigFromItems(SolrFacetSearchConfigModel configModel)
    {
        return (IndexConfig)this.indexConfigConverter.convert(configModel);
    }


    public void setSolrSearchConfigConverter(Converter<SolrSearchConfigModel, SearchConfig> solrSearchConfigConverter)
    {
        this.solrSearchConfigConverter = solrSearchConfigConverter;
    }


    public void setSolrServerConfigConverter(Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter)
    {
        this.solrServerConfigConverter = solrServerConfigConverter;
    }


    public void setIndexConfigConverter(Converter<SolrFacetSearchConfigModel, IndexConfig> indexConfigConverter)
    {
        this.indexConfigConverter = indexConfigConverter;
    }
}
