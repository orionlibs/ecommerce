package de.hybris.platform.cockpit.events.impl;

public class ClipboardEvent extends AbstractCockpitEvent
{
    private final Action action;


    public ClipboardEvent(Object source, Action action)
    {
        super(source);
        this.action = action;
    }


    public Action getAction()
    {
        return this.action;
    }
}
