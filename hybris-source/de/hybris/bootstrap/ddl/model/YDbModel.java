package de.hybris.bootstrap.ddl.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.dbtypesystem.Attribute;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.dbtypesystem.Type;
import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.log4j.Logger;

public class YDbModel implements YDbTableProvider
{
    private final String LOCALIZED_SUFFIX = "lp";
    private final DatabaseSettings databaseSettings;
    private final DbTypeSystem dbTypeSystem;
    private final Map<String, Map<String, String>> tableCreationParameters = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(YDbModel.class);
    private final List<YTable> localizedTables;
    private final Map<String, YTable> tables;


    public YDbModel(DatabaseSettings databaseSettings, DbTypeSystem dbTypeSystem)
    {
        this.localizedTables = new ArrayList<>();
        this.tables = (Map<String, YTable>)new Object(this);
        this.databaseSettings = databaseSettings;
        this.dbTypeSystem = dbTypeSystem;
    }


    public Database createDatabase()
    {
        return createDatabase("");
    }


    public Database createDatabase(String tablePrefix)
    {
        Database database = new Database();
        database.setName("hybris");
        database.addTables(sortTablesByName(this.tables.values(), tablePrefix));
        return database;
    }


    public CreationParameters getCreationParameters()
    {
        CreationParameters result = new CreationParameters();
        for(Map.Entry<String, YTable> tableEntry : this.tables.entrySet())
        {
            String tableName = tableEntry.getKey();
            YTable table = tableEntry.getValue();
            Map<String, String> props = this.tableCreationParameters.containsKey(tableName) ? this.tableCreationParameters.get(tableName) : Collections.<String, String>emptyMap();
            for(Map.Entry<String, String> propEntry : props.entrySet())
            {
                result.addParameter((Table)table, propEntry.getKey(), propEntry.getValue());
            }
        }
        return result;
    }


    public YTable getMappedTable(YComposedType yComposedType)
    {
        YDeployment depl = yComposedType.getDeployment();
        if(depl != null && !depl.isAbstract())
        {
            return getTable(depl.getTableName());
        }
        return null;
    }


    public YColumn findMappedColumn(YComposedType type, YAttributeDescriptor attribute)
    {
        YTable mappedTable = getMappedTable(type);
        return (mappedTable == null) ? null : mappedTable.findMappedColumn(attribute);
    }


    public YTable getTable(String tableName)
    {
        if(tableName == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Table name cannot be null. All the tables to be processed:\n " + this.tables.keySet() + "\n");
            }
            throw new IllegalArgumentException("Table name cannot be null, probably the database is corrupted, e.g. no entry in ydeployments table for a type, where it is expected");
        }
        return this.tables.get(tableName);
    }


    private Collection<YTable> sortTablesByName(Collection<YTable> tables, String tablePrefix)
    {
        Stream<YTable> collect = tables.stream().sorted(Comparator.comparing(o -> o.getName().toLowerCase(LocaleHelper.getPersistenceLocale())));
        if(StringUtils.isBlank(tablePrefix))
        {
            return collect.collect((Collector)Collectors.toList());
        }
        return collect.<Collection<YTable>>collect(ArrayList::new, (list, table) -> {
            table.setName(tablePrefix + tablePrefix);
            addPrefixToIndexNames(tablePrefix, table);
            list.add(table);
        }List::addAll);
    }


    private void addPrefixToIndexNames(String tablePrefix, YTable table)
    {
        for(Index index : table.getIndices())
        {
            index.setName(tablePrefix + tablePrefix);
        }
    }


    public YTable getLocalizedTable(String tableName)
    {
        return getTable(tableName + "lp");
    }


    public void removeLocalizedTable(String tableName)
    {
        this.tables.remove(tableName);
    }


    public void createTable(String tableName, int itemTypeCode)
    {
        createTable(tableName, itemTypeCode, Collections.emptyMap());
    }


    public void createTable(String tableName, int itemTypeCode, Map<String, String> customProperties)
    {
        YTable lpTable = new YTable(tableName + "lp", itemTypeCode);
        YTable table = new YTable(tableName, itemTypeCode);
        this.tables.put(table.getName(), table);
        this.tables.put(lpTable.getName(), lpTable);
        this.localizedTables.add(lpTable);
        this.tableCreationParameters.put(table.getName(), new HashMap<>(customProperties));
    }


    public List<YTable> getLocalizedTables()
    {
        return (List<YTable>)ImmutableList.copyOf(this.localizedTables);
    }


    public Map<String, YTable> getTables()
    {
        return (Map<String, YTable>)ImmutableMap.copyOf(this.tables);
    }


    public String getColumnName(YAttributeDeployment attributeDeployment)
    {
        return attributeDeployment.getColumnName(this.databaseSettings);
    }


    public String computeColumnNameForAttributeInType(YAttributeDescriptor attributeDescriptor, YComposedType enclosingType)
    {
        if(this.dbTypeSystem != null)
        {
            Type dbType = this.dbTypeSystem.findTypeByCode(enclosingType.getCode());
            if(dbType != null)
            {
                Attribute dbAttribute = dbType.getAttribute(attributeDescriptor.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
                if(dbAttribute != null)
                {
                    YAttributeDescriptor.PersistenceType dbPersistenceType = dbAttribute.calculatePersistenceType();
                    if(StringUtils.isNotEmpty(dbAttribute.getColumnName()) && dbPersistenceType != YAttributeDescriptor.PersistenceType.CMP)
                    {
                        return dbAttribute.getColumnName();
                    }
                }
            }
        }
        String result = attributeDescriptor.getColumnName(this.databaseSettings);
        if(attributeDescriptor.hasFixedColumnName())
        {
            return result;
        }
        return DDLGeneratorUtils.getShortenedColumnName(result);
    }
}
