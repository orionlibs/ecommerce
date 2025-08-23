package de.hybris.platform.solrfacetsearch.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrAbstractKeywordRedirectModel extends ItemModel
{
    public static final String _TYPECODE = "SolrAbstractKeywordRedirect";
    public static final String HMCLABEL = "hmcLabel";


    public SolrAbstractKeywordRedirectModel()
    {
    }


    public SolrAbstractKeywordRedirectModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrAbstractKeywordRedirectModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "hmcLabel", type = Accessor.Type.GETTER)
    public String getHmcLabel()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "hmcLabel");
    }
}
