package de.hybris.platform.solrfacetsearch.attribute;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class DefaultSolrServerConfigVersionProvider implements DynamicAttributeHandler<String, SolrServerConfigModel>
{
    protected static final String SEPARATOR = "_";


    public String get(SolrServerConfigModel model)
    {
        return generateVersion(model);
    }


    public void set(SolrServerConfigModel model, String value)
    {
        throw new UnsupportedOperationException("The attribute is readonly");
    }


    public String generateVersion(SolrServerConfigModel solrServerConfig)
    {
        StringBuilder version = new StringBuilder();
        version.append(convertDate(solrServerConfig.getModifiedtime()));
        if(CollectionUtils.isNotEmpty(solrServerConfig.getSolrEndpointUrls()))
        {
            version.append("_");
            version.append(solrServerConfig.getSolrEndpointUrls().stream().map(endpoint -> convertDate(endpoint.getModifiedtime()))
                            .sorted().collect(Collectors.joining("_")));
        }
        return version.toString();
    }


    protected String convertDate(Date date)
    {
        return String.valueOf(date.getTime());
    }
}
