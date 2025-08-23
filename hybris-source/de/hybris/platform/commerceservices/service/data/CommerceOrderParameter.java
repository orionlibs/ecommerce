package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import java.io.Serializable;
import java.util.List;

public class CommerceOrderParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private AbstractOrderModel order;
    private List<String> additionalValues;


    public void setOrder(AbstractOrderModel order)
    {
        this.order = order;
    }


    public AbstractOrderModel getOrder()
    {
        return this.order;
    }


    public void setAdditionalValues(List<String> additionalValues)
    {
        this.additionalValues = additionalValues;
    }


    public List<String> getAdditionalValues()
    {
        return this.additionalValues;
    }
}
