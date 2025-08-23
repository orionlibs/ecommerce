package de.hybris.platform.ordersplitting.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderEntryGroup extends ArrayList<AbstractOrderEntryModel>
{
    private final Map<String, Object> parameters = new HashMap<>();


    public Object getParameter(String paramName)
    {
        return this.parameters.get(paramName);
    }


    public void setParameter(String paramName, Object paramValue)
    {
        this.parameters.put(paramName, paramValue);
    }


    public OrderEntryGroup getEmpty()
    {
        OrderEntryGroup result = new OrderEntryGroup();
        result.parameters.putAll(this.parameters);
        return result;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        OrderEntryGroup that = (OrderEntryGroup)o;
        return this.parameters.equals(that.parameters);
    }


    public int hashCode()
    {
        return 31 * super.hashCode() + this.parameters.hashCode();
    }
}
