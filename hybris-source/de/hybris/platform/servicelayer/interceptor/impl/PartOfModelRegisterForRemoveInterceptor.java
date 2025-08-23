package de.hybris.platform.servicelayer.interceptor.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.config.PropertyActionReader;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class PartOfModelRegisterForRemoveInterceptor implements RemoveInterceptor
{
    private static final Logger LOG = Logger.getLogger(PartOfModelRegisterForRemoveInterceptor.class.getName());
    private TypeService typeService;
    private SessionService sessionService;
    private ConfigurationService configurationService;
    private PropertyActionReader propertyActionReader;
    private Set<String> ignoreAttributes = Collections.emptySet();


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    @Required
    public void setPropertyActionReader(PropertyActionReader propertyActionReader)
    {
        this.propertyActionReader = propertyActionReader;
    }


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ItemModel)
        {
            ItemModel itemModel = (ItemModel)model;
            String type = ctx.getModelService().getModelType(model);
            for(String partOfQualifier : excludeIgnoredAndDisabledFromPartOfAttributes(getPartOfAttributesAvailableInServiceLayer(ctx, type), this.ignoreAttributes, type))
            {
                try
                {
                    if(!ctx.getModelService().isRemoved(model))
                    {
                        Object attrValue = ctx.getModelService().getAttributeValue(model, partOfQualifier);
                        extractAndRegisterForRemoval(ctx, itemModel, attrValue, partOfQualifier);
                    }
                }
                catch(YNoSuchEntityException e)
                {
                    LOG.warn(e.getMessage());
                }
                catch(JaloObjectNoLongerValidException e)
                {
                    LOG.warn(e.getMessage());
                }
                catch(AttributeNotSupportedException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(e);
                    }
                }
            }
            setPartOfRemovedSessionMarker(itemModel);
        }
    }


    protected void setPartOfRemovedSessionMarker(ItemModel model)
    {
        if(this.configurationService.getConfiguration().getBoolean("relations.partof.suppressRemoveOnJalo", true))
        {
            this.sessionService.setAttribute("partOfRemoved." + model.getPk(), Boolean.valueOf(true));
        }
    }


    protected void registerForRemoval(InterceptorContext ctx, ItemModel parentModel, ItemModel subModel, String partOfAttrQualifier)
    {
        if(!ctx.getModelService().isRemoved(subModel))
        {
            ctx.registerElementFor(subModel, PersistenceOperation.DELETE);
        }
    }


    protected void extractAndRegisterForRemoval(InterceptorContext ctx, ItemModel parentModel, Object attrValue, String partOfAttrQualifier)
    {
        if(attrValue instanceof ItemModel)
        {
            if(isAttributeInherited(attrValue))
            {
                return;
            }
            registerForRemoval(ctx, parentModel, (ItemModel)attrValue, partOfAttrQualifier);
        }
        else if(attrValue instanceof java.util.Collection)
        {
            for(Object element : attrValue)
            {
                extractAndRegisterForRemoval(ctx, parentModel, element, partOfAttrQualifier);
            }
        }
        else if(attrValue instanceof Map)
        {
            for(Map.Entry e : ((Map)attrValue).entrySet())
            {
                extractAndRegisterForRemoval(ctx, parentModel, e.getValue(), partOfAttrQualifier);
            }
        }
    }


    private boolean isAttributeInherited(Object attrValue)
    {
        return (attrValue instanceof AttributeDescriptorModel && (((AttributeDescriptorModel)attrValue)
                        .getModifiers().intValue() & 0x400) != 0);
    }


    private Set<String> getPartOfAttributesAvailableInServiceLayer(InterceptorContext ctx, String itemType)
    {
        ModelService modelService = ctx.getModelService();
        if(modelService instanceof DefaultModelService)
        {
            ModelConverter modelConverterBySourceType = ((DefaultModelService)modelService).getConverterRegistry().getModelConverterBySourceType(itemType);
            if(modelConverterBySourceType instanceof ItemModelConverter)
            {
                return getFilteredPartOfAttributes((ItemModelConverter)modelConverterBySourceType);
            }
        }
        return this.typeService.getPartOfAttributes(itemType);
    }


    protected Set<String> getFilteredPartOfAttributes(ItemModelConverter itemModelConverter)
    {
        Set<ItemModelConverter.ModelAttributeInfo> availableAttributes = (Set<ItemModelConverter.ModelAttributeInfo>)itemModelConverter.getAllModelAttributes().stream().filter(atr -> atr.getAttributeInfo().isPartOf()).collect(
                        Collectors.toSet());
        return (Set<String>)availableAttributes.stream()
                        .map(ItemModelConverter.ModelAttributeInfo::getQualifier).collect(Collectors.toSet());
    }


    protected Set<String> excludeIgnoredAndDisabledFromPartOfAttributes(Set<String> partOfSet, Set<String> ignoreSet, String itemType)
    {
        return (Set<String>)excludeIgnoredFromPartOfAttributes(partOfSet, ignoreSet).stream()
                        .filter(s -> !this.propertyActionReader.isActionDisabledForType(s + ".partof.removal", itemType))
                        .collect(Collectors.toSet());
    }


    protected Set<String> excludeIgnoredFromPartOfAttributes(Set<String> partOfSet, Set<String> ignoreSet)
    {
        if(CollectionUtils.isNotEmpty(ignoreSet) && CollectionUtils.isNotEmpty(partOfSet))
        {
            partOfSet = Sets.newHashSet(partOfSet);
            partOfSet.removeAll(ignoreSet);
        }
        return partOfSet;
    }


    public void setIgnoreAttributes(Set<String> ignoreAttributes)
    {
        this.ignoreAttributes = ignoreAttributes;
    }
}
