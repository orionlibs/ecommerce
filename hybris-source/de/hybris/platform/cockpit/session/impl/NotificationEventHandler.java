package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationEventHandler extends AbstractRequestEventHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventHandler.class);
    public static final String TITLE_KEY = "title";
    public static final String MESSAGE_KEY = "msg";
    private String charset = "UTF-8";


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        if(perspective == null)
        {
            LOG.warn("Can not handle notification event. Reason: No perspective has been specified.");
        }
        else
        {
            BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)perspective;
            if(basePerspective.getNotifier() != null)
            {
                basePerspective.getNotifier().setNotification(new Notification(
                                getDecodedParameter(params, "title"), getDecodedParameter(params, "msg")));
            }
        }
    }


    protected String getDecodedParameter(Map<String, String[]> params, String key)
    {
        String ret = null;
        String parameter = getParameter(params, key);
        if(StringUtils.isNotBlank(parameter))
        {
            try
            {
                ret = URLDecoder.decode(parameter, getCharset());
            }
            catch(UnsupportedEncodingException e)
            {
                LOG.error("Could not decode notification message.", e);
            }
        }
        return ret;
    }


    public void setCharset(String charset)
    {
        this.charset = charset;
    }


    public String getCharset()
    {
        return this.charset;
    }
}
