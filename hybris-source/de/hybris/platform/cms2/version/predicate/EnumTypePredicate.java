package de.hybris.platform.cms2.version.predicate;

import de.hybris.platform.cms2.version.converter.attribute.data.VersionAttributeDescriptor;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class EnumTypePredicate implements Predicate<VersionAttributeDescriptor>
{
    private TypeService typeService;


    public boolean test(VersionAttributeDescriptor versionAttributeDescriptor)
    {
        TypeModel attributeType = versionAttributeDescriptor.getType();
        Predicate<String> isAssignableFrom = attributeTypeCode -> getTypeService().isAssignableFrom("EnumerationValue", attributeTypeCode);
        return isAssignableFrom.test(attributeType.getCode());
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
