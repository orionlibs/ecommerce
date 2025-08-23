package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
import de.hybris.platform.adaptivesearch.strategies.AsItemModelHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsItemModelHelper implements AsItemModelHelper
{
    private ModelService modelService;


    public String generateItemIdentifier(ItemModel item)
    {
        if(item == null)
        {
            return "null";
        }
        PK itemPk = item.getItemModelContext().isNew() ? getNewPkForNotSavedItem((AbstractItemModel)item) : item.getPk();
        if(itemPk == null)
        {
            throw new IllegalStateException("Could not generate identifier for item with unknown pk");
        }
        return itemPk.getLongValueAsString();
    }


    protected static PK getNewPkForNotSavedItem(AbstractItemModel item)
    {
        ItemModelInternalContext context = (ItemModelInternalContext)item.getItemModelContext();
        PK newPK = context.getNewPK();
        return (newPK == null) ? context.generateNewPK() : newPK;
    }


    public String decorateIdentifier(String identifier)
    {
        if(identifier == null)
        {
            return "null";
        }
        return identifier;
    }


    public AbstractAsSearchProfileModel getSearchProfileForSearchConfiguration(AbstractAsSearchConfigurationModel searchConfiguration)
    {
        return (AbstractAsSearchProfileModel)this.modelService.getAttributeValue(searchConfiguration, "searchProfile");
    }


    public AbstractAsSearchConfigurationModel getSearchConfigurationForFacetConfiguration(AbstractAsFacetConfigurationModel facetConfiguration)
    {
        return (AbstractAsSearchConfigurationModel)this.modelService.getAttributeValue(facetConfiguration, "searchConfiguration");
    }


    public AbstractAsSearchConfigurationModel getSearchConfigurationForBoostItemConfiguration(AbstractAsBoostItemConfigurationModel boostItemConfiguration)
    {
        return (AbstractAsSearchConfigurationModel)this.modelService.getAttributeValue(boostItemConfiguration, "searchConfiguration");
    }


    public AbstractAsSearchConfigurationModel getSearchConfigurationForSortConfiguration(AbstractAsSortConfigurationModel sortConfiguration)
    {
        return (AbstractAsSearchConfigurationModel)this.modelService.getAttributeValue(sortConfiguration, "searchConfiguration");
    }


    public AbstractAsFacetConfigurationModel getFacetConfigurationForFacetValueConfiguration(AbstractAsFacetValueConfigurationModel facetValueConfiguration)
    {
        return (AbstractAsFacetConfigurationModel)getModelService().getAttributeValue(facetValueConfiguration, "facetConfiguration");
    }


    public String generateCategoryAwareSearchConfigurationUniqueIdx(AsCategoryAwareSearchConfigurationModel searchConfiguration)
    {
        AbstractAsSearchProfileModel searchProfile = getSearchProfileForSearchConfiguration((AbstractAsSearchConfigurationModel)searchConfiguration);
        return generateItemIdentifier((ItemModel)searchProfile) + "_" + generateItemIdentifier((ItemModel)searchProfile);
    }


    public String generateSimpleSearchConfigurationUniqueIdx(AsSimpleSearchConfigurationModel searchConfiguration)
    {
        AbstractAsSearchProfileModel searchProfile = getSearchProfileForSearchConfiguration((AbstractAsSearchConfigurationModel)searchConfiguration);
        return generateItemIdentifier((ItemModel)searchProfile);
    }


    public String generateFacetConfigurationUniqueIdx(AbstractAsFacetConfigurationModel facetConfiguration)
    {
        AbstractAsSearchConfigurationModel searchConfiguration = getSearchConfigurationForFacetConfiguration(facetConfiguration);
        return generateItemIdentifier((ItemModel)searchConfiguration) + "_" + generateItemIdentifier((ItemModel)searchConfiguration);
    }


    public String generateFacetValueConfigurationUniqueIdx(AbstractAsFacetValueConfigurationModel facetValueConfiguration)
    {
        AbstractAsFacetConfigurationModel facetConfiguration = getFacetConfigurationForFacetValueConfiguration(facetValueConfiguration);
        return generateItemIdentifier((ItemModel)facetConfiguration) + "_" + generateItemIdentifier((ItemModel)facetConfiguration);
    }


    public String generateFacetRangeUniqueIdx(AsFacetRangeModel facetRange)
    {
        AbstractAsFacetConfigurationModel facetConfiguration = facetRange.getFacetConfiguration();
        return generateItemIdentifier((ItemModel)facetConfiguration) + "_" + generateItemIdentifier((ItemModel)facetConfiguration);
    }


    public String generateBoostItemConfigurationUniqueIdx(AbstractAsBoostItemConfigurationModel boostItemConfiguration)
    {
        AbstractAsSearchConfigurationModel searchConfiguration = getSearchConfigurationForBoostItemConfiguration(boostItemConfiguration);
        return generateItemIdentifier((ItemModel)searchConfiguration) + "_" + generateItemIdentifier((ItemModel)searchConfiguration);
    }


    public String generateSortConfigurationUniqueIdx(AbstractAsSortConfigurationModel sortConfiguration)
    {
        AbstractAsSearchConfigurationModel searchConfiguration = getSearchConfigurationForSortConfiguration(sortConfiguration);
        return generateItemIdentifier((ItemModel)searchConfiguration) + "_" + generateItemIdentifier((ItemModel)searchConfiguration);
    }


    public String generateSortExpressionUniqueIdx(AsSortExpressionModel sortExpression)
    {
        AbstractAsSortConfigurationModel sortConfiguration = sortExpression.getSortConfiguration();
        return generateItemIdentifier((ItemModel)sortConfiguration) + "_" + generateItemIdentifier((ItemModel)sortConfiguration);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
