package de.hybris.platform.solrfacetsearch.attribute;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrCategoryRedirectModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrProductRedirectModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrURIRedirectModel;

public class DefaultRedirectHmcLabelProvider implements DynamicAttributeHandler<String, SolrAbstractKeywordRedirectModel>
{
    public String get(SolrAbstractKeywordRedirectModel model)
    {
        if(model instanceof SolrURIRedirectModel)
        {
            return ((SolrURIRedirectModel)model).getUrl();
        }
        if(model instanceof SolrProductRedirectModel)
        {
            ProductModel redirectItem = ((SolrProductRedirectModel)model).getRedirectItem();
            return redirectItem.getName();
        }
        if(model instanceof SolrCategoryRedirectModel)
        {
            CategoryModel redirectItem = ((SolrCategoryRedirectModel)model).getRedirectItem();
            return redirectItem.getName();
        }
        return model.toString();
    }


    public void set(SolrAbstractKeywordRedirectModel model, String value)
    {
        throw new UnsupportedOperationException("The attribute is readonly");
    }
}
