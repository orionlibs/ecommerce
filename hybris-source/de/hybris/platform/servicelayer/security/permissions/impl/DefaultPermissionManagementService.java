package de.hybris.platform.servicelayer.security.permissions.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.security.PermissionContainer;
import de.hybris.platform.persistence.meta.MetaInformationEJB;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementStrategy;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementStrategyFactory;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.logging.Logs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPermissionManagementService implements PermissionManagementService
{
    private static final Logger LOG = Logger.getLogger(DefaultPermissionManagementService.class);
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private PermissionManagementStrategyFactory permissionManagementStrategyFactory;


    public Collection<String> getDefinedPermissions()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {code} FROM {UserRight}");
        fQuery.setResultClassList(Lists.newArrayList((Object[])new Class[] {String.class}));
        SearchResult<String> result = this.flexibleSearchService.search(fQuery);
        return result.getResult();
    }


    public Collection<PermissionAssignment> getItemPermissions(ItemModel item)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        if(this.modelService.isNew(item))
        {
            return Collections.emptyList();
        }
        Collection<PK> restrictedPrincipals = this.permissionManagementStrategyFactory.getStrategy().getRestrictedPrincipals(item.getPk());
        List<PrincipalModel> principals = new ArrayList<>();
        if(restrictedPrincipals != null)
        {
            for(PK principalPK : restrictedPrincipals)
            {
                Optional<PrincipalModel> principal = getModelForPk(principalPK);
                if(principal.isPresent())
                {
                    principals.add(principal.get());
                }
            }
        }
        return getItemPermissionsForPrincipals(item, principals);
    }


    public Collection<PermissionAssignment> getItemPermissionsForPrincipal(ItemModel item, PrincipalModel... principal)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        return getItemPermissionsForPrincipals(item, Arrays.asList(principal));
    }


    public Collection<PermissionAssignment> getItemPermissionsForName(ItemModel item, String... permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        return getItemPermissionsForName(item, Arrays.asList(permissionName));
    }


    public Collection<PermissionAssignment> getTypePermissions(ComposedTypeModel type)
    {
        return getItemPermissions((ItemModel)type);
    }


    public Collection<PermissionAssignment> getTypePermissionsForPrincipal(ComposedTypeModel type, PrincipalModel... principal)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("type", type);
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        return getItemPermissionsForPrincipals((ItemModel)type, Arrays.asList(principal));
    }


    public Collection<PermissionAssignment> getTypePermissionsForName(ComposedTypeModel type, String... permissionName)
    {
        return getItemPermissionsForName((ItemModel)type, Arrays.asList(permissionName));
    }


    public Collection<PermissionAssignment> getAttributePermissions(AttributeDescriptorModel attribute)
    {
        return getItemPermissions((ItemModel)attribute);
    }


    public Collection<PermissionAssignment> getAttributePermissionsForPrincipal(AttributeDescriptorModel attribute, PrincipalModel... principal)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("attribute", attribute);
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        return getItemPermissionsForPrincipals((ItemModel)attribute, Arrays.asList(principal));
    }


    public Collection<PermissionAssignment> getAttributePermissionsForName(AttributeDescriptorModel attribute, String... permissionName)
    {
        return getItemPermissionsForName((ItemModel)attribute, Arrays.asList(permissionName));
    }


    public Collection<PermissionAssignment> getGlobalPermissionsForPrincipal(PrincipalModel... principal)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        return getGlobalPermissionsForPrincipal(Arrays.asList(principal));
    }


    @Deprecated(since = "6.0.0", forRemoval = true)
    public Collection<PermissionAssignment> getGlobalPermissionsForName(String... permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        return getGlobalPermissionsForName(Arrays.asList(permissionName));
    }


    public void createPermission(String permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        UserRightModel newUserRight = (UserRightModel)this.modelService.create(UserRightModel.class);
        newUserRight.setCode(permissionName);
        this.modelService.save(newUserRight);
    }


    public void addItemPermission(ItemModel item, PermissionAssignment... permissionAssignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionAssignment", permissionAssignment);
        addItemPermissions(item, Arrays.asList(permissionAssignment));
    }


    public void addItemPermissions(ItemModel item, Collection<PermissionAssignment> permissionAssignments)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        validatePermissionAssignments(permissionAssignments);
        addPermissions(item, permissionAssignments);
    }


    public void setItemPermissions(ItemModel item, Collection<PermissionAssignment> permissionAssignments)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        validatePermissionAssignments(permissionAssignments);
        setPermissions(item, permissionAssignments);
    }


    public void removeItemPermission(ItemModel item, PermissionAssignment... permissionAssignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionAssignment", permissionAssignment);
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        removeItemPermissions(item, Arrays.asList(permissionAssignment));
    }


    public void removeItemPermissions(ItemModel item, Collection<PermissionAssignment> permissionAssignments)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        validatePermissionAssignments(permissionAssignments);
        removePermissions(item, permissionAssignments);
    }


    public void removeItemPermissionsForPrincipal(ItemModel item, PrincipalModel... principal)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        removePermissionsForPrincipals(item, Arrays.asList(principal));
    }


    public void removeItemPermissionsForName(ItemModel item, String... permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        removePermissionsForNames(item, Arrays.asList(permissionName));
    }


    public void clearItemPermissions(ItemModel item)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        clearPermissions(item);
    }


    public void addTypePermission(ComposedTypeModel type, PermissionAssignment... permissionAssignment)
    {
        addItemPermission((ItemModel)type, permissionAssignment);
    }


    public void addTypePermissions(ComposedTypeModel type, Collection<PermissionAssignment> permissionAssignments)
    {
        addItemPermissions((ItemModel)type, permissionAssignments);
    }


    public void setTypePermissions(ComposedTypeModel type, Collection<PermissionAssignment> permissionAssignments)
    {
        setItemPermissions((ItemModel)type, permissionAssignments);
    }


    public void removeTypePermission(ComposedTypeModel type, PermissionAssignment... permissionAssignment)
    {
        removeItemPermission((ItemModel)type, permissionAssignment);
    }


    public void removeTypePermissions(ComposedTypeModel type, Collection<PermissionAssignment> permissionAssignments)
    {
        removeItemPermissions((ItemModel)type, permissionAssignments);
    }


    public void removeTypePermissionsForPrincipal(ComposedTypeModel type, PrincipalModel... principal)
    {
        removeItemPermissionsForPrincipal((ItemModel)type, principal);
    }


    public void removeTypePermissionsForName(ComposedTypeModel type, String... permissionName)
    {
        removeItemPermissionsForName((ItemModel)type, permissionName);
    }


    public void clearTypePermissions(ComposedTypeModel type)
    {
        clearItemPermissions((ItemModel)type);
    }


    public void addAttributePermission(AttributeDescriptorModel attribute, PermissionAssignment... permissionAssignment)
    {
        addItemPermission((ItemModel)attribute, permissionAssignment);
    }


    public void addAttributePermissions(AttributeDescriptorModel attribute, Collection<PermissionAssignment> permissionAssignments)
    {
        addItemPermissions((ItemModel)attribute, permissionAssignments);
    }


    public void setAttributePermissions(AttributeDescriptorModel attribute, Collection<PermissionAssignment> permissionAssignments)
    {
        setItemPermissions((ItemModel)attribute, permissionAssignments);
    }


    public void removeAttributePermission(AttributeDescriptorModel attribute, PermissionAssignment... permissionAssignment)
    {
        removeItemPermission((ItemModel)attribute, permissionAssignment);
    }


    public void removeAttributePermissions(AttributeDescriptorModel attribute, Collection<PermissionAssignment> permissionAssignments)
    {
        removeItemPermissions((ItemModel)attribute, permissionAssignments);
    }


    public void removeAttributePermissionsForPrincipal(AttributeDescriptorModel attribute, PrincipalModel... principal)
    {
        removeItemPermissionsForPrincipal((ItemModel)attribute, principal);
    }


    public void removeAttributePermissionsForName(AttributeDescriptorModel attribute, String... permissionName)
    {
        removeItemPermissionsForName((ItemModel)attribute, permissionName);
    }


    public void clearAttributePermissions(AttributeDescriptorModel attribute)
    {
        clearItemPermissions((ItemModel)attribute);
    }


    public void addGlobalPermission(PermissionAssignment... permissionAssignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionAssignment", permissionAssignment);
        addGlobalPermissions(Arrays.asList(permissionAssignment));
    }


    public void addGlobalPermissions(Collection<PermissionAssignment> permissionAssignments)
    {
        addGlobalPermissionsInternal(permissionAssignments);
    }


    public void removeGlobalPermission(PermissionAssignment... permissionAssignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionAssignment", permissionAssignment);
        removeGlobalPermissions(Arrays.asList(permissionAssignment));
    }


    public void removeGlobalPermissions(Collection<PermissionAssignment> permissionAssignments)
    {
        removeGlobalPermissionsInternal(permissionAssignments);
    }


    public void removeGlobalPermissionsForPrincipal(PrincipalModel... principal)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        removeGlobalPermissionsForPrincipals(Arrays.asList(principal));
    }


    public void removeGlobalPermissionsForName(String... permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        removeGlobalPermissionsForNames(Arrays.asList(permissionName));
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected PK getPermissionPKForName(String permissionName)
    {
        UserRightModel userRight = getPermissionForName(permissionName);
        Preconditions.checkArgument((userRight != null), "No such permission: \"" + permissionName + "\"");
        return userRight.getPk();
    }


    protected UserRightModel getPermissionForName(String permissionName)
    {
        String query = "SELECT {pk} FROM {UserRight} WHERE {code}=?code";
        Map<String, String> params = new HashMap<>();
        params.put("code", permissionName);
        SearchResult<UserRightModel> result = this.flexibleSearchService.search("SELECT {pk} FROM {UserRight} WHERE {code}=?code", params);
        int numFound = result.getCount();
        if(numFound > 0)
        {
            if(numFound > 1)
            {
                LOG.error("Found more than one permissions for name " + permissionName + " [permissions:" + result
                                .getResult() + "]");
            }
            return result.getResult().get(0);
        }
        return null;
    }


    protected void addPermissions(ItemModel item, Collection<PermissionAssignment> permissionAssignments)
    {
        if(this.modelService.isNew(item))
        {
            LOG.warn("Cannot add persmissions to not saved model, skipping");
            return;
        }
        if(permissionAssignments.isEmpty())
        {
            return;
        }
        List<PermissionContainer> permissions = (List<PermissionContainer>)permissionAssignments.stream().map(this::toPermissionContainer).collect(Collectors.toList());
        this.permissionManagementStrategyFactory.getStrategy().writePermissionsForItem(item.getPk(), permissions);
    }


    protected void setPermissions(ItemModel item, Collection<PermissionAssignment> permissionAssignments)
    {
        if(this.modelService.isNew(item))
        {
            LOG.warn("Cannot set persmissions on not saved model, skipping");
            return;
        }
        clearPermissions(item);
        if(CollectionUtils.isNotEmpty(permissionAssignments))
        {
            addPermissions(item, permissionAssignments);
        }
    }


    protected void removePermissions(ItemModel item, Collection<PermissionAssignment> permissionAssignments)
    {
        if(this.modelService.isNew(item))
        {
            LOG.warn("Cannot remove persmissions from not saved model, skipping");
            return;
        }
        if(permissionAssignments.isEmpty())
        {
            return;
        }
        List<PermissionContainer> permissions = (List<PermissionContainer>)permissionAssignments.stream().map(this::toPermissionContainer).collect(Collectors.toList());
        this.permissionManagementStrategyFactory.getStrategy().removePermissionsByContainers(item.getPk(), permissions);
    }


    private PermissionContainer toPermissionContainer(PermissionAssignment permissionAssignment)
    {
        PK principalPK = permissionAssignment.getPrincipal().getPk();
        PK rightPK = getPermissionPKForName(permissionAssignment.getPermissionName());
        boolean negative = permissionAssignment.isDenied();
        return new PermissionContainer(principalPK, rightPK, negative);
    }


    protected void removePermissionsForPrincipals(ItemModel item, Collection<PrincipalModel> principals)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        validatePrincipals(principals);
        if(this.modelService.isNew(item))
        {
            LOG.warn("Cannot remove persmissions from not saved model, skipping");
        }
        if(principals.isEmpty())
        {
            return;
        }
        List<PK> principalsPK = new ArrayList<>();
        for(PrincipalModel principal : principals)
        {
            principalsPK.add(principal.getPk());
        }
        removePermissionsForPrincipalsPK(item, principalsPK);
    }


    protected void removePermissionsForPrincipalsPK(ItemModel item, Collection<PK> principals)
    {
        if(this.modelService.isNew(item))
        {
            LOG.warn("Cannot remove persmissions from not saved model, skipping");
        }
        this.permissionManagementStrategyFactory.getStrategy().removePermissionsByPrincipalPks(item.getPk(), asList(principals));
    }


    private <T> List<T> asList(Collection<T> collection)
    {
        return (List<T>)ImmutableList.builder().addAll(collection).build();
    }


    protected void removePermissionsForNames(ItemModel item, Collection<String> permissionNames)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        validatePermissionNames(permissionNames);
        if(this.modelService.isNew(item))
        {
            LOG.warn("Cannot remove persmissions from not saved model, skipping");
        }
        if(permissionNames.isEmpty())
        {
            return;
        }
        List<PK> rightsPKs = getRighPKsForNames(permissionNames);
        this.permissionManagementStrategyFactory.getStrategy().removePermissionsByPermissionPks(item.getPk(), rightsPKs);
    }


    private List<PK> getRighPKsForNames(Collection<String> permissionNames)
    {
        return (List<PK>)permissionNames.stream().map(this::getPermissionPKForName).collect(Collectors.toList());
    }


    protected void clearPermissions(ItemModel item)
    {
        if(this.modelService.isNew(item))
        {
            LOG.warn("Cannot clear persmissions from not saved model, skipping");
        }
        Collection<PK> restrictedPrincipals = this.permissionManagementStrategyFactory.getStrategy().getRestrictedPrincipals(item.getPk());
        if(CollectionUtils.isNotEmpty(restrictedPrincipals))
        {
            removePermissionsForPrincipalsPK(item, restrictedPrincipals);
        }
    }


    protected void addGlobalPermissionsInternal(Collection<PermissionAssignment> permissionAssignments)
    {
        validatePermissionAssignments(permissionAssignments);
        if(permissionAssignments.isEmpty())
        {
            return;
        }
        Map<PrincipalModel, List<PermissionAssignment>> groupByPrincipalMap = new HashMap<>();
        for(PermissionAssignment permissionAssignment : permissionAssignments)
        {
            if(!groupByPrincipalMap.containsKey(permissionAssignment.getPrincipal()))
            {
                groupByPrincipalMap.put(permissionAssignment.getPrincipal(), new ArrayList<>());
            }
            ((List<PermissionAssignment>)groupByPrincipalMap.get(permissionAssignment.getPrincipal())).add(permissionAssignment);
        }
        for(Map.Entry<PrincipalModel, List<PermissionAssignment>> entry : groupByPrincipalMap.entrySet())
        {
            PK principalPK = ((PrincipalModel)entry.getKey()).getPk();
            List<PermissionContainer> permissions = new ArrayList<>();
            for(PermissionAssignment permissionAssignment : entry.getValue())
            {
                PK rightPK = getPermissionPKForName(permissionAssignment.getPermissionName());
                boolean negative = permissionAssignment.isDenied();
                permissions.add(new PermissionContainer(principalPK, rightPK, negative));
            }
            this.permissionManagementStrategyFactory.getStrategy().writeGlobalPermissions(principalPK, permissions);
        }
    }


    protected void removeGlobalPermissionsInternal(Collection<PermissionAssignment> permissionAssignments)
    {
        validatePermissionAssignments(permissionAssignments);
        if(permissionAssignments.isEmpty())
        {
            return;
        }
        Map<PrincipalModel, List<PermissionAssignment>> groupByPrincipalMap = new HashMap<>();
        for(PermissionAssignment permissionAssignment : permissionAssignments)
        {
            if(!groupByPrincipalMap.containsKey(permissionAssignment.getPrincipal()))
            {
                groupByPrincipalMap.put(permissionAssignment.getPrincipal(), new ArrayList<>());
            }
            ((List<PermissionAssignment>)groupByPrincipalMap.get(permissionAssignment.getPrincipal())).add(permissionAssignment);
        }
        for(Map.Entry<PrincipalModel, List<PermissionAssignment>> entry : groupByPrincipalMap.entrySet())
        {
            PK principalPK = ((PrincipalModel)entry.getKey()).getPk();
            List<PermissionContainer> permissions = new ArrayList<>();
            for(PermissionAssignment permissionAssignment : entry.getValue())
            {
                PK rightPK = getPermissionPKForName(permissionAssignment.getPermissionName());
                boolean negative = permissionAssignment.isDenied();
                permissions.add(new PermissionContainer(principalPK, rightPK, negative));
            }
            this.permissionManagementStrategyFactory.getStrategy().removeGlobalPermissions(principalPK, permissions);
        }
    }


    protected void removeGlobalPermissionsForPrincipals(Collection<PrincipalModel> principals)
    {
        validatePrincipals(principals);
        if(principals.isEmpty())
        {
            return;
        }
        PermissionManagementStrategy strategy = this.permissionManagementStrategyFactory.getStrategy();
        for(PrincipalModel principal : principals)
        {
            Collection<PK> positiveUserRightPKs = new HashSet<>();
            Collection<PK> negativeUserRightPKs = new HashSet<>();
            positiveUserRightPKs.addAll(strategy.getGlobalNegativePermissions(principal.getPk()));
            negativeUserRightPKs.addAll(strategy.getGlobalPositivePermissions(principal.getPk()));
            List<PermissionContainer> permissions = new ArrayList<>();
            for(PK userRightPK : positiveUserRightPKs)
            {
                permissions.add(new PermissionContainer(principal.getPk(), userRightPK, false));
            }
            for(PK userRightPK : negativeUserRightPKs)
            {
                permissions.add(new PermissionContainer(principal.getPk(), userRightPK, true));
            }
            strategy.removeGlobalPermissions(principal.getPk(), permissions);
        }
    }


    protected void removeGlobalPermissionsForNames(Collection<String> permissionNames)
    {
        validatePermissionNames(permissionNames);
        if(permissionNames.isEmpty())
        {
            return;
        }
        List<PK> rightsPK = getRighPKsForNames(permissionNames);
        this.permissionManagementStrategyFactory.getStrategy().removeGlobalPermissionsByPermissionPks(rightsPK);
    }


    protected Collection<PermissionAssignment> getGlobalPermissionsForPrincipal(Collection<PrincipalModel> principals)
    {
        validatePrincipals(principals);
        if(principals.isEmpty())
        {
            return Collections.emptyList();
        }
        Collection<PermissionAssignment> permissionAssignments = new ArrayList<>();
        for(PrincipalModel p : principals)
        {
            Collection<PK> positiveRightPKs = this.permissionManagementStrategyFactory.getStrategy().getGlobalPositivePermissions(p.getPk());
            for(PK rightPK : positiveRightPKs)
            {
                String permissionName = ((UserRightModel)this.modelService.get(rightPK)).getCode();
                permissionAssignments.add(new PermissionAssignment(permissionName, p));
            }
            Collection<PK> negativeRightPKs = this.permissionManagementStrategyFactory.getStrategy().getGlobalNegativePermissions(p.getPk());
            for(PK rightPK : negativeRightPKs)
            {
                String permissionName = ((UserRightModel)this.modelService.get(rightPK)).getCode();
                permissionAssignments.add(new PermissionAssignment(permissionName, p, true));
            }
        }
        return permissionAssignments;
    }


    protected Collection<PermissionAssignment> getGlobalPermissionsForName(Collection<String> permissionNames)
    {
        validatePermissionNames(permissionNames);
        if(permissionNames.isEmpty())
        {
            return Collections.emptyList();
        }
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {ur.code}, acl.Negative, acl.PrincipalPK FROM {UserRight as ur} JOIN " + getAclentriesTableName() + " acl ON item_t0.PK=acl.PermissionPK WHERE acl.ItemPk=?metaPk AND {ur.code} IN (?permNames)");
        fQuery.setResultClassList(Lists.newArrayList((Object[])new Class[] {String.class, Boolean.class, PrincipalModel.class}));
        fQuery.addQueryParameter("metaPk", MetaInformationEJB.DEFAULT_PRIMARY_KEY);
        fQuery.addQueryParameter("permNames", permissionNames);
        SearchResult<List<?>> res = this.flexibleSearchService.search(fQuery);
        List<List<?>> result = res.getResult();
        return (Collection<PermissionAssignment>)result.stream().map(row -> new PermissionAssignment(row.get(0), (PrincipalModel)row.get(2), ((Boolean)row.get(1)).booleanValue()))
                        .collect(Collectors.toList());
    }


    private String getAclentriesTableName()
    {
        return Config.getString("db.tableprefix", "") + "aclentries";
    }


    protected Collection<PermissionAssignment> getItemPermissionsForPrincipals(ItemModel item, Collection<PrincipalModel> principals)
    {
        if(principals.contains(null))
        {
            throw new IllegalArgumentException("principal cannot be null");
        }
        Collection<PermissionAssignment> results = new ArrayList<>();
        for(PrincipalModel principal : principals)
        {
            Collection<PK> positivePermissionPKs = this.permissionManagementStrategyFactory.getStrategy().getPositivePermissions(item.getPk(), principal
                            .getPk());
            for(PK permissionPK : positivePermissionPKs)
            {
                String permissionName = ((UserRightModel)this.modelService.get(permissionPK)).getCode();
                results.add(new PermissionAssignment(permissionName, principal));
            }
            Collection<PK> negativePermissionPKs = this.permissionManagementStrategyFactory.getStrategy().getNegativePermissions(item.getPk(), principal
                            .getPk());
            for(PK permissionPK : negativePermissionPKs)
            {
                String permissionName = ((UserRightModel)this.modelService.get(permissionPK)).getCode();
                results.add(new PermissionAssignment(permissionName, principal, true));
            }
        }
        return results;
    }


    protected Collection<PermissionAssignment> getItemPermissionsForName(ItemModel item, List<String> permissionNames)
    {
        if(permissionNames.contains((Object)null))
        {
            throw new IllegalArgumentException("permissionName cannot be null");
        }
        if(this.modelService.isNew(item))
        {
            return Collections.emptyList();
        }
        Collection<PermissionAssignment> results = new ArrayList<>();
        List<PK> rightsPK = getRighPKsForNames(permissionNames);
        Map<PK, List<Boolean>> principalsPermissions = this.permissionManagementStrategyFactory.getStrategy().getPrincipalsPermissions(item
                        .getPk(), rightsPK);
        if(principalsPermissions.isEmpty())
        {
            return Collections.emptyList();
        }
        for(Map.Entry<PK, List<Boolean>> entry : principalsPermissions.entrySet())
        {
            List<Boolean> permissionMapping = entry.getValue();
            if(permissionMapping != null && !permissionMapping.isEmpty())
            {
                for(int i = 0; i < permissionMapping.size(); i++)
                {
                    Boolean assignmentValue = permissionMapping.get(i);
                    if(i < permissionNames.size() && assignmentValue != null)
                    {
                        Optional<PrincipalModel> principal = getModelForPk(entry.getKey());
                        if(principal.isPresent())
                        {
                            String permissionName = permissionNames.get(i);
                            results.add(new PermissionAssignment(permissionName, principal
                                            .get(), assignmentValue.booleanValue()));
                        }
                    }
                }
            }
        }
        return results;
    }


    private <T extends ItemModel> Optional<T> getModelForPk(PK pk)
    {
        try
        {
            return Optional.of((T)this.modelService.get(pk));
        }
        catch(ModelLoadingException e)
        {
            Logs.debug(LOG, () -> "Try to get model for PK: " + pk + " which is already removed, skipping");
            return Optional.empty();
        }
    }


    private void validatePrincipals(Collection<PrincipalModel> principals)
    {
        Preconditions.checkArgument((principals != null), "principals cannot be null");
        Preconditions.checkArgument(!principals.contains(null), "principals cannot cointain null");
    }


    private void validatePermissionAssignments(Collection<PermissionAssignment> permissionAssignments)
    {
        Preconditions.checkArgument((permissionAssignments != null), "permissionAssignments cannot be null");
        Preconditions.checkArgument(!permissionAssignments.contains(null), "permissionAssignments cannot cointain null");
    }


    private void validatePermissionNames(Collection<String> permissionNames)
    {
        Preconditions.checkArgument((permissionNames != null), "permissionNames cannot be null");
        Preconditions.checkArgument(!permissionNames.contains(null), "permissionNames cannot contain null");
    }


    @Required
    public void setPermissionManagementStrategyFactory(PermissionManagementStrategyFactory permissionManagementStrategyFactory)
    {
        this.permissionManagementStrategyFactory = permissionManagementStrategyFactory;
    }
}
