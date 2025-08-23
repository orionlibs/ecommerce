package de.hybris.platform.orderhandler;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DynamicAttributesOrderStatusDisplayByMap implements DynamicAttributeHandler<String, OrderModel>
{
    private Map<String, String> statusDisplayMap = new HashMap<>();
    private String defaultStatus;


    public Map<String, String> getStatusDisplayMap()
    {
        return (this.statusDisplayMap == null) ? Collections.<String, String>emptyMap() : this.statusDisplayMap;
    }


    public void setStatusDisplayMap(Map<String, String> statusDisplayMap)
    {
        this.statusDisplayMap = statusDisplayMap;
    }


    public String getDefaultStatus()
    {
        return this.defaultStatus;
    }


    public void setDefaultStatus(String defaultStatus)
    {
        this.defaultStatus = defaultStatus;
    }


    public String get(OrderModel order)
    {
        String statusLocalisationKey = getDefaultStatus();
        if(order != null && order.getStatus() != null && order.getStatus().getCode() != null)
        {
            String statusCode = order.getStatus().getCode();
            String statusDisplayEntry = getStatusDisplayMap().get(statusCode);
            if(statusDisplayEntry != null)
            {
                statusLocalisationKey = statusDisplayEntry;
            }
        }
        if(statusLocalisationKey == null || statusLocalisationKey.isEmpty())
        {
            return "";
        }
        return Localization.getLocalizedString(statusLocalisationKey);
    }


    public void set(OrderModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
