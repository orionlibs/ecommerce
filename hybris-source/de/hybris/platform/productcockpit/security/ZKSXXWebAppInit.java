package de.hybris.platform.productcockpit.security;

import org.zkoss.web.servlet.http.Encodes;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

public class ZKSXXWebAppInit implements WebAppInit
{
    public void init(WebApp webApp) throws Exception
    {
        Encodes.setURLEncoder((Encodes.URLEncoder)new ZKXSSURLEncoder());
    }
}
