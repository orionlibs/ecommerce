package de.hybris.platform.directpersistence;

import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.exception.ModelPersistenceException;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.persistence.property.TypeInfoMap;

public class DirectPersistenceUtils
{
    public static TypeInfoMap getInfoMapForType(String typeCode)
    {
        TypeInfoMap infoMap = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(typeCode);
        if(infoMap == null)
        {
            throw new ModelPersistenceException("Cannot retrieve type info map for type: " + typeCode);
        }
        return infoMap;
    }


    public static ColumnPayload createColumnPayloadForProperty(String name, Object value, TypeInfoMap infoMap)
    {
        ColumnPayload.Builder result;
        TypeInfoMap.PropertyColumnInfo info;
        int propertyType = infoMap.getPropertyType(name);
        switch(propertyType)
        {
            case 2:
                info = infoMap.getInfoForCoreProperty(name);
                result = ColumnPayload.builder().propertyColumnInfo(info).targetTableType(ColumnPayload.TargetTableType.ITEM).value(value);
                break;
            case 0:
                info = infoMap.getInfoForProperty(name, false);
                result = ColumnPayload.builder().propertyColumnInfo(info).targetTableType(ColumnPayload.TargetTableType.ITEM).value(value);
                break;
            case 1:
                info = infoMap.getInfoForProperty(name, true);
                result = ColumnPayload.builder().propertyColumnInfo(info).targetTableType(ColumnPayload.TargetTableType.LP).value(value);
                break;
            case 3:
                result = ColumnPayload.builder().declaredTypeClass(String.class).columnName(name).targetTableType(ColumnPayload.TargetTableType.PROPS).value(value);
                break;
            default:
                result = null;
                break;
        }
        return (result == null) ? null : result.build();
    }
}
