package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.SearchEvent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchEventHandler extends AbstractRequestEventHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(SearchEventHandler.class);
    public static final String QUERY_KEY = "query";


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        if(perspective == null)
        {
            LOG.warn("Can not handle search event. Reason: No perspective has been specified.");
        }
        else
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new SearchEvent(perspective, getParameter(params, "query")));
        }
    }
}
