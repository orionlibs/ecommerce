package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public abstract class AbstractItemCopyContext
{
    private static final Logger LOGGER = Logger.getLogger(AbstractItemCopyContext.class.getName());
    private final Map<String, TypeCopyDescriptor> descriptorCache = new HashMap<>();
    private final JaloSession session;
    protected SessionContext ctx;
    private final Level logLevel;
    private CatalogManager _cm;
    private FlexibleSearch _fs;


    protected AbstractItemCopyContext(SessionContext ctx, Level logLevel)
    {
        this.session = createSession();
        this.ctx = ctx;
        this.logLevel = logLevel;
    }


    protected JaloSession createSession()
    {
        return JaloSession.getCurrentSession();
    }


    protected void log(String message, Level level, Throwable e)
    {
        LOGGER.log((Priority)level, message, e);
    }


    protected void log(String message, Level level)
    {
        LOGGER.log((Priority)level, message);
    }


    public void debug(String message, Throwable e)
    {
        log(message, Level.DEBUG, e);
    }


    public void info(String message, Throwable e)
    {
        log(message, Level.INFO, e);
    }


    public void warn(String message, Throwable e)
    {
        log(message, Level.WARN, e);
    }


    public void error(String message, Throwable e)
    {
        log(message, Level.ERROR, e);
    }


    public void debug(String message)
    {
        log(message, Level.DEBUG);
    }


    public void info(String message)
    {
        log(message, Level.INFO);
    }


    public void warn(String message)
    {
        log(message, Level.WARN);
    }


    public void error(String message)
    {
        log(message, Level.ERROR);
    }


    public boolean isDebugEnabled()
    {
        return Level.DEBUG.isGreaterOrEqual((Priority)this.logLevel);
    }


    public boolean isInfoEnabled()
    {
        return Level.INFO.isGreaterOrEqual((Priority)this.logLevel);
    }


    public boolean isWarnEnabled()
    {
        return Level.WARN.isGreaterOrEqual((Priority)this.logLevel);
    }


    public boolean isErrorEnabled()
    {
        return Level.ERROR.isGreaterOrEqual((Priority)this.logLevel);
    }


    public final SessionContext getCtx()
    {
        return this.ctx;
    }


    protected final JaloSession getSession()
    {
        return this.session;
    }


    protected Collection<Item> allItems(AttributeCopyCreator acc, Object value)
    {
        return (value != null) ? collectItems(acc, value, acc.getDescriptor().isLocalized()) : Collections.EMPTY_SET;
    }


    private final Collection<Item> collectItems(AttributeCopyCreator acc, Object value, boolean localized)
    {
        if(value instanceof Item)
        {
            return Collections.singleton((Item)value);
        }
        if(value instanceof Collection)
        {
            Collection coll = (Collection)value;
            Collection<Item> ret = null;
            if(!coll.isEmpty())
            {
                for(Object o : coll)
                {
                    Collection<Item> elementCollected = collectItems(acc, o, false);
                    if(!elementCollected.isEmpty())
                    {
                        if(ret == null)
                        {
                            ret = new LinkedHashSet<>();
                        }
                        ret.addAll(elementCollected);
                    }
                }
            }
            return (ret != null) ? ret : Collections.EMPTY_SET;
        }
        if(value instanceof Map)
        {
            Map map = (Map)value;
            Collection<Item> ret = null;
            if(!map.isEmpty())
            {
                for(Map.Entry e : ((Map)value).entrySet())
                {
                    Object key = e.getKey();
                    Collection<Item> keyCollected = null;
                    Collection<Item> valueCollected = null;
                    if(!localized || !(key instanceof de.hybris.platform.jalo.c2l.Language))
                    {
                        keyCollected = collectItems(acc, key, false);
                        if(keyCollected != null && !keyCollected.isEmpty())
                        {
                            if(ret == null)
                            {
                                ret = new LinkedHashSet<>();
                            }
                            ret.addAll(keyCollected);
                        }
                    }
                    valueCollected = collectItems(acc, e.getValue(), false);
                    if(!valueCollected.isEmpty())
                    {
                        if(ret == null)
                        {
                            ret = new LinkedHashSet<>();
                        }
                        ret.addAll(valueCollected);
                    }
                }
            }
            return (ret != null) ? ret : Collections.EMPTY_SET;
        }
        return Collections.EMPTY_SET;
    }


    public TypeCopyDescriptor getTypeCopyDescriptor(ComposedType type)
    {
        String key = type.getCode();
        TypeCopyDescriptor typeCopyDescriptor = getCachedDescriptor(key);
        if(typeCopyDescriptor == null)
        {
            if(isDebugEnabled())
            {
                debug("creating new type descriptor " + key);
            }
            registerCopyDescriptor(key, typeCopyDescriptor = createCopyDescriptor(type));
        }
        return typeCopyDescriptor;
    }


    protected TypeCopyDescriptor createCopyDescriptor(ComposedType composedType)
    {
        return new TypeCopyDescriptor(this, composedType);
    }


    private final void registerCopyDescriptor(String key, TypeCopyDescriptor descr)
    {
        this.descriptorCache.put(key, descr);
    }


    private final TypeCopyDescriptor getCachedDescriptor(String key)
    {
        return this.descriptorCache.get(key);
    }


    protected Set<AttributeCopyDescriptor>[] splitDescriptors(TypeCopyDescriptor tcd, ComposedType composedType)
    {
        Set<AttributeCopyDescriptor> initialOnly = null;
        Set<AttributeCopyDescriptor> initial = null;
        Set<AttributeCopyDescriptor> partOf = null;
        Set<AttributeCopyDescriptor> others = null;
        Set<AttributeDescriptor> ignored = null;
        Set<AttributeDescriptor> initialSelectionOf = null;
        for(AttributeDescriptor attributeDescriptor : composedType.getAttributeDescriptorsIncludingPrivate())
        {
            String qualifier = attributeDescriptor.getQualifier();
            if(ignoreAttribute(attributeDescriptor, qualifier))
            {
                if(isDebugEnabled())
                {
                    if(ignored == null)
                    {
                        ignored = new HashSet<>();
                    }
                    ignored.add(attributeDescriptor);
                }
                continue;
            }
            if(attributeDescriptor.isWritable() || attributeDescriptor.isInitial())
            {
                int modifiers = 0;
                if(isPartOf(attributeDescriptor))
                {
                    modifiers += 2;
                }
                if(isCopyOnDemand(attributeDescriptor))
                {
                    modifiers += 64;
                }
                if(isAtomic(attributeDescriptor))
                {
                    modifiers++;
                }
                if(attributeDescriptor.isLocalized())
                {
                    modifiers += 4;
                }
                if(!attributeDescriptor.isWritable())
                {
                    modifiers += 16;
                }
                if(!attributeDescriptor.isOptional())
                {
                    modifiers += 32;
                }
                AttributeCopyDescriptor adc = new AttributeCopyDescriptor(tcd, qualifier, modifiers);
                if(adc.isAtomic() || adc.isInitialOnly() || attributeDescriptor.isUnique() ||
                                isRequiredForCreation(attributeDescriptor))
                {
                    if(!adc.isAtomic() && attributeDescriptor.isPartOf())
                    {
                        throw new JaloInvalidParameterException("cannot copy initial non-optional attribute " +
                                        toString(attributeDescriptor) + " before creating parent item", 0);
                    }
                    if(attributeDescriptor.getSelectionOf() != null)
                    {
                        if(initialSelectionOf == null)
                        {
                            initialSelectionOf = new LinkedHashSet<>();
                        }
                        initialSelectionOf.add(attributeDescriptor);
                    }
                    if(adc.isInitialOnly())
                    {
                        if(initialOnly == null)
                        {
                            initialOnly = new LinkedHashSet<>();
                        }
                        initialOnly.add(adc);
                        continue;
                    }
                    if(initial == null)
                    {
                        initial = new LinkedHashSet<>();
                    }
                    initial.add(adc);
                    continue;
                }
                if(adc.isPartOf())
                {
                    if(partOf == null)
                    {
                        partOf = new LinkedHashSet<>();
                    }
                    partOf.add(adc);
                    continue;
                }
                if(others == null)
                {
                    others = new LinkedHashSet<>();
                }
                others.add(adc);
                continue;
            }
            if(isDebugEnabled())
            {
                if(ignored == null)
                {
                    ignored = new LinkedHashSet<>();
                }
                ignored.add(attributeDescriptor);
            }
        }
        if(initialSelectionOf != null)
        {
            for(AttributeDescriptor ad : initialSelectionOf)
            {
                AttributeCopyDescriptor dummy = new AttributeCopyDescriptor(tcd, ad.getSelectionOf().getQualifier(), 0);
                if(isDebugEnabled())
                {
                    debug("checking selectionOf attribute " + toString(ad) + "->" + dummy + " against initial attributes " + initialOnly + " and " + initial);
                }
                if(!initialOnly.contains(dummy) && !initial.contains(dummy))
                {
                    throw new JaloInvalidParameterException("cannot copy since initial selectionOf attribute " + toString(ad) + " requires attribute " + dummy + " to be initial too", 0);
                }
            }
        }
        if(isDebugEnabled())
        {
            debug("split attribute descriptors : initialonly=" + initialOnly + ",initial=" + initial + ",partOf=" + partOf + ",other=" + others);
        }
        return (Set<AttributeCopyDescriptor>[])new Set[] {initialOnly, initial, partOf, others};
    }


    protected boolean isAtomic(AttributeDescriptor attributeDescriptor)
    {
        return isAtomic(attributeDescriptor.getRealAttributeType(), attributeDescriptor.isLocalized());
    }


    protected boolean isAtomic(Type attributeType, boolean localized)
    {
        if(attributeType instanceof CollectionType)
        {
            return isAtomic(((CollectionType)attributeType).getElementType(), false);
        }
        if(attributeType instanceof MapType)
        {
            MapType mapType = (MapType)attributeType;
            return ((localized || isAtomic(mapType.getArgumentType(), false)) &&
                            isAtomic(mapType.getReturnType(), false));
        }
        return attributeType instanceof de.hybris.platform.jalo.type.AtomicType;
    }


    protected boolean isPartOf(AttributeDescriptor attributeDescriptor)
    {
        return attributeDescriptor.isPartOf();
    }


    protected boolean isCopyOnDemand(AttributeDescriptor attributeDescriptor)
    {
        return false;
    }


    protected boolean isRequiredForCreation(AttributeDescriptor attributeDescriptor)
    {
        return !attributeDescriptor.isOptional();
    }


    protected boolean ignoreAttribute(AttributeDescriptor attributeDescriptor, String qualifier)
    {
        return Item.TYPE.equalsIgnoreCase(qualifier);
    }


    protected String toString(AttributeDescriptor attributeDescriptor)
    {
        return attributeDescriptor.getEnclosingType().getCode() + "." + attributeDescriptor.getEnclosingType().getCode();
    }


    protected String safeToString(Object attributeValue)
    {
        if(attributeValue == null)
        {
            return "<NULL>";
        }
        if(attributeValue instanceof ComposedType)
        {
            return "ComposedType '" + ((ComposedType)attributeValue).getCode() + "'";
        }
        if(attributeValue instanceof AttributeDescriptor)
        {
            return "Attribute " + ((AttributeDescriptor)attributeValue).getEnclosingType().getCode() + "." + ((AttributeDescriptor)attributeValue)
                            .getQualifier();
        }
        if(attributeValue instanceof Collection)
        {
            StringBuilder stringBuilder = new StringBuilder("(");
            for(Iterator it = ((Collection)attributeValue).iterator(); it.hasNext(); )
            {
                stringBuilder.append(safeToString(it.next()));
                if(it.hasNext())
                {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        if(attributeValue instanceof Map)
        {
            StringBuilder stringBuilder = new StringBuilder("{");
            for(Iterator it = ((Map)attributeValue).entrySet().iterator(); it.hasNext(); )
            {
                stringBuilder.append(safeToString(it.next()));
                if(it.hasNext())
                {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
        if(attributeValue instanceof PK)
        {
            return ((PK)attributeValue).getLongValueAsString();
        }
        return attributeValue.toString();
    }


    protected String valuesToString(Map attributeValues)
    {
        StringBuilder stringBuilder = new StringBuilder("{");
        for(Iterator<Map.Entry> it = attributeValues.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            String qualifier = (String)entry.getKey();
            Object value = entry.getValue();
            stringBuilder.append("'").append(qualifier).append("'->").append(safeToString(value));
            if(it.hasNext())
            {
                stringBuilder.append(" , ");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }


    public void cleanup()
    {
        this.descriptorCache.clear();
        this.ctx = null;
    }


    protected CatalogManager getCatalogManager()
    {
        if(this._cm == null)
        {
            this._cm = CatalogManager.getInstance();
        }
        return this._cm;
    }


    protected FlexibleSearch getFlexibleSearch()
    {
        if(this._fs == null)
        {
            this._fs = FlexibleSearch.getInstance();
        }
        return this._fs;
    }


    static Level getLogLevel(CatalogVersionSyncCronJob cronJob)
    {
        if(cronJob == null)
        {
            return Level.OFF;
        }
        Level dbLevel = cronJob.isLogToDatabaseAsPrimitive() ? cronJob.convertEnumToLogLevel(cronJob.getLogLevelDatabase()) : Level.OFF;
        Level fileLevel = cronJob.isLogToFileAsPrimitive() ? cronJob.convertEnumToLogLevel(cronJob.getLogLevelFile()) : Level.OFF;
        return dbLevel.isGreaterOrEqual((Priority)fileLevel) ? fileLevel : dbLevel;
    }


    protected Item toItem(PK pk)
    {
        if(pk == null)
        {
            return null;
        }
        try
        {
            return getSession().getItem(pk);
        }
        catch(JaloObjectNoLongerValidException e)
        {
            return null;
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    protected Collection<? extends Item> toItems(Collection<PK> pks)
    {
        if(pks == null || pks.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        try
        {
            return getSession().getItems(null, pks, true, false);
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }
}
