package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsItemVisitor implements ItemVisitor<ItemModel>
{
    private TypeService typeService;


    public List<ItemModel> visit(ItemModel source, List<ItemModel> path, Map<String, Object> context)
    {
        List<ItemModel> items = new ArrayList<>();
        ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(source.getItemtype());
        Set<AttributeDescriptorModel> attributeDescriptors = this.typeService.getAttributeDescriptorsForType(composedType);
        for(AttributeDescriptorModel attributeDescriptor : attributeDescriptors)
        {
            TypeModel attributeType = attributeDescriptor.getAttributeType();
            if(isValidType(attributeType))
            {
                Object value = source.getProperty(attributeDescriptor.getQualifier());
                if(value instanceof Collection)
                {
                    items.addAll((Collection<? extends ItemModel>)value);
                    continue;
                }
                if(value != null)
                {
                    items.add((ItemModel)value);
                }
            }
        }
        return items;
    }


    protected boolean isValidType(TypeModel type)
    {
        if(type instanceof ComposedTypeModel)
        {
            return isValidTypeCode(((ComposedTypeModel)type).getCode());
        }
        if(type instanceof CollectionTypeModel)
        {
            return isValidTypeCode(((CollectionTypeModel)type).getElementType().getCode());
        }
        return false;
    }


    protected boolean isValidTypeCode(String typeCode)
    {
        return (this.typeService.isAssignableFrom("AbstractAsSearchProfile", typeCode) || this.typeService
                        .isAssignableFrom("AbstractAsConfiguration", typeCode));
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
}
