package de.hybris.platform.voucher.jalo;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.ExtensionNotFoundException;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.constants.GeneratedVoucherConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class VoucherManager extends GeneratedVoucherManager
{
    private static final Logger LOG = Logger.getLogger(VoucherManager.class);


    public void createEssentialData(Map params, JspContext jspc)
    {
        try
        {
            for(ComposedType ct : getComposedType(GeneratedVoucherConstants.TC.VOUCHER).getAllSubTypes())
            {
                setDefaultValues(ct, "name");
            }
            for(ComposedType type : getComposedType(GeneratedVoucherConstants.TC.RESTRICTION).getAllSubTypes())
            {
                setDefaultValues(type, "description");
                setDefaultValues(type, "violationMessage");
            }
            for(ComposedType type : getComposedType(GeneratedVoucherConstants.TC.PRODUCTRESTRICTION).getAllSubTypes())
            {
                setDefaultValues(type, "description");
                setDefaultValues(type, "violationMessage");
            }
        }
        catch(Exception e)
        {
            LOG.error("Error while creating essential data vor voucher extension", e);
        }
    }


    public void afterOrderCreation(Order order, Cart cart)
    {
        removeCopiedVouchers(order, cart);
        afterOrderCreation(getSession().getSessionContext(), order, cart);
    }


    public void afterOrderCreation(SessionContext ctx, Order order, Cart cart)
    {
        Collection<String> appliedCodes = new HashSet<>(getAppliedVoucherCodes(ctx, cart));
        if(!appliedCodes.isEmpty())
        {
            appliedCodes.removeAll(getAppliedVoucherCodes(ctx, order));
            List<Voucher> invalid = new ArrayList<>();
            for(String codeToApply : appliedCodes)
            {
                VoucherInvalidation inv = null;
                try
                {
                    inv = redeemVoucher(codeToApply, order);
                }
                catch(Exception e)
                {
                    LOG.error("redeemVoucher failed", e);
                }
                if(inv == null)
                {
                    Voucher v = getVoucher(codeToApply);
                    if(v != null)
                    {
                        invalid.add(v);
                    }
                }
            }
            if(!invalid.isEmpty())
            {
                List<Discount> discounts = new ArrayList<>(order.getDiscounts());
                List<DiscountValue> discountValues = new ArrayList<>(order.getGlobalDiscountValues());
                for(Voucher v : invalid)
                {
                    discounts.remove(v);
                    String vCode = v.getCode();
                    for(int i = 0; i < discountValues.size(); i++)
                    {
                        DiscountValue dv = discountValues.get(i);
                        if(vCode.equals(dv.getCode()))
                        {
                            discountValues.remove(i);
                            break;
                        }
                    }
                }
                order.setDiscounts(discounts);
                order.setGlobalDiscountValues(discountValues);
                try
                {
                    order.calculateTotals(false);
                }
                catch(JaloPriceFactoryException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
        setAppliedVoucherCodes(ctx, (AbstractOrder)order, null);
    }


    private void setDefaultValues(ComposedType type, String attributeDescriptorQualifier)
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setLanguage(null);
        AttributeDescriptor attributeDescriptor = type.getAttributeDescriptor(attributeDescriptorQualifier);
        StringBuilder key = new StringBuilder();
        key.append("type.");
        key.append(type.getCode().toLowerCase());
        key.append(".");
        key.append(attributeDescriptorQualifier.toLowerCase());
        key.append(".");
        key.append("defaultvalue");
        attributeDescriptor.setDefaultValue(ctx, Localization.getLocalizedMap(key.toString()));
    }


    public Collection getAllVouchers()
    {
        return getSession().search(new GenericQuery(GeneratedVoucherConstants.TC.VOUCHER)).getResult();
    }


    public Collection<String> getAppliedVoucherCodes(Cart item)
    {
        Collection<String> ret = getAppliedVoucherCodes((AbstractOrder)item);
        return (ret != null) ? Collections.<String>unmodifiableCollection(ret) : Collections.<String>emptyList();
    }


    public Collection<String> getAppliedVoucherCodes(Order order)
    {
        return getAppliedVoucherCodes(getSession().getSessionContext(), order);
    }


    public Collection<String> getAppliedVoucherCodes(SessionContext ctx, Order order)
    {
        GenericQuery q = new GenericQuery(GeneratedVoucherConstants.TC.VOUCHERINVALIDATION, (GenericCondition)GenericCondition.and(new GenericCondition[] {GenericCondition.equals("order", order),
                        GenericCondition.equals("status", "confirmed")}));
        q.addSelectField(new GenericSelectField("code", String.class));
        q.addOrderBy(new GenericSearchOrderBy(new GenericSearchField(VoucherInvalidation.CREATION_TIME), true));
        q.addOrderBy(new GenericSearchOrderBy(new GenericSearchField(VoucherInvalidation.PK), true));
        return new LinkedHashSet<>(getSession().search(q).getResult());
    }


    public Collection<String> getAppliedVoucherCodes(SessionContext ctx, Cart item)
    {
        Collection<String> ret = getAppliedVoucherCodes(ctx, (AbstractOrder)item);
        return (ret != null) ? Collections.<String>unmodifiableCollection(ret) : Collections.<String>emptyList();
    }


    public Collection getAppliedVouchers(AbstractOrder anOrder)
    {
        Collection<Discount> result = new HashSet();
        for(Iterator<Discount> iterator = anOrder.getDiscounts().iterator(); iterator.hasNext(); )
        {
            Discount nextDiscount = iterator.next();
            if(nextDiscount instanceof Voucher)
            {
                result.add(nextDiscount);
            }
        }
        return result;
    }


    public static VoucherManager getInstance(JaloSession jaloSession)
    {
        try
        {
            return (VoucherManager)jaloSession.getExtensionManager().getExtension("voucher");
        }
        catch(ExtensionNotFoundException e)
        {
            return null;
        }
    }


    public static VoucherManager getInstance()
    {
        return getInstance(JaloSession.getCurrentSession());
    }


    public Voucher getVoucher(String voucherCode)
    {
        Collection<Voucher> result = getPromotionVouchers(voucherCode);
        if(result.isEmpty())
        {
            result = getSerialVouchers(voucherCode);
        }
        return result.isEmpty() ? null : result.iterator().next();
    }


    public Collection getPromotionVouchers(String voucherCode)
    {
        return getSession().search(new GenericQuery(GeneratedVoucherConstants.TC.PROMOTIONVOUCHER,
                        GenericCondition.equals("voucherCode", voucherCode))).getResult();
    }


    public Collection getSerialVouchers(String aVoucherCode)
    {
        String code = SerialVoucher.extractCode(aVoucherCode);
        if(code == null)
        {
            return Collections.emptyList();
        }
        Collection<SerialVoucher> ret = new LinkedHashSet<>();
        for(SerialVoucher voucher : getSession()
                        .search(new GenericQuery(GeneratedVoucherConstants.TC.SERIALVOUCHER, GenericCondition.equals("code", code))).getResult())
        {
            if(voucher.checkVoucherCode(aVoucherCode))
            {
                ret.add(voucher);
            }
        }
        return Collections.unmodifiableCollection(ret);
    }


    private ComposedType getComposedType(String code)
    {
        try
        {
            ComposedType type = getSession().getTypeManager().getComposedType(code);
            if(type == null)
            {
                throw new JaloSystemException(null, "got type null for " + code, 0);
            }
            return type;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "required type missing", 0);
        }
    }


    public boolean redeemVoucher(String aVoucherCode, Cart aCart) throws JaloPriceFactoryException
    {
        Voucher voucher = getVoucher(aVoucherCode);
        if(voucher != null)
        {
            return voucher.redeem(aVoucherCode, aCart);
        }
        return false;
    }


    public VoucherInvalidation redeemVoucher(String aVoucherCode, Order anOrder)
    {
        Voucher voucher = getVoucher(aVoucherCode);
        if(voucher != null)
        {
            return voucher.redeem(aVoucherCode, anOrder);
        }
        return null;
    }


    public void releaseVoucher(String aVoucherCode, Cart aCart) throws JaloPriceFactoryException
    {
        Voucher voucher = getVoucher(aVoucherCode);
        if(voucher != null)
        {
            voucher.release(aVoucherCode, aCart);
        }
    }


    public void releaseVoucher(String aVoucherCode, Order anOrder) throws ConsistencyCheckException
    {
        Voucher voucher = getVoucher(aVoucherCode);
        if(voucher != null)
        {
            voucher.release(aVoucherCode, anOrder);
        }
    }


    public VoucherInvalidation reserveVoucher(String aVoucherCode, Order anOrder)
    {
        Voucher voucher = getVoucher(aVoucherCode);
        if(voucher != null)
        {
            return voucher.reserve(aVoucherCode, anOrder);
        }
        return null;
    }


    private void removeCopiedVouchers(Order order, Cart cart)
    {
        VoucherManager vm = getInstance();
        Collection<String> appliedVoucherCodes = vm.getAppliedVoucherCodes(cart);
        if(CollectionUtils.isNotEmpty(appliedVoucherCodes))
        {
            List<Discount> discounts = order.getDiscounts();
            List<Discount> newOnes = new ArrayList<>();
            for(Discount d : discounts)
            {
                if(!(d instanceof Voucher))
                {
                    newOnes.add(d);
                }
            }
            order.setDiscounts(newOnes);
            setAppliedVoucherCodes((AbstractOrder)order, null);
        }
    }
}
