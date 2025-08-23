package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;
import java.util.Locale;

public class SolrSortModel extends ItemModel
{
    public static final String _TYPECODE = "SolrSort";
    public static final String _SOLRINDEXEDTYPE2SOLRSORTREL = "SolrIndexedType2SolrSortRel";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String USEBOOST = "useBoost";
    public static final String INDEXEDTYPEPOS = "indexedTypePOS";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String FIELDS = "fields";


    public SolrSortModel()
    {
    }


    public SolrSortModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSortModel(String _code, SolrIndexedTypeModel _indexedType)
    {
        setCode(_code);
        setIndexedType(_indexedType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSortModel(String _code, SolrIndexedTypeModel _indexedType, ItemModel _owner)
    {
        setCode(_code);
        setIndexedType(_indexedType);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "fields", type = Accessor.Type.GETTER)
    public List<SolrSortFieldModel> getFields()
    {
        return (List<SolrSortFieldModel>)getPersistenceContext().getPropertyValue("fields");
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
    public SolrIndexedTypeModel getIndexedType()
    {
        return (SolrIndexedTypeModel)getPersistenceContext().getPropertyValue("indexedType");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "useBoost", type = Accessor.Type.GETTER)
    public boolean isUseBoost()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("useBoost"));
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "fields", type = Accessor.Type.SETTER)
    public void setFields(List<SolrSortFieldModel> value)
    {
        getPersistenceContext().setPropertyValue("fields", value);
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
    public void setIndexedType(SolrIndexedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("indexedType", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "useBoost", type = Accessor.Type.SETTER)
    public void setUseBoost(boolean value)
    {
        getPersistenceContext().setPropertyValue("useBoost", toObject(value));
    }
}
