package de.hybris.platform.solrfacetsearch.search.product;

import de.hybris.platform.solrfacetsearch.search.AbstractSolrConverter;
import de.hybris.platform.solrfacetsearch.search.impl.SolrResult;
import java.io.Serializable;
import java.util.Collection;

public class DefaultSolrProductConverter extends AbstractSolrConverter<SolrProductData> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final String CATALOG = "catalog";
    private static final String PK = "pk";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String CATALOG_VERSION = "catalogVersion";
    private static final String CATEGORIES = "category";
    private static final String PRICE = "priceValue";
    private static final String CODE = "code";
    private static final String EAN = "ean";


    public SolrProductData convert(SolrResult solrResult, SolrProductData target)
    {
        target.setCatalog((String)getValue(solrResult, "catalog"));
        target.setPk((Long)getValue(solrResult, "pk"));
        target.setName((String)getValue(solrResult, "name"));
        target.setDescription((String)getValue(solrResult, "description"));
        target.setCatalogVersion((String)getValue(solrResult, "catalogVersion"));
        target.setCategories((Collection)getValue(solrResult, "category"));
        target.setPrice((Double)getValue(solrResult, "priceValue"));
        target.setCode((String)getValue(solrResult, "code"));
        target.setEan((String)getValue(solrResult, "ean"));
        return target;
    }


    protected SolrProductData createDataObject()
    {
        return new SolrProductData();
    }
}
