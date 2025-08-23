/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.search;

import com.hybris.backoffice.widgets.fulltextsearch.DefaultFullTextSearchStrategy;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeFullTextSearchStrategy extends DefaultFullTextSearchStrategy
{
    private CommonI18NService commonI18NService;


    @Override
    public Collection<String> getAvailableLanguages(final String typeCode)
    {
        return getCommonI18NService().getAllLanguages().stream().map(LanguageModel::getIsocode).collect(Collectors.toList());
    }


    protected CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }


    @Required
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
