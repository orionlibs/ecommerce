package de.hybris.bootstrap.ddl.tools;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.bootstrap.ddl.DataSourceCreator;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.HybrisDatabaseSettingsFactory;
import de.hybris.bootstrap.ddl.PropertiesLoader;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.DeploymentRow;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.TypeSystemRelatedDeployment;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.TypeSystemRelatedDeployments;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class TypeSystemHelper
{
    private static final Logger LOG = Logger.getLogger(TypeSystemHelper.class);
    private final PersistenceInformation persistenceInfo;


    public TypeSystemHelper(PersistenceInformation persistenceInfo)
    {
        this.persistenceInfo = Objects.<PersistenceInformation>requireNonNull(persistenceInfo);
    }


    public static void createTypeSystemBasedOnCurrentTypeSystem(DataSourceCreator dataSourceCreator, PropertiesLoader propertiesLoader, String newTypeSystemName)
    {
        Objects.requireNonNull(dataSourceCreator, "dataSourceCreator can't be null");
        Objects.requireNonNull(propertiesLoader, "propertiesLoader can't be null");
        Objects.requireNonNull(newTypeSystemName, "newTypeSystemName can't be null");
        DatabaseSettings dbSettings = (new HybrisDatabaseSettingsFactory(propertiesLoader)).createDatabaseSettings();
        DataSource dataSource = dataSourceCreator.createDataSource(dbSettings);
        copyTypeSystem(dataSource, propertiesLoader, dbSettings.getTypeSystemName(), newTypeSystemName);
        if(dbSettings.getDataBaseProvider().isHSqlUsed())
        {
            (new JdbcTemplate(dataSource)).execute("CHECKPOINT");
        }
    }


    public static void removeTypeSystem(DataSourceCreator dataSourceCreator, PropertiesLoader propertiesLoader, String typeSystemName)
    {
        Objects.requireNonNull(dataSourceCreator, "dataSourceCreator can't be null");
        Objects.requireNonNull(propertiesLoader, "propertiesLoader can't be null");
        Objects.requireNonNull(typeSystemName, "typeSystemName can't be null");
        DatabaseSettings dbSettings = (new HybrisDatabaseSettingsFactory(propertiesLoader)).createDatabaseSettings();
        DataSource dataSource = dataSourceCreator.createDataSource(dbSettings);
        SqlStatementsExecutor statementsExecutor = new SqlStatementsExecutor(dataSource);
        PersistenceInformation persistenceInfo = new PersistenceInformation(dataSource, propertiesLoader);
        TypeSystemHelper helper = new TypeSystemHelper(persistenceInfo);
        Iterable<SqlStatement> statementsToExecute = helper.getRemoveTypeSystemStatements(typeSystemName);
        statementsExecutor.execute(statementsToExecute);
    }


    public static void copyTypeSystem(DataSource dataSource, PropertiesLoader propertiesLoader, String sourceTypeSystemName, String destinationTypeSystemName)
    {
        Objects.requireNonNull(dataSource, "dataSource can't be null");
        Objects.requireNonNull(propertiesLoader, "propertiesLoader can't be null");
        Objects.requireNonNull(sourceTypeSystemName, "sourceTypeSystemName can't be null");
        Objects.requireNonNull(destinationTypeSystemName, "destinationTypeSystemName can't be null");
        SqlStatementsExecutor statementsExecutor = new SqlStatementsExecutor(dataSource);
        PersistenceInformation persistenceInfo = new PersistenceInformation(dataSource, propertiesLoader);
        TypeSystemHelper helper = new TypeSystemHelper(persistenceInfo);
        Iterable<SqlStatement> statementsToExecute = helper.getCopyTypeSystemStatements(sourceTypeSystemName, destinationTypeSystemName);
        statementsExecutor.execute(statementsToExecute);
    }


    public static void assureTypeSystemStructureIsUpToDate(DataSource dataSource, PropertiesLoader propertiesLoader)
    {
        Objects.requireNonNull(dataSource, "dataSource can't be null");
        Objects.requireNonNull(propertiesLoader, "propertiesLoader can't be null");
        SqlStatementsExecutor executor = new SqlStatementsExecutor(dataSource);
        PersistenceInformation persistenceInfo = new PersistenceInformation(dataSource, propertiesLoader);
        CreateTypeSystemNameColumnInYDeploymentsTable addTypeSystemColumn = new CreateTypeSystemNameColumnInYDeploymentsTable(persistenceInfo);
        MigrateTypeSystemProps migrateTypeSystemProps = new MigrateTypeSystemProps(persistenceInfo);
        Iterable<SqlStatement> statementsToExecute = Iterables.concat(addTypeSystemColumn.getStatementsToExecute(), migrateTypeSystemProps
                        .getStatementsToExecute());
        executor.execute(statementsToExecute);
    }


    public Iterable<SqlStatement> getCopyTypeSystemStatements(String sourceTypeSystemName, String destinationTypeSystemName)
    {
        requireInitializedDatabase();
        requireExistenceOfTypeSystem(sourceTypeSystemName);
        requireNotExistenceOfTypeSystem(destinationTypeSystemName);
        LOG.info("Copying '" + sourceTypeSystemName + "' typesystem to '" + destinationTypeSystemName + "' typesystem...");
        TypeSystemRelatedDeployments deployments = this.persistenceInfo.getTypeSystemRelatedDeployments(sourceTypeSystemName);
        Map<String, String> srcToDstTableName = findSrcToDstTablesMapping(deployments);
        List<CopyTableOperation> operations = getCopyOperationsToExecute(deployments, srcToDstTableName);
        Iterable<SqlStatement> copyStatements = (new TablesStructureCopier(this.persistenceInfo)).getCopyStatements(operations);
        List<SqlStatement> attachStatements = attachNewTypeSystemTablesToDeployments(sourceTypeSystemName, destinationTypeSystemName, srcToDstTableName);
        return Iterables.concat(copyStatements, attachStatements);
    }


    public Iterable<SqlStatement> getRemoveTypeSystemStatements(String typeSystemName)
    {
        Objects.requireNonNull(typeSystemName);
        requireTypeSystemOtherThanDefault(typeSystemName);
        ImmutableList.Builder<SqlStatement> result = ImmutableList.builder();
        Set<String> tablesToDrop = this.persistenceInfo.getTypeSystemRelatedDeployments(typeSystemName).getAllTablesWithLPTables();
        result.addAll((Iterable)FluentIterable.from(tablesToDrop).transform((Function)new Object(this)));
        String stmt = "delete from " + this.persistenceInfo.getDeploymentsTableName() + " where typesystemname=?";
        result.add(new DMLStatement(stmt, (Collection)ImmutableList.of(typeSystemName)));
        return (Iterable<SqlStatement>)result.build();
    }


    private List<CopyTableOperation> getCopyOperationsToExecute(TypeSystemRelatedDeployments deployments, Map<String, String> srcToDstTableName)
    {
        String pkColumn = "PK";
        String itemPkColumn = "ITEMPK";
        String langPkColumn = "LANGPK";
        String nameColumn = "NAME";
        ImmutableList.Builder result = ImmutableList.builder();
        Map<String, PropsTableInfo> propsTables = new HashMap<>();
        for(TypeSystemRelatedDeployment deployment : deployments)
        {
            String table = deployment.getTableName();
            String lpTable = deployment.getLPTableName();
            String propsTable = deployment.getPropsTableName();
            boolean isColumnBased = deployment.isColumnBased();
            result.add(CopyTableOperation.withData(table, srcToDstTableName.get(table), isColumnBased).withPK(new String[] {"PK"}));
            result.add(CopyTableOperation.withData(lpTable, srcToDstTableName.get(lpTable), isColumnBased).withPK(new String[] {"ITEMPK", "LANGPK"}));
            if(!propsTables.containsKey(propsTable))
            {
                propsTables.put(propsTable, new PropsTableInfo(propsTable));
            }
            ((PropsTableInfo)propsTables.get(propsTable)).hostsDeployment(deployment);
        }
        for(PropsTableInfo propsTableInfo : propsTables.values())
        {
            String propsTableName = propsTableInfo.getName();
            String propsSelector = propsTableInfo.getPropsSelector();
            boolean isColumnBased = propsTableInfo.isColumnBased();
            result.add(
                            CopyTableOperation.withData(propsTableName, srcToDstTableName.get(propsTableName), isColumnBased, propsSelector, new Object[0])
                                            .withPK(new String[] {"ITEMPK", "NAME", "LANGPK"}));
        }
        return (List<CopyTableOperation>)result.build();
    }


    private Map<String, String> findSrcToDstTablesMapping(TypeSystemRelatedDeployments deployments)
    {
        ImmutableSet immutableSet = ImmutableSet.builder().addAll(deployments.getAllTables()).build();
        Map<String, String> tableNameToNormalizedTableMapping = mapTableNamesToNormalizedTableNames((Set<String>)immutableSet);
        for(int s = 0; s < 65535; s++)
        {
            String suffix = Integer.toHexString(s);
            Map<String, String> candidate = getSuffixedTables(tableNameToNormalizedTableMapping, suffix);
            if(canCreateAllTables(candidate.values()))
            {
                return candidate;
            }
        }
        throw new IllegalStateException("Can't find common suffix.");
    }


    private Map<String, String> mapTableNamesToNormalizedTableNames(Set<String> tables)
    {
        String composedTypes = "composedtypes";
        String composedTypesTableName = getRequiredTableName("composedtypes", tables);
        String suffix = composedTypesTableName.substring(composedTypesTableName.lastIndexOf("composedtypes") + "composedtypes".length());
        ImmutableMap.Builder<String, String> resultBuilder = ImmutableMap.builder();
        for(String tableName : tables)
        {
            if(!tableName.endsWith(suffix))
            {
                throw new RuntimeException("table '" + tableName + "' doesn't have required suffix + '" + suffix + "'");
            }
            String normalizedTableName = tableName.substring(0, tableName.length() - suffix.length());
            resultBuilder.put(tableName, normalizedTableName);
        }
        return (Map<String, String>)resultBuilder.build();
    }


    private String getRequiredTableName(String normalizedTableName, Set<String> tables)
    {
        for(String table : tables)
        {
            if(table.toLowerCase(LocaleHelper.getPersistenceLocale()).contains(normalizedTableName.toLowerCase(LocaleHelper.getPersistenceLocale())))
            {
                return table;
            }
        }
        throw new RuntimeException("Can't find composedtypes table among " + tables);
    }


    private boolean canCreateAllTables(Collection<String> tables)
    {
        for(String table : tables)
        {
            if(this.persistenceInfo.containsTable(table))
            {
                return false;
            }
        }
        return true;
    }


    private Map<String, String> getSuffixedTables(Map<String, String> tableNameToNormalizedTableMapping, String suffix)
    {
        ImmutableMap.Builder result = ImmutableMap.builder();
        for(Map.Entry<String, String> tableMapping : tableNameToNormalizedTableMapping.entrySet())
        {
            String tableName = tableMapping.getKey();
            String normalizedTableName = tableMapping.getValue();
            String tableToCreate = normalizedTableName + normalizedTableName;
            result.put(tableName, tableToCreate);
            String lpTable = tableName + "lp";
            if(this.persistenceInfo.containsTable(lpTable))
            {
                result.put(lpTable, tableToCreate + "lp");
            }
        }
        return (Map<String, String>)result.build();
    }


    private void requireTypeSystemOtherThanDefault(String typeSystemName)
    {
        Objects.requireNonNull(typeSystemName, "Type system name must be given.");
        Preconditions.checkState(!this.persistenceInfo.isDefaultTypeSystem(typeSystemName), "Cannot drop " + "DEFAULT" + " type system.");
    }


    private void requireExistenceOfTypeSystem(String sourceTypeSystemName)
    {
        Objects.requireNonNull(sourceTypeSystemName, "Source type system must be given.");
        Preconditions.checkState(this.persistenceInfo.isKnownTypeSystem(sourceTypeSystemName), "Source type system '" + sourceTypeSystemName + "' doesn't exist.");
    }


    private void requireNotExistenceOfTypeSystem(String destinationTypeSystemName)
    {
        Objects.requireNonNull(destinationTypeSystemName, "Destination type system must be given.");
        Preconditions.checkState(!this.persistenceInfo.isKnownTypeSystem(destinationTypeSystemName), "Destination type system '" + destinationTypeSystemName + "' already exists.");
    }


    private void requireInitializedDatabase()
    {
        Preconditions.checkState(this.persistenceInfo.containsTable(this.persistenceInfo.getDeploymentsTableName()), "System is not initialized.");
    }


    private List<SqlStatement> attachNewTypeSystemTablesToDeployments(String sourceTypeSystemName, String destinationTypeSystemName, Map<String, String> srcToDstTableName)
    {
        List<DeploymentRow> newDeploymentRows = createNewDeploymentRows(sourceTypeSystemName, destinationTypeSystemName, srcToDstTableName);
        ImmutableList.Builder<SqlStatement> statements = ImmutableList.builder();
        String queryTemplate = "insert into " + this.persistenceInfo.getDeploymentsTableName() + "(%s) values (%s)";
        for(DeploymentRow row : newDeploymentRows)
        {
            ImmutableList immutableList = ImmutableList.copyOf(row.getColumns());
            List<Object> orderedValues = Lists.newArrayList(FluentIterable.from((Iterable)immutableList).transform((Function)new Object(this, row))
                            .toArray(Object.class));
            String columnsPart = Joiner.on(",").join((Iterable)immutableList);
            String valuesPart = "?" + Strings.repeat(",?", orderedValues.size() - 1);
            statements.add(new DMLStatement(String.format(queryTemplate, new Object[] {columnsPart, valuesPart}), orderedValues));
        }
        return (List<SqlStatement>)statements.build();
    }


    private List<DeploymentRow> createNewDeploymentRows(String sourceTypeSystemName, String destinationTypeSystemName, Map<String, String> srcToDstTableName)
    {
        Iterable<DeploymentRow> srcDeployments = this.persistenceInfo.getAllDeploymentRowsForTypeSystem(sourceTypeSystemName);
        ImmutableList.Builder<DeploymentRow> deploymentRows = ImmutableList.builder();
        for(DeploymentRow srcDeployment : srcDeployments)
        {
            DeploymentRow dstDeployment = srcDeployment.withChangedTypeSystem(destinationTypeSystemName);
            if(dstDeployment.isTypeSystemRelated())
            {
                String dstTableName = getDstDeploymentTableName(dstDeployment.getTableName(), srcToDstTableName);
                String dstPropsTableName = getDstDeploymentTableName(dstDeployment.getPropsTableName(), srcToDstTableName);
                dstDeployment = dstDeployment.withChangedTableName(dstTableName).withChangedPropsTableName(dstPropsTableName);
            }
            deploymentRows.add(dstDeployment);
        }
        return (List<DeploymentRow>)deploymentRows.build();
    }


    private String getDstDeploymentTableName(String srcTableName, Map<String, String> srcToDstTableName)
    {
        String realSrcTableName = this.persistenceInfo.toRealTableName(srcTableName);
        return srcToDstTableName.containsKey(realSrcTableName) ?
                        this.persistenceInfo.toGeneralTableName(srcToDstTableName.get(realSrcTableName)) : srcTableName;
    }
}
