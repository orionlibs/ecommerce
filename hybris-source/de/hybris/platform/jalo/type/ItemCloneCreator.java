package de.hybris.platform.jalo.type;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.util.JaloPropertyContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class ItemCloneCreator
{
    private static final String COPYING_PROPERTIES_ENABLED = "copying.properties.enabled";


    public Item copy(Item original) throws CannotCloneException, JaloBusinessException
    {
        return copy(null, original, new CopyContext());
    }


    public Item copy(Item original, CopyContext ctx) throws CannotCloneException, JaloBusinessException
    {
        return copy(null, original, ctx);
    }


    public Item copy(ComposedType targetType, Item original, CopyContext ctx) throws CannotCloneException, JaloBusinessException
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
        for(CopyItem ci : ctx.getCopyUntypedItems())
        {
            copyUntypedPropertiesWhenEnabled(ci, ctx);
        }
        return rootWrapper.getCopy();
    }


    private void copyUntypedPropertiesWhenEnabled(CopyItem item, CopyContext ctx)
    {
        boolean enabled = Registry.getCurrentTenant().getConfig().getBoolean("copying.properties.enabled", false);
        if(enabled)
        {
            copyUntypedProperties(item, ctx);
        }
    }


    public List<? extends Item> copyAll(List<? extends Item> originals, CopyContext ctx) throws CannotCloneException, JaloBusinessException
    {
        List<CopyItem> rootWrappers = new ArrayList<>(originals.size());
        for(Item original : originals)
        {
            rootWrappers.add(prepareCopy(null, original, ctx));
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
        for(CopyItem ci : ctx.getCopyUntypedItems())
        {
            copyUntypedPropertiesWhenEnabled(ci, ctx);
        }
        if(rootWrappers.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<Item> ret = new ArrayList<>(rootWrappers.size());
        for(CopyItem wr : rootWrappers)
        {
            ret.add(wr.getCopy());
        }
        return ret;
    }


    protected void copyUntypedProperties(CopyItem item, CopyContext ctx)
    {
        ComposedType copyType = item.getCopy().getComposedType();
        JaloPropertyContainer cont = null;
        for(Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)((ExtensibleItem)item.getOriginal())
                        .getAllProperties(null).entrySet())
        {
            if(!copyType.hasAttribute(e.getKey()))
            {
                if(cont == null)
                {
                    cont = JaloSession.getCurrentSession().createPropertyContainer();
                }
                try
                {
                    Object translated = translateAttributeValue(null, e.getValue(), ctx);
                    cont.setProperty(e.getKey(), translated);
                }
                catch(CannotTranslateException e1)
                {
                    throw new JaloSystemException(e1);
                }
            }
        }
        if(cont != null)
        {
            try
            {
                ((ExtensibleItem)item.getCopy()).setAllProperties(null, cont);
            }
            catch(ConsistencyCheckException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
    }


    protected void readAttributes(CopyItem item, CopyContext ctx)
    {
        try
        {
            ComposedType targetType = item.getTargetType();
            CaseInsensitiveMap caseInsensitiveMap = new CaseInsensitiveMap();
            Item src = item.getOriginal();
            Map<String, Object> values = src.getAllAttributes(null, (Item.AttributeFilter)new Object(this, ctx, src, targetType, (Map)caseInsensitiveMap));
            for(Iterator<Map.Entry<String, Object>> it = values.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String, Object> e = it.next();
                if(e.getValue() == null || (e.getValue() instanceof Collection && ((Collection)e.getValue()).isEmpty()) || (e
                                .getValue() instanceof Map && ((Map)e.getValue()).isEmpty()))
                {
                    continue;
                }
                item.addAttribute((AttributeDescriptor)caseInsensitiveMap.get(e.getKey()), e.getValue());
            }
            ComposedType ct = (targetType != null) ? targetType : item.getOriginal().getComposedType();
            for(Map.Entry<String, Object> e : (Iterable<Map.Entry<String, Object>>)ctx.getPresets(src).entrySet())
            {
                item.addPresetAttribute(ct.getAttributeDescriptorIncludingPrivate(e.getKey()), e.getValue());
            }
        }
        catch(JaloInvalidParameterException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloSecurityException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected CopyItem prepareCopy(ComposedType targetType, Item original, CopyContext ctx)
    {
        if(original == null)
        {
            throw new NullPointerException("original was null");
        }
        if(ctx == null)
        {
            throw new NullPointerException("ctx was null");
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
        else if(originalValue instanceof Item)
        {
            Item i = (Item)originalValue;
            prepareCopy(ctx.getTargetType(i), i, ctx);
        }
    }


    protected boolean process(CopyItem item, CopyContext ctx) throws JaloBusinessException
    {
        boolean modified = false;
        if(item.getCopy() == null)
        {
            Collection<CopyAttribute> attributes = translatePendingAttributes(item, ctx);
            if(attributes != null)
            {
                try
                {
                    item.setCopy(((item.getTargetType() != null) ? item.getTargetType() : item.getOriginal().getComposedType())
                                    .newInstance(null, toValueMap(attributes)));
                    consume(attributes);
                    modified = true;
                }
                catch(JaloGenericCreationException e)
                {
                    Throwable t = e.getCause();
                    if(t instanceof RuntimeException)
                    {
                        throw (RuntimeException)t;
                    }
                    if(t instanceof JaloBusinessException)
                    {
                        throw (JaloBusinessException)t;
                    }
                    if(t != null)
                    {
                        throw new JaloSystemException(t);
                    }
                    throw new JaloSystemException(e);
                }
                catch(JaloAbstractTypeException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
        else
        {
            Collection<CopyAttribute> attributes = translatePendingAttributes(item, ctx);
            if(attributes != null && !attributes.isEmpty())
            {
                try
                {
                    item.getCopy().setAllAttributes(null, toValueMap(attributes));
                    consume(attributes);
                    modified = true;
                }
                catch(JaloBusinessException e)
                {
                    throw e;
                }
            }
        }
        return modified;
    }


    protected void consume(Collection<CopyAttribute> readyAttributes)
    {
        for(CopyAttribute ca : readyAttributes)
        {
            ca.setConsumed(true);
        }
    }


    protected Map<String, Object> toValueMap(Collection<CopyAttribute> readyAttributes)
    {
        if(readyAttributes == null)
        {
            return null;
        }
        if(readyAttributes.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Item.ItemAttributeMap<String, Object> itemAttributeMap = new Item.ItemAttributeMap();
        for(CopyAttribute ca : readyAttributes)
        {
            itemAttributeMap.put(ca.getQualifier(), ca.getTranslatedValue());
        }
        return (Map<String, Object>)itemAttributeMap;
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
            Collection<Object> ret = (coll instanceof java.util.Set) ? new LinkedHashSet(coll.size()) : new ArrayList(coll.size());
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
        if(originalValue instanceof Item)
        {
            Item i = (Item)originalValue;
            if(ctx.mustBeTranslated(i))
            {
                Item copy = ctx.getCopy(i);
                if(copy == null)
                {
                    throw new CannotTranslateException(attr);
                }
                return copy;
            }
            return i;
        }
        return originalValue;
    }
}
