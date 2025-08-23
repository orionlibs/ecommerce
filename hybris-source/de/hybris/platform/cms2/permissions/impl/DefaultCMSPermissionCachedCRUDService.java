package de.hybris.platform.cms2.permissions.impl;

import de.hybris.platform.cms2.common.service.SessionCachedContextProvider;
import de.hybris.platform.cms2.permissions.PermissionCachedCRUDService;
import de.hybris.platform.cms2.permissions.PermissionEnablerService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSPermissionCachedCRUDService implements PermissionCachedCRUDService
{
    public static final String KEY_DELIMITER = "_";
    private SessionCachedContextProvider sessionCachedContextProvider;
    private PermissionCRUDService permissionCRUDService;
    private PermissionEnablerService permissionEnablerService;


    public void initCache()
    {
        getSessionCachedContextProvider().getAllItemsFromMapCache("attributePermissionCachedResults");
        getSessionCachedContextProvider().getAllItemsFromMapCache("typePermissionCachedResults");
    }


    public boolean canReadAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedAttributePermission("read", attributeDescriptor, getPermissionCRUDService()::canReadAttribute);
    }


    public boolean canReadAttribute(String typeCode, String attributeQualifier)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedAttributePermission("read", typeCode, attributeQualifier, getPermissionCRUDService()::canReadAttribute);
    }


    public boolean canChangeAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedAttributePermission("change", attributeDescriptor, getPermissionCRUDService()::canChangeAttribute);
    }


    public boolean canChangeAttribute(String typeCode, String attributeQualifier)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedAttributePermission("change", typeCode, attributeQualifier, getPermissionCRUDService()::canChangeAttribute);
    }


    public boolean canReadType(ComposedTypeModel type)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("read", type, getPermissionCRUDService()::canReadType);
    }


    public boolean canReadType(String typeCode)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("read", typeCode, getPermissionCRUDService()::canReadType);
    }


    public boolean canChangeType(ComposedTypeModel type)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("change", type, getPermissionCRUDService()::canChangeType);
    }


    public boolean canChangeType(String typeCode)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("change", typeCode, getPermissionCRUDService()::canChangeType);
    }


    public boolean canCreateTypeInstance(ComposedTypeModel type)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("create", type, getPermissionCRUDService()::canCreateTypeInstance);
    }


    public boolean canCreateTypeInstance(String typeCode)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("create", typeCode, getPermissionCRUDService()::canCreateTypeInstance);
    }


    public boolean canRemoveTypeInstance(ComposedTypeModel type)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("remove", type, getPermissionCRUDService()::canRemoveTypeInstance);
    }


    public boolean canRemoveTypeInstance(String typeCode)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("remove", typeCode, getPermissionCRUDService()::canRemoveTypeInstance);
    }


    public boolean canChangeTypePermission(ComposedTypeModel type)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("changerights", type, getPermissionCRUDService()::canChangeTypePermission);
    }


    public boolean canChangeTypePermission(String typeCode)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedTypePermission("changerights", typeCode, getPermissionCRUDService()::canChangeTypePermission);
    }


    public boolean canChangeAttributePermission(AttributeDescriptorModel attributeDescriptor)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedAttributePermission("changerights", attributeDescriptor, getPermissionCRUDService()::canChangeAttributePermission);
    }


    public boolean canChangeAttributePermission(String typeCode, String attributeQualifier)
    {
        Objects.requireNonNull(getPermissionCRUDService());
        return getOrSetCachedAttributePermission("changerights", typeCode, attributeQualifier, getPermissionCRUDService()::canChangeAttributePermission);
    }


    protected boolean getOrSetCachedTypePermission(String permissionName, String typeCode, Function<String, Boolean> permissionChecker)
    {
        if(getPermissionEnablerService().isTypeVerifiable(typeCode))
        {
            Map<String, Boolean> cache = getSessionCachedContextProvider().getAllItemsFromMapCache("typePermissionCachedResults");
            String uniqueTypeKey = getUniqueTypeKey(permissionName, typeCode);
            Boolean hasPermission = cache.get(uniqueTypeKey);
            if(hasPermission == null)
            {
                hasPermission = permissionChecker.apply(typeCode);
                addTypePermissionToCache(permissionName, typeCode, hasPermission.booleanValue());
            }
            return hasPermission.booleanValue();
        }
        return true;
    }


    protected boolean getOrSetCachedTypePermission(String permissionName, ComposedTypeModel composedType, Function<String, Boolean> permissionChecker)
    {
        return getOrSetCachedTypePermission(permissionName, composedType.getCode(), permissionChecker);
    }


    protected boolean getOrSetCachedAttributePermission(String permissionName, String typeCode, String qualifier, BiFunction<String, String, Boolean> permissionChecker)
    {
        if(getPermissionEnablerService().isAttributeVerifiable(typeCode, qualifier))
        {
            Map<String, Boolean> cache = getSessionCachedContextProvider().getAllItemsFromMapCache("attributePermissionCachedResults");
            String uniqueAttributeKey = getUniqueAttributeKey(permissionName, typeCode, qualifier);
            Boolean hasPermission = cache.get(uniqueAttributeKey);
            if(hasPermission == null)
            {
                hasPermission = permissionChecker.apply(typeCode, qualifier);
                addAttributePermissionToCache(permissionName, typeCode, qualifier, hasPermission.booleanValue());
            }
            return hasPermission.booleanValue();
        }
        return true;
    }


    protected boolean getOrSetCachedAttributePermission(String permissionName, AttributeDescriptorModel attributeDescriptor, BiFunction<String, String, Boolean> permissionChecker)
    {
        return getOrSetCachedAttributePermission(permissionName, attributeDescriptor.getEnclosingType().getCode(), attributeDescriptor.getQualifier(), permissionChecker);
    }


    protected void addAttributePermissionToCache(String permissionName, String typeCode, String qualifier, boolean hasPermission)
    {
        String uniqueAttributeKey = getUniqueAttributeKey(permissionName, typeCode, qualifier);
        getSessionCachedContextProvider().addItemToMapCache("attributePermissionCachedResults", uniqueAttributeKey, Boolean.valueOf(hasPermission));
    }


    protected void addTypePermissionToCache(String permissionName, String typeCode, boolean hasPermission)
    {
        String uniqueTypeKey = getUniqueTypeKey(permissionName, typeCode);
        getSessionCachedContextProvider().addItemToMapCache("typePermissionCachedResults", uniqueTypeKey, Boolean.valueOf(hasPermission));
    }


    protected String getUniqueAttributeKey(String permissionName, String typeCode, String qualifier)
    {
        return permissionName + "_" + permissionName + "_" + typeCode;
    }


    protected String getUniqueTypeKey(String permissionName, String typeCode)
    {
        return permissionName + "_" + permissionName;
    }


    protected SessionCachedContextProvider getSessionCachedContextProvider()
    {
        return this.sessionCachedContextProvider;
    }


    @Required
    public void setSessionCachedContextProvider(SessionCachedContextProvider sessionCachedContextProvider)
    {
        this.sessionCachedContextProvider = sessionCachedContextProvider;
    }


    protected PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    protected PermissionEnablerService getPermissionEnablerService()
    {
        return this.permissionEnablerService;
    }


    @Required
    public void setPermissionEnablerService(PermissionEnablerService permissionEnablerService)
    {
        this.permissionEnablerService = permissionEnablerService;
    }
}
