package de.hybris.platform.paymentstandard.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.payment.JaloPaymentModeException;
import de.hybris.platform.paymentstandard.constants.GeneratedStandardPaymentModeConstants;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class StandardPaymentModeManager extends GeneratedStandardPaymentModeManager
{
    private static final Logger log = Logger.getLogger(StandardPaymentModeManager.class.getName());


    public static StandardPaymentModeManager getInstance()
    {
        return (StandardPaymentModeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("paymentstandard");
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        if(item instanceof Currency)
        {
            List<StandardPaymentModeValue> values = FlexibleSearch.getInstance().search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedStandardPaymentModeConstants.TC.STANDARDPAYMENTMODEVALUE + "} WHERE {currency}=?item ", Collections.singletonMap("item", item), StandardPaymentModeValue.class)
                            .getResult();
            for(StandardPaymentModeValue val : values)
            {
                try
                {
                    val.remove(ctx);
                }
                catch(ConsistencyCheckException e)
                {
                    log.error("could not remove standard payment mode value " + val.getPK() + " due to " + e.getMessage());
                    log.error(Utilities.getStackTraceAsString((Throwable)e));
                }
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public StandardPaymentModeValue setCost(StandardPaymentMode mode, Currency curr, double value)
    {
        return mode.addNewPaymentModeValue(curr, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean removeCost(StandardPaymentMode mode, Currency curr)
    {
        return mode.removeCost(curr);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getValues(StandardPaymentMode mode)
    {
        return mode.getValues();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setValues(StandardPaymentMode mode, Map values)
    {
        mode.setValues(values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setNet(StandardPaymentMode mode, boolean net)
    {
        mode.setNet(net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isNet(StandardPaymentMode mode)
    {
        return mode.isNetAsPrimitive();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PriceValue getCost(SessionContext ctx, StandardPaymentMode mode, AbstractOrder order) throws JaloPaymentModeException
    {
        return mode.getCost(ctx, order);
    }


    public boolean isCreatorDisabled()
    {
        return true;
    }


    public void createProjectData(Map params, JspContext jspc)
    {
    }


    public void createEssentialData(Map params, JspContext jspc)
    {
    }
}
