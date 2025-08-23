package de.hybris.platform.catalog.jalo.copy;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Level;

@Deprecated(since = "ages", forRemoval = false)
public class GenericCatalogCopyContext extends ItemCopyContext
{
    private final CatalogVersion sourceVersion;
    private final CatalogVersion targetVersion;
    private final SyncItemJob job;
    private Set<Language> targetLanguages;
    private boolean copyCategoryProducts = false;
    private boolean copyCategorySubcategories = false;


    public GenericCatalogCopyContext(SyncItemJob job, SessionContext ctx, Level logLevel, boolean forceUpdate)
    {
        super(ctx, logLevel, forceUpdate);
        this.job = job;
        this.sourceVersion = job.getSourceVersion();
        this.targetVersion = job.getTargetVersion();
        setTargetLanguages(null);
    }


    protected void setTargetLanguages(Collection<Language> languages)
    {
        if(languages != null && !languages.isEmpty())
        {
            this.targetLanguages = Collections.unmodifiableSet(new HashSet<>(languages));
        }
        else
        {
            Set<Language> set = new HashSet<>(getSourceVersion().getLanguages());
            set.retainAll(getTargetVersion().getLanguages());
            this.targetLanguages = Collections.unmodifiableSet(set);
        }
    }


    protected Set<Language> getTargetLanguages()
    {
        return (this.targetLanguages != null) ? this.targetLanguages : Collections.EMPTY_SET;
    }


    public void setCategoryOptions(boolean copyProducts, boolean copySubcategories)
    {
        if(started())
        {
            throw new IllegalStateException("cannot change category options after copying started");
        }
        this.copyCategoryProducts = copyProducts;
        this.copyCategorySubcategories = copySubcategories;
    }


    public final CatalogVersion getSourceVersion()
    {
        return this.sourceVersion;
    }


    public final CatalogVersion getTargetVersion()
    {
        return this.targetVersion;
    }


    protected SyncItemJob getJob()
    {
        return this.job;
    }


    protected boolean isUpToDate(ItemCopyCreator parentCC, Item original, Item copy)
    {
        boolean ret = super.isUpToDate(parentCC, original, copy);
        if(!ret)
        {
            Date lastCopyTS = CatalogManager.getInstance().getLastSyncModifiedTime(getJob(), original, copy);
            Date modTS = original.getModificationTime();
            ret = (lastCopyTS == modTS || (modTS != null && modTS.equals(lastCopyTS)));
        }
        return ret;
    }


    public boolean isFromSourceVersion(Item item) throws JaloInvalidParameterException
    {
        return getSourceVersion().equals(CatalogManager.getInstance().getCatalogVersion(getCtx(), item));
    }


    protected Object translate(AttributeCopyCreator acc)
    {
        Object<Object, Object> ret = (Object<Object, Object>)super.translate(acc);
        if(!acc.isPreset() && acc.getDescriptor().isLocalized())
        {
            Map src = (Map)ret;
            Map<Object, Object> map = new HashMap<>();
            for(Language l : getTargetLanguages())
            {
                map.put(l, (src != null) ? src.get(l) : null);
            }
            ret = (Object<Object, Object>)map;
        }
        return ret;
    }


    protected void finishedCopying(ItemCopyCreator icc)
    {
        super.finishedCopying(icc);
        setTimestamp(icc);
    }


    protected void setTimestamp(ItemCopyCreator icc)
    {
        CatalogManager catalogManager = CatalogManager.getInstance();
        PK srcPK = icc.getSourceItemPK();
        Item src = (srcPK != null) ? JaloSession.getCurrentSession().getItem(srcPK) : null;
        Map<PK, ItemSyncTimestamp> candidates = catalogManager.getSyncTimestampMap(src, getJob());
        ItemSyncTimestamp itemSyncTimestamp = candidates.get(icc.getTargetItemPK());
        if(itemSyncTimestamp != null)
        {
            itemSyncTimestamp.setLastSyncSourceModifiedTime(src.getModificationTime());
            itemSyncTimestamp.setLastSyncTime(new Date());
        }
        else
        {
            if(!candidates.isEmpty())
            {
                if(isErrorEnabled())
                {
                    error("detected multiple copies of source item " + icc.getSourceItemPK() + " : " + candidates.keySet() + " (timestamps:" + candidates
                                    .values() + ")");
                }
            }
            catalogManager.createSyncTimestamp(getJob(), src, icc.getTargetItemPK());
        }
    }


    protected Item findExistingCopyToUpdate(ItemCopyCreator parentCreator, Item original, boolean asPartOf)
    {
        Item copy = super.findExistingCopyToUpdate(parentCreator, original, asPartOf);
        if(copy == null)
        {
            CatalogManager catalogManager = CatalogManager.getInstance();
            copy = catalogManager.getSynchronizedCopy(original, getJob());
            if(copy == null && catalogManager.isCatalogItem(original) && isFromSourceVersion(original))
            {
                copy = catalogManager.getCounterpartItem(getCtx(), original, getTargetVersion());
                if(copy != null && isDebugEnabled())
                {
                    debug("found non-copy counterpart item of " + original + " -> " + copy);
                }
            }
            else if(isDebugEnabled())
            {
                debug("found synchronized copy of " + original + " -> " + copy);
            }
            if(copy != null)
            {
                registerCopy((original != null) ? original.getPK() : null, copy.getPK());
            }
            else if(isDebugEnabled())
            {
                debug("cannot find existing copy of " + original);
            }
            return copy;
        }
        return copy;
    }


