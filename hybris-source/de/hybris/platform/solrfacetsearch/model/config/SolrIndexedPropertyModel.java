package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commerceservices.enums.SolrIndexedPropertyFacetSort;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.SolrIndexedPropertyFacetType;
import de.hybris.platform.solrfacetsearch.enums.SolrPropertiesTypes;
import de.hybris.platform.solrfacetsearch.enums.SolrWildcardType;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryPropertyModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SolrIndexedPropertyModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexedProperty";
    public static final String _SOLRINDEXEDTYPE2SOLRINDEXEDPROPERTY = "SolrIndexedType2SolrIndexedProperty";
    public static final String NAME = "name";
    public static final String DISPLAYNAME = "displayName";
    public static final String TYPE = "type";
    public static final String SORTABLETYPE = "sortableType";
    public static final String FACET = "facet";
    public static final String LOCALIZED = "localized";
    public static final String CURRENCY = "currency";
    public static final String MULTIVALUE = "multiValue";
    public static final String RANGESET = "rangeSet";
    public static final String FACETDISPLAYNAMEPROVIDER = "facetDisplayNameProvider";
    public static final String FACETTYPE = "facetType";
    public static final String FIELDVALUEPROVIDER = "fieldValueProvider";
    public static final String VALUEPROVIDERPARAMETER = "valueProviderParameter";
    public static final String VALUEPROVIDERPARAMETERS = "valueProviderParameters";
    public static final String EXPORTID = "exportId";
    public static final String USEFORSPELLCHECKING = "useForSpellchecking";
    public static final String USEFORAUTOCOMPLETE = "useForAutocomplete";
    public static final String PRIORITY = "priority";
    public static final String INCLUDEINRESPONSE = "includeInResponse";
    public static final String USEFORHIGHLIGHTING = "useForHighlighting";
    public static final String CUSTOMFACETSORTPROVIDER = "customFacetSortProvider";
    public static final String TOPVALUESPROVIDER = "topValuesProvider";
    public static final String FTSQUERY = "ftsQuery";
    public static final String FTSQUERYMINTERMLENGTH = "ftsQueryMinTermLength";
    public static final String FTSQUERYBOOST = "ftsQueryBoost";
    public static final String FTSFUZZYQUERY = "ftsFuzzyQuery";
    public static final String FTSFUZZYQUERYMINTERMLENGTH = "ftsFuzzyQueryMinTermLength";
    public static final String FTSFUZZYQUERYFUZZINESS = "ftsFuzzyQueryFuzziness";
    public static final String FTSFUZZYQUERYBOOST = "ftsFuzzyQueryBoost";
    public static final String FTSWILDCARDQUERY = "ftsWildcardQuery";
    public static final String FTSWILDCARDQUERYMINTERMLENGTH = "ftsWildcardQueryMinTermLength";
    public static final String FTSWILDCARDQUERYTYPE = "ftsWildcardQueryType";
    public static final String FTSWILDCARDQUERYBOOST = "ftsWildcardQueryBoost";
    public static final String FTSPHRASEQUERY = "ftsPhraseQuery";
    public static final String FTSPHRASEQUERYSLOP = "ftsPhraseQuerySlop";
    public static final String FTSPHRASEQUERYBOOST = "ftsPhraseQueryBoost";
    public static final String CLASSATTRIBUTEASSIGNMENT = "classAttributeAssignment";
    public static final String CATEGORYFIELD = "categoryField";
    public static final String VISIBLE = "visible";
    public static final String SOLRINDEXEDTYPEPOS = "solrIndexedTypePOS";
    public static final String SOLRINDEXEDTYPE = "solrIndexedType";
    public static final String RANGESETS = "rangeSets";
    public static final String SEARCHQUERYPROPERTIES = "searchQueryProperties";
    public static final String FACETSORT = "facetSort";


    public SolrIndexedPropertyModel()
    {
    }


    public SolrIndexedPropertyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexedPropertyModel(String _name, SolrPropertiesTypes _type)
    {
        setName(_name);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexedPropertyModel(String _name, ItemModel _owner, SolrPropertiesTypes _type)
    {
        setName(_name);
        setOwner(_owner);
        setType(_type);
    }


    @Accessor(qualifier = "classAttributeAssignment", type = Accessor.Type.GETTER)
    public ClassAttributeAssignmentModel getClassAttributeAssignment()
    {
        return (ClassAttributeAssignmentModel)getPersistenceContext().getPropertyValue("classAttributeAssignment");
    }


    @Accessor(qualifier = "customFacetSortProvider", type = Accessor.Type.GETTER)
    public String getCustomFacetSortProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("customFacetSortProvider");
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
    public String getDisplayName()
    {
        return getDisplayName(null);
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
    public String getDisplayName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("displayName", loc);
    }


    @Accessor(qualifier = "exportId", type = Accessor.Type.GETTER)
    public String getExportId()
    {
        return (String)getPersistenceContext().getPropertyValue("exportId");
    }


    @Accessor(qualifier = "facetDisplayNameProvider", type = Accessor.Type.GETTER)
    public String getFacetDisplayNameProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("facetDisplayNameProvider");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "facetSort", type = Accessor.Type.GETTER)
    public SolrIndexedPropertyFacetSort getFacetSort()
    {
        return (SolrIndexedPropertyFacetSort)getPersistenceContext().getPropertyValue("facetSort");
    }


    @Accessor(qualifier = "facetType", type = Accessor.Type.GETTER)
    public SolrIndexedPropertyFacetType getFacetType()
    {
        return (SolrIndexedPropertyFacetType)getPersistenceContext().getPropertyValue("facetType");
    }


    @Accessor(qualifier = "fieldValueProvider", type = Accessor.Type.GETTER)
    public String getFieldValueProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("fieldValueProvider");
    }


    @Accessor(qualifier = "ftsFuzzyQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsFuzzyQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsFuzzyQueryBoost");
    }


    @Accessor(qualifier = "ftsFuzzyQueryFuzziness", type = Accessor.Type.GETTER)
    public Integer getFtsFuzzyQueryFuzziness()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsFuzzyQueryFuzziness");
    }


    @Accessor(qualifier = "ftsFuzzyQueryMinTermLength", type = Accessor.Type.GETTER)
    public Integer getFtsFuzzyQueryMinTermLength()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsFuzzyQueryMinTermLength");
    }


    @Accessor(qualifier = "ftsPhraseQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsPhraseQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsPhraseQueryBoost");
    }


    @Accessor(qualifier = "ftsPhraseQuerySlop", type = Accessor.Type.GETTER)
    public Float getFtsPhraseQuerySlop()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsPhraseQuerySlop");
    }


    @Accessor(qualifier = "ftsQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsQueryBoost");
    }


    @Accessor(qualifier = "ftsQueryMinTermLength", type = Accessor.Type.GETTER)
    public Integer getFtsQueryMinTermLength()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsQueryMinTermLength");
    }


    @Accessor(qualifier = "ftsWildcardQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsWildcardQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsWildcardQueryBoost");
    }


    @Accessor(qualifier = "ftsWildcardQueryMinTermLength", type = Accessor.Type.GETTER)
    public Integer getFtsWildcardQueryMinTermLength()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsWildcardQueryMinTermLength");
    }


    @Accessor(qualifier = "ftsWildcardQueryType", type = Accessor.Type.GETTER)
    public SolrWildcardType getFtsWildcardQueryType()
    {
        return (SolrWildcardType)getPersistenceContext().getPropertyValue("ftsWildcardQueryType");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public int getPriority()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("priority"));
    }


    @Accessor(qualifier = "rangeSet", type = Accessor.Type.GETTER)
    public SolrValueRangeSetModel getRangeSet()
    {
        return (SolrValueRangeSetModel)getPersistenceContext().getPropertyValue("rangeSet");
    }


    @Accessor(qualifier = "rangeSets", type = Accessor.Type.GETTER)
    public List<SolrValueRangeSetModel> getRangeSets()
    {
        return (List<SolrValueRangeSetModel>)getPersistenceContext().getPropertyValue("rangeSets");
    }


    @Accessor(qualifier = "searchQueryProperties", type = Accessor.Type.GETTER)
    public Collection<SolrSearchQueryPropertyModel> getSearchQueryProperties()
    {
        return (Collection<SolrSearchQueryPropertyModel>)getPersistenceContext().getPropertyValue("searchQueryProperties");
    }


    @Accessor(qualifier = "solrIndexedType", type = Accessor.Type.GETTER)
    public SolrIndexedTypeModel getSolrIndexedType()
    {
        return (SolrIndexedTypeModel)getPersistenceContext().getPropertyValue("solrIndexedType");
    }


    @Accessor(qualifier = "sortableType", type = Accessor.Type.GETTER)
    public SolrPropertiesTypes getSortableType()
    {
        return (SolrPropertiesTypes)getPersistenceContext().getPropertyValue("sortableType");
    }


    @Accessor(qualifier = "topValuesProvider", type = Accessor.Type.GETTER)
    public String getTopValuesProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("topValuesProvider");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public SolrPropertiesTypes getType()
    {
        return (SolrPropertiesTypes)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "useForAutocomplete", type = Accessor.Type.GETTER)
    public Boolean getUseForAutocomplete()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useForAutocomplete");
    }


    @Accessor(qualifier = "useForHighlighting", type = Accessor.Type.GETTER)
    public Boolean getUseForHighlighting()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useForHighlighting");
    }


    @Accessor(qualifier = "useForSpellchecking", type = Accessor.Type.GETTER)
    public Boolean getUseForSpellchecking()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useForSpellchecking");
    }


    @Accessor(qualifier = "valueProviderParameter", type = Accessor.Type.GETTER)
    public String getValueProviderParameter()
    {
        return (String)getPersistenceContext().getPropertyValue("valueProviderParameter");
    }


    @Accessor(qualifier = "valueProviderParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getValueProviderParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("valueProviderParameters");
    }


    @Accessor(qualifier = "categoryField", type = Accessor.Type.GETTER)
    public boolean isCategoryField()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("categoryField"));
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public boolean isCurrency()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("currency"));
    }


    @Accessor(qualifier = "facet", type = Accessor.Type.GETTER)
    public boolean isFacet()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("facet"));
    }


    @Accessor(qualifier = "ftsFuzzyQuery", type = Accessor.Type.GETTER)
    public boolean isFtsFuzzyQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsFuzzyQuery"));
    }


    @Accessor(qualifier = "ftsPhraseQuery", type = Accessor.Type.GETTER)
    public boolean isFtsPhraseQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsPhraseQuery"));
    }


    @Accessor(qualifier = "ftsQuery", type = Accessor.Type.GETTER)
    public boolean isFtsQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsQuery"));
    }


    @Accessor(qualifier = "ftsWildcardQuery", type = Accessor.Type.GETTER)
    public boolean isFtsWildcardQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsWildcardQuery"));
    }


    @Accessor(qualifier = "includeInResponse", type = Accessor.Type.GETTER)
    public boolean isIncludeInResponse()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("includeInResponse"));
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
    public boolean isLocalized()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("localized"));
    }


    @Accessor(qualifier = "multiValue", type = Accessor.Type.GETTER)
    public boolean isMultiValue()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("multiValue"));
    }


    @Accessor(qualifier = "visible", type = Accessor.Type.GETTER)
    public boolean isVisible()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("visible"));
    }


    @Accessor(qualifier = "categoryField", type = Accessor.Type.SETTER)
    public void setCategoryField(boolean value)
    {
        getPersistenceContext().setPropertyValue("categoryField", toObject(value));
    }


    @Accessor(qualifier = "classAttributeAssignment", type = Accessor.Type.SETTER)
    public void setClassAttributeAssignment(ClassAttributeAssignmentModel value)
    {
        getPersistenceContext().setPropertyValue("classAttributeAssignment", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(boolean value)
    {
        getPersistenceContext().setPropertyValue("currency", toObject(value));
    }


    @Accessor(qualifier = "customFacetSortProvider", type = Accessor.Type.SETTER)
    public void setCustomFacetSortProvider(String value)
    {
        getPersistenceContext().setPropertyValue("customFacetSortProvider", value);
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.SETTER)
    public void setDisplayName(String value)
    {
        setDisplayName(value, null);
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.SETTER)
    public void setDisplayName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("displayName", loc, value);
    }


    @Accessor(qualifier = "exportId", type = Accessor.Type.SETTER)
    public void setExportId(String value)
    {
        getPersistenceContext().setPropertyValue("exportId", value);
    }


    @Accessor(qualifier = "facet", type = Accessor.Type.SETTER)
    public void setFacet(boolean value)
    {
        getPersistenceContext().setPropertyValue("facet", toObject(value));
    }


    @Accessor(qualifier = "facetDisplayNameProvider", type = Accessor.Type.SETTER)
    public void setFacetDisplayNameProvider(String value)
    {
        getPersistenceContext().setPropertyValue("facetDisplayNameProvider", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "facetSort", type = Accessor.Type.SETTER)
    public void setFacetSort(SolrIndexedPropertyFacetSort value)
    {
        getPersistenceContext().setPropertyValue("facetSort", value);
    }


    @Accessor(qualifier = "facetType", type = Accessor.Type.SETTER)
    public void setFacetType(SolrIndexedPropertyFacetType value)
    {
        getPersistenceContext().setPropertyValue("facetType", value);
    }


    @Accessor(qualifier = "fieldValueProvider", type = Accessor.Type.SETTER)
    public void setFieldValueProvider(String value)
    {
        getPersistenceContext().setPropertyValue("fieldValueProvider", value);
    }


    @Accessor(qualifier = "ftsFuzzyQuery", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsFuzzyQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQueryBoost", value);
    }


    @Accessor(qualifier = "ftsFuzzyQueryFuzziness", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQueryFuzziness(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQueryFuzziness", value);
    }


    @Accessor(qualifier = "ftsFuzzyQueryMinTermLength", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQueryMinTermLength(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQueryMinTermLength", value);
    }


    @Accessor(qualifier = "ftsPhraseQuery", type = Accessor.Type.SETTER)
    public void setFtsPhraseQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsPhraseQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsPhraseQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsPhraseQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsPhraseQueryBoost", value);
    }


    @Accessor(qualifier = "ftsPhraseQuerySlop", type = Accessor.Type.SETTER)
    public void setFtsPhraseQuerySlop(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsPhraseQuerySlop", value);
    }


    @Accessor(qualifier = "ftsQuery", type = Accessor.Type.SETTER)
    public void setFtsQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryBoost", value);
    }


    @Accessor(qualifier = "ftsQueryMinTermLength", type = Accessor.Type.SETTER)
    public void setFtsQueryMinTermLength(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryMinTermLength", value);
    }


    @Accessor(qualifier = "ftsWildcardQuery", type = Accessor.Type.SETTER)
    public void setFtsWildcardQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsWildcardQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsWildcardQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQueryBoost", value);
    }


    @Accessor(qualifier = "ftsWildcardQueryMinTermLength", type = Accessor.Type.SETTER)
    public void setFtsWildcardQueryMinTermLength(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQueryMinTermLength", value);
    }


    @Accessor(qualifier = "ftsWildcardQueryType", type = Accessor.Type.SETTER)
    public void setFtsWildcardQueryType(SolrWildcardType value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQueryType", value);
    }


    @Accessor(qualifier = "includeInResponse", type = Accessor.Type.SETTER)
    public void setIncludeInResponse(boolean value)
    {
        getPersistenceContext().setPropertyValue("includeInResponse", toObject(value));
    }


    @Accessor(qualifier = "localized", type = Accessor.Type.SETTER)
    public void setLocalized(boolean value)
    {
        getPersistenceContext().setPropertyValue("localized", toObject(value));
    }


    @Accessor(qualifier = "multiValue", type = Accessor.Type.SETTER)
    public void setMultiValue(boolean value)
    {
        getPersistenceContext().setPropertyValue("multiValue", toObject(value));
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(int value)
    {
        getPersistenceContext().setPropertyValue("priority", toObject(value));
    }


    @Accessor(qualifier = "rangeSet", type = Accessor.Type.SETTER)
    public void setRangeSet(SolrValueRangeSetModel value)
    {
        getPersistenceContext().setPropertyValue("rangeSet", value);
    }


    @Accessor(qualifier = "rangeSets", type = Accessor.Type.SETTER)
    public void setRangeSets(List<SolrValueRangeSetModel> value)
    {
        getPersistenceContext().setPropertyValue("rangeSets", value);
    }


    @Accessor(qualifier = "searchQueryProperties", type = Accessor.Type.SETTER)
    public void setSearchQueryProperties(Collection<SolrSearchQueryPropertyModel> value)
    {
        getPersistenceContext().setPropertyValue("searchQueryProperties", value);
    }


    @Accessor(qualifier = "solrIndexedType", type = Accessor.Type.SETTER)
    public void setSolrIndexedType(SolrIndexedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("solrIndexedType", value);
    }


    @Accessor(qualifier = "sortableType", type = Accessor.Type.SETTER)
    public void setSortableType(SolrPropertiesTypes value)
    {
        getPersistenceContext().setPropertyValue("sortableType", value);
    }


    @Accessor(qualifier = "topValuesProvider", type = Accessor.Type.SETTER)
    public void setTopValuesProvider(String value)
    {
        getPersistenceContext().setPropertyValue("topValuesProvider", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(SolrPropertiesTypes value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "useForAutocomplete", type = Accessor.Type.SETTER)
    public void setUseForAutocomplete(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useForAutocomplete", value);
    }


    @Accessor(qualifier = "useForHighlighting", type = Accessor.Type.SETTER)
    public void setUseForHighlighting(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useForHighlighting", value);
    }


    @Accessor(qualifier = "useForSpellchecking", type = Accessor.Type.SETTER)
    public void setUseForSpellchecking(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useForSpellchecking", value);
    }


    @Accessor(qualifier = "valueProviderParameter", type = Accessor.Type.SETTER)
    public void setValueProviderParameter(String value)
    {
        getPersistenceContext().setPropertyValue("valueProviderParameter", value);
    }


    @Accessor(qualifier = "valueProviderParameters", type = Accessor.Type.SETTER)
    public void setValueProviderParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("valueProviderParameters", value);
    }


    @Accessor(qualifier = "visible", type = Accessor.Type.SETTER)
    public void setVisible(boolean value)
    {
        getPersistenceContext().setPropertyValue("visible", toObject(value));
    }
}
