package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;

public abstract class SingleValueTranslator extends AbstractValueTranslator
{
    protected boolean isEmpty(String expr)
    {
        return (expr == null || expr.length() == 0);
    }


    protected Object getEmptyValue()
    {
        return null;
    }


    public final Object importValue(String valueExpr, Item toItem) throws JaloInvalidParameterException
    {
        Object ret;
        clearStatus();
        if(isEmpty(valueExpr))
        {
            ret = getEmptyValue();
            setEmpty();
        }
        else
        {
            ret = convertToJalo(valueExpr, toItem);
        }
        return ret;
    }


    public final String exportValue(Object value) throws JaloInvalidParameterException
    {
        clearStatus();
        return (value == null) ? "" : convertToString(value);
    }


    protected abstract Object convertToJalo(String paramString, Item paramItem);


    protected abstract String convertToString(Object paramObject);
}