    protected boolean mustBeTranslated(AttributeCopyCreator acc, Item original)
    {
        return (super.mustBeTranslated(acc, original) || (
                        CatalogManager.getInstance().isCatalogItem(original) && isFromSourceVersion(original)));
    }


    protected Map getCopyCreatorPresetValues(Item original, Item copyToupdate)
    {
        CatalogManager catalogManager = CatalogManager.getInstance();
        if(catalogManager.isCatalogItem(original) && isFromSourceVersion(original))
        {
            Map<Object, Object> map = new HashMap<>(super.getCopyCreatorPresetValues(original, copyToupdate));
            map.put(catalogManager.getCatalogVersionAttribute(getCtx(), original.getComposedType()).getQualifier(),
                            getTargetVersion());
            return map;
        }
        return super.getCopyCreatorPresetValues(original, copyToupdate);
    }


    protected boolean isUntranslatable(AttributeCopyCreator acc, Item original)
    {
        boolean untranslatable = super.isUntranslatable(acc, original);
        if(!untranslatable && CatalogManager.getInstance().isCatalogItem(original) && !isFromSourceVersion(original))
        {
            if(isDebugEnabled())
            {
                debug("found catalog item which does not originate from source version (item=" + original + ", attribute=" + acc + ") - do not translate");
            }
            return true;
        }
        return untranslatable;
    }


    protected boolean isPartOf(AttributeDescriptor attributeDescriptor)
    {
        int i, j;
        if(super.isPartOf(attributeDescriptor))
        {
            return true;
        }
        String attributeQualifier = attributeDescriptor.getQualifier();
        if(this.copyCategorySubcategories && "categories".equals(attributeQualifier))
        {
            Class<?> clazz = attributeDescriptor.getEnclosingType().getJaloClass();
            if(Category.class.isAssignableFrom(clazz))
            {
                return true;
            }
        }
        else if(this.copyCategoryProducts && "products".equals(attributeQualifier))
        {
            Class<?> clazz = attributeDescriptor.getEnclosingType().getJaloClass();
            if(Category.class.isAssignableFrom(clazz))
            {
                return true;
            }
        }
        Set catalogItemTypes = getContainedCatalogItemTypes(attributeDescriptor);
        if(catalogItemTypes == null || catalogItemTypes.isEmpty())
        {
            return false;
        }
        boolean pseudoPartOf = false;
        boolean mixedMode = false;
        for(Iterator<ComposedType> it = catalogItemTypes.iterator(); it.hasNext(); )
        {
            ComposedType composedType = it.next();
            Class<?> jaloClass = composedType.getJaloClass();
            i = pseudoPartOf | ((Media.class.isAssignableFrom(jaloClass) || Keyword.class.isAssignableFrom(jaloClass)) ? 1 : 0);
            j = mixedMode | ((Product.class.isAssignableFrom(jaloClass) || Category.class.isAssignableFrom(jaloClass)) ? 1 : 0);
        }
        if(i != 0 && j != 0 && isWarnEnabled())
        {
            warn("found attribute " + toString(attributeDescriptor) + " containing both pseudo partOf and referenced catalog item types " +
                            safeToString(catalogItemTypes) + " - cannot treat as partOf");
        }
        return (i != 0 && j == 0);
    }


    protected final Set getContainedCatalogItemTypes(AttributeDescriptor attributeDescriptor)
    {
        Set addTo = new HashSet();
        getContainedCatalogItems(attributeDescriptor, attributeDescriptor.getRealAttributeType(), attributeDescriptor
                        .isLocalized(), addTo);
        return addTo;
    }


    private final void getContainedCatalogItems(AttributeDescriptor attributeDescriptor, Type type, boolean localized, Set addTo)
    {
        if(type instanceof ComposedType)
        {
            getContainedCatalogItems(attributeDescriptor, (ComposedType)type, addTo);
        }
        else if(type instanceof CollectionType)
        {
            getContainedCatalogItems(attributeDescriptor, ((CollectionType)type).getElementType(), false, addTo);
        }
        else if(type instanceof MapType)
        {
            if(!localized)
            {
                getContainedCatalogItems(attributeDescriptor, ((MapType)type).getArgumentType(), false, addTo);
            }
            getContainedCatalogItems(attributeDescriptor, ((MapType)type).getReturnType(), false, addTo);
        }
    }


    protected void getContainedCatalogItems(AttributeDescriptor attributeDescriptor, ComposedType composedType, Set<ComposedType> addTo)
    {
        if(CatalogManager.getInstance().isCatalogItem(composedType))
        {
            addTo.add(composedType);
        }
    }


    protected Item getCopy(Object source)
    {
        Item copy = super.getCopy(source);
        if(copy == null && CatalogManager.getInstance().isCatalogItem((Item)source) && isFromSourceVersion((Item)source))
        {
            copy = getCounterpartItem((Item)source);
            if(copy != null)
            {
                registerCopy((source != null) ? ((Item)source).getPK() : null, copy.getPK());
                if(isDebugEnabled())
                {
                    debug("found catalog item copy " + source + "->" + copy + " and registered mapping");
                }
            }
        }
        return copy;
    }


    protected Item getCounterpartItem(Item original)
    {
        return CatalogManager.getInstance().getCounterpartItem(getCtx(), original, getTargetVersion());
    }
}
