package com.hybris.backoffice.daos;

import de.hybris.platform.core.model.security.UserRightModel;
import java.util.Collection;

public interface BackofficeUserRightsDao
{
    Collection<UserRightModel> findUserRightsByCode(String paramString);
}
