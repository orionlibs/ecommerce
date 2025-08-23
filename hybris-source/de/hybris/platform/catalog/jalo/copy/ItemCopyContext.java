package de.hybris.platform.catalog.jalo.copy;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.collections.fast.YLongSet;
import de.hybris.platform.util.collections.fast.YLongToLongMap;
import de.hybris.platform.util.collections.fast.YLongToObjectMap;
import de.hybris.platform.util.collections.fast.procedures.YLongAndObjectProcedure;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

@Deprecated(since = "ages", forRemoval = false)
public class ItemCopyContext
{
    private static final Logger LOGGER = Logger.getLogger(ItemCopyContext.class.getName());
    protected static final String UNIQUE_PREFIX = "Copy of ";
    private final Set<String> creationRequiredAttributes = new HashSet<>();
    private final Map<String, TypeCopyDescriptor> descriptorCache = new HashMap<>();
    private final SessionContext ctx;
    private final boolean forceUpdate;
    private boolean startedVariable = false;
    private boolean processingPendingReferences = false;
    private Level logLevel = Level.INFO;
    private boolean savePreviousValues = false;
    private int mediaTC = -1;
    private Map<PK, ItemCopyCreator> _currentlyCopying = new HashMap<>(10);
    private YLongSet processed = new YLongSet();
    private YLongToLongMap allMappings = new YLongToLongMap();
    private YLongToObjectMap<Set<AttributeCopyCreator>> waitingFor = new YLongToObjectMap();
    private YLongToObjectMap<Set<AttributeCopyCreator>> finallyUntranslatableValues = new YLongToObjectMap();


    public ItemCopyContext(SessionContext ctx, Level logLevel, boolean forceUpdate)
    {
        this.ctx = ctx;
        this.logLevel = logLevel;
        this.forceUpdate = forceUpdate;
        for(AttributeDescriptor ad : getCreationAttributes())
        {
            addCreationAttribute(ad);
        }
    }


    protected final Item toItem(long lpk)
    {
        if(lpk == 0L)
        {
            return null;
        }
        return toItem(PK.fromLong(lpk));
    }


