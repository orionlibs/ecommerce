package de.hybris.platform.ticket.utils;

import org.apache.commons.lang3.StringUtils;

public class AttachmentMediaUrlHelper
{
    private static final String TILDE = "~";
    private static final String FORWARD_SLASH = "/";


    public static String urlHelper(String url)
    {
        if(StringUtils.isEmpty(url))
        {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if(url.startsWith("/"))
        {
            sb.insert(0, "~");
        }
        return sb.toString();
    }
}
