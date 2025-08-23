package de.hybris.platform.jalo.order.payment;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.PriceValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentMode extends GeneratedPaymentMode
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String code = (String)allAttributes.get("code");
        if(code == null)
        {
            throw new JaloInvalidParameterException("Missing parameter( code) to create a PaymentMode", 0);
        }
        if(!checkConsistency(ctx, code))
        {
            throw new ConsistencyCheckException(null, "PaymentMode code \"" + code + "\" is already used", 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("paymentInfoType", Item.AttributeMode.INITIAL);
        if(!allAttributes.containsKey("code") || !allAttributes.containsKey("paymentInfoType"))
        {
            throw new JaloInvalidParameterException("Missing parameter(s) to create a PaymentMode", 0);
        }
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
            throw new ConsistencyCheckException(null, "PaymentMode code \"" + code + "\" is already used", 0);
        }
        super.setCode(ctx, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PriceValue getCost(SessionContext ctx, AbstractOrder order) throws JaloPaymentModeException
    {
        return new PriceValue(order.getCurrency().getIsoCode(), 0.0D, order.isNet().booleanValue());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PriceValue getCost(AbstractOrder order) throws JaloPaymentModeException
    {
        return getCost(getSession().getSessionContext(), order);
    }


    public void addSupportedDeliveryMode(DeliveryMode deliveryMode)
    {
        deliveryMode.addSupportedPaymentMode(this);
    }


    public void removeSupportedDeliveryMode(DeliveryMode deliveryMode)
    {
        deliveryMode.removeSupportedPaymentMode(this);
    }


    public boolean isSupportedDeliveryMode(DeliveryMode deliveryMode)
    {
        return deliveryMode.isSupportedPaymentMode(this);
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<DeliveryMode> getSupportedDeliveryModes(SessionContext ctx)
    {
        return getSession().getOrderManager().getSupportedDeliveryModes(this);
    }


    private boolean checkConsistency(SessionContext ctx, String code)
    {
        List<PaymentMode> result = FlexibleSearch.getInstance().search(ctx, "SELECT {PK} FROM {PaymentMode} WHERE {code}=?code", Collections.singletonMap("code", code), PaymentMode.class).getResult();
        return result.isEmpty();
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        String query = "SELECT {PK} FROM {DeliveryMode} WHERE {supportedPaymentModesInternal} LIKE '%," + getPK().getLongValueAsString() + ",%'";
        List<DeliveryMode> result = FlexibleSearch.getInstance().search(query, DeliveryMode.class).getResult();
        for(DeliveryMode mode : result)
        {
            mode.removeSupportedPaymentMode(this);
        }
        super.remove(ctx);
    }


    @SLDSafe(portingClass = "RemovePaymentModeCheckOrdersInterceptor")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        String query = "GET {" + GeneratedCoreConstants.TC.ABSTRACTORDER + "} WHERE {paymentMode}=?mode";
        Map<String, Object> params = new HashMap<>();
        params.put("mode", getPK());
        Collection<AbstractOrder> orders = FlexibleSearch.getInstance().search(query, params, AbstractOrder.class).getResult();
        if(!orders.isEmpty())
        {
            throw new ConsistencyCheckException(null, "payment mode " + this + " can not be removed since it is still used in orders", -1);
        }
    }
}
