package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class DateRestrictionModel extends RestrictionModel
{
    public static final String _TYPECODE = "DateRestriction";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";


    public DateRestrictionModel()
    {
    }


    public DateRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DateRestrictionModel(Date _endDate, Date _startDate, VoucherModel _voucher)
    {
        setEndDate(_endDate);
        setStartDate(_startDate);
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DateRestrictionModel(Date _endDate, ItemModel _owner, Date _startDate, VoucherModel _voucher)
    {
        setEndDate(_endDate);
        setOwner(_owner);
        setStartDate(_startDate);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
    public Date getEndDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("endDate");
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
    public Date getStartDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("startDate");
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
    public void setEndDate(Date value)
    {
        getPersistenceContext().setPropertyValue("endDate", value);
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
    public void setStartDate(Date value)
    {
        getPersistenceContext().setPropertyValue("startDate", value);
    }
}
