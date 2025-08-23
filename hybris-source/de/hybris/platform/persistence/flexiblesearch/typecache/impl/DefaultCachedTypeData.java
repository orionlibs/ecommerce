package de.hybris.platform.persistence.flexiblesearch.typecache.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.flexiblesearch.typecache.CachedTypeData;
import de.hybris.platform.persistence.property.TypeInfoMap;

public class DefaultCachedTypeData implements CachedTypeData
{
    private final TypeInfoMap typeInfoMap;


    public DefaultCachedTypeData(TypeInfoMap typeInfoMap)
    {
        this.typeInfoMap = typeInfoMap;
    }


    public PK getTypePk()
    {
        return this.typeInfoMap.getTypePK();
    }


    public boolean isAbstract()
    {
        return this.typeInfoMap.isAbstract();
    }


    public String getLocalizedTableName()
    {
        return this.typeInfoMap.getTableName(true);
    }


    public String getUnlocalizedTableName()
    {
        return this.typeInfoMap.getTableName(false);
    }


    public String getStandardTableName()
    {
        return this.typeInfoMap.getItemTableName();
    }


    public String getPropertyTableName()
    {
        return this.typeInfoMap.getOldPropTableName();
    }


    public int getPropertyTypeForName(String propertyName)
    {
        return this.typeInfoMap.getPropertyType(propertyName);
    }


    public String getLocalizedPropertyColumnName(String fieldName)
    {
        TypeInfoMap.PropertyColumnInfo infoForProperty = this.typeInfoMap.getInfoForProperty(fieldName, true);
        if(infoForProperty == null)
        {
            throw new IllegalStateException("No localized PropertyColumnInfo for field: " + fieldName);
        }
        return infoForProperty.getColumnName();
    }


    public String getUnlocalizedPropertyColumnName(String fieldName)
    {
        TypeInfoMap.PropertyColumnInfo infoForProperty = this.typeInfoMap.getInfoForProperty(fieldName, false);
        if(infoForProperty == null)
        {
            throw new IllegalStateException("No unlocalized PropertyColumnInfo for field: " + fieldName);
        }
        return infoForProperty.getColumnName();
    }


    public String getCorePropertyColumnName(String fieldName)
    {
        TypeInfoMap.PropertyColumnInfo infoForProperty = this.typeInfoMap.getInfoForCoreProperty(fieldName);
        return (infoForProperty == null) ? fieldName : infoForProperty.getColumnName();
    }


    public String toString()
    {
        return this.typeInfoMap.toString();
    }
}
