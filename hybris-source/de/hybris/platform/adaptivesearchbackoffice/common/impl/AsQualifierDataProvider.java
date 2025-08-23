package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.AsRuntimeException;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.common.DataProvider;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class AsQualifierDataProvider implements DataProvider<String, String>
{
    protected static final String INDEX_TYPE = "indexType";
    protected static final String INDEX_PROPERTY = "indexProperty";
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<String> getData(Map<String, Object> parameters)
    {
        try
        {
            AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
            String indexType = (String)parameters.get("indexType");
            String indexProperty = (String)parameters.get("indexProperty");
            if(indexType != null && indexProperty != null)
            {
                return searchProvider.getAvailableQualifiers(indexType, indexProperty);
            }
            return Collections.emptyList();
        }
        catch(AsException e)
        {
            throw new AsRuntimeException(e);
        }
    }


    public String getValue(String data, Map<String, Object> parameters)
    {
        return data;
    }


    public String getLabel(String data, Map<String, Object> parameters)
    {
        return data;
    }


    public AsSearchProviderFactory getAsSearchProviderFactory()
    {
        return this.asSearchProviderFactory;
    }


    @Required
    public void setAsSearchProviderFactory(AsSearchProviderFactory asSearchProviderFactory)
    {
        this.asSearchProviderFactory = asSearchProviderFactory;
    }
}
