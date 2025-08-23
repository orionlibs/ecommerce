package de.hybris.platform.jalo.flexiblesearch.hints.impl;

import com.google.common.base.Joiner;
import de.hybris.platform.jalo.flexiblesearch.hints.QueryHint;
import de.hybris.platform.util.Config;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public final class SQLServerQueryHints implements QueryHint
{
    public static final String FLEXIBLESEARCH_SQL_SERVER_MAXDOP_HINT = "flexiblesearch.hints.sqlserver.maxdop";
    public static final int NO_MAXDOP_VALUE = -1;
    public static final int MAX_MAXDOP_VALUE = 32767;
    private final boolean isSQLServerUsed;
    private final Set<String> hints;
    private static final Joiner JOINER = Joiner.on(',');


    SQLServerQueryHints(boolean isSQLServerUsed)
    {
        this.isSQLServerUsed = isSQLServerUsed;
        this.hints = new LinkedHashSet<>();
    }


    public String apply(String query)
    {
        if(shouldApply())
        {
            return query + " OPTION ( " + query + " )";
        }
        return query;
    }


    public static SQLServerQueryHints create()
    {
        return new SQLServerQueryHints(Config.isSQLServerUsed());
    }


    public Set<String> getHints()
    {
        return this.hints;
    }


    public void addMaxDOPHint(int maxDOPValue)
    {
        if(maxDOPValue > -1 && maxDOPValue <= 32767)
        {
            if(sqlServerQueryHintsContainMaxDOP(this.hints))
            {
                overrideMaxDOPValue();
            }
            String maxDOPHint = "MAXDOP " + maxDOPValue;
            this.hints.add(maxDOPHint);
        }
    }


    private boolean sqlServerQueryHintsContainMaxDOP(Set<String> hints)
    {
        return hints.stream().anyMatch(s -> s.startsWith("MAXDOP"));
    }


    private void overrideMaxDOPValue()
    {
        Objects.requireNonNull(this.hints);
        this.hints.stream().filter(s -> s.startsWith("MAXDOP")).forEach(this.hints::remove);
    }


    private boolean shouldApply()
    {
        return (this.isSQLServerUsed && CollectionUtils.isNotEmpty(this.hints));
    }
}
