package de.hybris.platform.cms2.misc;

import com.google.common.base.Preconditions;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import javax.servlet.http.HttpServletRequest;

public class UrlUtils
{
    private static final int MAX_DATA = 250;
    private static final int EXPECTED_BUFFER_DATA = 1024;


    public static String extractHostInformationFromRequest(HttpServletRequest request, CMSSiteModel cmsSiteModel)
    {
        return extractHostInformationFromRequest(request, cmsSiteModel.getPreviewURL());
    }


    public static String extractHostInformationFromRequest(HttpServletRequest request, String currentUrl)
    {
        StringBuilder urlBuilder = new StringBuilder(1024);
        if(!currentUrl.matches("(http://|https://)(.*)"))
        {
            buildHostInformationFromRequest(request, urlBuilder);
        }
        urlBuilder.append(currentUrl);
        return urlBuilder.toString();
    }


    public static String extractHostInformationFromRequest(HttpServletRequest request)
    {
        StringBuilder urlBuilder = new StringBuilder(1024);
        buildHostInformationFromRequest(request, urlBuilder);
        return urlBuilder.toString();
    }


    protected static void buildHostInformationFromRequest(HttpServletRequest request, StringBuilder urlBuilder)
    {
        checkAndAppendString(urlBuilder, request.getScheme());
        urlBuilder.append("://");
        checkAndAppendString(urlBuilder, request.getServerName());
        urlBuilder.append(":");
        urlBuilder.append(request.getServerPort());
    }


    protected static void checkAndAppendString(StringBuilder urlBuilder, String value)
    {
        Preconditions.checkArgument((value.length() < 250), "The value exceeded the max length allowed.");
        urlBuilder.append(value);
    }
}
