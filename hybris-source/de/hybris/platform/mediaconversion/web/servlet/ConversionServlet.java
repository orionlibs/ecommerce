package de.hybris.platform.mediaconversion.web.servlet;

import de.hybris.platform.core.PK;
import de.hybris.platform.mediaconversion.web.facades.OnDemandConversionFacade;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ConversionServlet extends HttpServlet
{
    private static final Logger LOG = Logger.getLogger(ConversionServlet.class);
    private static final long serialVersionUID = 4209313018489362033L;
    private static final String SLASH = "/";
    private static final Pattern PATH_INFO_SPLIT = Pattern.compile("/");
    private static final Pattern PATH_INFO_CLEANUP = Pattern.compile("/\\.?/");


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo) || !pathInfo.startsWith("/"))
        {
            resp.sendError(404, "Nothing's here...");
            return;
        }
        pathInfo = pathInfo.substring(1);
        pathInfo = PATH_INFO_CLEANUP.matcher(pathInfo).replaceAll("/");
        String[] pathSplit = PATH_INFO_SPLIT.split(pathInfo, 2);
        if(pathSplit.length != 2)
        {
            LOG.debug("No format specified.");
            resp.sendError(404, "No format specified.");
        }
        String cleanedPathSplit1 = cleanFromFormat(pathSplit[1]);
        process(resp, pathSplit[0], cleanedPathSplit1);
    }


    protected String cleanFromFormat(String pathInfo)
    {
        String format = pathInfo;
        if(format != null && !format.isEmpty())
        {
            int pos = format.lastIndexOf('.');
            if(pos != -1)
            {
                format = format.substring(0, pos);
            }
        }
        return format;
    }


    private void process(HttpServletResponse resp, String containerPK, String format) throws IOException
    {
        LOG.debug("Retrieving Media for container '" + containerPK + "' and format '" + format + "'.");
        try
        {
            String url = retrieveImageFacade().convert(PK.parse(containerPK), format);
            LOG.debug("Redirecting to '" + url + "'.");
            resp.sendRedirect(url);
        }
        catch(de.hybris.platform.core.PK.PKException e)
        {
            LOG.debug("Invalid PK '" + containerPK + "' specified. Not a number.", (Throwable)e);
            resp.sendError(404, "Invalid PK.");
        }
        catch(IllegalArgumentException e)
        {
            LOG.debug("Invalid PK '" + containerPK + "' specified. Unknown type.", e);
            resp.sendError(404, "Invalid PK.");
        }
        catch(ModelLoadingException e)
        {
            LOG.debug("MediaContainer for '" + containerPK + "' not found.", (Throwable)e);
            resp.sendError(404, "Not found.");
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Invalid format qualifier '" + format + "' specified. Not found.", (Throwable)e);
            resp.sendError(404, "Invalid format qualifier.");
        }
        catch(Exception e)
        {
            LOG.error("An error occurred while serving the request.", e);
            resp.sendError(500);
        }
    }


    OnDemandConversionFacade retrieveImageFacade()
    {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        if(ctx != null)
        {
            return (OnDemandConversionFacade)ctx.getBean("onDemandConversionFacade", OnDemandConversionFacade.class);
        }
        LOG.warn("Couldn't find bean + onDemandConversionFacade. ApplicationContext already/still down?");
        throw new NoSuchBeanDefinitionException("Couldn't find bean onDemandConversionFacade");
    }
}
