package de.hybris.platform.oauth2.services;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface OIDCService
{
    Map<String, Object> getConfiguration(String paramString, HttpServletRequest paramHttpServletRequest);


    Map<String, List<Map<String, String>>> getJWKS(String paramString, HttpServletRequest paramHttpServletRequest);
}
