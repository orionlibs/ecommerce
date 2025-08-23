package de.hybris.platform.deeplink.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.deeplink.DeeplinkUtils;
import de.hybris.platform.deeplink.services.DeeplinkUrlService;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;

public class GenericDeeplinkUrlServlet extends HttpServlet
{
    private static final Logger LOG = Logger.getLogger(GenericDeeplinkUrlServlet.class);


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String barcodeToken = ServletRequestUtils.getStringParameter((ServletRequest)req, DeeplinkUtils.getDeeplinkParameterName());
        if(GenericValidator.isBlankOrNull(barcodeToken))
        {
            return;
        }
        DeeplinkUrlService.LongUrlInfo generatedUrl = getDeeplinkUrlService().generateUrl(barcodeToken);
        if(generatedUrl != null)
        {
            if(generatedUrl.isUseForward())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Forward was triggered with destination address: " + generatedUrl.getUrl());
                }
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(generatedUrl.getUrl());
                requestDispatcher.forward((ServletRequest)req, (ServletResponse)resp);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Redirect was triggered with destination address: " + generatedUrl.getUrl());
                }
                resp.sendRedirect(generatedUrl.getUrl());
            }
        }
        else
        {
            LOG.info("There was no generated URL to use");
        }
    }


    protected DeeplinkUrlService getDeeplinkUrlService()
    {
        return (DeeplinkUrlService)Registry.getApplicationContext().getBean("deeplinkUrlService");
    }
}
