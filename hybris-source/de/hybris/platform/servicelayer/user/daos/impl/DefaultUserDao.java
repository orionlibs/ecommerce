package de.hybris.platform.servicelayer.user.daos.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import java.util.Collections;
import java.util.List;

public class DefaultUserDao extends DefaultGenericDao<UserModel> implements UserDao
{
    public DefaultUserDao()
    {
        super("User");
    }


    public UserModel findUserByUID(String uid)
    {
        List<UserModel> resList = find(Collections.singletonMap("uid", uid));
        if(resList.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + resList.size() + " users with the unique uid '" + uid + "'");
        }
        return resList.isEmpty() ? null : resList.get(0);
    }
}
