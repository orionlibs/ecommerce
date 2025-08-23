package de.hybris.platform.cockpit.test.mock;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class MockSessionScope implements Scope
{
    private final Map<String, Object> scopeMap = new HashMap<>();


    public Object get(String bean, ObjectFactory<?> factory)
    {
        Object object = this.scopeMap.get(bean);
        if(object == null)
        {
            object = factory.getObject();
            this.scopeMap.put(bean, object);
        }
        return object;
    }


    public String getConversationId()
    {
        return null;
    }


    public void registerDestructionCallback(String arg0, Runnable arg1)
    {
    }


    public Object remove(String bean)
    {
        return this.scopeMap.remove(bean);
    }


    public Object resolveContextualObject(String arg0)
    {
        return null;
    }
}
