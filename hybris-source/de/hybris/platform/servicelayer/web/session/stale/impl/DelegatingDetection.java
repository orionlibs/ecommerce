package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.user.listener.PasswordChangeEvent;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeListener;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionDetector;
import java.util.Collection;

public class DelegatingDetection implements StaleSessionDetector.Detection, PasswordChangeListener
{
    private Collection<StaleSessionDetector.Detection> delegates;


    public DelegatingDetection(Collection<StaleSessionDetector.Detection> delegates)
    {
        this.delegates = delegates;
    }


    public boolean isStaleSession()
    {
        for(StaleSessionDetector.Detection detection : this.delegates)
        {
            if(detection.isStaleSession())
            {
                return true;
            }
        }
        return false;
    }


    public void close()
    {
        this.delegates.forEach(StaleSessionDetector.Detection::close);
        this.delegates.clear();
    }


    public void passwordChanged(PasswordChangeEvent event)
    {
        for(StaleSessionDetector.Detection delegate : this.delegates)
        {
            if(delegate instanceof PasswordChangeListener)
            {
                ((PasswordChangeListener)delegate).passwordChanged(event);
            }
        }
    }
}
