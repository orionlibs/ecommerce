package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.jalo.util.DateTimeUtils;
import java.text.DateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class DateRestriction extends GeneratedDateRestriction
{
    private static final Logger LOG = Logger.getLogger(DateRestriction.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("startDate"))
        {
            throw new JaloBusinessException("missing startDate - cannot create item DateRestriction!");
        }
        if(!(allAttributes.get("startDate") instanceof Date))
        {
            throw new JaloBusinessException("startDate is NULL or not a Date");
        }
        if(!allAttributes.containsKey("endDate"))
        {
            throw new JaloBusinessException("missing endDate - cannot create item DateRestriction!");
        }
        if(!(allAttributes.get("endDate") instanceof Date))
        {
            throw new JaloBusinessException("endDate is NULL or not a Date");
        }
        Date startDate = (Date)allAttributes.get("startDate");
        Date endDate = (Date)allAttributes.get("endDate");
        validateStartEndDate(startDate, endDate);
        return super.createItem(ctx, type, allAttributes);
    }


    public void setStartDate(Date value)
    {
        super.setStartDate(value);
        validateStartEndDate(value, getEndDate());
    }


    public void setEndDate(Date value)
    {
        super.setEndDate(value);
        validateStartEndDate(getStartDate(), value);
    }


    protected void validateStartEndDate(Date startDate, Date endDate)
    {
        if(startDate != null && endDate != null && endDate.before(startDate))
        {
            String l13nMessage = Localization.getLocalizedString("type.daterestriction.validation.endstartdate");
            throw new JaloInvalidParameterException(l13nMessage, 0);
        }
    }


    protected String[] getMessageAttributeValues()
    {
        DateFormat dateFormat = DateTimeUtils.getDateFormat(3);
        String startdatestring = (getStartDate() != null) ? dateFormat.format(getStartDate()) : "n/a";
        String enddatestring = (getEndDate() != null) ? dateFormat.format(getEndDate()) : "n/a";
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()), startdatestring, enddatestring};
    }


    protected boolean isFulfilledInternal(AbstractOrder anOrder)
    {
        Date currentDate = new Date();
        Date startDate = getStartDate();
        Date endDate = getEndDate();
        boolean start = (startDate == null || startDate.before(currentDate) == isPositiveAsPrimitive());
        boolean end = (endDate == null || endDate.after(currentDate) == isPositiveAsPrimitive());
        return (start && end);
    }


    protected boolean isFulfilledInternal(Product aProduct)
    {
        return true;
    }
}
