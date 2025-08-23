package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.directpersistence.BatchCollector;
import java.util.Arrays;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class BatchInfo implements BatchCollector.ResultCheck
{
    private final Object[] params;
    private final PreparedStatementSetter statementSetter;
    private final BatchCollector.ResultCheck resultCheck;


    public BatchInfo(Object[] params)
    {
        this(null, params, null);
    }


    public BatchInfo(PreparedStatementSetter statementSetter)
    {
        this(statementSetter, null, null);
    }


    public BatchInfo(PreparedStatementSetter statementSetter, BatchCollector.ResultCheck resultCheck)
    {
        this(statementSetter, null, resultCheck);
    }


    public BatchInfo(Object[] params, BatchCollector.ResultCheck resultCheck)
    {
        this(null, params, resultCheck);
    }


    private BatchInfo(PreparedStatementSetter statementSetter, Object[] params, BatchCollector.ResultCheck resultCheck)
    {
        this.statementSetter = statementSetter;
        this.params = params;
        this.resultCheck = resultCheck;
    }


    public Object[] getParams()
    {
        return this.params;
    }


    public PreparedStatementSetter getStatementSetter()
    {
        return this.statementSetter;
    }


    public boolean hasStatementSetter()
    {
        return (this.statementSetter != null);
    }


    public boolean hasResultCheck()
    {
        return (this.resultCheck != null);
    }


    public String toString()
    {
        return hasStatementSetter() ? this.statementSetter.toString() : Arrays.toString(this.params);
    }


    public void checkResult(int result)
    {
        if(hasResultCheck())
        {
            this.resultCheck.checkResult(result);
        }
    }


    public BatchCollector.ResultCheck getResultCheck()
    {
        return this.resultCheck;
    }
}
