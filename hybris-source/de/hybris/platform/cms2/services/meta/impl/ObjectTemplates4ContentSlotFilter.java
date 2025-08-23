package de.hybris.platform.cms2.services.meta.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.media.impl.AbstractObjectTypeFilter;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class ObjectTemplates4ContentSlotFilter extends AbstractObjectTypeFilter<ObjectTemplate, ContentSlotNameModel>
{
    private TypeService typeService;


    public boolean isValidType(ObjectTemplate template, ContentSlotNameModel contentSlotName)
    {
        ServicesUtil.validateParameterNotNull(template, "Type cannot be null");
        ServicesUtil.validateParameterNotNull(contentSlotName, "contentSlotName cannot be null");
        TypeModel typeModel = this.typeService.getTypeForCode(template.getBaseType().getCode());
        return (isValidForComponentTypeGroup(contentSlotName, typeModel) ||
                        isValidForValidComponentTypes(contentSlotName, typeModel));
    }


    @Deprecated(since = "ages", forRemoval = true)
    @HybrisDeprecation(sinceVersion = "ages")
    protected boolean isValidForValidComponentTypes(ContentSlotNameModel contentSlotName, TypeModel typeModel)
    {
        if(contentSlotName.getValidComponentTypes() != null && !contentSlotName.getValidComponentTypes().isEmpty())
        {
            return contentSlotName.getValidComponentTypes().contains(typeModel);
        }
        return false;
    }


    protected boolean isValidForComponentTypeGroup(ContentSlotNameModel contentSlotName, TypeModel typeModel)
    {
        if(contentSlotName.getCompTypeGroup() != null && contentSlotName.getCompTypeGroup().getCmsComponentTypes() != null &&
                        !contentSlotName.getCompTypeGroup().getCmsComponentTypes().isEmpty())
        {
            return contentSlotName.getCompTypeGroup().getCmsComponentTypes().contains(typeModel);
        }
        return false;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
