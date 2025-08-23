package de.hybris.platform.adaptivesearchbackoffice.actions.results.discardoverridepromoteditem;

import com.hybris.cockpitng.actions.ActionContext;
import de.hybris.platform.adaptivesearchbackoffice.actions.results.unpromoteitem.UnpromoteItemAction;
import de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser.DocumentModel;

public class DiscardOverridePromotedItemAction extends UnpromoteItemAction
{
    public boolean canPerform(ActionContext<DocumentModel> ctx)
    {
        DocumentModel data = (DocumentModel)ctx.getData();
        if(data == null || data.getPk() == null)
        {
            return false;
        }
        return (data.isPromoted() && data.isFromSearchConfiguration() && data.isOverride());
    }
}
