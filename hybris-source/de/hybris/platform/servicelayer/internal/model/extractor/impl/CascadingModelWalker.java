package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.platform.servicelayer.exceptions.ModelTypeNotSupportedException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.WrapperRegistry;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class CascadingModelWalker
{
    private static final Logger LOG = Logger.getLogger(CascadingModelWalker.class.getName());
    private final DefaultModelService modelService;
    private final ModelWalkerCallback callback;


    public CascadingModelWalker(DefaultModelService modelService, ModelWalkerCallback callback)
    {
        this.modelService = modelService;
        this.callback = callback;
    }


    public void walkThrough(ModelWrapper toProcess, WrapperRegistry wrapperRegistry)
    {
        Collection<ModelWrapper> toCheck = Collections.singletonList(toProcess);
        do
        {
            Collection<ModelWrapper> addedModels = new LinkedHashSet<>();
            for(ModelWrapper wr : toCheck)
            {
                if(wr.getConverter() instanceof ItemModelConverter)
                {
                    ItemModelConverter conv = (ItemModelConverter)wr.getConverter();
                    ModelValueHistory h = ((ItemModelContextImpl)ModelContextUtils.getItemModelContext((AbstractItemModel)wr.getModel())).getValueHistory();
                    processUnlocalizedAttributes(wrapperRegistry, addedModels, wr, conv, h);
                    processLocalizedAttributes(wrapperRegistry, addedModels, wr, conv, h);
                }
            }
            toCheck = addedModels;
        }
        while(!toCheck.isEmpty());
    }


    private void processLocalizedAttributes(WrapperRegistry wrapperRegistry, Collection<ModelWrapper> addedModels, ModelWrapper parentWrapper, ItemModelConverter conv, ModelValueHistory h)
    {
        for(Map.Entry<Locale, Set<String>> e : (Iterable<Map.Entry<Locale, Set<String>>>)h.getDirtyLocalizedAttributes().entrySet())
        {
            for(String dirtyLocAttr : e.getValue())
            {
                ItemModelConverter.ModelAttributeInfo info = conv.getInfo(dirtyLocAttr);
                if(info == null)
                {
                    LOG.error("Model " + parentWrapper.getModel() + " has got unknown localized dirty attribute " + dirtyLocAttr + ". Most likely the code base had been changed without system update ! (ignoring attribute)");
                    continue;
                }
                if(info.getAttributeInfo().isReference())
                {
                    Set<Object> required = extractNewModels(conv
                                    .getDirtyLocalizedAttributeValue(parentWrapper.getModel(), dirtyLocAttr, e.getKey()), this.modelService);
                    if(!required.isEmpty())
                    {
                        processChildren(required, dirtyLocAttr, parentWrapper, wrapperRegistry, addedModels);
                    }
                }
            }
        }
    }


    private void processUnlocalizedAttributes(WrapperRegistry wrapperRegistry, Collection<ModelWrapper> addedModels, ModelWrapper parentWrapper, ItemModelConverter conv, ModelValueHistory h)
    {
        for(String dirtyUnlocAttr : h.getDirtyAttributes())
        {
            ItemModelConverter.ModelAttributeInfo info = conv.getInfo(dirtyUnlocAttr);
            if(info == null)
            {
                LOG.error("Model " + parentWrapper.getModel() + " has got unknown unlocalized dirty attribute " + dirtyUnlocAttr + ". Most likely the code base had been changed without system update ! (ignoring attribute)");
                continue;
            }
            if(info.getAttributeInfo().isReference())
            {
                Set<Object> required = extractNewModels(conv
                                .getDirtyAttributeValue(parentWrapper.getModel(), dirtyUnlocAttr), this.modelService);
                if(!required.isEmpty())
                {
                    processChildren(required, dirtyUnlocAttr, parentWrapper, wrapperRegistry, addedModels);
                }
            }
        }
    }


    private void processChildren(Set<Object> required, String attribute, ModelWrapper parentWrapper, WrapperRegistry wrapperRegistry, Collection<ModelWrapper> addedModels)
    {
        Collection<ModelWrapper> dependencies = new ArrayList<>(required.size());
        for(Object req : required)
        {
            ModelWrapper reqWr;
            if(!wrapperRegistry.containsWrapperFor(req, PersistenceOperation.SAVE))
            {
                reqWr = wrapperRegistry.createWrapper(req, PersistenceOperation.SAVE);
                if(this.callback.foundNewWrapper(reqWr))
                {
                    addedModels.add(reqWr);
                }
            }
            else
            {
                reqWr = wrapperRegistry.getWrapperFor(req, PersistenceOperation.SAVE);
            }
            dependencies.add(reqWr);
        }
        this.callback.foundNewDependencies(parentWrapper, attribute, dependencies);
    }


    private Set<Object> extractNewModels(Object value, DefaultModelService modelService)
    {
        Set<Object> ret = Collections.EMPTY_SET;
        if(value != null)
        {
            if(value instanceof Collection)
            {
                Collection coll = (Collection)value;
                if(!coll.isEmpty())
                {
                    ret = new LinkedHashSet(coll.size());
                    for(Object o : coll)
                    {
                        ret.addAll(extractNewModels(o, modelService));
                    }
                    return ret;
                }
            }
            else if(value instanceof Map)
            {
                Map<Object, Object> m = (Map<Object, Object>)value;
                if(!m.isEmpty())
                {
                    ret = new LinkedHashSet(m.size());
                    for(Map.Entry<Object, Object> e : m.entrySet())
                    {
                        ret.addAll(extractNewModels(e.getKey(), modelService));
                        ret.addAll(extractNewModels(e.getValue(), modelService));
                    }
                    return ret;
                }
            }
            else
            {
                try
                {
                    if(modelService.getConverterRegistry().getModelConverterByModel(value).isNew(value))
                    {
                        ret = Collections.singleton(value);
                    }
                }
                catch(ModelTypeNotSupportedException modelTypeNotSupportedException)
                {
                }
            }
        }
        return ret;
    }
}
