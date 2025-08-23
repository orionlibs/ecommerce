package de.hybris.platform.persistence.property;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.RuntimeSQLException;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.Utilities;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public final class OldPropertyJDBC
{
    private static final Logger log = Logger.getLogger(OldPropertyJDBC.class.getName());
    public static final String LOG_TOPIC = "core.properties";
    private static final JDBCValueMappings jdbcValueMappings = JDBCValueMappings.getInstance();
    public static final String ITEMPK = "ITEMPK";
    public static final String ITEMTYPEPK = "ITEMTYPEPK";
    public static final String NAME = "NAME";
    public static final String REALNAME = "REALNAME";
    public static final String LANGPK = "LANGPK";
    public static final String TYPE1 = "TYPE1";
    public static final String VALUESTRING1 = "VALUESTRING1";
    public static final String VALUE1 = "VALUE1";
    public static final int NULL = 0;
    public static final int STRING = 1;
    public static final int BOOLEAN = 2;
    public static final int DOUBLE = 3;
    public static final int LONG = 4;
    public static final int DATE = 5;
    public static final int INTEGER = 6;
    public static final int SERIALIZABLE = 7;
    public static final int STRING_TRUNCATED = 10;
    public static final int ITEM = 11;
    public static final int CLASS = 12;


    public static EJBPropertyCache createProperties(PK itemPK, long timestamp, String oldPropTableName)
    {
        return EJBPropertyCache.create(timestamp);
    }


    public static EJBPropertyCache getProperties(PK itemPK, long timestamp, String oldPropTableName)
    {
        EJBPropertyCache cache = null;
        Collection props = getPropertiesInternal(itemPK, oldPropTableName);
        if(props.isEmpty())
        {
            cache = EJBPropertyCache.create(timestamp);
        }
        else
        {
            cache = EJBPropertyCache.load(timestamp, props);
        }
        return cache;
    }


    public static void updatePropertyTypePKs(PK itemPK, PK typePK, String oldPropTableName)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String q = null;
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            conn = tenant.getDataSource().getConnection();
            pstmt = conn.prepareStatement("UPDATE " + oldPropTableName + " SET ITEMTYPEPK = ? WHERE ITEMPK = ?");
            JDBCValueMappings.ValueWriter wr = jdbcValueMappings.getValueWriter(PK.class);
            wr.setValue(pstmt, 1, typePK);
            wr.setValue(pstmt, 2, itemPK);
            pstmt.executeUpdate();
        }
        catch(SQLException e)
        {
            log.error("itemPK=" + itemPK);
            log.error("typePK=" + typePK);
            log.error("oldPropTableName=" + oldPropTableName);
            log.error("query=" + q);
            e.printStackTrace();
            throw new RuntimeSQLException(e, "error updating type pk of old style properties: ");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, null);
        }
    }


    public static void writeOldProperties(EJBPropertyCache oldPropCache, PK itemPK, PK typePK, String tableName)
    {
        try
        {
            if(!oldPropCache.needsUpdate())
            {
                return;
            }
            Collection props = oldPropCache.getUpdateableProperties();
            if(props.isEmpty())
            {
                return;
            }
            Collection<EJBProperty> toInsert = null;
            Collection<EJBProperty> toUpdate = null;
            Collection<EJBProperty> toRemove = null;
            for(Iterator<EJBProperty> it = props.iterator(); it.hasNext(); )
            {
                EJBProperty prop = it.next();
                if(!prop.hasChanged())
                {
                    continue;
                }
                if(!prop.isInDatabase())
                {
                    if(prop.getValue1Internal() == null)
                    {
                        log.warn("attempt to insert NULL propert " + prop + " for item " + itemPK + " (skipped)");
                        continue;
                    }
                    if(toInsert == null)
                    {
                        toInsert = new ArrayList();
                    }
                    toInsert.add(prop);
                    continue;
                }
                if(prop.getValue1Internal() == null)
                {
                    if(toRemove == null)
                    {
                        toRemove = new ArrayList();
                    }
                    toRemove.add(prop);
                    continue;
                }
                if(toUpdate == null)
                {
                    toUpdate = new ArrayList();
                }
                toUpdate.add(prop);
            }
            if(toInsert != null)
            {
                insertOldProperties(itemPK, typePK, toInsert, tableName);
            }
            if(toRemove != null)
            {
                removeOldProperties(itemPK, typePK, toRemove, tableName);
            }
            if(toUpdate != null)
            {
                updateOldProperties(itemPK, typePK, toUpdate, tableName);
            }
        }
        finally
        {
            oldPropCache.wroteChanges();
        }
    }


    public static void removeAllPropertyData(PK itemPK, String tableName)
    {
        PreparedStatement stmt = null;
        Connection conn = null;
        StringBuilder query = new StringBuilder();
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            conn = tenant.getDataSource().getConnection();
            query.append("DELETE FROM ").append(tableName).append(" WHERE ").append("ITEMPK").append("=?");
            stmt = conn.prepareStatement(query.toString());
            jdbcValueMappings.fillStatement(stmt, Arrays.asList(new PK[] {itemPK}));
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    public static void removeAllPropertyData(Collection<PK> itemTypePKs, String propertyName, String tableName)
    {
        PreparedStatement stmt = null;
        Connection conn = null;
        StringBuilder query = new StringBuilder();
        List<PK> typePKs = new ArrayList<>(itemTypePKs);
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            conn = tenant.getDataSource().getConnection();
            query.append("DELETE FROM ").append(tableName).append(" WHERE ").append("ITEMTYPEPK").append(" IN (");
            for(int i = 0; i < typePKs.size(); i++)
            {
                query.append((i > 0) ? "," : "").append("?");
            }
            query.append(") AND ").append("NAME").append(" = ?");
            stmt = conn.prepareStatement(query.toString());
            List<PK> lst = new ArrayList();
            lst.addAll(itemTypePKs);
            lst.add(propertyName.toLowerCase(LocaleHelper.getPersistenceLocale()));
            jdbcValueMappings.fillStatement(stmt, lst);
            int deleted = stmt.executeUpdate();
            if(log.isDebugEnabled())
            {
                log.debug("deleted dump properties for types " + itemTypePKs + " and property " + propertyName + " (removed " + deleted + " rows)");
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    public static void patchPropertyNamesAfterUpdate(String propsTableName)
    {
        Connection conn = null;
        Statement stmt = null;
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            conn = tenant.getDataSource().getConnection();
            stmt = conn.createStatement();
            int changed = stmt.executeUpdate("UPDATE " + propsTableName + " SET REALNAME = NAME WHERE REALNAME IS NULL");
            if(log.isDebugEnabled())
            {
                log.debug("initialized " + changed + " property value real names");
            }
            stmt.close();
            stmt = null;
            stmt = conn.createStatement();
            changed = stmt.executeUpdate("UPDATE " + propsTableName + " SET NAME = LOWER(NAME) WHERE NAME <> LOWER(NAME)");
            if(log.isDebugEnabled())
            {
                log.debug("changed " + changed + " property value names to lower case");
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "error patching props table '" + propsTableName + "'");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, null);
        }
    }


    public static void moveDumpData(int typeCode, String propertyName, String oldPropTableName, DumpPropertyConverter conv)
    {
        Statement stmt = null;
        PreparedStatement rstmt = null;
        Connection conn = null;
        Connection con2 = null;
        ResultSet rs = null;
        boolean mysql = Config.isMySQLUsed();
        JDBCValueMappings.ValueReader<PK, ?> pkReader = jdbcValueMappings.PK_READER;
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = jdbcValueMappings.PK_WRITER;
        JDBCValueMappings.ValueWriter<String, ?> stringWriter = jdbcValueMappings.STRING_WRITER;
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            HybrisDataSource ds = tenant.getDataSource();
            conn = ds.getConnection();
            String query = jdbcValueMappings.getDumpPropertyMovalQuery(oldPropTableName, propertyName, typeCode);
            if(mysql)
            {
                con2 = ds.getConnection();
                stmt = conn.prepareStatement(query, 1003, 1007);
                stmt.setFetchSize(-2147483648);
                rs = ((PreparedStatement)stmt).executeQuery();
            }
            else
            {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
            }
            while(rs.next())
            {
                PK itemPK = (PK)pkReader.getValue(rs, "ITEMPK");
                PK langPK = (PK)pkReader.getValue(rs, "LANGPK");
                boolean langPKAsZero = !rs.wasNull();
                PK typePK = (PK)pkReader.getValue(rs, "ITEMTYPEPK");
                if(itemPK.getTypeCode() == typeCode)
                {
                    EJBProperty dumpProp = readOldProperty(rs, itemPK);
                    if(log.isDebugEnabled())
                    {
                        log.debug("moving dump property " + dumpProp);
                    }
                    if(conv.convert(itemPK, typePK, dumpProp))
                    {
                        if(rstmt == null)
                        {
                            rstmt = (mysql ? con2 : conn).prepareStatement("DELETE FROM " + oldPropTableName + " WHERE ITEMPK=? AND NAME=? AND LANGPK=?");
                        }
                        pkWriter.setValue(rstmt, 1, itemPK);
                        stringWriter.setValue(rstmt, 2, propertyName.toLowerCase(LocaleHelper.getPersistenceLocale()));
                        pkWriter.setValue(rstmt, 3, langPKAsZero ? PK.NULL_PK : langPK);
                        int count = rstmt.executeUpdate();
                        if(count != 1 && ds.getTenant()
                                        .getConfig()
                                        .getBoolean("hjmp.throw.concurrent.modification.exceptions", true))
                        {
                            log.error("unexpected modification count removing dump property " + dumpProp + " : " + count);
                        }
                    }
                }
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "error reading and moving dump properties");
        }
        finally
        {
            if(rstmt != null)
            {
                try
                {
                    rstmt.close();
                }
                catch(SQLException ex)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug(ex.getMessage());
                    }
                }
            }
            Utilities.tryToCloseJDBC(conn, stmt, rs);
            if(mysql)
            {
                Utilities.tryToCloseJDBC(con2, null, null);
            }
        }
    }


    public static int checkDumpData(int typeCode, String propertyName, String oldPropTableName)
    {
        Statement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try
        {
            conn = Registry.getCurrentTenant().getDataSource().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(jdbcValueMappings
                            .getDumpPropertyCountQuery(oldPropTableName, propertyName, typeCode));
            return rs.next() ? rs.getInt(1) : 0;
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "error checking dump properties");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, rs);
        }
    }


    private static Collection getPropertiesInternal(PK itemPK, String oldPropTableName)
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        Collection<EJBProperty> result = null;
        try
        {
            HybrisDataSource ds = Registry.getCurrentTenant().getDataSource();
            conn = ds.getConnection();
            result = new ArrayList();
            pstmt = conn.prepareStatement(getOldAllPropertiesSelect(oldPropTableName));
            jdbcValueMappings.fillStatement(pstmt, Arrays.asList(new PK[] {itemPK}));
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                result.add(readOldProperty(rs, itemPK));
            }
            return result;
        }
        catch(SQLException e)
        {
            throw new RuntimeSQLException(e, "error reading old props ");
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, rs);
        }
    }


    private static void insertOldProperties(PK itemPK, PK itemTypePK, Collection toInsert, String tableName)
    {
        PreparedStatement pstmt = null;
        Connection conn = null;
        StringBuilder query = new StringBuilder();
        StringBuilder errors = null;
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            HybrisDataSource ds = tenant.getDataSource();
            conn = ds.getConnection();
            query.append("INSERT INTO ").append(tableName);
            query.append("(").append("ITEMPK").append(",").append("ITEMTYPEPK").append(",").append("NAME").append(",");
            query.append("REALNAME")
                            .append(",")
                            .append("LANGPK")
                            .append(",")
                            .append("TYPE1")
                            .append(",")
                            .append("VALUESTRING1")
                            .append(",")
                            .append("VALUE1");
            query.append(") VALUES (?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query.toString());
            for(Iterator<EJBProperty> it = toInsert.iterator(); it.hasNext(); )
            {
                EJBProperty prop = it.next();
                boolean success = false;
                try
                {
                    fillInsertStatement(itemPK, itemTypePK, pstmt, prop);
                    int count = pstmt.executeUpdate();
                    success = (count > 0);
                    if(count != 1 && ds.getTenant()
                                    .getConfig()
                                    .getBoolean("hjmp.throw.concurrent.modification.exceptions", true))
                    {
                        throw new EJBInternalException(null, "could not insert dump property " + prop + ",change count is " + count + ",item=" + itemPK + ",table=" + tableName + ",query=" + query, 0);
                    }
                }
                catch(SQLException e)
                {
                    if(errors == null)
                    {
                        errors = new StringBuilder();
                    }
                    else
                    {
                        errors.append("\n");
                    }
                    errors.append("sql error [").append(e.getErrorCode());
                    errors.append(",").append(e.getSQLState());
                    errors.append(",").append(e.getMessage());
                    errors.append(",").append(e.getLocalizedMessage());
                    errors.append(",").append(e.getClass().getName());
                    errors.append("] inserting dump property ").append(prop);
                    errors.append(",itemPK=").append(itemPK);
                    errors.append(",table=").append(tableName);
                    errors.append(",query=").append(query.toString());
                    errors.append(" : ").append(e);
                }
                finally
                {
                    prop.wroteChanges(success);
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.err);
            errors = new StringBuilder();
            errors.append("error inserting dump property : ").append(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, null);
        }
        if(errors != null)
        {
            throw new EJBInternalException(null, errors.toString(), 0);
        }
    }


    private static void removeOldProperties(PK itemPK, PK itemTypePK, Collection<EJBProperty> toRemove, String tableName)
    {
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder();
        Connection conn = null;
        StringBuilder errors = null;
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            conn = tenant.getDataSource().getConnection();
            query.setLength(0);
            query.append("DELETE FROM ").append(tableName).append(" WHERE ");
            query.append("ITEMPK").append("= ? AND ");
            query.append("LANGPK").append("= ? AND ");
            query.append("NAME").append("= ?");
            pstmt = conn.prepareStatement(query.toString());
            for(EJBProperty prop : toRemove)
            {
                jdbcValueMappings.fillStatement(pstmt,
                                Arrays.asList(new Serializable[] {(Serializable)itemPK, (prop.getLang() == null) ? (Serializable)PK.NULL_PK : (Serializable)prop.getLang(), prop
                                                .getName().toLowerCase(LocaleHelper.getPersistenceLocale())}));
                boolean success = false;
                try
                {
                    int count = pstmt.executeUpdate();
                    pstmt.clearParameters();
                    success = (count > 0);
                    if(count != 1)
                    {
                        throw new EJBInternalException(null, "could not delete dump property " + prop + ",change count is " + count + ",item=" + itemPK + ",table=" + tableName + ",query=" + query, 0);
                    }
                }
                catch(SQLException e)
                {
                    e.printStackTrace(System.err);
                    if(errors == null)
                    {
                        errors = new StringBuilder();
                    }
                    else
                    {
                        errors.append("\n");
                    }
                    errors.append("error removing dump property ").append(prop);
                    errors.append(",itemPK=").append(itemPK);
                    errors.append(",table=").append(tableName);
                    errors.append(",query=").append(query);
                    errors.append(" : ").append(e);
                }
                finally
                {
                    prop.wroteChanges(success);
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.err);
            errors = new StringBuilder();
            errors.append("error removing dump properties : ").append(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, null);
        }
        if(errors != null)
        {
            throw new EJBInternalException(null, errors.toString(), 0);
        }
    }


    private static void updateOldProperties(PK itemPK, PK itemTypePK, Collection toUpdate, String tableName)
    {
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder();
        StringBuilder errors = null;
        Connection conn = null;
        try
        {
            Tenant tenant = Registry.getCurrentTenant();
            tenant.forceMasterDataSource();
            HybrisDataSource ds = tenant.getDataSource();
            conn = ds.getConnection();
            query.append("UPDATE ");
            query.append(tableName).append(" SET ");
            query.append("TYPE1")
                            .append("=?, ")
                            .append("VALUESTRING1")
                            .append("=?, ")
                            .append("VALUE1")
                            .append("=?, ")
                            .append("ITEMTYPEPK")
                            .append("=? WHERE ");
            query.append("ITEMPK").append("=? AND ").append("LANGPK").append("=? AND ").append("NAME=?");
            pstmt = conn.prepareStatement(query.toString());
            for(Iterator<EJBProperty> it = toUpdate.iterator(); it.hasNext(); )
            {
                EJBProperty prop = it.next();
                boolean success = false;
                try
                {
                    int paramIndex = fillUpdateStatement(pstmt, prop, itemTypePK);
                    fillSelectParameters(paramIndex, pstmt, itemPK, prop);
                    int count = pstmt.executeUpdate();
                    success = (count > 0);
                    if(count != 1 && ds.getTenant()
                                    .getConfig()
                                    .getBoolean("hjmp.throw.concurrent.modification.exceptions", true))
                    {
                        throw new EJBInternalException(null, "could not update dump property " + prop + ",change count is " + count + ",item=" + itemPK + ",table=" + tableName + ",query=" + query, 0);
                    }
                }
                catch(SQLException e)
                {
                    e.printStackTrace(System.err);
                    if(errors == null)
                    {
                        errors = new StringBuilder();
                    }
                    else
                    {
                        errors.append("\n");
                    }
                    errors.append("error updating dump property ").append(prop);
                    errors.append(",itemPK=").append(itemPK);
                    errors.append(",table=").append(tableName);
                    errors.append(",query=").append(query);
                    errors.append(" : ").append(e);
                }
                finally
                {
                    prop.wroteChanges(success);
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.err);
            errors = new StringBuilder();
            errors.append("error updating dump property : ").append(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, null);
        }
        if(errors != null)
        {
            throw new EJBInternalException(null, errors.toString(), 0);
        }
    }


    private static void fillSelectParameters(int startIndex, PreparedStatement statement, PK itemPK, EJBProperty prop) throws SQLException
    {
        PK langPK = (prop.getLang() == null) ? PK.NULL_PK : prop.getLang();
        JDBCValueMappings.ValueWriter wr = jdbcValueMappings.getValueWriter(PK.class);
        wr.setValue(statement, startIndex, itemPK);
        wr.setValue(statement, startIndex + 1, langPK);
        statement.setString(startIndex + 2, prop.getName().toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public static EJBProperty readOldProperty(ResultSet rs, PK itemPK) throws SQLException
    {
        String name = rs.getString("REALNAME");
        PK langPK = (PK)jdbcValueMappings.PK_READER.getValue(rs, "LANGPK");
        if(PK.NULL_PK.equals(langPK))
        {
            langPK = null;
        }
        int typeCode = rs.getInt("TYPE1");
        Object value = getValue(rs, "VALUESTRING1", "VALUE1", typeCode);
        if(value == null)
        {
            log.error("found NULL value for (item=" + itemPK + ",name=" + name + ",lang=" + langPK + ",typeCode=" + typeCode + ") - using '' instead");
            value = "";
        }
        return EJBProperty.load(name, langPK, value);
    }


    private static final Map VALUE_TYPE_MAPPINGS = new HashMap<>();

    static
    {
        VALUE_TYPE_MAPPINGS.put(String.class, Integer.valueOf(1));
        VALUE_TYPE_MAPPINGS.put(Integer.class, Integer.valueOf(6));
        VALUE_TYPE_MAPPINGS.put(Boolean.class, Integer.valueOf(2));
        VALUE_TYPE_MAPPINGS.put(Long.class, Integer.valueOf(4));
        VALUE_TYPE_MAPPINGS.put(Double.class, Integer.valueOf(3));
        VALUE_TYPE_MAPPINGS.put(Date.class, Integer.valueOf(5));
        VALUE_TYPE_MAPPINGS.put(Date.class, Integer.valueOf(5));
        VALUE_TYPE_MAPPINGS.put(Timestamp.class, Integer.valueOf(5));
        VALUE_TYPE_MAPPINGS.put(ItemPropertyValue.class, Integer.valueOf(11));
        VALUE_TYPE_MAPPINGS.put(Class.class, Integer.valueOf(12));
    }

    public static int getValueTypeCode(Object value)
    {
        if(value == null)
        {
            return 0;
        }
        if(value instanceof String && ((String)value).length() > 999)
        {
            return 10;
        }
        Integer code = (Integer)VALUE_TYPE_MAPPINGS.get(value.getClass());
        return (code != null) ? code.intValue() : 7;
    }


    private static final Integer ZERO_INT = Integer.valueOf(0);
    private static final Integer SUBZERO_INT = Integer.valueOf(-1);
    private static final Double ZERO_DOUBLE = new Double(0.0D);
    private static final Long ZERO_LONG = Long.valueOf(0L);
    private static final String TRUE_STRING = "1";
    private static final String FALSE_STRING = "0";


    private static Object getObjectFromString(String str, int type)
    {
        if(str == null)
        {
            str = "";
        }
        Object ret = null;
        switch(type)
        {
            case 1:
                ret = str;
                return ret;
            case 2:
                ret = "1".equals(str) ? Boolean.TRUE : Boolean.FALSE;
                return ret;
            case 6:
                ret = Integer.valueOf(str);
                if(ZERO_INT.equals(ret))
                {
                    ret = ZERO_INT;
                }
                if(SUBZERO_INT.equals(ret))
                {
                    ret = SUBZERO_INT;
                }
                return ret;
            case 3:
                ret = new Double(str);
                if(Utilities.fuzzyEquals(ZERO_DOUBLE.doubleValue(), ((Double)ret).doubleValue()))
                {
                    ret = ZERO_DOUBLE;
                }
                return ret;
            case 4:
                ret = Long.valueOf(str);
                if(ZERO_LONG.equals(ret))
                {
                    ret = ZERO_LONG;
                }
                return ret;
        }
        return str;
    }


    private static Object getValue(ResultSet rs, String stringField, String blobField, int type) throws SQLException
    {
        Object<?> v = null;
        try
        {
            switch(type)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 6:
                    v = (Object<?>)getObjectFromString(rs.getString(stringField), type);
                    return v;
                case 5:
                    v = (Object<?>)rs.getString(stringField);
                    v = (Object<?>)((v != null) ? new Date(Long.parseLong((String)v)) : null);
                    return v;
                case 10:
                    v = (Object<?>)jdbcValueMappings.getValueReader(Serializable.class).getValue(rs, blobField);
                    return v;
                case 7:
                    v = (Object<?>)jdbcValueMappings.getValueReader(Serializable.class).getValue(rs, blobField);
                    return v;
                case 11:
                    v = (Object<?>)rs.getString(stringField);
                    v = (Object<?>)((v != null) ? new ItemPropertyValue(PK.parse((String)v)) : null);
                    return v;
                case 12:
                    v = (Object<?>)rs.getString(stringField);
                    if(v != null)
                    {
                        try
                        {
                            v = (Object<?>)Class.forName((String)v);
                        }
                        catch(ClassNotFoundException e)
                        {
                            throw new JaloSystemException(e, "!!", 0);
                        }
                    }
                    return v;
            }
            throw new EJBInternalException(null, "illegal property type : " + type + " (ignored)", 0);
        }
        catch(NumberFormatException e)
        {
            throw new EJBInternalException(e, "could not decode property native type [type=" + type + ",vStr=" + rs.getString(stringField) + "] (ignored)", 0);
        }
    }


    private static String getOldAllPropertiesSelect(String oldPropTableName)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append("REALNAME").append(",");
        query.append("LANGPK").append(",");
        query.append("TYPE1").append(",");
        query.append("VALUESTRING1").append(",");
        query.append("VALUE1").append(" FROM ");
        query.append(oldPropTableName).append(" WHERE ").append("ITEMPK").append(" = ? ");
        return query.toString();
    }


    private static int fillUpdateStatement(PreparedStatement pstmt, EJBProperty prop, PK typePK) throws SQLException
    {
        Object v1 = prop.getValue1Internal();
        int t1 = getValueTypeCode(v1);
        if(t1 == 0)
        {
            log.error("illegal state - writing NULL as property value (prop=" + prop + ")");
        }
        int count = 2;
        t1 = fillValueStatement(pstmt, t1, v1, count++, count++);
        pstmt.setInt(1, t1);
        jdbcValueMappings.PK_WRITER.setValue(pstmt, count++, typePK);
        return count;
    }


    private static void fillInsertStatement(PK itemPK, PK itemTypePK, PreparedStatement pstmt, EJBProperty prop) throws SQLException
    {
        Object v1 = prop.getValue1Internal();
        int t1 = getValueTypeCode(v1);
        if(t1 == 0)
        {
            log.error("illegal state - inserting NULL as property value (prop=" + prop + ") and item " + itemPK);
        }
        int count = 1;
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = jdbcValueMappings.PK_WRITER;
        pkWriter.setValue(pstmt, count++, itemPK);
        pkWriter.setValue(pstmt, count++, (itemTypePK == null) ? PK.NULL_PK : itemTypePK);
        pstmt.setString(count++, prop.getName().toLowerCase(LocaleHelper.getPersistenceLocale()));
        pstmt.setString(count++, prop.getName());
        if(prop.getLang() != null)
        {
            pkWriter.setValue(pstmt, count++, prop.getLang());
        }
        else
        {
            pkWriter.setValue(pstmt, count++, PK.NULL_PK);
        }
        int typePos = count++;
        t1 = fillValueStatement(pstmt, t1, v1, count++, count++);
        pstmt.setInt(typePos, t1);
    }


    public static int fillValueStatement(PreparedStatement pstmt, int typeCode, Object value, int stringIdx, int blobIdx) throws SQLException
    {
        String str;
        ItemPropertyValue ipv;
        Class c;
        String cutString = null;
        if(typeCode == 1)
        {
            cutString = PropertyJDBC.cutStringToByteLength((String)value, Config.getInt("property.maxlength", 3999));
            if(cutString.length() != ((String)value).length())
            {
                typeCode = 10;
            }
        }
        int binaryType = getBinaryTypeForDb();
        switch(typeCode)
        {
            case 0:
                pstmt.setNull(stringIdx, 12);
                pstmt.setNull(blobIdx, binaryType);
                return typeCode;
            case 2:
                pstmt.setString(stringIdx, ((Boolean)value).booleanValue() ? "1" : "0");
                pstmt.setNull(blobIdx, binaryType);
                return typeCode;
            case 1:
            case 3:
            case 4:
            case 6:
                pstmt.setString(stringIdx, value.toString());
                pstmt.setNull(blobIdx, binaryType);
                return typeCode;
            case 5:
                pstmt.setString(stringIdx, Long.toString(((Date)value).getTime()));
                pstmt.setNull(blobIdx, binaryType);
                return typeCode;
            case 7:
            case 10:
                str = (cutString != null) ? cutString : PropertyJDBC.cutStringToByteLength(value.toString(), Config.getInt("property.maxlength", 3999));
                pstmt.setString(stringIdx, str);
                jdbcValueMappings.getValueWriter(Serializable.class).setValue(pstmt, blobIdx, value);
                return typeCode;
            case 11:
                ipv = (ItemPropertyValue)value;
                pstmt.setString(stringIdx, ipv.getPK().toString());
                pstmt.setNull(blobIdx, binaryType);
                return typeCode;
            case 12:
                c = (Class)value;
                pstmt.setString(stringIdx, c.getName());
                pstmt.setNull(blobIdx, binaryType);
                return typeCode;
        }
        throw new EJBInternalException(null, "illegal property value type : " + typeCode + " (ignored)", 0);
    }


    private static int getBinaryTypeForDb()
    {
        int binaryType = 2004;
        if(Config.isPostgreSQLUsed())
        {
            binaryType = -4;
        }
        return binaryType;
    }
}
