package de.hybris.platform.adaptivesearchbackoffice.actions.results.overridepromoteditem;

import com.hybris.cockpitng.actions.ActionContext;
import de.hybris.platform.adaptivesearchbackoffice.actions.results.promoteitem.PromoteItemAction;
import de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser.DocumentModel;

public class OverridePromotedItemAction extends PromoteItemAction
{
    public boolean canPerform(ActionContext<DocumentModel> ctx)
    {
        DocumentModel data = (DocumentModel)ctx.getData();
        if(data == null || data.getPk() == null)
        {
            return false;
        }
        return (data.isPromoted() && !data.isFromSearchConfiguration());
    }
}
