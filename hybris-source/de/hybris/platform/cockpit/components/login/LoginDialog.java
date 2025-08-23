package de.hybris.platform.cockpit.components.login;

import de.hybris.platform.cockpit.forms.login.LoginForm;
import de.hybris.platform.cockpit.util.UITools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class LoginDialog extends Window
{
    private static final Logger LOG = LoggerFactory.getLogger(LoginDialog.class);


    public LoginDialog()
    {
        setShadow(false);
        UITools.addBusyListener((Component)this, "onOK", (EventListener)new Object(this), null, null);
        addEventListener("onCreate", (EventListener)new Object(this));
    }


    protected void doOK()
    {
        Clients.submitForm(getFellow("loginForm"));
    }


    protected LoginForm getLoginForm()
    {
        return (LoginForm)SpringUtil.getBean("LoginForm");
    }


    protected void showTimeoutNotice()
    {
        if("true".equalsIgnoreCase(Executions.getCurrent().getParameter("timeout")))
        {
            try
            {
                Messagebox.show(Labels.getLabel("timeout_notice"));
            }
            catch(InterruptedException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
