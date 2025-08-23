package de.hybris.platform.externaltax.impl;

import de.hybris.platform.basecommerce.model.externaltax.ProductTaxCodeModel;
import de.hybris.platform.externaltax.dao.ProductTaxCodeDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultProductTaxCodeDao extends AbstractItemDao implements ProductTaxCodeDao
{
    private static final Logger LOG = Logger.getLogger(DefaultProductTaxCodeDao.class.getName());
    private static final List<Class> SINGLE_CODE_SIG = Arrays.asList(new Class[] {String.class});
    private static final List<Class> BULK_CODE_SIG = Arrays.asList(new Class[] {String.class, String.class});


    public String getTaxCodeForProductAndAreaDirect(String productCode, String taxArea)
    {
        FlexibleSearchQuery query = createUniqueLookupQuery(productCode, taxArea, "{taxCode}");
        query.setResultClassList(SINGLE_CODE_SIG);
        SearchResult<String> result = search(query);
        if(result.getCount() == 0)
        {
            return null;
        }
        if(result.getCount() > 1)
        {
            LOG.warn("Multiple tax codes found for product " + productCode + " and area " + taxArea + " : " + result.getResult() + "! (Choosing first one)");
        }
        return result.getResult().get(0);
    }


    protected FlexibleSearchQuery createUniqueLookupQuery(String productCode, String taxArea, String selectClause)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT " + selectClause + " FROM {ProductTaxCode} WHERE {productCode}=?product AND {taxArea}=?area");
        query.addQueryParameter("product", productCode);
        query.addQueryParameter("area", taxArea);
        return query;
    }


    public List<List<String>> getTaxCodesForProductsAndAreaDirect(Collection<String> productCodes, String taxArea)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {productCode}, {taxCode} FROM {ProductTaxCode} WHERE {productCode} IN (?products) AND {taxArea}=?area");
        query.addQueryParameter("products", productCodes);
        query.addQueryParameter("area", taxArea);
        query.setResultClassList(BULK_CODE_SIG);
        return search(query).getResult();
    }


    public ProductTaxCodeModel getTaxCodeForProductAndArea(String productCode, String taxArea)
    {
        SearchResult<ProductTaxCodeModel> result = search(
                        createUniqueLookupQuery(productCode, taxArea, "{pk}"));
        if(result.getCount() == 0)
        {
            return null;
        }
        if(result.getCount() > 1)
        {
            LOG.warn("Multiple tax codes found for product " + productCode + " and area " + taxArea + " : " + result.getResult() + "! (Choosing first one)");
        }
        return result.getResult().get(0);
    }


    public List<ProductTaxCodeModel> getTaxCodesForProduct(String productCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {ProductTaxCode} WHERE {productCode}=?product ");
        query.addQueryParameter("product", productCode);
        SearchResult<ProductTaxCodeModel> result = search(query);
        return result.getResult();
    }
}
