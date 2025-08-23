package de.hybris.platform.servicelayer.user.daos.impl;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultUserGroupDao extends DefaultGenericDao<UserGroupModel> implements UserGroupDao
{
    public DefaultUserGroupDao()
    {
        super("UserGroup");
    }


    public UserGroupModel findUserGroupByUid(String uid)
    {
        return findUnique(Collections.singletonMap("uid", uid));
    }


    private UserGroupModel findUnique(Map<String, Object> params)
    {
        List<UserGroupModel> results = find(params);
        if(results.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + results.size() + " objects from type UserGroup with " + params
                            .toString() + "'");
        }
        return results.isEmpty() ? null : results.get(0);
    }
}
