package de.hybris.platform.directpersistence.statement;

import de.hybris.platform.directpersistence.BatchCollector;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class StatementHolder
{
    private final String statement;
    private final Object[] params;
    private final PreparedStatementSetter setter;
    private final BatchCollector.ResultCheck resultCheck;


    public StatementHolder(String statement, Object... params)
    {
        this.statement = statement;
        this.params = params;
        this.setter = null;
        this.resultCheck = null;
    }


    public StatementHolder(String statement, Object[] params, BatchCollector.ResultCheck resultCheck)
    {
        this.statement = statement;
        this.params = params;
        this.setter = null;
        this.resultCheck = resultCheck;
    }


    public StatementHolder(String statement, PreparedStatementSetter setter)
    {
        this.statement = statement;
        this.params = null;
        this.setter = setter;
        this.resultCheck = null;
    }


    public StatementHolder(String statement, PreparedStatementSetter setter, BatchCollector.ResultCheck resultCheck)
    {
        this.statement = statement;
        this.params = null;
        this.setter = setter;
        this.resultCheck = resultCheck;
    }


    public String getStatement()
    {
        return this.statement;
    }


    public Object[] getParams()
    {
        return this.params;
    }


    public PreparedStatementSetter getSetter()
    {
        return this.setter;
    }


    public BatchCollector.ResultCheck getResultCheck()
    {
        return this.resultCheck;
    }


    public boolean isSetterBased()
    {
        return (this.setter != null);
    }


    public String toString()
    {
        return this.statement + " [" + this.statement + "]";
    }
}
