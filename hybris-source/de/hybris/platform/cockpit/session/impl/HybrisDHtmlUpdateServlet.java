package de.hybris.platform.cockpit.session.impl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;

public class HybrisDHtmlUpdateServlet extends DHtmlUpdateServlet
{
    private static final Logger LOG = LoggerFactory.getLogger(HybrisDHtmlUpdateServlet.class);


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            if(request.getRequestURI().contains("comet") && (request
                            .getUserPrincipal() == null || request.getUserPrincipal().getName().contains("anonymous")))
            {
                response.sendRedirect("login.zul");
                return;
            }
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
        super.doPost(request, response);
    }
}
