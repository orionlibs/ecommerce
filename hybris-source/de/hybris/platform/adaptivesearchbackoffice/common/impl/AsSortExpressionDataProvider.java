package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
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

public class AsSortExpressionDataProvider extends AbstractAsDataProvider<AsExpressionData, String>
{
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsExpressionData> getData(Map<String, Object> parameters)
    {
        AsSortExpressionModel sortExpression = resolveSortExpression(parameters);
        AbstractAsSortConfigurationModel sortConfiguration = resolveSortConfiguration(sortExpression);
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = resolveSearchConfiguration(sortConfiguration);
        AbstractAsSearchProfileModel searchProfile = resolveSearchProfile(searchConfiguration);
        String indexType = searchProfile.getIndexType();
        if(StringUtils.isBlank(indexType))
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        List<AsExpressionData> expressions = searchProvider.getSupportedSortExpressions(indexType);
        Set<String> usedExpressions = collectUsedExpressions(sortConfiguration);
        if(StringUtils.isNotBlank(sortExpression.getExpression()))
        {
            usedExpressions.remove(sortExpression.getExpression());
        }
        return (List<AsExpressionData>)expressions.stream().filter(expression -> !usedExpressions.contains(expression.getExpression()))
                        .collect(Collectors.toList());
    }


    protected Set<String> collectUsedExpressions(AbstractAsSortConfigurationModel sortConfiguration)
    {
        Set<String> expressions = new HashSet<>();
        if(CollectionUtils.isNotEmpty(sortConfiguration.getExpressions()))
        {
            expressions.addAll((Collection<? extends String>)sortConfiguration.getExpressions().stream().map(AsSortExpressionModel::getExpression)
                            .collect(Collectors.toList()));
        }
        return expressions;
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


    protected AsSortExpressionModel resolveSortExpression(Map<String, Object> parameters)
    {
        Object sortExpression = parameters.get("parentObject");
        if(!(sortExpression instanceof AsSortExpressionModel))
        {
            throw new EditorRuntimeException("Sort expression not valid");
        }
        return (AsSortExpressionModel)sortExpression;
    }


    protected AbstractAsSortConfigurationModel resolveSortConfiguration(AsSortExpressionModel sortExpression)
    {
        Object sortConfiguration = getModelService().getAttributeValue(sortExpression, "sortConfiguration");
        if(!(sortConfiguration instanceof AbstractAsSortConfigurationModel))
        {
            throw new EditorRuntimeException("Sort configuration not valid");
        }
        return (AbstractAsSortConfigurationModel)sortConfiguration;
    }


    protected AbstractAsConfigurableSearchConfigurationModel resolveSearchConfiguration(AbstractAsSortConfigurationModel sortConfiguration)
    {
        Object searchConfiguration = getModelService().getAttributeValue(sortConfiguration, "searchConfiguration");
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
