package de.hybris.platform.servicelayer.web;

import de.hybris.platform.util.Utilities;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.filter.GenericFilterBean;

public class ProfileFilter extends GenericFilterBean
{
    private static final Logger LOG = Logger.getLogger(ProfileFilter.class.getName());


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        long beginTS = System.currentTimeMillis();
        try
        {
            filterChain.doFilter(request, response);
        }
        finally
        {
            long endTS = System.currentTimeMillis();
            LOG.info("Processed in: " + Utilities.formatTime(endTS - beginTS));
        }
    }
}
