package de.hybris.platform.util.localization.jdbc;

import com.google.common.base.Preconditions;

public class StatementWithParams
{
    public static final StatementWithParams NONE = (StatementWithParams)new None();
    private final String statement;
    private final Object[] params;


    public StatementWithParams(String statement, Object... params)
    {
        Preconditions.checkNotNull(statement, "statement can't be null");
        Preconditions.checkNotNull(params, "params can't be null");
        this.statement = statement;
        this.params = params;
    }


    public String getStatement()
    {
        return this.statement;
    }


    public Object[] getParams()
    {
        return this.params;
    }


    public boolean isEmpty()
    {
        return false;
    }
}
