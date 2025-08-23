package de.hybris.bootstrap.ddl;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.jdbc.JdbcType;
import de.hybris.bootstrap.ddl.jdbc.PlatformJDBCMappingProvider;
import de.hybris.bootstrap.ddl.model.YColumn;
import de.hybris.bootstrap.ddl.model.YDbModel;
import de.hybris.bootstrap.ddl.model.YTable;
import de.hybris.bootstrap.ddl.sql.DBAwareNonUniqueIndexExtended;
import de.hybris.bootstrap.ddl.sql.DbAwareNonUniqueIndex;
import de.hybris.bootstrap.ddl.sql.DbAwareUniqueIndex;
import de.hybris.bootstrap.ddl.sql.DbAwareUniqueIndexExtended;
import de.hybris.bootstrap.ddl.sql.DefaultFunctionalIndexColumn;
import de.hybris.bootstrap.ddl.sql.ExtendedAwareIndex;
import de.hybris.bootstrap.ddl.sql.ExtendedParamsForIndex;
import de.hybris.bootstrap.ddl.sql.IndexCreationMode;
import de.hybris.bootstrap.ddl.sql.MSSqlExtendedParamsForIndex;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YIndex;
import de.hybris.bootstrap.typesystem.YIndexDeployment;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YRelation;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.YType;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.util.LocaleHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.Table;
import org.apache.log4j.Logger;

public class DatabaseModelGenerator
{
    private static final Logger LOG = Logger.getLogger(DatabaseModelGenerator.class);
    private static final String LINK_TYPE_CODE = "Link";
    private static final String DB_ONLINEINDICES = "db.onlineindices";
    private static final String TABLENAME_MAXLENGTH = "deployment.tablename.maxlength";
    private static final String TABLENAME_MAX_LENGTH_DEFAULT = "24";
    private static final String USE_LOWER_INDEXES_FOR_DB_PREFIX = ".use.lower.indexes";
    private static final String DETECT_INCONSISTENCY_FOR_REUSED_COLUMN = "detect.inconsistency.for.reused.column";
    private final DatabaseSettings databaseSettings;
    private final YTypeSystem typeSystem;
    private final DbTypeSystem dbTypeSystem;
    private final PlatformJDBCMappingProvider databaseJdbcMappingProvider;
    private final PlatformConfig platformConfig;
    private final PropertiesLoader propertiesLoader;
    private final String tenantId;
    private final Supplier<Boolean> detectCorrectnessForReusedColumnsFlag = (Supplier<Boolean>)Suppliers.memoize(this::isDetectConsistencyForReusedColumnEnabled);
    private YDbModel yDbModel = null;
    private Properties loadedPlatformProps;
    private Table<String, String, String> ovveriddenDatabaseColumnConfiguration;
    private Table<String, String, String> addIncludeConfiguration;


    public DatabaseModelGenerator(YTypeSystem typeSystem, DatabaseSettings databaseSettings, DbTypeSystem dbTypeSystem, PlatformConfig platformConfig, String tenantId, PropertiesLoader properties)
    {
        this.databaseSettings = databaseSettings;
        this.typeSystem = typeSystem;
        this.dbTypeSystem = dbTypeSystem;
        this.databaseJdbcMappingProvider = databaseSettings.getDataBaseProvider().getJdbcProvider();
        this.platformConfig = platformConfig;
        this.tenantId = tenantId;
        this.propertiesLoader = properties;
    }


    public DatabaseModelGenerator(YTypeSystem typeSystem, DatabaseSettings databaseSettings, DbTypeSystem dbTypeSystem, PlatformConfig platformConfig, String tenantId)
    {
        this(typeSystem, databaseSettings, dbTypeSystem, platformConfig, tenantId, null);
    }


    public YDbModel createDatabaseModel()
    {
        if(this.yDbModel != null)
        {
            throw new IllegalStateException();
        }
        this.yDbModel = new YDbModel(this.databaseSettings, this.dbTypeSystem);
        this.ovveriddenDatabaseColumnConfiguration = loadAdditionalProperties("persistence\\.override\\.(.*)\\.(.*)\\.columntype", "persistence.override");
        this.addIncludeConfiguration = loadAdditionalProperties("extend\\.index\\.for\\.(.*)\\.(.*)\\.with.include", "extend.index.for");
        prepareDatabase();
        filterEmptyLocalizedTables();
        removeDuplicateIndexes();
        return this.yDbModel;
    }


    private Table<String, String, String> loadAdditionalProperties(String propertyRegex, String propertyStartWith)
    {
        Properties platformProperties = getPlatformProperties();
        HashBasedTable hashBasedTable = HashBasedTable.create();
        Pattern pattern = Pattern.compile(propertyRegex, 2);
        String persistenceStartWith = propertyStartWith.toUpperCase(LocaleHelper.getPersistenceLocale());
        for(String name : platformProperties.stringPropertyNames())
        {
            if(name.toUpperCase(LocaleHelper.getPersistenceLocale()).startsWith(persistenceStartWith))
            {
                Matcher matcher = pattern.matcher(name);
                if(matcher.matches())
                {
                    String enclosingType = matcher.group(1);
                    String secondGroup = matcher.group(2);
                    hashBasedTable.put(enclosingType.toUpperCase(LocaleHelper.getPersistenceLocale()), secondGroup
                                    .toUpperCase(LocaleHelper.getPersistenceLocale()), platformProperties
                                    .getProperty(name));
                }
            }
        }
        return (Table<String, String, String>)hashBasedTable;
    }


    private Properties getPlatformProperties()
    {
        if(this.propertiesLoader != null && Objects.isNull(this.loadedPlatformProps))
        {
            this.loadedPlatformProps = new Properties();
            this.loadedPlatformProps.putAll(this.propertiesLoader.getAllProperties());
        }
        if(Objects.isNull(this.loadedPlatformProps))
        {
            this.loadedPlatformProps = new Properties();
            ConfigUtil.loadRuntimeProperties(this.loadedPlatformProps, this.platformConfig);
        }
        return this.loadedPlatformProps;
    }


