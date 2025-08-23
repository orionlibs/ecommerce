package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CMSUserGroupRestrictionEvaluator implements CMSRestrictionEvaluator<CMSUserGroupRestrictionModel>
{
    private static final Logger LOG = Logger.getLogger(CMSUserGroupRestrictionEvaluator.class);
    private UserService userService;


    public boolean evaluate(CMSUserGroupRestrictionModel cmsUserGroupRestriction, RestrictionData context)
    {
        Collection<UserGroupModel> groups = cmsUserGroupRestriction.getUserGroups();
        UserModel currentUserModel = getUserService().getCurrentUser();
        Set<PrincipalGroupModel> userGroups = new HashSet<>(currentUserModel.getGroups());
        if(cmsUserGroupRestriction.isIncludeSubgroups())
        {
            userGroups.addAll(getSubgroups(userGroups));
        }
        List<String> restrGroupNames = new ArrayList<>();
        for(UserGroupModel group : groups)
        {
            restrGroupNames.add(group.getUid());
        }
        List<String> currentGroupNames = new ArrayList<>();
        for(PrincipalGroupModel group : userGroups)
        {
            currentGroupNames.add(group.getUid());
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Current UserGroups: " + StringUtils.join(currentGroupNames, "; "));
            LOG.debug("Restricted UserGroups: " + StringUtils.join(restrGroupNames, "; "));
        }
        for(String group : restrGroupNames)
        {
            if(currentGroupNames.contains(group))
            {
                return true;
            }
        }
        return false;
    }


    protected List<PrincipalGroupModel> getSubgroups(Collection<PrincipalGroupModel> groups)
    {
        List<PrincipalGroupModel> ret = new ArrayList<>(groups);
        for(PrincipalGroupModel principalGroup : groups)
        {
            ret.addAll(getSubgroups(principalGroup.getGroups()));
        }
        return ret;
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
