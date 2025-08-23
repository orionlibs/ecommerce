package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class ReturnRequestModel extends ItemModel
{
    public static final String _TYPECODE = "ReturnRequest";
    public static final String _ORDER2RETURNREQUEST = "Order2ReturnRequest";
    public static final String CODE = "code";
    public static final String RMA = "RMA";
    public static final String REPLACEMENTORDER = "replacementOrder";
    public static final String CURRENCY = "currency";
    public static final String STATUS = "status";
    public static final String SUBTOTAL = "subtotal";
    public static final String TOTALTAX = "totalTax";
    public static final String RETURNLABEL = "returnLabel";
    public static final String RETURNFORM = "returnForm";
    public static final String RETURNWAREHOUSE = "returnWarehouse";
    public static final String ORDERPOS = "orderPOS";
    public static final String ORDER = "order";
    public static final String RETURNENTRIES = "returnEntries";
    public static final String RETURNPROCESS = "returnProcess";
    public static final String REFUNDDELIVERYCOST = "refundDeliveryCost";


    public ReturnRequestModel()
    {
    }


    public ReturnRequestModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnRequestModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnRequestModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "refundDeliveryCost", type = Accessor.Type.GETTER)
    public Boolean getRefundDeliveryCost()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("refundDeliveryCost");
    }


    @Accessor(qualifier = "replacementOrder", type = Accessor.Type.GETTER)
    public ReplacementOrderModel getReplacementOrder()
    {
        return (ReplacementOrderModel)getPersistenceContext().getPropertyValue("replacementOrder");
    }


    @Accessor(qualifier = "returnEntries", type = Accessor.Type.GETTER)
    public List<ReturnEntryModel> getReturnEntries()
    {
        return (List<ReturnEntryModel>)getPersistenceContext().getPropertyValue("returnEntries");
    }


    @Accessor(qualifier = "returnForm", type = Accessor.Type.GETTER)
    public MediaModel getReturnForm()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("returnForm");
    }


    @Accessor(qualifier = "returnLabel", type = Accessor.Type.GETTER)
    public MediaModel getReturnLabel()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("returnLabel");
    }


    @Accessor(qualifier = "returnProcess", type = Accessor.Type.GETTER)
    public Collection<ReturnProcessModel> getReturnProcess()
    {
        return (Collection<ReturnProcessModel>)getPersistenceContext().getPropertyValue("returnProcess");
    }


    @Accessor(qualifier = "returnWarehouse", type = Accessor.Type.GETTER)
    public WarehouseModel getReturnWarehouse()
    {
        return (WarehouseModel)getPersistenceContext().getPropertyValue("returnWarehouse");
    }


    @Accessor(qualifier = "RMA", type = Accessor.Type.GETTER)
    public String getRMA()
    {
        return (String)getPersistenceContext().getPropertyValue("RMA");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public ReturnStatus getStatus()
    {
        return (ReturnStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "subtotal", type = Accessor.Type.GETTER)
    public BigDecimal getSubtotal()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("subtotal");
    }


    @Accessor(qualifier = "totalTax", type = Accessor.Type.GETTER)
    public BigDecimal getTotalTax()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("totalTax");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "refundDeliveryCost", type = Accessor.Type.SETTER)
    public void setRefundDeliveryCost(Boolean value)
    {
        getPersistenceContext().setPropertyValue("refundDeliveryCost", value);
    }


    @Accessor(qualifier = "replacementOrder", type = Accessor.Type.SETTER)
    public void setReplacementOrder(ReplacementOrderModel value)
    {
        getPersistenceContext().setPropertyValue("replacementOrder", value);
    }


    @Accessor(qualifier = "returnEntries", type = Accessor.Type.SETTER)
    public void setReturnEntries(List<ReturnEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("returnEntries", value);
    }


    @Accessor(qualifier = "returnForm", type = Accessor.Type.SETTER)
    public void setReturnForm(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("returnForm", value);
    }


    @Accessor(qualifier = "returnLabel", type = Accessor.Type.SETTER)
    public void setReturnLabel(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("returnLabel", value);
    }


    @Accessor(qualifier = "returnProcess", type = Accessor.Type.SETTER)
    public void setReturnProcess(Collection<ReturnProcessModel> value)
    {
        getPersistenceContext().setPropertyValue("returnProcess", value);
    }


    @Accessor(qualifier = "returnWarehouse", type = Accessor.Type.SETTER)
    public void setReturnWarehouse(WarehouseModel value)
    {
        getPersistenceContext().setPropertyValue("returnWarehouse", value);
    }


    @Accessor(qualifier = "RMA", type = Accessor.Type.SETTER)
    public void setRMA(String value)
    {
        getPersistenceContext().setPropertyValue("RMA", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(ReturnStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "subtotal", type = Accessor.Type.SETTER)
    public void setSubtotal(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("subtotal", value);
    }


    @Accessor(qualifier = "totalTax", type = Accessor.Type.SETTER)
    public void setTotalTax(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("totalTax", value);
    }
}
