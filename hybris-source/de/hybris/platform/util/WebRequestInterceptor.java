package de.hybris.platform.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebRequestInterceptor
{
    void doPreRequest(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);


    void doPostRequest(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}
