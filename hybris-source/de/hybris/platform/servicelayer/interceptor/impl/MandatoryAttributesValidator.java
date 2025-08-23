package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class MandatoryAttributesValidator extends LocalizedMessageAwareValidator implements ValidateInterceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(MandatoryAttributesValidator.class);
    private static final String MESSAGE_KEY = "missing";
    private TypeService typeService;
    private ModelService modelService;


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(!(model instanceof AbstractItemModel))
        {
            return;
        }
        String type = this.modelService.getModelType(model);
        Set<String> mandatoryAttributes = this.typeService.getMandatoryAttributes(type, ctx.isNew(model));
        Set<String> missingAttributes = null;
        for(String attribute : mandatoryAttributes)
        {
            if(shouldSkipCatalogPropertyValidation(type, mandatoryAttributes, attribute))
            {
                continue;
            }
            if(!ctx.isModified(model, attribute))
            {
                continue;
            }
            try
            {
                if(isValueMissing(getAttributeValue((AbstractItemModel)model, attribute)))
                {
                    if(missingAttributes == null)
                    {
                        missingAttributes = new LinkedHashSet<>(mandatoryAttributes.size());
                    }
                    missingAttributes.add(attribute);
                }
            }
            catch(AttributeNotSupportedException e)
            {
                LOG.debug("attribute {} is not supported by model type {} - skipping mandatory check", attribute, model
                                .getClass().getName());
            }
        }
        if(CollectionUtils.isNotEmpty(missingAttributes))
        {
            throw new MissingMandatoryAttributesException(getLocalizedMessage("missing", new Object[] {missingAttributes, model, type}), (AbstractItemModel)model, missingAttributes, this);
        }
    }


    private boolean shouldSkipCatalogPropertyValidation(String type, Set<String> mandatoryAttributes, String attribute)
    {
        return ("catalog".equals(attribute) && (!isCatalogVersionMandatory(type, mandatoryAttributes) || !isCatalogItemType(type)) && !"CatalogVersion".equals(type));
    }


    private boolean isCatalogItemType(String type)
    {
        ComposedTypeModel compType = this.typeService.getComposedTypeForCode(type);
        return (compType != null && Boolean.TRUE.equals(compType.getCatalogItemType()));
    }


    private boolean isCatalogVersionMandatory(String type, Set<String> mandatoryAttributes)
    {
        ComposedTypeModel compType = this.typeService.getComposedTypeForCode(type);
        AttributeDescriptorModel attDescriptor = compType.getCatalogVersionAttribute();
        return (attDescriptor != null && mandatoryAttributes.contains(attDescriptor.getQualifier()));
    }


    private Object getAttributeValue(AbstractItemModel model, String attribute) throws AttributeNotSupportedException
    {
        Object value = null;
        try
        {
            value = this.modelService.getAttributeValue(model, attribute);
        }
        catch(AttributeNotSupportedException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            LOG.error("could not read mandatory attribute {} due to {}", new Object[] {attribute, e.getMessage(), e});
        }
        return value;
    }


    private boolean isValueMissing(Object value)
    {
        boolean result = false;
        if(value == null)
        {
            result = true;
        }
        else if(value instanceof AbstractItemModel && this.modelService.isRemoved(value))
        {
            result = true;
        }
        else if(value instanceof java.util.Collection)
        {
            for(Object el : value)
            {
                result = isValueMissing(el);
                if(result)
                {
                    return result;
                }
            }
        }
        return result;
    }
}
