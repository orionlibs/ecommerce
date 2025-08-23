package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.data.AsFacetSortData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsFacetSortDataProvider extends AbstractAsDataProvider<AsFacetSortData, String>
{
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsFacetSortData> getData(Map<String, Object> parameters)
    {
        AbstractAsFacetConfigurationModel facetConfiguration = resolveFacetConfiguration(parameters);
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = resolveSearchConfiguration(facetConfiguration);
        AbstractAsSearchProfileModel searchProfile = resolveSearchProfile(searchConfiguration);
        String indexType = searchProfile.getIndexType();
        if(StringUtils.isBlank(indexType))
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        return searchProvider.getSupportedFacetSorts(indexType);
    }


    public String getValue(AsFacetSortData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return null;
        }
        return data.getCode();
    }


    public String getLabel(AsFacetSortData data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return "";
        }
        return data.getName();
    }


    protected AbstractAsFacetConfigurationModel resolveFacetConfiguration(Map<String, Object> parameters)
    {
        Object facetConfiguration = parameters.get("parentObject");
        if(!(facetConfiguration instanceof AbstractAsFacetConfigurationModel))
        {
            throw new EditorRuntimeException("Facet configuration not valid");
        }
        return (AbstractAsFacetConfigurationModel)facetConfiguration;
    }


    protected AbstractAsConfigurableSearchConfigurationModel resolveSearchConfiguration(AbstractAsFacetConfigurationModel facetConfiguration)
    {
        Object searchConfiguration = getModelService().getAttributeValue(facetConfiguration, "searchConfiguration");
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
