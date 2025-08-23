package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.cms2.model.restrictions.CMSUserRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CMSUserRestrictionEvaluator implements CMSRestrictionEvaluator<CMSUserRestrictionModel>
{
    private static final Logger LOG = Logger.getLogger(CMSUserRestrictionEvaluator.class);
    private UserService userService;


    public boolean evaluate(CMSUserRestrictionModel cmsUserRestriction, RestrictionData context)
    {
        UserModel user = getUserService().getCurrentUser();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Current User: " + user.getUid());
            Collection<String> users = new ArrayList<>();
            for(UserModel singleUser : cmsUserRestriction.getUsers())
            {
                users.add(singleUser.getUid());
            }
            LOG.debug("Restricted Users: " + StringUtils.join(users, "; "));
        }
        return cmsUserRestriction.getUsers().contains(user);
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }
}
