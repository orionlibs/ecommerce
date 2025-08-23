package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsFacetExpressionDataProvider extends AbstractAsDataProvider<AsExpressionData, String>
{
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsExpressionData> getData(Map<String, Object> parameters)
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
        List<AsExpressionData> expressions = searchProvider.getSupportedFacetExpressions(indexType);
        Set<String> usedExpressions = collectUsedExpressions(searchConfiguration);
        if(StringUtils.isNotBlank(facetConfiguration.getIndexProperty()))
        {
            usedExpressions.remove(facetConfiguration.getIndexProperty());
        }
        return (List<AsExpressionData>)expressions.stream().filter(expression -> !usedExpressions.contains(expression.getExpression()))
                        .collect(Collectors.toList());
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


    protected Set<String> collectUsedExpressions(AbstractAsConfigurableSearchConfigurationModel searchConfiguration)
    {
        Set<String> expressions = new HashSet<>();
        if(CollectionUtils.isNotEmpty(searchConfiguration.getPromotedFacets()))
        {
            expressions.addAll((Collection<? extends String>)searchConfiguration.getPromotedFacets().stream()
                            .map(AbstractAsFacetConfigurationModel::getIndexProperty).collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(searchConfiguration.getFacets()))
        {
            expressions.addAll((Collection<? extends String>)searchConfiguration.getFacets().stream().map(AbstractAsFacetConfigurationModel::getIndexProperty)
                            .collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(searchConfiguration.getExcludedFacets()))
        {
            expressions.addAll((Collection<? extends String>)searchConfiguration.getExcludedFacets().stream()
                            .map(AbstractAsFacetConfigurationModel::getIndexProperty).collect(Collectors.toList()));
        }
        return expressions;
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
