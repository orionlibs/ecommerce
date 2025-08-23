package de.hybris.platform.permissionsfacades;

import de.hybris.platform.permissionsfacades.data.CatalogPermissionsData;
import de.hybris.platform.permissionsfacades.data.PermissionsData;
import de.hybris.platform.permissionsfacades.data.TypePermissionsDataList;
import java.util.Collections;
import java.util.List;

public interface PermissionsFacade
{
    public static final String READ_ACCESS_TYPE = "read";
    public static final String WRITE_ACCESS_TYPE = "write";


    List<PermissionsData> calculateTypesPermissions(String paramString, List<String> paramList1, List<String> paramList2);


    PermissionsData calculateGlobalPermissions(String paramString, List<String> paramList);


    List<PermissionsData> calculateAttributesPermissions(String paramString, List<String> paramList1, List<String> paramList2);


    List<CatalogPermissionsData> calculateCatalogPermissions(String paramString, List<String> paramList1, List<String> paramList2);


    default TypePermissionsDataList applyPermissions(TypePermissionsDataList permissionsList)
    {
        TypePermissionsDataList list = new TypePermissionsDataList();
        list.setPermissionsList(Collections.emptyList());
        return list;
    }
}
