package de.hybris.platform.externaltax.impl;

import de.hybris.platform.basecommerce.model.externaltax.ProductTaxCodeModel;
import de.hybris.platform.externaltax.ProductTaxCodeService;
import de.hybris.platform.externaltax.dao.ProductTaxCodeDao;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultProductTaxCodeService implements ProductTaxCodeService
{
    private ProductTaxCodeDao productTaxCodeDao;


    public String lookupTaxCode(String productCode, String taxArea)
    {
        return this.productTaxCodeDao.getTaxCodeForProductAndAreaDirect(productCode, taxArea);
    }


    public Map<String, String> lookupTaxCodes(Collection<String> productCodes, String taxArea)
    {
        if(CollectionUtils.isNotEmpty(productCodes))
        {
            List<List<String>> rows = this.productTaxCodeDao.getTaxCodesForProductsAndAreaDirect(productCodes, taxArea);
            if(CollectionUtils.isNotEmpty(rows))
            {
                Map<String, String> ret = new HashMap<>(rows.size() * 2);
                for(List<String> row : rows)
                {
                    String productCode = row.get(0);
                    String taxCode = row.get(1);
                    ret.put(productCode, taxCode);
                }
                return ret;
            }
        }
        return Collections.emptyMap();
    }


    public ProductTaxCodeModel getTaxCodeForProductAndArea(String productCode, String taxArea)
    {
        return this.productTaxCodeDao.getTaxCodeForProductAndArea(productCode, taxArea);
    }


    public Collection<ProductTaxCodeModel> getTaxCodesForProduct(String productCode)
    {
        return this.productTaxCodeDao.getTaxCodesForProduct(productCode);
    }


    @Required
    public void setProductTaxCodeDao(ProductTaxCodeDao productTaxCodeDao)
    {
        this.productTaxCodeDao = productTaxCodeDao;
    }
}
