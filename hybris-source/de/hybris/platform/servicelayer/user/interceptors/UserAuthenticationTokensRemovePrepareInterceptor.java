package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import java.util.Collection;
import java.util.Collections;

public class UserAuthenticationTokensRemovePrepareInterceptor implements PrepareInterceptor<UserModel>
{
    private final TimeService timeService;


    public UserAuthenticationTokensRemovePrepareInterceptor(TimeService timeService)
    {
        this.timeService = timeService;
    }


    public void onPrepare(UserModel userModel, InterceptorContext ctx) throws InterceptorException
    {
        if(userModel.isLoginDisabled() || isUserDeactivated(userModel))
        {
            Collection<OAuthAccessTokenModel> tokensToRemove = userModel.getTokens();
            if(tokensToRemove != null)
            {
                tokensToRemove.forEach(token -> ctx.registerElementFor(token, PersistenceOperation.DELETE));
            }
            userModel.setTokens(Collections.emptyList());
        }
    }


    private boolean isUserDeactivated(UserModel userModel)
    {
        if(userModel.getDeactivationDate() == null)
        {
            return false;
        }
        return !this.timeService.getCurrentTime().before(userModel.getDeactivationDate());
    }
}
