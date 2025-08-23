package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
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

public class AsFacetTypeDataProvider extends AbstractAsDataProvider<AsFacetType, AsFacetType>
{
    protected static final String INDEX_TYPE_PARAM = "indexType";
    private LabelService labelService;
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsFacetType> getData(Map<String, Object> parameters)
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
        return searchProvider.getSupportedFacetTypes(indexType);
    }


    public AsFacetType getValue(AsFacetType data, Map<String, Object> parameters)
    {
        return data;
    }


    public String getLabel(AsFacetType data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return "";
        }
        return this.labelService.getObjectLabel(data);
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


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
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
