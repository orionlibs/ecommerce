package de.hybris.platform.cache.relation;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.MessageFormatUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

public class RelationCacheUnit extends AbstractCacheUnit
{
    private static final Logger LOG = LoggerFactory.getLogger(RelationCacheUnit.class);
    public static final int INVALIDATION_DEPTH = 2;
    private static final Map<String, Map<String, Boolean>> IS_SOURCE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, String>> TABLE_NAME_CACHE = new ConcurrentHashMap<>();
    private static final int COLUMN_ID_WHEN_SOURCE = 3;
    private static final int COLUMN_ID_WHEN_TARGET = 2;
    private final JdbcTemplate jdbcTemplate;

    static
    {
        Registry.registerTenantListener((TenantListener)new Object());
    }

    private final JDBCValueMappings.ValueReader<PK, ?> pkReader = (JDBCValueMappings.getInstance()).PK_READER;
    private final JDBCValueMappings.ValueWriter<PK, ?> pkWriter = (JDBCValueMappings.getInstance()).PK_WRITER;
    private final RelationCacheKey key;
    private final boolean isSource;
    private final Object[] invalidationKey;
    private final String table;
    private final String query;


    public RelationCacheUnit(Cache cache, RelationCacheKey key)
    {
        super(cache, key.getTenantId());
        this.key = key;
        this.jdbcTemplate = (JdbcTemplate)Registry.getApplicationContext().getBean("jdbcTemplate", JdbcTemplate.class);
        this.invalidationKey = key.toKey();
        this.isSource = isSourceOfRelation(key.getRelationSideId());
        this.table = getTableName(key.getRelationId());
        this.query = createQuery();
    }


    public String getRelationId()
    {
        return getKey().getRelationId();
    }


    public String getRelationSideId()
    {
        return getKey().getRelationSideId();
    }


    public PK getParentPk()
    {
        return getKey().getParentPk();
    }


    private String createQuery()
    {
        return MessageFormatUtils.format("select pk, sourcepk, targetpk from {1} where {2} = {0}", new Object[] {this.pkWriter
                        .convertValueToSQL(this.key.getParentPk()), this.table,
                        this.isSource ? "sourcepk" : "targetpk"});
    }


    private String getTableName(String relationId)
    {
        return ((Map<String, String>)TABLE_NAME_CACHE.computeIfAbsent(getTenantID(), tenantID -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(relationId, relId -> ((RelationType)TypeManager.getInstance().getType(relId)).getTable());
    }


    public static RelationCacheUnit createRelationCacheUnit(String relationId, String relationSide, PK sourcePK)
    {
        RelationCacheKey key = new RelationCacheKey(Registry.getCurrentTenant().getTenantID(), relationId, relationSide, sourcePK);
        return new RelationCacheUnit(Registry.getCurrentTenant().getCache(), key);
    }


    public static RelationCacheUnit createRelationCacheUnit(RelationCacheKey key)
    {
        return new RelationCacheUnit(Registry.getCurrentTenant().getCache(), key);
    }


    protected static void clearCacheUnitForTenant(String tenantID)
    {
        LOG.debug("Clear relation cache unit for tenant: {} ", tenantID);
        Optional.<Map>ofNullable(IS_SOURCE_CACHE.remove(tenantID)).ifPresent(Map::clear);
        Optional.<Map>ofNullable(TABLE_NAME_CACHE.remove(tenantID)).ifPresent(Map::clear);
    }


    public Object[] createKey()
    {
        return (Object[])this.invalidationKey.clone();
    }


    public int getInvalidationTopicDepth()
    {
        return 2;
    }


    public Object compute() throws Exception
    {
        try
        {
            return this.jdbcTemplate.query(this.query, (resultSet, i) -> this.isSource ? (PK)this.pkReader.getValue(resultSet, 3) : (PK)this.pkReader.getValue(resultSet, 2));
        }
        catch(BadSqlGrammarException e)
        {
            LOG.error("Compute failed: ", (Throwable)e);
            throw new JaloItemNotFoundException(e, 0);
        }
    }


    public List<PK> getCachedPKs()
    {
        try
        {
            return (List<PK>)get();
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
    }


    public String toString()
    {
        return "RelationCacheUnit{key=" + this.key + ", isSource=" + this.isSource + ", invalidationKey=" +
                        Arrays.toString(this.invalidationKey) + ", table='" + this.table + "', query='" + this.query + "'}";
    }


    private boolean isSourceOfRelation(String relationSide)
    {
        String relId = getRelationId();
        return ((Boolean)((Map<String, Boolean>)IS_SOURCE_CACHE.computeIfAbsent(getTenantID(), tenantId -> new ConcurrentHashMap<>()))
                        .computeIfAbsent(relId + "." + relId, k -> Boolean.valueOf(((RelationType)TypeManager.getInstance().getType(relId)).getSourceAttributeDescriptor().getQualifier().equals(relationSide)))).booleanValue();
    }


    public String getTable()
    {
        return this.table;
    }


    public RelationCacheKey getKey()
    {
        return this.key;
    }
}
