package de.hybris.bootstrap.ddl.tools;

import java.util.Objects;

public abstract class SqlStatement
{
    private final String statement;


    protected SqlStatement(String statement)
    {
        this.statement = Objects.<String>requireNonNull(statement);
    }


    public String getStatement()
    {
        return this.statement;
    }


    public String toString()
    {
        return this.statement;
    }
}
