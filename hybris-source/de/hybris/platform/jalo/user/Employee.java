package de.hybris.platform.jalo.user;

import de.hybris.platform.directpersistence.annotation.SLDSafe;

public class Employee extends GeneratedEmployee
{
    public static final String SUPERIOR = "superior";


    @SLDSafe(portingClass = "UserService", portingMethod = "isAdmin(UserModel)")
    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAdmin()
    {
        return UserManager.getInstance().isAdmin(this);
    }
}
