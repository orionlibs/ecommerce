package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.converters.AsSearchConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AsBoostRule;
import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacet;
import de.hybris.platform.adaptivesearch.data.AsExcludedItem;
import de.hybris.platform.adaptivesearch.data.AsExcludedSort;
import de.hybris.platform.adaptivesearch.data.AsFacet;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacet;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.data.AsPromotedSort;
import de.hybris.platform.adaptivesearch.data.AsSort;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedSortModel;
import de.hybris.platform.adaptivesearch.model.AsFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedSortModel;
import de.hybris.platform.adaptivesearch.model.AsSortModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class AsConfigurableSearchConfigurationPopulator implements ContextAwarePopulator<AbstractAsConfigurableSearchConfigurationModel, AsConfigurableSearchConfiguration, AsSearchConfigurationConverterContext>
{
    private ModelService modelService;
    private ContextAwareConverter<AsPromotedFacetModel, AsPromotedFacet, AsItemConfigurationConverterContext> asPromotedFacetConverter;
    private ContextAwareConverter<AsFacetModel, AsFacet, AsItemConfigurationConverterContext> asFacetConverter;
    private ContextAwareConverter<AsExcludedFacetModel, AsExcludedFacet, AsItemConfigurationConverterContext> asExcludedFacetConverter;
    private ContextAwareConverter<AsPromotedItemModel, AsPromotedItem, AsItemConfigurationConverterContext> asPromotedItemConverter;
    private ContextAwareConverter<AsExcludedItemModel, AsExcludedItem, AsItemConfigurationConverterContext> asExcludedItemConverter;
    private ContextAwareConverter<AsBoostRuleModel, AsBoostRule, AsItemConfigurationConverterContext> asBoostRuleConverter;
    private ContextAwareConverter<AsPromotedSortModel, AsPromotedSort, AsItemConfigurationConverterContext> asPromotedSortConverter;
    private ContextAwareConverter<AsSortModel, AsSort, AsItemConfigurationConverterContext> asSortConverter;
    private ContextAwareConverter<AsExcludedSortModel, AsExcludedSort, AsItemConfigurationConverterContext> asExcludedSortConverter;


    public void populate(AbstractAsConfigurableSearchConfigurationModel source, AsConfigurableSearchConfiguration target, AsSearchConfigurationConverterContext context)
    {
        AsItemConfigurationConverterContext childContext = new AsItemConfigurationConverterContext();
        childContext.setSearchProfileCode(context.getSearchProfileCode());
        childContext.setSearchConfigurationUid(source.getUid());
        target.setFacetsMergeMode(source.getFacetsMergeMode());
        target.setPromotedFacets(convertAll(this.asPromotedFacetConverter, source.getPromotedFacets(), childContext));
        target.setFacets(convertAll(this.asFacetConverter, source.getFacets(), childContext));
        target.setExcludedFacets(convertAll(this.asExcludedFacetConverter, source.getExcludedFacets(), childContext));
        target.setBoostItemsMergeMode(source.getBoostItemsMergeMode());
        target.setPromotedItems(convertAll(this.asPromotedItemConverter, source.getPromotedItems(), childContext));
        target.setExcludedItems(convertAll(this.asExcludedItemConverter, source.getExcludedItems(), childContext));
        target.setBoostRulesMergeMode(source.getBoostRulesMergeMode());
        target.setBoostRules(convertAll(this.asBoostRuleConverter, source.getBoostRules(), childContext));
        target.setSortsMergeMode(source.getSortsMergeMode());
        target.setPromotedSorts(convertAll(this.asPromotedSortConverter, source.getPromotedSorts(), childContext));
        target.setSorts(convertAll(this.asSortConverter, source.getSorts(), childContext));
        target.setExcludedSorts(convertAll(this.asExcludedSortConverter, source.getExcludedSorts(), childContext));
        target.setGroupMergeMode(source.getGroupMergeMode());
        target.setGroupExpression(source.getGroupExpression());
        target.setGroupLimit(source.getGroupLimit());
    }


    public static <S extends AbstractAsConfigurationModel, T, C> List<T> convertAll(ContextAwareConverter<S, T, C> converter, List<? extends S> sources, C childContext)
    {
        return (List<T>)sources.stream().filter(configuration -> !configuration.isCorrupted())
                        .map(source -> converter.convert(source, childContext)).collect(Collectors.toList());
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


    public ContextAwareConverter<AsPromotedFacetModel, AsPromotedFacet, AsItemConfigurationConverterContext> getAsPromotedFacetConverter()
    {
        return this.asPromotedFacetConverter;
    }


    @Required
    public void setAsPromotedFacetConverter(ContextAwareConverter<AsPromotedFacetModel, AsPromotedFacet, AsItemConfigurationConverterContext> asPromotedFacetConverter)
    {
        this.asPromotedFacetConverter = asPromotedFacetConverter;
    }


    public ContextAwareConverter<AsFacetModel, AsFacet, AsItemConfigurationConverterContext> getAsFacetConverter()
    {
        return this.asFacetConverter;
    }


    @Required
    public void setAsFacetConverter(ContextAwareConverter<AsFacetModel, AsFacet, AsItemConfigurationConverterContext> asFacetConverter)
    {
        this.asFacetConverter = asFacetConverter;
    }


    public ContextAwareConverter<AsExcludedFacetModel, AsExcludedFacet, AsItemConfigurationConverterContext> getAsExcludedFacetConverter()
    {
        return this.asExcludedFacetConverter;
    }


    @Required
    public void setAsExcludedFacetConverter(ContextAwareConverter<AsExcludedFacetModel, AsExcludedFacet, AsItemConfigurationConverterContext> asExcludedFacetConverter)
    {
        this.asExcludedFacetConverter = asExcludedFacetConverter;
    }


    public ContextAwareConverter<AsPromotedItemModel, AsPromotedItem, AsItemConfigurationConverterContext> getAsPromotedItemConverter()
    {
        return this.asPromotedItemConverter;
    }


    @Required
    public void setAsPromotedItemConverter(ContextAwareConverter<AsPromotedItemModel, AsPromotedItem, AsItemConfigurationConverterContext> asPromotedItemConverter)
    {
        this.asPromotedItemConverter = asPromotedItemConverter;
    }


    public ContextAwareConverter<AsExcludedItemModel, AsExcludedItem, AsItemConfigurationConverterContext> getAsExcludedItemConverter()
    {
        return this.asExcludedItemConverter;
    }


    @Required
    public void setAsExcludedItemConverter(ContextAwareConverter<AsExcludedItemModel, AsExcludedItem, AsItemConfigurationConverterContext> asExcludedItemConverter)
    {
        this.asExcludedItemConverter = asExcludedItemConverter;
    }


    public ContextAwareConverter<AsBoostRuleModel, AsBoostRule, AsItemConfigurationConverterContext> getAsBoostRuleConverter()
    {
        return this.asBoostRuleConverter;
    }


    @Required
    public void setAsBoostRuleConverter(ContextAwareConverter<AsBoostRuleModel, AsBoostRule, AsItemConfigurationConverterContext> asBoostRuleConverter)
    {
        this.asBoostRuleConverter = asBoostRuleConverter;
    }


    public ContextAwareConverter<AsPromotedSortModel, AsPromotedSort, AsItemConfigurationConverterContext> getAsPromotedSortConverter()
    {
        return this.asPromotedSortConverter;
    }


    @Required
    public void setAsPromotedSortConverter(ContextAwareConverter<AsPromotedSortModel, AsPromotedSort, AsItemConfigurationConverterContext> asPromotedSortConverter)
    {
        this.asPromotedSortConverter = asPromotedSortConverter;
    }


    public ContextAwareConverter<AsSortModel, AsSort, AsItemConfigurationConverterContext> getAsSortConverter()
    {
        return this.asSortConverter;
    }


    @Required
    public void setAsSortConverter(ContextAwareConverter<AsSortModel, AsSort, AsItemConfigurationConverterContext> asSortConverter)
    {
        this.asSortConverter = asSortConverter;
    }


    public ContextAwareConverter<AsExcludedSortModel, AsExcludedSort, AsItemConfigurationConverterContext> getAsExcludedSortConverter()
    {
        return this.asExcludedSortConverter;
    }


    @Required
    public void setAsExcludedSortConverter(ContextAwareConverter<AsExcludedSortModel, AsExcludedSort, AsItemConfigurationConverterContext> asExcludedSortConverter)
    {
        this.asExcludedSortConverter = asExcludedSortConverter;
    }
}
