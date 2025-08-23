package de.hybris.platform.cockpit.session;

import de.hybris.platform.core.model.user.UserModel;

public interface UISessionListener
{
    void afterLogin(UserModel paramUserModel);


    void beforeLogout(UserModel paramUserModel);


    void perspectiveChanged(UICockpitPerspective paramUICockpitPerspective1, UICockpitPerspective paramUICockpitPerspective2);


    void globalDataLanguageChanged();
}
