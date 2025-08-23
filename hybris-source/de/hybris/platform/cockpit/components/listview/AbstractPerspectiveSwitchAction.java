package de.hybris.platform.cockpit.components.listview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.EventListener;

public abstract class AbstractPerspectiveSwitchAction extends AbstractListViewAction
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPerspectiveSwitchAction.class);
    private String targetPerspUID;


    public boolean isAlwaysEnabled()
    {
        return true;
    }


    public void setTargetPerspectiveUid(String targetUID)
    {
        this.targetPerspUID = targetUID;
    }


    public String getTargetPerspectiveUid()
    {
        return this.targetPerspUID;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }
}
