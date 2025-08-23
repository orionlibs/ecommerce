package de.hybris.platform.jalo;

import com.google.common.cache.CacheBuilder;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.directpersistence.cache.SLDCacheUnitInvalidationListener;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.extension.ItemLifecycleListener;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.AttributeAccess;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ConstantAttributeAccess;
import de.hybris.platform.jalo.type.JaloTypeException;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.OneToManyRelationAttributeAccess;
import de.hybris.platform.jalo.type.ReflectionAttributeAccess;
import de.hybris.platform.jalo.type.RelationAttributeAccess;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.persistence.GenericItemEJBImpl;
import de.hybris.platform.persistence.ItemEJBImpl;
import de.hybris.platform.persistence.enumeration.EnumerationValueEJBImpl;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.persistence.link.LinkEJBImpl;
import de.hybris.platform.persistence.security.ACLCache;
import de.hybris.platform.persistence.test.TestItemEJBImpl;
import de.hybris.platform.persistence.type.AtomicTypeEJBImpl;
import de.hybris.platform.persistence.type.AttributeDescriptorEJBImpl;
import de.hybris.platform.persistence.type.CollectionTypeEJBImpl;
import de.hybris.platform.persistence.type.ComposedTypeEJBImpl;
import de.hybris.platform.persistence.type.MapTypeEJBImpl;
import de.hybris.platform.persistence.type.SearchRestrictionEJBImpl;
import de.hybris.platform.servicelayer.security.permissions.impl.SLDItemAclInvalidationListener;
import de.hybris.platform.spring.CGLibUtils;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.BridgeAbstraction;
import de.hybris.platform.util.BridgeInterface;
import de.hybris.platform.util.IsItemAliveCheckUnit;
import de.hybris.platform.util.ItemModificationInvalidationListener;
import de.hybris.platform.util.RelationsInfo;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.PropertyActionReader;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.io.ObjectStreamException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.log4j.Logger;

public abstract class Item extends BridgeAbstraction implements Comparable
{
    private static final Logger LOG = Logger.getLogger(Item.class);
    static final long serialVersionUID = -4406044770772129006L;
    private static final Logger log = Logger.getLogger(Item.class.getName());
    public static final Logger accessorLog = Logger.getLogger("hybris.attribute.accessor");
    public static final String FEATURE_ACCESSMAP_QUALIFIER = (Item.class.getName() + "_featureaccessmap").intern();
    private static final AttributeAccess NONE_ACCESS = (AttributeAccess)new ConstantAttributeAccess("none", null);
    private static final String CLEAR_ALL_IDENTIFIER = "###".intern();
    public static final String SAVE_FROM_SERVICE_LAYER = "save.from.service.layer";
    public static final String DISABLE_ITEMCHECK_BEFORE_REMOVABLE = "disableItemCheckBeforeRemovable";

    static
    {
        loadEJBImplClasses();
    }

