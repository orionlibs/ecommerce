/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.labels.LabelService;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.spring.SpringUtil;

public class MessageActionRenderer extends DefaultActionRenderer
{
    private LabelService labelService;


    @Override
    protected String getLocalizedName(final ActionContext context)
    {
        String name = super.getLocalizedName(context);
        if(StringUtils.equals(name, context.getName()))
        {
            name = getLabelService().getShortObjectLabel(context.getData());
        }
        return name;
    }


    public LabelService getLabelService()
    {
        if(labelService == null)
        {
            labelService = (LabelService)SpringUtil.getBean("labelService", LabelService.class);
        }
        return labelService;
    }
}
