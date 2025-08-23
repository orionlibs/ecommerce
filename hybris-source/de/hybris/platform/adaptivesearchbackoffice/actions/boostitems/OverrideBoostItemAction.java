package de.hybris.platform.adaptivesearchbackoffice.actions.boostitems;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedItem;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.ReferenceActionUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractBoostItemConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;

public class OverrideBoostItemAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AbstractEditorData, Object>
{
    @Resource
    private ModelService modelService;
    @Resource
    private ContextAwareConverter<AsPromotedItem, AsPromotedItemModel, AsItemConfigurationReverseConverterContext> asPromotedItemReverseConverter;
    @Resource
    private ContextAwareConverter<AsExcludedItem, AsExcludedItemModel, AsItemConfigurationReverseConverterContext> asExcludedItemReverseConverter;


    public ActionResult<Object> perform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        AbstractAsBoostItemConfiguration boostItemConfiguration = ((AbstractBoostItemConfigurationEditorData)data).getBoostItemConfiguration();
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = (AbstractAsConfigurableSearchConfigurationModel)ReferenceActionUtils.resolveCurrentObject(ctx, AbstractAsConfigurableSearchConfigurationModel.class);
        AsItemConfigurationReverseConverterContext context = new AsItemConfigurationReverseConverterContext();
        context.setCatalogVersion(searchConfiguration.getCatalogVersion());
        context.setParentConfiguration((AbstractAsConfigurationModel)searchConfiguration);
        if(boostItemConfiguration instanceof AsPromotedItem)
        {
            AsPromotedItemModel promotedItem = (AsPromotedItemModel)this.asPromotedItemReverseConverter.convert(boostItemConfiguration, context);
            this.modelService.save(promotedItem);
        }
        else if(boostItemConfiguration instanceof AsExcludedItem)
        {
            AsExcludedItemModel excludedItem = (AsExcludedItemModel)this.asExcludedItemReverseConverter.convert(boostItemConfiguration, context);
            this.modelService.save(excludedItem);
        }
        else
        {
            return new ActionResult("error");
        }
        ReferenceActionUtils.refreshCurrentObject(ctx);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        if(data == null || data.isFromSearchConfiguration())
        {
            return false;
        }
        return (data instanceof AbstractBoostItemConfigurationEditorData && ((AbstractBoostItemConfigurationEditorData)data)
                        .getBoostItemConfiguration() != null);
    }
}
