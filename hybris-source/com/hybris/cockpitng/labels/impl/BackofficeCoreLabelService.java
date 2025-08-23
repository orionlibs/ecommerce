/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Labels;
import com.hybris.cockpitng.labels.LabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.zkoss.spring.SpringUtil;

/**
 * Extension of {@link TypeAwareLabelService} that resolve {@link LabelProvider}. First it checks, if the bean is in the
 * cockpit module application context. If not, it falls back to {@link SpringUtil#getBean(String, Class)}.
 */
public class BackofficeCoreLabelService extends TypeAwareLabelService
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeCoreLabelService.class);


    @Override
    protected LabelProvider<Object> getLabelProvider(final Object object, final Labels labelConfig)
    {
        if(labelConfig.getBeanId() != null)
        {
            try
            {
                return getApplicationContext().getBean(labelConfig.getBeanId(), LabelProvider.class);
            }
            catch(final BeansException be)
            {
                final String errorMessage = String.format("Error creating object label provider for %s with Spring bean ID %s",
                                object.getClass().getSimpleName(), labelConfig.getBeanId());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(errorMessage, be);
                }
            }
        }
        return null;
    }
}
