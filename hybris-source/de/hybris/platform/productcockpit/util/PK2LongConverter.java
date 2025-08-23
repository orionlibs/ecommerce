package de.hybris.platform.productcockpit.util;

import de.hybris.platform.core.PK;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

public class PK2LongConverter implements TypeConverter
{
    public Object coerceToBean(Object val, Component comp)
    {
        String s = (String)val;
        PK ret = null;
        if(s != null)
        {
            s = s.trim();
            int split = s.indexOf('[');
            try
            {
                ret = PK.fromLong(Long.parseLong((split > -1) ? s.substring(0, split).trim() : s));
            }
            catch(NumberFormatException e)
            {
                System.err.println("wrong pk '" + s + "' - expected <long> or <long> [ <typecode> ]");
            }
        }
        return ret;
    }


    public Object coerceToUi(Object val, Component comp)
    {
        return (val != null) ? (((PK)val).toString() + " [" + ((PK)val).toString() + "]") : null;
    }
}
