package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrSortFieldModel extends ItemModel
{
    public static final String _TYPECODE = "SolrSortField";
    public static final String _SOLRSORT2SOLRSORTFIELDREL = "SolrSort2SolrSortFieldRel";
    public static final String FIELDNAME = "fieldName";
    public static final String ASCENDING = "ascending";
    public static final String SORTPOS = "sortPOS";
    public static final String SORT = "sort";


    public SolrSortFieldModel()
    {
    }


    public SolrSortFieldModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSortFieldModel(boolean _ascending, String _fieldName, SolrSortModel _sort)
    {
        setAscending(_ascending);
        setFieldName(_fieldName);
        setSort(_sort);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSortFieldModel(boolean _ascending, String _fieldName, ItemModel _owner, SolrSortModel _sort)
    {
        setAscending(_ascending);
        setFieldName(_fieldName);
        setOwner(_owner);
        setSort(_sort);
    }


    @Accessor(qualifier = "fieldName", type = Accessor.Type.GETTER)
    public String getFieldName()
    {
        return (String)getPersistenceContext().getPropertyValue("fieldName");
    }


    @Accessor(qualifier = "sort", type = Accessor.Type.GETTER)
    public SolrSortModel getSort()
    {
        return (SolrSortModel)getPersistenceContext().getPropertyValue("sort");
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


    @Accessor(qualifier = "fieldName", type = Accessor.Type.SETTER)
    public void setFieldName(String value)
    {
        getPersistenceContext().setPropertyValue("fieldName", value);
    }


    @Accessor(qualifier = "sort", type = Accessor.Type.SETTER)
    public void setSort(SolrSortModel value)
    {
        getPersistenceContext().setPropertyValue("sort", value);
    }
}
