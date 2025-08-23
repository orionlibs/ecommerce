package de.hybris.bootstrap.ddl.adjusters;

import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.SchemaAdjuster;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.log4j.Logger;

public class RemoveConfiguredIndicesFromDBModel implements SchemaAdjuster
{
    private static final Logger LOG = Logger.getLogger(RemoveConfiguredIndicesFromDBModel.class);
    private final RemoveIndicesParams indicesParams;
    private static final String SPLIT_CHAR = ";";


    public RemoveConfiguredIndicesFromDBModel(RemoveIndicesParams indicesParams)
    {
        Objects.requireNonNull(indicesParams);
        this.indicesParams = indicesParams;
    }


    public void adjust()
    {
        Set<TableNameWithIndexName> indexObjectsToDisable = new HashSet<>();
        indexObjectsToDisable.addAll(getObjectToDisableForConfiguredIndexNames());
        indexObjectsToDisable.addAll(getObjectToDisableForConfiguredIndexRegex());
        if(!this.indicesParams.isDropIndicesFoundInItems().booleanValue() && !indexObjectsToDisable.isEmpty() && this.indicesParams.getDatabase() != null)
        {
            disableObjectIndicesForDatabase(indexObjectsToDisable);
        }
    }


    private Set<TableNameWithIndexName> getObjectToDisableForConfiguredIndexNames()
    {
        Set<String> ignoredIndexNames = getIgnoredIndices(this.indicesParams.getIndicesNamesToIgnoreInDbModel());
        Set<TableNameWithIndexName> ignoredObjects = new HashSet<>();
        for(Table table : this.indicesParams.getDbModel().getTables().values())
        {
            for(Index itemsIndex : table.getIndices())
            {
                String adjustedIndexNameFromDbModel = adjustIndexName(itemsIndex.getName());
                if(ignoredIndexNames.contains(adjustedIndexNameFromDbModel))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(String.format("Disabling index for table %s by configured index name %s ", new Object[] {table.getName(), adjustedIndexNameFromDbModel}));
                    }
                    table.removeIndex(itemsIndex);
                    ignoredObjects.add(new TableNameWithIndexName(adjustTableName(table.getName()), adjustedIndexNameFromDbModel));
                }
            }
        }
        return ignoredObjects;
    }


    private String adjustIndexName(String indexNameFromDb)
    {
        String adjustedIndexNameFromDbModel = DDLGeneratorUtils.adjustForTablePrefix(indexNameFromDb, this.indicesParams.getTablePrefix()).toLowerCase(LocaleHelper.getPersistenceLocale());
        if(shouldShortNameOfIndex() && adjustedIndexNameFromDbModel.length() > this.indicesParams.getConstraintNameLength().intValue())
        {
            return adjustedIndexNameFromDbModel.substring(0, this.indicesParams.getConstraintNameLength().intValue());
        }
        return adjustedIndexNameFromDbModel;
    }


    private String adjustTableName(String tableNameFromDb)
    {
        String adjustedTableNameFromDbModel = DDLGeneratorUtils.adjustForTablePrefix(tableNameFromDb, this.indicesParams.getTablePrefix()).toLowerCase(LocaleHelper.getPersistenceLocale());
        if(shouldShortNameOfTable() && adjustedTableNameFromDbModel.length() > this.indicesParams.getTableNameLength().intValue())
        {
            return adjustedTableNameFromDbModel.substring(0, this.indicesParams.getTableNameLength().intValue());
        }
        return adjustedTableNameFromDbModel;
    }


    private Set<TableNameWithIndexName> getObjectToDisableForConfiguredIndexRegex()
    {
        List<Pattern> patternList = preparePatterns(getIgnoredIndices(this.indicesParams.getIndicesRegexToIgnoreInDbModel()));
        Set<TableNameWithIndexName> ignoredObjects = new HashSet<>();
        for(Table table : this.indicesParams.getDbModel().getTables().values())
        {
            for(Index itemsIndex : table.getIndices())
            {
                for(Pattern p : patternList)
                {
                    String adjustedIndexNameFromDbModel = adjustIndexName(itemsIndex.getName());
                    if(p.matcher(adjustedIndexNameFromDbModel).find())
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(String.format("Disabling index %s for table %s by configured regex %s ", new Object[] {adjustedIndexNameFromDbModel, table
                                            .getName(), p.pattern()}));
                        }
                        table.removeIndex(itemsIndex);
                        ignoredObjects.add(new TableNameWithIndexName(adjustTableName(table.getName()), adjustedIndexNameFromDbModel));
                    }
                }
            }
        }
        return ignoredObjects;
    }


    private void disableObjectIndicesForDatabase(Set<TableNameWithIndexName> objectIndexToRemove)
    {
        if(this.indicesParams.getDatabase() != null)
        {
            Map<String, Table> tableNameToTableMap = createNameToTableMap();
            for(TableNameWithIndexName objectWithIndexToDisable : objectIndexToRemove)
            {
                Table table = tableNameToTableMap.get(objectWithIndexToDisable.getTableName());
                if(table != null)
                {
                    for(Index index : table.getIndices())
                    {
                        if(index.getName().equalsIgnoreCase(objectWithIndexToDisable.getIndexName()))
                        {
                            table.removeIndex(index);
                        }
                    }
                }
            }
        }
    }


    private Map<String, Table> createNameToTableMap()
    {
        Map<String, Table> tableNameToTableMap = new HashMap<>();
        for(Table table : this.indicesParams.getDatabase().getTables())
        {
            tableNameToTableMap.put(table.getName().toLowerCase(LocaleHelper.getPersistenceLocale()), table);
        }
        return tableNameToTableMap;
    }


    private boolean shouldShortNameOfTable()
    {
        return (this.indicesParams.getTableNameLength() != null && this.indicesParams.getTableNameLength().intValue() > -1);
    }


    private boolean shouldShortNameOfIndex()
    {
        return (this.indicesParams.getConstraintNameLength() != null && this.indicesParams.getConstraintNameLength().intValue() > -1);
    }


    private List<Pattern> preparePatterns(Set<String> ignoredIndicesRegex)
    {
        List<Pattern> patternList = new ArrayList<>();
        for(String regex : ignoredIndicesRegex)
        {
            try
            {
                patternList.add(Pattern.compile(regex, 2));
            }
            catch(PatternSyntaxException pse)
            {
                LOG.info(String.format("Skipping invalid pattern %s", new Object[] {regex}), pse);
            }
        }
        return patternList;
    }


    private Set<String> getIgnoredIndices(Set<String> ignoredIndicesNames)
    {
        Set<String> returnSet = new HashSet<>();
        for(String ignoredIndicesName : ignoredIndicesNames)
        {
            returnSet.addAll((Collection<? extends String>)Stream.<String>of(ignoredIndicesName.split(";"))
                            .map(elem -> elem.toLowerCase(LocaleHelper.getPersistenceLocale()))
                            .collect(Collectors.toSet()));
        }
        return returnSet;
    }
}
