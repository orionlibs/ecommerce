package de.hybris.platform.productcockpit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;
import org.zkoss.zul.Comboitem;

public class ComboboxNumberConverter implements TypeConverter
{
    private static final Logger log = LoggerFactory.getLogger(ComboboxNumberConverter.class);


    public Object coerceToBean(Object val, Component comp)
    {
        Object o = null;
        if(val instanceof Comboitem)
        {
            try
            {
                Comboitem ci = (Comboitem)val;
                o = Integer.valueOf(Integer.parseInt(ci.getValue().toString()));
            }
            catch(NumberFormatException nfe)
            {
                log.error("Value can not be parsed as integer (class='" + val.getClass().getCanonicalName() + "')");
            }
        }
        else
        {
            log.error("Invalid value. Expected: 'Comboitem', Found: '" + val + "'.");
            o = TypeConverter.IGNORE;
        }
        return o;
    }


    public Object coerceToUi(Object val, Component comp)
    {
        Object o = null;
        if(val != null)
        {
            Comboitem ci = new Comboitem(val.toString());
            ci.setValue(val.toString());
            o = ci;
        }
        return o;
    }
}