    protected final Item toItem(PK pk)
    {
        if(pk == null)
        {
            return null;
        }
        JaloSession jSession = JaloSession.getCurrentSession();
        try
        {
            return jSession.getItem(pk);
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    protected final int getMediaTypeCode()
    {
        if(this.mediaTC < 0)
        {
            this.mediaTC = TypeManager.getInstance().getComposedType(Media.class).getItemTypeCode();
        }
        return this.mediaTC;
    }


    protected void setSavePrevousValues(boolean setSaveOn)
    {
        this.savePreviousValues = setSaveOn;
    }


    public boolean isSavePrevousValues()
    {
        return this.savePreviousValues;
    }


    protected void log(String message, Level level)
    {
        LOGGER.log((Priority)level, message);
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


    protected Set<AttributeDescriptor> getCreationAttributes()
    {
        Set<AttributeDescriptor> ret = new HashSet<>();
        TypeManager typeManager = TypeManager.getInstance();
        ComposedType composedType = typeManager.getComposedType(Product.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION));
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE));
        composedType = typeManager.getComposedType(VariantProduct.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate("baseProduct"));
        composedType = typeManager.getComposedType(Media.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION));
        composedType = typeManager.getComposedType(Category.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(CatalogConstants.Attributes.Category.CATALOGVERSION));
        composedType = typeManager.getComposedType(Keyword.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate("catalogVersion"));
        return ret;
    }


    protected final String translateAD(AttributeDescriptor attributeDescriptor)
    {
        return attributeDescriptor.getDeclaringEnclosingType().getCode() + "." + attributeDescriptor.getDeclaringEnclosingType().getCode();
    }


    protected final void addCreationAttribute(AttributeDescriptor attributeDescriptor)
    {
        this.creationRequiredAttributes.add(translateAD(attributeDescriptor));
    }


    private final void registerCopyDescriptor(String key, TypeCopyDescriptor descr)
    {
        this.descriptorCache.put(key, descr);
    }


    private final TypeCopyDescriptor getCachedDescriptor(String key)
    {
        return this.descriptorCache.get(key);
    }


    public final boolean started()
    {
        return this.startedVariable;
    }


    public void processPendingReferences()
    {
        this.processingPendingReferences = true;
        Set<ItemCopyCreator> iccs = new LinkedHashSet<>();
        this.waitingFor.forEachEntry((YLongAndObjectProcedure)new Object(this, iccs));
        this.waitingFor.clear();
        for(ItemCopyCreator icc : iccs)
        {
            try
            {
                icc.setPendingReferences(this, icc.getPendingAttributes());
            }
            catch(Exception e)
            {
                error("error setting pending references on " + icc + " : " + e.getMessage() + " \n" +
                                Utilities.getStackTraceAsString(e));
            }
        }
        this.processingPendingReferences = false;
        if(!this.finallyUntranslatableValues.isEmpty() && isInfoEnabled())
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Untranslatable Items Report \n");
            this.finallyUntranslatableValues.forEachEntry((YLongAndObjectProcedure)new Object(this, stringBuilder));
            this.finallyUntranslatableValues.clear();
            info(stringBuilder.toString());
        }
    }


    protected final PK transformCacheKey(Object object)
    {
        if(object instanceof Item)
        {
            return ((Item)object).getPK();
        }
        if(object instanceof PK)
        {
            return (PK)object;
        }
        if(object instanceof String)
        {
            throw new ClassCastException("class was " + object.getClass());
        }
        throw new ClassCastException("class was " + object.getClass());
    }


    protected final Set<PK> transformCacheKeys(Set objects)
    {
        if(objects == null || objects.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<PK> ret = new HashSet<>(objects.size());
        for(Iterator it = objects.iterator(); it.hasNext(); )
        {
            ret.add(transformCacheKey(it.next()));
        }
        return ret;
    }


    protected AttributeCopyCreator createAttributeCopyCreator(ItemCopyCreator icc, AttributeCopyDescriptor acd, boolean ispreset, Object sourceValue)
    {
        if(isDebugEnabled())
        {
            debug("createAttributeCopyCreator( ad=" + acd + ", preset=" + ispreset + ", source=" + sourceValue);
        }
        return new AttributeCopyCreator(icc, acd, ispreset, sourceValue);
    }


    protected Object translate(AttributeCopyCreator acc)
    {
        Object translated = null;
        if(acc.getDescriptor().isAtomic() || acc.isPreset())
        {
            Object original = acc.getSourceValue();
            translated = original;
            if(isDebugEnabled())
            {
                debug("left value for " + acc + " unchanged");
            }
        }
        else
        {
            Map<Item, Item> partOfMappings = acc.getDescriptor().isPartOf() ? copyPartOfValues(acc) : null;
            translated = translateValue(acc, acc.getSourceValue(), partOfMappings, acc.getDescriptor().isLocalized());
            if(isDebugEnabled())
            {
                debug("translated " + acc + " into " + translated);
            }
        }
        return translated;
    }


    protected Set<Item> collectPartOfItemsToCopy(AttributeCopyCreator acc)
    {
        Collection itemsToCopy = allItems(acc, acc.getSourceValue());
        if(itemsToCopy != null && !itemsToCopy.isEmpty())
        {
            Set<Item> ret = new HashSet();
            for(Iterator<Item> it = itemsToCopy.iterator(); it.hasNext(); )
            {
                Item poi = it.next();
                if(isUntranslatable(acc, poi))
                {
                    if(isDebugEnabled())
                    {
                        debug("found untranslatable partOf item " + poi + " for attribute " + acc.getDescriptor().getQualifier() + " - will not copy");
                    }
                    continue;
                }
                if(isDebugEnabled())
                {
                    debug("added translatable partOf item " + poi + " for attribute " + acc.getDescriptor().getQualifier());
                }
                ret.add(poi);
            }
            return ret;
        }
        return Collections.EMPTY_SET;
    }


    protected Map<Item, Item> copyPartOfValues(AttributeCopyCreator acc)
    {
        Map<Item, Item> ret = new HashMap<>();
        ItemCopyCreator parent = acc.getParent();
        for(Item poi : acc.getPartOfItemsToCopy())
        {
            Item copy;
            ItemCopyCreator itemCopyCreator = acc.getParent().getEnclosingCreatorFor(poi);
            if(itemCopyCreator != null)
            {
                copy = toItem(itemCopyCreator.getTargetItemPK());
            }
            else
            {
                copy = copy(parent, poi);
            }
            ret.put(poi, copy);
        }
        return ret;
    }


    protected final Collection allItems(AttributeCopyCreator acc, Object value)
    {
        return (value != null) ? allItems(acc, value, new HashSet(), acc.getDescriptor().isLocalized()) : Collections.EMPTY_SET;
    }


    protected final Collection allItems(AttributeCopyCreator acc, Object value, Collection<Object> valuesSoFar, boolean localized)
    {
        if(value == null)
        {
            return valuesSoFar;
        }
        if(value instanceof Collection)
        {
            for(Iterator it = ((Collection)value).iterator(); it.hasNext(); )
            {
                allItems(acc, it.next(), valuesSoFar, false);
            }
        }
        else if(value instanceof Map)
        {
            for(Iterator<Map.Entry> it = ((Map)value).entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = it.next();
                Object key = entry.getKey();
                if(!localized || !(key instanceof de.hybris.platform.jalo.c2l.Language))
                {
                    allItems(acc, key, valuesSoFar, false);
                }
                allItems(acc, entry.getValue(), valuesSoFar, false);
            }
        }
        else if(value instanceof Item)
        {
            valuesSoFar.add(value);
        }
        return valuesSoFar;
    }


    protected boolean isUntranslatable(AttributeCopyCreator acc, Item original)
    {
        String qualifier = acc.getDescriptor().getQualifier();
        if(isDebugEnabled())
        {
            debug("isUntranslatable( " + acc + "," + original + ") q = " + qualifier);
        }
        return false;
    }


    protected boolean isUpToDate(ItemCopyCreator parentCC, Item original, Item copy)
    {
        if(original == null || copy == null)
        {
            throw new IllegalArgumentException("original or copy was null");
        }
        return false;
    }


    protected boolean mustBeTranslated(AttributeCopyCreator acc, Item original)
    {
        return (isCurrentlyCopying(original) || hasCopy(original));
    }


    protected final Object translateValue(AttributeCopyCreator acc, Object original, Map<Item, Item> partOfMappings, boolean localized)
    {
        if(original instanceof Collection)
        {
            Collection coll = (Collection)original;
            if(coll.isEmpty())
            {
                return coll;
            }
            Collection<Object> newOne = (Collection)createNewContainerInstance(coll.getClass());
            Collection<Object> nonTranslateable = null;
            for(Iterator it = coll.iterator(); it.hasNext(); )
            {
                Object element = it.next();
                Object translated = translateValue(acc, element, partOfMappings, false);
                if(translated != null || element == null)
                {
                    newOne.add(translated);
                    continue;
                }
                if(isWarnEnabled())
                {
                    if(nonTranslateable == null)
                    {
                        nonTranslateable = new ArrayList();
                    }
                    nonTranslateable.add(element);
                }
            }
            if(nonTranslateable != null && isDebugEnabled())
            {
                debug("incomplete translation of " + acc.getDescriptor() + " (original=" + safeToString(coll) + ", translated=" +
                                safeToString(newOne) + ", untranslated=" + nonTranslateable + ")");
            }
            return newOne;
        }
        if(original instanceof Map)
        {
            Map map = (Map)original;
            if(map.isEmpty())
            {
                return map;
            }
            Map<Object, Object> newOne = (Map)createNewContainerInstance(map.getClass());
            Map<Object, Object> nonTranslateable = null;
            for(Iterator<Map.Entry> it = map.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = it.next();
                Object key = entry.getKey();
                Object translatedKey = localized ? key : translateValue(acc, key, partOfMappings, false);
                if(translatedKey != null)
                {
                    Object value = entry.getValue();
                    Object translatedValue = translateValue(acc, value, partOfMappings, false);
                    newOne.put(translatedKey, translatedValue);
                    if(translatedValue == null && value != null)
                    {
                        if(isWarnEnabled())
                        {
                            if(nonTranslateable == null)
                            {
                                nonTranslateable = new HashMap<>();
                            }
                            nonTranslateable.put(translatedKey, value);
                        }
                    }
                    continue;
                }
                if(isWarnEnabled())
                {
                    if(nonTranslateable == null)
                    {
                        nonTranslateable = new HashMap<>();
                    }
                    nonTranslateable.put(key, entry.getValue());
                }
            }
            if(nonTranslateable != null && isDebugEnabled())
            {
                debug("incomplete translation of " + acc.getDescriptor() + " (original=" + safeToString(map) + ", translated=" +
                                safeToString(newOne) + ", untranslated=" + nonTranslateable + ")");
            }
            return newOne;
        }
        if(original instanceof Item)
        {
            return translateValue(acc, (Item)original, partOfMappings);
        }
        return original;
    }


    protected Item translateValue(AttributeCopyCreator acc, Item original, Map<Item, Item> partOfMappings)
    {
        ItemCopyCreator itemCopyCreator = acc.getParent().getEnclosingCreatorFor(original);
        if(itemCopyCreator != null)
        {
            return toItem(itemCopyCreator.getTargetItemPK());
        }
        if(isUntranslatable(acc, original))
        {
            if(isDebugEnabled())
            {
                debug("found untranslatable item " + original + " for attribute " + acc.getDescriptor().getQualifier() + " - using original");
            }
            return original;
        }
        if(acc.getDescriptor().isPartOf())
        {
            Item copy = partOfMappings.get(original);
            if(copy == null)
            {
                registerPendingReference(acc, original);
            }
            return copy;
        }
        if(mustBeTranslated(acc, original))
        {
            Item copy = getCopy(original);
            if(copy != null)
            {
                if(isDebugEnabled())
                {
                    debug("found existing copy for non-partOf reference " + acc.getDescriptor() + ":" + original + "->" + copy);
                }
                return copy;
            }
            if(isDebugEnabled())
            {
                debug("cannot translate " + acc.getDescriptor() + " (value=" + original + ") - registering as pending attribute");
            }
            registerPendingReference(acc, original);
            return null;
        }
        if(isDebugEnabled())
        {
            debug(acc.getDescriptor().getQualifier() + ": found plain item reference " + acc.getDescriptor().getQualifier() + " which stays unchanged");
        }
        return original;
    }


    private final Object createNewContainerInstance(Class<?> type)
    {
        if(List.class.isAssignableFrom(type))
        {
            return new ArrayList();
        }
        if(Set.class.isAssignableFrom(type))
        {
            return SortedSet.class.isAssignableFrom(type) ? new TreeSet() : new LinkedHashSet();
        }
        if(Map.class.isAssignableFrom(type))
        {
            return SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();
        }
        return new ArrayList();
    }


    protected final void registerPendingReference(AttributeCopyCreator acc, Item waitingFor)
    {
        long lpk = transformCacheKey(waitingFor).getLongValue();
        if(!this.processingPendingReferences)
        {
            if(isDebugEnabled())
            {
                debug("added pending attribute " + acc + " for item " + acc.getParent());
            }
            acc.addPendingValue(lpk);
            Set<AttributeCopyCreator> waitingRefs = (Set<AttributeCopyCreator>)this.waitingFor.get(lpk);
            if(waitingRefs == null)
            {
                this.waitingFor.put(lpk, waitingRefs = new LinkedHashSet<>(3));
            }
            waitingRefs.add(acc);
        }
        else
        {
            if(isDebugEnabled())
            {
                debug("attribute " + acc + " for item " + acc.getParent() + " is finally pending - could not set");
            }
            Set<AttributeCopyCreator> waitingRefs = (Set<AttributeCopyCreator>)this.finallyUntranslatableValues.get(lpk);
            if(waitingRefs == null)
            {
                this.finallyUntranslatableValues.put(lpk, waitingRefs = new LinkedHashSet<>());
            }
            waitingRefs.add(acc);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected final boolean isCurrentlyCopying(Item original)
    {
        return isCurrentlyCopying(transformCacheKey(original));
    }


    protected final boolean isCurrentlyCopying(PK original)
    {
        return this._currentlyCopying.containsKey(original);
    }


    protected final void pushCurrentlyCopying(ItemCopyCreator icc)
    {
        this._currentlyCopying.put(icc.getSourceItemPK(), icc);
    }


    protected final void popCurrentlyCopying(ItemCopyCreator icc)
    {
        this._currentlyCopying.remove(icc.getSourceItemPK());
    }


    protected Item getCopy(Object source)
    {
        long lpk = this.allMappings.get(transformCacheKey(source).getLongValue());
        return (lpk != this.allMappings.getEmptyValue()) ? toItem(PK.fromLong(lpk)) : null;
    }


    protected final boolean hasCopy(Object source)
    {
        return (this.allMappings.get(transformCacheKey(source).getLongValue()) != this.allMappings.getEmptyValue());
    }


    public final boolean isAlreadyCopied(Item src)
    {
        return this.processed.contains(transformCacheKey(src).getLongValue());
    }


    protected final void addCopiedItem(PK source, PK copy)
    {
        this.allMappings.put(source.getLongValue(), copy.getLongValue());
        this.processed.add(source.getLongValue());
    }


    protected final void registerCopy(PK source, PK copy)
    {
        this.allMappings.put(source.getLongValue(), copy.getLongValue());
        checkPendingReferences(source);
    }


    protected final void checkPendingReferences(PK key)
    {
        if(isDebugEnabled())
        {
            debug("checking pending refs for newly copied item " + key);
        }
        Set<AttributeCopyCreator> waitingRefs = (this.waitingFor != null) ? (Set<AttributeCopyCreator>)this.waitingFor.remove(key.getLongValue()) : null;
        if(waitingRefs != null && !waitingRefs.isEmpty())
        {
            if(isDebugEnabled())
            {
                debug("found pending refs " + waitingRefs + " for newly copied item " + key);
            }
            Map<ItemCopyCreator, Set<AttributeCopyCreator>> iccs = null;
            Iterator<AttributeCopyCreator> it;
            label48:
            for(it = waitingRefs.iterator(); it.hasNext(); )
            {
                AttributeCopyCreator acc = it.next();
                it.remove();
                if(acc.isPending())
                {
                    acc.removePendingValue(key.getLongValue());
                    for(long pendingToo : acc.getPendingValuesToTranslate())
                    {
                        if(!this.allMappings.containsKey(pendingToo))
                        {
                            continue label48;
                        }
                        acc.removePendingValue(pendingToo);
                    }
                    ItemCopyCreator icc = acc.getParent();
                    Set<AttributeCopyCreator> refs = (iccs != null) ? iccs.get(icc) : null;
                    if(iccs == null)
                    {
                        iccs = new LinkedHashMap<>();
                    }
                    if(refs == null)
                    {
                        iccs.put(icc, refs = new LinkedHashSet<>());
                    }
                    refs.add(acc);
                }
            }
            if(iccs != null)
            {
                for(it = iccs.entrySet().iterator(); it.hasNext(); )
                {
                    Map.Entry<ItemCopyCreator, Set<AttributeCopyCreator>> e = (Map.Entry<ItemCopyCreator, Set<AttributeCopyCreator>>)it.next();
                    try
                    {
                        ((ItemCopyCreator)e.getKey()).setPendingReferences(this, e.getValue());
                    }
                    catch(Exception ex)
                    {
                        error("error setting pending references " + e.getValue() + " on " + e.getKey() + " : " + ex.getMessage() + " \n" +
                                        Utilities.getStackTraceAsString(ex));
                    }
                }
            }
        }
    }


    protected void beforeCopying(ItemCopyCreator icc)
    {
        pushCurrentlyCopying(icc);
        if(isDebugEnabled())
        {
            debug("starting to " + (icc.isUpdate() ? "update" : "copy") + " " + icc.getSourceItemPK());
        }
    }


    protected void finishedCopying(ItemCopyCreator icc)
    {
        if(icc.getSourceItemPK().getTypeCode() == getMediaTypeCode())
        {
            Media mSrc = (Media)toItem(icc.getSourceItemPK());
            if(mSrc.hasData())
            {
                copyMediaData(mSrc, (Media)toItem(icc.getTargetItemPK()));
            }
        }
        registerCopy(icc.getSourceItemPK(), icc.getTargetItemPK());
        this.processed.add(icc.getSourceItemPK().getLongValue());
        if(isDebugEnabled())
        {
            debug(icc.getReport());
        }
    }


    protected void copyMediaData(Media src, Media tgt)
    {
        try
        {
            long time1 = System.currentTimeMillis();
            tgt.setData(src);
            long time2 = System.currentTimeMillis();
            if(isDebugEnabled())
            {
                debug("copied media data from " + src.getPK() + "(" + src.getCode() + ") to " + tgt.getPK() + "(" + tgt.getCode() + ") in " + time2 - time1 + "ms");
            }
        }
        catch(Exception e)
        {
            if(isErrorEnabled())
            {
                error("could not copy data from " + src.getPK() + " to " + tgt.getPK() + " due to " + e.getLocalizedMessage());
            }
            try
            {
                tgt.setRealFileName(src.getRealFileName());
                tgt.setMime(src.getMime());
            }
            catch(Exception exception)
            {
            }
        }
    }


    public Item copy(Item original)
    {
        return copy(original, null);
    }


    public Item copy(Item original, Item existingCopy)
    {
        return copy(null, original, existingCopy);
    }


    protected Item copy(ItemCopyCreator parent, Item original)
    {
        return copy(parent, original, null);
    }


    protected Item copy(ItemCopyCreator parent, Item original, Item copyToUpdate)
    {
        Item actualCopyToUpdate;
        this.startedVariable = true;
        boolean partOf = (parent != null);
        if(isCurrentlyCopying(original))
        {
            throw new IllegalStateException("found existing copy creator for " + (partOf ? "partOf" : "") + " item " + original + (
                            partOf ? (" within " + parent) : ""));
        }
        if(isAlreadyCopied(original))
        {
            Item copy = getCopy(original);
            if(copyToUpdate != null && !copyToUpdate.equals(copy))
            {
                throw new IllegalStateException((partOf ? "partOf" : "") + "item " + (partOf ? "partOf" : "") + " is already copied to " + original + " - cannot copy to requested target item " + copy);
            }
            if(isDebugEnabled())
            {
                debug("found existing copy of " + original + " = " + copy + " - no need to copy again");
            }
            return copy;
        }
        if(copyToUpdate == null)
        {
            actualCopyToUpdate = findExistingCopyToUpdate(parent, original, partOf);
        }
        else
        {
            actualCopyToUpdate = copyToUpdate;
        }
        if(actualCopyToUpdate != null && !this.forceUpdate && isUpToDate(parent, original, actualCopyToUpdate))
        {
            if(isDebugEnabled())
            {
                debug("item " + original + " is up to date with existing coy " + actualCopyToUpdate + " - no need to copy");
            }
            return actualCopyToUpdate;
        }
        Map preset = getCopyCreatorPresetValues(original, actualCopyToUpdate);
        Set<?> blacklist = getCopyCreatorBlacklist(original, actualCopyToUpdate);
        Set<?> whitelist = getCopyCreatorWhitelist(original, actualCopyToUpdate);
        if(!preset.isEmpty())
        {
            if(blacklist != null && !blacklist.isEmpty())
            {
                blacklist = new HashSet(blacklist);
                for(Iterator<?> it = blacklist.iterator(); it.hasNext(); )
                {
                    String qualifier = (String)it.next();
                    if(preset.containsKey(qualifier))
                    {
                        it.remove();
                    }
                }
            }
            if(whitelist != null && !whitelist.isEmpty())
            {
                whitelist = new HashSet(whitelist);
                whitelist.addAll(preset.keySet());
            }
        }
        ItemCopyCreator icc = new ItemCopyCreator(this, parent, original, actualCopyToUpdate, blacklist, whitelist, preset);
        beforeCopying(icc);
        Item ret = icc.copy(this);
        if(ret != null)
        {
            finishedCopying(icc);
        }
        popCurrentlyCopying(icc);
        return ret;
    }


    protected Map getCopyCreatorPresetValues(Item original, Item copyToUpdate)
    {
        return Collections.EMPTY_MAP;
    }


    protected Set getCopyCreatorBlacklist(Item original, Item toUpdate)
    {
        Set<String> blacklist = new HashSet();
        blacklist.add(Item.MODIFIED_TIME);
        if(original instanceof Media && ((Media)original).hasData())
        {
            if(isDebugEnabled())
            {
                debug("found media " + ((Media)original).getCode() + " with data");
            }
            blacklist.add("dataPK");
            blacklist.add("size");
            blacklist.add("url");
            blacklist.add("internalURL");
            blacklist.add("url2");
            blacklist.add("location");
            blacklist.add("locationHash");
            blacklist.add("realFileName");
            blacklist.add("mime");
        }
        return blacklist;
    }


    protected Set getCopyCreatorWhitelist(Item original, Item toUpdate)
    {
        return Collections.EMPTY_SET;
    }


    protected Item findExistingCopyToUpdate(ItemCopyCreator parentCreator, Item original, boolean asPartOf)
    {
        return getCopy(original);
    }


    public final TypeCopyDescriptor getTypeCopyDescriptor(ComposedType type)
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


    protected boolean ignoreAttribute(AttributeDescriptor attributeDescriptor, String qualifier)
    {
        return Item.TYPE.equalsIgnoreCase(qualifier);
    }


    protected boolean isPartOf(AttributeDescriptor attributeDescriptor)
    {
        return attributeDescriptor.isPartOf();
    }


    public boolean forceCopy(AttributeDescriptor attributeDescriptor)
    {
        return false;
    }


    protected boolean isAtomic(AttributeDescriptor attributeDescriptor)
    {
        return isAtomic(attributeDescriptor.getRealAttributeType(), attributeDescriptor.isLocalized());
    }


    protected final boolean isAtomic(Type attributeType, boolean localized)
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


    protected boolean isRequiredForCreation(AttributeDescriptor attributeDescriptor)
    {
        return (!attributeDescriptor.isOptional() || this.creationRequiredAttributes.contains(translateAD(attributeDescriptor)));
    }


    protected final Set<AttributeCopyDescriptor>[] splitDescriptors(TypeCopyDescriptor tcd, ComposedType composedType)
    {
        Set<AttributeCopyDescriptor> initialOnly = null;
        Set<AttributeCopyDescriptor> initial = null;
        Set<AttributeCopyDescriptor> partOf = null;
        Set<AttributeCopyDescriptor> others = null;
        Set<AttributeDescriptor> ignored = null;
        Set<AttributeDescriptor> initialSelectionOf = null;
        for(AttributeDescriptor ad : composedType.getAttributeDescriptorsIncludingPrivate())
        {
            String qualifier = ad.getQualifier();
            if(ignoreAttribute(ad, qualifier))
            {
                if(isDebugEnabled())
                {
                    if(ignored == null)
                    {
                        ignored = new HashSet<>();
                    }
                    ignored.add(ad);
                }
                continue;
            }
            if(ad.isWritable() || ad.isInitial())
            {
                int modifiers = 0;
                if(isPartOf(ad))
                {
                    modifiers += 2;
                }
                if(isAtomic(ad))
                {
                    modifiers++;
                }
                if(ad.isLocalized())
                {
                    modifiers += 4;
                }
                if(ad.isUnique())
                {
                    modifiers += 8;
                }
                if(!ad.isWritable())
                {
                    modifiers += 16;
                }
                if(!ad.isOptional())
                {
                    modifiers += 32;
                }
                AttributeCopyDescriptor adc = new AttributeCopyDescriptor(tcd, qualifier, modifiers);
                if(adc.isAtomic() || adc.isInitialOnly() || isRequiredForCreation(ad))
                {
                    if(!adc.isAtomic() && ad.isPartOf())
                    {
                        throw new JaloInvalidParameterException("cannot copy initial non-optional attribute " + toString(ad) + " before creating parent item", 0);
                    }
                    if(ad.getSelectionOf() != null)
                    {
                        if(initialSelectionOf == null)
                        {
                            initialSelectionOf = new HashSet<>();
                        }
                        initialSelectionOf.add(ad);
                    }
                    if(adc.isInitialOnly())
                    {
                        if(initialOnly == null)
                        {
                            initialOnly = new HashSet<>();
                        }
                        initialOnly.add(adc);
                        continue;
                    }
                    if(initial == null)
                    {
                        initial = new HashSet<>();
                    }
                    initial.add(adc);
                    continue;
                }
                if(adc.isPartOf())
                {
                    if(partOf == null)
                    {
                        partOf = new HashSet<>();
                    }
                    partOf.add(adc);
                    continue;
                }
                if(others == null)
                {
                    others = new HashSet<>();
                }
                others.add(adc);
                continue;
            }
            if(isDebugEnabled())
            {
                if(ignored == null)
                {
                    ignored = new HashSet<>();
                }
                ignored.add(ad);
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


    protected String toString(AttributeDescriptor attributeDescriptor)
    {
        return attributeDescriptor.getEnclosingType().getCode() + "." + attributeDescriptor.getEnclosingType().getCode();
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


    public void cleanup()
    {
        this.descriptorCache.clear();
        this.creationRequiredAttributes.clear();
        this.allMappings = null;
        this.processed = null;
        this._currentlyCopying = null;
        this.finallyUntranslatableValues = null;
        this.waitingFor = null;
    }
}
