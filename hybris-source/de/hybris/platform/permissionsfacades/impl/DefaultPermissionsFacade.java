package de.hybris.platform.permissionsfacades.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.permissionsfacades.PermissionsFacade;
import de.hybris.platform.permissionsfacades.data.CatalogPermissionsData;
import de.hybris.platform.permissionsfacades.data.PermissionValuesData;
import de.hybris.platform.permissionsfacades.data.PermissionsData;
import de.hybris.platform.permissionsfacades.data.SyncPermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import de.hybris.platform.permissionsfacades.strategy.ApplyPermissionsStrategy;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPermissionsFacade implements PermissionsFacade
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPermissionsFacade.class);
    private static final String PRINCIPAL_PARAM_ERROR_MESSAGE = "principalUid parameter cannot be null";
    private static final String PERMISSIONS_NAMES_PARAM_ERROR_MESSAGE = "permissionNames parameter cannot be null";
    private static final List<String> PERMISSION_NAMES = List.of("create", "read", "remove", "change", "changerights");
    private PermissionCheckingService permissionCheckingService;
    private FlexibleSearchService flexibleSearchService;
    private TypeService typeService;
    private CatalogVersionService catalogVersionService;
    private SessionService sessionService;
    private SearchRestrictionService searchRestrictionService;
    private CatalogSynchronizationService catalogSynchronizationService;
    private ApplyPermissionsStrategy applyPermissionsStrategy;
    private final Pattern attributePattern = Pattern.compile("([^\\.]+)\\.([^\\.]+)");


    public List<PermissionsData> calculateTypesPermissions(String principalUid, List<String> types, List<String> permissionNames)
    {
        ServicesUtil.validateParameterNotNull(principalUid, "principalUid parameter cannot be null");
        ServicesUtil.validateParameterNotNull(types, "types parameter cannot be null");
        ServicesUtil.validateParameterNotNull(permissionNames, "permissionNames parameter cannot be null");
        PrincipalModel principal = findPrincipal(principalUid);
        List<PermissionsData> principalPermissionsList = new ArrayList<>();
        Set<String> uniqueTypes = new LinkedHashSet<>(types);
        for(String type : uniqueTypes)
        {
            PermissionsData permissionsData = new PermissionsData();
            principalPermissionsList.add(permissionsData);
            permissionsData.setId(type);
            permissionsData.setPermissions(new HashMap<>());
            for(String permissionName : permissionNames)
            {
                PermissionCheckResult permissionCheckResult = this.permissionCheckingService.checkTypePermission(type, principal, permissionName);
                permissionsData.getPermissions().put(permissionName,
                                permissionCheckResult.isGranted() ? Boolean.toString(true) : Boolean.toString(false));
            }
        }
        return principalPermissionsList;
    }


    public List<PermissionsData> calculateAttributesPermissions(String principalUid, List<String> typeAttributes, List<String> permissionNames)
    {
        ServicesUtil.validateParameterNotNull(principalUid, "principalUid parameter cannot be null");
        ServicesUtil.validateParameterNotNull(typeAttributes, "typeAttributes parameter cannot be null");
        ServicesUtil.validateParameterNotNull(permissionNames, "permissionNames parameter cannot be null");
        List<PermissionsData> principalPermissionsList = new ArrayList<>();
        PrincipalModel principal = findPrincipal(principalUid);
        Set<String> uniqueTypes = new LinkedHashSet<>(typeAttributes);
        for(String typeAttribute : uniqueTypes)
        {
            Matcher matcher = getAttributeNameMatcher(typeAttribute);
            String type = matcher.group(1);
            String attribute = matcher.group(2);
            for(String attributeQualifier : findAllAttributesForType(type, attribute))
            {
                PermissionsData permissionsData = retrieveSingleAttributePermissions(permissionNames, type, principal, attributeQualifier);
                principalPermissionsList.add(permissionsData);
            }
        }
        return principalPermissionsList;
    }


    public PermissionsData calculateGlobalPermissions(String principalUid, List<String> permissionNames)
    {
        ServicesUtil.validateParameterNotNull(principalUid, "principalUid parameter cannot be null");
        ServicesUtil.validateParameterNotNull(permissionNames, "permissionNames parameter cannot be null");
        PrincipalModel principal = findPrincipal(principalUid);
        PermissionsData permissionsData = new PermissionsData();
        permissionsData.setId("global");
        permissionsData.setPermissions(new HashMap<>());
        for(String permissionName : permissionNames)
        {
            PermissionCheckResult permissionCheckResult = this.permissionCheckingService.checkGlobalPermission(principal, permissionName);
            permissionsData.getPermissions().put(permissionName,
                            permissionCheckResult.isGranted() ? Boolean.toString(true) : Boolean.toString(false));
        }
        return permissionsData;
    }


    protected PermissionsData retrieveSingleAttributePermissions(List<String> permissionNames, String type, PrincipalModel principal, String attributeQualifier)
    {
        PermissionsData permissionsData = new PermissionsData();
        permissionsData.setId(type + "." + type);
        permissionsData.setPermissions(new HashMap<>());
        for(String permissionName : permissionNames)
        {
            PermissionCheckResult permissionCheckResult = this.permissionCheckingService.checkAttributeDescriptorPermission(type, attributeQualifier, principal, permissionName);
            permissionsData.getPermissions().put(permissionName,
                            permissionCheckResult.isGranted() ? Boolean.toString(true) : Boolean.toString(false));
        }
        return permissionsData;
    }


    protected Matcher getAttributeNameMatcher(String typeAttribute)
    {
        if(StringUtils.isNotBlank(typeAttribute))
        {
            Matcher matcher = this.attributePattern.matcher(typeAttribute);
            if(matcher.matches())
            {
                return matcher;
            }
        }
        throw new UnknownIdentifierException("Attribute doesn't exist :" + typeAttribute);
    }


    protected List<String> findAllAttributesForType(String type, String attribute)
    {
        ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(type);
        List<AttributeDescriptorModel> attributes = new ArrayList<>();
        attributes.addAll(composedType.getInheritedattributedescriptors());
        attributes.addAll(composedType.getDeclaredattributedescriptors());
        List<String> allAttributeQualifiers = new ArrayList<>();
        for(AttributeDescriptorModel attributeDescriptor : attributes)
        {
            allAttributeQualifiers.add(attributeDescriptor.getQualifier());
        }
        if(StringUtils.equals("*", attribute))
        {
            return allAttributeQualifiers;
        }
        if(allAttributeQualifiers.contains(attribute))
        {
            return Collections.singletonList(attribute);
        }
        throw new UnknownIdentifierException("Attribute doesn't exist : " + type + "." + attribute);
    }


    public List<CatalogPermissionsData> calculateCatalogPermissions(String principalUid, List<String> catalogIds, List<String> catalogVersions)
    {
        ServicesUtil.validateParameterNotNull(principalUid, "principalUid parameter cannot be null");
        ServicesUtil.validateParameterNotNull(catalogIds, "catalogIds parameter cannot be null");
        ServicesUtil.validateParameterNotNull(catalogVersions, "catalogVersions parameter cannot be null");
        PrincipalModel principal = findPrincipal(principalUid);
        List<CatalogVersionModel> allCvs = executeWithAllCatalogs(() -> getFilteredCatalogVersions(catalogIds, catalogVersions));
        Collection<CatalogVersionModel> writableCvs = getCatalogVersionService().getAllWritableCatalogVersions(principal);
        Collection<CatalogVersionModel> readableCvs = getCatalogVersionService().getAllReadableCatalogVersions(principal);
        return (List<CatalogPermissionsData>)allCvs.stream()
                        .map(cv -> generateCatalogPermissionsDTO(cv, readableCvs.contains(cv), writableCvs.contains(cv), principal, getSyncPermissions(cv, principal)))
                        .collect(Collectors.toList());
    }


    public TypePermissionsDataList applyPermissions(TypePermissionsDataList permissionsList)
    {
        getApplyPermissionsStrategy().apply(permissionsList);
        return getCurrentPermissionsForTypes(permissionsList);
    }


    protected TypePermissionsDataList getCurrentPermissionsForTypes(TypePermissionsDataList permissionsList)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionsList", permissionsList);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionsList.principalUid", permissionsList.getPrincipalUid());
        ServicesUtil.validateParameterNotNullStandardMessage("permissionsList.permissionsList", permissionsList.getPermissionsList());
        String principalUid = permissionsList.getPrincipalUid();
        List<String> types = (List<String>)permissionsList.getPermissionsList().stream().map(TypePermissionsData::getType).collect(Collectors.toList());
        List<PermissionsData> permissionsData = calculateTypesPermissions(principalUid, types, PERMISSION_NAMES);
        List<TypePermissionsData> typePermissionsData = toTypePermissionsData(permissionsData);
        TypePermissionsDataList currentTypePermissionsData = new TypePermissionsDataList();
        currentTypePermissionsData.setPrincipalUid(principalUid);
        currentTypePermissionsData.setPermissionsList(typePermissionsData);
        return currentTypePermissionsData;
    }


    protected List<TypePermissionsData> toTypePermissionsData(List<PermissionsData> permissionsData)
    {
        List<TypePermissionsData> permissionsList = new ArrayList<>();
        for(PermissionsData data : permissionsData)
        {
            PermissionValuesData permissionValuesData = new PermissionValuesData();
            Map<String, String> permissions = data.getPermissions();
            permissionValuesData.setCreate(Boolean.valueOf(permissions.get("create")));
            permissionValuesData.setRead(Boolean.valueOf(permissions.get("read")));
            permissionValuesData.setRemove(Boolean.valueOf(permissions.get("remove")));
            permissionValuesData.setChange(Boolean.valueOf(permissions.get("change")));
            permissionValuesData.setChangerights(Boolean.valueOf(permissions.get("changerights")));
            TypePermissionsData typePermissionsData = new TypePermissionsData();
            typePermissionsData.setType(data.getId());
            typePermissionsData.setPermissions(permissionValuesData);
            permissionsList.add(typePermissionsData);
        }
        return permissionsList;
    }


    protected CatalogPermissionsData generateCatalogPermissionsDTO(CatalogVersionModel cv, boolean readPermission, boolean writePermission, PrincipalModel principal, List<SyncPermissionsData> syncPermissions)
    {
        CatalogPermissionsData permissions = new CatalogPermissionsData();
        permissions.setCatalogId(cv.getCatalog().getId());
        permissions.setCatalogVersion(cv.getVersion());
        permissions.setPermissions(new HashMap<>());
        permissions.getPermissions().put("read", Boolean.toString(readPermission));
        permissions.getPermissions().put("write", Boolean.toString(writePermission));
        permissions.setSyncPermissions(syncPermissions);
        return permissions;
    }


    protected List<SyncPermissionsData> getSyncPermissions(CatalogVersionModel catalogVersion, PrincipalModel principal)
    {
        return (List<SyncPermissionsData>)catalogVersion.getSynchronizations().stream().map(syncJob -> buildSyncPermissionData(syncJob, principal))
                        .collect(Collectors.toList());
    }


    protected SyncPermissionsData buildSyncPermissionData(SyncItemJobModel syncJob, PrincipalModel principal)
    {
        SyncPermissionsData syncPermissionsData = new SyncPermissionsData();
        syncPermissionsData.setCanSynchronize(getCatalogSynchronizationService().canSynchronize(syncJob, principal));
        syncPermissionsData.setTargetCatalogVersion(syncJob.getTargetVersion().getVersion());
        return syncPermissionsData;
    }


    protected List<CatalogVersionModel> getFilteredCatalogVersions(List<String> catalogIds, List<String> catalogVersions)
    {
        Set<String> uniqueCatalogs = new LinkedHashSet<>(catalogIds);
        Set<String> uniqueVersions = new LinkedHashSet<>(catalogVersions);
        List<CatalogVersionModel> result = new ArrayList<>();
        uniqueCatalogs.forEach(cId -> uniqueVersions.forEach(()));
        return result;
    }


    protected PrincipalModel findPrincipal(String principalUid)
    {
        PrincipalModel example = new PrincipalModel();
        example.setUid(principalUid);
        return (PrincipalModel)this.flexibleSearchService.getModelByExample(example);
    }


    protected <T> T executeWithAllCatalogs(Supplier<T> action)
    {
        return executeInLocalView(() -> {
            setAllCatalogs();
            return action.get();
        });
    }


    protected <T> T executeInLocalView(Supplier<T> action)
    {
        return (T)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, action));
    }


    protected void setAllCatalogs()
    {
        try
        {
            this.searchRestrictionService.disableSearchRestrictions();
            this.catalogVersionService.setSessionCatalogVersions(this.catalogVersionService.getAllCatalogVersions());
        }
        finally
        {
            this.searchRestrictionService.enableSearchRestrictions();
        }
    }


    protected PermissionCheckingService getPermissionCheckingService()
    {
        return this.permissionCheckingService;
    }


    @Required
    public void setPermissionCheckingService(PermissionCheckingService permissionCheckingService)
    {
        this.permissionCheckingService = permissionCheckingService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected CatalogSynchronizationService getCatalogSynchronizationService()
    {
        return this.catalogSynchronizationService;
    }


    @Required
    public void setCatalogSynchronizationService(CatalogSynchronizationService catalogSynchronizationService)
    {
        this.catalogSynchronizationService = catalogSynchronizationService;
    }


    protected ApplyPermissionsStrategy getApplyPermissionsStrategy()
    {
        return this.applyPermissionsStrategy;
    }


    @Required
    public void setApplyPermissionsStrategy(ApplyPermissionsStrategy strategy)
    {
        this.applyPermissionsStrategy = strategy;
    }
}
