package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ItemModelCloneCreator
{
    private static final Logger LOG = Logger.getLogger(ItemModelCloneCreator.class.getName());
    private final ModelService modelService;
    private final I18NService i18nService;
    private final TypeService typeService;


    public ItemModelCloneCreator(ModelService modelService, I18NService i18nService, TypeService typeService)
    {
        this.modelService = modelService;
        this.i18nService = i18nService;
        this.typeService = typeService;
    }


    public ItemModel copy(ItemModel original) throws CannotCloneException
    {
        return copy((ComposedTypeModel)null, original, new CopyContext());
    }


    public ItemModel copy(ItemModel original, ModelCloningContext cloningContext) throws CannotCloneException
    {
        return copy((ComposedTypeModel)null, original, new CopyContext(cloningContext));
    }


    public ItemModel copy(ItemModel original, CopyContext ctx) throws CannotCloneException
    {
        return copy((ComposedTypeModel)null, original, ctx);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public ItemModel copy(ComposedType targetType, ItemModel original, CopyContext ctx) throws CannotCloneException
    {
        return copy((ComposedTypeModel)this.modelService.toModelLayer(targetType), original, ctx);
    }


    public ItemModel copy(ComposedTypeModel targetType, ItemModel original, ModelCloningContext ctx) throws CannotCloneException
    {
        return copy(targetType, original, new CopyContext(ctx));
    }


    public ItemModel copy(ComposedTypeModel targetType, ItemModel original, CopyContext ctx) throws CannotCloneException
    {
        CopyItem rootWrapper = prepareCopy(targetType, original, ctx);
        boolean lastTurnGotModifications = true;
        for(Collection<CopyItem> items = ctx.getPendingItems(); lastTurnGotModifications && !items.isEmpty();
                        items = ctx.getPendingItems())
        {
            lastTurnGotModifications = false;
            for(CopyItem item : items)
            {
                lastTurnGotModifications |= process(item, ctx);
            }
        }
        if(ctx.hasPendingItems())
        {
            throw new CannotCloneException("could not copy " + original + " completely - got pending items", ctx);
        }
        return rootWrapper.getCopy();
    }


    public List<ItemModel> copyAll(List<ItemModel> originals, CopyContext ctx) throws CannotCloneException
    {
        return copyAll((ComposedTypeModel)null, originals, ctx);
    }


    public List<ItemModel> copyAll(ComposedTypeModel targetType, List<ItemModel> originals, CopyContext ctx) throws CannotCloneException
    {
        List<CopyItem> rootWrappers = new ArrayList<>(originals.size());
        for(ItemModel original : originals)
        {
            rootWrappers.add(prepareCopy(targetType, original, ctx));
        }
        boolean lastTurnGotModifications = true;
        for(Collection<CopyItem> items = ctx.getPendingItems(); lastTurnGotModifications && !items.isEmpty();
                        items = ctx.getPendingItems())
        {
            lastTurnGotModifications = false;
            for(CopyItem item : items)
            {
                lastTurnGotModifications |= process(item, ctx);
            }
        }
        if(ctx.hasPendingItems())
        {
            throw new CannotCloneException("could not copy " + originals + " completely - got pending items", ctx);
        }
        if(rootWrappers.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<ItemModel> ret = new ArrayList<>(rootWrappers.size());
        for(CopyItem wr : rootWrappers)
        {
            ret.add(wr.getCopy());
        }
        return ret;
    }


    public void copyAttributes(ItemModel source, ItemModel target, Set<String> attributes)
    {
        CopyItem ret = new CopyItem(this.typeService.getComposedTypeForCode(target.getItemtype()), source);
        Object object = new Object(this, attributes);
        CopyContext copyContext = new CopyContext((ModelCloningContext)object);
        readAttributes(ret, copyContext);
        ret.setCopy(target);
        process(ret, copyContext);
    }


    protected void readAttributes(CopyItem item, CopyContext ctx)
    {
        ComposedTypeModel targetType = item.getTargetType();
        ItemModel src = item.getOriginal();
        ComposedTypeModel srcType = this.typeService.getComposedTypeForClass(src.getClass());
        Locale before = this.i18nService.getCurrentLocale();
        Set<Locale> supportedLocales = null;
        try
        {
            for(AttributeDescriptorModel ad : this.typeService.getAttributeDescriptorsForType(srcType))
            {
                Set<ModifiersFlag> modifiers = ModifiersFlag.copyModifiers(ad);
                String qualifier = ad.getQualifier();
                if(!ad.getInitial().booleanValue() && !ad.getWritable().booleanValue())
                {
                    continue;
                }
                if(ctx.skipAttribute(src, qualifier))
                {
                    continue;
                }
                if("itemtype".equalsIgnoreCase(qualifier) || "pk".equalsIgnoreCase(qualifier))
                {
                    continue;
                }
                if(targetType != null)
                {
                    try
                    {
                        this.typeService.getAttributeDescriptor(targetType, qualifier);
                    }
                    catch(UnknownIdentifierException e)
                    {
                        continue;
                    }
                }
                if(ctx.gotPreset(src, qualifier))
                {
                    item.addPresetAttribute(ad, modifiers, ctx.getPreset(src, qualifier));
                    continue;
                }
                try
                {
                    if(ad.getLocalized().booleanValue())
                    {
                        if(supportedLocales == null)
                        {
                            supportedLocales = this.i18nService.getSupportedLocales();
                        }
                        Map<Locale, Object> locValue = new HashMap<>(supportedLocales.size());
                        for(Locale l : supportedLocales)
                        {
                            this.i18nService.setCurrentLocale(l);
                            Object object = this.modelService.getAttributeValue(src, qualifier);
                            if(!isEmpty(object))
                            {
                                locValue.put(l, object);
                            }
                        }
                        if(ctx.treatAsPartOf(src, qualifier))
                        {
                            modifiers.add(ModifiersFlag.PARTOF);
                        }
                        item.addAttribute(ad, modifiers, locValue);
                        continue;
                    }
                    Object value = this.modelService.getAttributeValue(src, qualifier);
                    if(!isEmpty(value))
                    {
                        if(ctx.treatAsPartOf(src, qualifier))
                        {
                            modifiers.add(ModifiersFlag.PARTOF);
                        }
                        item.addAttribute(ad, modifiers, value);
                    }
                }
                catch(AttributeNotSupportedException e)
                {
                    if(!"allDocuments".equals(qualifier) && !"assignedCockpitItemTemplates".equals(qualifier))
                    {
                        LOG.info("cannot copy attribute " + qualifier + " since model layer does not support it");
                    }
                }
            }
        }
        finally
        {
            this.i18nService.setCurrentLocale(before);
        }
    }


    protected boolean isEmpty(Object value)
    {
        if(value == null)
        {
            return true;
        }
        if(value instanceof Collection)
        {
            return ((Collection)value).isEmpty();
        }
        if(value instanceof Map)
        {
            return ((Map)value).isEmpty();
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected CopyItem prepareCopy(ComposedType targetType, ItemModel original, CopyContext ctx)
    {
        return prepareCopy((ComposedTypeModel)this.modelService.toModelLayer(targetType), original, ctx);
    }


    protected CopyItem prepareCopy(ComposedTypeModel targetType, ItemModel original, CopyContext ctx)
    {
        if(original == null)
        {
            throw new CannotCloneException("original was null", ctx);
        }
        if(ctx == null)
        {
            throw new CannotCloneException("ctx was null", ctx);
        }
        if(ctx.mustBeTranslated(original))
        {
            return ctx.getCopyWrapper(original);
        }
        CopyItem ret = new CopyItem(targetType, original);
        ctx.add(ret);
        readAttributes(ret, ctx);
        for(CopyAttribute attr : ret.getPartOfAttributes())
        {
            findPartOfItems(attr.getOriginalValue(), ctx);
        }
        return ret;
    }


    protected void findPartOfItems(Object originalValue, CopyContext ctx)
    {
        if(originalValue instanceof Collection)
        {
            Collection coll = (Collection)originalValue;
            if(!coll.isEmpty())
            {
                for(Object o : coll)
                {
                    findPartOfItems(o, ctx);
                }
            }
        }
        else if(originalValue instanceof Map)
        {
            Map<Object, Object> map = (Map<Object, Object>)originalValue;
            if(!map.isEmpty())
            {
                for(Map.Entry<Object, Object> e : map.entrySet())
                {
                    findPartOfItems(e.getValue(), ctx);
                }
            }
        }
        else if(originalValue instanceof ItemModel)
        {
            ItemModel item = (ItemModel)originalValue;
            prepareCopy(ctx.getTargetType(item), item, ctx);
        }
    }


    protected boolean process(CopyItem item, CopyContext ctx)
    {
        boolean modified = false;
        if(item.getCopy() == null)
        {
            Collection<CopyAttribute> attributes = translatePendingAttributes(item, ctx);
            if(attributes != null)
            {
                String copyType = (item.getTargetType() != null) ? item.getTargetType().getCode() : this.modelService.getModelType(item.getOriginal());
                ItemModel copy = (ItemModel)this.modelService.create(copyType);
                item.setCopy(copy);
                setAllAttributes(copy, attributes);
                consume(attributes);
                modified = true;
            }
        }
        else
        {
            Collection<CopyAttribute> attributes = translatePendingAttributes(item, ctx);
            if(attributes != null && !attributes.isEmpty())
            {
                setAllAttributes(item.getCopy(), attributes);
                consume(attributes);
                modified = true;
            }
        }
        return modified;
    }


    protected void setAllAttributes(ItemModel copy, Collection<CopyAttribute> attributes)
    {
        Locale before = this.i18nService.getCurrentLocale();
        try
        {
            for(CopyAttribute ca : attributes)
            {
                String qualifier = ca.getQualifier();
                Object value = ca.getTranslatedValue();
                if(ca.isLocalized())
                {
                    for(Map.Entry<Locale, Object> e : (Iterable<Map.Entry<Locale, Object>>)((Map)ca.getTranslatedValue()).entrySet())
                    {
                        this.i18nService.setCurrentLocale(e.getKey());
                        this.modelService.setAttributeValue(copy, qualifier, e.getValue());
                    }
                    continue;
                }
                this.modelService.setAttributeValue(copy, qualifier, value);
            }
        }
        finally
        {
            this.i18nService.setCurrentLocale(before);
        }
    }


    protected void consume(Collection<CopyAttribute> readyAttributes)
    {
        for(CopyAttribute ca : readyAttributes)
        {
            ca.setConsumed(true);
        }
    }


    protected Collection<CopyAttribute> translatePendingAttributes(CopyItem item, CopyContext ctx)
    {
        Collection<CopyAttribute> pending = item.getPendingAttributes();
        for(Iterator<CopyAttribute> it = pending.iterator(); it.hasNext(); )
        {
            CopyAttribute attr = it.next();
            if(!attr.isTranslated())
            {
                try
                {
                    attr.setTranslatedValue(translateAttributeValue(attr, attr.getOriginalValue(), ctx));
                }
                catch(CannotTranslateException e)
                {
                    if(item.getCopy() == null && attr.isRequiredForCreation())
                    {
                        return null;
                    }
                    it.remove();
                }
            }
        }
        return pending;
    }


    protected Object translateAttributeValue(CopyAttribute attr, Object originalValue, CopyContext ctx) throws CannotTranslateException
    {
        if(originalValue instanceof Collection)
        {
            Collection coll = (Collection)originalValue;
            if(coll.isEmpty())
            {
                return coll;
            }
            Collection<Object> ret = (coll instanceof Set) ? new LinkedHashSet(coll.size()) : new ArrayList(coll.size());
            for(Object o : coll)
            {
                ret.add(translateAttributeValue(attr, o, ctx));
            }
            return ret;
        }
        if(originalValue instanceof Map)
        {
            Map<Object, Object> map = (Map<Object, Object>)originalValue;
            if(map.isEmpty())
            {
                return map;
            }
            Map<Object, Object> ret = new LinkedHashMap<>(map.size());
            for(Map.Entry<Object, Object> e : map.entrySet())
            {
                ret.put(e.getKey(), translateAttributeValue(attr, e.getValue(), ctx));
            }
            return ret;
        }
        if(originalValue instanceof ItemModel)
        {
            ItemModel item = (ItemModel)originalValue;
            if(ctx.mustBeTranslated(item))
            {
                ItemModel copy = ctx.getCopy(item);
                if(copy == null)
                {
                    throw new CannotTranslateException(attr);
                }
                return copy;
            }
            return item;
        }
        return originalValue;
    }
}
