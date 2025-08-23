package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionMessageParameterResolutionStrategy;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultProductResolutionStrategy implements PromotionMessageParameterResolutionStrategy
{
    private ProductService productService;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultProductResolutionStrategy.class);


    public String getValue(RuleParameterData data, PromotionResultModel promotionResult, Locale locale)
    {
        String productCode = (String)data.getValue();
        ProductModel product = getProduct(productCode);
        if(product != null)
        {
            return getProductRepresentation(product);
        }
        return productCode;
    }


    protected ProductModel getProduct(String productCode)
    {
        if(productCode != null)
        {
            try
            {
                return getProductService().getProductForCode(productCode);
            }
            catch(UnknownIdentifierException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException e)
            {
                LOG.error("cannot resolve product code: " + productCode + " to a product.", (Throwable)e);
                return null;
            }
        }
        return null;
    }


    protected String getProductRepresentation(ProductModel product)
    {
        return product.getName();
    }


    protected ProductService getProductService()
    {
        return this.productService;
    }


    @Required
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }
}
