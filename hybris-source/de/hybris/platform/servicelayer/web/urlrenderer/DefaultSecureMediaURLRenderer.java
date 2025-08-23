package de.hybris.platform.servicelayer.web.urlrenderer;

import de.hybris.platform.media.MediaSource;
import de.hybris.platform.util.MediaUtil;

public class DefaultSecureMediaURLRenderer implements MediaUtil.SecureMediaURLRenderer
{
    private final boolean addContextPath;
    private final String secureMediasResourcePathPrefix;
    private final String contextPath;


    public DefaultSecureMediaURLRenderer(String secureMediasResourcePathPrefix, boolean addContextPath, String contextPath)
    {
        this.secureMediasResourcePathPrefix = secureMediasResourcePathPrefix;
        this.addContextPath = addContextPath;
        this.contextPath = contextPath;
    }


    public String renderSecureMediaURL(MediaSource media)
    {
        StringBuilder sb = new StringBuilder(this.secureMediasResourcePathPrefix + "?mediaPK=" + this.secureMediasResourcePathPrefix);
        if(this.addContextPath)
        {
            sb.insert(0, this.contextPath + "/");
        }
        return sb.toString();
    }
}
