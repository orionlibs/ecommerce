package com.hybris.backoffice.daos;

import com.hybris.backoffice.model.user.BackofficeRoleModel;
import java.util.Set;

public interface BackofficeRoleDao
{
    Set<BackofficeRoleModel> findAllBackofficeRoles();
}
