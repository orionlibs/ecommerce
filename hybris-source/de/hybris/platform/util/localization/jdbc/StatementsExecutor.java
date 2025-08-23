package de.hybris.platform.util.localization.jdbc;

public interface StatementsExecutor
{
    void execute(Iterable<StatementWithParams> paramIterable);


    void flush();
}
