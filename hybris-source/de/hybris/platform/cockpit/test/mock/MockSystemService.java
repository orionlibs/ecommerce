package de.hybris.platform.cockpit.test.mock;

import de.hybris.platform.cockpit.services.impl.SystemServiceImpl;
import de.hybris.platform.core.model.user.UserModel;

public class MockSystemService extends SystemServiceImpl
{
    public boolean checkAttributePermissionOn(String typeCode, String attributeQualifier, String permissionCode)
    {
        return true;
    }


    public boolean checkPermissionOn(UserModel user, String typeCode, String permissionCode)
    {
        return true;
    }
}
