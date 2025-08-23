package de.hybris.platform.cockpit.session;

import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

public class UISessionUtils
{
    private static final String SESSION_BEAN_ID = "UICockpitSession";


    public static UISession getCurrentSession()
    {
        UISession ret = null;
        Execution execution = Executions.getCurrent();
        if(execution != null)
        {
            ret = (UISession)execution.getAttribute("UICockpitSession");
            if(ret == null)
            {
                ret = (UISession)SpringUtil.getApplicationContext().getBean("UICockpitSession");
                execution.setAttribute("UICockpitSession", ret);
            }
        }
        return ret;
    }


    public static void killCurrentSession()
    {
    }
}
