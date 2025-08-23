package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.user.BackofficeRoleService;
import de.hybris.platform.catalog.daos.impl.DefaultCatalogVersionDao;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeCatalogVersionDao extends DefaultCatalogVersionDao
{
    private BackofficeRoleService backofficeRoleService;


    protected Collection<PrincipalModel> getRelatedPrincipals(PrincipalModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "User cannot be null");
        Collection<PrincipalModel> relatedPrincipals = super.getRelatedPrincipals(user);
        if(getBackofficeRoleService().shouldTreatRolesAsGroups())
        {
            return relatedPrincipals;
        }
        relatedPrincipals.retainAll(CollectionUtils.union(getRelatedPrincipalsFromActiveRole(),
                        getBackofficeRoleService().getNonRolePrincipalsHierarchy(user)));
        return relatedPrincipals;
    }


    protected Collection<PrincipalModel> getRelatedPrincipalsFromActiveRole()
    {
        return getBackofficeRoleService().getActiveRoleModel().map(user -> super.getRelatedPrincipals((PrincipalModel)user)).orElseGet(Collections::emptySet);
    }


    protected BackofficeRoleService getBackofficeRoleService()
    {
        return this.backofficeRoleService;
    }


    @Required
    public void setBackofficeRoleService(BackofficeRoleService backofficeRoleService)
    {
        this.backofficeRoleService = backofficeRoleService;
    }
}
