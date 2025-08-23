package com.hybris.samlssobackoffice;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.DefaultSSOService;
import de.hybris.platform.samlsinglesignon.model.SamlUserGroupModel;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class BackofficeSSOService extends DefaultSSOService
{
    protected static final String ENABLE_BACKOFFICE_LOGIN_PARAM = "enableBackofficeLogin";


    protected DefaultSSOService.SSOUserMapping findMappingInProperties(Collection<String> roles)
    {
        DefaultSSOService.SSOUserMapping mapping = super.findMappingInProperties(roles);
        if(mapping != null)
        {
            mapping.getParameters().put("enableBackofficeLogin", Boolean.valueOf(getEnableBackofficeLogin(roles)));
        }
        return mapping;
    }


    protected boolean getEnableBackofficeLogin(Collection<String> roles)
    {
        return roles.stream().anyMatch(role -> Registry.getCurrentTenantNoFallback().getConfig().getBoolean("sso.mapping." + role + ".enableBackofficeLogin", false));
    }


    protected DefaultSSOService.SSOUserMapping performMapping(List<SamlUserGroupModel> userGroupModels)
    {
        DefaultSSOService.SSOUserMapping mapping = super.performMapping(userGroupModels);
        boolean enableBackofficeLogin = userGroupModels.stream().anyMatch(SamlUserGroupModel::getEnableBackofficeLogin);
        mapping.getParameters().put("enableBackofficeLogin", Boolean.valueOf(enableBackofficeLogin));
        return mapping;
    }


    protected void adjustUserAttributes(UserModel user, DefaultSSOService.SSOUserMapping mapping)
    {
        super.adjustUserAttributes(user, mapping);
        if(mapping.getParameters().containsKey("enableBackofficeLogin") && user.getBackOfficeLoginDisabled() == null)
        {
            user.setBackOfficeLoginDisabled(Boolean.valueOf(!Objects.equals(mapping.getParameters().get("enableBackofficeLogin"), Boolean.valueOf(true))));
        }
    }
}
