package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.util.ObjectConverter;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import java.util.Optional;

public class AsBoostRuleInterceptor extends AbstractAsInterceptor implements PrepareInterceptor<AsBoostRuleModel>, ValidateInterceptor<AsBoostRuleModel>, RemoveInterceptor<AsBoostRuleModel>
{
    public void onPrepare(AsBoostRuleModel boostRule, InterceptorContext context) throws InterceptorException
    {
        markItemAsModified(context, (ItemModel)boostRule, new String[] {"searchConfiguration", "searchProfile"});
    }


    public void onValidate(AsBoostRuleModel boostRule, InterceptorContext context) throws InterceptorException
    {
        CatalogVersionModel catalogVersion = boostRule.getCatalogVersion();
        AbstractAsConfigurableSearchConfigurationModel abstractAsConfigurableSearchConfigurationModel = boostRule.getSearchConfiguration();
        if(abstractAsConfigurableSearchConfigurationModel != null && !Objects.equals(catalogVersion, abstractAsConfigurableSearchConfigurationModel.getCatalogVersion()))
        {
            throw new InterceptorException("Invalid catalog version: " + catalogVersion
                            .getCatalog() + ":" + catalogVersion.getVersion());
        }
        AbstractAsSearchProfileModel searchProfile = getAsItemModelHelper().getSearchProfileForSearchConfiguration((AbstractAsSearchConfigurationModel)abstractAsConfigurableSearchConfigurationModel);
        if(searchProfile == null)
        {
            throw new InterceptorException("Invalid search profile");
        }
        String indexType = searchProfile.getIndexType();
        String indexProperty = boostRule.getIndexProperty();
        AsSearchProvider searchProvider = resolveSearchProvider();
        Optional<AsIndexPropertyData> indexPropertyDataOptional = searchProvider.getIndexPropertyForCode(indexType, indexProperty);
        if(indexPropertyDataOptional.isEmpty())
        {
            throw new InterceptorException("Index property does not exist: " + indexProperty);
        }
        AsBoostType boostType = boostRule.getBoostType();
        if(!searchProvider.isValidBoostType(indexType, boostType))
        {
            throw new InterceptorException("Boost type cannot be used for boost rules: " + boostType);
        }
        AsIndexPropertyData indexPropertyData = indexPropertyDataOptional.get();
        if(boostRule.getOperator() != null && !indexPropertyData.getSupportedBoostOperators().contains(boostRule.getOperator()))
        {
            throw new InterceptorException("Operator " + boostRule
                            .getOperator().getCode() + " is not supported for index property: " + indexProperty);
        }
        try
        {
            ObjectConverter.convert(boostRule.getValue(), indexPropertyData.getType());
        }
        catch(AsException e)
        {
            throw new InterceptorException("Value " + boostRule
                            .getValue() + " is not of type: " + indexPropertyData.getType().getName(), e);
        }
    }


    public void onRemove(AsBoostRuleModel boostRule, InterceptorContext context) throws InterceptorException
    {
        AbstractAsConfigurableSearchConfigurationModel abstractAsConfigurableSearchConfigurationModel = boostRule.getSearchConfiguration();
        markItemAsModified(context, (ItemModel)abstractAsConfigurableSearchConfigurationModel, new String[] {"searchProfile"});
    }
}
