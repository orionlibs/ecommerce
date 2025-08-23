package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.core.model.user.UserModel;
import java.util.List;

public interface UIConfigurationService
{
    List<UIRole> getPossibleRoles();


    List<UIRole> getPossibleRoles(UserModel paramUserModel);


    UIRole getFallbackRole();


    UIRole getSessionRole();


    void setSessionRole(UIRole paramUIRole);


    void setSessionRole(String paramString);


    <T extends UIComponentConfiguration> T getComponentConfiguration(UIRole paramUIRole, ObjectTemplate paramObjectTemplate, String paramString, Class<T> paramClass);


    <T extends UIComponentConfiguration> T getComponentConfiguration(ObjectTemplate paramObjectTemplate, String paramString, Class<T> paramClass);


    <T extends UIComponentConfiguration> void setLocalComponentConfiguration(T paramT, UserModel paramUserModel, ObjectTemplate paramObjectTemplate, String paramString, Class<T> paramClass);
}
