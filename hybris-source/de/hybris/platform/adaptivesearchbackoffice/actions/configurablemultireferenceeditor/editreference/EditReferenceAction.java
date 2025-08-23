package de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.editreference;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.ReferenceActionUtils;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.MultiReferenceEditorLogic;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;

public class EditReferenceAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AbstractEditorData, Object>
{
    @Resource
    private ModelService modelService;


    public ActionResult<Object> perform(ActionContext<AbstractEditorData> ctx)
    {
        MultiReferenceEditorLogic<AbstractEditorData, ?> editorLogic = ReferenceActionUtils.resolveEditorLogic(ctx);
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        editorLogic.triggerUpdateReference(data);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        if(data == null)
        {
            return false;
        }
        return data.isFromSearchConfiguration();
    }
}
