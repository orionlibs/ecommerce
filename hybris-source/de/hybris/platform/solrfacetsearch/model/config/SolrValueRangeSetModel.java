package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class SolrValueRangeSetModel extends ItemModel
{
    public static final String _TYPECODE = "SolrValueRangeSet";
    public static final String _SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION = "SolrFacetSearchConfig2SolrValueRangeSetRelation";
    public static final String _SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION = "SolrIndexedProperty2SolrValueRangeSetRelation";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String QUALIFIER = "qualifier";
    public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";
    public static final String SOLRVALUERANGES = "solrValueRanges";
    public static final String INDEXEDPROPERTIES = "indexedProperties";


    public SolrValueRangeSetModel()
    {
    }


    public SolrValueRangeSetModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrValueRangeSetModel(String _name, String _type)
    {
        setName(_name);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrValueRangeSetModel(String _name, ItemModel _owner, String _type)
    {
        setName(_name);
        setOwner(_owner);
        setType(_type);
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.GETTER)
    public List<SolrFacetSearchConfigModel> getFacetSearchConfigs()
    {
        return (List<SolrFacetSearchConfigModel>)getPersistenceContext().getPropertyValue("facetSearchConfigs");
    }


    @Accessor(qualifier = "indexedProperties", type = Accessor.Type.GETTER)
    public List<SolrIndexedPropertyModel> getIndexedProperties()
    {
        return (List<SolrIndexedPropertyModel>)getPersistenceContext().getPropertyValue("indexedProperties");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "solrValueRanges", type = Accessor.Type.GETTER)
    public List<SolrValueRangeModel> getSolrValueRanges()
    {
        return (List<SolrValueRangeModel>)getPersistenceContext().getPropertyValue("solrValueRanges");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType()
    {
        return (String)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.SETTER)
    public void setFacetSearchConfigs(List<SolrFacetSearchConfigModel> value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfigs", value);
    }


    @Accessor(qualifier = "indexedProperties", type = Accessor.Type.SETTER)
    public void setIndexedProperties(List<SolrIndexedPropertyModel> value)
    {
        getPersistenceContext().setPropertyValue("indexedProperties", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }


    @Accessor(qualifier = "solrValueRanges", type = Accessor.Type.SETTER)
    public void setSolrValueRanges(List<SolrValueRangeModel> value)
    {
        getPersistenceContext().setPropertyValue("solrValueRanges", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(String value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
