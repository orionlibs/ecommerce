package de.hybris.platform.acceleratorservices.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;

public class SolrPageRedirectModel extends SolrAbstractKeywordRedirectModel
{
    public static final String _TYPECODE = "SolrPageRedirect";
    public static final String REDIRECTITEM = "redirectItem";


    public SolrPageRedirectModel()
    {
    }


    public SolrPageRedirectModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrPageRedirectModel(AbstractPageModel _redirectItem)
    {
        setRedirectItem(_redirectItem);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrPageRedirectModel(ItemModel _owner, AbstractPageModel _redirectItem)
    {
        setOwner(_owner);
        setRedirectItem(_redirectItem);
    }


    @Accessor(qualifier = "redirectItem", type = Accessor.Type.GETTER)
    public AbstractPageModel getRedirectItem()
    {
        return (AbstractPageModel)getPersistenceContext().getPropertyValue("redirectItem");
    }


    @Accessor(qualifier = "redirectItem", type = Accessor.Type.SETTER)
    public void setRedirectItem(AbstractPageModel value)
    {
        getPersistenceContext().setPropertyValue("redirectItem", value);
    }
}