    private void removeDuplicateIndexes()
    {
        Map<String, YTable> tables = this.yDbModel.getTables();
        for(Map.Entry<String, YTable> tableEntry : tables.entrySet())
        {
            YTable table = tableEntry.getValue();
            for(Map.Entry<IndexKey, List<Index>> indexGroup : groupIndexes(table.getIndices()).entrySet())
            {
                List<Index> equalIndexes = indexGroup.getValue();
                if(equalIndexes.size() > 1)
                {
                    IndexKey key = indexGroup.getKey();
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Found multiple indexes " +
                                        indexesToString(equalIndexes) + " with columns " + key + " in table " + (String)tableEntry
                                        .getKey() + ". Ignoring duplicates!");
                    }
                    for(Index obsolete : equalIndexes.subList(1, equalIndexes.size()))
                    {
                        table.removeIndex(obsolete);
                    }
                }
            }
        }
    }


    private String indexesToString(Collection<Index> indexes)
    {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        sb.append('(');
        for(Index idx : indexes)
        {
            if(i > 0)
            {
                sb.append(',');
            }
            sb.append(idx.getName());
            i++;
        }
        sb.append(')');
        return sb.toString();
    }


    private Map<IndexKey, List<Index>> groupIndexes(Index[] indexes)
    {
        Map<IndexKey, List<Index>> ret = new HashMap<>();
        for(Index idx : indexes)
        {
            IndexKey key = new IndexKey(idx);
            List<Index> same = ret.get(key);
            if(same == null)
            {
                ret.put(key, same = new ArrayList<>());
            }
            same.add(idx);
        }
        return ret;
    }


    private void prepareDatabase()
    {
        YComposedType rootType = (YComposedType)this.typeSystem.getType("Item");
        extractDeploymentForType(rootType);
        createSystemTables();
        createRelations();
        LOG.info(String.format("Prepared Database Model with (%s) tables.", new Object[] {Integer.valueOf(this.yDbModel.getTables().size())}));
    }


    private void filterEmptyLocalizedTables()
    {
        List<YTable> localizedTables = this.yDbModel.getLocalizedTables();
        for(YTable yTable : localizedTables)
        {
            if(yTable.getColumnCount() < 1)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(
                                    String.format("Removing the localized table (%s) since it does not contain any columns.", new Object[] {yTable.getName()}));
                }
                this.yDbModel.removeLocalizedTable(yTable.getName());
                continue;
            }
            SystemTableGenerator.addInternalColumnsToLPTable(yTable, this.databaseJdbcMappingProvider);
            String dbName = this.databaseSettings.getDataBaseProvider().getDbName();
            SystemTableGenerator.addIndexesToLPTable(yTable, dbName, this.platformConfig);
        }
    }


    private void createRelations()
    {
        for(YRelation yRelation : this.typeSystem.getRelationTypes())
        {
            addRelation(yRelation);
        }
    }


    private void extractDeploymentForType(YComposedType type)
    {
        if(isPersistentType(type))
        {
            YDeployment deployment = type.getDeployment();
            if(mustBeMappedToTable(type, deployment))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Processing YComposedType  " + type.getCode() + " type code " + deployment.getItemTypeCode() + " deployment " + deployment
                                    .getTableName());
                }
                checkTableName(deployment, type.getCode());
                boolean createNewTable = isNewDeployment(type, deployment);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("#####Collecting columns for Table (%s)  #####", new Object[] {deployment.getTableName()}));
                }
                if(createNewTable)
                {
                    this.yDbModel.createTable(deployment.getTableName(), deployment.getItemTypeCode(), type.getCustomProps());
                }
                collectAttributesAndIndicesForTable(type, deployment, createNewTable);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("#####Collected columns for Table (%s) #####", new Object[] {deployment.getTableName()}));
                }
            }
            for(YComposedType subType : type.getSubtypes())
            {
                extractDeploymentForType(subType);
            }
        }
    }


    private boolean isPersistentType(YComposedType type)
    {
        return (!type.isJaloOnly() && !type.isViewType());
    }


    private boolean mustBeMappedToTable(YComposedType type, YDeployment deployment)
    {
        if(!type.isAbstract())
        {
            return true;
        }
        if(startsNewTable(type))
        {
            return true;
        }
        if(isNonAbstractDeployment(deployment))
        {
            return isHavingNonAbstractSubTypesInSameDeployment(deployment, type);
        }
        return false;
    }


    private boolean startsNewTable(YComposedType type)
    {
        return isNonAbstractDeployment(type.getOwnDeployment());
    }


    private boolean isNonAbstractDeployment(YDeployment depl)
    {
        return (depl != null && !depl.isAbstract());
    }


    private boolean isHavingNonAbstractSubTypesInSameDeployment(YDeployment deployment, YComposedType type)
    {
        for(YComposedType ct : type.getSubtypes())
        {
            if(!ct.isAbstract())
            {
                if(deployment.equals(ct.getDeployment()))
                {
                    return true;
                }
                continue;
            }
            if(deployment.equals(ct.getDeployment()))
            {
                if(isHavingNonAbstractSubTypesInSameDeployment(deployment, ct))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isNewDeployment(YComposedType type, YDeployment deployment)
    {
        if(!deployment.isAbstract())
        {
            YComposedType superType = type.getSuperType();
            if(superType != null)
            {
                YDeployment superTypeDeployment = superType.getDeployment();
                boolean isRoot = !deployment.equals(superTypeDeployment);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("New type is root:" + isRoot + " type: " + type.getCode());
                }
                return isRoot;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Type " + type.getCode() + " has no supertype.");
            }
            return true;
        }
        return false;
    }


    private void collectAttributesAndIndicesForTable(YComposedType typeMappedToTable, YDeployment deployment, boolean createNewTable)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Collecting properties of composed type " + typeMappedToTable.getCode() + " to table " + deployment
                            .getTableName());
        }
        if(createNewTable)
        {
            addColumn(deployment, "hjmpTS", "java.lang.Long", false, null);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("--Adding hjmpTS column manually to: " + deployment.getTableName());
            }
            Set<YAttributeDeployment> processedDeployments = new HashSet<>();
            for(YComposedType superType : typeMappedToTable.getAllSuperTypes())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("--Collecting properties of super composed type " + superType.getCode());
                }
                collectAttributes(typeMappedToTable, superType, deployment, processedDeployments);
                collectIndexes(superType, deployment);
            }
            collectAttributes(typeMappedToTable, typeMappedToTable, deployment, processedDeployments);
            collectIndexes(typeMappedToTable, deployment);
            addNonAttributeColumnsAndIndexes(deployment, processedDeployments);
            String propsTableName = deployment.getPropsTableName();
            if(propsTableName != null && this.yDbModel.getTable(propsTableName) == null)
            {
                createNonItemTable(this.typeSystem.getDeployment("Property." + propsTableName));
            }
            String auditTableName = deployment.getAuditTableName();
            if(deployment.isAuditingEnabled(this.platformConfig, this.tenantId) && auditTableName != null && this.yDbModel.getTable(auditTableName) == null)
            {
                YTable auditTable = createAuditTable(deployment);
                if(isManyToManyWithDeploymentRelation(typeMappedToTable) || isLinkDeployment(deployment))
                {
                    enhanceAuditTableForMany2ManyRelation(auditTable, deployment);
                }
            }
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Collecting properties of composed type " + typeMappedToTable.getCode());
            }
            collectAttributes(typeMappedToTable, typeMappedToTable, deployment, null);
            collectIndexes(typeMappedToTable, deployment);
        }
    }


    private boolean isLinkDeployment(YDeployment deployment)
    {
        return deployment.getName().equals("Link");
    }


    private boolean isManyToManyWithDeploymentRelation(YComposedType typeMappedToTable)
    {
        return (typeMappedToTable instanceof YRelation && !((YRelation)typeMappedToTable).isOneToMany());
    }


    private void enhanceAuditTableForMany2ManyRelation(YTable auditTable, YDeployment deployment)
    {
        addColumn(auditTable, "sourcePK", "HYBRIS.PK", false, null);
        addColumn(auditTable, "targetPK", "HYBRIS.PK", false, null);
        addColumn(auditTable, "languagePK", "HYBRIS.PK", false, null);
        createAuditIndex(auditTable, "sourcePK", "Sr", deployment);
        createAuditIndex(auditTable, "targetPK", "Tg", deployment);
        createAuditIndex(auditTable, "languagePK", "Ln", deployment);
    }


    private void createAuditIndex(YTable auditTable, String column, String indexName, YDeployment deployment)
    {
        Column sourcePK = auditTable.findColumn(column);
        String sourceIndexName = "Idx" + computeIndexName(column, auditTable, deployment) + "Sn";
        Index sourcePKIndex = createNonUniqueIndexOnColumns(sourceIndexName, new Column[] {sourcePK});
        addIndexToTableIfNotExist((Table)auditTable, sourcePKIndex);
    }


    private YTable addNonAttributeColumnsAndIndexes(YDeployment deployment, Set<YAttributeDeployment> processedDeployments)
    {
        for(YAttributeDeployment ad : deployment.getAttributeDeployments())
        {
            if(!processedDeployments.contains(ad))
            {
                YAttributeDeployment.ColumnMapping persistenceMapping = ad.getPersistenceMapping(this.databaseSettings
                                .getDataBaseProvider().getDbName());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Java type for deployment (%s) for column (%s) is (%s) and (%s)", new Object[] {deployment
                                    .getTableName(), persistenceMapping.getColumnName(), ad.getJavaTypeName(), ad
                                    .getJavaTypeName()}));
                }
                addColumn(deployment, persistenceMapping.getColumnName(), ad.getJavaTypeName(), ad.isPrimaryKey(), persistenceMapping
                                .getSqlDefinition());
            }
        }
        YTable table = this.yDbModel.getTable(deployment.getTableName());
        for(YIndexDeployment indexdeployment : deployment.getIndexDeployments())
        {
            createIndex(deployment, indexdeployment, table);
        }
        return table;
    }


    private YTable createNonItemTable(YDeployment deployment)
    {
        this.yDbModel.createTable(deployment.getTableName(), deployment.getItemTypeCode());
        YTable table = this.yDbModel.getTable(deployment.getTableName());
        addColumn(deployment, "hjmpTS", "java.lang.Long", false, null);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("--Adding hjmpTS column manually to non item table: " + deployment.getTableName());
        }
        for(YAttributeDeployment ad : deployment.getAttributeDeployments())
        {
            YAttributeDeployment.ColumnMapping persistenceMapping = ad.getPersistenceMapping(this.databaseSettings.getDataBaseProvider().getDbName());
            if(LOG.isDebugEnabled())
            {
                LOG.debug(
                                String.format("Java type for deployment (%s) for column (%s) is (%s) and (%s)", new Object[] {deployment.getTableName(), persistenceMapping
                                                .getColumnName(), ad.getJavaTypeName(), ad.getJavaTypeName()}));
            }
            addColumn(deployment, persistenceMapping.getColumnName(), ad.getJavaTypeName(), ad.isPrimaryKey(), persistenceMapping
                            .getSqlDefinition());
        }
        for(YIndexDeployment indexdeployment : deployment.getIndexDeployments())
        {
            createIndex(deployment, indexdeployment, table);
        }
        return table;
    }


    private YTable createAuditTable(YDeployment deployment)
    {
        this.yDbModel.createTable(deployment.getAuditTableName(), deployment.getItemTypeCode());
        YTable table = this.yDbModel.getTable(deployment.getAuditTableName());
        SystemTableGenerator.addInternalColumnsToAuditTable(table, this.databaseJdbcMappingProvider);
        addColumn(table, "changinguser", "java.lang.String", false, null);
        addColumn(table, "context", "HYBRIS.JSON", false, null);
        addColumn(table, "payloadbefore", "HYBRIS.JSON", false, null);
        addColumn(table, "payloadafter", "HYBRIS.JSON", false, null);
        addColumn(table, "operationtype", "java.lang.Long", false, null);
        createAuditIndex(table, "ITEMPK", "Pk", deployment);
        return table;
    }


    private void collectAttributes(YComposedType enclosingType, YComposedType type, YDeployment deployment, Set<YAttributeDeployment> processedDeployments)
    {
        for(YAttributeDescriptor attribute : type.getDeclaredAttributes())
        {
            addToTable(attribute, enclosingType, deployment);
            if(processedDeployments != null)
            {
                YAttributeDeployment attributeDeployment = attribute.tryGetAttributeDeployment();
                if(attributeDeployment != null)
                {
                    processedDeployments.add(attributeDeployment);
                }
            }
        }
        for(YAttributeDescriptor redeclaredAttribute : type.getRedeclaredAttributes())
        {
            if(redeclaredAttribute.isPersistable())
            {
                YAttributeDescriptor superAttr = redeclaredAttribute.getSuperAttribute();
                YColumn mappedColumn = this.yDbModel.findMappedColumn(type, superAttr);
                if(mappedColumn != null)
                {
                    mappedColumn.reuseByAttributeDescriptor(redeclaredAttribute);
                    continue;
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("no mapped column for redeclared attribute " + type.getCode() + "." + redeclaredAttribute
                                    .getQualifier() + " super attribute is " + superAttr.getEnclosingTypeCode() + "." + superAttr
                                    .getQualifier());
                }
            }
        }
    }


    private void collectIndexes(YComposedType type, YDeployment deployment)
    {
        for(YIndex idx : this.typeSystem.getIndexes(type.getCode()))
        {
            if(isForAllDatabases(idx) || isForCurrentDb(idx))
            {
                createItemRelatedIndex(deployment, idx, type);
            }
        }
    }


    private boolean isForAllDatabases(YIndex idx)
    {
        return (idx.getCreationMode() == IndexCreationMode.ALL || idx.getCreationMode() == IndexCreationMode.FORCE);
    }


    private boolean isForCurrentDb(YIndex idx)
    {
        return this.databaseSettings.getDataBaseProvider().getDbName().equals(idx.getCreationMode().getCreationModeCode());
    }


    private void createItemRelatedIndex(YDeployment deployment, YIndex yindex, YComposedType enclosingType)
    {
        YTable table = getTargetIndexTable(deployment, yindex);
        String indexName = computeIndexName(yindex.getName(), table, deployment);
        Index existingIndex = table.findIndex(indexName);
        if(existingIndex == null || yindex.isReplace())
        {
            if(existingIndex != null)
            {
                table.removeIndex(existingIndex);
            }
            if(yindex.isRemove())
            {
                return;
            }
            Index index = createDbAwareIndexInstance(yindex.isUnique(), yindex.getCreationMode());
            index.setName(indexName);
            for(YAttributeDescriptor attribute : yindex.getIndexedAttributes())
            {
                String columnName = this.yDbModel.computeColumnNameForAttributeInType(attribute, enclosingType);
                addColumnToIndex(getColumn(table, columnName), index, yindex.isLower(attribute.getQualifier()));
            }
            enhanceIndexForAdditionalInformation(index, yindex, table, enclosingType);
            table.addIndex(index);
        }
        else if(yindex.isRemove())
        {
            LOG.warn(String.format("Ignoring index (%s) defined for type (%s)  for table (%s). It's marked as removed but the replace attribute is set to false. ", new Object[] {indexName,
                            String.valueOf(deployment.getItemTypeCode()), table.getName()}));
        }
        else
        {
            LOG.info(String.format("Index (%s) already exists  for type (%s)  with table (%s) ", new Object[] {indexName,
                            String.valueOf(deployment.getItemTypeCode()), table.getName()}));
        }
    }


    private boolean checkIfEnhanceIndex(Index index)
    {
        return (index instanceof ExtendedAwareIndex && this.databaseSettings.getDataBaseProvider().isMssqlUsed());
    }


    private void enhanceIndexForAdditionalInformation(Index index, YIndex yindex, YTable table, YComposedType enclosingType)
    {
        if(checkIfEnhanceIndex(index))
        {
            MSSqlExtendedParamsForIndex includeCol = new MSSqlExtendedParamsForIndex();
            if(!tryToAddIncludeColumnsFromProperties(index, yindex, table, enclosingType, includeCol))
            {
                addIncludeColumnsFromItemsXml(index, yindex, table, enclosingType, includeCol);
            }
            ((ExtendedAwareIndex)index).setExtendedParams((ExtendedParamsForIndex)includeCol);
        }
    }


    private void addIncludeColumnsFromItemsXml(Index index, YIndex yindex, YTable table, YComposedType enclosingType, MSSqlExtendedParamsForIndex includeCol)
    {
        for(YAttributeDescriptor attribute : yindex.getIncludeAttributes())
        {
            String columnName = this.yDbModel.computeColumnNameForAttributeInType(attribute, enclosingType);
            addIncludeCol(includeCol, Optional.of(new IndexColumn(getColumn(table, columnName))), index);
        }
    }


    private boolean tryToAddIncludeColumnsFromProperties(Index index, YIndex yindex, YTable table, YComposedType enclosingType, MSSqlExtendedParamsForIndex includeCol)
    {
        String enclosingTypeCode = enclosingType.getCode().toUpperCase(Locale.ENGLISH);
        String indexName = yindex.getName().toUpperCase(Locale.ENGLISH);
        if(this.addIncludeConfiguration.contains(enclosingTypeCode, indexName))
        {
            String qualifiers = (String)this.addIncludeConfiguration.get(enclosingTypeCode, indexName);
            if(qualifiers.trim().isEmpty())
            {
                return true;
            }
            List<Pair<String, Optional<IndexColumn>>> list = getIndexColumnsForQualifierNames(table, enclosingType, qualifiers.split(","));
            List<String> invalidQualifiers = (List<String>)list.stream().filter(p -> ((Optional)p.getValue()).isEmpty()).map(Pair::getKey).collect(Collectors.toList());
            if(!invalidQualifiers.isEmpty())
            {
                String recreatePropertyName = String.format("extend.index.for.%s.%s.with.include", new Object[] {enclosingTypeCode
                                .toLowerCase(LocaleHelper.getPersistenceLocale()), indexName
                                .toLowerCase(LocaleHelper.getPersistenceLocale())});
                LOG.warn(String.format("Property %s has wrong qualifier set: %s and will be skipped", new Object[] {recreatePropertyName, invalidQualifiers}));
                return false;
            }
            list.forEach(val -> addIncludeCol(includeCol, (Optional<IndexColumn>)val.getValue(), index));
            return true;
        }
        return false;
    }


    private List<Pair<String, Optional<IndexColumn>>> getIndexColumnsForQualifierNames(YTable table, YComposedType enclosingType, String[] qualifierNames)
    {
        List<Pair<String, Optional<IndexColumn>>> qualifiersToColumns = new ArrayList<>();
        for(String qualifier : qualifierNames)
        {
            qualifiersToColumns.add(Pair.of(qualifier, getIndexColumnForAttributeQualifier(table, enclosingType, qualifier)));
        }
        return qualifiersToColumns;
    }


    private void addIncludeCol(MSSqlExtendedParamsForIndex additionalParams, Optional<IndexColumn> colToAdd, Index index)
    {
        if(colToAdd.isPresent())
        {
            IndexColumn column = colToAdd.get();
            boolean colNotExist = Arrays.<IndexColumn>stream(index.getColumns()).filter(idxCol -> idxCol.getName().equalsIgnoreCase(column.getName())).findFirst().isEmpty();
            if(colNotExist)
            {
                additionalParams.addColumn(column);
            }
        }
    }


    private Optional<IndexColumn> getIndexColumnForAttributeQualifier(YTable table, YComposedType enclosingType, String qualifier)
    {
        YAttributeDescriptor attributeDescriptor = enclosingType.getAttributeIncludingSuperType(qualifier);
        if(attributeDescriptor == null)
        {
            LOG.debug(String.format("Qualifier: %s not found for table: %s", new Object[] {qualifier, table.getName()}));
            return Optional.empty();
        }
        String columnName = this.yDbModel.computeColumnNameForAttributeInType(attributeDescriptor, enclosingType);
        try
        {
            return Optional.of(new IndexColumn(getColumn(table, columnName)));
        }
        catch(IllegalStateException e)
        {
            LOG.debug(String.format("Column: %s not found for table: %s", new Object[] {columnName, table.getName()}));
            return Optional.empty();
        }
    }


    private YTable getTargetIndexTable(YDeployment deployment, YIndex yindex)
    {
        String tableName = deployment.getTableName();
        return isIndexDeploymentLocalized(yindex) ? this.yDbModel.getLocalizedTable(tableName) : this.yDbModel.getTable(tableName);
    }


    private boolean isIndexDeploymentLocalized(YIndex index)
    {
        List<YAttributeDescriptor> indexedAttributes = index.getIndexedAttributes();
        YAttributeDescriptor first = (YAttributeDescriptor)Iterables.getFirst(indexedAttributes, null);
        Preconditions.checkState((first != null), "Detected index definition without columns (index: " + index + ")");
        boolean localized = first.isLocalized();
        for(YAttributeDescriptor descr : indexedAttributes)
        {
            Preconditions.checkState((descr.isLocalized() == localized), "Detected columns in Index definition which are defined in different tables! (deployment:" + index
                            .getEnclosingType().getDeployment() + ")");
        }
        return localized;
    }


    private void createIndex(YDeployment deploymentForTable, YIndexDeployment indexDeployment, YTable table)
    {
        String indexName = computeIndexName(indexDeployment.getIndexName(), table, deploymentForTable);
        if(table.findIndex(indexName) == null)
        {
            Index index = createDbAwareIndexInstance(indexDeployment.isUnique(), IndexCreationMode.ALL);
            index.setName(indexName);
            for(YAttributeDeployment attributeDeployment : indexDeployment.getIndexedAttributes())
            {
                String columnName = this.yDbModel.getColumnName(attributeDeployment);
                addColumnToIndex(getColumn(table, columnName), index, indexDeployment
                                .isLower(attributeDeployment.getAttributeQualifier()));
            }
            table.addIndex(index);
        }
        else
        {
            LOG.warn(String.format("Index (%s) already exist for table (%s) ", new Object[] {indexName, table.getName()}));
        }
    }


    private String computeIndexName(String declatedIdxName, YTable table, YDeployment deploymentForTable)
    {
        String tableName = table.getName();
        int typeCode = deploymentForTable.getItemTypeCode();
        String defaultName = (typeCode > 0) ? (declatedIdxName + "_" + declatedIdxName) : (declatedIdxName + "_" + declatedIdxName);
        if(isTypeSystemRelatedTable(deploymentForTable) && !this.databaseSettings.isDefaultTypeSystem())
        {
            return "i" + getTypeSystemHash() + "_" + defaultName;
        }
        return defaultName;
    }


    private boolean isTypeSystemRelatedTable(YDeployment deployment)
    {
        return (PersistenceInformation.isTypeSystemRelatedDeployment(deployment.getName()) ||
                        isTypeSystemPropsDeployment(deployment));
    }


    private boolean isTypeSystemPropsDeployment(YDeployment deployment)
    {
        return deployment.getName().toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("property.typesystemprops");
    }


    private String getTypeSystemHash()
    {
        byte[] digest = DigestUtils.getSha1Digest().digest(this.databaseSettings
                        .getTypeSystemName().getBytes());
        byte[] hash = {digest[0], digest[1], digest[digest.length - 2], digest[digest.length - 1]};
        return Hex.encodeHexString(hash);
    }


    private Index createBasicDbAwareIndexInstance(boolean isUnique, IndexCreationMode dbName)
    {
        DbAwareNonUniqueIndex dbAwareNonUniqueIndex;
        if(isUnique)
        {
            DbAwareUniqueIndex dbAwareUniqueIndex = new DbAwareUniqueIndex(dbName, isOnlineIndexSupported());
        }
        else
        {
            dbAwareNonUniqueIndex = new DbAwareNonUniqueIndex(dbName, isOnlineIndexSupported());
        }
        return (Index)dbAwareNonUniqueIndex;
    }


    private Index createExtendedDbAwareIndexInstance(boolean isUnique, IndexCreationMode dbName)
    {
        DBAwareNonUniqueIndexExtended dBAwareNonUniqueIndexExtended;
        if(isUnique)
        {
            DbAwareUniqueIndexExtended dbAwareUniqueIndexExtended = new DbAwareUniqueIndexExtended(dbName, isOnlineIndexSupported());
        }
        else
        {
            dBAwareNonUniqueIndexExtended = new DBAwareNonUniqueIndexExtended(dbName, isOnlineIndexSupported());
        }
        ((ExtendedAwareIndex)dBAwareNonUniqueIndexExtended).setExtendedParams((ExtendedParamsForIndex)new MSSqlExtendedParamsForIndex());
        return (Index)dBAwareNonUniqueIndexExtended;
    }


    private Index createDbAwareIndexInstance(boolean isUnique, IndexCreationMode dbName)
    {
        if(this.databaseSettings.getDataBaseProvider().isMssqlUsed())
        {
            return createExtendedDbAwareIndexInstance(isUnique, dbName);
        }
        return createBasicDbAwareIndexInstance(isUnique, dbName);
    }


    private boolean isOnlineIndexSupported()
    {
        return Boolean.parseBoolean(this.databaseSettings.getProperty("db.onlineindices", "false"));
    }


    private void addColumnToIndex(Column column, Index index, boolean lower)
    {
        IndexColumn indexColumn = getIndexColumnForColumn(lower);
        indexColumn.setColumn(column);
        indexColumn.setSize(column.getSize());
        index.addColumn(indexColumn);
    }


    private IndexColumn getIndexColumnForColumn(boolean lower)
    {
        if(lower && isLowerIndexEnabledForCurrentDatabase())
        {
            return (IndexColumn)new DefaultFunctionalIndexColumn("LOWER");
        }
        return new IndexColumn();
    }


    private boolean isLowerIndexEnabledForCurrentDatabase()
    {
        if(this.propertiesLoader != null)
        {
            if(Boolean.parseBoolean(this.propertiesLoader
                            .getProperty(this.databaseSettings
                                            .getDataBaseProvider().getDbName() + ".use.lower.indexes", "false")))
                ;
        }
        return false;
    }


    private Column getColumn(YTable table, String columnName)
    {
        Column column = table.findColumn(columnName);
        if(column == null)
        {
            throw new IllegalStateException("Table does not have column " + columnName);
        }
        return column;
    }


    private void addToTable(YAttributeDescriptor attributeDescriptor, YComposedType enclosingType, YDeployment deployment)
    {
        if(attributeDescriptor.isPersistable())
        {
            YTable targetTable = attributeDescriptor.isLocalized() ? this.yDbModel.getLocalizedTable(deployment.getTableName()) : this.yDbModel.getTable(deployment.getTableName());
            String targetColumnName = this.yDbModel.computeColumnNameForAttributeInType(attributeDescriptor, enclosingType);
            String availableColumnName = findAvailableColumnName(attributeDescriptor, targetTable, targetColumnName, enclosingType);
            YColumn targetColumn = (YColumn)targetTable.findColumn(availableColumnName);
            if(targetColumn == null)
            {
                targetTable.addColumn(createColumn(attributeDescriptor, availableColumnName, targetTable.getName()));
                targetTable.addTableColumnAttributeDescriptorRelation(availableColumnName, attributeDescriptor.getColumnType()
                                .getJavaClassName());
            }
            else
            {
                logAndCheckConsistencyForReusedColumn(attributeDescriptor, targetTable, availableColumnName, targetColumn);
                targetColumn.reuseByAttributeDescriptor(attributeDescriptor);
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Attribute (%s) is not persistable hence avoiding ", new Object[] {attributeDescriptor.getQualifier()}));
        }
    }


    private void logAndCheckConsistencyForReusedColumn(YAttributeDescriptor attributeDescriptor, YTable targetTable, String availableColumnName, YColumn targetColumn)
    {
        if(Boolean.TRUE.equals(this.detectCorrectnessForReusedColumnsFlag.get()))
        {
            DatabaseModelVerifier.DatabaseVerifierParams params = DatabaseModelVerifier.DatabaseVerifierParams.builder().withDatabaseModelGenerator(this).withAttributeToVerify(attributeDescriptor).withColumnName(availableColumnName).withTargetTableName(targetTable.getName())
                            .withExistingColumn(targetColumn).build();
            DatabaseModelVerifier.verifyCorrectnessOfReusedColumnForAttribute(params);
        }
        LOG.debug(String.format("Reusing column: %s (%s) for attribute %s", new Object[] {targetColumn.getName(), targetTable.getName(), attributeDescriptor}));
    }


    private boolean isDetectConsistencyForReusedColumnEnabled()
    {
        return Boolean.parseBoolean(getPlatformProperties().getProperty("detect.inconsistency.for.reused.column", Boolean.FALSE.toString()));
    }


    private String findAvailableColumnName(YAttributeDescriptor attributeDescriptor, YTable targetTable, String requestedColumnName, YComposedType enclosingType)
    {
        String proposedColumnName = requestedColumnName;
        YColumn column = (YColumn)targetTable.findColumn(proposedColumnName);
        if(attributeDescriptor.hasFixedColumnName())
        {
            if(columnCanBeUsedForAttribute(column, proposedColumnName, attributeDescriptor, targetTable, enclosingType))
            {
                return (column != null) ? column.getName() : proposedColumnName;
            }
            throw new IllegalStateException("Attribute " + attributeDescriptor + " has fixed column name " + proposedColumnName + " but cannot be mapped to existing incompatible column " + proposedColumnName + "!");
        }
        int turn = 0;
        while(!columnCanBeUsedForAttribute(column, proposedColumnName, attributeDescriptor, targetTable, enclosingType))
        {
            proposedColumnName = mutateColumnName(requestedColumnName, turn++);
            column = (YColumn)targetTable.findColumn(proposedColumnName);
        }
        return (column != null) ? column.getName() : proposedColumnName;
    }


    private boolean columnCanBeUsedForAttribute(YColumn existingColumn, String proposedColumnName, YAttributeDescriptor attributeDescriptor, YTable targetTable, YComposedType enclosingType)
    {
        if(existingColumn != null)
        {
            return canReuseExistingColumnForAttribute(existingColumn, attributeDescriptor, targetTable);
        }
        return !columnNameCollidesWithSomeSubTypeFixedColumn(attributeDescriptor, proposedColumnName, enclosingType);
    }


    protected boolean canReuseExistingColumnForAttribute(YColumn existingColumn, YAttributeDescriptor attributeDescriptor, YTable targetTable)
    {
        if(existingColumn == null)
        {
            return false;
        }
        if(attributeDescriptor.hasFixedColumnName())
        {
            return sqlTypeMatchesJavaType(existingColumn, targetTable, attributeDescriptor);
        }
        if(existingColumn.isUsedByAnyOfSuperTypesOf(attributeDescriptor.getDeclaringType()))
        {
            return false;
        }
        if(existingColumn.isMappedToAttributeWithQualifierDifferentThan(attributeDescriptor.getQualifier()))
        {
            return false;
        }
        return sqlTypeMatchesJavaType(existingColumn, targetTable, attributeDescriptor);
    }


    private boolean sqlTypeMatchesJavaType(YColumn existingColumn, YTable targetTable, YAttributeDescriptor attributeDescriptor)
    {
        String className = (String)targetTable.getColumnDescriptor().get(existingColumn.getName());
        return (className != null && className.equals(attributeDescriptor.getColumnType().getJavaClassName()));
    }


    private boolean columnNameCollidesWithSomeSubTypeFixedColumn(YAttributeDescriptor attributeDescriptor, String columnName, YComposedType enclosingType)
    {
        if(attributeDescriptor.hasFixedColumnName())
        {
            return false;
        }
        YComposedType declaringType = attributeDescriptor.getDeclaringType();
        for(YComposedType type : declaringType.getAllSubtypes())
        {
            for(YAttributeDescriptor ad : type.getDeclaredAttributes())
            {
                if(ad.hasFixedColumnName() && ad.getPersistenceType() != YAttributeDescriptor.PersistenceType.CMP)
                {
                    String thatColumnName = this.yDbModel.computeColumnNameForAttributeInType(ad, enclosingType);
                    if(thatColumnName != null && thatColumnName.equals(columnName))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private String mutateColumnName(String original, int turn)
    {
        String postFix = Integer.toString(turn);
        return original.substring(0, original.length() - postFix.length()) + original.substring(0, original.length() - postFix.length());
    }


    Column createColumn(YAttributeDescriptor attr, String columnName, String targetTableName)
    {
        String customColType = getCustomPersistenceType(attr);
        if(StringUtils.isNotBlank(customColType))
        {
            JdbcType customJdbcType = this.databaseJdbcMappingProvider.getMapping(customColType);
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("------Creating column (%s) of type (%s) for table (%s), attr java class is: (%s) and (%s)", new Object[] {columnName, customColType, targetTableName, attr
                                .getColumnType().getJavaClassName(), attr
                                .getColumnType()
                                .getJavaClassName()}));
            }
            if(customJdbcType != null)
            {
                return createColumn(attr, columnName, customJdbcType, isPrimaryKeyColumn(columnName), null);
            }
            YColumn yColumn = new YColumn(attr);
            yColumn.setName(columnName);
            yColumn.setCustomColumnDefinition(customColType);
            return (Column)yColumn;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("------Creating column (%s) of type (%s) for table (%s), attr java class is: (%s) and (%s) and (%s)", new Object[] {columnName, customColType, targetTableName, attr
                            .getColumnType().getJavaClassName(), attr.getColumnType()
                            .getJavaClassName(),
                            getPersistenceTypeForJavaType(attr)}));
        }
        return createColumn(attr, columnName, getPersistenceTypeForJavaType(attr),
                        isPrimaryKeyColumn(columnName), null);
    }


    private boolean isPrimaryKeyColumn(String columnName)
    {
        return "pk".equalsIgnoreCase(columnName);
    }


    protected JdbcType getPersistenceTypeForJavaType(YAttributeDescriptor attr)
    {
        String persistenceJavaType = getDefaultPersistenceJavaType(attr);
        JdbcType jdbcType = this.databaseJdbcMappingProvider.getMapping(persistenceJavaType);
        if(jdbcType == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Unknown persistence java type " + persistenceJavaType + "  for attribute (" + attr
                                .getEnclosingTypeCode() + "." + attr
                                .getQualifier() + ") - falling back to " + Serializable.class.getName());
            }
            jdbcType = this.databaseJdbcMappingProvider.getMapping(Serializable.class.getName());
        }
        if(jdbcType == null)
        {
            throw new IllegalArgumentException("illegal persistent attribute " + attr + " cannot find jdbc type for persistence java type " + persistenceJavaType);
        }
        return jdbcType;
    }


    private void addColumn(YDeployment deployment, String columnName, String columnJavaType, boolean primaryKey, String sqlDefinition)
    {
        YTable targetTable = this.yDbModel.getTable(deployment.getTableName());
        addColumn(targetTable, columnName, columnJavaType, primaryKey, sqlDefinition);
    }


    private void addColumn(YTable targetTable, String columnName, String columnJavaType, boolean primaryKey, String sqlDefinition)
    {
        if(targetTable.findColumn(columnName) == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("------Adding column (%s) of type (%s) to table (%s)", new Object[] {columnName, columnJavaType, targetTable
                                .getName()}));
            }
            JdbcType jdbcType = this.databaseJdbcMappingProvider.getMapping(columnJavaType);
            if(jdbcType != null)
            {
                Column column = createColumn(null, columnName, jdbcType, primaryKey, sqlDefinition);
                targetTable.addColumn(column);
            }
            else
            {
                throw new IllegalStateException("Missing JDBC  mapping  for column " + columnName + " for table " + targetTable
                                .getName() + " column java type  is " + columnJavaType);
            }
        }
    }


    private void addRelation(YRelation yRelation)
    {
        for(YComposedType yComposedType : yRelation.getAllSuperTypes())
        {
            if(yComposedType instanceof YRelation)
            {
                collectRelation((YRelation)yComposedType);
            }
        }
        collectRelation(yRelation);
    }


    protected String getCustomPersistenceType(YAttributeDescriptor attr)
    {
        String customType = getCustomPersistenceTypeFromAttribute(attr);
        if(StringUtils.isBlank(customType))
        {
            customType = getCustomPersistenceTypeFromAttributeDeployment(attr);
        }
        return customType;
    }


    protected String getDefaultPersistenceJavaType(YAttributeDescriptor attr)
    {
        if(attr.isLocalized())
        {
            return getDefaulLocalizedColumnPersistenceJavaType((YMapType)attr.getType());
        }
        String javaType = getPersistenceJavaTypeFromAttributeDeployment(attr);
        if(StringUtils.isBlank(javaType))
        {
            javaType = getDefaultUnlocalizedColumnPersistenceJavaType(attr.getType());
        }
        return javaType;
    }


    private Map<String, String> redefineColumnDefinitionForGivenTypeAndQualifierIfNeeded(YAttributeDescriptor attr)
    {
        String enclosingType = attr.getEnclosingTypeCode().toUpperCase(LocaleHelper.getPersistenceLocale());
        String qualifier = attr.getQualifier().toUpperCase(LocaleHelper.getPersistenceLocale());
        if(this.ovveriddenDatabaseColumnConfiguration.contains(enclosingType, qualifier))
        {
            return (Map<String, String>)new Object(this, enclosingType, qualifier);
        }
        return attr.getDbColumnDefinitions();
    }


    protected String getCustomPersistenceTypeFromAttribute(YAttributeDescriptor attr)
    {
        Map<String, String> dbColumnDefinitions = redefineColumnDefinitionForGivenTypeAndQualifierIfNeeded(attr);
        String customType = null;
        if(MapUtils.isNotEmpty(dbColumnDefinitions))
        {
            customType = dbColumnDefinitions.get(this.databaseSettings.getDataBaseProvider().getDbName());
            if(customType == null)
            {
                customType = dbColumnDefinitions.get(null);
            }
        }
        return customType;
    }


    protected String getCustomPersistenceTypeFromAttributeDeployment(YAttributeDescriptor attr)
    {
        YAttributeDeployment attributeDeployment = attr.tryGetAttributeDeployment();
        String customType = null;
        if(attributeDeployment != null)
        {
            YAttributeDeployment.ColumnMapping persistenceMapping = attributeDeployment.getPersistenceMapping(this.databaseSettings
                            .getDataBaseProvider()
                            .getDbName());
            if(persistenceMapping != null)
            {
                customType = persistenceMapping.getSqlDefinition();
            }
        }
        return customType;
    }


    protected String getPersistenceJavaTypeFromAttributeDeployment(YAttributeDescriptor attr)
    {
        YAttributeDeployment attributeDeployment = attr.tryGetAttributeDeployment();
        String javaType = null;
        if(attributeDeployment != null)
        {
            YAttributeDeployment.ColumnMapping persistenceMapping = attributeDeployment.getPersistenceMapping(this.databaseSettings
                            .getDataBaseProvider()
                            .getDbName());
            if(persistenceMapping != null)
            {
                javaType = attributeDeployment.getJavaTypeName();
            }
        }
        return javaType;
    }


    protected String getDefaultUnlocalizedColumnPersistenceJavaType(YType type)
    {
        if(type instanceof de.hybris.bootstrap.typesystem.YAtomicType)
        {
            return type.getJavaClassName();
        }
        if(type instanceof YComposedType)
        {
            return "HYBRIS.PK";
        }
        if(type instanceof YCollectionType && ((YCollectionType)type).getElementType() instanceof YComposedType)
        {
            return "HYBRIS.COMMA_SEPARATED_PKS";
        }
        return Object.class.getName();
    }


    protected String getDefaulLocalizedColumnPersistenceJavaType(YMapType type)
    {
        return getDefaultUnlocalizedColumnPersistenceJavaType(type.getReturnType());
    }


    private Column createColumn(YAttributeDescriptor attr, String colName, JdbcType columnDetails, boolean primaryKey, String sqlDefinition)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("-------creating the  Column (%s) for jave type  (%s)", new Object[] {colName, columnDetails}));
        }
        if(sqlDefinition != null)
        {
            YColumn column = new YColumn(sqlDefinition);
            column.setName(colName);
            if(primaryKey)
            {
                column.setPrimaryKey(primaryKey);
                column.setRequired(true);
            }
            return (Column)column;
        }
        Column col = createColumnInternal(attr, columnDetails);
        col.setName(colName);
        if(primaryKey)
        {
            col.setPrimaryKey(primaryKey);
            col.setRequired(true);
        }
        if(columnDetails != null)
        {
            col.setTypeCode(columnDetails.getJdbcType());
        }
        if(columnDetails != null && columnDetails.getSize() != null)
        {
            col.setSize(columnDetails.getSize());
        }
        if(columnDetails != null && columnDetails.getScale() > 0)
        {
            col.setScale(columnDetails.getScale());
        }
        if(columnDetails != null)
        {
            col.setDefaultValue(columnDetails.getDefaultValue());
        }
        return col;
    }


    private Column createColumnInternal(YAttributeDescriptor attribute, JdbcType columnType)
    {
        boolean attributeIsGiven = (attribute != null);
        boolean columnTypeIsGiven = (columnType != null);
        return (attributeIsGiven || (columnTypeIsGiven && columnType.hasComplexSize())) ? (Column)new YColumn(attribute) : new Column();
    }


    private boolean isOneToMany(YRelationEnd sourceEnd, YRelationEnd targetEnd)
    {
        return (isRelationEndTypeOne(sourceEnd) || isRelationEndTypeOne(targetEnd));
    }


    private boolean isRelationEndTypeOne(YRelationEnd relationEnd)
    {
        return YRelationEnd.Cardinality.ONE.equals(relationEnd.getCardinality());
    }


    private void createOneToManyRelationIndex(YRelationEnd sourceEnd, YRelationEnd targetEnd)
    {
        OneToManyRelationIndicesResolver indicesResolver = new OneToManyRelationIndicesResolver(this, sourceEnd, targetEnd);
        for(YDeployment deployment : indicesResolver.getManySideDeployments())
        {
            YTable table = this.yDbModel.getTable(deployment.getTableName());
            int itemTypeCode = deployment.getItemTypeCode();
            String prefix = "";
            if(isTypeSystemRelatedTable(deployment) && !this.databaseSettings.isDefaultTypeSystem())
            {
                prefix = "i" + getTypeSystemHash() + "_";
            }
            createAndAddIndexToTableIfColumnExists(indicesResolver.getIndexedAttribute(), prefix, "RelIDX_" + itemTypeCode, table);
            if(indicesResolver.hasOrdertingAttribute())
            {
                createAndAddIndexToTableIfColumnExists(indicesResolver.getIndexedOrderingAttribute(), prefix, "PosIDX_" + itemTypeCode, table);
            }
        }
    }


    private void createAndAddIndexToTableIfColumnExists(YAttributeDescriptor indexedAttribute, String idxNamePrefix, String idxNameSuffix, YTable table)
    {
        YColumn column = table.findMappedColumn(indexedAttribute);
        if(column == null)
        {
            return;
        }
        String indexName = computeIndexNameForColumn((Column)column, idxNamePrefix, idxNameSuffix);
        Index relIndex = createNonUniqueIndexOnColumns(indexName, new Column[] {(Column)column});
        addIndexToTableIfNotExist((Table)table, relIndex);
    }


    private void addIndexToTableIfNotExist(Table table, Index index)
    {
        if(hasIndex(table, index))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Index " + index.getName() + " already exists in table " + table.getName());
            }
        }
        else
        {
            table.addIndex(index);
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Adding index (%s) on table (%s)", new Object[] {index.getName(), table.getName()}));
            }
        }
    }


    private boolean hasIndex(Table table, Index index)
    {
        return (table.findIndex(index.getName()) != null);
    }


    private String computeIndexNameForColumn(Column column, String idxNamePrefix, String idxNameSuffix)
    {
        Preconditions.checkNotNull(column);
        String columnName = StringUtils.removeStartIgnoreCase(column.getName(), "p_");
        return idxNamePrefix + idxNamePrefix + columnName;
    }


    private Index createNonUniqueIndexOnColumns(String idxName, Column... columns)
    {
        Preconditions.checkNotNull(idxName);
        Preconditions.checkArgument((columns.length > 0), "At least one column is required to create index");
        Index index = createDbAwareIndexInstance(false, IndexCreationMode.ALL);
        index.setName(idxName);
        for(Column column : columns)
        {
            index.addColumn(createIndexColumn(column));
        }
        return index;
    }


    private IndexColumn createIndexColumn(Column column)
    {
        IndexColumn idxColumn = new IndexColumn(column);
        idxColumn.setSize(column.getSize());
        return idxColumn;
    }


    protected void createSystemTables()
    {
        createNonItemTable(this.typeSystem.getDeployment("YDeployment.YDeployments"));
        createNonItemTable(this.typeSystem.getDeployment("NumberSeries.NumberSeries"));
        createNonItemTable(this.typeSystem.getDeployment("MetaInformation"));
        createNonItemTable(this.typeSystem.getDeployment("ACLEntry.ACLEntries"));
        createNonItemTable(this.typeSystem.getDeployment("ConfigItem"));
    }


    private void collectRelation(YRelation yRelation)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Creating the relation " + yRelation.getCode());
        }
        YRelationEnd sourceEnd = yRelation.getSourceEnd();
        YRelationEnd targetEnd = yRelation.getTargetEnd();
        if(isOneToMany(sourceEnd, targetEnd))
        {
            createOneToManyRelationIndex(sourceEnd, targetEnd);
        }
    }


    private void checkTableName(YDeployment depl, String typeName)
    {
        String tableName = depl.getTableName();
        if(tableName != null)
        {
            int tableNameMaxLength = Integer.parseInt(this.databaseSettings.getProperty("deployment.tablename.maxlength", "24"));
            if(Boolean.parseBoolean(this.databaseSettings.getProperty("deployment.checktablename", "true")) && (tableName
                            .length() > tableNameMaxLength || !Pattern.matches("^[A-Za-z]+[A-Za-z0-9_]*$", tableName)))
            {
                throw new RuntimeException("cannot create deployment for type " + typeName + " - the table name " + tableName + " is too long or it has invalid characters");
            }
        }
    }
}
