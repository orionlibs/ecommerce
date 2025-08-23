package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.flexiblesearch.TypedNull;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jalo.flexiblesearch.internal.HintsApplier;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.Utilities;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

public class JDBCValueMappings
{
    private static final Logger LOG = Logger.getLogger(JDBCValueMappings.class.getName());
    public static final Integer T = Integer.valueOf(1);
    public static final Integer F = Integer.valueOf(0);
    public static final String UNLOC_TABLE_POSTFIX = "UP".toLowerCase().intern();
    public static final String LOC_TABLE_POSTFIX = "LP".toLowerCase().intern();
    private static final Map<Class<?>, Class<?>> primitiveClassMap = new HashMap<>();
    public final ValueWriter<PK, ?> PK_WRITER;
    public final ValueReader<PK, ?> PK_READER;
    public final ValueWriter<String, ?> STRING_WRITER;
    public final ValueReader<String, ?> STRING_READER;
    private final Class<?> rootItemClass;
    private Map<Class<?>, AbstractValueReaderWriter<?, ?>> javaRWMappingInternal;
    boolean pkIsString;

    static
    {
        primitiveClassMap.put(int.class, Integer.class);
        primitiveClassMap.put(byte.class, Byte.class);
        primitiveClassMap.put(short.class, Short.class);
        primitiveClassMap.put(char.class, Character.class);
        primitiveClassMap.put(double.class, Double.class);
        primitiveClassMap.put(boolean.class, Boolean.class);
        primitiveClassMap.put(long.class, Long.class);
        primitiveClassMap.put(float.class, Float.class);
    }

    private static final int BIG_DECIMAL_SCALE = Registry.getMasterTenant().getConfig()
                    .getInt("jdbcmappings.big_decimal_scale", 8);
    private static final int FLOAT_SCALE = Registry.getMasterTenant().getConfig().getInt("jdbcmappings.float_scale", 5);
    private static final String SPACE = " ";


    public static JDBCValueMappings getInstance()
    {
        return (JDBCValueMappings)Registry.getNonTenantSingleton((SingletonCreator.Creator)new Object());
    }


    private JDBCValueMappings()
    {
        this.rootItemClass = ItemRemote.class;
        initJavaRWMapping();
        this.PK_WRITER = getValueWriter(PK.class);
        this.PK_READER = getValueReader(PK.class);
        this.STRING_WRITER = getValueWriter(String.class);
        this.STRING_READER = getValueReader(String.class);
    }


    public static final Class<?> convertPrimitiveTypes(Class<?> c)
    {
        if(c.isPrimitive())
        {
            Class<?> mapped = primitiveClassMap.get(c);
            return (mapped != null) ? mapped : c;
        }
        return c;
    }


    private Class filterItemClasses(Class<?> maybeItemClass)
    {
        return (this.rootItemClass != null && this.rootItemClass.isAssignableFrom(maybeItemClass)) ? this.rootItemClass : maybeItemClass;
    }


    public final ValueReader getValueReader(Class<?> javaClass) throws IllegalArgumentException
    {
        ValueReader<?, ?> vw = (ValueReader<?, ?>)this.javaRWMappingInternal.get(filterItemClasses(convertPrimitiveTypes(javaClass)));
        if(vw == null)
        {
            vw = (ValueReader<?, ?>)this.javaRWMappingInternal.get(Serializable.class);
        }
        if(vw == null)
        {
            throw new IllegalArgumentException("not even a default ValueReader found for " + javaClass + " (mapping is " + this.javaRWMappingInternal + ")");
        }
        return vw;
    }


    public final ValueWriter getValueWriter(Class<?> javaClass) throws IllegalArgumentException
    {
        ValueWriter<?, ?> vw = (ValueWriter<?, ?>)this.javaRWMappingInternal.get(filterItemClasses(convertPrimitiveTypes(javaClass)));
        if(vw == null)
        {
            vw = (ValueWriter<?, ?>)this.javaRWMappingInternal.get(Serializable.class);
        }
        if(vw == null)
        {
            throw new IllegalArgumentException("not even a default ValueWriter found for " + javaClass + " (mapping is " + this.javaRWMappingInternal + ")");
        }
        return vw;
    }


    public static <JALO, DB> ValueReader<JALO, DB> getJDBCValueReader(Class<? extends JALO> javaClass)
    {
        return getInstance().getValueReader(javaClass);
    }


    public static <JALO, DB> ValueWriter<JALO, DB> getJDBCValueWriter(Class<? extends JALO> javaClass)
    {
        return getInstance().getValueWriter(javaClass);
    }


