package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;

public class SolrIndexModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndex";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String QUALIFIER = "qualifier";
    public static final String ACTIVE = "active";
    public static final String INDEXOPERATIONS = "indexOperations";


    public SolrIndexModel()
    {
    }


    public SolrIndexModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexModel(SolrFacetSearchConfigModel _facetSearchConfig, SolrIndexedTypeModel _indexedType, String _qualifier)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setIndexedType(_indexedType);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexModel(SolrFacetSearchConfigModel _facetSearchConfig, SolrIndexedTypeModel _indexedType, ItemModel _owner, String _qualifier)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setIndexedType(_indexedType);
        setOwner(_owner);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getFacetSearchConfig()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("facetSearchConfig");
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
    public SolrIndexedTypeModel getIndexedType()
    {
        return (SolrIndexedTypeModel)getPersistenceContext().getPropertyValue("indexedType");
    }


    @Accessor(qualifier = "indexOperations", type = Accessor.Type.GETTER)
    public Collection<SolrIndexOperationModel> getIndexOperations()
    {
        return (Collection<SolrIndexOperationModel>)getPersistenceContext().getPropertyValue("indexOperations");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public boolean isActive()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("active"));
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(boolean value)
    {
        getPersistenceContext().setPropertyValue("active", toObject(value));
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
    public void setFacetSearchConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfig", value);
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
    public void setIndexedType(SolrIndexedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("indexedType", value);
    }


    @Accessor(qualifier = "indexOperations", type = Accessor.Type.SETTER)
    public void setIndexOperations(Collection<SolrIndexOperationModel> value)
    {
        getPersistenceContext().setPropertyValue("indexOperations", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }
}
