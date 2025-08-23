package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.common.DataProvider;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.lang.Objects;

public class AsIndexPropertyDataProvider implements DataProvider<AsIndexPropertyData, String>
{
    protected static final String INDEX_TYPE = "indexType";
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsIndexPropertyData> getData(Map<String, Object> parameters)
    {
        String indexType = resolveIndexType(parameters);
        if(StringUtils.isBlank(indexType))
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        return searchProvider.getIndexProperties(indexType);
    }


    public String getValue(AsIndexPropertyData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return null;
        }
        return data.getCode();
    }


    public String getLabel(AsIndexPropertyData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return "";
        }
        return data.getCode();
    }


    protected String resolveIndexType(Map<String, Object> parameters)
    {
        return Objects.toString(parameters.get("indexType"));
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
