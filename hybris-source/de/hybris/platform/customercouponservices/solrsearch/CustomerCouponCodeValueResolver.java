package de.hybris.platform.customercouponservices.solrsearch;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomerCouponCodeValueResolver extends AbstractValueResolver<ProductModel, Object, Object>
{
    private FieldNameProvider fieldNameProvider;
    private CustomerCouponService customerCouponService;
    private ProductDao productDao;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ProductModel product, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        List<PromotionSourceRuleModel> promotionSourceRuleList = getPromotionSourceRulesForProduct(product);
        if(product instanceof VariantProductModel)
        {
            promotionSourceRuleList.addAll(getPromotionSourceRulesForProduct(((VariantProductModel)product).getBaseProduct()));
        }
        Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
        for(String fieldName : fieldNames)
        {
            for(PromotionSourceRuleModel promotionSourceRule : promotionSourceRuleList)
            {
                document.addField(fieldName,
                                getCustomerCouponService().getCouponCodeForPromotionSourceRule(promotionSourceRule.getCode()));
            }
        }
    }


    protected List<PromotionSourceRuleModel> getPromotionSourceRulesForProduct(ProductModel product)
    {
        List<PromotionSourceRuleModel> promotionSourceRuleList = new ArrayList<>();
        promotionSourceRuleList.addAll(getCustomerCouponService().getPromotionSourceRulesForProduct(product.getCode()));
        promotionSourceRuleList.addAll(getCustomerCouponService().getPromotionSourcesRuleForProductCategories(product));
        promotionSourceRuleList.removeAll(getCustomerCouponService().getExclPromotionSourceRulesForProduct(product.getCode()));
        return promotionSourceRuleList;
    }


    protected ProductDao getProductDao()
    {
        return this.productDao;
    }


    public void setProductDao(ProductDao productDao)
    {
        this.productDao = productDao;
    }


    protected CustomerCouponService getCustomerCouponService()
    {
        return this.customerCouponService;
    }


    public void setCustomerCouponService(CustomerCouponService customerCouponService)
    {
        this.customerCouponService = customerCouponService;
    }


    protected FieldNameProvider getFieldNameProvider()
    {
        return this.fieldNameProvider;
    }


    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }
}
