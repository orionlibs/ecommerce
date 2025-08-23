package de.hybris.platform.voucher.jalo;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Base64;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.constants.GeneratedVoucherConstants;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.jalo.util.VoucherValue;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

public abstract class Voucher extends GeneratedVoucher
{
    private static final Logger LOG = Logger.getLogger(Voucher.class);
    protected static final int CODELENGTH = 12;
    protected static final int LENGTH_CODE = 3;
    protected static final String DIVIDER = "-";
    private static final String ALGORITHM = "AES";
    private static final String KEY = "key";
    private static final String ALPHABET = "alphabet";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        Discount voucher = (Discount)super.createItem(ctx, type, allAttributes);
        createAndStoreKey(ctx, voucher);
        return changeTypeAfterCreation((Item)voucher, type);
    }


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        removeInvalidations(ctx);
        super.remove(ctx);
    }


    protected void removeInvalidations(SessionContext ctx)
    {
        for(VoucherInvalidation vi : getInvalidations(ctx))
        {
            try
            {
                vi.remove(ctx);
            }
            catch(Exception e)
            {
                LOG.warn("Voucher invalidation failed for: " + vi.getCode(), e);
            }
        }
    }


    protected void createAndStoreKey(SessionContext ctx, Discount item)
    {
        try
        {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecretKey skey = kgen.generateKey();
            item.setProperty(ctx, "key", Base64.encodeBytes(skey.getEncoded(), 8));
            String origChars = "123456789ABCDEFGHKLMNPRSTWXYZ";
            String chars = "";
            while(chars.length() < 16)
            {
                int pos = (int)(SecureRandom.getInstance("SHA1PRNG").nextDouble() * "123456789ABCDEFGHKLMNPRSTWXYZ".length());
                char nextChar = "123456789ABCDEFGHKLMNPRSTWXYZ".charAt(pos);
                if(chars.indexOf(nextChar) == -1)
                {
                    chars = chars + chars;
                }
            }
            item.setProperty(ctx, "alphabet", chars);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public VoucherInvalidation createVoucherInvalidation(String aVoucherCode, Order anOrder)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("voucher", this);
        params.put("code", aVoucherCode);
        params.put("user", anOrder.getUser());
        params.put("order", anOrder);
        params.put("status", "created");
        return VoucherManager.getInstance(getSession()).createVoucherInvalidation(params);
    }


    public abstract boolean checkVoucherCode(String paramString);


    public VoucherEntrySet getApplicableEntries(AbstractOrder anOrder)
    {
        VoucherEntrySet entries = new VoucherEntrySet(anOrder.getAllEntries());
        for(Iterator<Restriction> iterator = getRestrictions().iterator(); !entries.isEmpty() && iterator.hasNext(); )
        {
            Restriction nextRestriction = iterator.next();
            entries.retainAll((Collection)nextRestriction.getApplicableEntries(anOrder));
        }
        return entries;
    }


    protected VoucherValue getApplicableValue(AbstractOrder anOrder)
    {
        double applicableValue = 0.0D;
        for(Iterator<VoucherEntry> iterator = getApplicableEntries(anOrder).iterator(); iterator.hasNext(); )
        {
            VoucherEntry voucherEntry = iterator.next();
            AbstractOrderEntry orderEntry = voucherEntry.getOrderEntry();
            long voucherEntryQuantity = voucherEntry.getQuantity();
            long orderEntryQuantity = orderEntry.getQuantity().longValue();
            if(voucherEntryQuantity == orderEntryQuantity)
            {
                applicableValue += orderEntry.getTotalPrice().doubleValue();
                continue;
            }
            double calculatedBasePrice = orderEntry.getTotalPrice().doubleValue() / orderEntryQuantity;
            applicableValue += Math.min(voucherEntryQuantity, orderEntryQuantity) * calculatedBasePrice;
        }
        return new VoucherValue(applicableValue, anOrder.getCurrency());
    }


    public VoucherValue getAppliedValue(AbstractOrder anOrder)
    {
        if(isApplicable(anOrder))
        {
            return getVoucherValue(anOrder);
        }
        return new VoucherValue(0.0D, anOrder.getCurrency());
    }


    protected ComposedType getComposedType(Class aClass)
    {
        try
        {
            ComposedType type = getSession().getTypeManager().getComposedType(aClass);
            if(type == null)
            {
                throw new JaloSystemException(null, "got type null for " + aClass, 0);
            }
            return type;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "required type missing", 0);
        }
    }


    public DiscountValue getDiscountValue(AbstractOrder anOrder)
    {
        VoucherValue value = getAppliedValue(anOrder);
        return new DiscountValue(getCode(), value.getValue(), true, value.getValue(), value.getCurrencyIsoCode());
    }


    protected VoucherInvalidation getInvalidation(String aVoucherCode, Order anOrder)
    {
        GenericQuery query = new GenericQuery(GeneratedVoucherConstants.TC.VOUCHERINVALIDATION, (GenericCondition)GenericConditionList.createConditionList(new GenericCondition[] {GenericCondition.equals("code", aVoucherCode),
                        GenericCondition.equals("order", anOrder)}));
        List<VoucherInvalidation> invalidations = getSession().search(query, getSession().createSearchContext()).getResult();
        return invalidations.isEmpty() ? null : invalidations.get(0);
    }


    protected Collection<VoucherInvalidation> getInvalidations(String aVoucherCode)
    {
        return getInvalidationsInternal(GenericCondition.equals("code", aVoucherCode));
    }


    protected Collection<VoucherInvalidation> getInvalidations(String aVoucherCode, User anUser)
    {
        return getInvalidationsInternal(
                        (GenericCondition)GenericCondition.createConditionList(new GenericCondition[] {GenericCondition.equals("user", anUser),
                                        GenericCondition.equals("code", aVoucherCode)}));
    }


    private Collection<VoucherInvalidation> getInvalidationsInternal(GenericCondition condition)
    {
        return getSession()
                        .search(new GenericQuery(GeneratedVoucherConstants.TC.VOUCHERINVALIDATION, condition), getSession().createSearchContext())
                        .getResult();
    }


    public String getValueString(SessionContext ctx)
    {
        return "" + getValue() + getValue();
    }


    public List<Restriction> getViolatedRestrictions(AbstractOrder anOrder)
    {
        List<Restriction> restrictions = new ArrayList<>();
        for(Restriction nextRestriction : getRestrictions())
        {
            if(!nextRestriction.isFulfilled(anOrder))
            {
                restrictions.add(nextRestriction);
            }
        }
        return restrictions.isEmpty() ? Collections.<Restriction>emptyList() : restrictions;
    }


    public List<Restriction> getViolatedRestrictions(Product aProduct)
    {
        List<Restriction> restrictions = new ArrayList<>();
        for(Restriction nextRestriction : getRestrictions())
        {
            if(!nextRestriction.isFulfilled(aProduct))
            {
                restrictions.add(nextRestriction);
            }
        }
        return restrictions.isEmpty() ? Collections.<Restriction>emptyList() : restrictions;
    }


    public List<String> getViolationMessages(AbstractOrder anOrder)
    {
        return getViolationMessages(getViolatedRestrictions(anOrder));
    }


    public List<String> getViolationMessages(Product aProduct)
    {
        return getViolationMessages(getViolatedRestrictions(aProduct));
    }


    private List<String> getViolationMessages(Collection<Restriction> restrictions)
    {
        List<String> messages = new ArrayList<>();
        for(Restriction nextRestriction : restrictions)
        {
            messages.add(nextRestriction.getViolationMessage());
        }
        return messages.isEmpty() ? Collections.<String>emptyList() : messages;
    }


    public VoucherValue getVoucherValue(AbstractOrder anOrder)
    {
        double resultValue;
        Currency resultCurrency;
        Iterator<Discount> discounts = anOrder.getDiscounts().iterator();
        boolean found = false;
        boolean applyFreeShipping = false;
        while(discounts.hasNext() && !found)
        {
            Discount discount = discounts.next();
            if(discount instanceof Voucher)
            {
                Voucher voucher = (Voucher)discount;
                if(voucher.isApplicable(anOrder) && voucher.isFreeShippingAsPrimitive())
                {
                    if(voucher.equals(this))
                    {
                        applyFreeShipping = true;
                    }
                    found = true;
                }
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Processed voucher: [" + getCode() + "] " + getName());
            LOG.debug("Free shipping is: " + isFreeShippingAsPrimitive());
            LOG.debug("Free shipping will apply: " + applyFreeShipping);
        }
        VoucherValue applicableValue = getApplicableValue(anOrder);
        if(isAbsolute().booleanValue())
        {
            resultValue = Math.min(applicableValue.getCurrency().convertAndRound(getCurrency(), applicableValue.getValue()),
                            getValue().doubleValue());
            resultCurrency = getCurrency();
        }
        else
        {
            resultValue = applicableValue.getValue() * getValue().doubleValue() / 100.0D;
            resultCurrency = applicableValue.getCurrency();
        }
        if(isFreeShippingAsPrimitive() && applyFreeShipping)
        {
            resultValue += anOrder.getCurrency().convertAndRound(resultCurrency, anOrder.getDeliveryCosts());
        }
        return new VoucherValue(resultValue, resultCurrency);
    }


    public boolean isApplicable(AbstractOrder anOrder)
    {
        for(Iterator<Restriction> iterator = getRestrictions().iterator(); iterator.hasNext(); )
        {
            Restriction nextRestriction = iterator.next();
            if(!nextRestriction.isFulfilled(anOrder))
            {
                return false;
            }
        }
        return true;
    }


    public boolean isApplicable(Product aProduct)
    {
        for(Iterator<Restriction> iterator = getRestrictions().iterator(); iterator.hasNext(); )
        {
            Restriction nextRestriction = iterator.next();
            if(!nextRestriction.isFulfilled(aProduct))
            {
                return false;
            }
        }
        return true;
    }


    private boolean isApplied(String aVoucherCode, AbstractOrder abstractOrder)
    {
        boolean ret = false;
        VoucherManager vm = VoucherManager.getInstance();
        Voucher v = vm.getVoucher(aVoucherCode);
        if(v != null && abstractOrder.getDiscounts().contains(v))
        {
            if(abstractOrder instanceof Cart)
            {
                ret = vm.getAppliedVoucherCodes((Cart)abstractOrder).contains(aVoucherCode);
            }
            else
            {
                VoucherInvalidation inv = getInvalidation(aVoucherCode, (Order)abstractOrder);
                ret = (inv != null && "confirmed".equals(inv.getStatus()));
            }
        }
        return ret;
    }


    public abstract boolean isReservable(String paramString, User paramUser);


    public boolean isReservable(String aVoucherCode, AbstractOrder abstractOrder)
    {
        return (isReservable(aVoucherCode, abstractOrder.getUser()) && !isApplied(aVoucherCode, abstractOrder));
    }


    public boolean redeem(String aVoucherCode, Cart aCart) throws JaloPriceFactoryException
    {
        if(checkVoucherCode(aVoucherCode) && isReservable(aVoucherCode, (AbstractOrder)aCart))
        {
            aCart.addDiscount((Discount)this);
            aCart.recalculate();
            Collection<String> appliedVoucherCodes = new LinkedHashSet<>(VoucherManager.getInstance().getAppliedVoucherCodes(aCart));
            appliedVoucherCodes.add(aVoucherCode);
            VoucherManager.getInstance().setAppliedVoucherCodes((AbstractOrder)aCart, appliedVoucherCodes);
            return true;
        }
        return false;
    }


    public VoucherInvalidation redeem(String aVoucherCode, Order anOrder)
    {
        if(!checkVoucherCode(aVoucherCode))
        {
            return null;
        }
        VoucherInvalidation invalidation = getInvalidation(aVoucherCode, anOrder);
        if(invalidation == null)
        {
            invalidation = reserve(aVoucherCode, anOrder);
        }
        if(invalidation == null || "confirmed".equals(invalidation.getStatus()))
        {
            return null;
        }
        invalidation.setStatus("confirmed");
        return invalidation;
    }


    public void release(String aVoucherCode, Order anOrder) throws ConsistencyCheckException
    {
        if(checkVoucherCode(aVoucherCode))
        {
            anOrder.removeDiscount((Discount)this);
            VoucherInvalidation invalidation = getInvalidation(aVoucherCode, anOrder);
            if(invalidation != null)
            {
                invalidation.remove();
            }
        }
    }


    public void release(String aVoucherCode, Cart aCart) throws JaloPriceFactoryException
    {
        if(checkVoucherCode(aVoucherCode))
        {
            aCart.removeDiscount((Discount)this);
            aCart.recalculate();
            Collection<String> appliedVoucherCodes = new LinkedHashSet<>(VoucherManager.getInstance().getAppliedVoucherCodes(aCart));
            appliedVoucherCodes.remove(aVoucherCode);
            VoucherManager.getInstance().setAppliedVoucherCodes((AbstractOrder)aCart, appliedVoucherCodes);
        }
    }


    public VoucherInvalidation reserve(String aVoucherCode, Order anOrder)
    {
        if(checkVoucherCode(aVoucherCode) && isReservable(aVoucherCode, (AbstractOrder)anOrder))
        {
            anOrder.addDiscount((Discount)this);
            return createVoucherInvalidation(aVoucherCode, anOrder);
        }
        return null;
    }


    protected static String insertDividers(String voucherCode)
    {
        StringBuilder result = new StringBuilder(voucherCode);
        for(int index = 3; index < result.length(); index += 4 + "-".length())
        {
            result.insert(index, "-");
        }
        return result.toString();
    }


    protected static String removeDividers(String voucherCode)
    {
        StringBuilder result = new StringBuilder();
        for(StringTokenizer tokenizer = new StringTokenizer(voucherCode, "-"); tokenizer.hasMoreTokens(); )
        {
            result.append(tokenizer.nextToken());
        }
        return result.toString();
    }


    public String generateVoucherCode() throws NoSuchAlgorithmException
    {
        int voucherNumber = getNextVoucherNumber(getSession().getSessionContext());
        if(voucherNumber < 0 || voucherNumber >= 16777216)
        {
            throw new IllegalArgumentException("Given voucherNumber is not in accepted range!");
        }
        String clearText = getCode() + getCode();
        String sig = threeByteSig(clearText);
        return insertDividers(clearText.concat(sig));
    }


    private String getAlphabet(SessionContext ctx)
    {
        return (String)getProperty(ctx, "alphabet");
    }


    private synchronized Cipher getCipher() throws NoSuchAlgorithmException
    {
        Cipher cipher = null;
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(getKey(getSession().getSessionContext()), "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(1, skeySpec);
        }
        catch(NoSuchPaddingException | java.security.InvalidKeyException e)
        {
            throw new RuntimeException(e);
        }
        return cipher;
    }


    private int getIntFromHexByte(String value, int offset) throws InvalidVoucherKeyException
    {
        return (getIntFromHexNibble(value.charAt(0), offset) << 4) + getIntFromHexNibble(value.charAt(1), offset + 4);
    }


    private int getIntFromHexNibble(char value, int offset) throws InvalidVoucherKeyException
    {
        int pos = getAlphabet(getSession().getSessionContext()).indexOf(value);
        if(pos == -1)
        {
            throw new InvalidVoucherKeyException();
        }
        pos -= offset;
        while(pos < 0)
        {
            pos += 16;
        }
        return pos % 16;
    }


    private byte[] getKey(SessionContext ctx)
    {
        return Base64.decode((String)getProperty(ctx, "key"));
    }


    protected abstract int getNextVoucherNumber(SessionContext paramSessionContext);


    protected int getVoucherNumber(String voucherCode) throws InvalidVoucherKeyException
    {
        int voucherNumberPos = voucherCode.length() - 12;
        String voucherNumberHex = voucherCode.substring(voucherNumberPos, voucherNumberPos + 6);
        int firstByte = getIntFromHexByte(voucherNumberHex.substring(0, 2), 0);
        int secondByte = getIntFromHexByte(voucherNumberHex.substring(2, 4), 1);
        int thirdByte = getIntFromHexByte(voucherNumberHex.substring(4, 6), 7);
        return firstByte << 16 | secondByte << 8 | thirdByte;
    }


    private String nibbleHex(int value, int offset)
    {
        String chars = getAlphabet(getSession().getSessionContext());
        int pos = (value + offset) % 16;
        return chars.substring(pos, pos + 1);
    }


    private String oneByteHex(int value, int offset)
    {
        return nibbleHex(value >> 4, offset).concat(nibbleHex(value & 0xF, offset + 4));
    }


    private String threeByteHex(int number)
    {
        return oneByteHex(number >> 16 & 0xFF, 0).concat(oneByteHex(number >> 8 & 0xFF, 1)).concat(oneByteHex(number & 0xFF, 7));
    }


    protected String threeByteSig(String sigText) throws NoSuchAlgorithmException
    {
        try
        {
            String sigClearText = (sigText.length() < 9) ? sigText.concat(sigText) : sigText;
            byte[] sigData = getCipher().doFinal(sigClearText.getBytes());
            int sigByte0 = sigData[0] & 0xFF ^ sigData[3] & 0xFF ^ sigData[8] & 0xFF;
            int sigByte1 = sigData[1] & 0xFF ^ sigData[4] & 0xFF ^ sigData[6] & 0xFF;
            int sigByte2 = sigData[2] & 0xFF ^ sigData[5] & 0xFF ^ sigData[7] & 0xFF;
            return threeByteHex(sigByte0 << 16 | sigByte1 << 8 | sigByte2);
        }
        catch(BadPaddingException | javax.crypto.IllegalBlockSizeException e)
        {
            throw new RuntimeException(e);
        }
    }
}
