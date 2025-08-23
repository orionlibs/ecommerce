package de.hybris.platform.cms2.common.functions.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public class PageStatusEnumValueFlexibleSearchConverter implements Converter<String, String>
{
    private TypeService typeService;


    public String convert(String source)
    {
        EnumerationValueModel enumValueModel = getTypeService().getEnumerationValue("CmsPageStatus", source);
        return enumValueModel.getPk().getLongValueAsString();
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
