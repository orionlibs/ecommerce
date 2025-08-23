package de.hybris.platform.cockpit.components.login;

import de.hybris.platform.cockpit.forms.login.LoginForm;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;

public class LoginButton extends Button
{
    public LoginButton()
    {
        addEventListener("onCreate", (EventListener)new Object(this));
        addEventListener("onClick", (EventListener)new Object(this));
    }


    protected LoginForm getLoginForm()
    {
        return (LoginForm)SpringUtil.getBean("LoginForm");
    }


    protected void doOK()
    {
        Clients.submitForm(getFellow("loginForm"));
    }
}
