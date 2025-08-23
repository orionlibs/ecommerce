package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Date;

public class ReturnEntryModel extends ItemModel
{
    public static final String _TYPECODE = "ReturnEntry";
    public static final String _RETURNREQUEST2RETURNENTRY = "ReturnRequest2ReturnEntry";
    public static final String ORDERENTRY = "orderEntry";
    public static final String EXPECTEDQUANTITY = "expectedQuantity";
    public static final String RECEIVEDQUANTITY = "receivedQuantity";
    public static final String REACHEDDATE = "reachedDate";
    public static final String STATUS = "status";
    public static final String ACTION = "action";
    public static final String NOTES = "notes";
    public static final String TAX = "tax";
    public static final String RETURNREQUESTPOS = "returnRequestPOS";
    public static final String RETURNREQUEST = "returnRequest";


    public ReturnEntryModel()
    {
    }


    public ReturnEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnEntryModel(ReturnAction _action, AbstractOrderEntryModel _orderEntry, ReturnStatus _status)
    {
        setAction(_action);
        setOrderEntry(_orderEntry);
        setStatus(_status);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnEntryModel(ReturnAction _action, AbstractOrderEntryModel _orderEntry, ItemModel _owner, ReturnStatus _status)
    {
        setAction(_action);
        setOrderEntry(_orderEntry);
        setOwner(_owner);
        setStatus(_status);
    }


    @Accessor(qualifier = "action", type = Accessor.Type.GETTER)
    public ReturnAction getAction()
    {
        return (ReturnAction)getPersistenceContext().getPropertyValue("action");
    }


    @Accessor(qualifier = "expectedQuantity", type = Accessor.Type.GETTER)
    public Long getExpectedQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("expectedQuantity");
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.GETTER)
    public String getNotes()
    {
        return (String)getPersistenceContext().getPropertyValue("notes");
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
    public AbstractOrderEntryModel getOrderEntry()
    {
        return (AbstractOrderEntryModel)getPersistenceContext().getPropertyValue("orderEntry");
    }


    @Accessor(qualifier = "reachedDate", type = Accessor.Type.GETTER)
    public Date getReachedDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("reachedDate");
    }


    @Accessor(qualifier = "receivedQuantity", type = Accessor.Type.GETTER)
    public Long getReceivedQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("receivedQuantity");
    }


    @Accessor(qualifier = "returnRequest", type = Accessor.Type.GETTER)
    public ReturnRequestModel getReturnRequest()
    {
        return (ReturnRequestModel)getPersistenceContext().getPropertyValue("returnRequest");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public ReturnStatus getStatus()
    {
        return (ReturnStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "tax", type = Accessor.Type.GETTER)
    public BigDecimal getTax()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("tax");
    }


    @Accessor(qualifier = "action", type = Accessor.Type.SETTER)
    public void setAction(ReturnAction value)
    {
        getPersistenceContext().setPropertyValue("action", value);
    }


    @Accessor(qualifier = "expectedQuantity", type = Accessor.Type.SETTER)
    public void setExpectedQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("expectedQuantity", value);
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.SETTER)
    public void setNotes(String value)
    {
        getPersistenceContext().setPropertyValue("notes", value);
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
    public void setOrderEntry(AbstractOrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntry", value);
    }


    @Accessor(qualifier = "reachedDate", type = Accessor.Type.SETTER)
    public void setReachedDate(Date value)
    {
        getPersistenceContext().setPropertyValue("reachedDate", value);
    }


    @Accessor(qualifier = "receivedQuantity", type = Accessor.Type.SETTER)
    public void setReceivedQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("receivedQuantity", value);
    }


    @Accessor(qualifier = "returnRequest", type = Accessor.Type.SETTER)
    public void setReturnRequest(ReturnRequestModel value)
    {
        getPersistenceContext().setPropertyValue("returnRequest", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(ReturnStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "tax", type = Accessor.Type.SETTER)
    public void setTax(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("tax", value);
    }
}
