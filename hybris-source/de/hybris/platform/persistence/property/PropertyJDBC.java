package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.RuntimeSQLException;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.Utilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public final class PropertyJDBC
{
    private static final Logger LOG = Logger.getLogger(PropertyJDBC.class.getName());
    private static final JDBCValueMappings jdbcValueMappings = JDBCValueMappings.getInstance();
    public static final String REAL_PK = "PK";
    public static final String REAL_TYPEPK = "TypePkString";
    public static final String ITEMPK = "ITEMPK";
    public static final String LANGPK = "LANGPK";
    public static final String ITEMTYPEPK = "ITEMTYPEPK";
    private static final int MAX_CHAR_SIZE = 2;


    public static String cutStringToByteLength(String str, int max)
    {
        int l = str.length();
        if(l > max / 2)
        {
            int utf8_count = 0;
            for(int i = 0; i < l && i < max; i++)
            {
                char c = str.charAt(i);
                if(c > '')
                {
                    utf8_count++;
                }
            }
            int tooMuch = utf8_count * 1;
            if(l + tooMuch > max)
            {
                return str.substring(0, Math.max(max / 2 - 1, max - tooMuch - 1));
            }
        }
        return str;
    }


    private static String getLocPropsTableName(PersistenceManager pm, Collection<LocalizableItemRemote> items)
    {
        for(LocalizableItemRemote i : items)
        {
            TypeInfoMap info = pm.getPersistenceInfo(i.getTypeKey());
            if(info != null && info.hasInfos(true))
            {
                return info.getTableName(true);
            }
        }
        return null;
    }


    public static void preloadLocalizedProperties(Set<PK> langPKs, Collection<LocalizableItemRemote> _items)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            PersistenceManager pm = Registry.getPersistenceManager();
            String tblName = getLocPropsTableName(pm, _items);
            if(tblName == null)
            {
                return;
            }
            HybrisDataSource ds = Registry.getCurrentTenant().getDataSource();
            conn = ds.getConnection();
            int pageSize = ds.getMaxPreparedParameterCount();
            if(pageSize == -1)
            {
                pageSize = _items.size();
            }
            else
            {
                pageSize -= langPKs.size();
            }
            int offset = 0;
            List<LocalizableItemRemote> itemsList = new ArrayList<>(_items);
            while(offset < itemsList.size())
            {
                int currentPageEnd = Math.min(itemsList.size(), offset + pageSize);
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT * FROM ").append(tblName);
                sb.append(" WHERE ").append("ITEMPK").append(" IN (");
                int i;
                for(i = 0; i < currentPageEnd - offset; i++)
                {
                    if(i > 0)
                    {
                        sb.append(",");
                    }
                    sb.append("?");
                }
                sb.append(")");
                sb.append(" AND ").append("LANGPK");
                if(langPKs.size() == 1)
                {
                    sb.append("=?");
                }
                else
                {
                    sb.append(" IN (");
                    int s;
                    for(i = 0, s = langPKs.size(); i < s; i++)
                    {
                        if(i > 0)
                        {
                            sb.append(",");
                        }
                        sb.append("?");
                    }
                    sb.append(")");
                }
                stmt = conn.prepareStatement(sb.toString());
                JDBCValueMappings.ValueWriter wr = jdbcValueMappings.getValueWriter(PK.class);
                int pos = 1;
                Map<PK, Long> timestamps = new HashMap<>();
                Map<PK, LocalizableItemRemote> itemsMap = new HashMap<>();
                for(int j = offset; j < currentPageEnd; j++)
                {
                    LocalizableItemRemote item = itemsList.get(j);
                    PK pk = item.getPK();
                    itemsMap.put(pk, item);
                    timestamps.put(pk, Long.valueOf(item.getPropertyTimestamp()));
                    wr.setValue(stmt, pos++, pk);
                }
                for(PK langPK : langPKs)
                {
                    wr.setValue(stmt, pos++, langPK);
                }
                rs = stmt.executeQuery();
                JDBCValueMappings.ValueReader pkReader = jdbcValueMappings.getValueReader(PK.class);
                Map<PK, Map<PK, EJBPropertyRowCache>> rows = null;
                while(rs.next())
                {
                    if(rows == null)
                    {
                        rows = new HashMap<>();
                    }
                    PK langPK = (PK)pkReader.getValue(rs, "LANGPK");
                    PK itemPK = (PK)pkReader.getValue(rs, "ITEMPK");
                    PK typePK = (PK)pkReader.getValue(rs, "ITEMTYPEPK");
                    long ts = ((Long)timestamps.get(itemPK)).longValue();
                    TypeInfoMap info = pm.getPersistenceInfo(typePK);
                    Map<PK, EJBPropertyRowCache> lang2RowMap = rows.get(itemPK);
                    if(lang2RowMap == null)
                    {
                        rows.put(itemPK, lang2RowMap = new HashMap<>());
                    }
                    EJBPropertyRowCache prc;
                    lang2RowMap.put(langPK, prc = readPropertyRow(rs, itemPK, typePK, langPK, ts, info));
                    prc.setItemPK(itemPK);
                }
                for(Map.Entry<PK, LocalizableItemRemote> entry : itemsMap.entrySet())
                {
                    PK itemPK = entry.getKey();
                    LocalizableItemRemote item = entry.getValue();
                    for(PK langPK : langPKs)
                    {
                        Map<PK, EJBPropertyRowCache> lang2RowMap = (rows != null) ? rows.get(itemPK) : null;
                        EJBPropertyRowCache prc = (lang2RowMap != null) ? lang2RowMap.get(langPK) : null;
                        if(prc == null)
                        {
                            prc = EJBPropertyRowCache.createLocalized(langPK, ((Long)timestamps.get(itemPK)).longValue(), item
                                            .getTypeInfoMap()
                                            .getSortedNames(true));
                            prc.setItemPK(itemPK);
                        }
                        ((LocalizableItemRemote)entry.getValue()).hintPropertyCache(new ItemLocalizedPropertyCacheKey(prc));
                    }
                }
                Utilities.tryToCloseJDBC(null, stmt, rs);
                stmt = null;
                rs = null;
                offset += pageSize;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeSQLException(e, "error loading properties ");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, rs);
        }
    }


    public static EJBPropertyRowCache createProperties(TypeInfoMap infoMap, PK itemPK, PK typePK, PK langPK, long timestamp)
    {
        boolean localized = (langPK != null && !PK.NULL_PK.equals(langPK));
        if(infoMap != null && infoMap.hasInfos(localized))
        {
            EJBPropertyRowCache cache = localized ? EJBPropertyRowCache.createLocalized(langPK, timestamp, infoMap.getSortedNames(true)) : EJBPropertyRowCache.create(timestamp, infoMap.getSortedNames(false));
            cache.setItemPK(itemPK);
            return cache;
        }
        return null;
    }


    public static EJBPropertyRowCache getProperties(TypeInfoMap infoMap, PK itemPK, PK typePK, PK langPK, long timestamp)
    {
        boolean localized = (langPK != null && !PK.NULL_PK.equals(langPK));
        boolean useItemTable = !localized;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = Registry.getCurrentTenant().getDataSource().getConnection();
            if(infoMap != null && infoMap.hasInfos(localized))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT * FROM ").append(useItemTable ? infoMap.getItemTableName() : infoMap.getTableName(localized));
                sb.append(" WHERE ").append(useItemTable ? "PK" : "ITEMPK").append("=? ");
                if(langPK != null)
                {
                    sb.append(" AND ").append("LANGPK").append("=?");
                }
                stmt = conn.prepareStatement(sb.toString());
                JDBCValueMappings.ValueWriter wr = jdbcValueMappings.getValueWriter(PK.class);
                wr.setValue(stmt, 1, itemPK);
                if(langPK != null)
                {
                    wr.setValue(stmt, 2, langPK);
                }
                rs = stmt.executeQuery();
                EJBPropertyRowCache cache = rs.next() ? readPropertyRow(rs, itemPK, typePK, localized ? langPK : null, timestamp, infoMap) : null;
                if(cache == null)
                {
                    cache = localized ? EJBPropertyRowCache.createLocalized(langPK, timestamp, infoMap.getSortedNames(true)) : EJBPropertyRowCache.create(timestamp, infoMap.getSortedNames(false));
                    if(!localized)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("created new UP cache " + cache + " for item " + itemPK);
                        }
                    }
                }
                cache.setItemPK(itemPK);
                return cache;
            }
            return null;
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "error reading properties ");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, rs);
        }
    }


    public static void updatePropertyTypePKs(TypeInfoMap infoMap, PK itemPK, PK newTypePK)
    {
        PreparedStatement pstmt = null;
        Connection conn = null;
        String q = null;
        try
        {
            conn = Registry.getCurrentTenant().getDataSource().getConnection();
            if(infoMap != null && !infoMap.isEmpty())
            {
                if(infoMap.hasInfos(true))
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("UPDATE ").append(infoMap.getTableName(true));
                    sb.append(" SET ").append("ITEMTYPEPK").append("=?");
                    sb.append(" WHERE ").append("ITEMPK").append("=?");
                    pstmt = conn.prepareStatement(q = sb.toString());
                    JDBCValueMappings.ValueWriter wr = jdbcValueMappings.PK_WRITER;
                    wr.setValue(pstmt, 1, newTypePK);
                    wr.setValue(pstmt, 2, itemPK);
                    pstmt.executeUpdate();
                    pstmt.close();
                    pstmt = null;
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "error updating type pk for item " + itemPK + " to typePK " + newTypePK + " ( query = " + q + ") : ");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, null);
        }
    }


    public static void writeProperties(EJBPropertyRowCache prc, PK itemPK, PK typePK, TypeInfoMap infoMap, boolean localized)
    {
        List[] arrayOfList;
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        StringBuilder query = new StringBuilder();
        List<Object>[] data = null;
        try
        {
            conn = Registry.getCurrentTenant().getDataSource().getConnection();
            if(prc.getItemPK() != null && !prc.getItemPK().equals(itemPK))
            {
                throw new EJBInternalException(null, "itemPK mismatch for property cache " + prc + " and itemPK " + itemPK, 0);
            }
            boolean useItemTable = !localized;
            String tableName = useItemTable ? infoMap.getItemTableName() : infoMap.getTableName(localized);
            boolean update = prc.isInDatabase();
            if(useItemTable && !update)
            {
                throw new EJBInternalException(null, "impossible to insert unloc property row with NO_UP_TABLES activated (should always update item row)", 0);
            }
            if((!localized && !PK.NULL_PK.equals(prc.getLangPK())) || (localized && (prc
                            .getLangPK() == null || PK.NULL_PK.equals(prc.getLangPK()))))
            {
                throw new EJBInternalException(null, "<########## NewPropError ##########> wrong language '" + prc.getLangPK() + "' for EJBPropertyRowCache '" + prc + "' (ignored, wrote nothing)", 0);
            }
            arrayOfList = (List[])getChangeData(conn, infoMap, prc, localized);
            int colCount = arrayOfList[0].size();
            JDBCValueMappings vm = JDBCValueMappings.getInstance();
            JDBCValueMappings.ValueWriter<PK, ?> pkWriter = vm.PK_WRITER;
            if(update)
            {
                query.append("UPDATE ").append(tableName).append(" SET ");
                for(int j = 0; j < colCount; j++)
                {
                    query.append((j > 0) ? "," : "").append(arrayOfList[0].get(j)).append(" = ? ");
                }
                if(!useItemTable)
                {
                    query.append(",").append("ITEMTYPEPK").append(" = ?");
                    arrayOfList[1].add(typePK);
                    arrayOfList[2].add(pkWriter);
                }
                query.append(" WHERE ").append(useItemTable ? "PK" : "ITEMPK").append(" = ? ");
                arrayOfList[1].add(itemPK);
                arrayOfList[2].add(pkWriter);
                if(localized)
                {
                    query.append(" AND ").append("LANGPK").append(" = ? ");
                    arrayOfList[1].add(prc.getLangPK());
                    arrayOfList[2].add(pkWriter);
                }
            }
            else
            {
                query.append("INSERT INTO ").append(tableName).append(" ( ");
                int j;
                for(j = 0; j < colCount; j++)
                {
                    query.append((j > 0) ? "," : "").append(arrayOfList[0].get(j));
                }
                query.append(",").append("ITEMPK").append(",").append("ITEMTYPEPK");
                arrayOfList[1].add(itemPK);
                arrayOfList[2].add(pkWriter);
                arrayOfList[1].add(typePK);
                arrayOfList[2].add(pkWriter);
                if(localized)
                {
                    query.append(",").append("LANGPK");
                    arrayOfList[1].add(prc.getLangPK());
                    arrayOfList[2].add(pkWriter);
                }
                query.append(" ) VALUES ( ");
                for(j = 0; j < colCount; j++)
                {
                    query.append((j > 0) ? ",?" : "?");
                }
                query.append(localized ? ",?,?,?" : ",?,?");
                query.append(" ) ");
            }
            stmt = conn.prepareStatement(query.toString());
            for(int i = 0; i < arrayOfList[1].size(); i++)
            {
                ((JDBCValueMappings.ValueWriter)arrayOfList[2].get(i)).setValue(stmt, i + 1, arrayOfList[1].get(i));
            }
            int rows = stmt.executeUpdate();
            success = (rows == 1);
            if(!success)
            {
                throw new EJBInternalException(null, "error " + (
                                update ? "saving" : "inserting") + " " + prc + " for item " + itemPK + " , wrote " + rows + " rows, query = " + query
                                .toString() + " values = " + arrayOfList[1], 0);
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "sql error " + (
                            prc.isInDatabase() ? "saving" : "inserting") + " " + prc + " for item " + itemPK + " , query = " + query
                            .toString() + " values = " + ((arrayOfList != null) ?
                            String.valueOf(arrayOfList[1]) : "null") + e);
        }
        finally
        {
            prc.wroteChanges(success);
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    private static final List[] EMPTY_CHANGES = new List[] {Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST};


    public static List<Object>[] getChangeData(Connection con, TypeInfoMap infoMap, EJBPropertyRowCache prc, boolean localized) throws SQLException
    {
        List[] ret = null;
        BitSet changeSet = prc.getChangeSet();
        if(!changeSet.isEmpty())
        {
            int s = prc.getColumnCount();
            ret = new List[] {new ArrayList(s), new ArrayList(s), new ArrayList(s)};
            int i;
            for(i = changeSet.nextSetBit(0); i > -1; i = changeSet.nextSetBit(i + 1))
            {
                TypeInfoMap.PropertyColumnInfo info = infoMap.getInfoForProperty(prc.getName(i), localized);
                Object value = adjustValue(prc.getValue(i), infoMap, info, localized, prc, i);
                ret[0].add(info.getColumnName());
                ret[1].add(value);
                ret[2].add(info.getValueWriter());
            }
            return (List<Object>[])ret;
        }
        return (List<Object>[])EMPTY_CHANGES;
    }


    private static Object adjustValue(Object value, TypeInfoMap infoMap, TypeInfoMap.PropertyColumnInfo info, boolean localized, EJBPropertyRowCache prc, int i)
    {
        Object ret = value;
        Class<?> javaClass = info.getPropertyClass();
        if(value != null && !javaClass.isInstance(value) && !javaClass.equals(ItemPropertyValueCollection.class))
        {
            if(value instanceof PK && ItemPropertyValue.class.isAssignableFrom(javaClass))
            {
                ret = new ItemPropertyValue((PK)value);
            }
            else if(value instanceof ItemPropertyValue && PK.class.isAssignableFrom(javaClass))
            {
                ret = ((ItemPropertyValue)value).getPK();
            }
            else
            {
                throw new EJBInternalException(null, "<########## NewPropError ##########> value type mismatch in " + (
                                localized ? "localized" : "unlocalized") + " property '" + prc.getName(i) + "' of '" + infoMap
                                .getCode() + "' : expected '" + javaClass + "' but got '" + value
                                .getClass() + "' : '" + value + "' (info was " + info + ")", 0);
            }
        }
        return ret;
    }


    public static void removeAllPropertyData(PK itemPK, PK typePK)
    {
        PreparedStatement stmt = null;
        Connection conn = null;
        try
        {
            Tenant t = Registry.getCurrentTenant();
            conn = t.getDataSource().getConnection();
            TypeInfoMap infoMap = t.getPersistenceManager().getPersistenceInfo(typePK);
            if(infoMap.hasInfos(true))
            {
                StringBuilder query = new StringBuilder();
                query.append("DELETE FROM ").append(infoMap.getTableName(true)).append(" WHERE ").append("ITEMPK").append("=?");
                try
                {
                    stmt = conn.prepareStatement(query.toString());
                    (JDBCValueMappings.getInstance()).PK_WRITER.setValue(stmt, 1, itemPK);
                    stmt.executeUpdate();
                }
                finally
                {
                    if(stmt != null)
                    {
                        try
                        {
                            stmt.close();
                        }
                        catch(SQLException e)
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug(e.getMessage());
                            }
                        }
                    }
                    stmt = null;
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "!!");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    public static EJBPropertyRowCache readPropertyRow(ResultSet rs, PK itemPK, PK itemTypePK, PK langPK, long timestamp, TypeInfoMap infoMap) throws SQLException
    {
        String name = null;
        TypeInfoMap.PropertyColumnInfo info = null;
        PK realItemPK = null;
        PK realItemTypePK = null;
        try
        {
            boolean localized = (langPK != null && !PK.NULL_PK.equals(langPK));
            boolean useItemTable = !localized;
            List<String> sortedNames = infoMap.getSortedNames(localized);
            List<Object> sortedValues = new ArrayList(sortedNames.size());
            JDBCValueMappings.ValueReader<PK, ?> pkReader = (JDBCValueMappings.getInstance()).PK_READER;
            realItemPK = (PK)pkReader.getValue(rs, useItemTable ? "PK" : "ITEMPK");
            realItemTypePK = (PK)pkReader.getValue(rs, useItemTable ? "TypePkString" : "ITEMTYPEPK");
            for(String n : sortedNames)
            {
                name = n;
                info = infoMap.getInfoForProperty(name, localized);
                Object theValue = info.getValueReader().getValue(rs, info.getColumnName());
                sortedValues.add(theValue);
                SQLWarning warn = rs.getWarnings();
                if(warn != null)
                {
                    if(LOG.isEnabledFor((Priority)Level.ERROR))
                    {
                        LOG.error("warning while reading property field '" + name + "'/'" + info.getColumnName() + "' of item '" + itemPK + "'/'" + realItemPK + "' " + (
                                        (langPK != null) ? ("with langPK '" + langPK + "'") : "") + " (info was " + info + " ) \n warn : ");
                    }
                    warn.printStackTrace(System.err);
                    rs.clearWarnings();
                }
            }
            if(!itemPK.equals(realItemPK) || !itemTypePK.equals(realItemTypePK))
            {
                if(localized || !useItemTable || !Transaction.current().isRunning())
                {
                    if(localized && !useItemTable && !itemTypePK.equals(realItemTypePK))
                    {
                        LOG.info("found and fixed inconsistent property row (itemPK[" + itemPK + ":" + realItemPK + "] typePK[" + itemTypePK + ":" + realItemTypePK + "])");
                        updatePropertyTypePKs(infoMap, itemPK, itemTypePK);
                    }
                    else if(LOG.isEnabledFor((Priority)Level.WARN))
                    {
                        LOG.warn("found inconsistent property row (itemPK[" + itemPK + ":" + realItemPK + "] typePK[" + itemTypePK + ":" + realItemTypePK + "])");
                    }
                }
            }
            return localized ? EJBPropertyRowCache.loadLocalized(langPK, timestamp, sortedNames, sortedValues) :
                            EJBPropertyRowCache.load(timestamp, sortedNames, sortedValues);
        }
        catch(Exception e)
        {
            if(LOG.isEnabledFor((Priority)Level.ERROR))
            {
                LOG.error("error reading property field '" + name + "'/'" + ((info != null) ? info.getColumnName() : null) + "' of item '" + itemPK + "'/'" + realItemPK + "' " + (
                                (langPK != null) ? ("with langPK '" + langPK + "'") : "") + " (info was " + info + " ) \n stack: ");
            }
            if(e instanceof SQLException)
            {
                throw (SQLException)e;
            }
            throw new JaloSystemException(e);
        }
    }
}
