package de.hybris.platform.servicelayer.security.permissions.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.PermissionContainer;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.persistence.ItemPermissionFacade;
import de.hybris.platform.persistence.security.EJBSecurityException;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementStrategy;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class JaloPermissionManagementStrategy implements PermissionManagementStrategy
{
    public void writePermissionsForItem(PK itemPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(permissions, "permissions is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        try
        {
            permissionFacade.setPermissions(permissions);
        }
        catch(EJBSecurityException e)
        {
            throw new IllegalStateException(e);
        }
    }


    public void writeGlobalPermissions(PK itemPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(permissions, "permissions is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        try
        {
            permissionFacade.setGlobalPermissions(permissions);
        }
        catch(EJBSecurityException e)
        {
            throw new IllegalStateException(e);
        }
    }


    public void removePermissionsByContainers(PK itemPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(permissions, "permissions is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        try
        {
            permissionFacade.removePermissions(permissions);
        }
        catch(EJBSecurityException e)
        {
            throw new IllegalStateException(e);
        }
    }


    public void removePermissionsByPrincipalPks(PK itemPk, List<PK> principalPks)
    {
        Map<PK, Set<PK>> principalPKToPermissionsPKMap = new HashMap<>();
        for(PK principalPK : principalPks)
        {
            if(!principalPKToPermissionsPKMap.containsKey(principalPK))
            {
                principalPKToPermissionsPKMap.put(principalPK, new HashSet<>());
            }
            Set<PK> permissionsSet = principalPKToPermissionsPKMap.get(principalPK);
            permissionsSet.addAll(getItemPermissions(itemPk, principalPK));
        }
        List<PermissionContainer> permissions = new ArrayList<>();
        for(Map.Entry<PK, Set<PK>> entry : principalPKToPermissionsPKMap.entrySet())
        {
            PK principalPK = entry.getKey();
            if(!((Set)entry.getValue()).isEmpty())
            {
                for(PK rightPK : entry.getValue())
                {
                    permissions.add(new PermissionContainer(principalPK, rightPK, false));
                }
            }
        }
        if(!permissions.isEmpty())
        {
            removePermissionsByContainers(itemPk, permissions);
        }
    }


    public void removePermissionsByPermissionPks(PK itemPk, List<PK> permissionsPKs)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(permissionsPKs), "permissionsPKs cannot be null or empty");
        Preconditions.checkArgument(!permissionsPKs.contains(null), "permissionsPKs cannot be null");
        List<PK> _permissionsPKs = asList(permissionsPKs);
        Map<PK, List<Boolean>> principalsPermissions = getPrincipalsPermissions(itemPk, _permissionsPKs);
        if(principalsPermissions.isEmpty())
        {
            return;
        }
        List<PermissionContainer> permissions = new ArrayList<>();
        for(Map.Entry<PK, List<Boolean>> entry : principalsPermissions.entrySet())
        {
            List<Boolean> permissionMapping = entry.getValue();
            if(permissionMapping != null && !permissionMapping.isEmpty())
            {
                for(int i = 0; i < permissionMapping.size(); i++)
                {
                    Boolean mappingValue = permissionMapping.get(i);
                    if(mappingValue != null)
                    {
                        PK principalPK = entry.getKey();
                        PK rightPK = _permissionsPKs.get(i);
                        permissions.add(new PermissionContainer(principalPK, rightPK, false));
                    }
                }
            }
        }
        if(!permissions.isEmpty())
        {
            removePermissionsByContainers(itemPk, permissions);
        }
    }


    private Collection<PK> getItemPermissions(PK itemPk, PK principalPk)
    {
        Set<PK> result = new HashSet<>();
        result.addAll(getPositivePermissions(itemPk, principalPk));
        result.addAll(getNegativePermissions(itemPk, principalPk));
        return result;
    }


    public void removeGlobalPermissions(PK itemPk, List<PermissionContainer> permissions)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(permissions, "permissions is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        try
        {
            permissionFacade.removeGlobalPermissions(permissions);
        }
        catch(EJBSecurityException e)
        {
            throw new IllegalStateException(e);
        }
    }


    public void removeGlobalPermissionsByPermissionPks(List<PK> permissionPKs)
    {
        AccessManager accessManager = AccessManager.getInstance();
        Map<PK, List<PK>> principalToPermissionListMap = new HashMap<>();
        for(PK permissionPK : permissionPKs)
        {
            Collection<Principal> principals = (Collection<Principal>)wrap(accessManager.getGlobalRestrictedPrincipals(permissionPK));
            for(Principal principal : principals)
            {
                PK principalPK = principal.getPK();
                if(!principalToPermissionListMap.containsKey(principalPK))
                {
                    principalToPermissionListMap.put(principalPK, new ArrayList<>());
                }
                ((List<PK>)principalToPermissionListMap.get(principalPK)).add(permissionPK);
            }
        }
        Collection<PermissionContainer> permissions = new ArrayList<>();
        for(Map.Entry<PK, List<PK>> permissionEntry : principalToPermissionListMap.entrySet())
        {
            PK principalPK = permissionEntry.getKey();
            for(PK rightPK : permissionEntry.getValue())
            {
                permissions.add(new PermissionContainer(principalPK, rightPK, false));
            }
        }
        removeGlobalPermissionsForPermissionContainers(permissions);
    }


    private Object wrap(Object object)
    {
        return WrapperFactory.wrap(Registry.getCurrentTenant().getCache(), object);
    }


    private void removeGlobalPermissionsForPermissionContainers(Collection<PermissionContainer> permissions)
    {
        if(permissions.isEmpty())
        {
            return;
        }
        Map<PK, List<PermissionContainer>> groupByPrincipalMap = new HashMap<>();
        for(PermissionContainer permissionContainer : permissions)
        {
            if(!groupByPrincipalMap.containsKey(permissionContainer.getPrincipalPK()))
            {
                groupByPrincipalMap.put(permissionContainer.getPrincipalPK(), new ArrayList<>());
            }
            ((List<PermissionContainer>)groupByPrincipalMap.get(permissionContainer.getPrincipalPK())).add(permissionContainer);
        }
        for(Map.Entry<PK, List<PermissionContainer>> entry : groupByPrincipalMap.entrySet())
        {
            PK principalPK = entry.getKey();
            List<PermissionContainer> value = entry.getValue();
            removeGlobalPermissions(principalPK, value);
        }
    }


    public Map<PK, List<Boolean>> getPrincipalsPermissions(PK itemPk, List<PK> rightPKs)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(rightPKs, "rightPKs is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        Map<ItemPropertyValue, List<Boolean>> fromItemRemote = permissionFacade.getPrincipalToBooleanListMap(rightPKs);
        return (Map<PK, List<Boolean>>)fromItemRemote.entrySet().stream().filter(e -> (e.getKey() != null))
                        .collect(Collectors.toMap(e -> ((ItemPropertyValue)e.getKey()).getPK(), Map.Entry::getValue));
    }


    public List<PK> getRestrictedPrincipals(PK itemPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        return asList(permissionFacade.getRestrictedPrincipalPKs());
    }


    public List<PK> getPositivePermissions(PK itemPk, PK principalPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(principalPk, "principalPk is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        return asList(permissionFacade.getPermissionPKs(principalPk, false));
    }


    public List<PK> getNegativePermissions(PK itemPk, PK principalPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(principalPk, "principalPk is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        return asList(permissionFacade.getPermissionPKs(principalPk, true));
    }


    public List<PK> getGlobalPositivePermissions(PK principalPk)
    {
        Objects.requireNonNull(principalPk, "principalPk is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(principalPk);
        return asList(permissionFacade.getGlobalPermissionPKs(false));
    }


    public List<PK> getGlobalNegativePermissions(PK principalPk)
    {
        Objects.requireNonNull(principalPk, "principalPk is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(principalPk);
        return asList(permissionFacade.getGlobalPermissionPKs(true));
    }


    private <T> List<T> asList(Collection<T> collection)
    {
        return (List<T>)ImmutableList.builder().addAll(collection).build();
    }


    public int checkItemPermission(PK itemPk, PK principalPk, PK permissionPk)
    {
        Objects.requireNonNull(itemPk, "itemPk is required");
        Objects.requireNonNull(principalPk, "principalPk is required");
        Objects.requireNonNull(permissionPk, "principalPk is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(itemPk);
        return permissionFacade.checkItemPermission(principalPk, permissionPk);
    }


    public int checkGlobalPermission(PK principalPk, PK permissionPk)
    {
        Objects.requireNonNull(principalPk, "principalPk is required");
        Objects.requireNonNull(permissionPk, "principalPk is required");
        ItemPermissionFacade permissionFacade = getItemPermissionFacade(principalPk);
        return permissionFacade.checkOwnGlobalPermission(permissionPk);
    }


    protected ItemPermissionFacade getItemPermissionFacade(PK pk)
    {
        Item sourceItem = JaloSession.getCurrentSession().getItem(pk);
        return sourceItem.getImplementation().getPermissionFacade();
    }
}
