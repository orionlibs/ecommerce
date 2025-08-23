package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class UserGroupRuleParameterValueMapper implements RuleParameterValueMapper<UserGroupModel>
{
    private UserService userService;


    public String toString(UserGroupModel userGroup)
    {
        ServicesUtil.validateParameterNotNull(userGroup, "Object cannot be null");
        return userGroup.getUid();
    }


    public UserGroupModel fromString(String value)
    {
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        UserGroupModel userGroup = this.userService.getUserGroupForUID(value);
        if(userGroup == null)
        {
            throw new RuleParameterValueMapperException("Cannot find user group with the UID: " + value);
        }
        return userGroup;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
