package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.enums.B2BBookingLineStatus;
import de.hybris.platform.b2b.enums.BookingType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Date;

public class B2BBookingLineEntryModel extends ItemModel
{
    public static final String _TYPECODE = "B2BBookingLineEntry";
    public static final String BOOKINGSTATUS = "bookingStatus";
    public static final String COSTCENTER = "costCenter";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String PRODUCT = "product";
    public static final String QUANTITY = "quantity";
    public static final String ORDERID = "orderID";
    public static final String ORDERENTRYNR = "orderEntryNr";
    public static final String ORDERENTRY = "orderEntry";
    public static final String BOOKINGDATE = "bookingDate";
    public static final String BOOKINGTYPE = "bookingType";


    public B2BBookingLineEntryModel()
    {
    }


    public B2BBookingLineEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BBookingLineEntryModel(BigDecimal _amount, Date _bookingDate, B2BBookingLineStatus _bookingStatus, B2BCostCenterModel _costCenter, CurrencyModel _currency, String _product, Long _quantity)
    {
        setAmount(_amount);
        setBookingDate(_bookingDate);
        setBookingStatus(_bookingStatus);
        setCostCenter(_costCenter);
        setCurrency(_currency);
        setProduct(_product);
        setQuantity(_quantity);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BBookingLineEntryModel(BigDecimal _amount, Date _bookingDate, B2BBookingLineStatus _bookingStatus, B2BCostCenterModel _costCenter, CurrencyModel _currency, ItemModel _owner, String _product, Long _quantity)
    {
        setAmount(_amount);
        setBookingDate(_bookingDate);
        setBookingStatus(_bookingStatus);
        setCostCenter(_costCenter);
        setCurrency(_currency);
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public BigDecimal getAmount()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "bookingDate", type = Accessor.Type.GETTER)
    public Date getBookingDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("bookingDate");
    }


    @Accessor(qualifier = "bookingStatus", type = Accessor.Type.GETTER)
    public B2BBookingLineStatus getBookingStatus()
    {
        return (B2BBookingLineStatus)getPersistenceContext().getPropertyValue("bookingStatus");
    }


    @Accessor(qualifier = "bookingType", type = Accessor.Type.GETTER)
    public BookingType getBookingType()
    {
        return (BookingType)getPersistenceContext().getPropertyValue("bookingType");
    }


    @Accessor(qualifier = "costCenter", type = Accessor.Type.GETTER)
    public B2BCostCenterModel getCostCenter()
    {
        return (B2BCostCenterModel)getPersistenceContext().getPropertyValue("costCenter");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
    public OrderEntryModel getOrderEntry()
    {
        return (OrderEntryModel)getPersistenceContext().getPropertyValue("orderEntry");
    }


    @Accessor(qualifier = "orderEntryNr", type = Accessor.Type.GETTER)
    public Integer getOrderEntryNr()
    {
        return (Integer)getPersistenceContext().getPropertyValue("orderEntryNr");
    }


    @Accessor(qualifier = "orderID", type = Accessor.Type.GETTER)
    public String getOrderID()
    {
        return (String)getPersistenceContext().getPropertyValue("orderID");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public String getProduct()
    {
        return (String)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Long getQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
    public void setAmount(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("amount", value);
    }


    @Accessor(qualifier = "bookingDate", type = Accessor.Type.SETTER)
    public void setBookingDate(Date value)
    {
        getPersistenceContext().setPropertyValue("bookingDate", value);
    }


    @Accessor(qualifier = "bookingStatus", type = Accessor.Type.SETTER)
    public void setBookingStatus(B2BBookingLineStatus value)
    {
        getPersistenceContext().setPropertyValue("bookingStatus", value);
    }


    @Accessor(qualifier = "bookingType", type = Accessor.Type.SETTER)
    public void setBookingType(BookingType value)
    {
        getPersistenceContext().setPropertyValue("bookingType", value);
    }


    @Accessor(qualifier = "costCenter", type = Accessor.Type.SETTER)
    public void setCostCenter(B2BCostCenterModel value)
    {
        getPersistenceContext().setPropertyValue("costCenter", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
    public void setOrderEntry(OrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntry", value);
    }


    @Accessor(qualifier = "orderEntryNr", type = Accessor.Type.SETTER)
    public void setOrderEntryNr(Integer value)
    {
        getPersistenceContext().setPropertyValue("orderEntryNr", value);
    }


    @Accessor(qualifier = "orderID", type = Accessor.Type.SETTER)
    public void setOrderID(String value)
    {
        getPersistenceContext().setPropertyValue("orderID", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(String value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }
}
