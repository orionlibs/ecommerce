package de.hybris.platform.permissionsfacades.security.impl;

import de.hybris.platform.permissionsfacades.PermissionsFacade;
import de.hybris.platform.permissionsfacades.data.CatalogPermissionsData;
import de.hybris.platform.permissionsfacades.security.CatalogPermissionsChecker;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public class DefaultCatalogPermissionsChecker implements CatalogPermissionsChecker
{
    private PermissionsFacade permissionsFacade;
    private static final List<String> WRITE_HTTP_METHODS = Arrays.asList(new String[] {"POST", "PUT", "PATCH", "DELETE"});
    private static final List<String> READ_HTTP_METHODS = Collections.singletonList("GET");


    public boolean hasAccessToCatalog(Authentication authentication, HttpServletRequest request, String catalog, String catalogVersion)
    {
        if(WRITE_HTTP_METHODS.contains(request.getMethod()))
        {
            return hasCatalogPermission(authentication.getName(), catalog, catalogVersion, "write");
        }
        if(READ_HTTP_METHODS.contains(request.getMethod()))
        {
            return hasCatalogPermission(authentication.getName(), catalog, catalogVersion, "read");
        }
        return false;
    }


    protected boolean hasCatalogPermission(String uid, String catalog, String catalogVersion, String permissionName)
    {
        List<CatalogPermissionsData> permissions = this.permissionsFacade.calculateCatalogPermissions(uid,
                        Collections.singletonList(catalog), Collections.singletonList(catalogVersion));
        Optional<Map.Entry<String, String>> permission = permissions.stream().filter(p -> (catalog.equals(p.getCatalogId()) && catalogVersion.equals(p.getCatalogVersion()))).flatMap(p -> p.getPermissions().entrySet().stream())
                        .filter(entry -> (permissionName.equals(entry.getKey()) && Boolean.TRUE.toString().equals(entry.getValue()))).findAny();
        return permission.isPresent();
    }


    protected PermissionsFacade getPermissionsFacade()
    {
        return this.permissionsFacade;
    }


    public void setPermissionsFacade(PermissionsFacade permissionsFacade)
    {
        this.permissionsFacade = permissionsFacade;
    }
}
