package de.hybris.bootstrap.ddl.tools.persistenceinfo;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import de.hybris.bootstrap.ddl.DataBaseProvider;
import de.hybris.bootstrap.ddl.DataSourceCreator;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.HybrisDatabaseSettingsFactory;
import de.hybris.bootstrap.ddl.PropertiesLoader;
import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class PersistenceInformation
{
    private static final String COMPOSED_TYPE = "ComposedType";
    private static final String[] TYPE_SYSTEM_RELATED_TYPES = new String[] {"AtomicType", "AttributeDescriptor", "CollectionType", "ComposedType", "EnumerationValue", "MapType"};
    public static final String DEFAULT_TYPE_SYSTEM_NAME = "DEFAULT";
    public static final Set<String> TYPE_SYSTEM_DEPLOYMENTS = (Set<String>)ImmutableSet.builder()
                    .addAll(Lists.newArrayList((Object[])TYPE_SYSTEM_RELATED_TYPES)).build();
    private final JdbcTemplate jdbcTemplate;
    private final DatabaseSettings dbSettings;
    private final String ydeploymentsTableName;
    private Set<String> allTablesCache;


    public static boolean isTypeSystemRelatedDeployment(String name)
    {
        return TYPE_SYSTEM_DEPLOYMENTS.contains(name);
    }


    public PersistenceInformation(DataSourceCreator dataSourceCreator, PropertiesLoader propertiesLoader)
    {
        this(dataSourceCreator.createDataSource((new HybrisDatabaseSettingsFactory(
                        Objects.<PropertiesLoader>requireNonNull(propertiesLoader))).createDatabaseSettings()), propertiesLoader);
    }


    public PersistenceInformation(DataSource dataSource, PropertiesLoader propertiesLoader)
    {
        this.jdbcTemplate = new JdbcTemplate(Objects.<DataSource>requireNonNull(dataSource));
        HybrisDatabaseSettingsFactory dbSettingsFactory = new HybrisDatabaseSettingsFactory(Objects.<PropertiesLoader>requireNonNull(propertiesLoader));
        this.dbSettings = dbSettingsFactory.createDatabaseSettings();
        this.ydeploymentsTableName = toRealTableName("ydeployments");
    }


    public DataBaseProvider getDbType()
    {
        return this.dbSettings.getDataBaseProvider();
    }


    public String getDeploymentsTableName()
    {
        return this.ydeploymentsTableName;
    }


    public String getTypeSystemName()
    {
        return this.dbSettings.getTypeSystemName();
    }


    public boolean containsTypeSystemPropsTable()
    {
        String typeSystemPropsTableName = toRealTableName("typesystemprops");
        for(String tableName : allTables())
        {
            if(tableName.startsWith(typeSystemPropsTableName))
            {
                return true;
            }
        }
        return false;
    }


    public boolean containsTable(String tableName)
    {
        return allTables().contains(tableName);
    }


    public boolean isKnownTypeSystem(String typeSystemName)
    {
        Objects.requireNonNull(typeSystemName);
        return getAllTypeSystems().contains(typeSystemName);
    }


    public boolean isDefaultTypeSystem(String typeSystemName)
    {
        Objects.requireNonNull(typeSystemName);
        return typeSystemName.equalsIgnoreCase("DEFAULT");
    }


    public Set<String> getAllTypeSystems()
    {
        String query = "select distinct typesystemname from " + this.ydeploymentsTableName;
        return (Set<String>)ImmutableSet.builder().addAll(this.jdbcTemplate.queryForList(query, String.class)).build();
    }


    public <T> T query(String query, Class<T> clazz)
    {
        return (T)this.jdbcTemplate.queryForObject(query, clazz);
    }


    public Set<String> getDeploymentColumnNames()
    {
        return (Set<String>)this.jdbcTemplate.execute((ConnectionCallback)new Object(this));
    }


    public Iterable<DeploymentRow> getAllDeploymentRowsForTypeSystem(String typeSystemName)
    {
        Objects.requireNonNull(typeSystemName);
        String query = "select * from " + this.ydeploymentsTableName + " where typesystemname=?";
        Object[] params = {typeSystemName};
        return this.jdbcTemplate.query(query, params, (RowMapper)new Object(this));
    }


    public TypeSystemRelatedDeployments getTypeSystemRelatedDeployments(String typeSystemName)
    {
        TypeSystemRelatedDeployments basicInfo = getBasicDeploymentsInformation(typeSystemName);
        String composedTypeTable = basicInfo.findByName("ComposedType").getTableName();
        TypeSystemRelatedDeployments result = new TypeSystemRelatedDeployments();
        String typePKsQuery = "select pk from " + composedTypeTable + " where itemjndiname=?";
        for(TypeSystemRelatedDeployment deployment : basicInfo)
        {
            List<Long> typePKs = this.jdbcTemplate.queryForList(typePKsQuery, Long.class, new Object[] {deployment.getJndiName()});
            result.add(deployment.with((Iterable)ImmutableSet.copyOf(typePKs)));
        }
        return result;
    }


    private TypeSystemRelatedDeployments getBasicDeploymentsInformation(String typeSystemName)
    {
        boolean isHana = (this.dbSettings.getDataBaseProvider() == DataBaseProvider.HANA);
        String query = getBasicDeploymentsInformationQuery(isHana);
        Collection params = Lists.newArrayList((Object[])TYPE_SYSTEM_RELATED_TYPES);
        params.add(Objects.requireNonNull(typeSystemName));
        TypeSystemRelatedDeployments result = new TypeSystemRelatedDeployments();
        this.jdbcTemplate.query(query, params.toArray(), (RowCallbackHandler)new Object(this, isHana, result));
        return result;
    }


    private String getBasicDeploymentsInformationQuery(boolean isHana)
    {
        String inClause = "(?" + StringUtils.repeat(", ?", TYPE_SYSTEM_RELATED_TYPES.length - 1) + ")";
        return isHana ? ("select *, (select t.IS_COLUMN_TABLE from TABLES t where t.SCHEMA_NAME=CURRENT_SCHEMA and lower(TABLE_NAME)=lower(tablename)) as IS_COLUMN_TABLE from " +
                        this.ydeploymentsTableName + " where name in " + inClause + " and typesystemname = ?") : ("select * from " +
                        this.ydeploymentsTableName + " where name in " + inClause + " and typesystemname = ?");
    }


    public String toRealTableName(String tableName)
    {
        String tableNameLowerCase = ((String)Objects.<String>requireNonNull(tableName, "Table name can't be null")).toLowerCase(LocaleHelper.getPersistenceLocale());
        String tablePrefix = getTablePrefix();
        if(tablePrefix.isEmpty())
        {
            return tableNameLowerCase;
        }
        return tablePrefix + tablePrefix;
    }


    public String toGeneralTableName(String tableName)
    {
        String tableNameLowerCase = ((String)Objects.<String>requireNonNull(tableName, "Table name can't be null")).toLowerCase(LocaleHelper.getPersistenceLocale());
        String tablePrefix = getTablePrefix();
        if(tablePrefix.isEmpty())
        {
            return tableNameLowerCase;
        }
        if(tableNameLowerCase.startsWith(tablePrefix))
        {
            return StringUtils.substring(tableNameLowerCase, tablePrefix.length());
        }
        return tableNameLowerCase;
    }


    private String getTablePrefix()
    {
        String prefix = this.dbSettings.getTablePrefix();
        return (prefix == null) ? "" : prefix.toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    private Set<String> allTables()
    {
        if(this.allTablesCache == null)
        {
            try
            {
                this.allTablesCache = getAllTableNamesFromDatabase();
            }
            catch(SQLException e)
            {
                throw this.jdbcTemplate.getExceptionTranslator().translate("Getting all table names from database", null, e);
            }
        }
        return this.allTablesCache;
    }


    private Set<String> getAllTableNamesFromDatabase() throws SQLException
    {
        ImmutableSet.Builder<String> tables = ImmutableSet.builder();
        this.jdbcTemplate.execute((ConnectionCallback)new Object(this, tables));
        return (Set<String>)tables.build();
    }
}
