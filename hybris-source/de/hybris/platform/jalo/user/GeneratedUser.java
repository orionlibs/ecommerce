package de.hybris.platform.jalo.user;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.hmc.jalo.UserProfile;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.Quote;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedUser extends Principal
{
    public static final String DEFAULTPAYMENTADDRESS = "defaultPaymentAddress";
    public static final String DEFAULTSHIPMENTADDRESS = "defaultShipmentAddress";
    public static final String PASSWORDENCODING = "passwordEncoding";
    public static final String ENCODEDPASSWORD = "encodedPassword";
    public static final String PASSWORDANSWER = "passwordAnswer";
    public static final String PASSWORDQUESTION = "passwordQuestion";
    public static final String SESSIONLANGUAGE = "sessionLanguage";
    public static final String SESSIONCURRENCY = "sessionCurrency";
    public static final String LOGINDISABLED = "loginDisabled";
    public static final String LASTLOGIN = "lastLogin";
    public static final String HMCLOGINDISABLED = "hmcLoginDisabled";
    public static final String RETENTIONSTATE = "retentionState";
    public static final String USERPROFILE = "userprofile";
    public static final String DEACTIVATIONDATE = "deactivationDate";
    public static final String RANDOMTOKEN = "randomToken";
    public static final String CONTACTINFOS = "contactInfos";
    public static final String CARTS = "carts";
    public static final String QUOTES = "quotes";
    public static final String ORDERS = "orders";
    public static final String ADDRESSES = "addresses";
    public static final String PAYMENTINFOS = "paymentInfos";
    protected static final OneToManyHandler<AbstractContactInfo> CONTACTINFOSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.ABSTRACTCONTACTINFO, true, "user", "userPOS", true, true, 0);
    protected static final OneToManyHandler<Cart> CARTSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.CART, true, "user", null, false, true, 0);
    protected static final OneToManyHandler<Quote> QUOTESHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.QUOTE, true, "user", null, false, true, 0);
    protected static final OneToManyHandler<Order> ORDERSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.ORDER, true, "user", null, false, true, 0);
    protected static final OneToManyHandler<Address> ADDRESSESHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.ADDRESS, true, "owner", null, false, true, 0, "{original} is null");
    protected static final OneToManyHandler<PaymentInfo> PAYMENTINFOSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.PAYMENTINFO, true, "user", null, false, true, 0, "{original} is null");
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Principal.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("defaultPaymentAddress", Item.AttributeMode.INITIAL);
        tmp.put("defaultShipmentAddress", Item.AttributeMode.INITIAL);
        tmp.put("passwordEncoding", Item.AttributeMode.INITIAL);
        tmp.put("encodedPassword", Item.AttributeMode.INITIAL);
        tmp.put("passwordAnswer", Item.AttributeMode.INITIAL);
        tmp.put("passwordQuestion", Item.AttributeMode.INITIAL);
        tmp.put("sessionLanguage", Item.AttributeMode.INITIAL);
        tmp.put("sessionCurrency", Item.AttributeMode.INITIAL);
        tmp.put("loginDisabled", Item.AttributeMode.INITIAL);
        tmp.put("lastLogin", Item.AttributeMode.INITIAL);
        tmp.put("hmcLoginDisabled", Item.AttributeMode.INITIAL);
        tmp.put("retentionState", Item.AttributeMode.INITIAL);
        tmp.put("userprofile", Item.AttributeMode.INITIAL);
        tmp.put("deactivationDate", Item.AttributeMode.INITIAL);
        tmp.put("randomToken", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Address> getAddresses(SessionContext ctx)
    {
        return ADDRESSESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Address> getAddresses()
    {
        return getAddresses(getSession().getSessionContext());
    }


    public void setAddresses(SessionContext ctx, Collection<Address> value)
    {
        ADDRESSESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setAddresses(Collection<Address> value)
    {
        setAddresses(getSession().getSessionContext(), value);
    }


    public void addToAddresses(SessionContext ctx, Address value)
    {
        ADDRESSESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToAddresses(Address value)
    {
        addToAddresses(getSession().getSessionContext(), value);
    }


    public void removeFromAddresses(SessionContext ctx, Address value)
    {
        ADDRESSESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromAddresses(Address value)
    {
        removeFromAddresses(getSession().getSessionContext(), value);
    }


    public Collection<Cart> getCarts(SessionContext ctx)
    {
        return CARTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Cart> getCarts()
    {
        return getCarts(getSession().getSessionContext());
    }


    public void setCarts(SessionContext ctx, Collection<Cart> value)
    {
        CARTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCarts(Collection<Cart> value)
    {
        setCarts(getSession().getSessionContext(), value);
    }


    public void addToCarts(SessionContext ctx, Cart value)
    {
        CARTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCarts(Cart value)
    {
        addToCarts(getSession().getSessionContext(), value);
    }


    public void removeFromCarts(SessionContext ctx, Cart value)
    {
        CARTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCarts(Cart value)
    {
        removeFromCarts(getSession().getSessionContext(), value);
    }


    public Collection<AbstractContactInfo> getContactInfos(SessionContext ctx)
    {
        return CONTACTINFOSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<AbstractContactInfo> getContactInfos()
    {
        return getContactInfos(getSession().getSessionContext());
    }


    public void setContactInfos(SessionContext ctx, Collection<AbstractContactInfo> value)
    {
        CONTACTINFOSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setContactInfos(Collection<AbstractContactInfo> value)
    {
        setContactInfos(getSession().getSessionContext(), value);
    }


    public void addToContactInfos(SessionContext ctx, AbstractContactInfo value)
    {
        CONTACTINFOSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToContactInfos(AbstractContactInfo value)
    {
        addToContactInfos(getSession().getSessionContext(), value);
    }


    public void removeFromContactInfos(SessionContext ctx, AbstractContactInfo value)
    {
        CONTACTINFOSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromContactInfos(AbstractContactInfo value)
    {
        removeFromContactInfos(getSession().getSessionContext(), value);
    }


    public Date getDeactivationDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "deactivationDate");
    }


    public Date getDeactivationDate()
    {
        return getDeactivationDate(getSession().getSessionContext());
    }


    public void setDeactivationDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "deactivationDate", value);
    }


    public void setDeactivationDate(Date value)
    {
        setDeactivationDate(getSession().getSessionContext(), value);
    }


    public Address getDefaultPaymentAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "defaultPaymentAddress");
    }


    public Address getDefaultPaymentAddress()
    {
        return getDefaultPaymentAddress(getSession().getSessionContext());
    }


    public void setDefaultPaymentAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "defaultPaymentAddress", value);
    }


    public void setDefaultPaymentAddress(Address value)
    {
        setDefaultPaymentAddress(getSession().getSessionContext(), value);
    }


    public Address getDefaultShipmentAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "defaultShipmentAddress");
    }


    public Address getDefaultShipmentAddress()
    {
        return getDefaultShipmentAddress(getSession().getSessionContext());
    }


    public void setDefaultShipmentAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "defaultShipmentAddress", value);
    }


    public void setDefaultShipmentAddress(Address value)
    {
        setDefaultShipmentAddress(getSession().getSessionContext(), value);
    }


    public String getEncodedPassword(SessionContext ctx)
    {
        return (String)getProperty(ctx, "encodedPassword");
    }


    public String getEncodedPassword()
    {
        return getEncodedPassword(getSession().getSessionContext());
    }


    public void setEncodedPassword(SessionContext ctx, String value)
    {
        setProperty(ctx, "encodedPassword", value);
    }


    public void setEncodedPassword(String value)
    {
        setEncodedPassword(getSession().getSessionContext(), value);
    }


    public Boolean isHmcLoginDisabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "hmcLoginDisabled");
    }


    public Boolean isHmcLoginDisabled()
    {
        return isHmcLoginDisabled(getSession().getSessionContext());
    }


    public boolean isHmcLoginDisabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isHmcLoginDisabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isHmcLoginDisabledAsPrimitive()
    {
        return isHmcLoginDisabledAsPrimitive(getSession().getSessionContext());
    }


    public void setHmcLoginDisabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "hmcLoginDisabled", value);
    }


    public void setHmcLoginDisabled(Boolean value)
    {
        setHmcLoginDisabled(getSession().getSessionContext(), value);
    }


    public void setHmcLoginDisabled(SessionContext ctx, boolean value)
    {
        setHmcLoginDisabled(ctx, Boolean.valueOf(value));
    }


    public void setHmcLoginDisabled(boolean value)
    {
        setHmcLoginDisabled(getSession().getSessionContext(), value);
    }


    public Date getLastLogin(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "lastLogin");
    }


    public Date getLastLogin()
    {
        return getLastLogin(getSession().getSessionContext());
    }


    public void setLastLogin(SessionContext ctx, Date value)
    {
        setProperty(ctx, "lastLogin", value);
    }


    public void setLastLogin(Date value)
    {
        setLastLogin(getSession().getSessionContext(), value);
    }


    public Boolean isLoginDisabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "loginDisabled");
    }


    public Boolean isLoginDisabled()
    {
        return isLoginDisabled(getSession().getSessionContext());
    }


    public boolean isLoginDisabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLoginDisabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLoginDisabledAsPrimitive()
    {
        return isLoginDisabledAsPrimitive(getSession().getSessionContext());
    }


    public void setLoginDisabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "loginDisabled", value);
    }


    public void setLoginDisabled(Boolean value)
    {
        setLoginDisabled(getSession().getSessionContext(), value);
    }


    public void setLoginDisabled(SessionContext ctx, boolean value)
    {
        setLoginDisabled(ctx, Boolean.valueOf(value));
    }


    public void setLoginDisabled(boolean value)
    {
        setLoginDisabled(getSession().getSessionContext(), value);
    }


    public Collection<Order> getOrders(SessionContext ctx)
    {
        return ORDERSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Order> getOrders()
    {
        return getOrders(getSession().getSessionContext());
    }


    public void setOrders(SessionContext ctx, Collection<Order> value)
    {
        ORDERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setOrders(Collection<Order> value)
    {
        setOrders(getSession().getSessionContext(), value);
    }


    public void addToOrders(SessionContext ctx, Order value)
    {
        ORDERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToOrders(Order value)
    {
        addToOrders(getSession().getSessionContext(), value);
    }


    public void removeFromOrders(SessionContext ctx, Order value)
    {
        ORDERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromOrders(Order value)
    {
        removeFromOrders(getSession().getSessionContext(), value);
    }


    public String getPasswordAnswer(SessionContext ctx)
    {
        return (String)getProperty(ctx, "passwordAnswer");
    }


    public String getPasswordAnswer()
    {
        return getPasswordAnswer(getSession().getSessionContext());
    }


    public void setPasswordAnswer(SessionContext ctx, String value)
    {
        setProperty(ctx, "passwordAnswer", value);
    }


    public void setPasswordAnswer(String value)
    {
        setPasswordAnswer(getSession().getSessionContext(), value);
    }


    public String getPasswordEncoding(SessionContext ctx)
    {
        return (String)getProperty(ctx, "passwordEncoding");
    }


    public String getPasswordEncoding()
    {
        return getPasswordEncoding(getSession().getSessionContext());
    }


    public void setPasswordEncoding(SessionContext ctx, String value)
    {
        setProperty(ctx, "passwordEncoding", value);
    }


    public void setPasswordEncoding(String value)
    {
        setPasswordEncoding(getSession().getSessionContext(), value);
    }


    public String getPasswordQuestion(SessionContext ctx)
    {
        return (String)getProperty(ctx, "passwordQuestion");
    }


    public String getPasswordQuestion()
    {
        return getPasswordQuestion(getSession().getSessionContext());
    }


    public void setPasswordQuestion(SessionContext ctx, String value)
    {
        setProperty(ctx, "passwordQuestion", value);
    }


    public void setPasswordQuestion(String value)
    {
        setPasswordQuestion(getSession().getSessionContext(), value);
    }


    public Collection<PaymentInfo> getPaymentInfos(SessionContext ctx)
    {
        return PAYMENTINFOSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<PaymentInfo> getPaymentInfos()
    {
        return getPaymentInfos(getSession().getSessionContext());
    }


    public void setPaymentInfos(SessionContext ctx, Collection<PaymentInfo> value)
    {
        PAYMENTINFOSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPaymentInfos(Collection<PaymentInfo> value)
    {
        setPaymentInfos(getSession().getSessionContext(), value);
    }


    public void addToPaymentInfos(SessionContext ctx, PaymentInfo value)
    {
        PAYMENTINFOSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPaymentInfos(PaymentInfo value)
    {
        addToPaymentInfos(getSession().getSessionContext(), value);
    }


    public void removeFromPaymentInfos(SessionContext ctx, PaymentInfo value)
    {
        PAYMENTINFOSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPaymentInfos(PaymentInfo value)
    {
        removeFromPaymentInfos(getSession().getSessionContext(), value);
    }


    public Collection<Quote> getQuotes(SessionContext ctx)
    {
        return QUOTESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Quote> getQuotes()
    {
        return getQuotes(getSession().getSessionContext());
    }


    public void setQuotes(SessionContext ctx, Collection<Quote> value)
    {
        QUOTESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setQuotes(Collection<Quote> value)
    {
        setQuotes(getSession().getSessionContext(), value);
    }


    public void addToQuotes(SessionContext ctx, Quote value)
    {
        QUOTESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToQuotes(Quote value)
    {
        addToQuotes(getSession().getSessionContext(), value);
    }


    public void removeFromQuotes(SessionContext ctx, Quote value)
    {
        QUOTESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromQuotes(Quote value)
    {
        removeFromQuotes(getSession().getSessionContext(), value);
    }


    public String getRandomToken(SessionContext ctx)
    {
        return (String)getProperty(ctx, "randomToken");
    }


    public String getRandomToken()
    {
        return getRandomToken(getSession().getSessionContext());
    }


    public void setRandomToken(SessionContext ctx, String value)
    {
        setProperty(ctx, "randomToken", value);
    }


    public void setRandomToken(String value)
    {
        setRandomToken(getSession().getSessionContext(), value);
    }


    public EnumerationValue getRetentionState(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "retentionState");
    }


    public EnumerationValue getRetentionState()
    {
        return getRetentionState(getSession().getSessionContext());
    }


    public void setRetentionState(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "retentionState", value);
    }


    public void setRetentionState(EnumerationValue value)
    {
        setRetentionState(getSession().getSessionContext(), value);
    }


    public Currency getSessionCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "sessionCurrency");
    }


    public Currency getSessionCurrency()
    {
        return getSessionCurrency(getSession().getSessionContext());
    }


    public void setSessionCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "sessionCurrency", value);
    }


    public void setSessionCurrency(Currency value)
    {
        setSessionCurrency(getSession().getSessionContext(), value);
    }


    public Language getSessionLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "sessionLanguage");
    }


    public Language getSessionLanguage()
    {
        return getSessionLanguage(getSession().getSessionContext());
    }


    public void setSessionLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "sessionLanguage", value);
    }


    public void setSessionLanguage(Language value)
    {
        setSessionLanguage(getSession().getSessionContext(), value);
    }


    public UserProfile getUserprofile(SessionContext ctx)
    {
        return (UserProfile)getProperty(ctx, "userprofile");
    }


    public UserProfile getUserprofile()
    {
        return getUserprofile(getSession().getSessionContext());
    }


    public void setUserprofile(SessionContext ctx, UserProfile value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setUserprofile(UserProfile value)
    {
        setUserprofile(getSession().getSessionContext(), value);
    }
}
