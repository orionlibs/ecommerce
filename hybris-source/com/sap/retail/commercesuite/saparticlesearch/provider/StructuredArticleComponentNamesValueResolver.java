/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.commercesuite.saparticlesearch.provider;

import com.sap.retail.commercesuite.saparticlemodel.model.ArticleComponentModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import org.apache.log4j.Logger;

/**
 * This ValueProvider provides the product names of the component articles.
 */
public class StructuredArticleComponentNamesValueResolver extends BaseStructuredArticleComponentValueResolver
{
    private static final Logger LOGGER = Logger.getLogger(StructuredArticleComponentNamesValueResolver.class);


    @Override
    protected Object getComponentPropertyValue(final ArticleComponentModel articleComponent, final LanguageModel language)
    {
        if(language != null)
        {
            return articleComponent.getComponent().getName(getCommonI18NService().getLocaleForLanguage(language));
        }
        else
        {
            return articleComponent.getComponent().getName();
        }
    }


    @Override
    protected Logger getLogger()
    {
        return LOGGER;
    }
}
