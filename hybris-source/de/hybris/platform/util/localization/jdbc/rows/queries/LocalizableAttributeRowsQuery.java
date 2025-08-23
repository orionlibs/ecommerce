package de.hybris.platform.util.localization.jdbc.rows.queries;

import de.hybris.platform.util.localization.jdbc.DbInfo;
import de.hybris.platform.util.localization.jdbc.LocalizableRowsQuery;
import de.hybris.platform.util.localization.jdbc.StatementWithParams;
import de.hybris.platform.util.localization.jdbc.rows.LocalizableAttributeRow;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalizableAttributeRowsQuery extends LocalizableRowsQuery<LocalizableAttributeRow>
{
    public LocalizableAttributeRowsQuery(DbInfo dbInfo)
    {
        super(dbInfo);
    }


    public LocalizableAttributeRow mapResultSetToRow(ResultSet rs) throws SQLException
    {
        String qualifier = rs.getString("qualifier");
        String ownerCode = rs.getString("ownerCode");
        String declarerCode = rs.getString("declarerCode");
        long attributePKValue = rs.getLong("attributePK");
        long attributeTypePKValue = rs.getLong("attributeTypePK");
        Long languagePKValue = (rs.getObject("languagePK") == null) ? null : Long.valueOf(rs.getLong("languagePK"));
        String inheritancePath = rs.getString("inheritancePath");
        return new LocalizableAttributeRow(this.lpTableName, attributePKValue, attributeTypePKValue, languagePKValue, qualifier, ownerCode, declarerCode, inheritancePath);
    }


    protected StatementWithParams createQuery(DbInfo dbInfo)
    {
        String attributesTable = dbInfo.getTableNameFor("AttributeDescriptor");
        String attributesLpTable = getLpTableName(dbInfo);
        String composedTypesTable = dbInfo.getTableNameFor("ComposedType");
        return new StatementWithParams(
                        "select ownerType.InternalCodeLowerCase as ownerCode, attr.QualifierLowerCaseInternal as qualifier, superAttrOwnerType.InternalCodeLowerCase as declarerCode, attr.PK as attributePK, attr.TypePkString as attributeTypePK, attrLocalization.LANGPK as languagePK, ownerType.InheritancePathString as inheritancePath from "
                                        + attributesTable + " attr join " + composedTypesTable + " ownerType on (attr.OwnerPkString=ownerType.PK) left join " + attributesTable + " superAttr on (attr.SuperAttributeDescriptorPK=superAttr.PK) left join " + composedTypesTable
                                        + " superAttrOwnerType on (superAttr.OwnerPkString=superAttrOwnerType.PK) left join " + attributesLpTable + " attrLocalization on (attr.PK=attrLocalization.ITEMPK) order by attr.PK", new Object[0]);
    }


    protected String getLpTableName(DbInfo dbInfo)
    {
        return dbInfo.getTableNameFor("AttributeDescriptor") + "lp";
    }
}
