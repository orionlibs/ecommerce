package de.hybris.platform.directpersistence.statement.sql;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import java.util.Set;
import org.apache.log4j.Logger;

public class FluentSqlBuilder
{
    private static final Logger LOG = Logger.getLogger(FluentSqlBuilder.class);
    private static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();
    private static final String SPACE = " ";
    private static final String STAR = "*";
    private static final String PLACEHOLDER = "?";
    private static final String COMMA = ",";
    private static final String QUOTATION = "'";
    private static final String SELECT = "SELECT";
    private static final String UPDATE = "UPDATE";
    private static final String INSERT = "INSERT";
    private static final String DELETE = "DELETE";
    private static final String SET = "SET";
    private static final String FROM = "FROM";
    private static final String WHERE = "WHERE";
    private static final String IN = "IN";
    private static final String AS = "AS";
    private static final String INTO = "INTO";
    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final String VALUES = "VALUES";
    private static final String GROUP_BY = "GROUP BY";
    private static final String ORDER_BY = "ORDER BY";
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    private static final String UNION = "UNION";
    private static final String UNION_ALL = "UNION ALL";
    private static final String EQUALS = "=";
    private static final String NOT_EQUALS = "!=";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String MAX = "max";
    private static final String DISTINCT = "DISTINCT";
    private static final String COL_NAMES_PLACEHOLDER = "%%COL_NAMES%%";
    private static final String IS_NULL = "IS NULL";
    private static final String IS_NOT_NULL = "IS NOT NULL";
    private final StringBuilder sb = new StringBuilder();


    public static FluentSqlBuilder genericBuilder()
    {
        return new FluentSqlBuilder();
    }


    public static FluentSqlBuilder builder(Config.DatabaseName dbName)
    {
        if(Config.DatabaseName.ORACLE == dbName)
        {
            return (FluentSqlBuilder)new OracleSqlBuilder();
        }
        return genericBuilder();
    }


    public FluentSqlBuilder select(String... columnNames)
    {
        Preconditions.checkArgument((columnNames != null), "at least one field is required");
        this.sb.append("SELECT").append(" ");
        this.sb.append(COMMA_JOINER.join((Object[])columnNames)).append(" ");
        return this;
    }


    public FluentSqlBuilder selectAll()
    {
        return select(new String[] {"*"});
    }


    public FluentSqlBuilder selectDistinct(String... columnNames)
    {
        select(columnNames);
        this.sb.insert("SELECT".length() + 1, "DISTINCT ");
        return this;
    }


    public FluentSqlBuilder delete()
    {
        this.sb.append("DELETE").append(" ");
        return this;
    }


    public FluentSqlBuilder insert()
    {
        this.sb.append("INSERT").append(" ");
        return this;
    }


    public FluentSqlBuilder update(String tableName)
    {
        this.sb.append("UPDATE").append(" ").append(tableName).append(" ");
        return this;
    }


    public FluentSqlBuilder set(Set<String> columnNames)
    {
        this.sb.append("SET").append(" ");
        this.sb.append(COMMA_JOINER.join(Iterables.transform(columnNames, (Function)new Object(this))));
        this.sb.append(" ");
        return this;
    }


    public FluentSqlBuilder set(Iterable<String> columnNames)
    {
        this.sb.append("SET").append(" ");
        this.sb.append(COMMA_JOINER.join(Iterables.transform(columnNames, (Function)new Object(this))));
        this.sb.append(" ");
        return this;
    }


    public FluentSqlBuilder set(String... columnNames)
    {
        return set(Arrays.asList(columnNames));
    }


    public FluentSqlBuilder into(String tableName)
    {
        this.sb.append("INTO").append(" ");
        this.sb.append(tableName).append(" ");
        this.sb.append("(").append("%%COL_NAMES%%");
        this.sb.append(")").append(" ");
        return this;
    }


    public FluentSqlBuilder fieldIsNullOr(String columnName)
    {
        this.sb.append(" ").append("(");
        this.sb.append(columnName).append(" ").append("IS NULL");
        this.sb.append(" ").append("OR").append(" ").append(columnName).append("=").append("?").append(" ");
        this.sb.append(")").append(" ");
        return this;
    }


    public FluentSqlBuilder values(Set<? extends Object> values)
    {
        this.sb.append("VALUES").append(" ");
        this.sb.append("(").append(placeholders(values).getPlaceholders());
        this.sb.append(")");
        return this;
    }


    public FluentSqlBuilder usingFields(String... columnNames)
    {
        return usingFields(Arrays.asList(columnNames));
    }


