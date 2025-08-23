package de.hybris.platform.jalo.security.event;

public class LegacyLoginFailureEvent extends AbstractLoginEvent
{
    public LegacyLoginFailureEvent(String uid)
    {
        super(uid);
    }
}
