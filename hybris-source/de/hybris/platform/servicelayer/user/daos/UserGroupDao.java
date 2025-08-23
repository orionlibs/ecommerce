package de.hybris.platform.servicelayer.user.daos;

import de.hybris.platform.core.model.user.UserGroupModel;

public interface UserGroupDao
{
    UserGroupModel findUserGroupByUid(String paramString);
}
