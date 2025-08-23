package de.hybris.bootstrap.ddl.adjusters;

import de.hybris.bootstrap.ddl.model.YDbModel;
import java.util.Set;
import org.apache.ddlutils.model.Database;

public class RemoveIndicesParams
{
    private final Database database;
    private final YDbModel dbModel;
    private final String tablePrefix;
    private final Integer tableNameLength;
    private final Integer constraintNameLength;
    private String ignoredIndicesStr;
    private Boolean recreateIndicesFoundInItems;
    private Set<String> indicesNamesToIgnoreInDbModel;
    private Set<String> indicesRegexToIgnoreInDbModel;
    private Boolean dropIndicesFoundInItems;


    private RemoveIndicesParams(Database database, YDbModel dbModel, String ignoredIndicesStr, Boolean recreateIndicesFoundInItems, String tablePrefix, Integer tableNameLength, Integer constraintNameLength)
    {
        this.database = database;
        this.dbModel = dbModel;
        this.ignoredIndicesStr = ignoredIndicesStr;
        this.recreateIndicesFoundInItems = recreateIndicesFoundInItems;
        this.tablePrefix = tablePrefix;
        this.tableNameLength = tableNameLength;
        this.constraintNameLength = constraintNameLength;
    }


    private RemoveIndicesParams(Database database, YDbModel dbModel, Set<String> indicesNames, Set<String> indicesRegex, String tablePrefix, Integer tableNameLength, Integer constraintNameLength, Boolean dropIndicesFoundInItems)
    {
        this.database = database;
        this.dbModel = dbModel;
        this.tablePrefix = tablePrefix;
        this.tableNameLength = tableNameLength;
        this.constraintNameLength = constraintNameLength;
        this.indicesNamesToIgnoreInDbModel = indicesNames;
        this.indicesRegexToIgnoreInDbModel = indicesRegex;
        this.dropIndicesFoundInItems = dropIndicesFoundInItems;
    }


    public Database getDatabase()
    {
        return this.database;
    }


    public YDbModel getDbModel()
    {
        return this.dbModel;
    }


    public Set<String> getIndicesNamesToIgnoreInDbModel()
    {
        return this.indicesNamesToIgnoreInDbModel;
    }


    public Set<String> getIndicesRegexToIgnoreInDbModel()
    {
        return this.indicesRegexToIgnoreInDbModel;
    }


    public String getIgnoredIndicesStr()
    {
        return this.ignoredIndicesStr;
    }


    public Boolean isRecreateIndicesFoundInItems()
    {
        return this.recreateIndicesFoundInItems;
    }


    public Boolean isDropIndicesFoundInItems()
    {
        return this.dropIndicesFoundInItems;
    }


    public String getTablePrefix()
    {
        return this.tablePrefix;
    }


    public Integer getTableNameLength()
    {
        return this.tableNameLength;
    }


    public Integer getConstraintNameLength()
    {
        return this.constraintNameLength;
    }


    public static RemoveIndicesWithSpecificPrefixesParamsBuilder builder()
    {
        return new RemoveIndicesWithSpecificPrefixesParamsBuilder();
    }


    public static RemoveConfiguredIndicesFromDbModelParamsBuilder builderForIgnoreIndicesInItems()
    {
        return new RemoveConfiguredIndicesFromDbModelParamsBuilder();
    }
}
