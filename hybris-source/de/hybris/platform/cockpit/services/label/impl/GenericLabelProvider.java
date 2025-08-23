package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultValueHandler;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.ObjectLabelProvider;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenericLabelProvider implements ObjectLabelProvider
{
    private static final String DELIMITER = " - ";
    private final List<ValueHandler> valueHandlers;
    private final List<PropertyDescriptor> propertyDescriptors;


    public GenericLabelProvider(List<PropertyDescriptor> propertyDescriptors)
    {
        List<ValueHandler> valueHandlers = new ArrayList<>(propertyDescriptors.size());
        this.propertyDescriptors = propertyDescriptors;
        for(PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            valueHandlers.add(new DefaultValueHandler(propertyDescriptor));
        }
        this.valueHandlers = valueHandlers;
    }


    public List<PropertyDescriptor> getPropertyDescriptors()
    {
        return this.propertyDescriptors;
    }


    public String getLabel(TypedObject typeObject)
    {
        return getLabel(typeObject, null);
    }


    public String getLabel(TypedObject typeObject, String languageIso)
    {
        StringBuffer buf = new StringBuffer();
        Iterator<ValueHandler> itr = this.valueHandlers.iterator();
        while(itr.hasNext())
        {
            Object value;
            ValueHandler valueHandler = itr.next();
            try
            {
                if(languageIso == null || languageIso.length() == 0)
                {
                    value = valueHandler.getValue(typeObject);
                }
                else
                {
                    value = valueHandler.getValue(typeObject, languageIso);
                }
            }
            catch(ValueHandlerException e)
            {
                value = null;
            }
            if(value != null)
            {
                buf.append(value.toString());
            }
            else
            {
                buf.append("<null>");
            }
            if(itr.hasNext())
            {
                buf.append(" - ");
            }
        }
        return buf.toString();
    }


    public String getDescription(TypedObject object)
    {
        return "";
    }


    public String getDescription(TypedObject object, String languageIso)
    {
        return "";
    }


    public String getIconPath(TypedObject object)
    {
        return null;
    }


    public String getIconPath(TypedObject object, String languageIso)
    {
        return null;
    }
}
