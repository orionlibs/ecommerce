package com.hybris.datahub.dto.filter;

public enum ComparisonOperator
{
    gt(">"),
    lt("<"),
    gte(">="),
    lte("<=");
    private final String operatorString;


    ComparisonOperator(String operatorString)
    {
        this.operatorString = operatorString;
    }


    public static ComparisonOperator fromQueryString(String queryString)
    {
        int matchLength = 0;
        ComparisonOperator matchedOperator = null;
        for(ComparisonOperator op : values())
        {
            if(queryString.startsWith(op.getOperatorString()) && op.getOperatorString().length() > matchLength)
            {
                matchLength = op.getOperatorString().length();
                matchedOperator = op;
            }
        }
        return matchedOperator;
    }


    public static String[] operatorStringValues()
    {
        String[] values = new String[(values()).length];
        int i = 0;
        for(ComparisonOperator op : values())
        {
            values[i] = op.operatorString;
            i++;
        }
        return values;
    }


    public String getOperatorString()
    {
        return this.operatorString;
    }
}
