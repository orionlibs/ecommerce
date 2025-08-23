package de.hybris.platform.adaptivesearchbackoffice.actions.facets;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacet;
import de.hybris.platform.adaptivesearch.data.AsFacet;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacet;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.ReferenceActionUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;

public class OverrideFacetAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AbstractEditorData, Object>
{
    @Resource
    private ModelService modelService;
    @Resource
    private ContextAwareConverter<AsPromotedFacet, AsPromotedFacetModel, AsItemConfigurationReverseConverterContext> asPromotedFacetReverseConverter;
    @Resource
    private ContextAwareConverter<AsFacet, AsFacetModel, AsItemConfigurationReverseConverterContext> asFacetReverseConverter;
    @Resource
    private ContextAwareConverter<AsExcludedFacet, AsExcludedFacetModel, AsItemConfigurationReverseConverterContext> asExcludedFacetReverseConverter;


    public ActionResult<Object> perform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        AbstractAsFacetConfiguration facetConfiguration = ((AbstractFacetConfigurationEditorData)data).getFacetConfiguration();
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = (AbstractAsConfigurableSearchConfigurationModel)ReferenceActionUtils.resolveCurrentObject(ctx, AbstractAsConfigurableSearchConfigurationModel.class);
        AsItemConfigurationReverseConverterContext context = new AsItemConfigurationReverseConverterContext();
        context.setCatalogVersion(searchConfiguration.getCatalogVersion());
        context.setParentConfiguration((AbstractAsConfigurationModel)searchConfiguration);
        if(facetConfiguration instanceof AsPromotedFacet)
        {
            AsPromotedFacetModel promotedFacet = (AsPromotedFacetModel)this.asPromotedFacetReverseConverter.convert(facetConfiguration, context);
            this.modelService.save(promotedFacet);
        }
        else if(facetConfiguration instanceof AsFacet)
        {
            AsFacetModel facet = (AsFacetModel)this.asFacetReverseConverter.convert(facetConfiguration, context);
            this.modelService.save(facet);
        }
        else if(facetConfiguration instanceof AsExcludedFacet)
        {
            AsExcludedFacetModel excludedFacet = (AsExcludedFacetModel)this.asExcludedFacetReverseConverter.convert(facetConfiguration, context);
            this.modelService.save(excludedFacet);
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
        return (data instanceof AbstractFacetConfigurationEditorData && ((AbstractFacetConfigurationEditorData)data)
                        .getFacetConfiguration() != null);
    }
}
