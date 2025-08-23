package de.hybris.platform.jalo.flexiblesearch.hints.impl;

public final class JdbcHints
{
    public static DefaultPreparedStatementHints preparedStatementHints()
    {
        return new DefaultPreparedStatementHints(ps -> ps);
    }
}
