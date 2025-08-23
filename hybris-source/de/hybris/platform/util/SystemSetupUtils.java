package de.hybris.platform.util;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import javax.servlet.http.HttpSession;

public final class SystemSetupUtils
{
    private static final String INIT_METHOD_SESSION_KEY = "initMethod";
    private static final String INIT_METHOD_INIT = "INIT";
    private static final String EXECUTION_TYPE_SESSION_KEY = "executionType";
    private static final String EXECUTION_TYPE_HAC = "HAC";


    public static void setInitMethodInHttpSession(String initMethod)
    {
        HttpSession httpSession = WebSessionFunctions.getCurrentHttpSession();
        if(httpSession != null)
        {
            httpSession.setAttribute("initMethod", initMethod);
            httpSession.setAttribute("executionType", "HAC");
        }
    }


    public static boolean isInit(SystemSetupContext context)
    {
        if(context == null)
        {
            HttpSession httpSession = WebSessionFunctions.getCurrentHttpSession();
            if(httpSession != null)
            {
                return "INIT".equals(httpSession.getAttribute("initMethod"));
            }
            return false;
        }
        return context.getProcess().equals(SystemSetup.Process.INIT);
    }


    public static boolean isHAC()
    {
        HttpSession httpSession = WebSessionFunctions.getCurrentHttpSession();
        if(httpSession != null)
        {
            return "HAC".equals(httpSession.getAttribute("executionType"));
        }
        return false;
    }
}
