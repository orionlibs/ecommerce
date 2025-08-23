package de.hybris.platform.servicelayer.session.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.Session;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultSession implements Session, Serializable
{
    private volatile ModelService modelService;
    private volatile JaloSession jaloSession;


    public void setAttribute(String name, Object value)
    {
        Object model = value;
        try
        {
            model = getModelService().toPersistenceLayer(value);
        }
        catch(IllegalStateException illegalStateException)
        {
        }
        finally
        {
            this.jaloSession.setAttribute(name, model);
        }
    }


    public <T> T getAttribute(String name)
    {
        return (T)getModelService().toModelLayer(this.jaloSession.getAttribute(name));
    }


    public <T> Map<String, T> getAllAttributes()
    {
        ModelService modelService = getModelService();
        Map<String, Object> jaloAttributes = this.jaloSession.getAttributes();
        Map<String, T> tmpMap = new LinkedHashMap<>();
        for(String key : jaloAttributes.keySet())
        {
            tmpMap.put(key, (T)modelService.toModelLayer(jaloAttributes.get(key)));
        }
        return Collections.unmodifiableMap(tmpMap);
    }


    public String getSessionId()
    {
        return this.jaloSession.getSessionID();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setJaloSession(JaloSession jaloSession)
    {
        this.jaloSession = jaloSession;
    }


    public void init(JaloSession jaloSession, ModelService modelService)
    {
        setJaloSession(jaloSession);
        this.modelService = modelService;
    }


    public JaloSession getJaloSession()
    {
        return this.jaloSession;
    }


    public final boolean equals(Object object)
    {
        if(object == this)
        {
            return true;
        }
        return (object instanceof DefaultSession && ((DefaultSession)object).getSessionId().equals(getSessionId()));
    }


    public final int hashCode()
    {
        return getSessionId().hashCode();
    }


    public void removeAttribute(String name)
    {
        this.jaloSession.removeAttribute(name);
    }


    private ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        }
        return this.modelService;
    }
}
