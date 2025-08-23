package de.hybris.platform.productcockpit.security;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.web.servlet.http.Encodes;
import org.zkoss.xml.XMLs;

public class ZKXSSURLEncoder implements Encodes.URLEncoder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZKXSSURLEncoder.class);


    public String encodeURL(ServletContext ctx, ServletRequest request, ServletResponse response, String url)
    {
        if(url == null || url.length() == 0)
        {
            return url;
        }
        String escapedUrl = XMLs.escapeXML(url);
        if(!url.equals(escapedUrl))
        {
            LOGGER.info("{} is escaped to {}", url, escapedUrl);
        }
        return escapedUrl;
    }
}
