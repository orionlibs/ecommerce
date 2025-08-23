package de.hybris.platform.adaptivesearchbackoffice.actions.facets;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.ReferenceActionUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.FacetFiltersRequestData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.MultiReferenceEditorLogic;
import java.util.Collections;
import org.apache.commons.collections4.CollectionUtils;

public class RemoveFacetFiltersAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AbstractFacetConfigurationEditorData, Object>
{
    public ActionResult<Object> perform(ActionContext<AbstractFacetConfigurationEditorData> ctx)
    {
        MultiReferenceEditorLogic<AbstractFacetConfigurationEditorData, ?> editorLogic = ReferenceActionUtils.resolveEditorLogic(ctx);
        AbstractFacetConfigurationEditorData data = (AbstractFacetConfigurationEditorData)ctx.getData();
        FacetFiltersRequestData request = new FacetFiltersRequestData();
        request.setIndexProperty(data.getIndexProperty());
        request.setValues(Collections.emptyList());
        editorLogic.getWidgetInstanceManager().sendOutput("searchRequest", request);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<AbstractFacetConfigurationEditorData> ctx)
    {
        AbstractFacetConfigurationEditorData data = (AbstractFacetConfigurationEditorData)ctx.getData();
        if(data == null || data.getFacet() == null)
        {
            return false;
        }
        return CollectionUtils.isNotEmpty(data.getFacet().getSelectedValues());
    }
}
