package de.hybris.bootstrap.ddl.sql;

import org.apache.ddlutils.model.IndexColumn;

public class DefaultFunctionalIndexColumn extends IndexColumn
{
    private final String functionName;


    public DefaultFunctionalIndexColumn(String functionName)
    {
        this.functionName = functionName;
    }


    public String getFunctionName()
    {
        return this.functionName;
    }
}
