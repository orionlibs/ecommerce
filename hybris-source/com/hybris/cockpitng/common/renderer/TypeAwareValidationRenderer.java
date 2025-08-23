/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.renderer;

import com.hybris.cockpitng.components.validation.DefaultValidationRenderer;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class TypeAwareValidationRenderer extends DefaultValidationRenderer
{
    private TypeFacade typeFacade;
    private LabelService labelService;
    private CockpitLocaleService cockpitLocaleService;


    @Override
    public String getLabel(final Object object, final String path)
    {
        String attributeDescription = super.getLabel(object, path);
        if(StringUtils.isEmpty(path))
        {
            attributeDescription = getLabelService().getObjectLabel(object);
        }
        else
        {
            final String locale = ObjectValuePath.getLocaleFromPath(path);
            final DataAttribute attribute = getTypeFacade().getAttribute(object, ObjectValuePath.getNotLocalizedPath(path));
            if(attribute != null)
            {
                attributeDescription = attribute.getLabel(getCockpitLocaleService().getCurrentLocale());
                if(!StringUtils.isEmpty(attributeDescription) && !StringUtils.isEmpty(locale))
                {
                    attributeDescription = attributeDescription.concat(String.format(" [%s]", locale));
                }
            }
        }
        return StringUtils.isEmpty(attributeDescription) ? path : attributeDescription;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
