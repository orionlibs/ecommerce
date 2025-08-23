package de.hybris.platform.adaptivesearchbackoffice.actions.sorts;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.ReferenceActionUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import javax.annotation.Resource;

public class PromoteSortAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AbstractEditorData, Object>
{
    @Resource
    private AsConfigurationService asConfigurationService;


    public ActionResult<Object> perform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        AbstractAsSortConfigurationModel sort = (AbstractAsSortConfigurationModel)data.getModel();
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = (AbstractAsConfigurableSearchConfigurationModel)ReferenceActionUtils.resolveCurrentObject(ctx, AbstractAsConfigurableSearchConfigurationModel.class);
        if(sort instanceof de.hybris.platform.adaptivesearch.model.AsSortModel)
        {
            this.asConfigurationService.moveConfiguration((AbstractAsConfigurationModel)searchConfiguration, "sorts", "promotedSorts", sort.getUid());
        }
        else if(sort instanceof de.hybris.platform.adaptivesearch.model.AsExcludedSortModel)
        {
            this.asConfigurationService.moveConfiguration((AbstractAsConfigurationModel)searchConfiguration, "excludedSorts", "promotedSorts", sort.getUid());
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
        if(data == null)
        {
            return false;
        }
        Object model = data.getModel();
        if(!(model instanceof de.hybris.platform.adaptivesearch.model.AsSortModel) && !(model instanceof de.hybris.platform.adaptivesearch.model.AsExcludedSortModel))
        {
            return false;
        }
        return data.isFromSearchConfiguration();
    }
}
