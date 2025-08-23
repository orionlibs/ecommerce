package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.core.model.user.UserModel;

public interface ConfigurationPersistingStrategy<CONFIG extends UIComponentConfiguration>
{
    public static final String COMPONENT_PERMISSION_CODE = "cockpit.personalizedconfiguration";


    void persistComponentConfiguration(CONFIG paramCONFIG, UserModel paramUserModel, ObjectTemplate paramObjectTemplate, String paramString);


    Class<CONFIG> getComponentClass();
}
