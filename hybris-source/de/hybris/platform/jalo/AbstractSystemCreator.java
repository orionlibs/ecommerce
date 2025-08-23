package de.hybris.platform.jalo;

import de.hybris.platform.core.Initialization;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.Collections;
import org.apache.log4j.Logger;

public abstract class AbstractSystemCreator
{
    private static final Logger LOGGER = Logger.getLogger(Initialization.class.getName());
    public static final String USE_NICE_PKS = "systemcreator.usenicepks";
    public static final String CREATE_SAMPLE_DATA = "sample data";
    protected JspContext jspContext;


    public AbstractSystemCreator(JspContext jspContext)
    {
        this.jspContext = jspContext;
    }


    protected void assureJaloSessionAvailable(JspContext jspc)
    {
        try
        {
            JaloSession jaloSession = WebSessionFunctions.getSession(Collections.EMPTY_MAP, null, jspc
                            .getServletRequest().getSession(), jspc
                            .getServletRequest(), jspc.getServletResponse());
            if(!jaloSession.getUser().isAdmin())
            {
                jaloSession.setUser((User)jaloSession.getUserManager().getAdminEmployee());
            }
        }
        catch(JaloSystemNotInitializedException e)
        {
            LOGGER.warn("cannot get jalo session because system is not initialized");
        }
        catch(Exception e)
        {
            LOGGER.warn("cannot get jalo session because cannot connect to system");
        }
    }


    protected void log(String logstring)
    {
        log(logstring, this.jspContext);
    }


    public static void log(String logstring, JspContext jspContext)
    {
        LOGGER.info(Utilities.filterOutHTMLTags(logstring));
        if(jspContext != null)
        {
            jspContext.print(logstring);
        }
    }


    public static void logln(String logstring, JspContext jspContext)
    {
        LOGGER.info(Utilities.filterOutHTMLTags(logstring));
        if(jspContext != null)
        {
            jspContext.println(logstring);
        }
    }
}
