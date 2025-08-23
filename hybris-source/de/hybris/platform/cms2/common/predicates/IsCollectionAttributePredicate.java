package de.hybris.platform.cms2.common.predicates;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.function.Predicate;

public class IsCollectionAttributePredicate implements Predicate<AttributeDescriptorModel>
{
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        TypeModel attributeType = attributeDescriptorModel.getLocalized().booleanValue() ? ((MapTypeModel)attributeDescriptorModel.getAttributeType()).getReturntype() : attributeDescriptorModel.getAttributeType();
        return isCollection(attributeType);
    }


    protected boolean isCollection(TypeModel type)
    {
        return type.getItemtype().contains("CollectionType");
    }
}
