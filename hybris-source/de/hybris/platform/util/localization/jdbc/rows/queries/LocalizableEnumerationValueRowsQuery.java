package de.hybris.platform.util.localization.jdbc.rows.queries;

import de.hybris.platform.util.localization.jdbc.DbInfo;
import de.hybris.platform.util.localization.jdbc.LocalizableRowsQuery;
import de.hybris.platform.util.localization.jdbc.StatementWithParams;
import de.hybris.platform.util.localization.jdbc.rows.LocalizableEnumerationValueRow;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalizableEnumerationValueRowsQuery extends LocalizableRowsQuery<LocalizableEnumerationValueRow>
{
    public LocalizableEnumerationValueRowsQuery(DbInfo dbInfo)
    {
        super(dbInfo);
    }


    public LocalizableEnumerationValueRow mapResultSetToRow(ResultSet rs) throws SQLException
    {
        String typeCode = rs.getString("typeCode");
        String valueCode = rs.getString("valueCode");
        long itemPKValue = rs.getLong("valuePK");
        long itemTypePKValue = rs.getLong("typePK");
        Long languagePKValue = (rs.getObject("langPK") == null) ? null : Long.valueOf(rs.getLong("langPK"));
        return new LocalizableEnumerationValueRow(typeCode, valueCode, this.lpTableName, itemPKValue, itemTypePKValue, languagePKValue);
    }


    protected StatementWithParams createQuery(DbInfo dbInfo)
    {
        String enumerationValuesTable = dbInfo.getTableNameFor("EnumerationValue");
        String enumerationValuesLpTable = getLpTableName(dbInfo);
        String composedTypesTableName = dbInfo.getTableNameFor("ComposedType");
        String sql = "select t.InternalCodeLowerCase as typeCode, v.codeLowerCase as valueCode, v.PK as valuePK, v.TypePkString as typePK, l.LANGPK as langPK from " + enumerationValuesTable + " v join " + composedTypesTableName + " t on (v.TypePkString=t.pk) left join " + enumerationValuesLpTable
                        + " l on (v.PK=l.ITEMPK) order by v.PK";
        return new StatementWithParams(sql, new Object[0]);
    }


    protected String getLpTableName(DbInfo dbInfo)
    {
        return dbInfo.getTableNameFor("EnumerationValue") + "lp";
    }
}
