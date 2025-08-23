package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import java.util.List;

public class SolrIndexerQueryModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexerQuery";
    public static final String _SOLRINDEXEDTYPE2SOLRINDEXERQUERY = "SolrIndexedType2SolrIndexerQuery";
    public static final String IDENTIFIER = "identifier";
    public static final String TYPE = "type";
    public static final String QUERY = "query";
    public static final String INJECTLASTINDEXTIME = "injectLastIndexTime";
    public static final String INJECTCURRENTTIME = "injectCurrentTime";
    public static final String INJECTCURRENTDATE = "injectCurrentDate";
    public static final String USER = "user";
    public static final String PARAMETERPROVIDER = "parameterProvider";
    public static final String SOLRINDEXEDTYPEPOS = "solrIndexedTypePOS";
    public static final String SOLRINDEXEDTYPE = "solrIndexedType";
    public static final String SOLRINDEXERQUERYPARAMETERS = "solrIndexerQueryParameters";


    public SolrIndexerQueryModel()
    {
    }


    public SolrIndexerQueryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerQueryModel(String _identifier, String _query, IndexerOperationValues _type)
    {
        setIdentifier(_identifier);
        setQuery(_query);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerQueryModel(String _identifier, ItemModel _owner, String _query, IndexerOperationValues _type)
    {
        setIdentifier(_identifier);
        setOwner(_owner);
        setQuery(_query);
        setType(_type);
    }


    @Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
    public String getIdentifier()
    {
        return (String)getPersistenceContext().getPropertyValue("identifier");
    }


    @Accessor(qualifier = "parameterProvider", type = Accessor.Type.GETTER)
    public String getParameterProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("parameterProvider");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "solrIndexedType", type = Accessor.Type.GETTER)
    public SolrIndexedTypeModel getSolrIndexedType()
    {
        return (SolrIndexedTypeModel)getPersistenceContext().getPropertyValue("solrIndexedType");
    }


    @Accessor(qualifier = "solrIndexerQueryParameters", type = Accessor.Type.GETTER)
    public List<SolrIndexerQueryParameterModel> getSolrIndexerQueryParameters()
    {
        return (List<SolrIndexerQueryParameterModel>)getPersistenceContext().getPropertyValue("solrIndexerQueryParameters");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public IndexerOperationValues getType()
    {
        return (IndexerOperationValues)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "injectCurrentDate", type = Accessor.Type.GETTER)
    public boolean isInjectCurrentDate()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("injectCurrentDate"));
    }


    @Accessor(qualifier = "injectCurrentTime", type = Accessor.Type.GETTER)
    public boolean isInjectCurrentTime()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("injectCurrentTime"));
    }


    @Accessor(qualifier = "injectLastIndexTime", type = Accessor.Type.GETTER)
    public boolean isInjectLastIndexTime()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("injectLastIndexTime"));
    }


    @Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
    public void setIdentifier(String value)
    {
        getPersistenceContext().setPropertyValue("identifier", value);
    }


    @Accessor(qualifier = "injectCurrentDate", type = Accessor.Type.SETTER)
    public void setInjectCurrentDate(boolean value)
    {
        getPersistenceContext().setPropertyValue("injectCurrentDate", toObject(value));
    }


    @Accessor(qualifier = "injectCurrentTime", type = Accessor.Type.SETTER)
    public void setInjectCurrentTime(boolean value)
    {
        getPersistenceContext().setPropertyValue("injectCurrentTime", toObject(value));
    }


    @Accessor(qualifier = "injectLastIndexTime", type = Accessor.Type.SETTER)
    public void setInjectLastIndexTime(boolean value)
    {
        getPersistenceContext().setPropertyValue("injectLastIndexTime", toObject(value));
    }


    @Accessor(qualifier = "parameterProvider", type = Accessor.Type.SETTER)
    public void setParameterProvider(String value)
    {
        getPersistenceContext().setPropertyValue("parameterProvider", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }


    @Accessor(qualifier = "solrIndexedType", type = Accessor.Type.SETTER)
    public void setSolrIndexedType(SolrIndexedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("solrIndexedType", value);
    }


    @Accessor(qualifier = "solrIndexerQueryParameters", type = Accessor.Type.SETTER)
    public void setSolrIndexerQueryParameters(List<SolrIndexerQueryParameterModel> value)
    {
        getPersistenceContext().setPropertyValue("solrIndexerQueryParameters", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(IndexerOperationValues value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
