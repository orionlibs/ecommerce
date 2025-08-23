package de.hybris.platform.adaptivesearchbackoffice.actions.facets;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.ReferenceActionUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import javax.annotation.Resource;

public class RankFacetDownAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AbstractEditorData, Object>
{
    @Resource
    private AsConfigurationService asConfigurationService;


    public ActionResult<Object> perform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        AbstractAsFacetConfigurationModel facet = (AbstractAsFacetConfigurationModel)data.getModel();
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = (AbstractAsConfigurableSearchConfigurationModel)ReferenceActionUtils.resolveCurrentObject(ctx, AbstractAsConfigurableSearchConfigurationModel.class);
        if(facet instanceof de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel)
        {
            this.asConfigurationService.rerankConfiguration((AbstractAsConfigurationModel)searchConfiguration, "promotedFacets", facet.getUid(), 1);
        }
        else if(facet instanceof de.hybris.platform.adaptivesearch.model.AsFacetModel)
        {
            this.asConfigurationService.rerankConfiguration((AbstractAsConfigurationModel)searchConfiguration, "facets", facet.getUid(), 1);
        }
        else if(facet instanceof de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel)
        {
            this.asConfigurationService.rerankConfiguration((AbstractAsConfigurationModel)searchConfiguration, "excludedFacets", facet.getUid(), 1);
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
        if(data == null || !data.isRankDownAllowed())
        {
            return false;
        }
        Object model = data.getModel();
        if(!(model instanceof de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel) && !(model instanceof de.hybris.platform.adaptivesearch.model.AsFacetModel) && !(model instanceof de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel))
        {
            return false;
        }
        return data.isFromSearchConfiguration();
    }
}
