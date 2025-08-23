package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsGroupExpressionDataProvider extends AbstractAsDataProvider<AsExpressionData, String>
{
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsExpressionData> getData(Map<String, Object> parameters)
    {
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = resolveSearchConfiguration(parameters);
        AbstractAsSearchProfileModel searchProfile = resolveSearchProfile(searchConfiguration);
        String indexType = searchProfile.getIndexType();
        if(StringUtils.isBlank(indexType))
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        return searchProvider.getSupportedGroupExpressions(indexType);
    }


    public String getValue(AsExpressionData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return null;
        }
        return data.getExpression();
    }


    public String getLabel(AsExpressionData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return "";
        }
        return data.getExpression();
    }


    protected AbstractAsConfigurableSearchConfigurationModel resolveSearchConfiguration(Map<String, Object> parameters)
    {
        Object searchConfiguration = parameters.get("parentObject");
        if(!(searchConfiguration instanceof AbstractAsConfigurableSearchConfigurationModel))
        {
            throw new EditorRuntimeException("Search configuration not valid");
        }
        return (AbstractAsConfigurableSearchConfigurationModel)searchConfiguration;
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
