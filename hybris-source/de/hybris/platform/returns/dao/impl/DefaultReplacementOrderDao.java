package de.hybris.platform.returns.dao.impl;

import de.hybris.platform.basecommerce.enums.ReturnFulfillmentStatus;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.returns.dao.ReplacementOrderDao;
import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultReplacementOrderDao extends AbstractItemDao implements ReplacementOrderDao
{
    public ReplacementOrderModel getReplacementOrder(String rma)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("rma", rma);
        String query = "SELECT {order} FROM { ReturnRequest} WHERE { RMA}=?rma ORDER BY {" + Item.PK + "} ASC";
        List<ReplacementOrderModel> result = getFlexibleSearchService().search(query, params).getResult();
        return (result == null) ? null : result.iterator().next();
    }


    public ReplacementOrderModel createReplacementOrder(ReturnRequestModel request)
    {
        ReplacementOrderModel replacementOrder = (ReplacementOrderModel)getModelService().create(ReplacementOrderModel.class);
        replacementOrder.setFulfilmentStatus(ReturnFulfillmentStatus.INITIAL);
        if(request != null)
        {
            if(request.getOrder() != null)
            {
                replacementOrder.setCurrency(request.getOrder().getCurrency());
                replacementOrder.setDate(request.getOrder().getDate());
                replacementOrder.setNet(request.getOrder().getNet());
                replacementOrder.setUser(request.getOrder().getUser());
                replacementOrder.setDeliveryAddress(request.getOrder().getDeliveryAddress());
            }
            request.setReplacementOrder(replacementOrder);
        }
        getModelService().save(replacementOrder);
        return replacementOrder;
    }
}
