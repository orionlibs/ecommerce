package de.hybris.platform.servicelayer.user.interceptors;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ModifySystemUsersInterceptor implements RemoveInterceptor, ValidateInterceptor
{
    private static final Logger LOG = Logger.getLogger(ModifySystemUsersInterceptor.class);
    public static final String ADMIN_PASSWORD_REQUIRED = "admin.password.required";
    public static final String USER_DEACTIVATION_BLOCK_FOR_ALL_ADMINS = "user.deactivation.blockForAllAdmins";
    private static final String DISABLE_CACHE = "disableCache";
    private UserService userService;
    private SessionService sessionService;
    private SearchRestrictionService searchRestrictionService;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(tryingToChangeUIDOfSystemAcount(model, ctx))
        {
            ItemModelContext mctx = ModelContextUtils.getItemModelContext((AbstractItemModel)model);
            String originalUID = mctx.isLoaded("uid") ? (String)mctx.getOriginalValue("uid") : "n/a";
            throw new InterceptorException("It is not allowed to modify the UID for the system account: " + originalUID + " !");
        }
        if(tryingToSetDisableLoginForBlockedEmployee(model, ctx))
        {
            throw new InterceptorException("It is not allowed to disable the login for this admin account");
        }
        if(tryingToSetDeactivationDateForBlockedEmployee(model, ctx))
        {
            throw new InterceptorException("It is not allowed to deactivate this admin account");
        }
        if(tryingToAssignAnonymousToAdminGroup(model, ctx))
        {
            throw new InterceptorException("It is not allowed to assign the anonymous user to any admin group...");
        }
        if(tryingToEnableLoginForAnonymousUser(model, ctx) && Config.getBoolean("login.anonymous.always.disabled", true))
        {
            LOG.warn("Changing logingDisabled for anonymous account has no effect as long as 'login.anonymous.always.disabled' is set to true. If you really want to enable login for the anonymous account you've got to set it to false!");
        }
        if(Config.getBoolean("admin.password.required", false) && tryingToSetEmptyPasswordForAdmins(model, ctx))
        {
            throw new InterceptorException("Admin password must NOT be empty!");
        }
    }


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(isSystemAccount(model))
        {
            throw new InterceptorException("It is not allowed to remove the system account: " + ((PrincipalModel)model)
                            .getUid() + " !");
        }
    }


    private boolean tryingToChangeUIDOfSystemAcount(Object model, InterceptorContext ctx)
    {
        return (isSystemAccount(model) && ctx.isModified(model, "uid"));
    }


    private boolean tryingToSetDeactivationDateForBlockedEmployee(Object model, InterceptorContext ctx)
    {
        return (isDeactivationBlockedEmployee(model) && tryingToSetDeactivationDate((EmployeeModel)model, ctx));
    }


    private boolean tryingToSetDisableLoginForBlockedEmployee(Object model, InterceptorContext ctx)
    {
        return (isDeactivationBlockedEmployee(model) && tryingToDisableLogin((EmployeeModel)model, ctx));
    }


    private boolean tryingToEnableLoginForAnonymousUser(Object model, InterceptorContext ctx)
    {
        if(!isAnonymous(model) || !ctx.isModified(model, "loginDisabled"))
        {
            return false;
        }
        return !((CustomerModel)model).isLoginDisabled();
    }


    private boolean hasAdminGroup(PrincipalModel model)
    {
        Objects.requireNonNull(this.userService);
        return model.getGroups().stream().map(ug -> (UserGroupModel)ug).anyMatch(this.userService::isAdminGroup);
    }


    private boolean tryingToSetEmptyPasswordForAdmins(Object model, InterceptorContext ctx)
    {
        return (isMemberOfAdminGroup(model) &&
                        isAdminPasswordValidationNeeded((UserModel)model, ctx) &&
                        StringUtils.isBlank(((UserModel)model).getEncodedPassword()));
    }


    private boolean tryingToAssignAnonymousToAdminGroup(Object model, InterceptorContext ctx)
    {
        return (isAnonymous(model) && ctx.isModified(model, "groups") && hasAdminGroup((PrincipalModel)model));
    }


    private boolean tryingToSetDeactivationDate(EmployeeModel model, InterceptorContext ctx)
    {
        return (ctx.isModified(model, "deactivationDate") && model
                        .getDeactivationDate() != null);
    }


    private boolean tryingToDisableLogin(EmployeeModel model, InterceptorContext ctx)
    {
        return (ctx.isModified(model, "loginDisabled") && model.isLoginDisabled());
    }


    private boolean isSystemAccount(Object model)
    {
        return (isAdminEmployee(model) || isAnonymous(model) || isAdminGroup(model));
    }


    private boolean isMemberOfAdminGroup(Object model)
    {
        return ((Boolean)this.sessionService.executeInLocalViewWithParams((Map)ImmutableMap.of("disableCache", Boolean.valueOf(true)), (SessionExecutionBody)new Object(this, model))).booleanValue();
    }


    private boolean isAdminPasswordValidationNeeded(UserModel model, InterceptorContext ctx)
    {
        return (ctx.isNew(model) || ctx.isModified(model, "encodedPassword") || ctx.isModified(model, "groups"));
    }


    private boolean isAdminEmployee(Object model)
    {
        return (model instanceof EmployeeModel && this.userService.getAdminUser().equals(model));
    }


    private boolean isAnonymous(Object model)
    {
        return (model instanceof CustomerModel && this.userService.getAnonymousUser().equals(model));
    }


    private boolean isAdminGroup(Object model)
    {
        return (model instanceof UserGroupModel && this.userService.getAdminUserGroup().equals(model));
    }


    private boolean isDeactivationBlockedEmployee(Object model)
    {
        if(model instanceof EmployeeModel)
        {
            return (this.userService.isAdminEmployee((UserModel)model) || (Config.getBoolean("user.deactivation.blockForAllAdmins", false) && this.userService
                            .isAdmin((UserModel)model)));
        }
        return false;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
