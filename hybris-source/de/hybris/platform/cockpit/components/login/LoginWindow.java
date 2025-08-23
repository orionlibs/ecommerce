package de.hybris.platform.cockpit.components.login;

import de.hybris.platform.cockpit.forms.login.LoginForm;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Window;

public class LoginWindow extends Window
{
    public LoginWindow()
    {
        setShadow(false);
        addEventListener("onCreate", (EventListener)new Object(this));
    }


    protected LoginForm getLoginForm()
    {
        return (LoginForm)SpringUtil.getBean("LoginForm");
    }
}
