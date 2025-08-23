package de.hybris.platform.adaptivesearchbackoffice.actions.sorts;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedSort;
import de.hybris.platform.adaptivesearch.data.AsPromotedSort;
import de.hybris.platform.adaptivesearch.data.AsSort;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedSortModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedSortModel;
import de.hybris.platform.adaptivesearch.model.AsSortModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.ReferenceActionUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractSortConfigurationEditorData;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;

public class OverrideSortAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AbstractEditorData, Object>
{
    @Resource
    private ModelService modelService;
    @Resource
    private ContextAwareConverter<AsPromotedSort, AsPromotedSortModel, AsItemConfigurationReverseConverterContext> asPromotedSortReverseConverter;
    @Resource
    private ContextAwareConverter<AsSort, AsSortModel, AsItemConfigurationReverseConverterContext> asSortReverseConverter;
    @Resource
    private ContextAwareConverter<AsExcludedSort, AsExcludedSortModel, AsItemConfigurationReverseConverterContext> asExcludedSortReverseConverter;


    public ActionResult<Object> perform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        AbstractAsSortConfiguration sortConfiguration = ((AbstractSortConfigurationEditorData)data).getSortConfiguration();
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = (AbstractAsConfigurableSearchConfigurationModel)ReferenceActionUtils.resolveCurrentObject(ctx, AbstractAsConfigurableSearchConfigurationModel.class);
        AsItemConfigurationReverseConverterContext context = new AsItemConfigurationReverseConverterContext();
        context.setCatalogVersion(searchConfiguration.getCatalogVersion());
        context.setParentConfiguration((AbstractAsConfigurationModel)searchConfiguration);
        if(sortConfiguration instanceof AsPromotedSort)
        {
            AsPromotedSortModel promotedSort = (AsPromotedSortModel)this.asPromotedSortReverseConverter.convert(sortConfiguration, context);
            this.modelService.save(promotedSort);
        }
        else if(sortConfiguration instanceof AsSort)
        {
            AsSortModel sort = (AsSortModel)this.asSortReverseConverter.convert(sortConfiguration, context);
            this.modelService.save(sort);
        }
        else if(sortConfiguration instanceof AsExcludedSort)
        {
            AsExcludedSortModel excludedSort = (AsExcludedSortModel)this.asExcludedSortReverseConverter.convert(sortConfiguration, context);
            this.modelService.save(excludedSort);
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
        return (data instanceof AbstractSortConfigurationEditorData && ((AbstractSortConfigurationEditorData)data)
                        .getSortConfiguration() != null);
    }
}
