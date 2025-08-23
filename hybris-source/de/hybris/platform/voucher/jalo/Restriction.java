package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class Restriction extends GeneratedRestriction
{
    private static final Logger LOG = Logger.getLogger(Restriction.class.getName());


    private String format(String pattern)
    {
        String result = null;
        if(pattern != null)
        {
            result = MessageFormat.format(pattern, (Object[])getMessageAttributeValues());
        }
        return result;
    }


    public Map getAllRestrictionType(SessionContext ctx)
    {
        Map<Object, Object> result = new HashMap<>();
        SessionContext myctx = getSession().createSessionContext();
        for(Iterator<Language> iterator = getSession().getC2LManager().getAllLanguages().iterator(); iterator.hasNext(); )
        {
            Language language = iterator.next();
            myctx.setLanguage(language);
            String itemTypeName = getRestrictionType(myctx);
            if(itemTypeName != null)
            {
                result.put(language, itemTypeName);
            }
        }
        return result;
    }


    public VoucherEntrySet getApplicableEntries(AbstractOrder anOrder)
    {
        VoucherEntrySet entries = new VoucherEntrySet();
        if(isFulfilled(anOrder))
        {
            entries.addAll(anOrder.getAllEntries());
        }
        return entries;
    }


    public String getDescription(SessionContext ctx)
    {
        return format(super.getDescription(ctx));
    }


    public String getRestrictionType(SessionContext ctx)
    {
        return getComposedType().getName(ctx);
    }


    public final String getViolationMessage(SessionContext ctx)
    {
        return format(super.getViolationMessage(ctx));
    }


    protected String[] getMessageAttributeValues()
    {
        return new String[] {getRestrictionType()};
    }


    public final boolean isFulfilled(AbstractOrder anOrder)
    {
        return (anOrder != null && isFulfilledInternal(anOrder));
    }


    public final boolean isFulfilled(Product aProduct)
    {
        return (aProduct != null && isFulfilledInternal(aProduct));
    }


    protected abstract boolean isFulfilledInternal(AbstractOrder paramAbstractOrder);


    protected abstract boolean isFulfilledInternal(Product paramProduct);
}
