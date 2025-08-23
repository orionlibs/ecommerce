package de.hybris.platform.cockpit.components.login;

import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.servicelayer.user.UserService;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Toolbarbutton;

public class AnonymousLoginButton extends Toolbarbutton
{
    private LoginService loginService;
    private UserService userService;


    public AnonymousLoginButton()
    {
        setup();
    }


    public AnonymousLoginButton(String label)
    {
        super(label);
        setup();
    }


    public AnonymousLoginButton(String label, String image)
    {
        super(label, image);
        setup();
    }


    private void setup()
    {
        addEventListener("onClick", (EventListener)new Object(this));
    }


    public LoginService getLoginService()
    {
        if(this.loginService == null)
        {
            this.loginService = (LoginService)SpringUtil.getBean("loginService");
        }
        return this.loginService;
    }


    private UserService getUserService()
    {
        if(this.userService == null)
        {
            this.userService = (UserService)SpringUtil.getBean("userService");
        }
        return this.userService;
    }
}
