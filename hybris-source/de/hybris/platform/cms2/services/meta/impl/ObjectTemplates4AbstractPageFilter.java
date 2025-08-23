package de.hybris.platform.cms2.services.meta.impl;

import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.media.impl.AbstractObjectTypeFilter;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class ObjectTemplates4AbstractPageFilter extends AbstractObjectTypeFilter<ObjectTemplate, AbstractPageModel>
{
    private TypeService typeService;
    private CMSAdminRestrictionService cmsAdminRestrictionService;


    public boolean isValidType(ObjectTemplate template, AbstractPageModel page)
    {
        ServicesUtil.validateParameterNotNull(template, "Type cannot be null");
        ServicesUtil.validateParameterNotNull(page, "contentSlotName cannot be null");
        BaseType baseType = template.getBaseType();
        String code = baseType.getCode();
        ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(code);
        if(composedType instanceof RestrictionTypeModel)
        {
            RestrictionTypeModel restrictionTypeForTemplate = (RestrictionTypeModel)composedType;
            Collection<RestrictionTypeModel> restrictionTypesForPage = this.cmsAdminRestrictionService.getAllowedRestrictionTypesForPage(page);
            return restrictionTypesForPage.contains(restrictionTypeForTemplate);
        }
        throw new IllegalArgumentException("ObjectTemplate is not an instance of RestrictionTypeModel");
    }


    public CMSAdminRestrictionService getCmsAdminRestrictionService()
    {
        return this.cmsAdminRestrictionService;
    }


    @Required
    public void setCmsAdminRestrictionService(CMSAdminRestrictionService cmsAdminRestrictionService)
    {
        this.cmsAdminRestrictionService = cmsAdminRestrictionService;
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
