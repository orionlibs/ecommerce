package de.hybris.platform.servicelayer.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class SecureResponseWrapper extends HttpServletResponseWrapper
{
    private static final String CR_LF_PATTERN = "(\r)|(\n)";
    private final Pattern pattern = Pattern.compile("(\r)|(\n)");


    public SecureResponseWrapper(HttpServletResponse response)
    {
        super(response);
    }


    public void sendRedirect(String location) throws IOException
    {
        if(containsCRLF(location))
        {
            throw new MalformedURLException("CR or LF detected in redirect URL: possible http response splitting attack");
        }
        super.sendRedirect(location);
    }


    public void setHeader(String name, String value)
    {
        if(containsCRLF(value))
        {
            throw new IllegalArgumentException("Header value must not contain CR or LF characters");
        }
        super.setHeader(name, value);
    }


    public void addHeader(String name, String value)
    {
        if(containsCRLF(value))
        {
            throw new IllegalArgumentException("Header value must not contain CR or LF characters");
        }
        super.addHeader(name, value);
    }


    private boolean containsCRLF(String value)
    {
        return this.pattern.matcher(value).find();
    }
}
