package de.hybris.platform.jalo;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.hmc.jalo.ChangedItemAttributes;
import de.hybris.platform.hmc.jalo.SavedValueEntry;
import de.hybris.platform.hmc.jalo.SavedValues;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.meta.MetaInformationManager;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.persistence.security.PasswordEncoderFactory;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

public class JaloConnection
{
    private static final Logger LOG = Logger.getLogger(JaloConnection.class.getName());
    public static final String SESSION_CLASS = "session.class";
    public static final String DEFAULT_SESSION_CLASS = "default.session.class";
    public static final String LOGHMCCHANGES = "logHMCChanges".intern();
    private final Tenant tenant;
    private final List<JaloSessionListener> jaloSessionListeners;
    private volatile ProductManager productManager;
    private volatile UserManager userManager;
    private volatile C2LManager c2lManager;
    private volatile MetaInformationManager metaInformationManager;
    private volatile ExtensionManager extensionManager;
    private volatile MediaManager mediaManager;
    private volatile AccessManager accessManager;
    private volatile NumberSeriesManager numberSeriesManager;
    private volatile LinkManager linkManager;
    private volatile EnumerationManager enumerationManager;
    private volatile TypeManager typeManager;
    private volatile OrderManager orderManager;
    private volatile FlexibleSearch flexibleSearch;
    private volatile PasswordEncoderFactory passwordEncoderFactory;
    private volatile List<Manager> allManagers;
    private volatile Collection<JaloSessionListener> sessionListenersIncludingManagers;
    public static final Properties ANONYMOUS_LOGIN_PROPERTIES = new Properties();
    private final AbstractSynchronizedPropertyGetter<AccessManager> accessManagerGetter = (AbstractSynchronizedPropertyGetter<AccessManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<ProductManager> productManagerGetter = (AbstractSynchronizedPropertyGetter<ProductManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<UserManager> userManagerGetter = (AbstractSynchronizedPropertyGetter<UserManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<C2LManager> c2lManagerGetter = (AbstractSynchronizedPropertyGetter<C2LManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<MetaInformationManager> metaManagerGetter = (AbstractSynchronizedPropertyGetter<MetaInformationManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<ExtensionManager> extensionManagerGetter = (AbstractSynchronizedPropertyGetter<ExtensionManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<MediaManager> mediaManagerGetter = (AbstractSynchronizedPropertyGetter<MediaManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<NumberSeriesManager> numberManagerGetter = (AbstractSynchronizedPropertyGetter<NumberSeriesManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<LinkManager> linkManagerGetter = (AbstractSynchronizedPropertyGetter<LinkManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<EnumerationManager> enumManagerGetter = (AbstractSynchronizedPropertyGetter<EnumerationManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<TypeManager> typeManagerGetter = (AbstractSynchronizedPropertyGetter<TypeManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<OrderManager> orderManagerGetter = (AbstractSynchronizedPropertyGetter<OrderManager>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<FlexibleSearch> searchManagerGetter = (AbstractSynchronizedPropertyGetter<FlexibleSearch>)new Object(this);
    private final AbstractSynchronizedPropertyGetter<PasswordEncoderFactory> encoderFactoryGetter = (AbstractSynchronizedPropertyGetter<PasswordEncoderFactory>)new Object(this);


    public static JaloConnection getInstance()
    {
        return ((AbstractTenant)Registry.getCurrentTenant()).getJaloConnection();
    }


    public JaloConnection(Tenant tenant)
    {
        this.tenant = tenant;
        this.jaloSessionListeners = new CopyOnWriteArrayList<>();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public JaloConnection()
    {
        this(Registry.getCurrentTenant());
    }


    @Deprecated(since = "5.0", forRemoval = false)
    public void setTenant(Tenant tenant)
    {
        throw new UnsupportedOperationException("Tenant id should not mutate during  JaloConnection life time");
    }


    public Tenant getTenant()
    {
        return this.tenant;
    }


    public void registerJaloSessionListener(JaloSessionListener jaloSessionListener)
    {
        this.jaloSessionListeners.add(jaloSessionListener);
    }


    public void unregisterJaloSessionListener(JaloSessionListener jaloSessionListener)
    {
        this.jaloSessionListeners.remove(jaloSessionListener);
    }


    public List<JaloSessionListener> getJaloSessionListeners()
    {
        return Collections.unmodifiableList(this.jaloSessionListeners);
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public int cleanExpiredSessions()
    {
        return cleanExpiredSessions(false);
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public int cleanExpiredSessions(boolean force)
    {
        return -1;
    }


    protected Class guessSessionClass(Map props)
    {
        Class<?> clazz = (props != null) ? (Class)props.get("session.class") : null;
        if(clazz == null)
        {
            String className = getTenant().getConfig().getString("default.session.class", null);
            if(className != null)
            {
                try
                {
                    clazz = Class.forName(className);
                }
                catch(ClassNotFoundException e)
                {
                    LOG.warn("session class " + className + " not found, using default.");
                }
            }
        }
        if(clazz != null && !JaloSession.class.isAssignableFrom(clazz))
        {
            LOG.error("configured session class " + clazz + " is no subclass of JaloSession - using default instead");
            clazz = JaloSession.class;
        }
        return (clazz != null) ? clazz : JaloSession.class;
    }


    public JaloSession createAnonymousCustomerSession() throws JaloSecurityException
    {
        return createAnonymousCustomerSession(guessSessionClass(null));
    }


    public JaloSession createAnonymousCustomerSession(Map initialAttributes) throws JaloSecurityException
    {
        try
        {
            return createSession(ANONYMOUS_LOGIN_PROPERTIES, guessSessionClass(null), initialAttributes);
        }
        catch(JaloInvalidParameterException e)
        {
            throw new JaloInternalException(e, "no anonymous user found", 0);
        }
    }


    public JaloSession createAnonymousCustomerSession(Class sessionClass) throws JaloSecurityException
    {
        try
        {
            return createSession(ANONYMOUS_LOGIN_PROPERTIES, sessionClass);
        }
        catch(JaloInvalidParameterException e)
        {
            throw new JaloInternalException(e, "no anonymous user found", 0);
        }
    }


    public JaloSession createSession(Map loginProperties) throws JaloSecurityException, JaloInvalidParameterException
    {
        return createSession(loginProperties, guessSessionClass(loginProperties));
    }


    public JaloSession createSession(Map loginProperties, Class sessionClass) throws JaloSecurityException, JaloInvalidParameterException
    {
        return createSession(loginProperties, sessionClass, Collections.emptyMap());
    }


    public JaloSession createSession(Map loginProperties, Class sessionClass, Map initialAttributes) throws JaloSecurityException, JaloInvalidParameterException
    {
        JaloSession oldSession = null;
        oldSession = JaloSession.internalGetCurrentSession();
        try
        {
            JaloSession.deactivate();
            return JaloSession.createInstance(loginProperties, sessionClass,
                            (initialAttributes == null) ? Collections.emptyMap() : initialAttributes);
        }
        catch(JaloSecurityException e)
        {
            if(oldSession != null)
            {
                oldSession.activate();
            }
            throw e;
        }
        catch(JaloInvalidParameterException e)
        {
            if(oldSession != null)
            {
                oldSession.activate();
            }
            throw e;
        }
        catch(RuntimeException e)
        {
            if(!isSystemInitialized())
            {
                throw new JaloSecurityException("system not initialized", 0);
            }
            if(oldSession != null)
            {
                oldSession.activate();
            }
            throw e;
        }
    }


    public JaloSession createSession(String user, String pwd) throws JaloSecurityException
    {
        Map<Object, Object> map = new HashMap<>();
        map.put("user.principal", user);
        map.put("user.credentials", pwd);
        return createSession(map);
    }


    public JaloSession createSession(User user) throws JaloSecurityException
    {
        return createSession(Collections.singletonMap("user.pk", user.getPK()));
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public Collection<JaloSession> getAllSessions()
    {
        return Collections.EMPTY_LIST;
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public JaloSession getSession(String sessionID)
    {
        JaloSession current = JaloSession.hasCurrentSession() ? JaloSession.getCurrentSession() : null;
        if(current != null && current.getSessionID().equals(sessionID))
        {
            return current;
        }
        return null;
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public Collection<JaloSession> getExpiredSessions()
    {
        return Collections.EMPTY_LIST;
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public void checkAndAddToSessionCache(JaloSession session)
    {
    }


    public String getSystemName()
    {
        return getTenant().getSystemEJB().getMetaInformationManager().getSystemName();
    }


    public boolean isSystemInitialized()
    {
        boolean isSystemLocked = Config.getBoolean("system.unlocking.disabled", false);
        if(isSystemLocked)
        {
            return true;
        }
        return Utilities.isSystemInitialized(getTenant().getDataSource());
    }


    public String toString()
    {
        return "JaloConnection " + hashCode();
    }


    public boolean equals(Object object)
    {
        return (this == object);
    }


    public int hashCode()
    {
        return super.hashCode();
    }


    public UserManager getUserManager()
    {
        return (UserManager)this.userManagerGetter.get();
    }


    public ProductManager getProductManager()
    {
        return (ProductManager)this.productManagerGetter.get();
    }


    public NumberSeriesManager getNumberSeriesManager()
    {
        return (NumberSeriesManager)this.numberManagerGetter.get();
    }


    public FlexibleSearch getFlexibleSearch()
    {
        return (FlexibleSearch)this.searchManagerGetter.get();
    }


    public C2LManager getC2LManager()
    {
        return (C2LManager)this.c2lManagerGetter.get();
    }


    public MetaInformationManager getMetaInformationManager()
    {
        return (MetaInformationManager)this.metaManagerGetter.get();
    }


    public MediaManager getMediaManager()
    {
        return (MediaManager)this.mediaManagerGetter.get();
    }


    public OrderManager getOrderManager()
    {
        return (OrderManager)this.orderManagerGetter.get();
    }


    public PasswordEncoder getPasswordEncoder(String encoding) throws PasswordEncoderNotFoundException
    {
        return getPasswordEncoderFactory().getEncoder(encoding);
    }


    public PasswordEncoderFactory getPasswordEncoderFactory()
    {
        return (PasswordEncoderFactory)this.encoderFactoryGetter.get();
    }


    public LinkManager getLinkManager()
    {
        return (LinkManager)this.linkManagerGetter.get();
    }


    public ExtensionManager getExtensionManager()
    {
        return (ExtensionManager)this.extensionManagerGetter.get();
    }


    public TypeManager getTypeManager()
    {
        return (TypeManager)this.typeManagerGetter.get();
    }


    public EnumerationManager getEnumerationManager()
    {
        return (EnumerationManager)this.enumManagerGetter.get();
    }


    public AccessManager getAccessManager()
    {
        return (AccessManager)this.accessManagerGetter.get();
    }


    public Collection<JaloSessionListener> getJaloSessionListenersIncludingManagers()
    {
        if(this.sessionListenersIncludingManagers == null)
        {
            Collection<JaloSessionListener> listeners = new LinkedHashSet<>(5);
            listeners.addAll(getJaloSessionListeners());
            for(Extension e : this.extensionManager.getExtensions())
            {
                if(e instanceof JaloSessionListener)
                {
                    listeners.add((JaloSessionListener)e);
                }
            }
            this.sessionListenersIncludingManagers = Collections.unmodifiableCollection(listeners);
        }
        return this.sessionListenersIncludingManagers;
    }


    public List<Manager> getManagers()
    {
        if(this.allManagers == null)
        {
            this.allManagers = Collections.unmodifiableList(Arrays.asList(new Manager[] {
                            (Manager)
                                            getMetaInformationManager(), (Manager)
                            getTypeManager(), (Manager)
                            getEnumerationManager(), (Manager)
                            getC2LManager(), (Manager)
                            getUserManager(), (Manager)
                            getAccessManager(), (Manager)
                            getOrderManager(), (Manager)
                            getProductManager(), (Manager)
                            getMediaManager(), (Manager)
                            getFlexibleSearch(),
                            (Manager)
                                            getNumberSeriesManager(), (Manager)
                            getLinkManager(), (Manager)
                            getExtensionManager()}));
        }
        return this.allManagers;
    }


    public SavedValues logItemRemoval(PK itemPk, boolean saveAttributes)
    {
        try
        {
            return logItemRemoval(JaloSession.getCurrentSession().getItem(itemPk), saveAttributes);
        }
        catch(JaloItemNotFoundException jinfe)
        {
            LOG.error("Removal information couldn't be written! The assigned item " + itemPk + " could not be found !");
            return null;
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SavedValues logItemRemoval(Item item, boolean saveAttributes)
    {
        if(item == null)
        {
            LOG.error("Removal information couldn't be written! The assigned item never exists!");
            return null;
        }
        if(isLoggingEnabled(item))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("processing logItemRemoval...");
            }
            boolean alive = item.isAlive();
            SessionContext ctx = JaloSession.getCurrentSession(getTenant()).getSessionContext();
            return createSavedValues(ctx
                                            .getUser(), item,
                            alive ? item.toString() : ("removed " + item.getClass().getName() + " " + item.getPK()), new Date(), (
                                            alive && saveAttributes) ? getRemovedItemAttributes(item) : null,
                            getEnumerationManager().getEnumerationValue(GeneratedCoreConstants.TC.SAVEDVALUEENTRYTYPE, GeneratedCoreConstants.Enumerations.SavedValueEntryType.REMOVED));
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("logging of " + item + " is disabled");
        }
        return null;
    }


    protected Collection<ChangedItemAttributes> getRemovedItemAttributes(Item item)
    {
        Collection<ChangedItemAttributes> ret = new ArrayList<>();
        try
        {
            for(Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)item.getAllAttributes().entrySet())
            {
                ret.add(new ChangedItemAttributes(item, e.getKey(), e.getValue(), null));
            }
        }
        catch(Exception ex)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("cannot save attributes of item to be remove " + item + " due to " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
        }
        return ret;
    }


    protected Collection<ChangedItemAttributes> getModifiedItemAttributes(Item item, Map<String, Object> previousValues, Map<String, Object> newValues)
    {
        if(newValues == null || newValues.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<ChangedItemAttributes> ret = new ArrayList<>(newValues.size());
        for(Map.Entry<String, Object> entry : newValues.entrySet())
        {
            ret.add(new ChangedItemAttributes(item, entry.getKey(), previousValues.get(entry.getKey()), entry.getValue()));
        }
        return ret;
    }


    protected boolean isLoggingEnabled(Item toLog)
    {
        if(toLog instanceof SavedValueEntry)
        {
            return false;
        }
        if(toLog == null)
        {
            return false;
        }
        ComposedType composedType = toLog.getComposedType();
        if(composedType.isJaloOnly())
        {
            return false;
        }
        Boolean doLog = isLogHMCChanges(JaloSession.getCurrentSession(getTenant()).getSessionContext(), composedType);
        return (doLog == null || Boolean.TRUE.equals(doLog));
    }


    private final SavedValues createSavedValues(User user, Item item, String message, Date timeStamp, Collection<ChangedItemAttributes> values, EnumerationValue modificationType)
    {
        pruneSavedValuesFor(item);
        Item.ItemAttributeMap<String, User> itemAttributeMap = new Item.ItemAttributeMap();
        itemAttributeMap.put("user", user);
        itemAttributeMap.put("modifiedItem", item.isAlive() ? item : null);
        itemAttributeMap.put("modifiedItemDisplayString", message);
        itemAttributeMap.put("timestamp", timeStamp);
        itemAttributeMap.put("modifiedItemType", item.getComposedType());
        itemAttributeMap.put("modificationType", modificationType);
        try
        {
            SavedValues parent = (SavedValues)TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.SAVEDVALUES).newInstance((Map)itemAttributeMap);
            if(values != null && !values.isEmpty())
            {
                for(ChangedItemAttributes cia : values)
                {
                    createSavedValueEntry(parent, cia.getItem(), cia.getQualifier(), cia.getOldValue(), cia.getNewValue());
                }
            }
            return parent;
        }
        catch(JaloGenericCreationException e)
        {
            Throwable throwable = (e.getCause() != null) ? e.getCause() : (Throwable)e;
            if(throwable instanceof RuntimeException)
            {
                throw (RuntimeException)throwable;
            }
            throw new JaloSystemException(throwable);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloInternalException(e, "SavedValues should never be abstract", 0);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e, "cannot get type for " + SavedValues.class, 0);
        }
    }


    private SavedValueEntry createSavedValueEntry(SavedValues parent, Item item, String qualifier, Object oldValue, Object newValue)
    {
        Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
        itemAttributeMap.put("modifiedAttribute", qualifier);
        itemAttributeMap.put("oldValue", oldValue);
        itemAttributeMap.put("parent", parent);
        itemAttributeMap.put("newValue", newValue);
        itemAttributeMap.put("OldValueAttributeDescriptor", item
                        .getComposedType().getAttributeDescriptorIncludingPrivate(qualifier));
        try
        {
            return (SavedValueEntry)TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.SAVEDVALUEENTRY).newInstance((Map)itemAttributeMap);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable throwable = (e.getCause() != null) ? e.getCause() : (Throwable)e;
            if(throwable instanceof RuntimeException)
            {
                throw (RuntimeException)throwable;
            }
            throw new JaloSystemException(throwable);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloInternalException(e, "SavedValueEntry should never be abstract", 0);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e, "cannot get type for " + SavedValueEntry.class, 0);
        }
    }


    private final void pruneSavedValuesFor(Item item)
    {
        int max = SavedValues.getMaxSize();
        if(max > -1)
        {
            for(SavedValues toRemove : getSavedValues(null, item, max - 1, -1))
            {
                try
                {
                    toRemove.remove();
                }
                catch(ConsistencyCheckException e)
                {
                    LOG.error("cannot prune saved values due to " + e.getMessage());
                }
            }
        }
    }


    protected Collection<SavedValues> getSavedValues(SessionContext ctx, Item item, int start, int count)
    {
        return
                        JaloSession.getCurrentSession(getTenant())
                                        .getFlexibleSearch()
                                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCoreConstants.TC.SAVEDVALUES + "} WHERE {modifiedItem}=?modifiedOne ORDER BY {timestamp} DESC",
                                                        Collections.singletonMap("modifiedOne", item), Collections.singletonList(SavedValues.class), true, true, start, count)
                                        .getResult();
    }


    public Boolean isLogHMCChanges(SessionContext ctx, ComposedType item)
    {
        Boolean isLogHMCChanges = (Boolean)item.getProperty(ctx, LOGHMCCHANGES);
        return (isLogHMCChanges != null) ? isLogHMCChanges : Boolean.TRUE;
    }


    public SavedValues logItemModification(PK itemPk, Map<String, Object> values, Map<String, Object> previousValues, boolean detectRemovedValues)
    {
        try
        {
            return logItemModification(JaloSession.getCurrentSession().getItem(itemPk), values, previousValues, detectRemovedValues);
        }
        catch(JaloItemNotFoundException jinfe)
        {
            LOG.error("Removal information couldn't be written! The assigned item " + itemPk + " could not be found !");
            return null;
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SavedValues logItemModification(Item item, Map<String, Object> values, Map<String, Object> previousValues, boolean detectRemovedValues)
    {
        if(isLoggingEnabled(item))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("processing logItemModification...");
            }
            SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
            ctx.setLanguage(null);
            Date time = new Date();
            User user = ctx.getUser();
            SavedValues ret = createSavedValues(user, item, item
                                            .toString(), time,
                            getModifiedItemAttributes(item, previousValues, values),
                            EnumerationManager.getInstance().getEnumerationValue(GeneratedCoreConstants.TC.SAVEDVALUEENTRYTYPE, GeneratedCoreConstants.Enumerations.SavedValueEntryType.CHANGED));
            if(detectRemovedValues)
            {
                Collection<Item> removedItems = new HashSet<>();
                for(Map.Entry<String, Object> e : values.entrySet())
                {
                    removedItems.addAll(filterRemovedItems(previousValues.get(e.getKey())));
                }
                if(!removedItems.isEmpty())
                {
                    EnumerationValue enumRemoved = EnumerationManager.getInstance().getEnumerationValue(GeneratedCoreConstants.TC.SAVEDVALUEENTRYTYPE, GeneratedCoreConstants.Enumerations.SavedValueEntryType.REMOVED);
                    for(Item removedOne : removedItems)
                    {
                        createSavedValues(user, removedOne, "removed " + removedOne.getClass().getName() + " " + removedOne
                                        .getPK().toString(), time, null, enumRemoved);
                    }
                }
            }
            return ret;
        }
        return null;
    }


    protected Collection filterRemovedItems(Object attributeValue)
    {
        Collection ret = Collections.EMPTY_LIST;
        if(attributeValue != null)
        {
            if(attributeValue instanceof Item)
            {
                if(!((Item)attributeValue).isAlive())
                {
                    ret = Collections.singletonList(attributeValue);
                }
            }
            else if(attributeValue instanceof Collection)
            {
                ret = new ArrayList();
                for(Iterator iter = ((Collection)attributeValue).iterator(); iter.hasNext(); )
                {
                    Object element = iter.next();
                    ret.addAll(filterRemovedItems(element));
                }
            }
            else if(attributeValue instanceof Map)
            {
                ret = new ArrayList();
                for(Iterator<Map.Entry> iter = ((Map)attributeValue).entrySet().iterator(); iter.hasNext(); )
                {
                    Map.Entry entry = iter.next();
                    ret.addAll(filterRemovedItems(entry.getKey()));
                    ret.addAll(filterRemovedItems(entry.getValue()));
                }
            }
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SavedValues logItemCreation(Item item, Map<String, Object> values)
    {
        if(isLoggingEnabled(item))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("processing logItemCreation...");
            }
            return createSavedValues(
                            JaloSession.getCurrentSession().getUser(), item, item
                                            .toString(), new Date(),
                            getCreatedItemAttributes(item, values),
                            EnumerationManager.getInstance().getEnumerationValue(GeneratedCoreConstants.TC.SAVEDVALUEENTRYTYPE, GeneratedCoreConstants.Enumerations.SavedValueEntryType.CREATED));
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("logging of item creation is disabled");
        }
        return null;
    }


    public SavedValues logItemCreation(PK itemPk, Map values)
    {
        try
        {
            return logItemCreation(JaloSession.getCurrentSession().getItem(itemPk), values);
        }
        catch(JaloItemNotFoundException jinfe)
        {
            LOG.error("Creation information couldn't be written! The assigned item " + itemPk + " could not be found !");
            return null;
        }
    }


    protected Collection<ChangedItemAttributes> getCreatedItemAttributes(Item item, Map<String, Object> values)
    {
        if(values == null || values.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<ChangedItemAttributes> ret = new ArrayList<>(values.size());
        for(Map.Entry<String, Object> entry : values.entrySet())
        {
            ret.add(new ChangedItemAttributes(item, entry.getKey(), null, entry.getValue()));
        }
        return ret;
    }
}
