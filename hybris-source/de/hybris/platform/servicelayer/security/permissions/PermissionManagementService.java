package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collection;

public interface PermissionManagementService
{
    void createPermission(String paramString);


    Collection<String> getDefinedPermissions();


    Collection<PermissionAssignment> getItemPermissions(ItemModel paramItemModel);


    Collection<PermissionAssignment> getItemPermissionsForPrincipal(ItemModel paramItemModel, PrincipalModel... paramVarArgs);


    Collection<PermissionAssignment> getItemPermissionsForName(ItemModel paramItemModel, String... paramVarArgs);


    void addItemPermission(ItemModel paramItemModel, PermissionAssignment... paramVarArgs);


    void addItemPermissions(ItemModel paramItemModel, Collection<PermissionAssignment> paramCollection);


    void setItemPermissions(ItemModel paramItemModel, Collection<PermissionAssignment> paramCollection);


    void removeItemPermission(ItemModel paramItemModel, PermissionAssignment... paramVarArgs);


    void removeItemPermissions(ItemModel paramItemModel, Collection<PermissionAssignment> paramCollection);


    void removeItemPermissionsForPrincipal(ItemModel paramItemModel, PrincipalModel... paramVarArgs);


    void removeItemPermissionsForName(ItemModel paramItemModel, String... paramVarArgs);


    void clearItemPermissions(ItemModel paramItemModel);


    Collection<PermissionAssignment> getTypePermissions(ComposedTypeModel paramComposedTypeModel);


    Collection<PermissionAssignment> getTypePermissionsForPrincipal(ComposedTypeModel paramComposedTypeModel, PrincipalModel... paramVarArgs);


    Collection<PermissionAssignment> getTypePermissionsForName(ComposedTypeModel paramComposedTypeModel, String... paramVarArgs);


    void addTypePermission(ComposedTypeModel paramComposedTypeModel, PermissionAssignment... paramVarArgs);


    void addTypePermissions(ComposedTypeModel paramComposedTypeModel, Collection<PermissionAssignment> paramCollection);


    void setTypePermissions(ComposedTypeModel paramComposedTypeModel, Collection<PermissionAssignment> paramCollection);


    void removeTypePermission(ComposedTypeModel paramComposedTypeModel, PermissionAssignment... paramVarArgs);


    void removeTypePermissions(ComposedTypeModel paramComposedTypeModel, Collection<PermissionAssignment> paramCollection);


    void removeTypePermissionsForPrincipal(ComposedTypeModel paramComposedTypeModel, PrincipalModel... paramVarArgs);


    void removeTypePermissionsForName(ComposedTypeModel paramComposedTypeModel, String... paramVarArgs);


    void clearTypePermissions(ComposedTypeModel paramComposedTypeModel);


    Collection<PermissionAssignment> getAttributePermissions(AttributeDescriptorModel paramAttributeDescriptorModel);


    Collection<PermissionAssignment> getAttributePermissionsForPrincipal(AttributeDescriptorModel paramAttributeDescriptorModel, PrincipalModel... paramVarArgs);


    Collection<PermissionAssignment> getAttributePermissionsForName(AttributeDescriptorModel paramAttributeDescriptorModel, String... paramVarArgs);


    void addAttributePermission(AttributeDescriptorModel paramAttributeDescriptorModel, PermissionAssignment... paramVarArgs);


    void addAttributePermissions(AttributeDescriptorModel paramAttributeDescriptorModel, Collection<PermissionAssignment> paramCollection);


    void setAttributePermissions(AttributeDescriptorModel paramAttributeDescriptorModel, Collection<PermissionAssignment> paramCollection);


    void removeAttributePermission(AttributeDescriptorModel paramAttributeDescriptorModel, PermissionAssignment... paramVarArgs);


    void removeAttributePermissions(AttributeDescriptorModel paramAttributeDescriptorModel, Collection<PermissionAssignment> paramCollection);


    void removeAttributePermissionsForPrincipal(AttributeDescriptorModel paramAttributeDescriptorModel, PrincipalModel... paramVarArgs);


    void removeAttributePermissionsForName(AttributeDescriptorModel paramAttributeDescriptorModel, String... paramVarArgs);


    void clearAttributePermissions(AttributeDescriptorModel paramAttributeDescriptorModel);


    Collection<PermissionAssignment> getGlobalPermissionsForPrincipal(PrincipalModel... paramVarArgs);


    @Deprecated(since = "6.0.0", forRemoval = true)
    Collection<PermissionAssignment> getGlobalPermissionsForName(String... paramVarArgs);


    void addGlobalPermission(PermissionAssignment... paramVarArgs);


    void addGlobalPermissions(Collection<PermissionAssignment> paramCollection);


    void removeGlobalPermission(PermissionAssignment... paramVarArgs);


    void removeGlobalPermissions(Collection<PermissionAssignment> paramCollection);


    void removeGlobalPermissionsForPrincipal(PrincipalModel... paramVarArgs);


    void removeGlobalPermissionsForName(String... paramVarArgs);
}
