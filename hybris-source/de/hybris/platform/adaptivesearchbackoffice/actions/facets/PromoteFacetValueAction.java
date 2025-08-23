package de.hybris.platform.adaptivesearchbackoffice.actions.facets;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetValueModel;
import de.hybris.platform.adaptivesearchbackoffice.editors.facets.FacetValueModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

public class PromoteFacetValueAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<FacetValueModel, Object>
{
    protected static final String PROMOTED_OBJECT_EXPRESSION = "currentObject.promotedValues";
    @Resource
    private ModelService modelService;


    public ActionResult<Object> perform(ActionContext<FacetValueModel> ctx)
    {
        AsPromotedFacetValueModel promotedFacetValue;
        FacetValueModel facetValue = (FacetValueModel)ctx.getData();
        AbstractAsFacetConfigurationModel facetConfiguration = resolveCurrentObject(ctx);
        if(facetValue.getModel() instanceof AsPromotedFacetValueModel)
        {
            promotedFacetValue = (AsPromotedFacetValueModel)facetValue.getModel();
        }
        else if(facetValue.getModel() instanceof de.hybris.platform.adaptivesearch.model.AsExcludedFacetValueModel)
        {
            promotedFacetValue = (AsPromotedFacetValueModel)this.modelService.clone(facetValue.getModel(), AsPromotedFacetValueModel.class);
        }
        else
        {
            promotedFacetValue = (AsPromotedFacetValueModel)this.modelService.create(AsPromotedFacetValueModel.class);
            promotedFacetValue.setFacetConfiguration(facetConfiguration);
            promotedFacetValue.setCatalogVersion(facetConfiguration.getCatalogVersion());
            promotedFacetValue.setValue(facetValue.getData().getValue());
        }
        List<AsPromotedFacetValueModel> promotedValues = new ArrayList<>(facetConfiguration.getPromotedValues());
        promotedValues.add(promotedFacetValue);
        updateCurrentObject(ctx, promotedValues);
        return new ActionResult("success");
    }


    protected AbstractAsFacetConfigurationModel resolveCurrentObject(ActionContext<FacetValueModel> ctx)
    {
        WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)ctx.getParameter("widgetInstanceManager");
        return (AbstractAsFacetConfigurationModel)widgetInstanceManager.getModel().getValue("currentObject", AbstractAsFacetConfigurationModel.class);
    }


    protected void updateCurrentObject(ActionContext<FacetValueModel> ctx, List<AsPromotedFacetValueModel> promotedValues)
    {
        WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)ctx.getParameter("widgetInstanceManager");
        widgetInstanceManager.getModel().setValue("currentObject.promotedValues", promotedValues);
    }
}
