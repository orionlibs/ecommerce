package de.hybris.platform.solrfacetsearch.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrCategoryRedirectModel extends SolrAbstractKeywordRedirectModel
{
    public static final String _TYPECODE = "SolrCategoryRedirect";
    public static final String REDIRECTITEM = "redirectItem";


    public SolrCategoryRedirectModel()
    {
    }


    public SolrCategoryRedirectModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrCategoryRedirectModel(CategoryModel _redirectItem)
    {
        setRedirectItem(_redirectItem);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrCategoryRedirectModel(ItemModel _owner, CategoryModel _redirectItem)
    {
        setOwner(_owner);
        setRedirectItem(_redirectItem);
    }


    @Accessor(qualifier = "redirectItem", type = Accessor.Type.GETTER)
    public CategoryModel getRedirectItem()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("redirectItem");
    }


    @Accessor(qualifier = "redirectItem", type = Accessor.Type.SETTER)
    public void setRedirectItem(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("redirectItem", value);
    }
}
