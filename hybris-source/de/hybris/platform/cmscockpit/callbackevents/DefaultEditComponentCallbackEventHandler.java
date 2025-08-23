package de.hybris.platform.cmscockpit.callbackevents;

import de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.components.liveedit.impl.LiveEditPopupEditDialog;
import java.util.Map;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Messagebox;

public class DefaultEditComponentCallbackEventHandler implements CallbackEventHandler<LiveEditView>
{
    private static final Logger LOG = Logger.getLogger(DefaultEditComponentCallbackEventHandler.class);


    public String getEventId()
    {
        return "defaultCallback";
    }


    public void onCallbackEvent(LiveEditView view, Map<String, Object> attributesMap) throws InterruptedException
    {
        boolean isLocked = view.getModel().isSlotLockedForId((String)attributesMap.get("slot_id"));
        if(isLocked)
        {
            int choice = Messagebox.show(Labels.getLabel("sectionlock.unlock.msg"),
                            Labels.getLabel("sectionlock.unlock.title"), 3, "z-msgbox z-msgbox-exclamation");
            if(choice == 1)
            {
                isLocked = false;
            }
        }
        if(!isLocked)
        {
            view.setPopupEditorDialog(new LiveEditPopupEditDialog(attributesMap, view.getModel().getCurrentPreviewData().getCatalogVersions(), view));
            view.getViewComponent().appendChild((Component)view.getPopupEditorDialog());
        }
        LOG.info("Received callback event (show popup editor) from element [id = " + (String)attributesMap.get("cmp_id") + "]");
    }
}
