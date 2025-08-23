package de.hybris.platform.servicelayer.web.urlrenderer;

import de.hybris.platform.util.MediaUtil;

public class DefaultPublicMediaURLRenderer implements MediaUtil.PublicMediaURLRenderer
{
    private final String contextPath;


    public DefaultPublicMediaURLRenderer(String contextPath)
    {
        this.contextPath = contextPath;
    }


    public String renderPublicMediaURL(String rawUrl)
    {
        return this.contextPath + this.contextPath;
    }
}
