package de.hybris.platform.personalizationwebservices.security.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.permissionsfacades.PermissionsFacade;
import de.hybris.platform.permissionsfacades.data.CatalogPermissionsData;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.security.QueryEndpointPermissionsChecker;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.ForbiddenException;

public class DefaultQueryEndpointPermissionsChecker implements QueryEndpointPermissionsChecker
{
    private PermissionsFacade permissionsFacade;
    private UserService userService;


    public void checkCurrentUserAllowed(List<CatalogVersionWsDTO> readAccessRequiredCatalogs, List<CatalogVersionWsDTO> writeAccessRequiredCatalogs)
    {
        Preconditions.checkArgument((this.userService.getCurrentUser() != null));
        String uid = this.userService.getCurrentUser().getUid();
        Preconditions.checkArgument((uid != null));
        if(this.userService.isAdmin(this.userService.getCurrentUser()))
        {
            return;
        }
        List<CatalogVersionWsDTO> cvs = new ArrayList<>(readAccessRequiredCatalogs);
        cvs.addAll(writeAccessRequiredCatalogs);
        List<String> allCatalogIds = (List<String>)cvs.stream().map(cv -> cv.getCatalog()).collect(Collectors.toList());
        List<String> allCatalogVersions = (List<String>)cvs.stream().map(cv -> cv.getVersion()).collect(Collectors.toList());
        List<CatalogPermissionsData> permissions = this.permissionsFacade.calculateCatalogPermissions(uid, allCatalogIds, allCatalogVersions);
        readAccessRequiredCatalogs.stream()
                        .filter(cv -> !hasCatalogReadPermission(permissions, cv))
                        .findAny().ifPresent(i -> {
                            throw new ForbiddenException("Access is denied.");
                        });
        writeAccessRequiredCatalogs.stream()
                        .filter(cv -> !hasCatalogWritePermission(permissions, cv))
                        .findAny().ifPresent(i -> {
                            throw new ForbiddenException("Access is denied.");
                        });
    }


    private boolean hasCatalogReadPermission(List<CatalogPermissionsData> permissions, CatalogVersionWsDTO cv)
    {
        return hasCatalogPermission(permissions, cv.getCatalog(), cv.getVersion(), "read");
    }


    private boolean hasCatalogWritePermission(List<CatalogPermissionsData> permissions, CatalogVersionWsDTO cv)
    {
        return hasCatalogPermission(permissions, cv.getCatalog(), cv.getVersion(), "write");
    }


    protected boolean hasCatalogPermission(List<CatalogPermissionsData> permissions, String catalog, String catalogVersion, String permissionName)
    {
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


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
