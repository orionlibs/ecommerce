package de.hybris.platform.webservicescommons.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class SessionHidingRequestWrapper extends HttpServletRequestWrapper
{
    private final HttpServletRequest wrappedRequest;


    public SessionHidingRequestWrapper(HttpServletRequest request)
    {
        super(request);
        this.wrappedRequest = request;
    }


    public HttpSession getSession(boolean create)
    {
        if(create)
        {
            return getDummySession();
        }
        return null;
    }


    public HttpSession getSession()
    {
        return getDummySession();
    }


    protected HttpSession getDummySession()
    {
        return (HttpSession)new Object(this);
    }
}
