package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.RequestEventHandler;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRequestHandler extends AbstractRequestHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRequestHandler.class);
    public static final String PERSP_KEY = "persp";


    public UICockpitPerspective getRequestedPerspective(Map<String, String[]> params)
    {
        UICockpitPerspective perspective = null;
        String perspParam = null;
        if(params != null)
        {
            if(params.containsKey("persp") && params.get("persp") != null)
            {
                perspParam = ((String[])params.get("persp"))[0];
            }
        }
        if(StringUtils.isBlank(perspParam) || !UISessionUtils.getCurrentSession().isPerspectiveAvailable(perspParam))
        {
            if(!StringUtils.isBlank(perspParam))
            {
                LOG.warn("No perspective available with UID '" + perspParam + "'. Loading default perspective");
            }
            List<UICockpitPerspective> availablePerspectives = UISessionUtils.getCurrentSession().getAvailablePerspectives();
            if(availablePerspectives.isEmpty())
            {
                LOG.error("Can not handle request. Reason: No perspectives available.");
            }
            else
            {
                perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            }
        }
        else
        {
            perspective = UISessionUtils.getCurrentSession().getPerspective(perspParam);
        }
        return perspective;
    }


    public void handleRequest(Map<String, String[]> params)
    {
        List<String> eventNames = extractEventNames(params);
        handleRequestEvent(UISessionUtils.getCurrentSession().getCurrentPerspective(), eventNames, params);
    }


    private List<String> extractEventNames(Map<String, String[]> params)
    {
        List<String> eventNames = new ArrayList<>();
        if(params != null && params.containsKey("events") && params.get("events") != null)
        {
            String[] eventValues = params.get("events");
            eventNames = new ArrayList<>();
            for(String eventValue : eventValues)
            {
                if(!StringUtils.isBlank(eventValue))
                {
                    for(String eventName : StringUtils.split(eventValue, ','))
                    {
                        if(!eventNames.contains(eventName))
                        {
                            eventNames.add(eventName);
                        }
                    }
                }
            }
        }
        return eventNames;
    }


    protected void handleRequestEvent(UICockpitPerspective perspective, List<String> events, Map<String, String[]> params)
    {
        if(events != null && !events.isEmpty())
        {
            for(String eventName : events)
            {
                for(RequestEventHandler eventHandler : getRequestEventHandlers(eventName))
                {
                    try
                    {
                        eventHandler.handleEvent(perspective, params);
                    }
                    catch(Exception e)
                    {
                        LOG.error("An error occurred while processing event '" + eventName + "'.", e);
                    }
                }
            }
        }
    }
}
