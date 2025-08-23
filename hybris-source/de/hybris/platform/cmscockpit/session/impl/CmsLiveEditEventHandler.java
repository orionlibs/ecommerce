package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.events.impl.CmsLiveEditEvent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CmsLiveEditEventHandler extends CmsNavigationEventHandler
{
    private static final Logger LOG = Logger.getLogger(CmsLiveEditEventHandler.class);
    public static final String URL_KEY = "url";


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        CMSSiteModel site = (CMSSiteModel)getObject(params, "site");
        String url = checkUrl(site, getURL(params));
        if(site != null && !url.isEmpty())
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new CmsLiveEditEvent(perspective, site, url));
        }
        else
        {
            LOG.warn("Can not show site url. Reason: No valid site or url specified.");
        }
        super.handleEvent(perspective, params);
    }


    protected String getURL(Map<String, String[]> params)
    {
        if(StringUtils.isBlank(getParameter(params, "url")))
        {
            LOG.warn("Can not show site url since no url has been specified.");
        }
        else
        {
            return getParameter(params, "url");
        }
        return "";
    }


    protected String checkUrl(CMSSiteModel site, String url)
    {
        if(site != null && !url.isEmpty())
        {
            if(!site.getPreviewURL().isEmpty())
            {
                if(StringUtils.startsWithIgnoreCase(url, site.getPreviewURL()))
                {
                    return url;
                }
                if(StringUtils.startsWithIgnoreCase(url, "/"))
                {
                    return site.getPreviewURL() + site.getPreviewURL();
                }
                LOG.warn("The provided url '" + url + "' is not valid.");
                return site.getPreviewURL();
            }
            return url;
        }
        return "";
    }
}
