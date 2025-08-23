package de.hybris.platform.cmscockpit.components.login;

import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;

public class LoginTextBox extends Textbox
{
    protected static final String COCKPIT_ID_LOGIN_USERID_INPUT = "Login_UserId_input";


    public LoginTextBox()
    {
        UITools.applyTestID((Component)this, "Login_UserId_input");
    }


    public LoginTextBox(String value)
    {
        super(value);
        UITools.applyTestID((Component)this, "Login_UserId_input");
    }
}
