package de.hybris.platform.cmscockpit.components.login;

import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;

public class PasswordTextBox extends Textbox
{
    protected static final String COCKPIT_ID_LOGIN_USERPASSWORD_INPUT = "Login_Password_input";


    public PasswordTextBox()
    {
        UITools.applyTestID((Component)this, "Login_Password_input");
    }


    public PasswordTextBox(String value)
    {
        super(value);
        UITools.applyTestID((Component)this, "Login_Password_input");
    }
}
