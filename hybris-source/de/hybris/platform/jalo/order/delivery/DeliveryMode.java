package de.hybris.platform.jalo.order.delivery;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.PriceValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DeliveryMode extends GeneratedDeliveryMode
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String code = (String)allAttributes.get("code");
        if(code == null)
        {
            throw new JaloInvalidParameterException("Missing parameter( code) to create a DeliveryMode", 0);
        }
        if(!checkConsistency(ctx, code))
        {
            throw new ConsistencyCheckException(null, "delivery mode code \"" + code + "\" is already used", 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllNames(SessionContext ctx)
    {
        return getAllName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllNames(SessionContext ctx, Map names)
    {
        setAllName(ctx, names);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllDescriptions(SessionContext ctx)
    {
        return getAllDescription(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllDescriptions(SessionContext ctx, Map descriptions)
    {
        setAllDescription(ctx, descriptions);
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(String code) throws ConsistencyCheckException
    {
        setCode(getSession().getSessionContext(), code);
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String code) throws ConsistencyCheckException
    {
        if(!checkConsistency(ctx, code))
        {
            throw new ConsistencyCheckException(null, "Duplicate code '" + code + "' of DeliveryMode.", 0);
        }
        super.setCode(ctx, code);
    }


    private boolean checkConsistency(SessionContext ctx, String code)
    {
        List<DeliveryMode> result = FlexibleSearch.getInstance().search(ctx, "SELECT {PK} FROM {DeliveryMode} WHERE {code}=?code", Collections.singletonMap("code", code), DeliveryMode.class).getResult();
        return result.isEmpty();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PriceValue getCost(SessionContext ctx, AbstractOrder order) throws JaloDeliveryModeException
    {
        return new PriceValue(order.getCurrency().getIsoCode(), 0.0D, order.isNet().booleanValue());
    }


    public PriceValue getCost(AbstractOrder order) throws JaloDeliveryModeException
    {
        return getCost(getSession().getSessionContext(), order);
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<PaymentMode> getSupportedPaymentModes(SessionContext ctx)
    {
        Collection<PaymentMode> modes = (Collection<PaymentMode>)WrapperFactory.wrap(
                        EJBTools.instantiateCommaSeparatedPKString(
                                        getSupportedPaymentModesInternal(ctx)));
        return (modes != null) ? modes : new HashSet<>();
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setSupportedPaymentModes(SessionContext ctx, Collection paymentModes)
    {
        String modes = EJBTools.getPKCollectionString(paymentModes);
        setSupportedPaymentModesInternal(ctx, modes);
    }


    public void addSupportedPaymentMode(PaymentMode paymentMode)
    {
        Collection<PaymentMode> modes = getSupportedPaymentModes();
        boolean changed = modes.add(paymentMode);
        if(changed)
        {
            setSupportedPaymentModesInternal(EJBTools.getPKCollectionString(modes));
        }
    }


    public void removeSupportedPaymentMode(PaymentMode paymentMode)
    {
        Collection modes = getSupportedPaymentModes();
        boolean changed = modes.remove(paymentMode);
        if(changed)
        {
            setSupportedPaymentModesInternal(EJBTools.getPKCollectionString(modes));
        }
    }


    public boolean isSupportedPaymentMode(PaymentMode paymentMode)
    {
        return getSupportedPaymentModes().contains(paymentMode);
    }


    @ForceJALO(reason = "something else")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        String query = "GET {" + GeneratedCoreConstants.TC.ABSTRACTORDER + "} WHERE {deliveryMode}=?mode";
        Map<String, Object> params = new HashMap<>();
        params.put("mode", getPK());
        Collection<AbstractOrder> orders = FlexibleSearch.getInstance().search(query, params, AbstractOrder.class).getResult();
        if(!orders.isEmpty())
        {
            throw new ConsistencyCheckException(null, "delivery mode " + this + " can not be removed since it is still used in orders", -1);
        }
    }
}
