package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import java.io.Serializable;
import java.util.Map;

public class IndexedProperty implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String displayName;
    private String sortableType;
    private String type;
    private String exportId;
    private boolean facet;
    private boolean localized;
    private boolean currency;
    private boolean multiValue;
    private boolean spellCheck;
    private boolean autoSuggest;
    private boolean highlight;
    private FacetType facetType;
    private int priority;
    private boolean includeInResponse;
    private String facetDisplayNameProvider;
    private String fieldValueProvider;
    private String valueProviderParameter;
    private Map<String, String> valueProviderParameters;
    private String facetSortProvider;
    private String topValuesProvider;
    private Map<String, ValueRangeSet> valueRangeSets;
    private boolean ftsQuery;
    private Integer ftsQueryMinTermLength;
    private Float ftsQueryBoost;
    private boolean ftsFuzzyQuery;
    private Integer ftsFuzzyQueryMinTermLength;
    private Integer ftsFuzzyQueryFuzziness;
    private Float ftsFuzzyQueryBoost;
    private boolean ftsWildcardQuery;
    private Integer ftsWildcardQueryMinTermLength;
    private WildcardType ftsWildcardQueryType;
    private Float ftsWildcardQueryBoost;
    private boolean ftsPhraseQuery;
    private Float ftsPhraseQuerySlop;
    private Float ftsPhraseQueryBoost;
    private ClassAttributeAssignmentModel classAttributeAssignment;
    private boolean categoryField;
    private boolean multiSelect;
    private boolean visible;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }


    public void setSortableType(String sortableType)
    {
        this.sortableType = sortableType;
    }


    public String getSortableType()
    {
        return this.sortableType;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
    {
        return this.type;
    }


    public void setExportId(String exportId)
    {
        this.exportId = exportId;
    }


    public String getExportId()
    {
        return this.exportId;
    }


    public void setFacet(boolean facet)
    {
        this.facet = facet;
    }


    public boolean isFacet()
    {
        return this.facet;
    }


    public void setLocalized(boolean localized)
    {
        this.localized = localized;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public void setCurrency(boolean currency)
    {
        this.currency = currency;
    }


    public boolean isCurrency()
    {
        return this.currency;
    }


    public void setMultiValue(boolean multiValue)
    {
        this.multiValue = multiValue;
    }


    public boolean isMultiValue()
    {
        return this.multiValue;
    }


    public void setSpellCheck(boolean spellCheck)
    {
        this.spellCheck = spellCheck;
    }


    public boolean isSpellCheck()
    {
        return this.spellCheck;
    }


    public void setAutoSuggest(boolean autoSuggest)
    {
        this.autoSuggest = autoSuggest;
    }


    public boolean isAutoSuggest()
    {
        return this.autoSuggest;
    }


    public void setHighlight(boolean highlight)
    {
        this.highlight = highlight;
    }


    public boolean isHighlight()
    {
        return this.highlight;
    }


    public void setFacetType(FacetType facetType)
    {
        this.facetType = facetType;
    }


    public FacetType getFacetType()
    {
        return this.facetType;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public int getPriority()
    {
        return this.priority;
    }


    public void setIncludeInResponse(boolean includeInResponse)
    {
        this.includeInResponse = includeInResponse;
    }


    public boolean isIncludeInResponse()
    {
        return this.includeInResponse;
    }


    public void setFacetDisplayNameProvider(String facetDisplayNameProvider)
    {
        this.facetDisplayNameProvider = facetDisplayNameProvider;
    }


    public String getFacetDisplayNameProvider()
    {
        return this.facetDisplayNameProvider;
    }


    public void setFieldValueProvider(String fieldValueProvider)
    {
        this.fieldValueProvider = fieldValueProvider;
    }


    public String getFieldValueProvider()
    {
        return this.fieldValueProvider;
    }


    public void setValueProviderParameter(String valueProviderParameter)
    {
        this.valueProviderParameter = valueProviderParameter;
    }


    public String getValueProviderParameter()
    {
        return this.valueProviderParameter;
    }


    public void setValueProviderParameters(Map<String, String> valueProviderParameters)
    {
        this.valueProviderParameters = valueProviderParameters;
    }


    public Map<String, String> getValueProviderParameters()
    {
        return this.valueProviderParameters;
    }


    public void setFacetSortProvider(String facetSortProvider)
    {
        this.facetSortProvider = facetSortProvider;
    }


    public String getFacetSortProvider()
    {
        return this.facetSortProvider;
    }


    public void setTopValuesProvider(String topValuesProvider)
    {
        this.topValuesProvider = topValuesProvider;
    }


    public String getTopValuesProvider()
    {
        return this.topValuesProvider;
    }


    public void setValueRangeSets(Map<String, ValueRangeSet> valueRangeSets)
    {
        this.valueRangeSets = valueRangeSets;
    }


    public Map<String, ValueRangeSet> getValueRangeSets()
    {
        return this.valueRangeSets;
    }


    public void setFtsQuery(boolean ftsQuery)
    {
        this.ftsQuery = ftsQuery;
    }


    public boolean isFtsQuery()
    {
        return this.ftsQuery;
    }


    public void setFtsQueryMinTermLength(Integer ftsQueryMinTermLength)
    {
        this.ftsQueryMinTermLength = ftsQueryMinTermLength;
    }


    public Integer getFtsQueryMinTermLength()
    {
        return this.ftsQueryMinTermLength;
    }


    public void setFtsQueryBoost(Float ftsQueryBoost)
    {
        this.ftsQueryBoost = ftsQueryBoost;
    }


    public Float getFtsQueryBoost()
    {
        return this.ftsQueryBoost;
    }


    public void setFtsFuzzyQuery(boolean ftsFuzzyQuery)
    {
        this.ftsFuzzyQuery = ftsFuzzyQuery;
    }


    public boolean isFtsFuzzyQuery()
    {
        return this.ftsFuzzyQuery;
    }


    public void setFtsFuzzyQueryMinTermLength(Integer ftsFuzzyQueryMinTermLength)
    {
        this.ftsFuzzyQueryMinTermLength = ftsFuzzyQueryMinTermLength;
    }


    public Integer getFtsFuzzyQueryMinTermLength()
    {
        return this.ftsFuzzyQueryMinTermLength;
    }


    public void setFtsFuzzyQueryFuzziness(Integer ftsFuzzyQueryFuzziness)
    {
        this.ftsFuzzyQueryFuzziness = ftsFuzzyQueryFuzziness;
    }


    public Integer getFtsFuzzyQueryFuzziness()
    {
        return this.ftsFuzzyQueryFuzziness;
    }


    public void setFtsFuzzyQueryBoost(Float ftsFuzzyQueryBoost)
    {
        this.ftsFuzzyQueryBoost = ftsFuzzyQueryBoost;
    }


    public Float getFtsFuzzyQueryBoost()
    {
        return this.ftsFuzzyQueryBoost;
    }


    public void setFtsWildcardQuery(boolean ftsWildcardQuery)
    {
        this.ftsWildcardQuery = ftsWildcardQuery;
    }


    public boolean isFtsWildcardQuery()
    {
        return this.ftsWildcardQuery;
    }


    public void setFtsWildcardQueryMinTermLength(Integer ftsWildcardQueryMinTermLength)
    {
        this.ftsWildcardQueryMinTermLength = ftsWildcardQueryMinTermLength;
    }


    public Integer getFtsWildcardQueryMinTermLength()
    {
        return this.ftsWildcardQueryMinTermLength;
    }


    public void setFtsWildcardQueryType(WildcardType ftsWildcardQueryType)
    {
        this.ftsWildcardQueryType = ftsWildcardQueryType;
    }


    public WildcardType getFtsWildcardQueryType()
    {
        return this.ftsWildcardQueryType;
    }


    public void setFtsWildcardQueryBoost(Float ftsWildcardQueryBoost)
    {
        this.ftsWildcardQueryBoost = ftsWildcardQueryBoost;
    }


    public Float getFtsWildcardQueryBoost()
    {
        return this.ftsWildcardQueryBoost;
    }


    public void setFtsPhraseQuery(boolean ftsPhraseQuery)
    {
        this.ftsPhraseQuery = ftsPhraseQuery;
    }


    public boolean isFtsPhraseQuery()
    {
        return this.ftsPhraseQuery;
    }


    public void setFtsPhraseQuerySlop(Float ftsPhraseQuerySlop)
    {
        this.ftsPhraseQuerySlop = ftsPhraseQuerySlop;
    }


    public Float getFtsPhraseQuerySlop()
    {
        return this.ftsPhraseQuerySlop;
    }


    public void setFtsPhraseQueryBoost(Float ftsPhraseQueryBoost)
    {
        this.ftsPhraseQueryBoost = ftsPhraseQueryBoost;
    }


    public Float getFtsPhraseQueryBoost()
    {
        return this.ftsPhraseQueryBoost;
    }


    public void setClassAttributeAssignment(ClassAttributeAssignmentModel classAttributeAssignment)
    {
        this.classAttributeAssignment = classAttributeAssignment;
    }


    public ClassAttributeAssignmentModel getClassAttributeAssignment()
    {
        return this.classAttributeAssignment;
    }


    public void setCategoryField(boolean categoryField)
    {
        this.categoryField = categoryField;
    }


    public boolean isCategoryField()
    {
        return this.categoryField;
    }


    public void setMultiSelect(boolean multiSelect)
    {
        this.multiSelect = multiSelect;
    }


    public boolean isMultiSelect()
    {
        return this.multiSelect;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public boolean isVisible()
    {
        return this.visible;
    }
}
