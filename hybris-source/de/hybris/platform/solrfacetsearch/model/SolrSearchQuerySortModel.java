package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrSearchQuerySortModel extends ItemModel
{
    public static final String _TYPECODE = "SolrSearchQuerySort";
    public static final String _SOLRSEARCHQUERYTEMPLATE2SOLRSEARCHQUERYSORT = "SolrSearchQueryTemplate2SolrSearchQuerySort";
    public static final String FIELD = "field";
    public static final String ASCENDING = "ascending";
    public static final String SEARCHQUERYTEMPLATEPOS = "searchQueryTemplatePOS";
    public static final String SEARCHQUERYTEMPLATE = "searchQueryTemplate";


    public SolrSearchQuerySortModel()
    {
    }


    public SolrSearchQuerySortModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchQuerySortModel(SolrSearchQueryTemplateModel _searchQueryTemplate)
    {
        setSearchQueryTemplate(_searchQueryTemplate);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchQuerySortModel(ItemModel _owner, SolrSearchQueryTemplateModel _searchQueryTemplate)
    {
        setOwner(_owner);
        setSearchQueryTemplate(_searchQueryTemplate);
    }


    @Accessor(qualifier = "field", type = Accessor.Type.GETTER)
    public String getField()
    {
        return (String)getPersistenceContext().getPropertyValue("field");
    }


    @Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.GETTER)
    public SolrSearchQueryTemplateModel getSearchQueryTemplate()
    {
        return (SolrSearchQueryTemplateModel)getPersistenceContext().getPropertyValue("searchQueryTemplate");
    }


    @Accessor(qualifier = "ascending", type = Accessor.Type.GETTER)
    public boolean isAscending()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ascending"));
    }


    @Accessor(qualifier = "ascending", type = Accessor.Type.SETTER)
    public void setAscending(boolean value)
    {
        getPersistenceContext().setPropertyValue("ascending", toObject(value));
    }


    @Accessor(qualifier = "field", type = Accessor.Type.SETTER)
    public void setField(String value)
    {
        getPersistenceContext().setPropertyValue("field", value);
    }


    @Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.SETTER)
    public void setSearchQueryTemplate(SolrSearchQueryTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("searchQueryTemplate", value);
    }
}
