package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jalo.flexiblesearch.hints.PreparedStatementHint;
import de.hybris.platform.jalo.flexiblesearch.hints.QueryHint;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public final class HintsApplier
{
    public static PreparedStatement filterAndApplyPreparedStatementHints(PreparedStatement statement, List<Hint> hints)
    {
        if(hints == null)
        {
            return statement;
        }
        Objects.requireNonNull(PreparedStatementHint.class);
        Objects.requireNonNull(PreparedStatementHint.class);
        return (PreparedStatement)hints.stream().filter(PreparedStatementHint.class::isInstance).map(PreparedStatementHint.class::cast)
                        .reduce(statement, (s, h) -> (PreparedStatement)sqlExAware(()), (s1, s2) -> s2);
    }


    public static String filterAndApplyQueryHints(String query, List<Hint> hints)
    {
        if(hints == null)
        {
            return query;
        }
        Objects.requireNonNull(QueryHint.class);
        Objects.requireNonNull(QueryHint.class);
        return (String)hints.stream().filter(QueryHint.class::isInstance).map(QueryHint.class::cast)
                        .reduce(query, (q, hint) -> hint.apply(q), (v1, v2) -> v2);
    }


    private static <T> T sqlExAware(FlexibleSearchExecutor.SQLAwareSupplier<T> supplier)
    {
        try
        {
            return (T)supplier.get();
        }
        catch(SQLException ex)
        {
            throw new FlexibleSearchException(ex, ex.getMessage());
        }
    }
}
