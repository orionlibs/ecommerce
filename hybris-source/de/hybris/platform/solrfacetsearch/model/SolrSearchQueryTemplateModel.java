package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;
import java.util.Map;

public class SolrSearchQueryTemplateModel extends ItemModel
{
    public static final String _TYPECODE = "SolrSearchQueryTemplate";
    public static final String _SOLRINDEXEDTYPE2SOLRSEARCHQUERYTEMPLATE = "SolrIndexedType2SolrSearchQueryTemplate";
    public static final String NAME = "name";
    public static final String SHOWFACETS = "showFacets";
    public static final String RESTRICTFIELDSINRESPONSE = "restrictFieldsInResponse";
    public static final String ENABLEHIGHLIGHTING = "enableHighlighting";
    public static final String GROUP = "group";
    public static final String GROUPPROPERTY = "groupProperty";
    public static final String GROUPLIMIT = "groupLimit";
    public static final String GROUPFACETS = "groupFacets";
    public static final String PAGESIZE = "pageSize";
    public static final String FTSQUERYBUILDER = "ftsQueryBuilder";
    public static final String FTSQUERYBUILDERPARAMETERS = "ftsQueryBuilderParameters";
    public static final String INDEXEDTYPEPOS = "indexedTypePOS";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String SEARCHQUERYPROPERTIES = "searchQueryProperties";
    public static final String SEARCHQUERYSORTS = "searchQuerySorts";


    public SolrSearchQueryTemplateModel()
    {
    }


    public SolrSearchQueryTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchQueryTemplateModel(SolrIndexedTypeModel _indexedType, String _name)
    {
        setIndexedType(_indexedType);
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchQueryTemplateModel(SolrIndexedTypeModel _indexedType, String _name, ItemModel _owner)
    {
        setIndexedType(_indexedType);
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "ftsQueryBuilder", type = Accessor.Type.GETTER)
    public String getFtsQueryBuilder()
    {
        return (String)getPersistenceContext().getPropertyValue("ftsQueryBuilder");
    }


    @Accessor(qualifier = "ftsQueryBuilderParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getFtsQueryBuilderParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("ftsQueryBuilderParameters");
    }


    @Accessor(qualifier = "groupLimit", type = Accessor.Type.GETTER)
    public Integer getGroupLimit()
    {
        return (Integer)getPersistenceContext().getPropertyValue("groupLimit");
    }


    @Accessor(qualifier = "groupProperty", type = Accessor.Type.GETTER)
    public SolrIndexedPropertyModel getGroupProperty()
    {
        return (SolrIndexedPropertyModel)getPersistenceContext().getPropertyValue("groupProperty");
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
    public SolrIndexedTypeModel getIndexedType()
    {
        return (SolrIndexedTypeModel)getPersistenceContext().getPropertyValue("indexedType");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "pageSize", type = Accessor.Type.GETTER)
    public Integer getPageSize()
    {
        return (Integer)getPersistenceContext().getPropertyValue("pageSize");
    }


    @Accessor(qualifier = "searchQueryProperties", type = Accessor.Type.GETTER)
    public Collection<SolrSearchQueryPropertyModel> getSearchQueryProperties()
    {
        return (Collection<SolrSearchQueryPropertyModel>)getPersistenceContext().getPropertyValue("searchQueryProperties");
    }


    @Accessor(qualifier = "searchQuerySorts", type = Accessor.Type.GETTER)
    public Collection<SolrSearchQuerySortModel> getSearchQuerySorts()
    {
        return (Collection<SolrSearchQuerySortModel>)getPersistenceContext().getPropertyValue("searchQuerySorts");
    }


    @Accessor(qualifier = "enableHighlighting", type = Accessor.Type.GETTER)
    public boolean isEnableHighlighting()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("enableHighlighting"));
    }


    @Accessor(qualifier = "group", type = Accessor.Type.GETTER)
    public boolean isGroup()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("group"));
    }


    @Accessor(qualifier = "groupFacets", type = Accessor.Type.GETTER)
    public boolean isGroupFacets()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("groupFacets"));
    }


    @Accessor(qualifier = "restrictFieldsInResponse", type = Accessor.Type.GETTER)
    public boolean isRestrictFieldsInResponse()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("restrictFieldsInResponse"));
    }


    @Accessor(qualifier = "showFacets", type = Accessor.Type.GETTER)
    public boolean isShowFacets()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("showFacets"));
    }


    @Accessor(qualifier = "enableHighlighting", type = Accessor.Type.SETTER)
    public void setEnableHighlighting(boolean value)
    {
        getPersistenceContext().setPropertyValue("enableHighlighting", toObject(value));
    }


    @Accessor(qualifier = "ftsQueryBuilder", type = Accessor.Type.SETTER)
    public void setFtsQueryBuilder(String value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryBuilder", value);
    }


    @Accessor(qualifier = "ftsQueryBuilderParameters", type = Accessor.Type.SETTER)
    public void setFtsQueryBuilderParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryBuilderParameters", value);
    }


    @Accessor(qualifier = "group", type = Accessor.Type.SETTER)
    public void setGroup(boolean value)
    {
        getPersistenceContext().setPropertyValue("group", toObject(value));
    }


    @Accessor(qualifier = "groupFacets", type = Accessor.Type.SETTER)
    public void setGroupFacets(boolean value)
    {
        getPersistenceContext().setPropertyValue("groupFacets", toObject(value));
    }


    @Accessor(qualifier = "groupLimit", type = Accessor.Type.SETTER)
    public void setGroupLimit(Integer value)
    {
        getPersistenceContext().setPropertyValue("groupLimit", value);
    }


    @Accessor(qualifier = "groupProperty", type = Accessor.Type.SETTER)
    public void setGroupProperty(SolrIndexedPropertyModel value)
    {
        getPersistenceContext().setPropertyValue("groupProperty", value);
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
    public void setIndexedType(SolrIndexedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("indexedType", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "pageSize", type = Accessor.Type.SETTER)
    public void setPageSize(Integer value)
    {
        getPersistenceContext().setPropertyValue("pageSize", value);
    }


    @Accessor(qualifier = "restrictFieldsInResponse", type = Accessor.Type.SETTER)
    public void setRestrictFieldsInResponse(boolean value)
    {
        getPersistenceContext().setPropertyValue("restrictFieldsInResponse", toObject(value));
    }


    @Accessor(qualifier = "searchQueryProperties", type = Accessor.Type.SETTER)
    public void setSearchQueryProperties(Collection<SolrSearchQueryPropertyModel> value)
    {
        getPersistenceContext().setPropertyValue("searchQueryProperties", value);
    }


    @Accessor(qualifier = "searchQuerySorts", type = Accessor.Type.SETTER)
    public void setSearchQuerySorts(Collection<SolrSearchQuerySortModel> value)
    {
        getPersistenceContext().setPropertyValue("searchQuerySorts", value);
    }


    @Accessor(qualifier = "showFacets", type = Accessor.Type.SETTER)
    public void setShowFacets(boolean value)
    {
        getPersistenceContext().setPropertyValue("showFacets", toObject(value));
    }
}
