package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.security.PermissionContainer;
import java.util.List;
import java.util.Map;

public interface PermissionManagementStrategy
{
    void writePermissionsForItem(PK paramPK, List<PermissionContainer> paramList);


    void writeGlobalPermissions(PK paramPK, List<PermissionContainer> paramList);


    void removePermissionsByContainers(PK paramPK, List<PermissionContainer> paramList);


    void removePermissionsByPrincipalPks(PK paramPK, List<PK> paramList);


    void removePermissionsByPermissionPks(PK paramPK, List<PK> paramList);


    void removeGlobalPermissions(PK paramPK, List<PermissionContainer> paramList);


    void removeGlobalPermissionsByPermissionPks(List<PK> paramList);


    Map<PK, List<Boolean>> getPrincipalsPermissions(PK paramPK, List<PK> paramList);


    List<PK> getRestrictedPrincipals(PK paramPK);


    List<PK> getPositivePermissions(PK paramPK1, PK paramPK2);


    List<PK> getNegativePermissions(PK paramPK1, PK paramPK2);


    List<PK> getGlobalPositivePermissions(PK paramPK);


    List<PK> getGlobalNegativePermissions(PK paramPK);


    int checkItemPermission(PK paramPK1, PK paramPK2, PK paramPK3);


    int checkGlobalPermission(PK paramPK1, PK paramPK2);
}