    public static void registerJaloInvalidationListeners(InvalidationManager invMan, Cache cache)
    {
        InvalidationTopic topic = invMan.getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new ItemModificationInvalidationListener(cache));
        topic.addInvalidationListener((InvalidationListener)new IsItemAliveCheckUnit.ItemAliveInvalidationListener());
        topic.addInvalidationListener((InvalidationListener)new SLDItemAclInvalidationListener());
        topic.addInvalidationListener((InvalidationListener)new SLDCacheUnitInvalidationListener());
    }


    @Deprecated(since = "2011", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item refenrecedItem)
    {
        return true;
    }


    private static final SingletonCreator.Creator<Map> FEATURE_ACCESSMAP_CREATOR = (SingletonCreator.Creator<Map>)new Object();


    private static Map<Object, AttributeAccess> getFeatureAccessMap(Tenant tenant)
    {
        return (Map<Object, AttributeAccess>)tenant.getSingletonCreator().getSingleton(FEATURE_ACCESSMAP_CREATOR);
    }


    public ItemImpl getImplementation()
    {
        return (ItemImpl)super.getImplementation();
    }


    private static String getAccessKey(Class clazz, String qualifierLowerCase)
    {
        return clazz.getName() + "." + clazz.getName();
    }


    private static Object getAccessKey(AttributeDescriptor attributeDescriptor)
    {
        return PlatformStringUtils.toLowerCaseCached(attributeDescriptor.getEnclosingType().getCode()) + "." + PlatformStringUtils.toLowerCaseCached(attributeDescriptor.getEnclosingType().getCode());
    }


    public static void registerAccessFor(Class clazz, String qualifier, AttributeAccess access)
    {
        registerAccessFor(null, clazz, qualifier, access);
    }


    public static void registerAccessFor(String extensionName, Class clazz, String qualifier, AttributeAccess access)
    {
        new Object(clazz, qualifier, access, extensionName);
    }


    protected static final void internal_registerNonClassAccessorFor(Tenant tenant, AttributeDescriptor attributeDescriptor, AttributeAccess access)
    {
        if(attributeDescriptor == null || access == null)
        {
            throw new JaloInternalException(null, "illegal call internal_registerAccessFor(" + attributeDescriptor + "," + access + ") - null not allowed", 0);
        }
        Object key = getAccessKey(attributeDescriptor);
        getFeatureAccessMap(tenant).put(key, access);
        if(accessorLog.isDebugEnabled())
        {
            accessorLog.debug("registered non-class access for " + key + " -> " + access);
        }
    }


    private static AttributeAccess getRegisteredNonClassAccessesorFor(Tenant tenant, AttributeDescriptor attributeDescriptor)
    {
        if(attributeDescriptor == null)
        {
            throw new JaloInternalException(null, "attribute descriptor cannot be NULL", 0);
        }
        return getFeatureAccessMap(tenant).get(getAccessKey(attributeDescriptor));
    }


    protected static final void internal_registerClassAccessorFor(Tenant tenant, Class clazz, String qualifier, AttributeAccess access)
    {
        if(clazz == null || qualifier == null || access == null)
        {
            throw new JaloInternalException(null, "illegal call internal_registerAccessFor(" + clazz + "," + qualifier + "," + access + ") - null not allowed", 0);
        }
        Object key = getAccessKey(clazz, PlatformStringUtils.toLowerCaseCached(qualifier));
        getFeatureAccessMap(tenant).put(key, access);
        if(accessorLog.isDebugEnabled())
        {
            accessorLog.debug("registered reflection access for " + key + " -> " + (
                            (access instanceof ReflectionAttributeAccess) ? ((ReflectionAttributeAccess)access).getInfo() : access));
        }
    }


    public static boolean hasRegisteredClassAccessorFor(Tenant tenant, Class clazz, String qualifier)
    {
        return (getRegisteredClassAccessesorFor(tenant, clazz, qualifier) != null);
    }


    private static AttributeAccess getRegisteredClassAccessesorFor(Tenant tenant, Class clazz, String qualifier)
    {
        if(qualifier == null)
        {
            throw new JaloInternalException(null, "qualifier cannot be NULL", 0);
        }
        if(clazz == null)
        {
            throw new JaloInternalException(null, "class cannot be NULL", 0);
        }
        Collection<Object> keys = null;
        Map<Object, AttributeAccess> featureAccessMap = getFeatureAccessMap(tenant);
        AttributeAccess ret = null;
        String qualiLower = PlatformStringUtils.toLowerCaseCached(qualifier);
        for(Class tmp = clazz; ret == null && tmp != null; tmp = tmp.getSuperclass())
        {
            Object key = getAccessKey(tmp, qualiLower);
            Object object = featureAccessMap.get(key);
            if(object != null)
            {
                if(NONE_ACCESS.equals(object))
                {
                    break;
                }
                ret = (AttributeAccess)object;
            }
            else
            {
                if(accessorLog.isDebugEnabled())
                {
                    accessorLog.debug("searching coded access for " + tmp.getName() + "." + qualifier + "...");
                }
                ret = findStaticAccessorByReflection(tmp, qualifier);
                if(keys == null)
                {
                    keys = new ArrayList(10);
                }
                keys.add(key);
            }
        }
        if(keys != null)
        {
            for(Object key : keys)
            {
                featureAccessMap.put(key, (ret != null) ? ret : NONE_ACCESS);
                if(ret != null && accessorLog.isDebugEnabled())
                {
                    accessorLog.debug("found coded access for " + key + " -> " + ret);
                }
            }
        }
        return ret;
    }


    private static AttributeAccess findStaticAccessorByReflection(Class myClass, String qualifier)
    {
        try
        {
            Field fdaField = myClass.getDeclaredField("_AD_" + qualifier.toUpperCase(LocaleHelper.getPersistenceLocale()));
            fdaField.setAccessible(true);
            AttributeAccess ret = (AttributeAccess)fdaField.get(null);
            if(ret == null)
            {
                accessorLog.error("coded access member " + fdaField.getName() + " is empty!");
            }
            return ret;
        }
        catch(NoSuchFieldException noSuchFieldException)
        {
        }
        catch(Exception e)
        {
            log.warn("security error while trying to get jalo attribute acces for '" + qualifier + "' : " + e);
        }
        return null;
    }


    private static final AttributeAccess _AD_ITEMTYPE = (AttributeAccess)new Object();
    private static final AttributeAccess _AD_MODIFIEDTIME = (AttributeAccess)new Object();
    private PK thePK;


    protected final AttributeAccess getAccessorFor(String qualifier, Class myClass)
    {
        assureExtensionsLoaded();
        Tenant tenant = getTenant();
        AttributeAccess accessor = getRegisteredClassAccessesorFor(tenant, myClass, qualifier);
        if(accessor == null)
        {
            if(accessorLog.isDebugEnabled())
            {
                accessorLog.debug("no registered access for " + myClass.getName() + "." + qualifier + " - trying to find getter/setter via reflection");
            }
            try
            {
                AttributeDescriptor fieldDescriptor = getComposedType().getAttributeDescriptorIncludingPrivate(qualifier);
                accessor = getRegisteredNonClassAccessesorFor(tenant, fieldDescriptor);
                if(accessor == null)
                {
                    accessor = ReflectionAttributeAccess.createReflectionAccess(tenant, myClass, fieldDescriptor);
                    if(accessor != null)
                    {
                        internal_registerClassAccessorFor(tenant, myClass, qualifier, accessor);
                    }
                    else
                    {
                        accessor = createNonClassAccessor(fieldDescriptor);
                        if(accessor != null)
                        {
                            internal_registerNonClassAccessorFor(tenant, fieldDescriptor, accessor);
                        }
                    }
                }
            }
            catch(JaloItemNotFoundException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        return accessor;
    }


    protected AttributeAccess createNonClassAccessor(AttributeDescriptor fieldDescriptor)
    {
        RelationAttributeAccess relationAttributeAccess;
        AttributeAccess ret = null;
        if(fieldDescriptor instanceof RelationDescriptor)
        {
            RelationDescriptor relationDescriptor = (RelationDescriptor)fieldDescriptor;
            RelationType relationType = relationDescriptor.getRelationType();
            if(relationType.isAbstract())
            {
                OneToManyRelationAttributeAccess oneToManyRelationAttributeAccess = new OneToManyRelationAttributeAccess(relationType, relationDescriptor);
            }
            else
            {
                relationAttributeAccess = new RelationAttributeAccess(relationType.getCode(), relationDescriptor.isSource(), relationType.isLocalized(), false);
            }
        }
        return (AttributeAccess)relationAttributeAccess;
    }


    protected void assureExtensionsLoaded()
    {
        if(!getExtensionLoadedCache().booleanValue())
        {
            getSession().getExtensionManager().getExtensions();
            getExtensionLoadedCache().setValue(true);
        }
    }


    private MutableBoolean getExtensionLoadedCache()
    {
        return (MutableBoolean)getTenant().getCache().getStaticCacheContent((SingletonCreator.Creator)new Object(this));
    }


    public static void removeItemCollection(Collection collection) throws ConsistencyCheckException
    {
        removeItemCollection(JaloSession.getCurrentSession().getSessionContext(), collection);
    }


    public static void removeItemCollection(SessionContext ctx, Collection collection) throws ConsistencyCheckException
    {
        for(Iterator<Item> it = collection.iterator(); it.hasNext(); )
        {
            ((Item)it.next()).remove(ctx);
        }
    }


    private volatile boolean isRemoved = false;
    private transient PK composedTypePK = null;
    protected final boolean isJaloOnly;
    private volatile transient boolean cacheBound = false;
    private volatile transient StillInCacheCallback stillInCacheCallback = StillInCacheCallback.NOT_IN_CACHE;
    private final AtomicReference<LocalItemCache> localItemCacheRef = (AtomicReference<LocalItemCache>)new Object(this);
    protected static final ConcurrentMap<PK, Map<String, Object>> staticTransientObjects = CacheBuilder.newBuilder().maximumSize(100000L).build().asMap();
    public static final String PK = "pk".intern();
    public static final String _CREATION_TIME_INTERNAL = "creationTimestampInternal".intern();
    public static final String _MODIFIED_TIME_INTERNAL = "modifiedTimestampInternal".intern();
    public static final String TYPE = "itemtype".intern();
    public static final String CREATION_TIME = "creationtime".intern();
    public static final String MODIFIED_TIME = "modifiedtime".intern();
    public static final String OWNER = "owner".intern();
    public static final int NOT_FOUND = -1;
    public static final int POSITIVE = 0;
    public static final int NEGATIVE = 1;
    public static final int EVEN = 2;
    public static final String HJMPTS = "hjmpTS".intern();


    public Item()
    {
        this.isJaloOnly = this instanceof JaloOnlyItem;
    }


    private boolean canUseLocalCache()
    {
        return (getImplementation().isLocalCachingSupported() && isCacheBound() && !Transaction.current().isRunning());
    }


    private LocalItemCache getLocalItemCache()
    {
        requireImplementationToSupportLocalCaching();
        LocalItemCache ret = this.localItemCacheRef.get();
        while(ret == null)
        {
            if(!this.localItemCacheRef.compareAndSet(null, ret = new LocalItemCache(getModificationTimeFromPersistenceLayer())))
            {
                ret = this.localItemCacheRef.get();
            }
        }
        return ret;
    }


    private void requireImplementationToSupportLocalCaching()
    {
        ItemImpl implementation = getImplementation();
        if(!implementation.isLocalCachingSupported())
        {
            throw new UnsupportedOperationException("Local caching is not supported by the underlying implementation '" + implementation + "'.");
        }
    }


    private Map<CacheableItemLogic, CacheEntry> getGetterSetterCache()
    {
        return (getLocalItemCache()).getterSetterCache;
    }


    private void clearLocalGetterSetterCache()
    {
        this.localItemCacheRef.set(null);
    }


    public Tenant getTenant()
    {
        Tenant tenant = super.getTenant();
        if(tenant == null)
        {
            tenant = Registry.getCurrentTenantNoFallback();
        }
        return tenant;
    }


    public void setImplementation(BridgeInterface impl)
    {
        super.setImplementation(impl);
        this.thePK = getImplementation().getPK();
    }


    @SLDSafe
    public PK getPK()
    {
        return this.isJaloOnly ? ((JaloOnlyItem)this).providePK() : this.thePK;
    }


    @SLDSafe
    public Date getCreationTime()
    {
        if(this.isJaloOnly)
        {
            return ((JaloOnlyItem)this).provideCreationTime();
        }
        return (Date)(new Object(this, CREATION_TIME))
                        .get();
    }


    @SLDSafe
    protected void setCreationTime(Date creationTime)
    {
        if(this.isJaloOnly)
        {
            throw new JaloInvalidParameterException("cannot change creation time of jaloOnly items", 0);
        }
        (new Object(this, CREATION_TIME, creationTime))
                        .set();
    }


    public Date getModificationTime()
    {
        if(this.isJaloOnly)
        {
            return ((JaloOnlyItem)this).provideModificationTime();
        }
        if(canUseLocalCache())
        {
            return (getLocalItemCache()).modificationTime;
        }
        return getModificationTimeFromPersistenceLayer();
    }


    private Date getModificationTimeFromPersistenceLayer()
    {
        try
        {
            return getImplementation().getModificationTime();
        }
        catch(YNoSuchEntityException e)
        {
            throw new JaloObjectNoLongerValidException(this, e, e.getMessage(), 0);
        }
    }


    public long getPersistenceVersion()
    {
        if(this.isJaloOnly)
        {
            return -1L;
        }
        return ((Long)(new Object(this, HJMPTS))
                        .get()).longValue();
    }


    public void setModificationTime(Date date)
    {
        try
        {
            (new Object(this, MODIFIED_TIME, date))
                            .set();
        }
        catch(Exception e)
        {
            log.warn("could not change modifiaction time for " + getPK() + " due to " + e.getMessage() + " - ignored");
        }
    }


    public JaloSession getSession()
    {
        return JaloSession.getCurrentSession(getTenant());
    }


    protected SessionContext ctx()
    {
        return getSession().getSessionContext();
    }


    public PK getComposedTypePK()
    {
        return getComposedType().getPK();
    }


    public ComposedType getComposedType()
    {
        if(this.composedTypePK == null)
        {
            if(this.isJaloOnly)
            {
                return ((JaloOnlyItem)this).provideComposedType();
            }
            return (ComposedType)(new Object(this, TYPE))
                            .get();
        }
        return (ComposedType)getSession().getItem(this.composedTypePK);
    }


    public boolean isInstanceOf(ComposedType type)
    {
        return type.isInstance(this);
    }


    public Item setComposedType(ComposedType type) throws JaloInvalidParameterException
    {
        if(this.isJaloOnly)
        {
            return this;
        }
        try
        {
            return (Item)(new Object(this, TYPE, type))
                            .set();
        }
        catch(JaloCachedComputationException e)
        {
            Throwable cause = e.getCause();
            if(cause instanceof JaloInvalidParameterException)
            {
                throw (JaloInvalidParameterException)cause;
            }
            throw new JaloSystemException(cause);
        }
    }


    @SLDSafe
    public Item getOwner()
    {
        if(this.isJaloOnly)
        {
            return null;
        }
        return (Item)(new Object(this, OWNER))
                        .get();
    }


    @SLDSafe
    public void setOwner(Item item) throws ConsistencyCheckException
    {
        if(!this.isJaloOnly)
        {
            (new Object(this, OWNER, item))
                            .set();
        }
    }


    public Object getAttribute(String qualifier) throws JaloInvalidParameterException, JaloSecurityException
    {
        return getAttribute(getSession().getSessionContext(), qualifier);
    }


    public Object getAttribute(SessionContext ctx, String qualifier) throws JaloInvalidParameterException, JaloSecurityException
    {
        try
        {
            AttributeAccess accessor = getAccessorFor(qualifier, getClass());
            if(accessor == null)
            {
                if(this instanceof JaloOnlyItem)
                {
                    return ((JaloOnlyItem)this).doGetAttribute(ctx, qualifier);
                }
                if(accessorLog.isDebugEnabled())
                {
                    logMissingAccessor(qualifier);
                }
                return null;
            }
            return accessor.getValue(ctx, this);
        }
        catch(JaloTypeException e)
        {
            throw new JaloSystemException(e, e.getMessage(), e.getErrorCode());
        }
    }


    public void setAttributeFromString(String qualifier, String value) throws JaloInvalidParameterException, JaloBusinessException
    {
        setAttributeFromString(getSession().getSessionContext(), qualifier, value);
    }


    public void setAttributeFromString(SessionContext ctx, String qualifier, String value) throws JaloInvalidParameterException, JaloBusinessException
    {
        setAttribute(ctx, qualifier, getComposedType().getAttributeDescriptorIncludingPrivate(qualifier).getAttributeType(ctx)
                        .parseValue(ctx, value));
    }


    public Map getAllAttributes() throws JaloInvalidParameterException, JaloSecurityException
    {
        return getAllAttributes(getSession().getSessionContext());
    }


    public Map getAllAttributes(SessionContext ctx) throws JaloInvalidParameterException, JaloSecurityException
    {
        return getAllAttributes(ctx, DEFAULT_FILTER);
    }


    private static final AttributeFilter DEFAULT_FILTER = (AttributeFilter)new Object();
    private static final String MISSING_ACCESSOR_MSG = "No accessor found for attribute '%s' of type '%s'. It seems that this attribute was removed from items.xml or is declared as a dynamic one. If attribute was removed please remove the related attribute descriptor manually";


    public Map getAllAttributes(SessionContext ctx, AttributeFilter filter) throws JaloInvalidParameterException, JaloSecurityException
    {
        Set<String> qualifiers = new LinkedHashSet<>();
        for(AttributeDescriptor ad : getComposedType().getAttributeDescriptors())
        {
            if(filter.processAttribute(ad))
            {
                qualifiers.add(ad.getQualifier());
            }
        }
        return getAllAttributes(ctx, qualifiers);
    }


    protected void setUseTA(SessionContext ctx, boolean use)
    {
        ctx.setAttribute("all.attributes.use.ta", Boolean.valueOf(use));
    }


    protected boolean useTA(SessionContext ctx, boolean def)
    {
        if(getImplementation() != null && !getImplementation().isTransactionSupported())
        {
            return false;
        }
        Boolean isUseTransaction = (ctx == null) ? null : (Boolean)ctx.getAttribute("all.attributes.use.ta");
        return (isUseTransaction != null) ? isUseTransaction.booleanValue() : def;
    }


    public Map getAllAttributes(SessionContext ctx, Set<String> qualfiers) throws JaloInvalidParameterException, JaloSecurityException
    {
        if(useTA(ctx, false))
        {
            try
            {
                return (Map)Transaction.current().execute((TransactionBody)new Object(this, ctx, qualfiers));
            }
            catch(Exception e)
            {
                if(e instanceof JaloInvalidParameterException)
                {
                    throw (JaloInvalidParameterException)e;
                }
                if(e instanceof JaloSecurityException)
                {
                    throw (JaloSecurityException)e;
                }
                if(e instanceof RuntimeException)
                {
                    throw (RuntimeException)e;
                }
                throw new JaloSystemException(e);
            }
        }
        return getAllAttributesInternal(ctx, qualfiers);
    }


    protected Map<String, Object> getAllAttributesInternal(SessionContext ctx, Set<String> qualfiers) throws JaloInvalidParameterException, JaloSecurityException
    {
        ItemAttributeMap<String, Object> itemAttributeMap = new ItemAttributeMap();
        for(String qual : qualfiers)
        {
            itemAttributeMap.put(qual, getAttribute(ctx, qual));
        }
        return (Map<String, Object>)itemAttributeMap;
    }


    public void setAllAttributes(Map values) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        setAllAttributes(getSession().getSessionContext(), values);
    }


    public void setAllAttributes(SessionContext ctx, Map values) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        if(useTA(ctx, true))
        {
            try
            {
                Transaction.current().execute((TransactionBody)new Object(this, ctx, values));
            }
            catch(Exception e)
            {
                if(e instanceof JaloInvalidParameterException)
                {
                    throw (JaloInvalidParameterException)e;
                }
                if(e instanceof JaloSecurityException)
                {
                    throw (JaloSecurityException)e;
                }
                if(e instanceof JaloBusinessException)
                {
                    throw (JaloBusinessException)e;
                }
                if(e instanceof RuntimeException)
                {
                    throw (RuntimeException)e;
                }
                throw new JaloSystemException(e);
            }
        }
        else
        {
            setAllAttributesInternal(ctx, values);
        }
    }


    protected void setAllAttributesInternal(SessionContext ctx, Map values) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        if(values == null)
        {
            throw new JaloInvalidParameterException("cannot set attributes using NULL value map", 0);
        }
        boolean inCreate = (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("core.types.creation.initial")));
        boolean noAttributeCheck = (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("disable.attribute.check")));
        ComposedType composedType = noAttributeCheck ? null : getComposedType();
        Map<String, Object> invalid = null;
        Set<String> nonWritable = null;
        for(Map.Entry<String, Object> valueEntry : (Iterable<Map.Entry<String, Object>>)values.entrySet())
        {
            AttributeDescriptor attributeDescriptor;
            String qual = valueEntry.getKey();
            Object value = valueEntry.getValue();
            if(noAttributeCheck)
            {
                setAttribute(ctx, qual, value);
                continue;
            }
            try
            {
                attributeDescriptor = composedType.getAttributeDescriptorIncludingPrivate(qual);
            }
            catch(JaloItemNotFoundException e)
            {
                if(invalid == null)
                {
                    invalid = new HashMap<>();
                }
                invalid.put(qual, value);
                continue;
            }
            if(attributeDescriptor.isWritable() || (inCreate && attributeDescriptor.isInitial()))
            {
                if(ctx != null)
                {
                    ctx.setAttribute("core.types.creation.initial", Boolean.TRUE);
                }
                setAttribute(ctx, qual, value);
                continue;
            }
            if(nonWritable == null)
            {
                nonWritable = new HashSet<>();
            }
            nonWritable.add(qual);
        }
        if(invalid != null || nonWritable != null)
        {
            throw new JaloInvalidParameterException("Could not set given values because either attribute is not writable or does not exist within composed type " +
                            getComposedType().getCode() + " (invalid=" + invalid + ", non-writable=" + nonWritable + ") - rollback changes", 0);
        }
    }


    protected void checkConstraint(ItemConstraint constr) throws ConsistencyCheckException
    {
        if(Transaction.current().isRunning())
        {
            Transaction.current().addDelayedConstraint(this, constr);
        }
        else
        {
            try
            {
                constr.assertConstraint(this);
            }
            catch(ConsistencyCheckException e)
            {
                try
                {
                    constr.undoChanges(this);
                }
                catch(Exception ex)
                {
                    log.error("unexpected error while undoing changes for constraint " + constr + " : " + ex.getMessage());
                    log.error(Utilities.getStackTraceAsString(ex));
                }
                throw e;
            }
        }
    }


    public static boolean isInCreate(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("core.types.creation.initial")));
    }


    public void setAttribute(String qualifier, Object value) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        setAttribute(getSession().getSessionContext(), qualifier, value);
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        try
        {
            AttributeAccess access = getAccessorFor(qualifier, getClass());
            if(access != null)
            {
                access.setValue(ctx, this, value);
            }
            else if(this instanceof JaloOnlyItem)
            {
                ((JaloOnlyItem)this).doSetAttribute(ctx, qualifier, value);
            }
            else
            {
                logMissingAccessor(qualifier);
            }
        }
        catch(JaloTypeException e)
        {
            throw new JaloSystemException(e, e.getMessage(), e.getErrorCode());
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, e.getMessage(), e.getErrorCode());
        }
    }


    private void logMissingAccessor(String qualifier)
    {
        String msg = String.format("No accessor found for attribute '%s' of type '%s'. It seems that this attribute was removed from items.xml or is declared as a dynamic one. If attribute was removed please remove the related attribute descriptor manually",
                        new Object[] {qualifier, getClass().getName()});
        accessorLog.debug(msg);
    }


    public List getLinkedItems(boolean itemIsSource, String qualifier, Language lang)
    {
        return getSession().getLinkManager().getLinkedItems(this, itemIsSource, qualifier, lang);
    }


    public List getLinkedItems(boolean itemIsSource, String qualifier, Language lang, int start, int count)
    {
        return getSession().getLinkManager().getLinkedItems(this, itemIsSource, qualifier, lang, start, count);
    }


    public long getLinkedItemsCount(boolean itemIsSource, String qualifier, Language lang)
    {
        return getSession().getLinkManager().getLinkedItemsCount(this, itemIsSource, qualifier, lang);
    }


    public List getLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang)
    {
        LinkManager lm = getSession().getLinkManager();
        List ret = lm.getLinkedItems(ctx, this, itemIsSource, qualifier, lang);
        if(lang != null && isEmptyRelationValue(ctx, qualifier, ret) && isRelationLocalizationFallbackEnabled(ctx))
        {
            for(Language fbLang : ctx.getLanguage().getFallbackLanguages())
            {
                ret = lm.getLinkedItems(ctx, this, itemIsSource, qualifier, fbLang);
                if(!isEmptyRelationValue(ctx, qualifier, ret))
                {
                    break;
                }
            }
        }
        return ret;
    }


    public List getLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        LinkManager lm = getSession().getLinkManager();
        List ret = lm.getLinkedItems(ctx, this, itemIsSource, qualifier, lang, 0, -1, sortSrc2Tgt, sortTgt2Src);
        if(lang != null && isEmptyRelationValue(ctx, qualifier, ret) && isRelationLocalizationFallbackEnabled(ctx))
        {
            for(Language fbLang : ctx.getLanguage().getFallbackLanguages())
            {
                ret = lm.getLinkedItems(ctx, this, itemIsSource, qualifier, fbLang, 0, -1, sortSrc2Tgt, sortTgt2Src);
                if(!isEmptyRelationValue(ctx, qualifier, ret))
                {
                    break;
                }
            }
        }
        return ret;
    }


    public List getLinkedItems(SessionContext ctx, boolean itemIsSource, String relationCode, String relatedItemCode, Language lang, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        LinkManager lm = getSession().getLinkManager();
        List ret = lm.getLinkedItems(ctx, getPK(), itemIsSource, (lang == null) ? null : lang.getPK(), relationCode, relatedItemCode, 0, -1, sortSrc2Tgt, sortTgt2Src);
        if(lang != null && isEmptyRelationValue(ctx, relationCode, ret) && isRelationLocalizationFallbackEnabled(ctx))
        {
            for(Language fbLang : ctx.getLanguage().getFallbackLanguages())
            {
                ret = lm.getLinkedItems(ctx, getPK(), itemIsSource, fbLang.getPK(), relationCode, relatedItemCode, 0, -1, sortSrc2Tgt, sortTgt2Src);
                if(!isEmptyRelationValue(ctx, relationCode, ret))
                {
                    break;
                }
            }
        }
        return ret;
    }


    protected boolean isEmptyRelationValue(SessionContext ctx, String name, List value)
    {
        return CollectionUtils.isEmpty(value);
    }


    protected boolean isRelationLocalizationFallbackEnabled(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("enable.language.fallback")));
    }


    public List getLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, int start, int count)
    {
        return getSession().getLinkManager().getLinkedItems(ctx, this, itemIsSource, qualifier, lang, start, count);
    }


    public List getLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, int start, int count, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        return getSession().getLinkManager().getLinkedItems(ctx, this, itemIsSource, qualifier, lang, start, count, sortSrc2Tgt, sortTgt2Src);
    }


    public long getLinkedItemsCount(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang)
    {
        return getSession().getLinkManager().getLinkedItemsCount(ctx, this, itemIsSource, qualifier, lang);
    }


    public long getLinkedItemsCount(SessionContext ctx, boolean itemIsSource, String relationTypeCode, String relatedItemCode, Language lang)
    {
        return getSession().getLinkManager().getLinkedItemsCount(ctx, getPK(), itemIsSource, relationTypeCode, relatedItemCode,
                        (lang == null) ? null : lang.getPK());
    }


    public Map getAllLinkedItems(boolean itemIsSource, String qualifier)
    {
        return getSession().getLinkManager().getAllLinkedItems(this, itemIsSource, qualifier);
    }


    public void setLinkedItems(boolean itemIsSource, String qualifier, Language lang, List<? extends Item> itemsToLink)
    {
        getSession().getLinkManager().setLinkedItems(this, itemIsSource, qualifier, lang, itemsToLink);
    }


    public void setLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List<? extends Item> itemsToLink)
    {
        getSession().getLinkManager().setLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink);
    }


    public void setLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, Collection<? extends Item> itemsToLink)
    {
        getSession().getLinkManager().setLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink);
    }


    public void setLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, Collection<? extends Item> itemsToLink, boolean markModified)
    {
        getSession().getLinkManager().setLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, markModified);
    }


    public void setLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List<? extends Item> itemsToLink, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        getSession().getLinkManager().setLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, sortSrc2Tgt, sortTgt2Src, true);
    }


    public void setLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List<? extends Item> itemsToLink, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        getSession().getLinkManager().setLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public void setLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, Collection<? extends Item> itemsToLink, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        getSession().getLinkManager().setLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, sortSrc2Tgt, sortTgt2Src, true);
    }


    public void setLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, Collection<? extends Item> itemsToLink, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        getSession().getLinkManager().setLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public void setAllLinkedItems(boolean itemIsSource, String qualifier, Map languageToItemListMap)
    {
        setAllLinkedItems(getSession().getSessionContext(), itemIsSource, qualifier, languageToItemListMap);
    }


    public void setAllLinkedItems(boolean itemIsSource, String qualifier, Map languageToItemListMap, boolean markModified)
    {
        setAllLinkedItems(getSession().getSessionContext(), itemIsSource, qualifier, languageToItemListMap, markModified);
    }


    public void setAllLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Map languageToItemListMap)
    {
        getSession().getLinkManager().setAllLinkedItems(ctx, this, itemIsSource, qualifier, languageToItemListMap);
    }


    public void setAllLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Map languageToItemListMap, boolean markModified)
    {
        getSession().getLinkManager().setAllLinkedItems(ctx, this, itemIsSource, qualifier, languageToItemListMap, markModified);
    }


    public void addLinkedItems(boolean itemIsSource, String qualifier, Language lang, List itemsToLink)
    {
        getSession().getLinkManager().addLinkedItems(this, itemIsSource, qualifier, lang, itemsToLink);
    }


    public void addLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToLink)
    {
        getSession().getLinkManager().addLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink);
    }


    public void addLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToLink, boolean markModified)
    {
        getSession().getLinkManager().addLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, markModified);
    }


    public void addLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToLink, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        getSession().getLinkManager().addLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, -1, true, sortSrc2Tgt, sortTgt2Src, true);
    }


    public void addLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToLink, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        getSession().getLinkManager().addLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, -1, true, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public void addLinkedItems(boolean itemIsSource, String qualifier, Language lang, List itemsToLink, int position)
    {
        getSession().getLinkManager().addLinkedItems(this, itemIsSource, qualifier, lang, itemsToLink, position);
    }


    public void addLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToLink, int position)
    {
        getSession().getLinkManager().addLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, position);
    }


    public void addLinkedItems(boolean itemIsSource, String qualifier, Language lang, List itemsToLink, int position, boolean shift)
    {
        getSession().getLinkManager().addLinkedItems(this, itemIsSource, qualifier, lang, itemsToLink, position, shift);
    }


    public void addLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToLink, int position, boolean shift)
    {
        getSession().getLinkManager().addLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToLink, position, shift);
    }


    public void removeLinkedItems(boolean itemIsSource, String qualifier, Language lang, List itemsToUnlink)
    {
        getSession().getLinkManager().removeLinkedItems(this, itemIsSource, qualifier, lang, itemsToUnlink);
    }


    public void removeLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToUnlink)
    {
        getSession().getLinkManager().removeLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToUnlink);
    }


    public void removeLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToUnlink, boolean sortSrc2Tgt, boolean sortTgt2Src)
    {
        getSession().getLinkManager().removeLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToUnlink, sortSrc2Tgt, sortTgt2Src, true);
    }


    public void removeLinkedItems(SessionContext ctx, boolean itemIsSource, String qualifier, Language lang, List itemsToUnlink, boolean sortSrc2Tgt, boolean sortTgt2Src, boolean markModified)
    {
        getSession().getLinkManager().removeLinkedItems(ctx, this, itemIsSource, qualifier, lang, itemsToUnlink, sortSrc2Tgt, sortTgt2Src, markModified);
    }


    public void remove() throws ConsistencyCheckException
    {
        remove(getSession().getSessionContext());
    }


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        if(isAlive())
        {
            doCheckRemovable(ctx);
            doRemove(ctx);
        }
        else
        {
            debugMultipleRemovalAttempt();
        }
    }


    private void debugMultipleRemovalAttempt()
    {
        if(log.isDebugEnabled())
        {
            log.debug("trying to remove already removed item " + getPK() + " again - skipped");
            log.debug(Utilities.getStackTraceAsString(new RuntimeException()));
        }
    }


    private void doCheckRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        if(!isItemCheckBeforeRemoveableDisabled(ctx))
        {
            checkRemovable(ctx);
        }
    }


    private synchronized void doRemove(SessionContext ctx) throws ConsistencyCheckException
    {
        if(isAlive())
        {
            try
            {
                registerAsRemoving(this);
                notifyItemRemoval(ctx);
                PK ctPK = getComposedType().getPK();
                if(this.isJaloOnly)
                {
                    ((JaloOnlyItem)this).removeJaloOnly();
                }
                else
                {
                    try
                    {
                        removeDependentItems(ctx);
                        Map<Object, Object> removalCtx = new HashMap<>(10);
                        doBeforeRemove(ctx, removalCtx);
                        removeItem(ctx);
                        doAfterRemove(ctx, removalCtx);
                    }
                    catch(JaloObjectNoLongerValidException e)
                    {
                        if(!this.thePK.equals(e.getJaloObjectPK()))
                        {
                            throw e;
                        }
                    }
                }
                markRemovedAndStoreItemEssentialsForAfterlife(ctPK);
            }
            finally
            {
                unregisterAsRemoving(this);
            }
        }
        else
        {
            debugMultipleRemovalAttempt();
        }
    }


    protected void doBeforeRemove(SessionContext ctx, Map removalCtx)
    {
    }


    protected void doAfterRemove(SessionContext ctx, Map removalCtx)
    {
    }


    private static final ThreadLocal<LinkedList<PK>> currentItemsInRemoval = (ThreadLocal<LinkedList<PK>>)new Object();
    public static final String INITIAL_CREATION_FLAG = "core.types.creation.initial";
    public static final String DISABLE_ATTRIBUTE_CHECK = "disable.attribute.check";


    private static void registerAsRemoving(Item item)
    {
        ((LinkedList<PK>)currentItemsInRemoval.get()).add(item.getPK());
    }


    private static void unregisterAsRemoving(Item item)
    {
        ((LinkedList)currentItemsInRemoval.get()).remove(item.getPK());
    }


    public static boolean isCurrentlyRemoving(Item item)
    {
        return isCurrentlyRemoving(item.getPK());
    }


    public static boolean isCurrentlyRemoving(PK pk)
    {
        LinkedList<PK> list = currentItemsInRemoval.get();
        return list.contains(pk);
    }


    public static int getCurrentlyRemovingCount()
    {
        return ((LinkedList)currentItemsInRemoval.get()).size();
    }


    private void removeItem(SessionContext ctx) throws ConsistencyCheckException
    {
        getImplementation().remove(ctx);
    }


    private void removeDependentItems(SessionContext ctx) throws ConsistencyCheckException
    {
        removePartOfItems(ctx);
        removeLinks();
    }


    private void markRemovedAndStoreItemEssentialsForAfterlife(PK composedTypePK)
    {
        this.composedTypePK = composedTypePK;
        this.isRemoved = true;
    }


    protected void removePartOfItems(SessionContext ctx) throws ConsistencyCheckException
    {
        CaseInsensitiveMap<String, Object> caseInsensitiveMap;
        if(getPartOfRemovedSessionMarker(ctx))
        {
            return;
        }
        Map<String, Object> partOf = null;
        for(AttributeDescriptor ad : getComposedType().getPartOfAutoRemovalAttributeDescriptors())
        {
            if(suppressRelation(ad.getQualifier(), getComposedType().getCode()))
            {
                continue;
            }
            if(partOf == null)
            {
                caseInsensitiveMap = new CaseInsensitiveMap();
            }
            Type partOfType = ad.isLocalized() ? ((MapType)ad.getRealAttributeType()).getReturnType() : ad.getRealAttributeType();
            Object nullToken = null;
            if(partOfType instanceof CollectionType)
            {
                switch(((CollectionType)partOfType).getTypeOfCollection())
                {
                    case 1:
                    case 3:
                        nullToken = Collections.EMPTY_SET;
                        break;
                    default:
                        nullToken = Collections.EMPTY_LIST;
                        break;
                }
            }
            caseInsensitiveMap.put(ad.getQualifier(), nullToken);
        }
        if(caseInsensitiveMap != null)
        {
            removePartOfItems(ctx, (Map<String, Object>)caseInsensitiveMap);
        }
    }


    protected boolean suppressRelation(String relationQualifier, String type)
    {
        return PropertyActionReader.getPropertyActionReader().isActionDisabledForType(relationQualifier + ".partof.removal", type);
    }


    protected boolean getPartOfRemovedSessionMarker(SessionContext ctx)
    {
        String partOfRemovedName = "partOfRemoved." + getPK();
        Object partOfRemoved = ctx.getAttribute(partOfRemovedName);
        if(Boolean.TRUE.equals(partOfRemoved))
        {
            ctx.removeAttribute(partOfRemovedName);
            return true;
        }
        return false;
    }


    protected void removePartOfItems(SessionContext ctx, Map<String, Object> emptyValuesMap) throws ConsistencyCheckException
    {
        SessionContext unlocCtx = (ctx != null) ? new SessionContext(ctx) : null;
        if(unlocCtx != null)
        {
            unlocCtx.setLanguage(null);
        }
        try
        {
            setAllAttributes((unlocCtx != null) ? unlocCtx : ctx, emptyValuesMap);
        }
        catch(Exception e)
        {
            throw new ConsistencyCheckException(e, "could not remove partOf attribute items in " + emptyValuesMap
                            .keySet() + " for " + this + " due to " + e
                            .getMessage(), 0);
        }
    }


    protected void removeLinks()
    {
        LinkManager.getInstance().removeAllLinksFor(getSession().getSessionContext(), this);
    }


    private List<Manager> getManagers()
    {
        return JaloConnection.getInstance().getManagers();
    }


    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        for(Iterator<Manager> it = getManagers().iterator(); it.hasNext(); )
        {
            ((Manager)it.next()).checkBeforeItemRemoval(ctx, this);
        }
    }


    protected void notifyItemRemoval(SessionContext ctx)
    {
        for(Manager m : getManagers())
        {
            try
            {
                notifyManagerAboutItemRemoval(m, ctx);
            }
            catch(Exception e)
            {
                if(Transaction.current().isRunning() && HJMPUtils.isConcurrentModificationException(e))
                {
                    String logMsg = "Concurrent modification detected during " + m.getClass().getName() + "notifyItemRemoval. Rethrowing the exception.";
                    if(log.isDebugEnabled())
                    {
                        log.debug(logMsg, e);
                    }
                    else
                    {
                        log.info(logMsg);
                    }
                    throw e;
                }
                log.error("error removing item: " + e.getMessage() + " - set log to debug for de.hybris.platform.jalo.Item to see stacktrace.");
                if(log.isDebugEnabled())
                {
                    log.debug(Utilities.getStackTraceAsString(e));
                }
            }
        }
    }


    protected void notifyManagerAboutItemRemoval(Manager manager, SessionContext ctx)
    {
        manager.notifyItemRemoval(ctx, this);
    }


    public final boolean equals(Object object)
    {
        return (super.equals(object) || (object instanceof Item && ((Item)object)
                        .getPK().equals(getPK()) && getTenant().equals(((Item)object)
                        .getTenant())));
    }


    public int compareTo(Object object)
    {
        if(!(object instanceof Item))
        {
            return 1;
        }
        int ret = getPK().compareTo(((Item)object).getPK());
        if(ret == 1)
        {
            Tenant mine = getTenant();
            Tenant other = ((Item)object).getTenant();
            if(!mine.equals(other))
            {
                ret = mine.getTenantID().compareTo(other.getTenantID());
            }
        }
        return ret;
    }


    public final int hashCode()
    {
        return getPK().hashCode();
    }


    public String toString()
    {
        if(getPK() == null)
        {
            return "<unsaved>";
        }
        try
        {
            AttributeAccess codeAccess = getAccessorFor("code", getClass());
            Object code = codeAccess.getValue(null, this);
            return (code != null) ? ("" + code + "(" + code + ")") : getPK().toString();
        }
        catch(Exception e)
        {
            return getPK().getLongValueAsString();
        }
    }


    public Set getRestrictedPrincipals()
    {
        return this.isJaloOnly ? Collections.EMPTY_SET : new HashSet(((ItemImpl)this.impl).getRestrictedPrincipals());
    }


    public Set getPermissions(Principal principal, boolean negative)
    {
        if(!this.isJaloOnly)
        {
            if(principal == null)
            {
                throw new JaloInvalidParameterException("principal cannot be null", 0);
            }
            return new HashSet(getImplementation().getPrincipalPermissions(principal.getPK(), negative));
        }
        return Collections.EMPTY_SET;
    }


    public Map getPermissionMap(List userRights)
    {
        if(!this.isJaloOnly)
        {
            if(userRights == null || userRights.isEmpty())
            {
                throw new JaloInvalidParameterException("user right list cannot be empty or null", 0);
            }
            return getImplementation().getPermissionMap(userRights);
        }
        return Collections.EMPTY_MAP;
    }


    public void setPermissionsByMap(List userRights, Map permissionMap) throws JaloSecurityException
    {
        if(!this.isJaloOnly)
        {
            if(userRights == null || userRights.isEmpty())
            {
                throw new JaloInvalidParameterException("user right list cannot be empty or null", 0);
            }
            if(permissionMap == null || permissionMap.isEmpty())
            {
                throw new JaloInvalidParameterException("permission map cannot be empty or null", 0);
            }
            getImplementation().setPermissionMap(userRights, permissionMap);
        }
    }


    public Set getPositivePermissions(Principal principal)
    {
        if(principal == null)
        {
            throw new JaloInvalidParameterException("principal cannot be null", 0);
        }
        return getPermissions(principal, false);
    }


    public Set getNegativePermissions(Principal principal)
    {
        if(principal == null)
        {
            throw new JaloInvalidParameterException("principal cannot be null", 0);
        }
        return getPermissions(principal, true);
    }


    public void addPermission(Principal principal, UserRight right, boolean negative)
    {
        if(!this.isJaloOnly)
        {
            if(principal == null)
            {
                throw new JaloInvalidParameterException("principal cannot be null", 0);
            }
            if(right == null)
            {
                throw new JaloInvalidParameterException("right cannot be null", 0);
            }
            getImplementation().addPermission(principal.getPK(), right.getPK(), negative);
        }
    }


    public void clearPermission(Principal principal, UserRight right)
    {
        if(!this.isJaloOnly)
        {
            if(principal == null)
            {
                throw new JaloInvalidParameterException("principal cannot be null", 0);
            }
            if(right == null)
            {
                throw new JaloInvalidParameterException("right cannot be null", 0);
            }
            getImplementation().clearPermission(principal.getPK(), right.getPK());
        }
    }


    public void addPositivePermission(Principal principal, UserRight right)
    {
        addPermission(principal, right, false);
    }


    public void addNegativePermission(Principal principal, UserRight right)
    {
        if(principal == null)
        {
            throw new JaloInvalidParameterException("principal cannot be null", 0);
        }
        if(right == null)
        {
            throw new JaloInvalidParameterException("right cannot be null", 0);
        }
        addPermission(principal, right, true);
    }


    public boolean checkPermission(UserRight right)
    {
        if(right == null)
        {
            throw new JaloInvalidParameterException("right cannot be null", 0);
        }
        return checkPermission((Principal)getSession().getSessionContext().getUser(), right);
    }


    public boolean checkPermission(Principal principal, UserRight right)
    {
        if(principal == null)
        {
            throw new JaloInvalidParameterException("Unable to execute permission check because no Principal instance was specified (was <null>)!", 0);
        }
        if(right == null)
        {
            throw new JaloInvalidParameterException("Unable to execute permission check because no UserRight instance was specified (was <null>)!", 0);
        }
        if(principal.isAdmin())
        {
            return true;
        }
        int match = checkItemPermission(principal, right);
        return (match != -1) ? ACLCache.translatePermissionToBoolean(match) : principal.checkGlobalPermission(right);
    }


    protected int checkItemPermission(Principal principal, UserRight right)
    {
        if(this.isJaloOnly)
        {
            return 0;
        }
        ItemImpl itemImpl = getImplementation();
        int match = itemImpl.checkItemPermission(principal.getPK(), right.getPK());
        if(match != -1)
        {
            return match;
        }
        Set groups = principal.getGroups();
        while(match == -1 && !groups.isEmpty())
        {
            Set nextGroups = new HashSet();
            int proCount = 0;
            int conCount = 0;
            for(Iterator<PrincipalGroup> it = groups.iterator(); it.hasNext(); )
            {
                PrincipalGroup grp = it.next();
                switch(itemImpl.checkItemPermission(grp.getPK(), right.getPK()))
                {
                    case 0:
                        proCount++;
                        break;
                    case 1:
                        conCount++;
                        break;
                }
                nextGroups.addAll(grp.getGroups());
            }
            if(proCount > 0)
            {
                return (conCount > 0) ? 2 : 0;
            }
            if(conCount > 0)
            {
                return 1;
            }
            groups = nextGroups;
        }
        return -1;
    }


    public void setTransientObject(String key, Object value)
    {
        ConcurrentMap<String, Object> oldMap;
        ConcurrentMap<String, Object> newMap;
        do
        {
            oldMap = (ConcurrentMap<String, Object>)staticTransientObjects.get(getPK());
            newMap = (oldMap == null) ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(oldMap);
            if(value == null)
            {
                newMap.remove(key);
            }
            else
            {
                newMap.put(key, value);
            }
            if(newMap.isEmpty() && oldMap == null)
            {
                return;
            }
            if(newMap.isEmpty() && staticTransientObjects.remove(getPK(), oldMap))
            {
                return;
            }
            if(oldMap == null && staticTransientObjects.putIfAbsent(getPK(), newMap) == null)
            {
                return;
            }
        }
        while(oldMap == null || !staticTransientObjects.replace(getPK(), oldMap, newMap));
    }


    public Object getTransientObject(String key)
    {
        Map<String, Object> transientObjects = staticTransientObjects.get(getPK());
        if(transientObjects == null)
        {
            return null;
        }
        return transientObjects.get(key);
    }


    @Deprecated(since = "18.08", forRemoval = false)
    public Map<String, Object> getTransientObjectMap()
    {
        return staticTransientObjects.computeIfAbsent(getPK(), k -> new ConcurrentHashMap<>());
    }


    public boolean isAlive()
    {
        if(this.isRemoved)
        {
            return false;
        }
        if(this.isJaloOnly)
        {
            return true;
        }
        try
        {
            WrapperFactory.getCachedItem(getTenant().getCache(), getPK());
        }
        catch(JaloItemNotFoundException e)
        {
            return false;
        }
        return true;
    }


    public <T extends Item> T getCacheBoundItem()
    {
        if(isCacheBound() || this.isRemoved || this.isJaloOnly)
        {
            return (T)this;
        }
        if(getSession() == null)
        {
            LOG.warn("Session is not adequate anymore.");
            throw new JaloItemNotFoundException("Session is not adequate anymore.", 0);
        }
        try
        {
            return (T)getSession().getItem(getPK());
        }
        catch(JaloItemNotFoundException e)
        {
            return (T)this;
        }
    }


    public <T extends Item> T getAndCheckCacheBoundItem()
    {
        if(this.isRemoved || this.isJaloOnly)
        {
            return (T)this;
        }
        if(!isCacheBound())
        {
            if(getSession() == null)
            {
                LOG.warn("Session is not adequate anymore.");
                throw new JaloItemNotFoundException("Session is not adequate anymore.", 0);
            }
            try
            {
                return (T)getSession().getItem(getPK());
            }
            catch(JaloItemNotFoundException e)
            {
                return (T)this;
            }
        }
        if(getSession() == null)
        {
            LOG.warn("Session is not adequate anymore.");
            throw new JaloItemNotFoundException("Session is not adequate anymore.", 0);
        }
        try
        {
            Item item = getSession().getItem(getPK());
            if(item != this)
            {
                setCacheBound(false);
                return (T)item;
            }
            return (T)this;
        }
        catch(JaloItemNotFoundException e)
        {
            return (T)this;
        }
    }


    public void setCacheBound(boolean bound)
    {
        this.cacheBound = bound;
        if(!bound)
        {
            clearLocalGetterSetterCache();
        }
    }


    void setStillInCacheCallback(StillInCacheCallback stillInCacheCallback)
    {
        this.stillInCacheCallback = stillInCacheCallback;
    }


    public boolean isCacheBound()
    {
        if(!this.cacheBound)
        {
            return false;
        }
        if(this.stillInCacheCallback == null || !this.stillInCacheCallback.isStillInCache())
        {
            setCacheBound(false);
            return false;
        }
        return true;
    }


    public void invalidateLocalCaches()
    {
        clearLocalGetterSetterCache();
        getImplementation().invalidateLocalCaches();
    }


    public final Item newInstance(SessionContext ctx, ComposedType type, Map attributeAssignment) throws JaloBusinessException
    {
        try
        {
            if(this.isJaloOnly || (ctx != null && Boolean.TRUE.equals(ctx
                            .getAttribute("transaction_in_create_disabled"))))
            {
                return newInstanceInternal(ctx, type, attributeAssignment);
            }
            return (Item)Transaction.current().execute((TransactionBody)new Object(this, ctx, type, attributeAssignment));
        }
        catch(Exception e)
        {
            if(e instanceof JaloBusinessException)
            {
                throw (JaloBusinessException)e;
            }
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    private Item newInstanceInternal(SessionContext ctx, ComposedType type, Map attributeAssignment) throws JaloBusinessException
    {
        try
        {
            SessionContext myCtx = new SessionContext(ctx);
            myCtx.setAttribute("core.types.creation.initial", Boolean.TRUE);
            ItemAttributeMap attributes = new ItemAttributeMap(type.getAllDefaultValues(myCtx));
            if(attributeAssignment != null && !attributeAssignment.isEmpty())
            {
                attributes.putAll(attributeAssignment);
            }
            attributes.setDefaultAttributeModes(getDefaultAttributeModes());
            attributes.setComposedType(type);
            if(attributes.get(PK) == null)
            {
                if(type.isJaloOnly())
                {
                    attributes.put(PK, PK.createUUIDPK(type.getItemTypeCode()));
                }
                else
                {
                    attributes.put(PK, PK.createCounterPK(type.getItemTypeCode()));
                }
            }
            else
            {
                PK preset = (PK)attributes.get(PK);
                int typeCode = type.getItemTypeCode();
                if(preset.getTypeCode() != typeCode)
                {
                    if(preset.isCounterBased())
                    {
                        attributes.put(PK, PK.createFixedUUIDPK(typeCode, preset.getLongValue()));
                    }
                    else
                    {
                        attributes.put(PK, PK.createFixedCounterPK(typeCode, preset.getLongValue()));
                    }
                }
            }
            notifyExtensionsBeforeItemCreation(ctx, type, attributes);
            Item zwItem = createItem(myCtx, type, attributes);
            Item item = changeTypeAfterCreation(zwItem, type);
            ItemAttributeMap nonInitial = getNonInitialAttributes(myCtx, attributes);
            setNonInitialAttributes(myCtx, item, nonInitial);
            notifyExtensionsAfterItemCreation(ctx, type, item, new ItemAttributeMap((Map)attributes));
            return item;
        }
        catch(Exception e)
        {
            if(e instanceof JaloBusinessException)
            {
                throw (JaloBusinessException)e;
            }
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    protected void notifyExtensionsBeforeItemCreation(SessionContext ctx, ComposedType type, ItemAttributeMap attributes) throws JaloBusinessException
    {
        try
        {
            for(Extension e : ExtensionManager.getInstance().getExtensions())
            {
                Map map = e.getDefaultAttributeModes(getClass());
                attributes.setDefaultAttributeModes(map);
                e.beforeItemCreation(ctx, type, attributes);
            }
        }
        catch(Exception e)
        {
            log.error(e);
            if(e instanceof JaloBusinessException)
            {
                throw (JaloBusinessException)e;
            }
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    protected void notifyExtensionsAfterItemCreation(SessionContext ctx, ComposedType type, Item createdItem, ItemAttributeMap attributes) throws JaloBusinessException
    {
        try
        {
            for(ItemLifecycleListener e : getTenant().getAllItemLifecycleListeners())
            {
                e.afterItemCreation(ctx, type, createdItem, attributes);
            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }


    protected Item changeTypeAfterCreation(Item newOne, ComposedType requestedType) throws JaloInvalidParameterException
    {
        return !requestedType.equals(newOne.getComposedType()) ? newOne.setComposedType(requestedType) : newOne;
    }


    public void setNonInitialAttributes(SessionContext ctx, Item item, ItemAttributeMap nonInitialAttributes) throws JaloBusinessException
    {
        for(Iterator<Map.Entry> it = nonInitialAttributes.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            item.setAttribute(ctx, (String)entry.getKey(), entry.getValue());
        }
    }


    protected ItemAttributeMap getNonInitialAttributes(SessionContext ctx, ItemAttributeMap allAttributes)
    {
        ItemAttributeMap ret = new ItemAttributeMap((Map)allAttributes);
        ret.remove(PK);
        ret.remove(TYPE);
        ret.remove("itemtype");
        return ret;
    }


    protected static boolean checkMandatoryAttribute(String qualifier, ItemAttributeMap allAttributes, Set missingSet)
    {
        return checkMandatoryAttribute(qualifier, allAttributes, missingSet, false);
    }


    protected static boolean checkMandatoryAttribute(String qualifier, ItemAttributeMap allAttributes, Set<String> missingSet, boolean nullAllowed)
    {
        boolean gotKey = allAttributes.containsKey(qualifier);
        Object value = allAttributes.get(qualifier);
        if(!gotKey || (!nullAllowed && value == null))
        {
            missingSet.add(qualifier);
            return false;
        }
        return true;
    }


    public Object getSyncObject()
    {
        if(getTenant().getConfig().getBoolean("itemsync.enabled", true))
        {
            return this;
        }
        return new Integer(0);
    }


    private static void loadEJBImplClasses()
    {
        try
        {
            Class.forName(ItemEJBImpl.class.getName());
            Class.forName(LinkEJBImpl.class.getName());
            Class.forName(TestItemEJBImpl.class.getName());
            Class.forName(GenericItemEJBImpl.class.getName());
            Class.forName(AtomicTypeEJBImpl.class.getName());
            Class.forName(ComposedTypeEJBImpl.class.getName());
            Class.forName(CollectionTypeEJBImpl.class.getName());
            Class.forName(MapTypeEJBImpl.class.getName());
            Class.forName(AttributeDescriptorEJBImpl.class.getName());
            Class.forName(SearchRestrictionEJBImpl.class.getName());
            Class.forName(EnumerationValueEJBImpl.class.getName());
        }
        catch(ClassNotFoundException e)
        {
            throw new JaloInternalException(e, "error loading class/registering core wrapper", -1);
        }
    }


    public Object writeReplace() throws ObjectStreamException
    {
        if(this.isJaloOnly)
        {
            return this;
        }
        try
        {
            Class<Item> clazz = CGLibUtils.getOriginalClass(getClass());
            Item replacement = clazz.newInstance();
            replacement.thePK = this.thePK;
            replacement.isRemoved = this.isRemoved;
            if(this.tenant == null)
            {
                throw new IllegalStateException("cannot serialize item " + this + " without specified tenant");
            }
            replacement.tenant = this.tenant;
            return replacement;
        }
        catch(Exception e)
        {
            log.error("error replacing item " + getPK() + " for serialization - using item itself");
            return this;
        }
    }


    public Object readResolve() throws ObjectStreamException
    {
        if(this.isJaloOnly || this.isRemoved)
        {
            return this;
        }
        if(this.tenant == null)
        {
            throw new IllegalStateException("stream item " + this + " has got no tenant");
        }
        boolean mustSwitchTenant = !Registry.isCurrentTenant(this.tenant);
        Tenant backup = null;
        try
        {
            if(mustSwitchTenant)
            {
                backup = Registry.getCurrentTenantNoFallback();
                Registry.setCurrentTenant(this.tenant);
            }
            return this.isRemoved ? this : JaloSession.lookupItem(this.tenant, this.thePK);
        }
        catch(JaloItemNotFoundException e)
        {
            this.isRemoved = true;
            return this;
        }
        finally
        {
            if(mustSwitchTenant)
            {
                if(backup == null)
                {
                    Registry.unsetCurrentTenant();
                }
                else
                {
                    Registry.setCurrentTenant(backup);
                }
            }
        }
    }


    protected boolean isItemCheckBeforeRemoveableDisabled(SessionContext ctx)
    {
        if(ctx != null)
        {
            if(Boolean.TRUE.equals(ctx.getAttribute("disableItemCheckBeforeRemovable")))
            {
                return true;
            }
        }
        return false;
    }


    protected Map<String, AttributeMode> getDefaultAttributeModes()
    {
        return null;
    }


    public <T extends Item> Collection<T> getRelatedItems(String relationQualifier)
    {
        return null;
    }


    public <T extends Item> Collection<T> getRelatedItems(RelationsInfo relationsInfo)
    {
        return null;
    }


    public <T extends Item> boolean setRelatedItems(String relationQualifier, Collection<T> values)
    {
        return false;
    }


    protected abstract Item createItem(SessionContext paramSessionContext, ComposedType paramComposedType, ItemAttributeMap paramItemAttributeMap) throws JaloBusinessException;
}
