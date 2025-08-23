package de.hybris.platform.persistence.audit.gateway.impl;

import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.AuditSqlQuery;
import de.hybris.platform.persistence.audit.gateway.AuditSqlQueryFactory;
import de.hybris.platform.persistence.audit.gateway.AuditStorageUtils;
import de.hybris.platform.persistence.audit.gateway.SearchRule;
import de.hybris.platform.util.Config;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultAuditSqlQueryFactory implements AuditSqlQueryFactory
{
    private static final EnumMap<Config.DatabaseName, QueryTemplate> templates = new EnumMap<>(Config.DatabaseName.class);

    static
    {
        templates.put(Config.DatabaseName.HANA, new QueryTemplate("SELECT * FROM {0} LIMIT ? OFFSET ?", "SELECT * FROM {0} WHERE {1} LIMIT ? OFFSET ?", ParamIndex.PENULTIMATE, ParamIndex.LAST));
    }

    private static final QueryTemplate defaultTemplates = new QueryTemplate("SELECT * FROM {0}", "SELECT * FROM {0} WHERE {1}", ParamIndex.NONE, ParamIndex.NONE);


    public AuditSqlQuery createSqlQuery(AuditSearchQuery searchQuery)
    {
        Config.DatabaseName dbName = Config.getDatabaseName();
        if(isLimitSizeForDBSetToMoreThanZero(dbName))
        {
            return createAuditSqlQuery(searchQuery, dbName);
        }
        return createAuditSqlQuery(searchQuery, null);
    }


    public AuditSqlQuery createStandardSqlQuery(AuditSearchQuery searchQuery)
    {
        return createAuditSqlQuery(searchQuery, null);
    }


    public AuditSqlQuery createAuditSqlQuery(AuditSearchQuery searchQuery, Config.DatabaseName dbName)
    {
        Object[] params = craftParamsForDB(craftParams(searchQuery), dbName);
        int limitIndex = indexPos(params, getLimitIndexForDB(dbName));
        int offsetIndex = indexPos(params, getOffsetIndexForDB(dbName));
        return new AuditSqlQuery(getQueryString(searchQuery, dbName), params, limitIndex, offsetIndex);
    }


    private String getQueryString(AuditSearchQuery searchQuery, Config.DatabaseName databaseName)
    {
        if(searchQuery.hasStandardSearchRules())
        {
            return MessageFormat.format(getQueryWhereTemplateForDB(databaseName), new Object[] {AuditStorageUtils.getAuditTableName(searchQuery.getTypeCode()), searchQuery
                            .getStandardSearchRules().stream().map(this::toSql).collect(Collectors.joining(" OR "))});
        }
        return MessageFormat.format(getQueryTemplateForDB(databaseName), new Object[] {AuditStorageUtils.getAuditTableName(searchQuery.getTypeCode())});
    }


    private String toSql(SearchRule<?> searchRule)
    {
        return searchRule.getFieldName() + "=?";
    }


    private Object[] craftParamsForDB(List<Object> params, Config.DatabaseName databaseName)
    {
        ParamIndex limitIndex = getLimitIndexForDB(databaseName);
        ParamIndex offsetIndex = getOffsetIndexForDB(databaseName);
        if(limitIndex.compareTo((Enum)offsetIndex) < 0)
        {
            addElementToList(params, getLimitSizeForDB(databaseName), limitIndex);
            addElementToList(params, 0, offsetIndex);
        }
        else
        {
            addElementToList(params, 0, offsetIndex);
            addElementToList(params, getLimitSizeForDB(databaseName), limitIndex);
        }
        return params.toArray();
    }


    private List<Object> craftParams(AuditSearchQuery searchQuery)
    {
        if(searchQuery.hasStandardSearchRules())
        {
            return (List<Object>)searchQuery.getStandardSearchRules().stream().map(SearchRule::getValue).collect(Collectors.toList());
        }
        return new ArrayList();
    }


    private void addElementToList(List<Object> listToAddTo, int element, ParamIndex position)
    {
        int size = listToAddTo.size();
        switch(null.$SwitchMap$de$hybris$platform$persistence$audit$gateway$impl$DefaultAuditSqlQueryFactory$ParamIndex[position.ordinal()])
        {
            case 1:
                listToAddTo.add(0, Integer.valueOf(element));
                break;
            case 2:
                listToAddTo.add((size > 0) ? 1 : 0, Integer.valueOf(element));
                break;
            case 3:
                listToAddTo.add((size > 0) ? (size - 1) : 0, Integer.valueOf(element));
                break;
            case 4:
                listToAddTo.add(Integer.valueOf(element));
                break;
        }
    }


    private int indexPos(Object[] params, ParamIndex position)
    {
        int size = params.length;
        switch(null.$SwitchMap$de$hybris$platform$persistence$audit$gateway$impl$DefaultAuditSqlQueryFactory$ParamIndex[position.ordinal()])
        {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return size - 2;
            case 4:
                return size - 1;
        }
        return -1;
    }


    private int getLimitSizeForDB(Config.DatabaseName databaseName)
    {
        if(databaseName != null)
        {
            return Config.getInt("audit.query.limit." + databaseName.getName(), -1);
        }
        return -1;
    }


    private boolean isLimitSizeForDBSetToMoreThanZero(Config.DatabaseName databaseName)
    {
        return (getLimitSizeForDB(databaseName) > 0);
    }


    private String getQueryWhereTemplateForDB(Config.DatabaseName databaseName)
    {
        return ((QueryTemplate)templates.getOrDefault(databaseName, defaultTemplates)).getWhereQuery();
    }


    private String getQueryTemplateForDB(Config.DatabaseName databaseName)
    {
        return ((QueryTemplate)templates.getOrDefault(databaseName, defaultTemplates)).getQuery();
    }


    private ParamIndex getOffsetIndexForDB(Config.DatabaseName databaseName)
    {
        return ((QueryTemplate)templates.getOrDefault(databaseName, defaultTemplates)).getOffsetIndex();
    }


    private ParamIndex getLimitIndexForDB(Config.DatabaseName databaseName)
    {
        return ((QueryTemplate)templates.getOrDefault(databaseName, defaultTemplates)).getLimitIndex();
    }
}
