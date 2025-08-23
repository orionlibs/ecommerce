package de.hybris.platform.servicelayer.web;

import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class Log4JFilter extends GenericFilterBean
{
    public static final String REMOTE_ADDRESS = "RemoteAddr";


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        LoggingContextFactory.getLoggingContextHandler().put("RemoteAddr", Utilities.getIPAddressForLogOutput(request.getRemoteAddr()).toString());
        try
        {
            filterChain.doFilter(request, response);
        }
        finally
        {
            LoggingContextFactory.getLoggingContextHandler().remove("RemoteAddr");
        }
    }
}
