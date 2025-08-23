package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrIndexerQueryParameterModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexerQueryParameter";
    public static final String _SOLRINDEXERQUERY2SOLRINDEXERQUERYPARAMETER = "SolrIndexerQuery2SolrIndexerQueryParameter";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String SOLRINDEXERQUERYPOS = "solrIndexerQueryPOS";
    public static final String SOLRINDEXERQUERY = "solrIndexerQuery";


    public SolrIndexerQueryParameterModel()
    {
    }


    public SolrIndexerQueryParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerQueryParameterModel(String _name, String _value)
    {
        setName(_name);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerQueryParameterModel(String _name, ItemModel _owner, String _value)
    {
        setName(_name);
        setOwner(_owner);
        setValue(_value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "solrIndexerQuery", type = Accessor.Type.GETTER)
    public SolrIndexerQueryModel getSolrIndexerQuery()
    {
        return (SolrIndexerQueryModel)getPersistenceContext().getPropertyValue("solrIndexerQuery");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public String getValue()
    {
        return (String)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "solrIndexerQuery", type = Accessor.Type.SETTER)
    public void setSolrIndexerQuery(SolrIndexerQueryModel value)
    {
        getPersistenceContext().setPropertyValue("solrIndexerQuery", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(String value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
