package de.hybris.platform.jalo.security.event;

public class LegacyLoginSuccessfulEvent extends AbstractLoginEvent
{
    public LegacyLoginSuccessfulEvent(String uid)
    {
        super(uid);
    }
}
