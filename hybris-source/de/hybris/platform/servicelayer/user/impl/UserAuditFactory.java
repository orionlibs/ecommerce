package de.hybris.platform.servicelayer.user.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.BruteForceLoginDisabledAuditModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.model.user.UserPasswordChangeAuditModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Required;

public class UserAuditFactory
{
    protected UserService userService;
    protected ModelService modelService;
    protected static final String IP_ADDRESS = "RemoteAddr";


    public UserPasswordChangeAuditModel createUserPasswordChangeAudit(PK pk, String uid, String previousPassword, String previousEncoding)
    {
        UserPasswordChangeAuditModel userAudit = (UserPasswordChangeAuditModel)this.modelService.create(UserPasswordChangeAuditModel.class);
        userAudit.setUid(uid);
        userAudit.setUserPK(pk.getLong());
        userAudit.setEncodedPassword(previousPassword);
        userAudit.setPasswordEncoding(previousEncoding);
        userAudit.setIpAddress(LoggingContextFactory.getLoggingContextHandler().get("RemoteAddr"));
        userAudit.setChangingUser(determineCurrentUser());
        userAudit.setChangingApplication(determineContextPath());
        return processAfterCreation(userAudit);
    }


    public BruteForceLoginDisabledAuditModel createBruteForceLoginDisabledAudit(UserModel user, Integer numFailedLogins)
    {
        BruteForceLoginDisabledAuditModel audit = (BruteForceLoginDisabledAuditModel)this.modelService.create(BruteForceLoginDisabledAuditModel.class);
        audit.setUserPK(user.getPk().getLong());
        audit.setUid(user.getUid());
        audit.setFailedLogins(numFailedLogins);
        return processAfterCreation(audit);
    }


    protected <X extends de.hybris.platform.core.model.user.AbstractUserAuditModel> X processAfterCreation(X user)
    {
        return user;
    }


    protected String determineContextPath()
    {
        ServletContext bean = Registry.getServletContextIfExists();
        return (bean != null) ? bean.getContextPath() : null;
    }


    protected String determineCurrentUser()
    {
        UserModel currentUser = this.userService.getCurrentUser();
        if(currentUser != null)
        {
            return currentUser.getUid();
        }
        return null;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
