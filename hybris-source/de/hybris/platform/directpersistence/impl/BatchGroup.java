package de.hybris.platform.directpersistence.impl;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BatchGroup
{
    private final List<BatchInfo> batchInfos = new ArrayList<>();
    private Boolean statementSetters;
    private Boolean checkResult;


    public void addBatchInfo(BatchInfo info)
    {
        this.statementSetters = checkBoolean(this.statementSetters, info.hasStatementSetter());
        this.checkResult = checkBoolean(this.checkResult, info.hasResultCheck());
        this.batchInfos.add(info);
    }


    private Boolean checkBoolean(Boolean value, boolean input)
    {
        Preconditions.checkArgument((value == null || value.booleanValue() == input), "Inconsistent usage of sql for batch, cannot mix statementSetter and parameters with the same sql statement");
        return Boolean.valueOf(input);
    }


    public List<BatchInfo> getBatchInfos()
    {
        return this.batchInfos;
    }


    public boolean isSettersBased()
    {
        return Boolean.TRUE.equals(this.statementSetters);
    }


    public boolean isCheckResult()
    {
        return Boolean.TRUE.equals(this.checkResult);
    }


    public void checkResult(int[] result)
    {
        if(isCheckResult())
        {
            int i = 0;
            for(BatchInfo info : this.batchInfos)
            {
                info.checkResult(result[i++]);
            }
        }
    }


    public List<Object[]> getParams()
    {
        if(isSettersBased())
        {
            return Collections.EMPTY_LIST;
        }
        List<Object[]> result = new ArrayList();
        for(BatchInfo info : this.batchInfos)
        {
            result.add(info.getParams());
        }
        return result;
    }


    public int batchSize()
    {
        return this.batchInfos.size();
    }


    public String toString()
    {
        return this.batchInfos.toString();
    }
}
