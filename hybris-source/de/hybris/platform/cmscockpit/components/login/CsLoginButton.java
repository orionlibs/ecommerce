package de.hybris.platform.cmscockpit.components.login;

import de.hybris.platform.cockpit.components.login.LoginButton;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;

public class CsLoginButton extends LoginButton
{
    protected static final String COCKPIT_ID_LOGIN_LOGIN_BUTTON = "Login_Login_button";


    public CsLoginButton()
    {
        UITools.applyTestID((Component)this, "Login_Login_button");
    }
}
