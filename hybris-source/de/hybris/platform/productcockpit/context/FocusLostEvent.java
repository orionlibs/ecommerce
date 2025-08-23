package de.hybris.platform.productcockpit.context;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class FocusLostEvent extends Event
{
    public static final String ON_FOCUS_LOST = "onFocusLost";
    private final Component newOne;


    public FocusLostEvent(Component target, Component newOne)
    {
        super("onFocusLost", target);
        this.newOne = newOne;
    }


    public Component getNewFocusOwner()
    {
        return this.newOne;
    }
}
