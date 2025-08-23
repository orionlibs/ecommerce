package de.hybris.platform.patches.internal;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.util.JspContext;

public final class JspContextHolder
{
    private static final ThreadLocal<JspContext> jspContext = new ThreadLocal<>();


    public static void setJspContext(SystemSetupContext setupContext)
    {
        if(setupContext.getJspContext() != null)
        {
            jspContext.set(setupContext.getJspContext());
        }
    }


    public static JspContext getJspContext()
    {
        return jspContext.get();
    }


    public static void removeJspContext()
    {
        jspContext.remove();
    }
}
