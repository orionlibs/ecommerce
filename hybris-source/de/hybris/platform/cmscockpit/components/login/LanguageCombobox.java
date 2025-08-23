package de.hybris.platform.cmscockpit.components.login;

import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

public class LanguageCombobox extends Combobox
{
    protected static final String COCKPIT_ID_LOGIN_USERLANGUAGE_DROPDOWN = "Login_Language_dropdown";


    public LanguageCombobox()
    {
        UITools.applyTestID((Component)this, "Login_Language_dropdown");
    }


    public LanguageCombobox(String value)
    {
        super(value);
        UITools.applyTestID((Component)this, "Login_Language_dropdown");
    }
}
