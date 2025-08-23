package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.session.FocusablePerspectiveArea;

public class FocusEvent extends AbstractCockpitEvent
{
    private final FocusablePerspectiveArea area;


    public FocusEvent(Object source, FocusablePerspectiveArea focusedArea)
    {
        super(source);
        if(focusedArea == null)
        {
            throw new IllegalArgumentException("Focusable area can not be null.");
        }
        this.area = focusedArea;
    }


    public FocusablePerspectiveArea getFocusedArea()
    {
        return this.area;
    }
}
