package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.enums.SnSearchTolerance;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SnFieldModel extends ItemModel
{
    public static final String _TYPECODE = "SnField";
    public static final String _SNINDEXTYPE2FIELD = "SnIndexType2Field";
    public static final String _SNINDEXERITEMSOURCEOPERATION2FIELD = "SnIndexerItemSourceOperation2Field";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String FIELDTYPE = "fieldType";
    public static final String VALUEPROVIDER = "valueProvider";
    public static final String VALUEPROVIDERPARAMETERS = "valueProviderParameters";
    public static final String RETRIEVABLE = "retrievable";
    public static final String SEARCHABLE = "searchable";
    public static final String SEARCHTOLERANCE = "searchTolerance";
    public static final String LOCALIZED = "localized";
    public static final String QUALIFIERTYPEID = "qualifierTypeId";
    public static final String MULTIVALUED = "multiValued";
    public static final String USEFORSPELLCHECKING = "useForSpellchecking";
    public static final String USEFORSUGGESTING = "useForSuggesting";
    public static final String WEIGHT = "weight";
    public static final String INDEXTYPEPOS = "indexTypePOS";
    public static final String INDEXTYPE = "indexType";
    public static final String INDEXERITEMSOURCEOPERATIONS = "indexerItemSourceOperations";


    public SnFieldModel()
    {
    }


    public SnFieldModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnFieldModel(SnFieldType _fieldType, String _id, SnIndexTypeModel _indexType)
    {
        setFieldType(_fieldType);
        setId(_id);
        setIndexType(_indexType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnFieldModel(SnFieldType _fieldType, String _id, SnIndexTypeModel _indexType, ItemModel _owner)
    {
        setFieldType(_fieldType);
        setId(_id);
        setIndexType(_indexType);
        setOwner(_owner);
    }


    @Accessor(qualifier = "fieldType", type = Accessor.Type.GETTER)
    public SnFieldType getFieldType()
    {
        return (SnFieldType)getPersistenceContext().getPropertyValue("fieldType");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "indexerItemSourceOperations", type = Accessor.Type.GETTER)
    public List<SnIndexerItemSourceOperationModel> getIndexerItemSourceOperations()
    {
        return (List<SnIndexerItemSourceOperationModel>)getPersistenceContext().getPropertyValue("indexerItemSourceOperations");
    }


    @Accessor(qualifier = "indexType", type = Accessor.Type.GETTER)
    public SnIndexTypeModel getIndexType()
    {
        return (SnIndexTypeModel)getPersistenceContext().getPropertyValue("indexType");
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
    public Boolean getLocalized()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("localized");
    }


    @Accessor(qualifier = "multiValued", type = Accessor.Type.GETTER)
    public Boolean getMultiValued()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("multiValued");
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


    @Accessor(qualifier = "qualifierTypeId", type = Accessor.Type.GETTER)
    public String getQualifierTypeId()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifierTypeId");
    }


    @Accessor(qualifier = "retrievable", type = Accessor.Type.GETTER)
    public Boolean getRetrievable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("retrievable");
    }


    @Accessor(qualifier = "searchable", type = Accessor.Type.GETTER)
    public Boolean getSearchable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("searchable");
    }


    @Accessor(qualifier = "searchTolerance", type = Accessor.Type.GETTER)
    public SnSearchTolerance getSearchTolerance()
    {
        return (SnSearchTolerance)getPersistenceContext().getPropertyValue("searchTolerance");
    }


    @Accessor(qualifier = "useForSpellchecking", type = Accessor.Type.GETTER)
    public Boolean getUseForSpellchecking()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useForSpellchecking");
    }


    @Accessor(qualifier = "useForSuggesting", type = Accessor.Type.GETTER)
    public Boolean getUseForSuggesting()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useForSuggesting");
    }


    @Accessor(qualifier = "valueProvider", type = Accessor.Type.GETTER)
    public String getValueProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("valueProvider");
    }


    @Accessor(qualifier = "valueProviderParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getValueProviderParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("valueProviderParameters");
    }


    @Accessor(qualifier = "weight", type = Accessor.Type.GETTER)
    public Float getWeight()
    {
        return (Float)getPersistenceContext().getPropertyValue("weight");
    }


    @Accessor(qualifier = "fieldType", type = Accessor.Type.SETTER)
    public void setFieldType(SnFieldType value)
    {
        getPersistenceContext().setPropertyValue("fieldType", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "indexerItemSourceOperations", type = Accessor.Type.SETTER)
    public void setIndexerItemSourceOperations(List<SnIndexerItemSourceOperationModel> value)
    {
        getPersistenceContext().setPropertyValue("indexerItemSourceOperations", value);
    }


    @Accessor(qualifier = "indexType", type = Accessor.Type.SETTER)
    public void setIndexType(SnIndexTypeModel value)
    {
        getPersistenceContext().setPropertyValue("indexType", value);
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.SETTER)
    public void setLocalized(Boolean value)
    {
        getPersistenceContext().setPropertyValue("localized", value);
    }


    @Accessor(qualifier = "multiValued", type = Accessor.Type.SETTER)
    public void setMultiValued(Boolean value)
    {
        getPersistenceContext().setPropertyValue("multiValued", value);
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


    @Accessor(qualifier = "qualifierTypeId", type = Accessor.Type.SETTER)
    public void setQualifierTypeId(String value)
    {
        getPersistenceContext().setPropertyValue("qualifierTypeId", value);
    }


    @Accessor(qualifier = "retrievable", type = Accessor.Type.SETTER)
    public void setRetrievable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("retrievable", value);
    }


    @Accessor(qualifier = "searchable", type = Accessor.Type.SETTER)
    public void setSearchable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("searchable", value);
    }


    @Accessor(qualifier = "searchTolerance", type = Accessor.Type.SETTER)
    public void setSearchTolerance(SnSearchTolerance value)
    {
        getPersistenceContext().setPropertyValue("searchTolerance", value);
    }


    @Accessor(qualifier = "useForSpellchecking", type = Accessor.Type.SETTER)
    public void setUseForSpellchecking(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useForSpellchecking", value);
    }


    @Accessor(qualifier = "useForSuggesting", type = Accessor.Type.SETTER)
    public void setUseForSuggesting(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useForSuggesting", value);
    }


    @Accessor(qualifier = "valueProvider", type = Accessor.Type.SETTER)
    public void setValueProvider(String value)
    {
        getPersistenceContext().setPropertyValue("valueProvider", value);
    }


    @Accessor(qualifier = "valueProviderParameters", type = Accessor.Type.SETTER)
    public void setValueProviderParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("valueProviderParameters", value);
    }


    @Accessor(qualifier = "weight", type = Accessor.Type.SETTER)
    public void setWeight(Float value)
    {
        getPersistenceContext().setPropertyValue("weight", value);
    }
}
