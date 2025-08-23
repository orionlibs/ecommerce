package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrValueRangeModel extends ItemModel
{
    public static final String _TYPECODE = "SolrValueRange";
    public static final String _SOLRVALUERANGESET2SOLRVALUERANGERELATION = "SolrValueRangeSet2SolrValueRangeRelation";
    public static final String NAME = "name";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String SOLRVALUERANGESETPOS = "solrValueRangeSetPOS";
    public static final String SOLRVALUERANGESET = "solrValueRangeSet";


    public SolrValueRangeModel()
    {
    }


    public SolrValueRangeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrValueRangeModel(String _from, String _name, SolrValueRangeSetModel _solrValueRangeSet)
    {
        setFrom(_from);
        setName(_name);
        setSolrValueRangeSet(_solrValueRangeSet);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrValueRangeModel(String _from, String _name, ItemModel _owner, SolrValueRangeSetModel _solrValueRangeSet)
    {
        setFrom(_from);
        setName(_name);
        setOwner(_owner);
        setSolrValueRangeSet(_solrValueRangeSet);
    }


    @Accessor(qualifier = "from", type = Accessor.Type.GETTER)
    public String getFrom()
    {
        return (String)getPersistenceContext().getPropertyValue("from");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "solrValueRangeSet", type = Accessor.Type.GETTER)
    public SolrValueRangeSetModel getSolrValueRangeSet()
    {
        return (SolrValueRangeSetModel)getPersistenceContext().getPropertyValue("solrValueRangeSet");
    }


    @Accessor(qualifier = "to", type = Accessor.Type.GETTER)
    public String getTo()
    {
        return (String)getPersistenceContext().getPropertyValue("to");
    }


    @Accessor(qualifier = "from", type = Accessor.Type.SETTER)
    public void setFrom(String value)
    {
        getPersistenceContext().setPropertyValue("from", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "solrValueRangeSet", type = Accessor.Type.SETTER)
    public void setSolrValueRangeSet(SolrValueRangeSetModel value)
    {
        getPersistenceContext().setPropertyValue("solrValueRangeSet", value);
    }


    @Accessor(qualifier = "to", type = Accessor.Type.SETTER)
    public void setTo(String value)
    {
        getPersistenceContext().setPropertyValue("to", value);
    }
}
