package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsUidGenerator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsModelCloningContext implements ModelCloningContext
{
    private TypeService typeService;
    private AsUidGenerator asUidGenerator;


    public boolean skipAttribute(Object model, String qualifier)
    {
        return false;
    }


    public boolean treatAsPartOf(Object model, String qualifier)
    {
        if(!(model instanceof de.hybris.platform.core.model.ItemModel))
        {
            return false;
        }
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(model.getClass());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(composedType, qualifier);
        return (BooleanUtils.isTrue(attributeDescriptor.getPartOf()) && BooleanUtils.isTrue(attributeDescriptor.getWritable()));
    }


    public boolean usePresetValue(Object model, String qualifier)
    {
        return (model instanceof de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel && StringUtils.equals("uid", qualifier));
    }


    public Object getPresetValue(Object model, String qualifier)
    {
        return this.asUidGenerator.generateUid();
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public AsUidGenerator getAsUidGenerator()
    {
        return this.asUidGenerator;
    }


    @Required
    public void setAsUidGenerator(AsUidGenerator asUidGenerator)
    {
        this.asUidGenerator = asUidGenerator;
    }
}
