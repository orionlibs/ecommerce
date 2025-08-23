package de.hybris.platform.jalo.user;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.model.user.UserPasswordChangeAuditModel;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.security.CannotDecodePasswordException;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.jalo.type.AttributeAccess;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.security.EJBCannotDecodePasswordException;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import de.hybris.platform.servicelayer.user.exceptions.PasswordPolicyViolationException;
import de.hybris.platform.servicelayer.user.impl.UserAuditFactory;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeEvent;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeListener;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.Utilities;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class User extends GeneratedUser
{
    public static final String USER_AUDIT_ENABLED = "user.audit.enabled";
    public static final String USER_DEACTIVATION_BLOCK_FOR_ALL_ADMINS = "user.deactivation.blockForAllAdmins";
    private static final Logger log = Logger.getLogger(User.class);
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DEFAULT_PAYMENT_ADDRESS = "defaultPaymentAddress";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DEFAULT_SHIPPING_ADDRESS = "defaultShipmentAddress";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PASSWORD_QUESTION = "passwordQuestion";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PASSWORD_ANSWER = "passwordAnswer";
    public static final String PAYMENT_MODES = "paymentModes";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String SESSION_LANGUAGE = "sessionLanguage";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String SESSION_CURRENCY = "sessionCurrency";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String LOGIN_DISABLED = "loginDisabled";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String LAST_LOGIN = "lastLogin";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CURRENT_TIME = "currentTime";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CURRENT_DATE = "currentDate";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PASSWORD = "password";


    @SLDSafe(portingClass = "MandatoryAttributesValidator, UniqueAttributesInterceptor", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("uid"))
        {
            throw new JaloInvalidParameterException("Missing parameter (uid) to create a User", 0);
        }
        if(allAttributes.get("uid") == null)
        {
            throw new JaloInvalidParameterException("Parameter (uid) cannot be null to create a User", 0);
        }
        if(type == null)
        {
            throw new JaloInvalidParameterException("User type cannot be null", 0);
        }
        if(!User.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("User type " + type + " is incompatible to default User class", 0);
        }
        String uid = (String)allAttributes.get("uid");
        checkConsistencyUid(uid, "error: duplicate user for uid \"" + uid + "\"", type.getCode());
        allAttributes.setAttributeMode("uid", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkSystemPrincipal() throws ConsistencyCheckException
    {
        if(Constants.USER.ADMIN_EMPLOYEE.equals(getUid()) || Constants.USER.ANONYMOUS_CUSTOMER.equals(getUid()))
        {
            throw new ConsistencyCheckException(null, "cannot change login for admin or anonymous", 0);
        }
    }


    @SLDSafe(portingClass = "ModifySystemUsersInterceptor", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    public void setUid(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        checkSystemPrincipal();
        String oldUid = getUid();
        if(oldUid != value && (oldUid == null || !oldUid.equals(value)))
        {
            checkConsistencyUid(value, "error: duplicate user for uid \"" + value + "\"", getComposedType().getCode());
            super.setUid(ctx, value);
        }
    }


    protected PasswordEncoder getEncoder(String encoding) throws PasswordEncoderNotFoundException
    {
        return getTenant().getJaloConnection().getPasswordEncoder(encoding);
    }


    private static final AttributeAccess _AD_DEFAULTSHIPMENTADDRESS = (AttributeAccess)new Object();


    @SLDSafe(portingClass = "UserCurrentTimeAttributeHandler", portingMethod = "get(UserModel model)")
    @Deprecated(since = "ages", forRemoval = false)
    public Date getCurrentTime(SessionContext ctx)
    {
        if(ctx != null)
        {
            return ctx.getAdjustedCurrentTime();
        }
        JaloSession js = getSession();
        if(js != null)
        {
            return js.getSessionContext().getAdjustedCurrentTime();
        }
        return new Date();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Date getCurrentTime()
    {
        return getCurrentTime(getSession().getSessionContext());
    }


    private static long currentDate = 0L;
    private static long currentDateValidTo = 0L;


    @SLDSafe(portingClass = "UserCurrentDateAttributeHandler", portingMethod = "get(UserModel model)")
    @Deprecated(since = "ages", forRemoval = false)
    public Date getCurrentDate(SessionContext ctx)
    {
        if(ctx != null)
        {
            return ctx.getAdjustedCurrentDate();
        }
        JaloSession js = getSession();
        if(js != null)
        {
            return js.getSessionContext().getAdjustedCurrentDate();
        }
        return getCurrentDateOldStyle();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Date getCurrentDate()
    {
        return getCurrentDate(getSession().getSessionContext());
    }


    protected Date getCurrentDateOldStyle()
    {
        if(currentDateValidTo < System.currentTimeMillis())
        {
            Calendar cal = Utilities.getDefaultCalendar();
            cal.set(14, 0);
            cal.set(13, 0);
            cal.set(12, 0);
            cal.set(10, 0);
            cal.set(9, 0);
            currentDate = cal.getTime().getTime();
            cal.roll(5, true);
            currentDateValidTo = cal.getTime().getTime();
        }
        return new Date(currentDate);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getLogin()
    {
        return getUID();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getLogin(SessionContext ctx)
    {
        return getUID();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setLogin(String login) throws ConsistencyCheckException
    {
        setUID(login);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setLogin(SessionContext ctx, String login) throws ConsistencyCheckException
    {
        setUID(login);
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "getPassword(UserModel user)")
    @Deprecated(since = "ages", forRemoval = false)
    public String getPassword() throws CannotDecodePasswordException, PasswordEncoderNotFoundException
    {
        return getPassword(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "getPassword(UserModel user)")
    @Deprecated(since = "ages", forRemoval = false)
    public String getPassword(SessionContext ctx) throws CannotDecodePasswordException, PasswordEncoderNotFoundException
    {
        try
        {
            return getEncoder(getPasswordEncoding()).decode(getEncodedPassword(ctx));
        }
        catch(EJBCannotDecodePasswordException e)
        {
            throw new CannotDecodePasswordException(e.getMessage(), e.getErrorCode());
        }
    }


    @SLDSafe(portingClass = "UserPasswordAttributeHandler", portingMethod = "set(final UserModel model, final String password)")
    @Deprecated(since = "ages", forRemoval = false)
    public void setPassword(String password) throws PasswordEncoderNotFoundException
    {
        setPassword(getSession().getSessionContext(), password);
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "setPassword(UserModel user, String plainPassword, String encoding)")
    @Deprecated(since = "ages", forRemoval = false)
    public void setPassword(String password, String encoding) throws PasswordEncoderNotFoundException
    {
        setPassword(getSession().getSessionContext(), password, encoding);
    }


    @SLDSafe(portingClass = "UserPasswordAttributeHandler", portingMethod = "set(final UserModel model, final String password)")
    @Deprecated(since = "ages", forRemoval = false)
    public void setPassword(SessionContext ctx, String password)
    {
        setPassword(ctx, password, getPasswordEncoding());
    }


    protected String getRealEncoding(String optionalEncoding)
    {
        if(StringUtils.isBlank(optionalEncoding) || "*".equalsIgnoreCase(optionalEncoding))
        {
            return Config.getString("default.password.encoding", "*");
        }
        return optionalEncoding;
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "setPassword(UserModel user, String plainPassword, String encoding)")
    @Deprecated(since = "ages", forRemoval = false)
    public void setPassword(SessionContext ctx, String newPassword, String encoding) throws PasswordEncoderNotFoundException
    {
        if(getTenant().getConfig()
                        .getBoolean("admin.password.required", false) &&
                        isAdmin() && StringUtils.isBlank(newPassword))
        {
            throw new JaloSystemException("Admin password must NOT be empty!");
        }
        List<PasswordPolicyViolation> passwordPolicyViolations = checkPasswordPolicies(newPassword, encoding);
        if(!passwordPolicyViolations.isEmpty())
        {
            throw new PasswordPolicyViolationException(passwordPolicyViolations);
        }
        String oldEncodedPassword = getEncodedPassword();
        String oldPasswordEncoding = getPasswordEncoding();
        String realEncoding = getRealEncoding(encoding);
        setEncodedPassword(ctx, getEncoder(realEncoding).encode(getUid(), newPassword));
        setPasswordEncoding(realEncoding);
        if(StringUtils.isNotBlank(oldEncodedPassword))
        {
            getPasswordChangeListener().passwordChanged(new PasswordChangeEvent(getUid()));
            if(getTenant().getConfig().getBoolean("user.audit.enabled", true))
            {
                auditUserPasswordChange(oldEncodedPassword, oldPasswordEncoding);
            }
        }
    }


    public static PasswordChangeListener getPasswordChangeListener()
    {
        PasswordChangeListener listener = (PasswordChangeListener)Registry.getCoreApplicationContext().getBean("passwordChangeListener", PasswordChangeListener.class);
        if(listener != null)
        {
            return listener;
        }
        return (PasswordChangeListener)NoOpListener.INSTANCE;
    }


    protected void auditUserPasswordChange(String oldEncodedPassword, String oldPasswordEncoding)
    {
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        UserAuditFactory userAuditFactory = (UserAuditFactory)ctx.getBean("userAuditFactory", UserAuditFactory.class);
        UserPasswordChangeAuditModel userAudit = userAuditFactory.createUserPasswordChangeAudit(getPK(), getUid(), oldEncodedPassword, oldPasswordEncoding);
        ModelService ms = (ModelService)ctx.getBean("modelService", ModelService.class);
        try
        {
            ms.save(userAudit);
        }
        finally
        {
            ms.detach(userAudit);
        }
    }


    protected List<PasswordPolicyViolation> checkPasswordPolicies(String password, String encoding)
    {
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        ModelService ms = (ModelService)ctx.getBean("modelService", ModelService.class);
        boolean attachedBefore = ms.isSourceAttached(this);
        UserModel userModel = null;
        try
        {
            userModel = (UserModel)ms.get(this);
            PasswordPolicyService pps = (PasswordPolicyService)ctx.getBean("passwordPolicyService", PasswordPolicyService.class);
            List<PasswordPolicyViolation> passwordPolicyViolations = pps.verifyPassword(userModel, password, encoding);
            return passwordPolicyViolations;
        }
        finally
        {
            if(!attachedBefore && userModel != null)
            {
                ms.detach(userModel);
            }
        }
    }


    @SLDSafe(portingClass = "UserPasswordEncodingPreparer", portingMethod = "onPrepare(final Object model, final InterceptorContext ctx)")
    public void setEncodedPassword(String password)
    {
        setEncodedPassword(getSession().getSessionContext(), password);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setEncodedPassword(String password, String encoding)
    {
        setEncodedPassword(getSession().getSessionContext(), password, encoding);
    }


    @SLDSafe(portingClass = "UserPasswordEncodingPreparer", portingMethod = "onPrepare(final Object model, final InterceptorContext ctx)")
    public void setEncodedPassword(SessionContext ctx, String password)
    {
        setEncodedPassword(ctx, password, getRealEncoding(getPasswordEncoding()));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setEncodedPassword(SessionContext ctx, String password, String encoding)
    {
        super.setEncodedPassword(ctx, password);
        setPasswordEncoding(encoding);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean checkPassword(String plainPassword)
    {
        return checkPassword(getSession().getSessionContext(), plainPassword);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean checkPassword(LoginToken token)
    {
        try
        {
            return UserManager.getInstance().checkPassword(this, token);
        }
        catch(EJBPasswordEncoderNotFoundException e)
        {
            throw new PasswordEncoderNotFoundException(e.getMessage(), e.getErrorCode());
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean checkPassword(SessionContext ctx, String plainPassword)
    {
        try
        {
            return UserManager.getInstance().checkPassword(this, plainPassword);
        }
        catch(EJBPasswordEncoderNotFoundException e)
        {
            throw new PasswordEncoderNotFoundException(e.getMessage(), e.getErrorCode());
        }
    }


    @SLDSafe(portingClass = "ModifySystemUsersInterceptor", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    public void setLoginDisabled(SessionContext ctx, Boolean disabled)
    {
        if(Boolean.TRUE.equals(disabled) && deactivationBlocked())
        {
            throw new JaloSystemException("It is not allowed to disable the login for this admin account", 1718);
        }
        super.setLoginDisabled(ctx, disabled);
    }


    @SLDSafe(portingClass = "ModifySystemUsersInterceptor", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    public void setDeactivationDate(SessionContext ctx, Date value)
    {
        if(value != null && deactivationBlocked())
        {
            throw new JaloSystemException("It is not allowed to deactivate this admin account");
        }
        super.setDeactivationDate(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllAddresses()
    {
        return getAddresses();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress()
    {
        return createAddress((PK)null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(PK pk)
    {
        return createAddress(pk, getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(SessionContext ctx)
    {
        return createAddress(null, ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(PK pk, SessionContext ctx)
    {
        try
        {
            return (Address)ComposedType.newInstance(ctx, Address.class, new Object[] {Item.OWNER, this, Item.PK, pk});
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(Map fields)
    {
        return createAddress((PK)null, fields);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(PK pk, Map fields)
    {
        return createAddress(pk, getSession().getSessionContext(), fields);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(SessionContext ctx, Map fields)
    {
        return createAddress(null, ctx, fields);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(PK pk, SessionContext ctx, Map fields)
    {
        Item.ItemAttributeMap params = new Item.ItemAttributeMap();
        if(fields != null)
        {
            params.putAll(fields);
        }
        params.put(Item.OWNER, this);
        params.put(Item.PK, pk);
        try
        {
            return (Address)ComposedType.newInstance(ctx, Address.class, (Map)params);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address getDefaultShippingAddress()
    {
        return getDefaultDeliveryAddress();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address getDefaultShippingAddress(SessionContext ctx)
    {
        return getDefaultDeliveryAddress(ctx);
    }


    public Address getDefaultDeliveryAddress()
    {
        return getDefaultDeliveryAddress(ctx());
    }


    public Address getDefaultDeliveryAddress(SessionContext ctx)
    {
        return getDefaultShipmentAddress(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDefaultShippingAddress(Address adr)
    {
        setDefaultDeliveryAddress(adr);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDefaultShippingAddress(SessionContext ctx, Address adr)
    {
        setDefaultDeliveryAddress(ctx, adr);
    }


    public void setDefaultDeliveryAddress(Address adr)
    {
        setDefaultDeliveryAddress(ctx(), adr);
    }


    public void setDefaultDeliveryAddress(SessionContext ctx, Address adr)
    {
        setDefaultShipmentAddress(ctx, adr);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PaymentInfo createPaymentInfo(String code)
    {
        return createPaymentInfo(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PaymentInfo createPaymentInfo(SessionContext ctx, String code)
    {
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("code", code);
            params.put("user", this);
            ComposedType type = TypeManager.getInstance().getComposedType(PaymentInfo.class);
            return (PaymentInfo)type.newInstance(ctx, (Map)params);
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


    @Deprecated(since = "ages", forRemoval = false)
    public Cart saveCurrentCart(String code) throws ConsistencyCheckException
    {
        return saveCurrentCart(ctx(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart saveCurrentCart(SessionContext ctx, String code) throws ConsistencyCheckException
    {
        Cart cloned = OrderManager.getInstance().createCart((AbstractOrder)getSession().getCart(), code);
        cloned.setUser(ctx, this);
        return cloned;
    }


    public Cart getCart(String code)
    {
        Map<String, Object> params = new HashMap<>(3);
        params.put("me", this);
        params.put("code", code);
        List<Cart> matches = FlexibleSearch.getInstance().search("GET {Cart} WHERE {user}=?me AND {code}=?code ORDER BY {" + PK + "} ASC", params, Cart.class).getResult();
        if(matches.isEmpty())
        {
            return null;
        }
        if(matches.size() > 1)
        {
            log.error("found multple user carts matching '" + code + "' : " + matches + " - choosing first one!");
        }
        return matches.get(0);
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "isAdminGroup(UserModel)")
    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAdmin()
    {
        return UserManager.getInstance().isAdmin(this);
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "isAdminEmployee(UserModel)")
    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAdminEmployee()
    {
        return Constants.USER.ADMIN_EMPLOYEE.equals(getLogin());
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "isAnonymousUser(UserModel)")
    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAnonymousCustomer()
    {
        return Constants.USER.ANONYMOUS_CUSTOMER.equals(getLogin());
    }


    @SLDSafe(portingClass = "UserDisplayNameLocalizedAttributeHandler", portingMethod = "get(final UserModel model)")
    @Deprecated(since = "ages", forRemoval = false)
    public String getDisplayName(SessionContext ctx)
    {
        return getName(ctx);
    }


    @SLDSafe(portingClass = "UserDisplayNameLocalizedAttributeHandler", portingMethod = "get(final UserModel model, final Locale loc)")
    @Deprecated(since = "ages", forRemoval = false)
    public Map<Language, String> getAllDisplayName(SessionContext ctx)
    {
        Map<Language, String> ret = new HashMap<>();
        String name = getName(ctx);
        if(name != null)
        {
            for(Language l : C2LManager.getInstance().getAllLanguages())
            {
                ret.put(l, name);
            }
        }
        return ret;
    }


    @SLDSafe(portingClass = "UserRemoveInterceptor", portingMethod = "onRemove(final Object model, final InterceptorContext ctx)")
    @Deprecated(since = "ages", forRemoval = false)
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        super.checkRemovable(ctx);
        String query = "GET {" + GeneratedCoreConstants.TC.ABSTRACTORDER + "} WHERE {user}=?user";
        Map<String, Object> params = new HashMap<>();
        params.put("user", getPK());
        Collection<AbstractOrder> orders = FlexibleSearch.getInstance().search(query, params, AbstractOrder.class).getResult();
        if(!orders.isEmpty())
        {
            throw new ConsistencyCheckException(null, "user " + this + " can not be removed since he still owns orders " +
                            EJBTools.toPKList(orders), -1);
        }
    }


    private boolean deactivationBlocked()
    {
        return (isAdminEmployee() || (getTenant().getConfig()
                        .getBoolean("user.deactivation.blockForAllAdmins", false) && isAdmin()));
    }
}
