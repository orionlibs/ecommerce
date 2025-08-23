package de.hybris.platform.persistence.links.jdbc.dml.relation;

import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.persistence.links.jdbc.dml.RelationTypeHelper;
import de.hybris.platform.persistence.links.jdbc.dml.RelationsSearchParams;
import de.hybris.platform.persistence.property.TypeInfoMap;

class DatabaseHelper
{
    static String getTableName(RelationsSearchParams params)
    {
        return getTypeInfoMap(params).getItemTableName();
    }


    static String getPkColumnName(RelationsSearchParams params)
    {
        return getColumnName(params, Link.PK);
    }


    static String getParentPKColumnName(RelationsSearchParams params)
    {
        return params.isParentSource() ? getSourcePKColumnName(params) : getTargetPKColumnName(params);
    }


    static String getChildPKColumnName(RelationsSearchParams params)
    {
        return params.isParentSource() ? getTargetPKColumnName(params) : getSourcePKColumnName(params);
    }


    static String getOrderColumn(RelationsSearchParams params)
    {
        return params.isParentSource() ? getSequenceNumberColumnName(params) : getReverseSequenceColumnName(params);
    }


    static String getLanguagePKColumnName(RelationsSearchParams params)
    {
        return getColumnName(params, "language");
    }


    static String getSourcePKColumnName(RelationsSearchParams params)
    {
        return getColumnName(params, "source");
    }


    static String getTargetPKColumnName(RelationsSearchParams params)
    {
        return getColumnName(params, "target");
    }


    static String getSequenceNumberColumnName(RelationsSearchParams params)
    {
        return getColumnName(params, "sequenceNumber");
    }


    static String getReverseSequenceColumnName(RelationsSearchParams params)
    {
        return getColumnName(params, "reverseSequenceNumber");
    }


    static String getQualifierColumnName(RelationsSearchParams params)
    {
        return getColumnName(params, "qualifier");
    }


    static String getVersionColumnName(RelationsSearchParams params)
    {
        return "HJMPTS";
    }


    static String getColumnName(RelationsSearchParams params, String propertyName)
    {
        TypeInfoMap typeInfoMap = getTypeInfoMap(params);
        TypeInfoMap.PropertyColumnInfo coreProperty = typeInfoMap.getInfoForCoreProperty(propertyName);
        if(coreProperty != null)
        {
            return coreProperty.getColumnName();
        }
        return typeInfoMap.getInfoForProperty(propertyName, false).getColumnName();
    }


    static TypeInfoMap getTypeInfoMap(RelationsSearchParams params)
    {
        return RelationTypeHelper.getTypeInfoMap(params.getRelationCode());
    }
}
