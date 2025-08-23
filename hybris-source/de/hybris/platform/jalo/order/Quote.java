package de.hybris.platform.jalo.order;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.Map;
import org.apache.log4j.Logger;

public class Quote extends GeneratedQuote
{
    private static final Logger LOG = Logger.getLogger(Quote.class.getName());


    @ForceJALO(reason = "abstract method implementation")
    protected String getAbstractOrderEntryTypeCode()
    {
        return "QuoteEntry";
    }


    protected QuoteEntry createNewEntry(SessionContext ctx, ComposedType entryType, Product product, long quantity, Unit unit, int position)
    {
        if(entryType != null && !QuoteEntry.class.isAssignableFrom(entryType.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type is not assignable from QuoteEntry", 0);
        }
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("order", this);
            params.put("entryNumber", Integer.valueOf(position));
            params.put("quantity", Long.valueOf(quantity));
            params.put("unit", unit);
            params.put("product", product);
            if(entryType == null)
            {
                entryType = TypeManager.getInstance().getComposedType(QuoteEntry.class);
            }
            return (QuoteEntry)entryType.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }
}
