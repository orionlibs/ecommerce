package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.common.DataProvider;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsQueryContextDataProvider implements DataProvider<String, String>
{
    protected static final String INDEX_TYPE = "indexType";
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<String> getData(Map<String, Object> parameters)
    {
        AbstractAsSearchProfileModel searchProfile = resolveSearchProfile(parameters);
        String indexType = searchProfile.getIndexType();
        if(StringUtils.isBlank(indexType))
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        return searchProvider.getSupportedQueryContexts(indexType);
    }


    public String getValue(String data, Map<String, Object> parameters)
    {
        return data;
    }


    public String getLabel(String data, Map<String, Object> parameters)
    {
        return data;
    }


    protected AbstractAsSearchProfileModel resolveSearchProfile(Map<String, Object> parameters)
    {
        Object searchProfile = parameters.get("parentObject");
        if(!(searchProfile instanceof AbstractAsSearchProfileModel))
        {
            throw new EditorRuntimeException("Search profile not valid");
        }
        return (AbstractAsSearchProfileModel)searchProfile;
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