    public final PreparedStatement fillStatement(PreparedStatement stmt, List values) throws SQLException
    {
        if(values == null || values.isEmpty())
        {
            return stmt;
        }
        for(int i = 0, s = values.size(); i < s; i++)
        {
            Object value = values.get(i);
            if(value == null)
            {
                throw new IllegalArgumentException("you cannot search for NULL values - use 'IS NULL' statements instead ( nr = " + i + ", values = " + values + ")");
            }
            if(value instanceof TypedNull)
            {
                Class<?> clazz = ((TypedNull)value).getElementType();
                setValue(stmt, i + 1, getTypedNullValue(clazz), clazz);
            }
            else
            {
                setValue(stmt, i + 1, value, value.getClass());
            }
        }
        return stmt;
    }


    private Object getTypedNullValue(Class<?> clazz)
    {
        if(clazz.equals(String.class))
        {
            return "$does$not$match$###";
        }
        if(PK.class.isAssignableFrom(clazz) || Item.class.isAssignableFrom(clazz) || ItemRemote.class
                        .isAssignableFrom(clazz) || ItemPropertyValue.class.isAssignableFrom(clazz))
        {
            return PK.NULL_PK;
        }
        if(Number.class.isAssignableFrom(clazz))
        {
            return null;
        }
        throw new IllegalArgumentException("empty value with a TypedNULL with invalid class " + clazz.getName());
    }


    public final RowFetchResult getQueryResults(ResultSet rs, List<Class<?>> resultClasses, int start, int count) throws SQLException
    {
        return getRows(rs, resultClasses, start, count);
    }


    public final int getTotalCount(ResultSet rs, RowFetchResult rfr, int start, int count, Connection con, TranslatedQuery.ExecutableQuery executableQuery, boolean rsWasLimited) throws SQLException
    {
        return getTotalCount(rs, rfr, start, count, con, executableQuery, rsWasLimited, Collections.emptyList());
    }


    public final int getTotalCount(ResultSet rs, RowFetchResult rfr, int start, int count, Connection con, TranslatedQuery.ExecutableQuery executableQuery, boolean rsWasLimited, List<Hint> hints) throws SQLException
    {
        boolean useStart = (start > 0);
        boolean useCount = (count >= 0);
        boolean atEnd = rfr.atEnd;
        int fetched = rfr.rows.size();
        if(!useStart && count > 0 && fetched == 0)
        {
            return 0;
        }
        if(!rsWasLimited && atEnd)
        {
            return rfr.lastResultSetPosition + 1;
        }
        if(useStart || useCount)
        {
            int threshold = getScrollableThreshold();
            int pos = rfr.lastResultSetPosition;
            boolean thresholdActive = !Config.isHSQLDBUsed();
            while(!rsWasLimited && (!thresholdActive || pos < threshold) && rs.next())
            {
                pos++;
            }
            if(!rsWasLimited && (!thresholdActive || (pos < threshold && threshold > 0)))
            {
                return pos + 1;
            }
            return getTotalCountFromCountQuery(con, executableQuery.getCountSQL(), executableQuery.getCountValueList(), hints);
        }
        return fetched;
    }


    public int getTotalCountFromCountQuery(Connection con, String query, List values) throws SQLException
    {
        return getTotalCountFromCountQuery(con, query, values, Collections.emptyList());
    }


    public int getTotalCountFromCountQuery(Connection con, String query, List values, List<Hint> hints) throws SQLException
    {
        PreparedStatement countStmt = null;
        ResultSet countRs = null;
        try
        {
            StringBuilder countQuery = new StringBuilder();
            countQuery.append("SELECT COUNT(*) AS CNT FROM ( ").append(query).append(" ) CNTTABLEALIAS");
            countStmt = con.prepareStatement(HintsApplier.filterAndApplyQueryHints(countQuery.toString(), hints));
            fillStatement(HintsApplier.filterAndApplyPreparedStatementHints(countStmt, hints), values);
            countRs = countStmt.executeQuery();
            countRs.next();
            return countRs.getInt("CNT");
        }
        finally
        {
            Utilities.tryToCloseJDBC(null, countStmt, countRs);
        }
    }


    public static final int getScrollableThreshold()
    {
        String param = Config.getParameter("jdbcmappings.scrollablethreshold");
        int threshold = -1;
        try
        {
            threshold = Integer.parseInt(param);
        }
        catch(NumberFormatException numberFormatException)
        {
        }
        return threshold;
    }


    private RowFetchResult getRows(ResultSet rs, List<Class<?>> resultClasses, int start, int count) throws IllegalArgumentException, SQLException
    {
        List<Object> res = new ArrayList();
        Objects.requireNonNull(res);
        Pair<Boolean, Integer> ret = processRows(rs, resultClasses, start, count, res::add);
        return new RowFetchResult(res, ((Integer)ret.getRight()).intValue(), ((Boolean)ret.getLeft()).booleanValue());
    }


