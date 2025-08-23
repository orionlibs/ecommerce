package de.hybris.platform.servicelayer.session.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSessionService extends AbstractService implements SessionService
{
    private static final String JALOSESSION_ATTRIBUTE_PREFIX = "_slsession_";
    private ModelService modelService;


    public Session getCurrentSession()
    {
        return getOrBindSession(getOrCreateCurrentJaloSession());
    }


    @Deprecated(since = "5.5.1", forRemoval = true)
    public Session getSession(String sessionID)
    {
        return getOrBindSession(getJaloConnection().getSession(sessionID));
    }


    public Session createNewSession()
    {
        try
        {
            JaloSession jaloSession = getJaloConnection().createAnonymousCustomerSession();
            return getOrBindSession(jaloSession);
        }
        catch(JaloSecurityException e)
        {
            throw new SystemException(e.getMessage());
        }
    }


    public Object executeInLocalView(SessionExecutionBody body)
    {
        try
        {
            getOrCreateCurrentJaloSession().createLocalSessionContext();
            return body.execute();
        }
        finally
        {
            getOrCreateCurrentJaloSession().removeLocalSessionContext();
        }
    }


    public <T> T executeInLocalViewWithParams(Map<String, Object> localViewParameters, SessionExecutionBody body)
    {
        Preconditions.checkNotNull(localViewParameters, "localViewParameters cannot be null");
        try
        {
            getOrCreateCurrentJaloSession().createLocalSessionContext();
            Session s = getCurrentSession();
            setParametersInSession(s, localViewParameters);
            return (T)body.execute();
        }
        finally
        {
            getOrCreateCurrentJaloSession().removeLocalSessionContext();
        }
    }


    private void setParametersInSession(Session session, Map<String, Object> localViewParameters)
    {
        for(Map.Entry<String, Object> entry : localViewParameters.entrySet())
        {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
    }


    public Object executeInLocalView(SessionExecutionBody body, UserModel model)
    {
        try
        {
            SessionContext ctx = getOrCreateCurrentJaloSession().createLocalSessionContext();
            ctx.setUser((User)this.modelService.getSource(model));
            return body.execute();
        }
        finally
        {
            getOrCreateCurrentJaloSession().removeLocalSessionContext();
        }
    }


    public void closeSession(Session session)
    {
        JaloSession jaloSession = getJaloSession(session);
        jaloSession.close();
    }


    public void closeCurrentSession()
    {
        if(hasCurrentSession())
        {
            closeSession(getCurrentSession());
        }
    }


    public void setAttribute(String name, Object value)
    {
        getCurrentSession().setAttribute(name, value);
    }


    public void removeAttribute(String name)
    {
        getCurrentSession().removeAttribute(name);
    }


    public <T> T getAttribute(String name)
    {
        return (T)getCurrentSession().getAttribute(name);
    }


    public <T> T getOrLoadAttribute(String name, SessionService.SessionAttributeLoader<T> loader)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("loader", loader);
        JaloSession currentSession = JaloSession.getCurrentSession();
        T result = getAttribute(name);
        if(result == null)
        {
            synchronized(currentSession)
            {
                result = getAttribute(name);
                if(result == null)
                {
                    result = (T)loader.load();
                    setAttribute(name, result);
                }
            }
        }
        return result;
    }


    public <T> Map<String, T> getAllAttributes()
    {
        return getCurrentSession().getAllAttributes();
    }


    private Session getOrBindSession(JaloSession jaloSession)
    {
        Session session = (Session)jaloSession.getAttribute("_slsession_");
        if(session == null)
        {
            session = createSession();
            if(session instanceof DefaultSession)
            {
                ((DefaultSession)session).init(jaloSession, this.modelService);
            }
            jaloSession.setAttribute("_slsession_", session);
        }
        return session;
    }


    public boolean hasCurrentSession()
    {
        return (hasCurrentJaloSession() && !getOrCreateCurrentJaloSession().isClosed());
    }


    private JaloConnection getJaloConnection()
    {
        return getCurrentTenant().getJaloConnection();
    }


    private boolean hasCurrentJaloSession()
    {
        return JaloSession.hasCurrentSession(getCurrentTenant());
    }


    private JaloSession getOrCreateCurrentJaloSession()
    {
        return JaloSession.getCurrentSession(getCurrentTenant());
    }


    private JaloSession getJaloSession(Session session)
    {
        JaloSession jaloSession;
        if(session instanceof DefaultSession)
        {
            jaloSession = ((DefaultSession)session).getJaloSession();
        }
        else
        {
            throw new UnsupportedOperationException("JaloSession is only accessible for de.hybris.platform.servicelayer.session.impl.DefaultSession instances");
        }
        return jaloSession;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public Session createSession()
    {
        throw new UnsupportedOperationException("please override DefaultSessionService.createSession() or use <lookup-method>");
    }


    public Session getBoundSession(Object rawSession)
    {
        if(rawSession instanceof JaloSession)
        {
            return getOrBindSession((JaloSession)rawSession);
        }
        throw new IllegalStateException("Given rawSession is not an instance of JaloSession.");
    }


    public Object getRawSession(Session session)
    {
        return getJaloSession(session);
    }
}