    public FluentSqlBuilder usingFields(Iterable<String> columnNames)
    {
        int indexOf = this.sb.indexOf("%%COL_NAMES%%");
        this.sb.replace(indexOf, indexOf + "%%COL_NAMES%%".length(), COMMA_JOINER.join(columnNames));
        return this;
    }


    public FluentSqlBuilder from(String tableName)
    {
        this.sb.append("FROM").append(" ");
        this.sb.append(tableName).append(" ");
        return this;
    }


    public FluentSqlBuilder from(FluentSqlBuilder subQuery)
    {
        this.sb.append("FROM").append(" ").append("(");
        this.sb.append(subQuery.toSql()).append(")");
        return this;
    }


    public FluentSqlBuilder as(String as)
    {
        this.sb.append(" ").append("AS").append(" ").append(as).append(" ");
        return this;
    }


    public FluentSqlBuilder where()
    {
        this.sb.append("WHERE").append(" ");
        return this;
    }


    public FluentSqlBuilder field(String field)
    {
        this.sb.append(field);
        return this;
    }


    public FluentSqlBuilder field(ServiceCol field)
    {
        this.sb.append(field.colName());
        return this;
    }


    public FluentSqlBuilder isEqual()
    {
        this.sb.append("=").append(placeholder().getPlaceholders());
        return this;
    }


    public FluentSqlBuilder isNotEqual()
    {
        this.sb.append("!=").append(placeholder().getPlaceholders());
        return this;
    }


    public FluentSqlBuilder isNull()
    {
        this.sb.append(" ").append("IS NULL").append(" ");
        return this;
    }


    public FluentSqlBuilder isNotNull()
    {
        this.sb.append(" ").append("IS NOT NULL").append(" ");
        return this;
    }


    public FluentSqlBuilder and()
    {
        this.sb.append(" ").append("AND").append(" ");
        return this;
    }


    public FluentSqlBuilder or()
    {
        this.sb.append(" ").append("OR").append(" ");
        return this;
    }


    public FluentSqlBuilder in(Set<? extends Object> values)
    {
        return in(values);
    }


    public FluentSqlBuilder in(Iterable<? extends Object> values)
    {
        Placeholders placeholders = placeholders(values);
        this.sb.append(" ").append("IN").append(" ");
        this.sb.append("(").append(placeholders.getPlaceholders());
        this.sb.append(")");
        return this;
    }


    public FluentSqlBuilder groupBy(String... columnNames)
    {
        this.sb.append(" ").append("GROUP BY").append(" ");
        this.sb.append(COMMA_JOINER.join((Object[])columnNames));
        return this;
    }


    public FluentSqlBuilder orderBy(String... columnNames)
    {
        this.sb.append(" ").append("ORDER BY").append(" ");
        this.sb.append(COMMA_JOINER.join((Object[])columnNames));
        return this;
    }


    public FluentSqlBuilder ascending()
    {
        this.sb.append("ASC").append(" ");
        return this;
    }


    public FluentSqlBuilder descending()
    {
        this.sb.append("DESC").append(" ");
        return this;
    }


    public FluentSqlBuilder union(FluentSqlBuilder builder)
    {
        this.sb.append(" ").append("UNION").append(" ").append(builder.toSql());
        return this;
    }


    public FluentSqlBuilder unionAll(FluentSqlBuilder builder)
    {
        this.sb.append(" ").append("UNION ALL").append(" ").append(builder.toSql());
        return this;
    }


    public static String max(String columnName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("max").append("(").append(columnName).append(")");
        return sb.toString();
    }


    public FluentSqlBuilder join(String tableName)
    {
        this.sb.append(" ").append("JOIN").append(" ").append(tableName);
        return this;
    }


    public FluentSqlBuilder left()
    {
        this.sb.append(" ").append("LEFT").append(" ");
        return this;
    }


    public FluentSqlBuilder outer()
    {
        this.sb.append(" ").append("OUTER").append(" ");
        return this;
    }


    public FluentSqlBuilder on(String onPhrase)
    {
        this.sb.append(" ").append("ON").append(" ").append(onPhrase).append(" ");
        return this;
    }


    public String toSql()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Generated SQL: " + this.sb.toString());
        }
        return this.sb.toString().trim();
    }


    private Placeholders placeholders(Iterable<? extends Object> params)
    {
        return new Placeholders(Iterables.size(params));
    }


    private Placeholder placeholder()
    {
        return new Placeholder();
    }


    protected StringBuilder getInternalStringBuilder()
    {
        return this.sb;
    }
}
