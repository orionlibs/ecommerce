package de.hybris.platform.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.genericsearch.GenericSearchTranslator;
import de.hybris.platform.genericsearch.impl.GenericSearchQueryAdjuster;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.meta.MetaInformationManager;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.event.LegacyLoginFailureEvent;
import de.hybris.platform.jalo.security.event.LegacyLoginSuccessfulEvent;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.StandardSearchContext;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserNetCheckingStrategy;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.JaloPropertyContainerAdapter;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class JaloSession extends AbstractJaloSession
{
    private static final Logger LOG = Logger.getLogger(JaloSession.class.getName());
    private final AbstractTenant tenant;
    private final long tenantRestartMarker;
    public static final String IS_HMC_SESSION = "is.hmc.session";
    public static final String REMOVE_CART_ON_CLOSE = "session.remove.cart.on.close";
    public static final String SESSION_TIMEOUT = "default.session.timeout";
    public static final String PREFETCH_LANGUAGES = "session.prefetch.languages";
    public static final String CART_TYPE = "default.session.cart.type";
    public static final String HYBRIS_ASSERT_INTERNALS = "hybris.assert.internals";
    private static final String JALOSESSION_SPRING_ID = "jalosession";
    public static final String CART = "cart";
    public static final SessionContext CTX_NO_PREFTCH_LANGUAGES = new SessionContext();

    static
    {
        CTX_NO_PREFTCH_LANGUAGES.setAttribute("session.prefetch.languages", Collections.EMPTY_SET);
    }

    public <T extends Tenant> T getTenant()
    {
        return (T)this.tenant;
    }


    private static final Map<ApplicationContext, Boolean> gotSpringSessionBeanCache = new ConcurrentHashMap<>();
    private final String sessionID;

    static
    {
        Registry.registerTenantListener((TenantListener)new Object());
    }

    private transient AtomicLong lastAccessed = new AtomicLong(System.currentTimeMillis());
    private final long creationTime;
    private Map loginProperties;
    private SessionContext sessionContext;
    private volatile boolean closed = false;
    private volatile PK cartPK;
    private volatile transient Cart _cart;
    private volatile String httpSessionId = null;
    private volatile transient PriceFactory injectedPriceFactory;
    private static final String LOCAL_CTX_CREATION_STACK = "debug.local.session.context.creation.stack";


    static final JaloSession createInstance(Map props, Class<? extends JaloSession> sessionClass) throws JaloSecurityException
    {
        return createInstance(props, sessionClass, Collections.emptyMap());
    }


    static final JaloSession createInstance(Map props, Class<? extends JaloSession> sessionClass, Map initialAttributes) throws JaloSecurityException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Creating new JaloSession.Stack is: " + Utilities.getStackTraceAsString(new Exception()));
        }
        JaloSession session = createViaSpringIfBeanExists();
        if(session == null)
        {
            session = createViaJalo(sessionClass);
        }
        session.initSessionContext(props);
        session.init();
        session.setAttributes((initialAttributes == null) ? Collections.emptyMap() : initialAttributes);
        notifyExtensionsAfterSessionCreation(session);
        return session;
    }


    private static JaloSession createViaJalo(Class sessionClass)
    {
        try
        {
            return JaloSessionFactory.createWithSessionClass(sessionClass);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, "invalid session class " + sessionClass + " for creating a new JaloSession", 0);
        }
    }


    private static JaloSession createViaSpringIfBeanExists()
    {
        ApplicationContext ctx = Registry.getApplicationContext();
        if(ctx != null && hasSessionSpringBean(ctx))
        {
            return (JaloSession)ctx.getBean("jalosession", JaloSession.class);
        }
        return null;
    }


    private static boolean hasSessionSpringBean(ApplicationContext ctx)
    {
        Boolean ret = gotSpringSessionBeanCache.get(ctx);
        if(ret == null)
        {
            ret = Boolean.valueOf(ctx.containsBean("jalosession"));
            gotSpringSessionBeanCache.put(ctx, ret);
        }
        return ret.booleanValue();
    }


    public JaloSession()
    {
        if(!Registry.hasCurrentTenant())
        {
            throw new IllegalStateException("cannot create new jalo session outside tenant scope");
        }
        this.tenant = (AbstractTenant)Registry.getCurrentTenant();
        this.tenantRestartMarker = this.tenant.getTenantRestartMarker();
        setLastAccessed(System.currentTimeMillis());
        this.creationTime = System.currentTimeMillis();
        setLastAccessed(this.creationTime);
        this.sessionID = createSessionID();
    }


    public static boolean assureSessionNotStale(JaloSession session)
    {
        Preconditions.checkNotNull(session);
        AbstractTenant tenant = session.<AbstractTenant>getTenant();
        Preconditions.checkNotNull(tenant);
        if(isTenantNotAccessible(tenant))
        {
            return true;
        }
        boolean result = (tenant.getTenantRestartMarker() == session.tenantRestartMarker);
        if(!result)
        {
            LOG.warn("Jalo session (" + session.getSessionID() + ") has been created in context of tenant " + tenant + " which since that has been initialized - session is not adequate anymore (" + session.tenantRestartMarker + "<>" + tenant
                            .getTenantRestartMarker() + ")");
        }
        return result;
    }


    public static boolean isTenantNotAccessible(AbstractTenant tenant)
    {
        return (tenant.getState() == null || tenant.getState() == AbstractTenant.State.INACTIVE || tenant.isStopping());
    }


    protected void init()
    {
    }


    protected SessionContext createSessionContext(SessionContext original)
    {
        return createSessionContextImpl(original, false);
    }


    protected boolean isDefaultAnonymous(Map props)
    {
        return (JaloConnection.ANONYMOUS_LOGIN_PROPERTIES == props);
    }


    protected void initSessionContext(Map<?, ?> props) throws JaloSecurityException
    {
        this.loginProperties = (props != null) ? Collections.unmodifiableMap(props) : Collections.EMPTY_MAP;
        SessionContext ctx = createSessionContext(null);
        ctx.setSession(this);
        this.sessionContext = ctx;
        activate();
        User user = initUser(props, isDefaultAnonymous(props));
        ctx.initializeOnSessionStartup(user, getInitialSessionLanguage(user), getInitialSessionCurrency(user), this.tenant
                        .getTenantSpecificTimeZone(), this.injectedPriceFactory);
    }


    protected User initUser(Map props, boolean isDefaultAnonymousSession) throws JaloSecurityException
    {
        if(isDefaultAnonymousSession)
        {
            return (User)getUserManager().getAnonymousCustomer();
        }
        String login = null;
        String password = null;
        if(props != null)
        {
            login = (String)props.get("user.principal");
            password = (String)props.get("user.credentials");
        }
        return performLogin(login, password, props);
    }


    protected String createSessionID()
    {
        return "s" + PK.createUUIDPK(0).getLongValueAsString();
    }


    protected Language getInitialSessionLanguage(User user)
    {
        Language sessionLang = user.getSessionLanguage();
        return (sessionLang == null) ? C2LManager.getInstance().getDefaultLanguageForTenant(getTenant()) : sessionLang;
    }


    protected Currency getInitialSessionCurrency(User user)
    {
        Currency sessionCurrency = user.getSessionCurrency();
        return (sessionCurrency == null) ? C2LManager.getInstance().getDefaultCurrencyForTenant(getTenant()) : sessionCurrency;
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.lastAccessed = new AtomicLong(System.currentTimeMillis());
    }


    public String generateLoginTokenCookieValue() throws EJBPasswordEncoderNotFoundException
    {
        return generateLoginTokenCookieValue(getSessionContext().getLanguage().getIsoCode());
    }


    public String generateLoginTokenCookieValue(String iso) throws EJBPasswordEncoderNotFoundException
    {
        User user = getUser();
        return getUserManager().generateLoginTokenCookieValue(user.getUID(),
                        (iso != null) ? iso : getSessionContext().getLanguage().getIsoCode(), null);
    }


    public void storeLoginTokenCookie(HttpServletResponse response) throws EJBPasswordEncoderNotFoundException
    {
        if(response == null)
        {
            return;
        }
        User user = getUser();
        getUserManager().storeLoginTokenCookie(user.getUID(), getSessionContext().getLanguage().getIsoCode(), null, response);
    }


    protected User performLogin(LoginToken token, Map loginProperties) throws JaloSecurityException
    {
        if(token == null)
        {
            return null;
        }
        User user = token.getUser();
        if(user == null || !user.checkPassword(token))
        {
            throw new JaloSecurityException("wrong token based credentials! token was: " + token, 1000);
        }
        if(user.isLoginDisabledAsPrimitive())
        {
            throw new JaloSecurityException("Login for user has been disabled.", 201);
        }
        if(user.getDeactivationDate() != null && user.getDeactivationDate().toInstant().isBefore(Instant.now()))
        {
            throw new JaloSecurityException("User has bean deactivated.", 201);
        }
        Language language = token.getLanguage();
        if(language != null)
        {
            user.setSessionLanguage(language);
        }
        return user;
    }


    protected User performLogin(String login, String password, Map loginProperties) throws JaloSecurityException
    {
        User user;
        PK userPK = (PK)loginProperties.get("user.pk");
        if(userPK != null)
        {
            try
            {
                user = getItem(userPK);
            }
            catch(JaloItemNotFoundException e)
            {
                throw new JaloInvalidParameterException("session user pk '" + userPK + "' is invalid", 0);
            }
            catch(ClassCastException e)
            {
                throw new JaloInvalidParameterException("session user pk '" + userPK + "' does not belong to a user", 0);
            }
            if(password != null && !user.checkPassword(password))
            {
                throw new JaloSecurityException("wrong credentials", 1000);
            }
        }
        else
        {
            if(login == null || login.length() == 0)
            {
                throw new JaloInvalidParameterException("performLogin(): you have to specify 'user.principal'.", 200);
            }
            String sessionType = (String)loginProperties.get("session.type");
            boolean isAnonymousUser = isAnonymousCustomer(login);
            boolean isAnonymousSession = "anonymous".equals(sessionType);
            if(isAnonymousUser)
            {
                if(isAnonymousSession)
                {
                    Customer customer = getUserManager().getAnonymousCustomer();
                }
                else
                {
                    throw new JaloSecurityException("you must use createAnonymousCustomerSession() to log in as 'anonymous'", 0);
                }
            }
            else
            {
                if(isAnonymousSession)
                {
                    throw new JaloSecurityException("only the '" + Constants.USER.ANONYMOUS_CUSTOMER + "' customer can login with session type 'anonymous'", 0);
                }
                user = findSessionUser(findSessionUserType(sessionType), login);
            }
            validateLoginIsNotDisabed(login, user);
            if(!user.checkPassword(password))
            {
                getEventService().publishEvent((AbstractEvent)new LegacyLoginFailureEvent(user.getUid()));
                throw new JaloSecurityException("wrong credentials", 1000);
            }
        }
        validateLoginIsNotDisabed(login, user);
        LoginToken token = (LoginToken)loginProperties.get("login.token.url.parameter");
        if(token != null)
        {
            if(token.getUser().equals(user) && token.getLanguage() != null)
            {
                user.setSessionLanguage(token.getLanguage());
            }
        }
        getEventService().publishEvent((AbstractEvent)new LegacyLoginSuccessfulEvent(user.getUid()));
        return user;
    }


    private void validateLoginIsNotDisabed(String login, User user) throws JaloSecurityException
    {
        if(isAnonymousAndAnonymousLoginIsDisabled(login))
        {
            throw new JaloSecurityException("Anonymous login is disabled", 201);
        }
        if(user.isLoginDisabledAsPrimitive())
        {
            throw new JaloSecurityException("Login for user has been disabled.", 201);
        }
        if(user.getDeactivationDate() != null && user.getDeactivationDate().toInstant().isBefore(Instant.now()))
        {
            throw new JaloSecurityException("User has bean deactivated has been disabled.", 201);
        }
    }


    private EventService getEventService()
    {
        return (EventService)Registry.getApplicationContext().getBean("eventService", EventService.class);
    }


    protected boolean isAnonymousAndAnonymousLoginIsDisabled(String login)
    {
        return (isAnonymousCustomer(login) && Config.getBoolean("login.anonymous.always.disabled", true));
    }


    protected boolean isNotAnonymousOrAnonymousLoginIsAllowed(String login)
    {
        return (!isAnonymousCustomer(login) || !Config.getBoolean("login.anonymous.always.disabled", true));
    }


    private boolean isAnonymousCustomer(String login)
    {
        return Constants.USER.ANONYMOUS_CUSTOMER.equals(login);
    }


    protected ComposedType findSessionUserType(String sessionTypeString) throws JaloInvalidParameterException
    {
        try
        {
            if(StringUtils.isBlank(sessionTypeString))
            {
                return getTypeManager().getComposedType(User.class);
            }
            if("anonymous".equalsIgnoreCase(sessionTypeString) || "customer"
                            .equalsIgnoreCase(sessionTypeString))
            {
                return getTypeManager().getComposedType(Customer.class);
            }
            if("employee".equalsIgnoreCase(sessionTypeString))
            {
                return getTypeManager().getComposedType(Employee.class);
            }
            return getItem(PK.parse(sessionTypeString));
        }
        catch(ClassCastException e)
        {
            throw new JaloInvalidParameterException("session user type pk '" + sessionTypeString + "' doesnt belong to a composed type : " + e, 0);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInvalidParameterException("session user type pk '" + sessionTypeString + "' is invalid", 0);
        }
    }


    protected User findSessionUser(ComposedType userType, String login) throws JaloSecurityException, JaloInvalidParameterException
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT {").append(Item.PK).append("} FROM {").append(userType.getCode()).append("} ");
            query.append("WHERE {").append("uid").append("} = ?uid  ");
            Map<Object, Object> values = new HashMap<>();
            values.put("uid", login);
            SearchResult res = getFlexibleSearch().search(query.toString(), values, Collections.singletonList(User.class), true, true, 0, -1);
            int count = res.getCount();
            if(count > 1)
            {
                throw new JaloInvalidParameterException("found multiple users for login '" + login + "' and type " + userType + " : " + res
                                .getResult(), 0);
            }
            if(count == 0)
            {
                throw new JaloSecurityException("did not find user (type: " + userType + ")", 2000);
            }
            return res.getResult().iterator().next();
        }
        catch(ClassCastException e)
        {
            e.printStackTrace(System.err);
            throw new JaloInvalidParameterException("doesnt seem to be a User item: " + e, 0);
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloSystemException("Got FlexibleSearchException in findSessionUser: " + e);
        }
    }


    public void activate()
    {
        Registry.startup();
        if(!assureSessionNotStale(this))
        {
            throw new IllegalStateException("The session " + getSessionID() + " being activated is stale its origin tenant has been initialized or updated since the session was created");
        }
        JaloSession current = this.tenant.getActiveSession();
        if(!equals(current))
        {
            if(current != null)
            {
                current.removeAllLocalSessionContexts();
            }
            this.tenant.setActiveSessionForCurrentThread(this);
        }
        touch();
        getExtensionManager().notifyOnFirstSessionCreation();
    }


    public static void deactivate()
    {
        if(Registry.hasCurrentTenant())
        {
            AbstractTenant tenant = (AbstractTenant)Registry.getCurrentTenant();
            JaloSession current = tenant.getActiveSession();
            if(current != null)
            {
                current.removeAllLocalSessionContexts();
                current.setLastAccessed(System.currentTimeMillis());
                tenant.setActiveSessionForCurrentThread(null);
            }
        }
    }


    protected void setLastAccessed(long accessed)
    {
        this.lastAccessed.set(accessed);
    }


    public long getCreationTime()
    {
        return this.creationTime;
    }


    public long getLastAccessed()
    {
        return this.lastAccessed.get();
    }


    public static JaloSession getCurrentSession()
    {
        return getCurrentSession(Registry.getCurrentTenant());
    }


    public static JaloSession getCurrentSession(Tenant tenant)
    {
        JaloSession session = tenant.getActiveSession();
        if(session != null && !session.isClosed())
        {
            if(!assureSessionNotStale(session))
            {
                deactivate();
                try
                {
                    return tenant.getJaloConnection().createAnonymousCustomerSession();
                }
                catch(JaloSecurityException e)
                {
                    throw new JaloSystemException("Got JaloSecurityException in getCurrentSession: " + e);
                }
            }
            return session;
        }
        if(Registry.isCurrentTenant(tenant))
        {
            try
            {
                return tenant.getJaloConnection().createAnonymousCustomerSession();
            }
            catch(JaloSecurityException e)
            {
                throw new JaloSystemException("Got JaloSecurityException in getCurrentSession: " + e);
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Getting session for other tenant " + tenant + " than current " +
                            Registry.getCurrentTenantNoFallback());
        }
        return null;
    }


    protected void checkSessionValidity()
    {
        if(isClosed())
        {
            throw new SessionClosedException("session " + getSessionID() + " is already closed!");
        }
    }


    public void touch()
    {
        setLastAccessed(System.currentTimeMillis());
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public int getTimeout()
    {
        return -1;
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public void setTimeout(int seconds)
    {
        setTimeoutInMillis(1000L * seconds);
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public void setTimeoutInMillis(long timeOutInMillis)
    {
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public boolean isExpired()
    {
        return false;
    }


    protected int getDefaultSessionTimeout()
    {
        return Config.getInt("default.session.timeout", 86400);
    }


    public static JaloSession internalGetCurrentSession()
    {
        return Registry.hasCurrentTenant() ? Registry.getCurrentTenant().getActiveSession() : null;
    }


    public static boolean hasCurrentSession()
    {
        return (Registry.hasCurrentTenant() && hasCurrentSession(Registry.getCurrentTenant()));
    }


    public static boolean hasCurrentSession(Tenant tenant)
    {
        JaloSession session = (tenant != null) ? tenant.getActiveSession() : null;
        return (session != null && !session.isClosed() && assureSessionNotStale(session));
    }


    public User getUser()
    {
        return getSessionContext().getUser();
    }


    public void setUser(User user)
    {
        if(user == null)
        {
            throw new JaloInvalidParameterException("session user cannot be null", 0);
        }
        if(!user.equals(getUser()))
        {
            User previous = getUser();
            getSessionContext().setUser(user);
            if(hasCart())
            {
                getCart().setUser(user);
            }
            notifyExtensionsAfterUserChange(previous);
        }
    }


    public void transfer(Map props) throws JaloSecurityException, JaloInvalidParameterException
    {
        transfer(props, false);
    }


    public void transfer(String user, String pwd) throws JaloSecurityException
    {
        Map<Object, Object> props = new HashMap<>();
        props.put("user.principal", user);
        props.put("user.credentials", pwd);
        transfer(props, false);
    }


    public void transfer(Map props, boolean activateUserLanguageAndCurrency) throws JaloSecurityException, JaloInvalidParameterException
    {
        User newUser;
        if(isDefaultAnonymous(props))
        {
            Customer customer = UserManager.getInstance().getAnonymousCustomer();
        }
        else
        {
            LoginToken loginToken = getLoginToken(props);
            if(loginToken != null)
            {
                newUser = performLogin(loginToken, props);
            }
            else
            {
                String login = null;
                String password = null;
                if(props != null)
                {
                    login = (String)props.get("user.principal");
                    password = (String)props.get("user.credentials");
                }
                newUser = performLogin(login, password, props);
            }
        }
        setUser(newUser);
        if(activateUserLanguageAndCurrency)
        {
            Currency userCurr = newUser.getSessionCurrency();
            Language userLang = newUser.getSessionLanguage();
            if(userCurr != null)
            {
                getSessionContext().setCurrency(userCurr);
            }
            if(userLang != null)
            {
                getSessionContext().setLanguage(userLang);
            }
        }
        Language currentLanguage = getSessionContext().getLanguage();
        Collection<Language> activeLanguages = getC2LManager().getActiveLanguages();
        if(activeLanguages.isEmpty())
        {
            throw new JaloSystemException(null, "No active language found for new session of user " + newUser.getLogin() + ". Check restrictions.", 0);
        }
        if(!currentLanguage.isAlive() || !activeLanguages.contains(currentLanguage))
        {
            Language newLanguage = activeLanguages.iterator().next();
            LOG.info("transfering session to user " + newUser.getLogin() + ": forced to switch langanguage to " + newLanguage
                            .getIsoCode());
            getSessionContext().setLanguage(newLanguage);
        }
        Currency currentCurrency = getSessionContext().getCurrency();
        Collection<Currency> activeCurrencies = getC2LManager().getActiveCurrencies();
        if(activeCurrencies.isEmpty())
        {
            throw new JaloSystemException(null, "No active currency found for new session of user " + newUser.getLogin() + ". Check restrictions.", 0);
        }
        if(!currentCurrency.isAlive() || !activeCurrencies.contains(currentCurrency))
        {
            Currency newCurr = activeCurrencies.iterator().next();
            LOG.info("transfering session to user " + newUser
                            .getLogin() + ": forced to switch currency to " + newCurr.getIsoCode());
            getSessionContext().setCurrency(newCurr);
        }
        setLoginProperties(props);
    }


    protected LoginToken getLoginToken(Map props)
    {
        if(props != null && props.get("user.principal") == null && props.get("user.pk") == null && props
                        .get("login.token.url.parameter") != null)
        {
            return (LoginToken)props.get("login.token.url.parameter");
        }
        return null;
    }


    public Cart getCart()
    {
        if(this.cartPK == null)
        {
            synchronized(this)
            {
                if(this.cartPK == null)
                {
                    setAttachedCart(createCart());
                }
            }
        }
        return getAttachedCart();
    }


    protected Cart getAttachedCart()
    {
        if(this.cartPK == null)
        {
            return null;
        }
        try
        {
            this._cart = getItem(this.cartPK);
            return this._cart;
        }
        catch(JaloItemNotFoundException e)
        {
            return this._cart;
        }
    }


    protected void setAttachedCart(Cart cart)
    {
        this._cart = cart;
        this.cartPK = (this._cart == null) ? null : this._cart.getPK();
    }


    protected Cart createCart()
    {
        User user = getUser();
        return doCreateCart(
                        getCartType(), user,
                        getSessionContext().getCurrency(), new Date(),
                        isNetUser(user));
    }


    private boolean isNetUser(User user)
    {
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        UserModel userModel = (UserModel)modelService.get(user);
        UserNetCheckingStrategy userNetCheckingStrategy = (UserNetCheckingStrategy)Registry.getApplicationContext().getBean("userNetCheckingStrategy", UserNetCheckingStrategy.class);
        return userNetCheckingStrategy.isNetUser(userModel);
    }


    protected ComposedType getCartType()
    {
        ComposedType ret = null;
        Object cardType = getSessionContext().getAttribute("default.session.cart.type");
        if(cardType != null)
        {
            ComposedType composedType = null;
            if(cardType instanceof ComposedType)
            {
                composedType = (ComposedType)cardType;
            }
            else if(cardType instanceof String)
            {
                try
                {
                    composedType = TypeManager.getInstance().getComposedType((String)cardType);
                }
                catch(JaloItemNotFoundException e)
                {
                    LOG.error("invalid session context cart type " + cardType + " - cannot find composed type");
                }
            }
            if(composedType != null && !Cart.class.isAssignableFrom(composedType.getJaloClass()))
            {
                LOG.error("invalid session context cart type " + composedType.getCode() + " - is not assignable from Cart class");
                composedType = null;
            }
            ret = composedType;
        }
        if(ret == null)
        {
            String cfgType = getTenant().getConfig().getParameter("default.session.cart.type");
            if(StringUtils.isNotBlank(cfgType))
            {
                ComposedType composedType = null;
                try
                {
                    composedType = TypeManager.getInstance().getComposedType(cfgType);
                }
                catch(JaloItemNotFoundException e)
                {
                    LOG.error("invalid configured cart type " + cardType + " - cannot find composed type");
                }
                if(composedType != null && !Cart.class.isAssignableFrom(composedType.getJaloClass()))
                {
                    LOG.error("invalid session context cart type " + composedType
                                    .getCode() + " - is not assignable from Cart class");
                    composedType = null;
                }
                ret = composedType;
            }
        }
        return ret;
    }


    protected Cart doCreateCart(ComposedType cartType, User user, Currency curr, Date date, boolean isNet)
    {
        try
        {
            return getOrderManager().createCart(cartType, (String)null, user, curr, date, isNet);
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e, "cart creation failed ", 290774);
        }
    }


    public boolean hasCart()
    {
        return (getAttachedCart() != null);
    }


    public void removeCart()
    {
        Cart tmpCart = getAttachedCart();
        if(tmpCart != null)
        {
            synchronized(this)
            {
                tmpCart = getAttachedCart();
                if(tmpCart != null)
                {
                    try
                    {
                        tmpCart.remove();
                    }
                    catch(Exception e)
                    {
                        LOG.error("Couldn't remove cart" + tmpCart.getPK());
                    }
                    finally
                    {
                        setAttachedCart(null);
                    }
                }
            }
        }
    }


    public void setCart(Cart cart)
    {
        Cart old = getAttachedCart();
        if(cart != old && (cart == null || !cart.equals(old)))
        {
            removeCart();
            setAttachedCart(cart);
        }
    }


    public void restoreSavedCart(Cart savedCart)
    {
        getCart().removeAllEntries();
        appendSavedCart(savedCart);
    }


    public void appendSavedCart(Cart appendCart)
    {
        getOrderManager().appendToCart(appendCart, getCart());
    }


    public Map getLoginProperties()
    {
        return this.loginProperties;
    }


    protected void setLoginProperties(Map loginProperties)
    {
        this.loginProperties = (Map)new CaseInsensitiveMap(loginProperties);
    }


    public void setAttributes(Map attributes)
    {
        for(Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)attributes.entrySet())
        {
            setAttribute(e.getKey(), e.getValue());
        }
    }


    public Map getAttributes()
    {
        Map<String, Object> attributes = getSessionContext().getAttributes();
        Cart cart = getAttachedCart();
        if(cart != null)
        {
            Map<String, Object> tmp = new HashMap<>(attributes);
            tmp.put("cart", cart);
            attributes = Collections.unmodifiableMap(tmp);
        }
        return attributes;
    }


    public Object getAttribute(String name)
    {
        if("cart".equalsIgnoreCase(name))
        {
            return getAttachedCart();
        }
        return getSessionContext().getAttribute(name);
    }


    public Object setAttribute(String name, Object value)
    {
        if("cart".equalsIgnoreCase(name))
        {
            Cart old = getAttachedCart();
            setCart((Cart)value);
            return old;
        }
        return getSessionContext().setAttribute(name, value);
    }


    public Enumeration getAttributeNames()
    {
        Collection<String> names = getSessionContext().getAllAttributeNames();
        if(hasCart())
        {
            Collection<String> tmp = new HashSet<>(names);
            tmp.add("cart");
            names = tmp;
        }
        return Collections.enumeration(names);
    }


    public Object removeAttribute(String name)
    {
        if("cart".equalsIgnoreCase(name))
        {
            Cart old = getAttachedCart();
            removeCart();
            return old;
        }
        return getSessionContext().removeAttribute(name);
    }


    public AccessManager getAccessManager()
    {
        return getTenant().getJaloConnection().getAccessManager();
    }


    public UserManager getUserManager()
    {
        return getTenant().getJaloConnection().getUserManager();
    }


    public ProductManager getProductManager()
    {
        return getTenant().getJaloConnection().getProductManager();
    }


    public NumberSeriesManager getNumberSeriesManager()
    {
        return getTenant().getJaloConnection().getNumberSeriesManager();
    }


    public FlexibleSearch getFlexibleSearch()
    {
        return getTenant().getJaloConnection().getFlexibleSearch();
    }


    public C2LManager getC2LManager()
    {
        return getTenant().getJaloConnection().getC2LManager();
    }


    public MetaInformationManager getMetaInformationManager()
    {
        return getTenant().getJaloConnection().getMetaInformationManager();
    }


    public MediaManager getMediaManager()
    {
        return getTenant().getJaloConnection().getMediaManager();
    }


    public OrderManager getOrderManager()
    {
        return getTenant().getJaloConnection().getOrderManager();
    }


    public LinkManager getLinkManager()
    {
        return getTenant().getJaloConnection().getLinkManager();
    }


    public ExtensionManager getExtensionManager()
    {
        return getTenant().getJaloConnection().getExtensionManager();
    }


    public TypeManager getTypeManager()
    {
        return getTenant().getJaloConnection().getTypeManager();
    }


    public EnumerationManager getEnumerationManager()
    {
        return getTenant().getJaloConnection().getEnumerationManager();
    }


    public SessionContext getSessionContext()
    {
        SessionContext localCtx = getLocalSessionContext();
        if(this.sessionContext == null)
        {
            checkSessionValidity();
        }
        return (localCtx != null) ? localCtx : this.sessionContext;
    }


    protected SessionContext getLocalSessionContext()
    {
        LinkedList<SessionContext> list = (LinkedList<SessionContext>)this.tenant.getActiveSessionContextList();
        return !list.isEmpty() ? list.getLast() : null;
    }


    protected void addLocalSessionContext(SessionContext localCtx)
    {
        LinkedList<SessionContext> list = (LinkedList)this.tenant.getActiveSessionContextList();
        list.add(localCtx);
    }


    public SessionContext createSessionContext()
    {
        return createSessionContext(getSessionContext());
    }


    public SessionContext createLocalSessionContext()
    {
        return createLocalSessionContext(getSessionContext());
    }


    public SessionContext createLocalSessionContext(SessionContext original)
    {
        SessionContext newLocalCtx = createSessionContextImpl(original, true);
        if(LOG.isDebugEnabled())
        {
            newLocalCtx.setAttribute("debug.local.session.context.creation.stack", new RuntimeException());
        }
        addLocalSessionContext(newLocalCtx);
        return newLocalCtx;
    }


    private SessionContext createSessionContextImpl(SessionContext original, boolean asLocal)
    {
        return new SessionContext(original, asLocal);
    }


    public void removeLocalSessionContext()
    {
        LinkedList list = (LinkedList)this.tenant.getActiveSessionContextList();
        if(!list.isEmpty())
        {
            list.removeLast();
        }
    }


    protected void removeAllLocalSessionContexts()
    {
        List<SessionContext> list = this.tenant.getActiveSessionContextList();
        if(!list.isEmpty())
        {
            String msg = "There are some active local session contexts left (" + list.size() + ") for tenant " + this.tenant.getTenantID() + ", this might indicate there are not properly freed localsession context instances.";
            if(LOG.isDebugEnabled())
            {
                LOG.warn(msg, new RuntimeException());
                for(SessionContext localCtx : list)
                {
                    LOG.debug("Open local context:", (Throwable)localCtx.getAttribute("debug.local.session.context.creation.stack"));
                }
            }
            else
            {
                LOG.warn(msg);
            }
        }
        list.clear();
    }


    public SearchContext createSearchContext()
    {
        return (SearchContext)new StandardSearchContext(getSessionContext());
    }


    public static <T extends Item> T lookupItem(PK pk) throws JaloItemNotFoundException
    {
        return (T)WrapperFactory.getCachedItem(Registry.getCurrentTenantNoFallback().getCache(), pk);
    }


    public static <T extends Item> T lookupItem(Tenant tenant, PK pk) throws JaloItemNotFoundException
    {
        return (T)WrapperFactory.getCachedItem(tenant.getCache(), pk);
    }


    public static Collection<? extends Item> lookupItems(SessionContext ctx, Collection<PK> pks) throws JaloItemNotFoundException
    {
        return lookupItems(ctx, pks, false);
    }


    public static Collection<? extends Item> lookupItems(SessionContext ctx, Collection<PK> pks, boolean ignoreMissingItems) throws JaloItemNotFoundException
    {
        return lookupItems(ctx, pks, ignoreMissingItems, false);
    }


    public static Collection<? extends Item> lookupItems(SessionContext ctx, Collection<PK> pks, boolean ignoreMissingItems, boolean returnMissingAsNull) throws JaloItemNotFoundException
    {
        return lookupItems(Registry.getCurrentTenantNoFallback(), ctx, pks, ignoreMissingItems, returnMissingAsNull);
    }


    public static Collection<? extends Item> lookupItems(Tenant tenant, SessionContext ctx, Collection<PK> pks, boolean ignoreMissingItems, boolean returnMissingAsNull) throws JaloItemNotFoundException
    {
        if(pks == null)
        {
            return null;
        }
        if(pks.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        if(pks.size() == 1)
        {
            try
            {
                return Collections.singleton(lookupItem(pks.iterator().next()));
            }
            catch(JaloItemNotFoundException e)
            {
                if(ignoreMissingItems)
                {
                    return Collections.EMPTY_LIST;
                }
                throw e;
            }
        }
        return WrapperFactory.getCachedItems(tenant.getCache(), pks, WrapperFactory.getPrefetchLanguages(ctx), ignoreMissingItems, returnMissingAsNull);
    }


    public <T extends Item> T getItem(PK pk) throws JaloItemNotFoundException
    {
        return lookupItem(pk);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public <T extends Item> T getItem(String pk) throws JaloItemNotFoundException, IllegalArgumentException
    {
        return lookupItem(PK.parse(pk));
    }


    public Collection<? extends Item> getItems(Collection<PK> pks) throws JaloItemNotFoundException
    {
        return lookupItems(getSessionContext(), pks);
    }


    public Collection<? extends Item> getItems(SessionContext ctx, Collection<PK> pks) throws JaloItemNotFoundException
    {
        return lookupItems(ctx, pks, false);
    }


    public Collection<? extends Item> getItems(SessionContext ctx, Collection<PK> pks, boolean ignoreMissingItems) throws JaloItemNotFoundException
    {
        return lookupItems(ctx, pks, ignoreMissingItems, false);
    }


    public Collection<? extends Item> getItems(SessionContext ctx, Collection<PK> pks, boolean ignoreMissingItems, boolean returnMissingAsNull) throws JaloItemNotFoundException
    {
        return lookupItems(ctx, pks, ignoreMissingItems, returnMissingAsNull);
    }


    public String getSessionID()
    {
        return this.sessionID;
    }


    public String toString()
    {
        return "JaloSession: (" + getTenantIDSafe() + "/" + getSessionID() + ",USERUID:" + getUIDSafe() + ")";
    }


    private String getTenantIDSafe()
    {
        Tenant tenant = getTenant();
        return (tenant != null) ? tenant.getTenantID() : "n/a";
    }


    private String getUIDSafe()
    {
        try
        {
            return getUser().getLogin();
        }
        catch(NullPointerException e)
        {
            return "<no user>";
        }
        catch(JaloObjectNoLongerValidException e)
        {
            return "<user removed>";
        }
        catch(Exception e)
        {
            return "<unknown error:" + e.getMessage() + ">";
        }
    }


    public boolean isClosed()
    {
        return this.closed;
    }


    public void close()
    {
        if(!this.closed)
        {
            notifyExtensionsBeforeSessionClose();
            try
            {
                cleanup();
                if(equals(this.tenant.getActiveSession()))
                {
                    deactivate();
                }
            }
            finally
            {
                this.closed = true;
            }
        }
    }


    protected void cleanup()
    {
        try
        {
            if(isRemoveCartOnSessionClose())
            {
                if(!hasCurrentSession() || !equals(getCurrentSession()))
                {
                    JaloSession prev = hasCurrentSession() ? getCurrentSession() : null;
                    try
                    {
                        activate();
                        removeCart();
                    }
                    finally
                    {
                        if(prev != null)
                        {
                            prev.activate();
                        }
                        else
                        {
                            deactivate();
                        }
                    }
                }
                else
                {
                    removeCart();
                }
            }
        }
        finally
        {
            this.loginProperties = null;
            this.sessionContext = null;
        }
    }


    protected boolean isRemoveCartOnSessionClose()
    {
        return Config.getBoolean("session.remove.cart.on.close", true);
    }


    public JaloPropertyContainer createPropertyContainer()
    {
        return (JaloPropertyContainer)new JaloPropertyContainerAdapter(this);
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public HttpSession getHttpSession()
    {
        return null;
    }


    public void setHttpSessionId(String httpSessionId)
    {
        this.httpSessionId = httpSessionId;
    }


    public String getHttpSessionId()
    {
        return this.httpSessionId;
    }


    public SearchResult search(GenericQuery query, SearchContext ctx)
    {
        return search(query, (Principal)getUser(), ctx);
    }


    public SearchResult search(GenericQuery query)
    {
        return search(query, createSearchContext());
    }


    public void setPriceFactory(PriceFactory factory)
    {
        if(this.sessionContext == null)
        {
            this.injectedPriceFactory = factory;
        }
        else
        {
            getSessionContext().setPriceFactory(factory);
        }
    }


    public PriceFactory getPriceFactory()
    {
        if(this.sessionContext == null)
        {
            return this.injectedPriceFactory;
        }
        return getSessionContext().getPriceFactory();
    }


    public SearchResult search(GenericQuery genericQuery, Principal principal, SearchContext searchCtx)
    {
        boolean dontNeedTotal = (searchCtx.getProperty("genericsearch.dontNeedTotal") != null) ? ((Boolean)searchCtx.getProperty("genericsearch.dontNeedTotal")).booleanValue() : true;
        SessionContext sessionCtx = createSessionContext();
        sessionCtx.setLanguage(searchCtx.getLanguage());
        ComposedType initialType = getTypeManager().getComposedType(genericQuery.getInitialTypeCode());
        if(initialType instanceof ViewType)
        {
            ViewType viewType = (ViewType)initialType;
            return viewType.search(sessionCtx, viewType.convertGenericValueConditions(genericQuery.getCondition()), dontNeedTotal, searchCtx
                            .getRangeStart(), searchCtx.getRangeCount());
        }
        Map<Object, Object> values = new HashMap<>();
        GenericSearchQueryAdjuster.getDefault().adjust(genericQuery);
        String query = GenericSearchTranslator.translate(genericQuery, values);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("QUERY: " + query);
            LOG.debug("VALUES: " + values);
        }
        return getFlexibleSearch().search(sessionCtx, query, values, genericQuery.getResultClasses(), false, dontNeedTotal, searchCtx
                        .getRangeStart(), searchCtx.getRangeCount());
    }


    protected void notifyExtensionsBeforeSessionClose()
    {
        for(JaloSessionListener listener : JaloConnection.getInstance().getJaloSessionListenersIncludingManagers())
        {
            try
            {
                listener.beforeSessionClose(this);
            }
            catch(Exception ex)
            {
                if(!RedeployUtilities.isShutdownInProgress())
                {
                    LOG.error("error notifying session listener " + listener + " before close of " + this, ex);
                    continue;
                }
                LOG.error("error notifying session listener " + listener + " before close of " + this);
            }
        }
    }


    private void notifyExtensionsAfterUserChange(User previous)
    {
        try
        {
            for(JaloSessionListener listener : JaloConnection.getInstance().getJaloSessionListenersIncludingManagers())
            {
                listener.afterSessionUserChange(this, previous);
            }
        }
        catch(Exception e)
        {
            if(!RedeployUtilities.isShutdownInProgress())
            {
                LOG.error("Got exception in notifyExtensionsAfterUserChange", e);
            }
            else
            {
                LOG.warn("Error in notifyExtensionsAfterUserChange : " + e.getMessage());
            }
        }
    }


    private static void notifyExtensionsAfterSessionCreation(JaloSession session)
    {
        try
        {
            for(JaloSessionListener listener : JaloConnection.getInstance().getJaloSessionListenersIncludingManagers())
            {
                listener.afterSessionCreation(session);
            }
        }
        catch(Exception e)
        {
            if(!RedeployUtilities.isShutdownInProgress())
            {
                LOG.error("Got exception in notifyExtensionsAfterSessionCreation", e);
            }
            else
            {
                LOG.warn("Error in notifyExtensionsAfterSessionCreation : " + e.getMessage());
            }
        }
    }


    public final boolean equals(Object object)
    {
        if(object == this)
        {
            return true;
        }
        return (object instanceof JaloSession && ((JaloSession)object).getSessionID().equals(getSessionID()));
    }


    public int compareTo(Object object)
    {
        if(!(object instanceof JaloSession))
        {
            return 1;
        }
        return getSessionID().compareTo(((JaloSession)object).getSessionID());
    }


    public final int hashCode()
    {
        return getSessionID().hashCode();
    }
}
