package de.hybris.platform.util.localization.jdbc.rows.queries;

import de.hybris.platform.util.localization.jdbc.DbInfo;
import de.hybris.platform.util.localization.jdbc.LocalizableRowsQuery;
import de.hybris.platform.util.localization.jdbc.StatementWithParams;
import de.hybris.platform.util.localization.jdbc.rows.LocalizableTypeRow;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LocalizableTypeRowsQuery extends LocalizableRowsQuery<LocalizableTypeRow>
{
    public LocalizableTypeRowsQuery(DbInfo dbInfo)
    {
        super(dbInfo);
    }


    public LocalizableTypeRow mapResultSetToRow(ResultSet rs) throws SQLException
    {
        long typePK = rs.getLong("typePK");
        long metaTypePK = rs.getLong("metaTypePK");
        Long langPK = (rs.getObject("langPK") == null) ? null : Long.valueOf(rs.getLong("langPK"));
        String typeCode = rs.getString("typeCode");
        return new LocalizableTypeRow(typeCode, this.lpTableName, typePK, metaTypePK, langPK);
    }


    protected StatementWithParams createQuery(DbInfo dbInfo)
    {
        String tableName = dbInfo.getTableNameFor(getMetaTypeCode());
        String lpTableName = getLpTableName(dbInfo);
        String sql = "select type.PK as typePK, type.InternalCodeLowerCase as typeCode, type.TypePkString as metaTypePK, loc.LANGPK as langPK from " + tableName + " type left join " + lpTableName + " loc on (type.PK = loc.ITEMPK) order by type.PK";
        return new StatementWithParams(sql, new Object[0]);
    }


    protected String getLpTableName(DbInfo dbInfo)
    {
        return dbInfo.getTableNameFor(getMetaTypeCode()) + "lp";
    }


    protected abstract String getMetaTypeCode();
}
