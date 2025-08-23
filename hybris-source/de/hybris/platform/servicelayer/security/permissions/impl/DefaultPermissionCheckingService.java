package de.hybris.platform.servicelayer.security.permissions.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionChecker;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementStrategyFactory;
import de.hybris.platform.servicelayer.security.strategies.PermissionCheckValueMappingStrategy;
import de.hybris.platform.servicelayer.security.strategies.PrincipalHierarchyCheckingStrategy;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPermissionCheckingService implements PermissionCheckingService
{
    public static final int NOT_FOUND = -1;
    public static final int POSITIVE = 0;
    public static final int NEGATIVE = 1;
    public static final int EVEN = 2;
    private UserService userService;
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private TypeService typeService;
    private PermissionCheckValueMappingStrategy permissionCheckValMappingStrategy;
    private PrincipalHierarchyCheckingStrategy principalHierarchyCheckingStrategy;
    private PermissionManagementStrategyFactory permissionManagementStrategyFactory;


    public PermissionCheckResult checkItemPermission(ItemModel item, PrincipalModel principal, String permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("item", item);
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        PermissionCheckValue value = checkItemPermissionForPrincipalHierarchy(item, principal, permissionName);
        if(value == PermissionCheckValue.NOT_DEFINED)
        {
            return checkTypePermission(this.typeService.getComposedTypeForCode(item.getItemtype()), principal, permissionName);
        }
        return getPermissionCheckValMappingStrategy().getPermissionCheckResult(value);
    }


    public PermissionCheckResult checkItemPermission(ItemModel item, String permissionName)
    {
        return checkItemPermission(item, getDefaultPrincipal(), permissionName);
    }


    public PermissionCheckResult checkTypePermission(ComposedTypeModel type, PrincipalModel principal, String permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("type", type);
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        PermissionCheckValue value = checkTypePermissionForTypeHierarchy(type, principal, permissionName);
        if(value == PermissionCheckValue.NOT_DEFINED)
        {
            return checkGlobalPermission(principal, permissionName);
        }
        return getPermissionCheckValMappingStrategy().getPermissionCheckResult(value);
    }


    public PermissionCheckResult checkTypePermission(ComposedTypeModel type, String permissionName)
    {
        return checkTypePermission(type, getDefaultPrincipal(), permissionName);
    }


    public PermissionCheckResult checkTypePermission(String typeCode, PrincipalModel principal, String permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("typeCode", typeCode);
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(typeCode);
        return checkTypePermission(type, principal, permissionName);
    }


    public PermissionCheckResult checkTypePermission(String typeCode, String permissionName)
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(typeCode);
        return checkTypePermission(type, getDefaultPrincipal(), permissionName);
    }


    public PermissionCheckResult checkAttributeDescriptorPermission(AttributeDescriptorModel attributeDescriptor, PrincipalModel principal, String permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("attributeDescriptor", attributeDescriptor);
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        PermissionCheckValue value = checkAttributePermission(attributeDescriptor, principal, permissionName);
        if(value == PermissionCheckValue.NOT_DEFINED)
        {
            return checkGlobalPermission(principal, permissionName);
        }
        return getPermissionCheckValMappingStrategy().getPermissionCheckResult(value);
    }


    public PermissionCheckResult checkAttributeDescriptorPermission(String typeCode, String attributeQualifier, PrincipalModel principal, String permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("typeCode", typeCode);
        ServicesUtil.validateParameterNotNullStandardMessage("attributeQualifier", attributeQualifier);
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(typeCode, attributeQualifier);
        return checkAttributeDescriptorPermission(attributeDescriptor, principal, permissionName);
    }


    public PermissionCheckResult checkAttributeDescriptorPermission(AttributeDescriptorModel attributeDescriptor, String permissionName)
    {
        return checkAttributeDescriptorPermission(attributeDescriptor, getDefaultPrincipal(), permissionName);
    }


    public PermissionCheckResult checkAttributeDescriptorPermission(String typeCode, String attributeQualifier, String permissionName)
    {
        return checkAttributeDescriptorPermission(typeCode, attributeQualifier, getDefaultPrincipal(), permissionName);
    }


    public PermissionCheckResult checkGlobalPermission(PrincipalModel principal, String permissionName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        Object object = new Object(this);
        PermissionCheckValue value = genericPermissionCheckingForPrincipalHierarchy((PermissionChecker)object, principal, permissionName);
        return getPermissionCheckValMappingStrategy().getPermissionCheckResult(value);
    }


    public PermissionCheckResult checkGlobalPermission(String permissionName)
    {
        return checkGlobalPermission(getDefaultPrincipal(), permissionName);
    }


    protected PermissionCheckValue checkPermissionForGlobal(PrincipalModel principal, String permissionName)
    {
        PK permissionPK = getPermissionPKForName(permissionName);
        if(permissionPK == null)
        {
            return PermissionCheckValue.NOT_DEFINED;
        }
        return translateItemCheckingOutcome(this.permissionManagementStrategyFactory
                        .getStrategy().checkGlobalPermission(principal.getPk(), permissionPK));
    }


    protected PermissionCheckValue checkPermissionForItem(ItemModel item, PrincipalModel principal, String permissionName)
    {
        PK permissionPK = getPermissionPKForName(permissionName);
        if(permissionPK == null)
        {
            return PermissionCheckValue.NOT_DEFINED;
        }
        return translateItemCheckingOutcome(this.permissionManagementStrategyFactory
                        .getStrategy().checkItemPermission(item.getPk(), principal
                                        .getPk(), permissionPK));
    }


    protected PermissionCheckValue translateItemCheckingOutcome(int rawMatch)
    {
        switch(rawMatch)
        {
            case -1:
                result = PermissionCheckValue.NOT_DEFINED;
                return result;
            case 2:
                result = PermissionCheckValue.CONFLICTING;
                return result;
            case 1:
                result = PermissionCheckValue.DENIED;
                return result;
            case 0:
                result = PermissionCheckValue.ALLOWED;
                return result;
        }
        PermissionCheckValue result = PermissionCheckValue.NOT_DEFINED;
        return result;
    }


    protected PermissionCheckValue checkItemPermissionForPrincipalHierarchy(ItemModel item, PrincipalModel principal, String permissionName)
    {
        Object object = new Object(this, item);
        return genericPermissionCheckingForPrincipalHierarchy((PermissionChecker)object, principal, permissionName);
    }


    protected PermissionCheckValue checkTypePermissionForTypeHierarchy(ComposedTypeModel type, PrincipalModel principal, String permissionName)
    {
        if(principal instanceof UserModel)
        {
            if(this.userService.isAdmin((UserModel)principal))
            {
                return PermissionCheckValue.ALLOWED;
            }
        }
        PermissionCheckValue result = checkItemPermissionForPrincipalHierarchy((ItemModel)type, principal, permissionName);
        if(PermissionCheckValue.NOT_DEFINED == result)
        {
            ComposedTypeModel superType = type.getSuperType();
            for(; PermissionCheckValue.NOT_DEFINED == result && superType != null; superType = superType.getSuperType())
            {
                result = checkItemPermissionForPrincipalHierarchy((ItemModel)superType, principal, permissionName);
            }
        }
        return result;
    }


    private AttributeDescriptorModel getSuperAttributeDescriptor(AttributeDescriptorModel attribute)
    {
        ComposedTypeModel superEnclosingType = attribute.getEnclosingType().getSuperType();
        if(superEnclosingType != null)
        {
            for(AttributeDescriptorModel availableAttribute : superEnclosingType.getDeclaredattributedescriptors())
            {
                if(availableAttribute.getQualifier().equals(attribute.getQualifier()))
                {
                    return this.typeService.getAttributeDescriptor(superEnclosingType, attribute.getQualifier());
                }
            }
            for(AttributeDescriptorModel availableAttribute : superEnclosingType.getInheritedattributedescriptors())
            {
                if(availableAttribute.getQualifier().equals(attribute.getQualifier()))
                {
                    return this.typeService.getAttributeDescriptor(superEnclosingType, attribute.getQualifier());
                }
            }
        }
        return null;
    }


    protected PermissionCheckValue checkAttributePermission(AttributeDescriptorModel attributeDescriptor, PrincipalModel principal, String permissionName)
    {
        PermissionCheckValue match;
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        if(principal instanceof UserModel)
        {
            if(this.userService.isAdmin((UserModel)principal))
            {
                return PermissionCheckValue.ALLOWED;
            }
        }
        ComposedTypeModel enclosing = null;
        AttributeDescriptorModel currentAttribute = attributeDescriptor;
        do
        {
            match = checkItemPermissionForPrincipalHierarchy((ItemModel)currentAttribute, principal, permissionName);
            if(match != PermissionCheckValue.NOT_DEFINED)
            {
                continue;
            }
            enclosing = currentAttribute.getEnclosingType();
            match = checkItemPermissionForPrincipalHierarchy((ItemModel)enclosing, principal, permissionName);
            if(match != PermissionCheckValue.NOT_DEFINED)
            {
                continue;
            }
            currentAttribute = getSuperAttributeDescriptor(currentAttribute);
        }
        while(match == PermissionCheckValue.NOT_DEFINED && currentAttribute != null);
        if(match == PermissionCheckValue.NOT_DEFINED && enclosing != null)
        {
            for(enclosing = enclosing.getSuperType(); enclosing != null; enclosing = enclosing.getSuperType())
            {
                match = checkItemPermissionForPrincipalHierarchy((ItemModel)enclosing, principal, permissionName);
                if(match != PermissionCheckValue.NOT_DEFINED)
                {
                    break;
                }
            }
        }
        return match;
    }


    protected PermissionCheckValue genericPermissionCheckingForPrincipalHierarchy(PermissionChecker permissionChecker, PrincipalModel principal, String permissionName)
    {
        return getPrincipalHierarchyCheckingStrategy().checkPermissionsForPrincipalHierarchy(permissionChecker, principal, permissionName);
    }


    protected PrincipalModel getDefaultPrincipal()
    {
        return (PrincipalModel)this.userService.getCurrentUser();
    }


    protected PK getPermissionPKForName(String permissionName)
    {
        UserRightModel userRight = getPermissionForName(permissionName);
        if(userRight == null)
        {
            return null;
        }
        return userRight.getPk();
    }


    protected UserRightModel getPermissionForName(String permissionName)
    {
        String query = "SELECT {pk} FROM {UserRight} WHERE {code}=?code";
        ImmutableMap immutableMap = ImmutableMap.of("code", permissionName);
        SearchResult<UserRightModel> results = this.flexibleSearchService.search("SELECT {pk} FROM {UserRight} WHERE {code}=?code", (Map)immutableMap);
        if(results.getCount() == 1)
        {
            return results.getResult().get(0);
        }
        return null;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
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


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public PermissionCheckValueMappingStrategy getPermissionCheckValMappingStrategy()
    {
        return this.permissionCheckValMappingStrategy;
    }


    @Required
    public void setPermissionCheckValMappingStrategy(PermissionCheckValueMappingStrategy permissionCheckValMappingStrategy)
    {
        this.permissionCheckValMappingStrategy = permissionCheckValMappingStrategy;
    }


    public PrincipalHierarchyCheckingStrategy getPrincipalHierarchyCheckingStrategy()
    {
        return this.principalHierarchyCheckingStrategy;
    }


    @Required
    public void setPrincipalHierarchyCheckingStrategy(PrincipalHierarchyCheckingStrategy principalHierarchyCheckingStrategy)
    {
        this.principalHierarchyCheckingStrategy = principalHierarchyCheckingStrategy;
    }


    @Required
    public void setPermissionManagementStrategyFactory(PermissionManagementStrategyFactory permissionManagementStrategyFactory)
    {
        this.permissionManagementStrategyFactory = permissionManagementStrategyFactory;
    }
}
