/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.commercesuite.saparticleb2caddon.commerceservices;

import com.sap.retail.commercesuite.saparticlemodel.util.ArticleCommonUtils;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.product.impl.DefaultCommerceProductService;
import org.apache.log4j.Logger;

/**
 * Adds SAP merchandise category classification catalog handling to {@link DefaultCommerceProductService}.
 */
public class SapAdmCommerceProductService extends DefaultCommerceProductService
{
    private static final Logger LOGGER = Logger.getLogger(SapAdmCommerceProductService.class);
    private String merchandiseCatalogId = null;
    private ArticleCommonUtils articleCommonUtils;


    /**
     * Injection setter for merchandise catalog ID.
     *
     * @param merchandiseCatalogId
     *           merchandise catalog ID
     */
    public void setMerchandiseCatalogId(final String merchandiseCatalogId)
    {
        this.merchandiseCatalogId = merchandiseCatalogId;
    }


    @Override
    protected boolean toBeConverted(final CategoryModel categoryModel, final String catalogId)
    {
        if(getArticleCommonUtils().isCAREnabled() || getArticleCommonUtils().isCOSEnabled())
        {
            // Adds the merchandise category classification catalog to allowed super categories.
            if(merchandiseCatalogId != null && merchandiseCatalogId.equals(categoryModel.getCatalogVersion().getCatalog().getId())
                            && !(categoryModel instanceof ClassificationClassModel))
            {
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("toBeConverted: Convert super category " + categoryModel.getCode()
                                    + " since it belongs to merchandise catalog " + merchandiseCatalogId);
                }
                return true;
            }
        }
        return super.toBeConverted(categoryModel, catalogId);
    }


    public ArticleCommonUtils getArticleCommonUtils()
    {
        return articleCommonUtils;
    }


    public void setArticleCommonUtils(ArticleCommonUtils articleCommonUtils)
    {
        this.articleCommonUtils = articleCommonUtils;
    }
}

