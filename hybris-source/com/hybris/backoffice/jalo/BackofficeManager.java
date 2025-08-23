package com.hybris.backoffice.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSessionListener;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.user.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackofficeManager extends GeneratedBackofficeManager implements JaloSessionListener
{
    private final transient List<PersistenceLayerSessionListener> listeners = Collections.synchronizedList(new ArrayList<>());


    public static BackofficeManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (BackofficeManager)em.getExtension("backoffice");
    }


    public void addSessionListener(PersistenceLayerSessionListener listener)
    {
        this.listeners.add(listener);
    }


    public void removeListener(PersistenceLayerSessionListener listener)
    {
        this.listeners.remove(listener);
    }


    public void afterSessionCreation(JaloSession jaloSession)
    {
        synchronized(this.listeners)
        {
            this.listeners.forEach(listener -> listener.sessionCreated(jaloSession));
        }
    }


    public void beforeSessionClose(JaloSession jaloSession)
    {
        synchronized(this.listeners)
        {
            this.listeners.forEach(listener -> listener.sessionClosed(jaloSession));
        }
    }


    public void afterSessionUserChange(JaloSession jaloSession, User user)
    {
    }


    public void afterSessionAttributeChange(JaloSession jaloSession, String name, Object value)
    {
    }
}
