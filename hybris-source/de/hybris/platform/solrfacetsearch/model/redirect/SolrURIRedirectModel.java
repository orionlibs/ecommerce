package de.hybris.platform.solrfacetsearch.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrURIRedirectModel extends SolrAbstractKeywordRedirectModel
{
    public static final String _TYPECODE = "SolrURIRedirect";
    public static final String URL = "url";


    public SolrURIRedirectModel()
    {
    }


    public SolrURIRedirectModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrURIRedirectModel(String _url)
    {
        setUrl(_url);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrURIRedirectModel(ItemModel _owner, String _url)
    {
        setOwner(_owner);
        setUrl(_url);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }
}
