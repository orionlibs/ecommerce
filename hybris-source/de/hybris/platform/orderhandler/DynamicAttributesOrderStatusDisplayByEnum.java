package de.hybris.platform.orderhandler;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import org.springframework.beans.factory.annotation.Required;

public class DynamicAttributesOrderStatusDisplayByEnum implements DynamicAttributeHandler<String, OrderModel>
{
    private EnumerationService enumerationService;


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    public String get(OrderModel order)
    {
        String ret = "";
        if(order == null)
        {
            throw new IllegalArgumentException("Item model is required");
        }
        if(order.getStatus() == null)
        {
            return "";
        }
        return this.enumerationService.getEnumerationName((HybrisEnumValue)order.getStatus());
    }


    public void set(OrderModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
