package de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.deleteoverridereference;

import com.hybris.cockpitng.actions.ActionContext;
import de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor.deletereference.DeleteReferenceAction;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;

public class DeleteOverrideReferenceAction extends DeleteReferenceAction
{
    public boolean canPerform(ActionContext<AbstractEditorData> ctx)
    {
        AbstractEditorData data = (AbstractEditorData)ctx.getData();
        if(data == null)
        {
            return false;
        }
        return (data.isFromSearchConfiguration() && data.isOverride());
    }
}
