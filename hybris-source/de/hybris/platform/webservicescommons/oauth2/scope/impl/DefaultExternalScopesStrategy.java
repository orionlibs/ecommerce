package de.hybris.platform.webservicescommons.oauth2.scope.impl;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDExternalScopesModel;
import de.hybris.platform.webservicescommons.oauth2.scope.ExternalScopesDao;
import de.hybris.platform.webservicescommons.oauth2.scope.OpenIDExternalScopesStrategy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExternalScopesStrategy implements OpenIDExternalScopesStrategy
{
    protected ExternalScopesDao externalScopesDao;
    protected UserService userService;


    public List<String> getExternalScopes(OAuthClientDetailsModel clientDetailsModel, String principal)
    {
        UserModel userModel = this.userService.getUserForUID(principal);
        return (List<String>)ImmutableList.copyOf(getScopes(clientDetailsModel, (PrincipalModel)userModel));
    }


    private Set<String> getScopes(OAuthClientDetailsModel clientDetailsModel, PrincipalModel principalModel)
    {
        Set<String> scopes = new HashSet<>();
        List<OpenIDExternalScopesModel> externalScopes = this.externalScopesDao.findScopesByClientAndPrincipal(clientDetailsModel, principalModel);
        for(OpenIDExternalScopesModel externalScopesModel : externalScopes)
        {
            scopes.addAll(externalScopesModel.getScope());
        }
        if(principalModel.getAllGroups() != null)
        {
            for(PrincipalModel principalGroupModel : principalModel.getAllGroups())
            {
                scopes.addAll(getScopes(clientDetailsModel, principalGroupModel));
            }
        }
        return scopes;
    }


    @Required
    public void setExternalScopesDao(ExternalScopesDao externalScopesDao)
    {
        this.externalScopesDao = externalScopesDao;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
