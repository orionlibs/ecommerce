package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class SolrSearchConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SolrSearchConfig";
    public static final String PAGESIZE = "pageSize";
    public static final String DEFAULTSORTORDER = "defaultSortOrder";
    public static final String DESCRIPTION = "description";
    public static final String RESTRICTFIELDSINRESPONSE = "restrictFieldsInResponse";
    public static final String ENABLEHIGHLIGHTING = "enableHighlighting";
    public static final String ALLFACETVALUESINRESPONSE = "allFacetValuesInResponse";
    public static final String LEGACYMODE = "legacyMode";
    public static final String GROUPINGPROPERTY = "groupingProperty";


    public SolrSearchConfigModel()
    {
    }


    public SolrSearchConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchConfigModel(Integer _pageSize)
    {
        setPageSize(_pageSize);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchConfigModel(ItemModel _owner, Integer _pageSize)
    {
        setOwner(_owner);
        setPageSize(_pageSize);
    }


    @Accessor(qualifier = "defaultSortOrder", type = Accessor.Type.GETTER)
    public Collection<String> getDefaultSortOrder()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("defaultSortOrder");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "groupingProperty", type = Accessor.Type.GETTER)
    public SolrIndexedPropertyModel getGroupingProperty()
    {
        return (SolrIndexedPropertyModel)getPersistenceContext().getPropertyValue("groupingProperty");
    }


    @Accessor(qualifier = "pageSize", type = Accessor.Type.GETTER)
    public Integer getPageSize()
    {
        return (Integer)getPersistenceContext().getPropertyValue("pageSize");
    }


    @Accessor(qualifier = "allFacetValuesInResponse", type = Accessor.Type.GETTER)
    public boolean isAllFacetValuesInResponse()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("allFacetValuesInResponse"));
    }


    @Accessor(qualifier = "enableHighlighting", type = Accessor.Type.GETTER)
    public boolean isEnableHighlighting()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("enableHighlighting"));
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "legacyMode", type = Accessor.Type.GETTER)
    public boolean isLegacyMode()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("legacyMode"));
    }


    @Accessor(qualifier = "restrictFieldsInResponse", type = Accessor.Type.GETTER)
    public boolean isRestrictFieldsInResponse()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("restrictFieldsInResponse"));
    }


    @Accessor(qualifier = "allFacetValuesInResponse", type = Accessor.Type.SETTER)
    public void setAllFacetValuesInResponse(boolean value)
    {
        getPersistenceContext().setPropertyValue("allFacetValuesInResponse", toObject(value));
    }


    @Accessor(qualifier = "defaultSortOrder", type = Accessor.Type.SETTER)
    public void setDefaultSortOrder(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("defaultSortOrder", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "enableHighlighting", type = Accessor.Type.SETTER)
    public void setEnableHighlighting(boolean value)
    {
        getPersistenceContext().setPropertyValue("enableHighlighting", toObject(value));
    }


    @Accessor(qualifier = "groupingProperty", type = Accessor.Type.SETTER)
    public void setGroupingProperty(SolrIndexedPropertyModel value)
    {
        getPersistenceContext().setPropertyValue("groupingProperty", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "legacyMode", type = Accessor.Type.SETTER)
    public void setLegacyMode(boolean value)
    {
        getPersistenceContext().setPropertyValue("legacyMode", toObject(value));
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
}
