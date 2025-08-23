/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.impl;

import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.impl.BackofficeCoreLabelService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.enumeration.EnumerationService;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;

/**
 * Default backoffice extension of the {@link BackofficeCoreLabelService} capable of computing labels for
 * {@link HybrisEnumValue}.
 */
public class BackofficeLabelService extends BackofficeCoreLabelService
{
    @Resource
    private EnumerationService enumerationService;
    @Resource
    private CockpitLocaleService cockpitLocaleService;


    @Override
    public String getObjectLabel(final Object object)
    {
        if(object instanceof HybrisEnumValue)
        {
            if(getPermissionFacade().canReadInstance(object))
            {
                final HybrisEnumValue enumValue = (HybrisEnumValue)object;
                final String result = enumerationService.getEnumerationName(enumValue, cockpitLocaleService.getCurrentLocale());
                if(StringUtils.isNotBlank(result))
                {
                    return result;
                }
            }
            else
            {
                return getNoReadAccessMessage(object);
            }
        }
        final String label = super.getObjectLabel(object);
        if(StringUtils.isNotBlank(label))
        {
            return label;
        }
        if(object instanceof ItemModel)
        {
            final String type = getType(object);
            final PK pk = ((ItemModel)object).getPk();
            return String.format("%s (%s)", type, pk == null ? Labels.getLabel("backoffice.data.is.unsaved") : pk.toString());
        }
        if(object == null)
        {
            return Labels.getLabel("backoffice.data.is.null");
        }
        return object.toString();
    }


    @Override
    protected String getNoReadAccessMessage(final Object object)
    {
        final String label = Labels.getLabel("backoffice.data.not.visible");
        return StringUtils.isNotBlank(label) ? label : super.getNoReadAccessMessage(object);
    }


    @Override
    protected String getDisabledMessage(final Object object)
    {
        final String label = Labels.getLabel("backoffice.data.language.disabled");
        return StringUtils.isNotBlank(label) ? label : super.getDisabledMessage(object);
    }
}
