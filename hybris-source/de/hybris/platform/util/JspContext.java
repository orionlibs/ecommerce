package de.hybris.platform.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import org.apache.log4j.Logger;

public class JspContext
{
    public static final JspContext NULL_CONTEXT = new JspContext(null, null, null);
    private static final Logger log = Logger.getLogger(JspContext.class.getName());
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final JspWriter out;


    public JspContext(JspWriter out, HttpServletRequest request, HttpServletResponse response)
    {
        this.out = out;
        this.request = request;
        this.response = response;
    }


    public JspWriter getJspWriter()
    {
        return this.out;
    }


    public HttpServletRequest getServletRequest()
    {
        return this.request;
    }


    public HttpServletResponse getServletResponse()
    {
        return this.response;
    }


    public void print(String string)
    {
        try
        {
            if(getJspWriter() != null)
            {
                getJspWriter().print(string);
                getJspWriter().flush();
            }
            if(getServletResponse() != null)
            {
                getServletResponse().flushBuffer();
            }
        }
        catch(Exception e)
        {
            if(log.isDebugEnabled())
            {
                log.debug(e.getMessage());
            }
        }
    }


    public void println(String string)
    {
        try
        {
            if(getJspWriter() != null)
            {
                getJspWriter().println(string + "<br/>");
                getJspWriter().flush();
            }
            if(getServletResponse() != null)
            {
                getServletResponse().flushBuffer();
            }
        }
        catch(Exception e)
        {
            if(log.isDebugEnabled())
            {
                log.warn(e.getMessage());
            }
        }
    }
}
