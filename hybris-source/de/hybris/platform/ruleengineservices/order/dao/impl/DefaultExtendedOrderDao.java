package de.hybris.platform.ruleengineservices.order.dao.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.impl.DefaultOrderDao;
import de.hybris.platform.ruleengineservices.order.dao.ExtendedOrderDao;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;

public class DefaultExtendedOrderDao extends DefaultOrderDao implements ExtendedOrderDao
{
    public AbstractOrderModel findOrderByCode(String code)
    {
        AbstractOrderModel result;
        ServicesUtil.validateParameterNotNull(code, "Code must not be null");
        AbstractOrderModel example = new AbstractOrderModel();
        example.setCode(code);
        List<AbstractOrderModel> orders = getFlexibleSearchService().getModelsByExample(example);
        if(orders.isEmpty())
        {
            throw new ModelNotFoundException("Cannot find order/cart with code: " + code);
        }
        if(orders.size() == 1)
        {
            result = orders.get(0);
        }
        else
        {
            result = (AbstractOrderModel)orders.stream().filter(this::isOrderModelOriginal).findFirst().orElseThrow(() -> new ModelNotFoundException("Cannot find order/cart with code: " + code));
        }
        return result;
    }


    protected boolean isOrderModelOriginal(AbstractOrderModel abstractOrderModel)
    {
        boolean result;
        if(abstractOrderModel instanceof OrderModel)
        {
            OrderModel orderModel = (OrderModel)abstractOrderModel;
            result = (orderModel.getVersionID() == null);
        }
        else
        {
            result = false;
        }
        return result;
    }
}
