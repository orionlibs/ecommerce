package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.impl.UserAuditFactory;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeListener;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class UserPasswordChangeAuditPrepareInterceptor implements PrepareInterceptor<UserModel>
{
    private UserAuditFactory userAuditFactory;
    private PasswordChangeListener passwordChangeListener;


    public void onPrepare(UserModel userModel, InterceptorContext ctx) throws InterceptorException
    {
        UserAuditor userAuditor = new UserAuditor(this, userModel, ctx, this.userAuditFactory);
        userAuditor.executeIfContentChanged();
    }


    @Required
    public void setUserAuditFactory(UserAuditFactory userAuditFactory)
    {
        this.userAuditFactory = userAuditFactory;
    }


    private PasswordChangeListener getPasswordChangeListener()
    {
        if(this.passwordChangeListener == null)
        {
            this.passwordChangeListener = User.getPasswordChangeListener();
        }
        return this.passwordChangeListener;
    }


    public void setPasswordChangeListener(PasswordChangeListener passwordChangeListener)
    {
        this.passwordChangeListener = Objects.<PasswordChangeListener>requireNonNull(passwordChangeListener, "passwordChangeListener cannot be null.");
    }
}
