package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.RequestEventHandler;
import de.hybris.platform.cockpit.session.RequestHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractRequestHandler implements RequestHandler
{
    private final Map<String, List<RequestEventHandler>> eventHandlers = new HashMap<>();


    public void addRequestEventHandler(String eventName, RequestEventHandler eventHandler)
    {
        if(!StringUtils.isBlank(eventName) && eventHandler != null)
        {
            List<RequestEventHandler> handlers = this.eventHandlers.get(eventName);
            if(handlers == null)
            {
                handlers = new ArrayList<>();
                this.eventHandlers.put(eventName, handlers);
            }
            if(!handlers.contains(eventHandler))
            {
                handlers.add(eventHandler);
            }
        }
    }


    public void removeRequestEventHandler(String eventName, RequestEventHandler eventHandler)
    {
        if(!StringUtils.isBlank(eventName) && eventHandler != null)
        {
            List<RequestEventHandler> handlers = this.eventHandlers.get(eventName);
            if(handlers != null)
            {
                handlers.remove(eventHandler);
            }
        }
    }


    public void setRequestEventHandlers(Map<String, List<RequestEventHandler>> eventHandlers)
    {
        this.eventHandlers.clear();
        if(eventHandlers != null)
        {
            this.eventHandlers.putAll(eventHandlers);
        }
    }


    protected List<RequestEventHandler> getRequestEventHandlers(String eventName)
    {
        List<RequestEventHandler> handlers = this.eventHandlers.get(eventName);
        return (handlers == null) ? Collections.EMPTY_LIST : Collections.<RequestEventHandler>unmodifiableList(handlers);
    }
}
