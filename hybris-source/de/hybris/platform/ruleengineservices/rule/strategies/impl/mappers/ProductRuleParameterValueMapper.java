package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ProductRuleParameterValueMapper implements RuleParameterValueMapper<ProductModel>
{
    private ProductDao productDao;
    private CatalogVersionService catalogVersionService;
    private RuleParameterValueMapper<CatalogModel> catalogRuleParameterValueMapper;
    private String delimiter;
    private String catalogVersionName;


    public String toString(ProductModel product)
    {
        ServicesUtil.validateParameterNotNull(product, "Object cannot be null");
        return String.join(getDelimiter(), new CharSequence[] {product.getCode(),
                        getCatalogRuleParameterValueMapper().toString(product.getCatalogVersion().getCatalog())});
    }


    public ProductModel fromString(String value)
    {
        List<ProductModel> products;
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        String productCode = value;
        if(value.contains(getDelimiter()))
        {
            String catalogIdentifier = StringUtils.substringAfter(value, getDelimiter());
            CatalogModel catalog = (CatalogModel)getCatalogRuleParameterValueMapper().fromString(catalogIdentifier);
            CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(catalog.getId(), getCatalogVersionName());
            productCode = StringUtils.substringBefore(value, getDelimiter());
            products = getProductDao().findProductsByCode(catalogVersion, productCode);
        }
        else
        {
            products = getProductDao().findProductsByCode(value);
        }
        if(CollectionUtils.isEmpty(products))
        {
            throw new RuleParameterValueMapperException("Cannot find product with the code: " + productCode);
        }
        return products.iterator().next();
    }


    protected ProductDao getProductDao()
    {
        return this.productDao;
    }


    @Required
    public void setProductDao(ProductDao productDao)
    {
        this.productDao = productDao;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected RuleParameterValueMapper<CatalogModel> getCatalogRuleParameterValueMapper()
    {
        return this.catalogRuleParameterValueMapper;
    }


    @Required
    public void setCatalogRuleParameterValueMapper(RuleParameterValueMapper<CatalogModel> catalogRuleParameterValueMapper)
    {
        this.catalogRuleParameterValueMapper = catalogRuleParameterValueMapper;
    }


    protected String getDelimiter()
    {
        return this.delimiter;
    }


    @Required
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }


    protected String getCatalogVersionName()
    {
        return this.catalogVersionName;
    }


    @Required
    public void setCatalogVersionName(String catalogVersionName)
    {
        this.catalogVersionName = catalogVersionName;
    }
}
