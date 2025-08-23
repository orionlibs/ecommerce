package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class SectionPanelEvent extends Event
{
    public SectionPanelEvent(String name, Component target)
    {
        super(name, target);
    }


    public SectionPanelEvent(String name, Component target, Object object)
    {
        super(name, target, object);
    }
}
