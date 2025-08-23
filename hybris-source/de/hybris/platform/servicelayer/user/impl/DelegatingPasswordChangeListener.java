package de.hybris.platform.servicelayer.user.impl;

import de.hybris.platform.servicelayer.user.listener.PasswordChangeEvent;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class DelegatingPasswordChangeListener implements PasswordChangeListener
{
    private final Collection<PasswordChangeListener> delegates;


    public DelegatingPasswordChangeListener(Collection<PasswordChangeListener> delegates)
    {
        this.delegates = new ArrayList<>(Objects.<Collection<? extends PasswordChangeListener>>requireNonNull(delegates, "delegates cannot be null."));
    }


    public void passwordChanged(PasswordChangeEvent event)
    {
        for(PasswordChangeListener l : this.delegates)
        {
            l.passwordChanged(event);
        }
    }
}