    public Pair<Boolean, Integer> processRows(ResultSet rs, List<Class<?>> resultClasses, int start, int count, Consumer<Object> rowConsumer) throws SQLException
    {
        boolean useCount = (count >= 0);
        int pos = -1;
        boolean done = !rs.next();
        if(!done)
        {
            pos++;
            while(pos < start)
            {
                if(!(done = !rs.next()))
                {
                    pos++;
                }
            }
            if(!done && (!useCount || count > 0))
            {
                int rowCount = count;
                do
                {
                    rowConsumer.accept(readColumns(rs, resultClasses));
                    done = !rs.next();
                    if(done)
                    {
                        continue;
                    }
                    pos++;
                }
                while(!done && (!useCount || --rowCount > 0));
            }
        }
        return Pair.of(Boolean.valueOf(done), Integer.valueOf(pos));
    }


    private Object readColumns(ResultSet rs, List<Class<?>> resultClasses) throws SQLException
    {
        int columnCount = resultClasses.size();
        if(columnCount == 1)
        {
            return readValue(rs, 0, resultClasses.get(0));
        }
        Object[] data = new Object[columnCount];
        int i = 0;
        for(Class<?> cl : resultClasses)
        {
            data[i] = readValue(rs, i, cl);
            i++;
        }
        return Arrays.asList(data);
    }


    private Object readValue(ResultSet rs, int idx, Class<?> resultClass) throws IllegalArgumentException, SQLException
    {
        return getValueReader(resultClass).getValue(rs, idx + 1);
    }


    private void setValue(PreparedStatement pstmt, int pos, Object value, Class<?> valueClass) throws IllegalArgumentException, SQLException
    {
        getValueWriter(valueClass).setValue(pstmt, pos, value);
    }


    public void initJavaRWMapping()
    {
        this.javaRWMappingInternal = new HashMap<>();
        this.javaRWMappingInternal.put(String.class, new DefaultStringValueReaderWriter());
        this.javaRWMappingInternal.put(Class.class, new Object(this));
        this.javaRWMappingInternal.put(Integer.class, new DefaultIntegerValueReaderWriter());
        this.javaRWMappingInternal.put(PK.class, new PKReaderWriter());
        this.javaRWMappingInternal.put(Byte.class, new Object(this));
        this.javaRWMappingInternal.put(Short.class, new Object(this));
        this.javaRWMappingInternal.put(Character.class, new Object(this));
        this.javaRWMappingInternal.put(Double.class, new Object(this));
        this.javaRWMappingInternal.put(BigDecimal.class, new Object(this));
        this.javaRWMappingInternal.put(Boolean.class, new Object(this));
        this.javaRWMappingInternal.put(Long.class, new DefaultLongReaderWriter());
        this.javaRWMappingInternal.put(Float.class, new Object(this));
        this.javaRWMappingInternal.put(Date.class, new DefaultDateTimestampValueReaderWriter());
        this.javaRWMappingInternal.put(Serializable.class, new BlobValueWriter());
        this.javaRWMappingInternal.put(Object.class, this.javaRWMappingInternal.get(Serializable.class));
        this.javaRWMappingInternal.put(ItemRemote.class, new ItemReaderWriter());
        this.javaRWMappingInternal.put(ItemPropertyValue.class, new ItemPropertyValueReaderWriter());
        this.javaRWMappingInternal.put(ItemPropertyValueCollection.class, new Object(this));
    }


    public static byte[] stringToByteArray(String str)
    {
        byte[] res = new byte[str.length() / 2];
        for(int i = 0; i < str.length() - 1; i += 2)
        {
            res[i / 2] = (byte)Integer.parseInt(String.valueOf(str.charAt(i)) + String.valueOf(str.charAt(i)), 16);
        }
        return res;
    }


    public String pkToSQL(PK pk)
    {
        PK pkToSet;
        if(pk == null)
        {
            pkToSet = PK.NULL_PK;
        }
        else
        {
            pkToSet = pk;
        }
        StringBuilder b = new StringBuilder(" ");
        b.append(pkToSet.getLongValueAsString());
        b.append(" ");
        return b.toString();
    }


    public String getDumpPropertyMovalQuery(String dumpTableName, String name, int typeCode)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * ").append("FROM ").append(dumpTableName).append(" ");
        query.append("WHERE ").append("NAME").append(" = '").append(name.toLowerCase()).append("'");
        query.append("ORDER BY ").append("ITEMPK").append(" ASC");
        return query.toString();
    }


    public String getDumpPropertyCountQuery(String dumpTableName, String name, int typeCode)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) ");
        query.append("FROM ").append(dumpTableName).append(" ");
        query.append("WHERE ").append("NAME").append(" = '").append(name.toLowerCase()).append("' AND ");
        query.append("CAST(").append("ITEMPK").append("/281474976710656 AS ")
                        .append(Config.isMySQLUsed() ? "UNSIGNED " : "").append("INTEGER) = ").append(typeCode).append(" ");
        query.append("ORDER BY ").append("ITEMPK").append(" ASC");
        return query.toString();
    }


    public static boolean isChar2CharMappingEnabled()
    {
        return !Config.getBoolean("db.mapping.char.legacy", true);
    }
}
