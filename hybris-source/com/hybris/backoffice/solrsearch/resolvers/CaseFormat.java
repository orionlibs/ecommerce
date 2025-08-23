package com.hybris.backoffice.solrsearch.resolvers;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class CaseFormat extends Format
{
    private final Case aCase;


    public CaseFormat(Case aCase)
    {
        this.aCase = aCase;
    }


    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        if(obj != null)
        {
            toAppendTo.append(this.aCase.apply(obj.toString()));
        }
        return toAppendTo;
    }


    public Object parseObject(String source, ParsePosition pos)
    {
        if(source != null)
        {
            pos.setIndex((source.length() > 0) ? source.length() : 1);
        }
        else
        {
            pos.setIndex(1);
        }
        return source;
    }
}
