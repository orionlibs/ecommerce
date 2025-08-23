package de.hybris.platform.cms2.version.predicate;

import de.hybris.platform.cms2.version.converter.attribute.data.VersionAttributeDescriptor;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class NonCMSItemTypePredicate implements Predicate<VersionAttributeDescriptor>
{
    private TypeService typeService;


    public boolean test(VersionAttributeDescriptor attributeDescriptor)
    {
        TypeModel attributeType = attributeDescriptor.getType();
        Predicate<String> isAssignableFromItem = attributeTypeCode -> getTypeService().isAssignableFrom("Item", attributeTypeCode);
        Predicate<String> isAssignableFromCMSItem = attributeTypeCode -> getTypeService().isAssignableFrom("CMSItem", attributeTypeCode);
        return (isAssignableFromItem.test(attributeType.getCode()) && isAssignableFromCMSItem.negate().test(attributeType.getCode()));
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
