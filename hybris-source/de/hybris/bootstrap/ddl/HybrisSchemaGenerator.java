package de.hybris.bootstrap.ddl;

import com.google.common.base.Stopwatch;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.adjusters.CompleteSizeForIndicesFromDBModel;
import de.hybris.bootstrap.ddl.adjusters.CompleteSizeForIndicesFromDatabase;
import de.hybris.bootstrap.ddl.adjusters.RemoveConfiguredIndicesFromDBModel;
import de.hybris.bootstrap.ddl.adjusters.RemoveFullTextHanaIndicesFromDatabase;
import de.hybris.bootstrap.ddl.adjusters.RemoveIndicesFromDatabase;
import de.hybris.bootstrap.ddl.adjusters.RemoveIndicesFromDbModel;
import de.hybris.bootstrap.ddl.adjusters.RemoveIndicesParams;
import de.hybris.bootstrap.ddl.adjusters.RemoveIndicesWithSpecificPrefixesFromDatabase;
import de.hybris.bootstrap.ddl.adjusters.RemoveTablesNotMentionedInDbModelFromDatabase;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.dbtypesystem.NumberSeries;
import de.hybris.bootstrap.ddl.dbtypesystem.impl.DbTypeSystemException;
import de.hybris.bootstrap.ddl.dbtypesystem.impl.DbTypeSystemFactory;
import de.hybris.bootstrap.ddl.dbtypesystem.impl.DbTypeSystemProvider;
import de.hybris.bootstrap.ddl.model.YDbModel;
import de.hybris.bootstrap.ddl.model.YDbTableProvider;
import de.hybris.bootstrap.ddl.model.YRecord;
import de.hybris.bootstrap.ddl.model.YTable;
import de.hybris.bootstrap.ddl.pk.PkFactory;
import de.hybris.bootstrap.ddl.pk.impl.DbTypeSystemDecoratorForPkFactory;
import de.hybris.bootstrap.ddl.pk.impl.DefaultPkFactory;
import de.hybris.bootstrap.ddl.tools.DeepCloner;
import de.hybris.bootstrap.typesystem.OverridenItemsXml;
import de.hybris.bootstrap.typesystem.PatchedYTypeSystemLoader;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.typesystem.YTypeSystemHandler;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;
import de.hybris.bootstrap.typesystem.YTypeSystemSource;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class HybrisSchemaGenerator
{
    private static final Logger LOG = Logger.getLogger(HybrisSchemaGenerator.class);
    public static final String CLUSTER_ID_CONFIG_KEY = "cluster.id";
    public static final String DEFAULT_CLUSTER_ID = "0";
    private static final String PROPERTY_MAXLENGTH = "property.maxlength";
    private static final String PROPERTY_MAX_LENGTH_DEFAULT = "3999";
    private static final String IGNORE_INDICES = "bootstrap.init.type.system.ignore.indices";
    private static final String IGNORE_UNKNOWN_TABLES = "bootstrap.init.type.system.ignore.unknown.tables";
    private static final String IGNORE_INDEX_NAMES_STARTING_WITH = "bootstrap.init.type.system.custom.index.ignore.names.starting.with";
    private static final String INDICES_USE_ITEMS_DEFINITIONS = "bootstrap.init.type.system.custom.indices.use.items.definitions";
    private static final String MODEL_INDEX_IGNORE_NAMES = "bootstrap\\.init\\.type\\.system\\.model\\.index\\.ignore\\.names\\.(.*)";
    private static final String MODEL_INDEX_IGNORE_REGEX = "bootstrap\\.init\\.type\\.system\\.model\\.index\\.ignore\\.regex\\.(.*)";
    private static final String MODEL_IGNORED_INDICES_DROP_FROM_DB = "bootstrap.init.type.system.model.ignored.indices.drop.from.db";
    private static final WriterProvider defaultDdlDropWriterProvider = (WriterProvider)new Object();
    private static final WriterProvider defaultDmlWriterProvider = (WriterProvider)new Object();
    private static final WriterProvider defaultDdlWriterProvider = (WriterProvider)new Object();
    final Stopwatch timer = Stopwatch.createUnstarted();
    protected final PropertiesLoader properties;
    private final PlatformConfig platformConfig;
    private final DbTypeSystemProvider dbTypeSystemProvider;
    private final OverridenItemsXml overridenItemsXml;
    private final DataSourceCreator dataSourceCreator;
    private final CodeGenerator codeGenerator;
    private final YTypeSystemHandler yTypeSystemHandler;
    private final DatabaseSettings databaseSettings;
    private final HybrisDbScriptsExecutor dbScriptsExecutor;
    private final DeepCloner<Database> deepCloner;
    private final String tenantId;
    private String ddlFileName;
    private String dmlFileName;
    private String ddlDropFileName;
    private String changesFileName;
    private DMLRecordFactory dmlRecordFactory;
    private DMLGenerator dmlGenerator;
    private WriterProvider ddlDropWriterProvider = defaultDdlDropWriterProvider;
    private WriterProvider ddlWriterProvider = defaultDdlWriterProvider;
    private WriterProvider dmlWriterProvider = defaultDmlWriterProvider;
    private boolean changesDetected = false;


    public HybrisSchemaGenerator(PlatformConfig platformConfig, PropertiesLoader propertiesLoader, DataSourceCreator dataSourceCreator, boolean dryRun, String tenantId) throws Exception
    {
        this(platformConfig, propertiesLoader, dataSourceCreator, (DbTypeSystemProvider)new DbTypeSystemFactory(), OverridenItemsXml.empty(), dryRun, tenantId);
    }


    public HybrisSchemaGenerator(PlatformConfig platformConfig, PropertiesLoader propertiesLoader, DataSourceCreator dataSourceCreator, DbTypeSystemProvider dbTypeSystemProvider, OverridenItemsXml overridenItemsXml, boolean dryRun, String tenantId) throws Exception
    {
        this.platformConfig = platformConfig;
        this.dataSourceCreator = dataSourceCreator;
        this.dbTypeSystemProvider = dbTypeSystemProvider;
        this.overridenItemsXml = overridenItemsXml;
        this.yTypeSystemHandler = (YTypeSystemHandler)new YTypeSystemLoader(true);
        this.deepCloner = new DeepCloner();
        this.properties = propertiesLoader;
        this.tenantId = tenantId;
        this.databaseSettings = getHybrisDatabaseSettingsFactory(propertiesLoader).createDatabaseSettings();
        this.dbScriptsExecutor = HybrisDbScriptsExecutorFactory.getExecutor(dataSourceCreator, this.databaseSettings, propertiesLoader, dryRun);
        this.codeGenerator = new CodeGenerator(platformConfig, this.yTypeSystemHandler);
        this.ddlFileName = "" + platformConfig.getSystemConfig().getTempDir() + platformConfig.getSystemConfig().getTempDir() + "hybrisDDL.sql";
        this.ddlDropFileName = "" + platformConfig.getSystemConfig().getTempDir() + platformConfig.getSystemConfig().getTempDir() + "hybrisDDL_drop_schema.sql";
        this.dmlFileName = "" + platformConfig.getSystemConfig().getTempDir() + platformConfig.getSystemConfig().getTempDir() + "hybrisDML.sql";
        this.changesFileName = "" + platformConfig.getSystemConfig().getTempDir() + platformConfig.getSystemConfig().getTempDir() + "hybrisChanges.json";
        if(LOG.isDebugEnabled())
        {
            LOG.debug("##########################");
            LOG.debug("Using database settings :: " + this.databaseSettings);
            LOG.debug("##########################");
        }
    }


    protected HybrisDatabaseSettingsFactory getHybrisDatabaseSettingsFactory(PropertiesLoader propertiesLoader)
    {
        return new HybrisDatabaseSettingsFactory(propertiesLoader);
    }


    protected DatabaseSettings getDatabaseSettings()
    {
        return this.databaseSettings;
    }


    public void setDdlDropWriterProvider(WriterProvider ddlDropWriterProvider)
    {
        this.ddlDropWriterProvider = (ddlDropWriterProvider == null) ? defaultDdlDropWriterProvider : ddlDropWriterProvider;
    }


    public void setDdlWriterProvider(WriterProvider ddlWriterProvider)
    {
        this.ddlWriterProvider = (ddlWriterProvider == null) ? defaultDdlWriterProvider : ddlWriterProvider;
    }


    public void setDmlWriterProvider(WriterProvider dmlWriterProvider)
    {
        this.dmlWriterProvider = (dmlWriterProvider == null) ? defaultDmlWriterProvider : dmlWriterProvider;
    }


    public void initialize()
    {
        Locale defaultLocale = Locale.getDefault();
        try
        {
            LOG.info("switching locale to ROOT");
            Locale.setDefault(Locale.ROOT);
            YTypeSystem typeSystem = loadTypeSystem();
            YDbModel yDbModel = buildDbModel(typeSystem, null);
            Platform platform = createDDLUtilsPlatform();
            SchemaAdjuster adjuster = createSchemaAdjusterForInitialization(yDbModel, platform);
            adjuster.adjust();
            Database database = yDbModel.createDatabase(this.databaseSettings.getTablePrefix());
            PkFactory pkFactory = getDefaultPkFactory(Collections.emptyList());
            createDMLGenerator(typeSystem, yDbModel, database, platform, pkFactory);
            generateInitStatements(yDbModel,
                            createDatabaseStatementGeneratorForInit(platform, database, yDbModel.getCreationParameters()));
            executeInitStatements();
        }
        finally
        {
            LOG.info("restoring " + defaultLocale + " locale");
            Locale.setDefault(defaultLocale);
        }
    }


    private void executeInitStatements()
    {
        this.dbScriptsExecutor.executeDropDdl(Paths.get(this.ddlDropFileName, new String[0]));
        this.dbScriptsExecutor.executeDDl(Paths.get(this.ddlFileName, new String[0]));
        this.dbScriptsExecutor.executeDml(Paths.get(this.dmlFileName, new String[0]));
        LOG.info("DDL DROP Script generated to: " + this.ddlDropFileName);
        LOG.info("DDL CREATE Script generated to: " + this.ddlFileName);
        LOG.info("DML Script generated to: " + this.dmlFileName);
    }


    public boolean check()
    {
        update();
        if(this.changesDetected)
        {
            LOG.info("changes file generated to: " + this.changesFileName);
        }
        return this.changesDetected;
    }


    public void update()
    {
        Locale defaultLocale = Locale.getDefault();
        try
        {
            LOG.info("switching locale to ROOT");
            Locale.setDefault(Locale.ROOT);
            DbTypeSystem dbTypeSystem = createDbTypeSystem();
            YTypeSystem typeSystem = loadTypeSystemForUpdate(dbTypeSystem);
            YDbModel yDbModel = buildDbModel(typeSystem, dbTypeSystem);
            Platform platform = createConnectedDDLUtilsPlatform();
            Stopwatch sw = Stopwatch.createUnstarted();
            LOG.info("Preparing target and source models");
            sw.start();
            Database sourceDDLDatabase = platform.readModelFromDatabase("source-hybris");
            LOG.info("\tSource model: " + sw.stop());
            SchemaAdjuster adjuster = createSchemaAdjusterForUpdate(yDbModel, sourceDDLDatabase, platform);
            adjuster.adjust();
            sw.reset().start();
            Database targetDDLDatabase = (Database)this.deepCloner.cloneDeeply(sourceDDLDatabase);
            LOG.info("\tTarget model: " + sw.stop());
            PkFactory defaultPkFactory = getDefaultPkFactory(dbTypeSystem.getNumberSeries());
            DbTypeSystemDecoratorForPkFactory dbTypeSystemDecoratorForPkFactory = new DbTypeSystemDecoratorForPkFactory(defaultPkFactory, dbTypeSystem);
            Database modelDatabase = yDbModel.createDatabase(this.databaseSettings.getTablePrefix());
            createDMLGenerator(typeSystem, yDbModel, modelDatabase, platform, (PkFactory)dbTypeSystemDecoratorForPkFactory);
            DbModelAdjuster dataBaseAdjuster = new DbModelAdjuster((YDbTableProvider)yDbModel);
            DMLStatementsGenerator dmlStatementsGenerator = new DMLStatementsGenerator(dbTypeSystem, modelDatabase, platform, this.databaseSettings.getTablePrefix(), getChangesFileName());
            DatabaseStatementGenerator statementGenerator = createDatabaseStatementGeneratorForUpdate(platform, sourceDDLDatabase, dataBaseAdjuster
                            .adjust(targetDDLDatabase), dmlStatementsGenerator, yDbModel
                            .getCreationParameters());
            generateUpdateStatements(yDbModel, statementGenerator);
            executeUpdateStatements();
            this.changesDetected = dmlStatementsGenerator.getChangesDetected();
        }
        finally
        {
            LOG.info("restoring " + defaultLocale + " locale");
            Locale.setDefault(defaultLocale);
        }
    }


    private void executeUpdateStatements()
    {
        this.dbScriptsExecutor.executeDDl(Paths.get(this.ddlFileName, new String[0]));
        this.dbScriptsExecutor.executeDml(Paths.get(this.dmlFileName, new String[0]));
        LOG.info("DDL CREATE Script generated to: " + this.ddlFileName);
        LOG.info("DML Script generated to: " + this.dmlFileName);
    }


    private SchemaAdjuster createSchemaAdjusterForInitialization(YDbModel dbModel, Platform platform)
    {
        SchemaAdjuster result = SchemaAdjuster.NONE;
        if(shouldIgnoreIndices())
        {
            result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)new RemoveIndicesFromDbModel(dbModel)});
        }
        Set<String> regexForIgnoredIndices = getConfiguredRegexForIgnoredIndicesForDBModel();
        Set<String> namesForIgnoredIndices = getConfiguredNamesForIgnoredIndicesForDBModel();
        if(!regexForIgnoredIndices.isEmpty() || !namesForIgnoredIndices.isEmpty())
        {
            RemoveIndicesParams paramsForRemoveConfiguredIndices = createParamsForRemoveConfiguredIndices(dbModel, null, platform);
            RemoveConfiguredIndicesFromDBModel specyficIndicesRemover = new RemoveConfiguredIndicesFromDBModel(paramsForRemoveConfiguredIndices);
            result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)specyficIndicesRemover});
        }
        return result;
    }


    private RemoveIndicesParams createParamsForRemoveConfiguredIndices(YDbModel dbModel, Database ddlDatabase, Platform platform)
    {
        String databasePrefix = this.databaseSettings.getTablePrefix();
        int constraintNameLen = platform.getPlatformInfo().getMaxConstraintNameLength();
        int tableNameLen = platform.getPlatformInfo().getMaxTableNameLength();
        return RemoveIndicesParams.builderForIgnoreIndicesInItems()
                        .withDatabase(ddlDatabase)
                        .withDbModel(dbModel)
                        .withIndicesNamesToIgnoreInDBModel(getConfiguredNamesForIgnoredIndicesForDBModel())
                        .withIndicesRegexToIgnoreInDBModel(getConfiguredRegexForIgnoredIndicesForDBModel())
                        .withConstraintNameLength(Integer.valueOf(constraintNameLen))
                        .withTablePrefix(databasePrefix)
                        .withTableNameLength(Integer.valueOf(tableNameLen))
                        .withDropIndicesFoundInItems(Boolean.valueOf(shouldDropIndicesFromDBFoundInItemsXml()))
                        .build();
    }


    private SchemaAdjuster createSchemaAdjusterForUpdate(YDbModel dbModel, Database ddlDatabase, Platform platform)
    {
        SchemaAdjuster result = SchemaAdjuster.NONE;
        if(shouldIgnoreUnknowTables())
        {
            result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)new RemoveTablesNotMentionedInDbModelFromDatabase(dbModel, ddlDatabase, this.databaseSettings
                            .getTablePrefix())});
        }
        if(shouldIgnoreIndices())
        {
            result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)new RemoveIndicesFromDbModel(dbModel), (SchemaAdjuster)new RemoveIndicesFromDatabase(ddlDatabase)});
        }
        if(shouldIgnoreIndicesFromDBWithSpecificPrefixes())
        {
            String databasePrefix = this.databaseSettings.getTablePrefix();
            int constraintNameLen = platform.getPlatformInfo().getMaxConstraintNameLength();
            int tableNameLen = platform.getPlatformInfo().getMaxTableNameLength();
            RemoveIndicesParams params = RemoveIndicesParams.builder().withDatabase(ddlDatabase).withDbModel(dbModel).withIgnoredIndicesStr(getPrefixesForIgnoredIndices()).withRecreateIndicesFoundInItems(Boolean.valueOf(shouldRecreateIndicesFoundInItemsXml()))
                            .withConstraintNameLength(Integer.valueOf(constraintNameLen)).withTablePrefix(databasePrefix).withTableNameLength(Integer.valueOf(tableNameLen)).build();
            RemoveIndicesWithSpecificPrefixesFromDatabase indicesWithPrefixRemover = new RemoveIndicesWithSpecificPrefixesFromDatabase(params);
            result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)indicesWithPrefixRemover});
        }
        Set<String> ignoredRegexIndices = getConfiguredRegexForIgnoredIndicesForDBModel();
        Set<String> ignoredNamesIndices = getConfiguredNamesForIgnoredIndicesForDBModel();
        if(!ignoredRegexIndices.isEmpty() || !ignoredNamesIndices.isEmpty())
        {
            RemoveIndicesParams paramsForRemoveConfiguredIndices = createParamsForRemoveConfiguredIndices(dbModel, ddlDatabase, platform);
            RemoveConfiguredIndicesFromDBModel specyficIndicesRemover = new RemoveConfiguredIndicesFromDBModel(paramsForRemoveConfiguredIndices);
            result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)specyficIndicesRemover});
        }
        if(this.databaseSettings.getDataBaseProvider().isHanaUsed())
        {
            result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)new RemoveFullTextHanaIndicesFromDatabase(new JdbcTemplate(this.dataSourceCreator
                            .createDataSource(this.databaseSettings)), ddlDatabase)});
        }
        result = SchemaAdjuster.chained(new SchemaAdjuster[] {result, (SchemaAdjuster)new CompleteSizeForIndicesFromDBModel(dbModel), (SchemaAdjuster)new CompleteSizeForIndicesFromDatabase(ddlDatabase)});
        return result;
    }


    private boolean shouldIgnoreIndicesFromDBWithSpecificPrefixes()
    {
        String prefixesWithIgnoredIndices = getPrefixesForIgnoredIndices();
        return !StringUtils.isEmpty(prefixesWithIgnoredIndices);
    }


    private boolean shouldRecreateIndicesFoundInItemsXml()
    {
        return Boolean.parseBoolean(this.databaseSettings.getProperty("bootstrap.init.type.system.custom.indices.use.items.definitions", Boolean.TRUE.toString()));
    }


    private boolean shouldIgnoreIndices()
    {
        return Boolean.parseBoolean(this.databaseSettings.getProperty("bootstrap.init.type.system.ignore.indices", Boolean.FALSE.toString()));
    }


    private boolean shouldIgnoreUnknowTables()
    {
        return Boolean.parseBoolean(this.databaseSettings.getProperty("bootstrap.init.type.system.ignore.unknown.tables", Boolean.TRUE.toString()));
    }


    private String getPrefixesForIgnoredIndices()
    {
        return this.databaseSettings.getProperty("bootstrap.init.type.system.custom.index.ignore.names.starting.with", "");
    }


    private boolean shouldDropIndicesFromDBFoundInItemsXml()
    {
        return Boolean.parseBoolean(this.databaseSettings.getProperty("bootstrap.init.type.system.model.ignored.indices.drop.from.db", Boolean.TRUE.toString()));
    }


    private Set<String> getConfiguredNamesForIgnoredIndicesForDBModel()
    {
        return getIgnoredIndicesMatchingRegex("bootstrap\\.init\\.type\\.system\\.model\\.index\\.ignore\\.names\\.(.*)");
    }


    private Set<String> getConfiguredRegexForIgnoredIndicesForDBModel()
    {
        return getIgnoredIndicesMatchingRegex("bootstrap\\.init\\.type\\.system\\.model\\.index\\.ignore\\.regex\\.(.*)");
    }


    private Set<String> getIgnoredIndicesMatchingRegex(String key)
    {
        Set<String> regexIndicesNames = new HashSet<>();
        Map<String, String> allRegexIndicesNames = this.databaseSettings.getParametersMatching(key, true);
        regexIndicesNames.addAll(allRegexIndicesNames.values());
        return regexIndicesNames;
    }


    private PkFactory getDefaultPkFactory(Iterable<NumberSeries> currentNumberSerries)
    {
        int clusterId = Integer.valueOf(this.databaseSettings.getProperty("cluster.id", "0")).intValue();
        return (PkFactory)new DefaultPkFactory(currentNumberSerries, clusterId);
    }


    private DbTypeSystem createDbTypeSystem()
    {
        DbTypeSystem dbTypeSystem = null;
        try
        {
            DataSource dataSource = this.dataSourceCreator.createDataSource(this.databaseSettings);
            dbTypeSystem = this.dbTypeSystemProvider.createDbTypeSystem(dataSource, this.databaseSettings.getTablePrefix(), this.databaseSettings
                            .getTypeSystemName());
        }
        catch(DbTypeSystemException e)
        {
            LOG.error(e.getMessage());
            System.exit(1);
        }
        return dbTypeSystem;
    }


    private void generateInitStatements(YDbModel yDbModel, DatabaseStatementGenerator statementGenerator)
    {
        try
        {
            LOG.info("Preparing  Statements");
            this.timer.reset();
            this.timer.start();
            Collection<YRecord> yRecords = this.dmlGenerator.generateRecords();
            generateAllInitSqlStatements(statementGenerator, yRecords);
            this.timer.stop();
            LOG.info("Prepared  Statements. Time taken " + this.timer);
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            throw new RuntimeException(e);
        }
        if(LOG.isDebugEnabled())
        {
            logCreatedTables(yDbModel);
        }
    }


    private void generateUpdateStatements(YDbModel yDbModel, DatabaseStatementGenerator statementGenerator)
    {
        try
        {
            LOG.info("Preparing  Statements");
            this.timer.reset();
            this.timer.start();
            Collection<YRecord> yRecords = this.dmlGenerator.generateRecordsForUpdate();
            generateAllUpdateSqlStatements(statementGenerator, yRecords);
            this.timer.stop();
            LOG.info("Prepared  Statements. Time taken " + this.timer);
        }
        catch(Exception e)
        {
            shutdownGracefully(e);
        }
        if(LOG.isDebugEnabled())
        {
            logCreatedTables(yDbModel);
        }
    }


    void createDMLGenerator(YTypeSystem typeSystem, YDbModel yDbModel, Database database, Platform platform, PkFactory pkFactory)
    {
        this.dmlRecordFactory = createRecordFactory(typeSystem, yDbModel, database, (HybrisPlatform)platform, this.codeGenerator, pkFactory);
        this.dmlGenerator = new DMLGenerator(typeSystem, this.dmlRecordFactory);
    }


    DatabaseStatementGenerator createDatabaseStatementGeneratorForUpdate(Platform platform, Database sourceDatabase, Database targetDatabase, DMLStatementsGenerator dmlStatementsGenerator, CreationParameters tableCreationParameters)
    {
        DatabaseStatementGeneratorBuilder statementGeneratorBuilder = DatabaseStatementGeneratorBuilder.builder(platform);
        statementGeneratorBuilder.withExistingDataBase(sourceDatabase);
        statementGeneratorBuilder.withTargetDataBase(targetDatabase);
        statementGeneratorBuilder.withTableCreationParameters(tableCreationParameters);
        statementGeneratorBuilder.withDMLStatementsGenerator(dmlStatementsGenerator);
        return statementGeneratorBuilder.build();
    }


    DatabaseStatementGenerator createDatabaseStatementGeneratorForInit(Platform platform, Database targetDatabase, CreationParameters tableCreationParameters)
    {
        DatabaseStatementGeneratorBuilder statementGeneratorBuilder = DatabaseStatementGeneratorBuilder.builder(platform);
        statementGeneratorBuilder.withTargetDataBase(targetDatabase);
        statementGeneratorBuilder.withTableCreationParameters(tableCreationParameters);
        statementGeneratorBuilder.withDropTableStatement();
        statementGeneratorBuilder.withTableCreationParameters(tableCreationParameters);
        return statementGeneratorBuilder.build();
    }


    protected DMLRecordFactory getDMLFactory()
    {
        return this.dmlRecordFactory;
    }


    protected DMLRecordFactory createRecordFactory(YTypeSystem typeSystem, YDbModel yDbModel, Database database, HybrisPlatform platform, CodeGenerator codeGenerator, PkFactory pkFactory)
    {
        int maxPropertyLength = Integer.valueOf(this.databaseSettings.getProperty("property.maxlength", "3999")).intValue();
        String typeSystemName = this.databaseSettings.getTypeSystemName();
        return new DMLRecordFactory(yDbModel, database, typeSystem, platform, codeGenerator, pkFactory, maxPropertyLength, typeSystemName, this.databaseSettings
                        .getTablePrefix(), this.properties);
    }


    protected Platform createDDLUtilsPlatform()
    {
        return HybrisPlatformFactory.createInstance(this.databaseSettings);
    }


    protected Platform createConnectedDDLUtilsPlatform()
    {
        return HybrisPlatformFactory.createInstance(this.databaseSettings, this.dataSourceCreator.createDataSource(this.databaseSettings));
    }


    protected YTypeSystem loadTypeSystem()
    {
        LOG.info("Reading type system Information.");
        this.timer.reset();
        this.timer.start();
        YTypeSystem typeSystem = (new YTypeSystemSource(this.platformConfig, this.yTypeSystemHandler, this.overridenItemsXml)).getTypeSystem();
        this.timer.stop();
        LOG.info("Read type system Information. Time taken " + this.timer);
        return typeSystem;
    }


    protected YTypeSystem loadTypeSystemForUpdate(DbTypeSystem dbTypeSystem)
    {
        LOG.info("Reading type system Information.");
        this.timer.reset();
        this.timer.start();
        PatchedYTypeSystemLoader patchedYTypeSystemLoader = new PatchedYTypeSystemLoader(this.yTypeSystemHandler, dbTypeSystem);
        YTypeSystem mergedTypeSystem = (new YTypeSystemSource(this.platformConfig, (YTypeSystemHandler)patchedYTypeSystemLoader, this.overridenItemsXml)).getTypeSystem();
        this.timer.stop();
        LOG.info("Read type system Information. Time taken " + this.timer);
        return mergedTypeSystem;
    }


    protected YDbModel buildDbModel(YTypeSystem typeSystem, DbTypeSystem dbTypeSystem)
    {
        LOG.info("Preparing  Db model. ");
        this.timer.reset();
        this.timer.start();
        YDbModel yDbModel = (new DatabaseModelGenerator(typeSystem, this.databaseSettings, dbTypeSystem, this.platformConfig, this.tenantId, this.properties)).createDatabaseModel();
        this.timer.stop();
        LOG.info("Prepared  Db model. Time taken " + this.timer);
        return yDbModel;
    }


    private void generateAllInitSqlStatements(DatabaseStatementGenerator statementGenerator, Collection<YRecord> records)
    {
        generateDropTableStatements(statementGenerator);
        generateDDLStatements(statementGenerator);
        generateDMLStatements(statementGenerator, records);
    }


    private void generateAllUpdateSqlStatements(DatabaseStatementGenerator statementGenerator, Collection<YRecord> records)
    {
        generateDDLStatements(statementGenerator);
        generateDMLStatements(statementGenerator, records);
    }


    private void generateDropTableStatements(DatabaseStatementGenerator statementGenerator)
    {
        Writer ddlDropWriter = null;
        try
        {
            ddlDropWriter = getWriterForDdlDropStatements();
            statementGenerator.generateDropDDL(ddlDropWriter);
            DataSource dataSource = this.dataSourceCreator.createDataSource(this.databaseSettings);
            if(dataSource != null)
            {
                String tablePrefix = (this.databaseSettings.getTablePrefix() == null) ? "" : this.databaseSettings.getTablePrefix();
                List<String> duplicatedTablesList = statementGenerator.retrieveAllSystemDeploymentsTablesForAllTypeSystems(new JdbcTemplate(dataSource), tablePrefix);
                statementGenerator.generateDropStatementsForCustomTypeSystemTables(ddlDropWriter, duplicatedTablesList);
            }
        }
        catch(DataAccessException dataAccessException)
        {
        }
        catch(Exception e)
        {
            IOUtils.closeQuietly(ddlDropWriter);
            shutdownGracefully(e);
        }
        finally
        {
            IOUtils.closeQuietly(ddlDropWriter);
        }
    }


    private void generateDDLStatements(DatabaseStatementGenerator statementGenerator)
    {
        Writer ddlWriter = null;
        try
        {
            ddlWriter = getWriterForDdlStatements();
            statementGenerator.generateDDL(ddlWriter);
        }
        catch(Exception e)
        {
            IOUtils.closeQuietly(ddlWriter);
            shutdownGracefully(e);
        }
        finally
        {
            IOUtils.closeQuietly(ddlWriter);
        }
    }


    private void generateDMLStatements(DatabaseStatementGenerator statementGenerator, Collection<YRecord> records)
    {
        Writer dmlWriter = null;
        try
        {
            dmlWriter = getWriterForDmlStatements();
            statementGenerator.createInserts(dmlWriter, records);
        }
        catch(Exception e)
        {
            IOUtils.closeQuietly(dmlWriter);
            shutdownGracefully(e);
        }
        finally
        {
            IOUtils.closeQuietly(dmlWriter);
        }
    }


    private void shutdownGracefully(Exception e)
    {
        LOG.error(e.getMessage(), e);
        LOG.error("An error occurred during update script generation. The stack trace can be found above.");
        System.exit(1);
    }


    private Writer getWriterForDdlDropStatements() throws Exception
    {
        return this.ddlDropWriterProvider.getWriterFor(this);
    }


    private Writer getWriterForDdlStatements() throws Exception
    {
        return this.ddlWriterProvider.getWriterFor(this);
    }


    private Writer getWriterForDmlStatements() throws Exception
    {
        return this.dmlWriterProvider.getWriterFor(this);
    }


    public String getDdlFileName()
    {
        return this.ddlFileName;
    }


    public void setDdlFileName(String ddlFileName)
    {
        this.ddlFileName = ddlFileName;
    }


    public String getDmlFileName()
    {
        return this.dmlFileName;
    }


    public void setDmlFileName(String dmlFileName)
    {
        this.dmlFileName = dmlFileName;
    }


    public String getChangesFileName()
    {
        return this.changesFileName;
    }


    public void setChangesFileName(String changesFileName)
    {
        this.changesFileName = changesFileName;
    }


    public String getDdlDropFileName()
    {
        return this.ddlDropFileName;
    }


    public void setDdlDropFileName(String ddlDropFileName)
    {
        this.ddlDropFileName = ddlDropFileName;
    }


    private void logCreatedTables(YDbModel yDbModel)
    {
        for(Map.Entry<String, YTable> entry : (Iterable<Map.Entry<String, YTable>>)yDbModel.getTables().entrySet())
        {
            YTable table = entry.getValue();
            LOG.debug("Table: " + table.getName());
            for(Map.Entry<String, String> relation : (Iterable<Map.Entry<String, String>>)table.getColumnDescriptor().entrySet())
            {
                String key = relation.getKey();
                String value = relation.getValue();
                LOG.debug("key: " + key + " value: " + value);
            }
        }
    }
}
