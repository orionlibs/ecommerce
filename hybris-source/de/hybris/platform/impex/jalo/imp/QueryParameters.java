package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryParameters
{
    private final String typeCode;
    private final Set<StandardColumnDescriptor> uniqueColumns;
    private final Map<StandardColumnDescriptor, Object> uniqueValues;
    private final boolean singleton;
    private Set<StandardColumnDescriptor> searchableColumns;
    private Set<StandardColumnDescriptor> nonSearchableColumns;
    private final Map<String, Object> parameters;
    private final String query;
    private final boolean isMySQl;
    private int hashCache = -1;


    public QueryParameters(String typeCode, boolean singleton, Set<StandardColumnDescriptor> uniqueColumns, Map<StandardColumnDescriptor, Object> values)
    {
        Objects.requireNonNull(values, "values mustn't be null");
        this.typeCode = typeCode;
        this.singleton = singleton;
        this.uniqueColumns = uniqueColumns;
        this.uniqueValues = values;
        this.parameters = new HashMap<>();
        this.isMySQl = isMySqlUsed();
        filterNonSearchableColumns();
        this.query = buildQuery();
    }


    boolean isMySqlUsed()
    {
        return Config.isMySQLUsed();
    }


    private void filterNonSearchableColumns()
    {
        for(StandardColumnDescriptor cd : this.uniqueColumns)
        {
            if(cd.getAttributeDescriptor().getDatabaseColumn() != null)
            {
                if(this.searchableColumns == null)
                {
                    this.searchableColumns = new HashSet<>(this.uniqueColumns.size());
                }
                this.searchableColumns.add(cd);
                continue;
            }
            if(cd.getAttributeDescriptor().isProperty())
            {
                Type type = cd.isLocalized() ? ((MapType)cd.getAttributeDescriptor().getRealAttributeType()).getReturnType() : cd.getAttributeDescriptor().getRealAttributeType();
                if(type instanceof de.hybris.platform.jalo.type.AtomicType || type instanceof de.hybris.platform.jalo.type.ComposedType)
                {
                    if(this.searchableColumns == null)
                    {
                        this.searchableColumns = new HashSet<>(this.uniqueColumns.size());
                    }
                    this.searchableColumns.add(cd);
                    continue;
                }
                if(this.nonSearchableColumns == null)
                {
                    this.nonSearchableColumns = new HashSet<>(this.uniqueColumns.size());
                }
                this.nonSearchableColumns.add(cd);
                continue;
            }
            if(this.nonSearchableColumns == null)
            {
                this.nonSearchableColumns = new HashSet<>(this.uniqueColumns.size());
            }
            this.nonSearchableColumns.add(cd);
        }
    }


    private String buildQuery()
    {
        return buildQueryForMultipleValues(Collections.singletonList(getUniqueValues()));
    }


    private String buildQueryForMultipleValues(List<Map<StandardColumnDescriptor, Object>> valuesList)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT {").append("pk").append("} FROM {").append(getTypeCode()).append("} ");
        appendWhereClause(sb, valuesList);
        return sb.toString();
    }


    private void appendWhereClause(StringBuilder query, List<Map<StandardColumnDescriptor, Object>> valuesList)
    {
        ParamNames paramNames = new ParamNames();
        Set<StandardColumnDescriptor> searchable = getSearchableColumns();
        if(searchable.isEmpty())
        {
            return;
        }
        query.append("WHERE ");
        if(valuesList.size() == 1)
        {
            query.append(getWherePartForValues(paramNames, searchable, valuesList.get(0)));
            return;
        }
        query.append("(").append(getWherePartForValues(paramNames, searchable, valuesList.get(0))).append(")");
        valuesList.subList(1, valuesList.size())
                        .forEach(values -> query.append(" OR (").append(getWherePartForValues(paramNames, searchable, values)).append(")"));
    }


    private String getWherePartForValues(ParamNames paramNames, Set<StandardColumnDescriptor> searchable, Map<StandardColumnDescriptor, Object> values)
    {
        return searchable.stream().map(desc -> getParamToValueSql(paramNames, desc, values.get(desc)))
                        .collect(Collectors.joining(" AND "));
    }


    private String getParamToValueSql(ParamNames paramNames, StandardColumnDescriptor desc, Object value)
    {
        String param = desc.getQualifier();
        if(value == null)
        {
            return "{" + param + "} IS NULL";
        }
        boolean caseInsensitiveString = desc.isCaseInsensitiveStringAttribute();
        String languageIsoCode = desc.getLanguageIsoCode();
        String paramName = paramNames.getNameFor(param + param);
        StringBuilder sb = new StringBuilder();
        sb.append(caseInsensitiveString ? " LOWER(" : "").append("{").append(param);
        if(languageIsoCode != null)
        {
            sb.append("[").append(languageIsoCode).append("]");
        }
        sb.append("}").append(caseInsensitiveString ? ")" : "");
        sb.append(" = ");
        sb.append(caseInsensitiveString ? " LOWER( ?" : " ?").append(paramName);
        sb.append(caseInsensitiveString ? " ) " : "");
        this.parameters.put(paramName, value);
        return sb.toString();
    }


    public String getQuery()
    {
        return this.query;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }


    public Set<StandardColumnDescriptor> getUniqueColumns()
    {
        return this.uniqueColumns;
    }


    public Map<StandardColumnDescriptor, Object> getUniqueValues()
    {
        return this.uniqueValues;
    }


    public Set<StandardColumnDescriptor> getSearchableColumns()
    {
        return (this.searchableColumns != null) ? this.searchableColumns : Collections.EMPTY_SET;
    }


    Set<StandardColumnDescriptor> getNonSearchableColumns()
    {
        return (this.nonSearchableColumns != null) ? this.nonSearchableColumns : Collections.EMPTY_SET;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public boolean isSingleton()
    {
        return this.singleton;
    }


    public int hashCode()
    {
        if(this.hashCache == -1)
        {
            this.hashCache = (this.uniqueValues != null) ? this.uniqueValues.hashCode() : 0;
        }
        return this.hashCache;
    }


    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            return true;
        }
        try
        {
            QueryParameters other = (QueryParameters)obj;
            return this.uniqueValues.equals(other.uniqueValues);
        }
        catch(ClassCastException e)
        {
            return false;
        }
    }


    public static String buildQueryForValues(QueryParameters params, List<Map<StandardColumnDescriptor, Object>> valueList)
    {
        params.parameters.clear();
        return params.buildQueryForMultipleValues(valueList);
    }
}
