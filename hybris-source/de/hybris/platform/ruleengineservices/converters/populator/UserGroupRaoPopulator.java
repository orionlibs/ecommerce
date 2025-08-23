package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;

public class UserGroupRaoPopulator implements Populator<UserGroupModel, UserGroupRAO>
{
    public void populate(UserGroupModel source, UserGroupRAO target)
    {
        target.setId(source.getUid());
    }
}
