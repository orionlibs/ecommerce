package de.hybris.platform.adaptivesearchbackoffice.actions.facets;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetValueModel;
import de.hybris.platform.adaptivesearchbackoffice.editors.facets.FacetValueModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

public class ExcludeFacetValueAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<FacetValueModel, Object>
{
    protected static final String EXCLUDED_OBJECT_EXPRESSION = "currentObject.excludedValues";
    @Resource
    private ModelService modelService;


    public ActionResult<Object> perform(ActionContext<FacetValueModel> ctx)
    {
        AsExcludedFacetValueModel excludedFacetValue;
        FacetValueModel facetValue = (FacetValueModel)ctx.getData();
        AbstractAsFacetConfigurationModel facetConfiguration = resolveCurrentObject(ctx);
        if(facetValue.getModel() instanceof AsExcludedFacetValueModel)
        {
            excludedFacetValue = (AsExcludedFacetValueModel)facetValue.getModel();
        }
        else if(facetValue.getModel() instanceof de.hybris.platform.adaptivesearch.model.AsPromotedFacetValueModel)
        {
            excludedFacetValue = (AsExcludedFacetValueModel)this.modelService.clone(facetValue.getModel(), AsExcludedFacetValueModel.class);
        }
        else
        {
            excludedFacetValue = (AsExcludedFacetValueModel)this.modelService.create(AsExcludedFacetValueModel.class);
            excludedFacetValue.setFacetConfiguration(facetConfiguration);
            excludedFacetValue.setCatalogVersion(facetConfiguration.getCatalogVersion());
            excludedFacetValue.setValue(facetValue.getData().getValue());
        }
        List<AsExcludedFacetValueModel> excludedValues = new ArrayList<>(facetConfiguration.getExcludedValues());
        excludedValues.add(excludedFacetValue);
        updateCurrentObject(ctx, excludedValues);
        return new ActionResult("success");
    }


    protected AbstractAsFacetConfigurationModel resolveCurrentObject(ActionContext<FacetValueModel> ctx)
    {
        WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)ctx.getParameter("widgetInstanceManager");
        return (AbstractAsFacetConfigurationModel)widgetInstanceManager.getModel().getValue("currentObject", AbstractAsFacetConfigurationModel.class);
    }


    protected void updateCurrentObject(ActionContext<FacetValueModel> ctx, List<AsExcludedFacetValueModel> excludedValues)
    {
        WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)ctx.getParameter("widgetInstanceManager");
        widgetInstanceManager.getModel().setValue("currentObject.excludedValues", excludedValues);
    }
}
