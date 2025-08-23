package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.common.ConfigurationUtils;
import de.hybris.platform.solrfacetsearch.config.EndpointURL;
import de.hybris.platform.solrfacetsearch.config.QueryMethod;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import de.hybris.platform.solrfacetsearch.model.config.SolrEndpointUrlModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrServerConfigPopulator implements Populator<SolrServerConfigModel, SolrConfig>
{
    private Converter<SolrServerConfigModel, SolrClientConfig> solrClientConfigConverter;
    private Converter<SolrServerConfigModel, SolrClientConfig> solrIndexingClientConfigConverter;
    private Converter<SolrEndpointUrlModel, EndpointURL> endpointUrlConverter;


    public void populate(SolrServerConfigModel source, SolrConfig target)
    {
        target.setName(source.getName());
        target.setMode(populateConfigServerMode(source));
        target.setEndpointURLs(populateEndpointUrls(source));
        target.setClientConfig(populateClientConfig(source));
        target.setIndexingClientConfig(populateIndexingClientConfig(source));
        target.setUseMasterNodeExclusivelyForIndexing(populateConfigUseMasterNodeExclusivelyForIndexing(source));
        target.setNumShards(populateConfigNumShards(source));
        target.setReplicationFactor(populateReplicationFactor(source));
        target.setAutoAddReplicas(populateAutoAddReplicas(source));
        target.setQueryMethod(
                        (source.getSolrQueryMethod() == null) ? null : QueryMethod.valueOf(source.getSolrQueryMethod().toString()));
        target.setModifiedTime(source.getModifiedtime());
        target.setVersion(source.getVersion());
    }


    protected boolean populateConfigUseMasterNodeExclusivelyForIndexing(SolrServerConfigModel source)
    {
        Object value = ConfigurationUtils.getObject((ItemModel)source, "useMasterNodeExclusivelyForIndexing", "solr.config.%s.useMasterNodeExclusivelyForIndexing", new Object[] {source
                        .getName()});
        if(value instanceof Boolean)
        {
            return ((Boolean)value).booleanValue();
        }
        if(value instanceof String)
        {
            return Boolean.valueOf((String)value).booleanValue();
        }
        return false;
    }


    protected Integer populateConfigNumShards(SolrServerConfigModel source)
    {
        Object value = ConfigurationUtils.getObject((ItemModel)source, "numShards", "solr.config.%s.numShards", new Object[] {source
                        .getName()});
        if(value instanceof Integer)
        {
            return (Integer)value;
        }
        if(value instanceof String)
        {
            return Integer.valueOf((String)value);
        }
        return null;
    }


    protected Integer populateReplicationFactor(SolrServerConfigModel source)
    {
        Object value = ConfigurationUtils.getObject((ItemModel)source, "replicationFactor", "solr.config.%s.replicationFactor", new Object[] {source
                        .getName()});
        if(value instanceof Integer)
        {
            return (Integer)value;
        }
        if(value instanceof String)
        {
            return Integer.valueOf((String)value);
        }
        return null;
    }


    protected boolean populateAutoAddReplicas(SolrServerConfigModel source)
    {
        Object value = ConfigurationUtils.getObject((ItemModel)source, "autoAddReplicas", "solr.config.%s.autoAddReplicas", new Object[] {source
                        .getName()});
        if(value instanceof Boolean)
        {
            return ((Boolean)value).booleanValue();
        }
        if(value instanceof String)
        {
            return Boolean.valueOf((String)value).booleanValue();
        }
        return false;
    }


    protected SolrServerMode populateConfigServerMode(SolrServerConfigModel source)
    {
        Object value = ConfigurationUtils.getObject((ItemModel)source, "mode", "solr.config.%s.mode", new Object[] {source
                        .getName()});
        if(value instanceof SolrServerModes)
        {
            return SolrServerMode.valueOf(((SolrServerModes)value).name());
        }
        if(value instanceof String)
        {
            return SolrServerMode.valueOf(((String)value).toUpperCase(Locale.ROOT));
        }
        return null;
    }


    protected List<EndpointURL> populateEndpointUrls(SolrServerConfigModel source)
    {
        Object value = ConfigurationUtils.getObject((ItemModel)source, "solrEndpointUrls", "solr.config.%s.urls", new Object[] {source
                        .getName()});
        if(value instanceof String)
        {
            Date modifiedTime = new Date();
            List<EndpointURL> urls = new ArrayList<>();
            String[] urlsFromProperty = ((String)value).trim().split("[\\s,]+");
            if(urlsFromProperty.length == 0)
            {
                return Collections.emptyList();
            }
            urls.add(createEndpoint(urlsFromProperty[0], true, modifiedTime));
            for(int i = 1; i < urlsFromProperty.length; i++)
            {
                urls.add(createEndpoint(urlsFromProperty[i], false, modifiedTime));
            }
            return urls;
        }
        return Converters.convertAll((List)value, this.endpointUrlConverter);
    }


    protected EndpointURL createEndpoint(String url, boolean isMaster, Date modifiedTime)
    {
        EndpointURL result = new EndpointURL();
        result.setMaster(isMaster);
        result.setUrl(url);
        result.setModifiedTime(modifiedTime);
        return result;
    }


    protected SolrClientConfig populateClientConfig(SolrServerConfigModel source)
    {
        return (SolrClientConfig)this.solrClientConfigConverter.convert(source);
    }


    protected SolrClientConfig populateIndexingClientConfig(SolrServerConfigModel source)
    {
        return (SolrClientConfig)this.solrIndexingClientConfigConverter.convert(source);
    }


    public Converter<SolrServerConfigModel, SolrClientConfig> getSolrClientConfigConverter()
    {
        return this.solrClientConfigConverter;
    }


    @Required
    public void setSolrClientConfigConverter(Converter<SolrServerConfigModel, SolrClientConfig> solrClientConfigConverter)
    {
        this.solrClientConfigConverter = solrClientConfigConverter;
    }


    public Converter<SolrServerConfigModel, SolrClientConfig> getSolrIndexingClientConfigConverter()
    {
        return this.solrIndexingClientConfigConverter;
    }


    @Required
    public void setSolrIndexingClientConfigConverter(Converter<SolrServerConfigModel, SolrClientConfig> solrIndexingClientConfigConverter)
    {
        this.solrIndexingClientConfigConverter = solrIndexingClientConfigConverter;
    }


    @Required
    public void setEndpointUrlConverter(Converter<SolrEndpointUrlModel, EndpointURL> endpointUrlConverter)
    {
        this.endpointUrlConverter = endpointUrlConverter;
    }
}
