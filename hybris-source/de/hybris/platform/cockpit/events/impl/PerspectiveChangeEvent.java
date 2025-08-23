package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.session.UICockpitPerspective;

public class PerspectiveChangeEvent extends AbstractCockpitEvent
{
    private final UICockpitPerspective perspective;
    private final Object data;


    public PerspectiveChangeEvent(Object source, UICockpitPerspective perspective, Object data)
    {
        super(source);
        if(perspective == null)
        {
            throw new IllegalArgumentException("Perspective can not be null.");
        }
        this.perspective = perspective;
        this.data = data;
    }


    public UICockpitPerspective getPerspective()
    {
        return this.perspective;
    }


    public Object getData()
    {
        return this.data;
    }
}
