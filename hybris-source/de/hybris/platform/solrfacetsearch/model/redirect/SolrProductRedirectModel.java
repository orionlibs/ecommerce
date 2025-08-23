package de.hybris.platform.solrfacetsearch.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrProductRedirectModel extends SolrAbstractKeywordRedirectModel
{
    public static final String _TYPECODE = "SolrProductRedirect";
    public static final String REDIRECTITEM = "redirectItem";


    public SolrProductRedirectModel()
    {
    }


    public SolrProductRedirectModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrProductRedirectModel(ProductModel _redirectItem)
    {
        setRedirectItem(_redirectItem);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrProductRedirectModel(ItemModel _owner, ProductModel _redirectItem)
    {
        setOwner(_owner);
        setRedirectItem(_redirectItem);
    }


    @Accessor(qualifier = "redirectItem", type = Accessor.Type.GETTER)
    public ProductModel getRedirectItem()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("redirectItem");
    }


    @Accessor(qualifier = "redirectItem", type = Accessor.Type.SETTER)
    public void setRedirectItem(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("redirectItem", value);
    }
}
