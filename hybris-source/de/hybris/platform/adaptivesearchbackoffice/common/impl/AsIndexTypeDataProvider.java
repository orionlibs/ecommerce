package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.common.DataProvider;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.lang.Objects;

public class AsIndexTypeDataProvider implements DataProvider<AsIndexTypeData, String>
{
    private static final String INDEX_CONFIGURATION = "indexConfiguration";
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsIndexTypeData> getData(Map<String, Object> parameters)
    {
        List<AsIndexTypeData> indexTypes;
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        String indexConfiguration = resolveIndexConfiguration(parameters);
        if(StringUtils.isBlank(indexConfiguration))
        {
            indexTypes = searchProvider.getIndexTypes();
        }
        else
        {
            indexTypes = searchProvider.getIndexTypes(indexConfiguration);
        }
        Collections.sort(indexTypes, this::compareIndexTypes);
        return indexTypes;
    }


    public String getValue(AsIndexTypeData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return null;
        }
        return data.getCode();
    }


    public String getLabel(AsIndexTypeData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return "";
        }
        return data.getCode();
    }


    protected String resolveIndexConfiguration(Map<String, Object> parameters)
    {
        return Objects.toString(parameters.get("indexConfiguration"));
    }


    protected int compareIndexTypes(AsIndexTypeData indexType1, AsIndexTypeData indexType2)
    {
        return indexType1.getCode().compareTo(indexType2.getCode());
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
