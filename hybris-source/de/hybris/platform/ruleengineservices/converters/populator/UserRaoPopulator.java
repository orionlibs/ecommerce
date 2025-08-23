package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class UserRaoPopulator implements Populator<UserModel, UserRAO>
{
    private Converter<UserGroupModel, UserGroupRAO> userGroupConverter;
    private UserService userService;


    public void populate(UserModel source, UserRAO target)
    {
        target.setId(source.getUid());
        target.setPk(source.getPk().getLongValueAsString());
        Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(source);
        if(CollectionUtils.isNotEmpty(userGroups))
        {
            target.setGroups(new LinkedHashSet(Converters.convertAll(userGroups, getUserGroupConverter())));
        }
    }


    protected Converter<UserGroupModel, UserGroupRAO> getUserGroupConverter()
    {
        return this.userGroupConverter;
    }


    @Required
    public void setUserGroupConverter(Converter<UserGroupModel, UserGroupRAO> userGroupConverter)
    {
        this.userGroupConverter = userGroupConverter;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
