package de.hybris.platform.cmscockpit.resolvers;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;

public class LiveUrlPageResolver extends PreviewUrlPageResolver
{
    private static final Logger LOG = Logger.getLogger(LiveUrlPageResolver.class);
    private static final String QST_MARK = "?";
    private static final String SITE_PARAM = "site";
    private static final String EQUALS = "=";
    private static final String CLEAR_SITE = "&clear=true";
    private static final String SLASH = "/";


    public String resolve(AbstractPageModel page)
    {
        String previewUrl = super.resolve(page);
        try
        {
            String resolvedUrl = resolveByWebService(preProcessPreviewUrl(previewUrl));
            return postProcessLiveUrl(previewUrl, resolvedUrl, getCmsAdminSiteService().getActiveSite());
        }
        catch(Exception e)
        {
            LOG.error("Could not resolve Live URL for page " + page.getUid() + " due to : " + e.getMessage(), e);
            return null;
        }
    }


    protected String resolveByWebService(String url) throws Exception
    {
        if(isInternal(url))
        {
            HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
            connection.setRequestMethod(HttpMethod.GET.name());
            return resolveByWebService(connection);
        }
        throw new Exception("tried to establish a connection to a non trusted location");
    }


    String resolveByWebService(HttpURLConnection connection) throws Exception
    {
        InputStream is = connection.getInputStream();
        try
        {
            String response = IOUtils.toString(is, "UTF-8");
            if(connection.getResponseCode() == Response.Status.OK.getStatusCode())
            {
                String str = response;
                if(is != null)
                {
                    is.close();
                }
                return str;
            }
            throw new Exception(response);
        }
        catch(Throwable throwable)
        {
            if(is != null)
            {
                try
                {
                    is.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    protected String preProcessPreviewUrl(String previewUrl)
    {
        previewUrl = previewUrl.replaceAll("cx-preview", "resolvePageUrl");
        previewUrl = previewUrl.replaceAll("cmsTicketId", "cmsPageResolveTicketId");
        return previewUrl;
    }


    protected String postProcessLiveUrl(String previewUrl, String liveUrl, CMSSiteModel activeSite)
    {
        int cmsTicketMarker = liveUrl.indexOf("?");
        if(cmsTicketMarker != -1)
        {
            liveUrl = liveUrl.substring(0, cmsTicketMarker);
        }
        String result = "";
        int endMarkerIndex = previewUrl.indexOf("cx-preview");
        if(endMarkerIndex != -1)
        {
            result = previewUrl.substring(0, endMarkerIndex);
        }
        while(result.endsWith("/"))
        {
            result = result.substring(0, result.length() - 1);
        }
        StringBuilder urlBuilder = new StringBuilder(result);
        if(!liveUrl.startsWith("/"))
        {
            urlBuilder.append("/");
        }
        return urlBuilder.append(liveUrl).append("?").append("site").append("=").append(activeSite.getUid())
                        .append("&clear=true").toString();
    }
}
