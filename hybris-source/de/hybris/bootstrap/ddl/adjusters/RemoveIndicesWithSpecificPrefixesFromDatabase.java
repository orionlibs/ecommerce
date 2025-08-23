package de.hybris.bootstrap.ddl.adjusters;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.SchemaAdjuster;
import de.hybris.bootstrap.ddl.model.YDbModel;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveIndicesWithSpecificPrefixesFromDatabase implements SchemaAdjuster
{
    private static final Logger LOG = LoggerFactory.getLogger(RemoveIndicesWithSpecificPrefixesFromDatabase.class);
    private final List<String> ignoredIndices;
    private final RemoveIndicesParams params;


    public RemoveIndicesWithSpecificPrefixesFromDatabase(RemoveIndicesParams params)
    {
        Objects.requireNonNull(params);
        this.params = params;
        this.ignoredIndices = getIgnoredIndices(params.getIgnoredIndicesStr());
    }


    public void adjust()
    {
        Map<String, Table> dbModelMap = null;
        List<Pattern> patternList = preparePatterns(this.ignoredIndices);
        for(Pattern indexPrefixToIgnore : patternList)
        {
            for(Table table : this.params.getDatabase().getTables())
            {
                for(Index index : table.getIndices())
                {
                    if(indexPrefixToIgnore.matcher(index.getName()).find())
                    {
                        if(dbModelMap == null)
                        {
                            dbModelMap = getDbModelTableMap(this.params.getDbModel(), this.params.getTablePrefix());
                        }
                        adjustModels(table, index, this.params.isRecreateIndicesFoundInItems().booleanValue(), dbModelMap);
                    }
                }
            }
        }
    }


    private void adjustModels(Table tableFromDatabase, Index indexFromDatabase, boolean shouldRecreateIndicesFoundInItems, Map<String, Table> dbModelMap)
    {
        boolean indexFoundInDBModel = false;
        Table tableFromDbModel = dbModelMap.get(tableFromDatabase.getName().toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(tableFromDbModel != null)
        {
            for(Index indexFromDbModel : tableFromDbModel.getIndices())
            {
                if(compareIndexNames(indexFromDbModel, indexFromDatabase, this.params.getTablePrefix()))
                {
                    indexFoundInDBModel = true;
                    if(shouldRecreateIndicesFoundInItems)
                    {
                        break;
                    }
                    LOG.debug("Removing index: {} table: {} for database and db model", indexFromDatabase.getName(), tableFromDatabase
                                    .getName());
                    tableFromDatabase.removeIndex(indexFromDatabase);
                    tableFromDbModel.removeIndex(indexFromDbModel);
                    break;
                }
            }
        }
        if(!indexFoundInDBModel)
        {
            LOG.debug("Removing index: {} table: {} for database model", indexFromDatabase.getName(), tableFromDatabase
                            .getName());
            tableFromDatabase.removeIndex(indexFromDatabase);
        }
    }


    private List<String> getIgnoredIndices(String ignoredIndices)
    {
        return (List<String>)Stream.<String>of(ignoredIndices.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
    }


    private boolean shouldShortNameOfTable()
    {
        return (this.params.getTableNameLength() != null && this.params.getTableNameLength().intValue() > -1);
    }


    private boolean shouldShortNameOfIndex()
    {
        return (this.params.getConstraintNameLength() != null && this.params.getConstraintNameLength().intValue() > -1);
    }


    private boolean compareIndexNames(Index indexFromDbModel, Index indexFromDatabase, String tablePrefix)
    {
        String indexFromDbModelName = DDLGeneratorUtils.adjustForTablePrefix(indexFromDbModel.getName(), tablePrefix).toLowerCase(LocaleHelper.getPersistenceLocale());
        String adjustedIndexName = indexFromDbModelName;
        if(shouldShortNameOfIndex() && adjustedIndexName.length() > this.params.getConstraintNameLength().intValue())
        {
            adjustedIndexName = adjustedIndexName.substring(0, this.params.getConstraintNameLength().intValue());
        }
        return adjustedIndexName.equalsIgnoreCase(indexFromDatabase.getName());
    }


    private Map<String, Table> getDbModelTableMap(YDbModel dbModel, String tablePrefix)
    {
        ImmutableMap.Builder<String, Table> resultBuilder = ImmutableMap.builder();
        for(Table table : dbModel.getTables().values())
        {
            String adjustedTableNameWithPrefix = DDLGeneratorUtils.adjustForTablePrefix(table.getName(), tablePrefix).toLowerCase(LocaleHelper.getPersistenceLocale());
            String adjustedTableName = adjustedTableNameWithPrefix;
            if(shouldShortNameOfTable() && adjustedTableName.length() > this.params.getTableNameLength().intValue())
            {
                adjustedTableName = adjustedTableName.substring(0, this.params.getTableNameLength().intValue());
            }
            resultBuilder.put(adjustedTableName, table);
        }
        return (Map<String, Table>)resultBuilder.build();
    }


    private List<Pattern> preparePatterns(List<String> ignoredIndicesRegex)
    {
        List<Pattern> patternList = new ArrayList<>();
        for(String regex : ignoredIndicesRegex)
        {
            try
            {
                String fullRegex = adjustStartWithPrefixToRegexStartWith(regex);
                patternList.add(Pattern.compile(fullRegex, 2));
            }
            catch(Exception pse)
            {
                LOG.info(String.format("Skipping invalid pattern %s", new Object[] {regex}), pse);
            }
        }
        return patternList;
    }


    private String adjustStartWithPrefixToRegexStartWith(String startWith)
    {
        String regexStartWith = startWith.replace("*", ".*");
        return "^" + regexStartWith;
    }
}
