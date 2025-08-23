package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class OrphanedPartOfCleanupPrepareInterceptor implements PrepareInterceptor
{
    @Resource
    private TypeService typeService;


    public void onPrepare(Object object, InterceptorContext ctx) throws InterceptorException
    {
        if(isModifiedItemUsingSLD(object, ctx))
        {
            AbstractItemModel model = (AbstractItemModel)object;
            String type = model.getItemModelContext().getItemType();
            for(String partOfAttributeQualifier : getWritablePartOfAttributes(ctx, type))
            {
                try
                {
                    Map<String, Set<Locale>> dirtyAttributes = ctx.getDirtyAttributes(model);
                    if(dirtyAttributes.containsKey(partOfAttributeQualifier))
                    {
                        Set<Locale> locales = dirtyAttributes.get(partOfAttributeQualifier);
                        if(locales == null)
                        {
                            Object currentValue = getAttributeValue(ctx, model, partOfAttributeQualifier);
                            Object originalValue = getOriginalValue(model, partOfAttributeQualifier);
                            registerForRemovalIfNecessary(ctx, currentValue, originalValue);
                            continue;
                        }
                        for(Locale loc : locales)
                        {
                            Object currentValue = getAttributeValue(ctx, model, partOfAttributeQualifier, loc);
                            Object originalValue = getOriginalValue(model, partOfAttributeQualifier, loc);
                            registerForRemovalIfNecessary(ctx, currentValue, originalValue);
                        }
                    }
                }
                catch(AttributeNotSupportedException e)
                {
                }
            }
        }
    }


    protected Set<String> getWritablePartOfAttributes(InterceptorContext ctx, String itemType)
    {
        return ((DefaultModelService)ctx.getModelService()).getConverterRegistry().getModelConverterBySourceType(itemType)
                        .getWritablePartOfAttributes(this.typeService);
    }


    protected void registerForRemovalIfNecessary(InterceptorContext ctx, Object currentValue, Object originalValue)
    {
        if(originalValue != null && !originalValue.equals(currentValue))
        {
            registerForRemoval(collectItemsToRemove(originalValue, currentValue), ctx);
        }
    }


    protected boolean isModifiedItemUsingSLD(Object object, InterceptorContext ctx)
    {
        try
        {
            AbstractItemModel model = (AbstractItemModel)object;
            return (ctx.isModified(model) && !ctx.isNew(model) && !ctx.isRemoved(model) &&
                            !PersistenceUtils.isPersistenceLegacyModeEnabled());
        }
        catch(ClassCastException e)
        {
            return false;
        }
    }


    protected void registerForRemoval(Collection items, InterceptorContext ctx)
    {
        for(Object item : items)
        {
            ctx.registerElementFor(item, PersistenceOperation.DELETE);
        }
    }


    protected Collection collectItemsToRemove(Object originalValue, Object currentValue)
    {
        if(originalValue instanceof Collection)
        {
            Collection<?> originalCollection = (Collection)originalValue;
            if(CollectionUtils.isNotEmpty(originalCollection))
            {
                Collection<?> currentCollection = (Collection)currentValue;
                if(currentCollection == null)
                {
                    currentCollection = Collections.EMPTY_LIST;
                }
                Set distinctElementsToKeep = new HashSet(currentCollection);
                Set distinctOriginalElements = new HashSet(originalCollection);
                Collection distinctElementsToRemove = CollectionUtils.subtract(distinctOriginalElements, distinctElementsToKeep);
                return distinctElementsToRemove;
            }
            return Collections.EMPTY_LIST;
        }
        if(originalValue instanceof AbstractItemModel)
        {
            return Collections.singletonList(originalValue);
        }
        throw new IllegalArgumentException("Not supported original value " + originalValue);
    }


    protected Object getOriginalValue(AbstractItemModel model, String partOfQualifier)
    {
        return model.getItemModelContext().getOriginalValue(partOfQualifier);
    }


    protected Object getOriginalValue(AbstractItemModel model, String partOfQualifier, Locale loc)
    {
        return model.getItemModelContext().getOriginalValue(partOfQualifier, loc);
    }


    protected Object getAttributeValue(InterceptorContext ctx, AbstractItemModel model, String qualifier)
    {
        return ctx.getModelService().getAttributeValue(model, qualifier);
    }


    protected Object getAttributeValue(InterceptorContext ctx, AbstractItemModel model, String qualifier, Locale loc)
    {
        return ctx.getModelService().getAttributeValue(model, qualifier, loc);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
