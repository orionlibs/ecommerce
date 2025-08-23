package de.hybris.platform.mediaweb;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@Deprecated(since = "6.1.0", forRemoval = true)
public class SecureResponseWrapper extends HttpServletResponseWrapper
{
    private final de.hybris.platform.servicelayer.web.SecureResponseWrapper delegate;


    public SecureResponseWrapper(HttpServletResponse response)
    {
        super(response);
        this.delegate = new de.hybris.platform.servicelayer.web.SecureResponseWrapper(response);
    }


    public void sendRedirect(String location) throws IOException
    {
        this.delegate.sendRedirect(location);
    }


    public void setHeader(String name, String value)
    {
        this.delegate.setHeader(name, value);
    }


    public void addHeader(String name, String value)
    {
        this.delegate.addHeader(name, value);
    }
}
