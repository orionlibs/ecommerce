package de.hybris.platform.servicelayer.security.permissions.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.jalo.security.PermissionContainer;
import de.hybris.platform.persistence.meta.MetaInformationEJB;
import de.hybris.platform.servicelayer.security.permissions.PermissionDAO;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementStrategy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class SLDPermissionManagementStrategy implements PermissionManagementStrategy
{
    private PermissionDAO permissionDAO;
    private CacheInvalidator cacheInvalidator;


    public void writePermissionsForItem(PK itemPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(permissions, "permissions collection is required");
        this.permissionDAO.upsertAclsForItem(itemPk, permissions);
        triggerItemInvalidation(itemPk);
    }


    public void writeGlobalPermissions(PK principalPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(principalPk, "itemPk is required");
        Objects.requireNonNull(permissions, "permissions collection is required");
        this.permissionDAO.upsertAclsForItem(getGlobalAclItemPk(), permissions);
        triggerItemInvalidation(principalPk);
    }


    public void removePermissionsByContainers(PK itemPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(permissions, "permissions collection is required");
        this.permissionDAO.deleteAclsForItem(itemPk, permissions);
        triggerItemInvalidation(itemPk);
    }


    public void removePermissionsByPrincipalPks(PK itemPk, List<PK> principalPks)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(principalPks), "principalPks cannot be null or empty");
        this.permissionDAO.deleteAclsForItemAndPrincipals(itemPk, principalPks);
        triggerItemInvalidation(itemPk);
    }


    public void removePermissionsByPermissionPks(PK itemPk, List<PK> permissionsPKs)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(permissionsPKs), "permissionsPKs cannot be null or empty");
        this.permissionDAO.deleteAclsForPermissions(itemPk, permissionsPKs);
        triggerItemInvalidation(itemPk);
    }


    public void removeGlobalPermissions(PK principalPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(principalPk, "itemPk is required");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(permissions), "permissions cannot be null or empty");
        this.permissionDAO.deleteAclsForItem(getGlobalAclItemPk(), permissions);
        triggerItemInvalidation(principalPk);
    }


    public void removeGlobalPermissionsByPermissionPks(List<PK> permissionPKs)
    {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(permissionPKs), "permissionPKs cannot be null or empty");
        List<PK> principals = this.permissionDAO.findGlobalRestrictedPrincipalsForPermissions(permissionPKs);
        this.permissionDAO.deleteAclsForItemAndPermissions(getGlobalAclItemPk(), permissionPKs);
        principals.stream().forEach(this::triggerItemInvalidation);
    }


    private void triggerItemInvalidation(PK principalOrItemPk)
    {
        this.cacheInvalidator.invalidate(Collections.singleton(new DefaultPersistResult(CrudEnum.UPDATE, principalOrItemPk)));
    }


    PK getGlobalAclItemPk()
    {
        return MetaInformationEJB.DEFAULT_PRIMARY_KEY;
    }


    public Map<PK, List<Boolean>> getPrincipalsPermissions(PK itemPk, List<PK> rightPKs)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(rightPKs, "rightPKs list is required");
        PermissionDAO.AclContainer aclsForItem = getItemAclContainer(itemPk);
        return (Map<PK, List<Boolean>>)aclEntries(aclsForItem).stream()
                        .filter(e -> rightPKs.contains(e.getPermissionPk()))
                        .collect(Collectors.groupingBy(PermissionDAO.AclEntry::getUserPk,
                                        Collectors.mapping(PermissionDAO.AclEntry::getNegative, Collectors.toList())));
    }


    public List<PK> getRestrictedPrincipals(PK itemPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        PermissionDAO.AclContainer aclsForItem = getItemAclContainer(itemPk);
        return (List<PK>)aclEntries(aclsForItem).stream().map(PermissionDAO.AclEntry::getUserPk).collect(Collectors.toList());
    }


    public List<PK> getPositivePermissions(PK itemPk, PK principalPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(principalPk, "principalPk is required");
        PermissionDAO.AclContainer aclsForItem = getItemAclContainer(itemPk);
        return (List<PK>)aclEntries(aclsForItem).stream()
                        .filter(e -> e.getUserPk().equals(principalPk))
                        .filter(PermissionDAO.AclEntry::isPositive)
                        .map(PermissionDAO.AclEntry::getPermissionPk)
                        .collect(Collectors.toList());
    }


    public List<PK> getNegativePermissions(PK itemPk, PK principalPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(principalPk, "principalPk is required");
        PermissionDAO.AclContainer aclsForItem = getItemAclContainer(itemPk);
        return (List<PK>)aclEntries(aclsForItem).stream()
                        .filter(e -> e.getUserPk().equals(principalPk))
                        .filter(PermissionDAO.AclEntry::isNegative)
                        .map(PermissionDAO.AclEntry::getPermissionPk)
                        .collect(Collectors.toList());
    }


    public List<PK> getGlobalPositivePermissions(PK principalPk)
    {
        Objects.requireNonNull(principalPk, "principalPk is required");
        PermissionDAO.AclContainer globalAcls = getGlobalAclContainer(principalPk);
        return (List<PK>)aclEntries(globalAcls).stream()
                        .filter(e -> e.getUserPk().equals(principalPk))
                        .filter(PermissionDAO.AclEntry::isPositive)
                        .map(PermissionDAO.AclEntry::getPermissionPk)
                        .collect(Collectors.toList());
    }


    public List<PK> getGlobalNegativePermissions(PK principalPk)
    {
        Objects.requireNonNull(principalPk, "principalPk is required");
        PermissionDAO.AclContainer globalAcls = getGlobalAclContainer(principalPk);
        return (List<PK>)aclEntries(globalAcls).stream()
                        .filter(e -> e.getUserPk().equals(principalPk))
                        .filter(PermissionDAO.AclEntry::isNegative)
                        .map(PermissionDAO.AclEntry::getPermissionPk)
                        .collect(Collectors.toList());
    }


    public int checkItemPermission(PK itemPk, PK principalPk, PK permissionPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(principalPk, "principalPk is required");
        Objects.requireNonNull(permissionPk, "permissionPk is required");
        PermissionDAO.AclContainer aclsForItem = getItemAclContainer(itemPk);
        PermissionDAO.AclEntry permission = aclsForItem.findPermission(principalPk, permissionPk);
        return getPermissionIntValue(permission);
    }


    public int checkGlobalPermission(PK principalPk, PK permissionPk)
    {
        Objects.requireNonNull(principalPk, "principalPk is required");
        Objects.requireNonNull(permissionPk, "permissionPk is required");
        PermissionDAO.AclContainer globalAcls = getGlobalAclContainer(principalPk);
        PermissionDAO.AclEntry permission = globalAcls.findPermission(principalPk, permissionPk);
        return getPermissionIntValue(permission);
    }


    private int getPermissionIntValue(PermissionDAO.AclEntry permission)
    {
        return (permission == null) ? -1 : (
                        permission.isNegative() ? 1 : 0);
    }


    private PermissionDAO.AclContainer getGlobalAclContainer(PK principalPk)
    {
        AclCacheUnit cacheUnit = AclCacheUnit.globalCacheUnit(getCache(), principalPk, () -> this.permissionDAO.findGlobalAclsForPrincipalPk(principalPk));
        return cacheUnit.getAsContainer();
    }


    private PermissionDAO.AclContainer getItemAclContainer(PK itemPk)
    {
        AclCacheUnit cacheUnit = AclCacheUnit.itemCacheUnit(getCache(), itemPk, () -> this.permissionDAO.findAclsForItem(itemPk));
        return cacheUnit.getAsContainer();
    }


    private Collection<PermissionDAO.AclEntry> aclEntries(PermissionDAO.AclContainer acls)
    {
        return acls.getAllEntries().values();
    }


    Cache getCache()
    {
        return Registry.getCurrentTenant().getCache();
    }


    @Required
    public void setPermissionDAO(PermissionDAO permissionDAO)
    {
        this.permissionDAO = permissionDAO;
    }


    @Required
    public void setCacheInvalidator(CacheInvalidator cacheInvalidator)
    {
        this.cacheInvalidator = cacheInvalidator;
    }
}
