package de.hybris.platform.cms2.version.predicate;

import de.hybris.platform.cms2.version.converter.attribute.data.VersionAttributeDescriptor;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class CMSItemTypePredicate implements Predicate<VersionAttributeDescriptor>
{
    private TypeService typeService;


    public boolean test(VersionAttributeDescriptor attributeDescriptor)
    {
        return getTypeService().isAssignableFrom("CMSItem", attributeDescriptor.getType().getCode());
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
