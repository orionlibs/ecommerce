package de.hybris.platform.cockpit.model.search;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Query implements Cloneable
{
    private int start;
    private int count;
    private String simpleText;
    private boolean orMode = false;
    private Set<FacetValue> selectedFacetValues;
    private List<SearchParameterValue> parameterValues;
    private List<List<SearchParameterValue>> parameterOrValues;
    private Map<PropertyDescriptor, Boolean> sortProperties;
    private Set<SearchType> selectedTypes;
    private Map<String, Object> context;
    private boolean excludeSubTypes = false;
    private boolean needTotalCount;
    private final Set<TypedObject> excludedItems = new HashSet<>();
    private boolean simpleSearch;


    public Query(Collection<SearchType> types, String simpleText, int start, int count)
    {
        this.simpleText = simpleText;
        this.start = start;
        this.count = count;
        this.selectedTypes = (types != null) ? Collections.<SearchType>unmodifiableSet(new LinkedHashSet<>(types)) : null;
        this.needTotalCount = true;
    }


    public void setSelectedTypes(Collection<SearchType> types)
    {
        this.selectedTypes = (types == null) ? null : Collections.<SearchType>unmodifiableSet(new LinkedHashSet<>(types));
    }


    public int getStart()
    {
        return this.start;
    }


    public int getCount()
    {
        return this.count;
    }


    public String getSimpleText()
    {
        return this.simpleText;
    }


    public void setStart(int start)
    {
        this.start = start;
    }


    public void setCount(int count)
    {
        this.count = count;
    }


    public void setExcludedItems(Collection<TypedObject> excludedItems)
    {
        this.excludedItems.clear();
        if(excludedItems != null && !excludedItems.isEmpty())
        {
            this.excludedItems.addAll(excludedItems);
        }
    }


    public Collection<TypedObject> getExcludedItems()
    {
        return Collections.unmodifiableSet(this.excludedItems);
    }


    public void clearExcludedItems()
    {
        this.excludedItems.clear();
    }


    public void setSimpleText(String simpleText)
    {
        this.simpleText = simpleText;
    }


    public Set<FacetValue> getSelectedFacetValues()
    {
        return (this.selectedFacetValues != null) ? Collections.<FacetValue>unmodifiableSet(this.selectedFacetValues) : Collections.EMPTY_SET;
    }


    public void setSelectedFacetValues(Collection<FacetValue> selectedFacetValues)
    {
        this.selectedFacetValues = (selectedFacetValues != null) ? new LinkedHashSet<>(selectedFacetValues) : null;
    }


    public void addSelectedFacetValue(FacetValue value)
    {
        if(this.selectedFacetValues == null)
        {
            this.selectedFacetValues = new LinkedHashSet<>();
        }
        this.selectedFacetValues.add(value);
    }


    public void removeSelectedFacetValue(FacetValue value)
    {
        if(this.selectedFacetValues != null)
        {
            this.selectedFacetValues.remove(value);
        }
    }


    public List<SearchParameterValue> getParameterValues()
    {
        return (this.parameterValues == null) ? Collections.EMPTY_LIST : Collections.<SearchParameterValue>unmodifiableList(this.parameterValues);
    }


    public List<List<SearchParameterValue>> getParameterOrValues()
    {
        return (this.parameterOrValues == null) ? Collections.EMPTY_LIST : Collections.<List<SearchParameterValue>>unmodifiableList(this.parameterOrValues);
    }


    public void setParameterValues(List<SearchParameterValue> parameterValues)
    {
        this.parameterValues = (parameterValues != null) ? new ArrayList<>(parameterValues) : null;
    }


    public void addParameterValue(SearchParameterValue value)
    {
        if(this.parameterValues == null)
        {
            this.parameterValues = new LinkedList<>();
        }
        this.parameterValues.add(value);
    }


    public void addParameterOrValues(List<SearchParameterValue> orValues)
    {
        if(this.parameterOrValues == null)
        {
            this.parameterOrValues = new LinkedList<>();
        }
        this.parameterOrValues.add(orValues);
    }


    public void setParameterOrValues(List<List<SearchParameterValue>> orValues)
    {
        this.parameterOrValues = (orValues == null) ? null : new ArrayList<>(orValues);
    }


    public void clearParameterValues()
    {
        if(this.parameterValues != null)
        {
            this.parameterValues.clear();
        }
    }


    public void clearParameterOrValues()
    {
        if(this.parameterOrValues != null)
        {
            this.parameterOrValues.clear();
        }
    }


    public void removeParameterValue(SearchParameterValue value)
    {
        if(this.parameterValues != null)
        {
            this.parameterValues.remove(value);
        }
    }


    public Map<PropertyDescriptor, Boolean> getSortCriteria()
    {
        return (this.sortProperties == null) ? Collections.EMPTY_MAP : Collections.<PropertyDescriptor, Boolean>unmodifiableMap(this.sortProperties);
    }


    public void setSortCriteria(Map<PropertyDescriptor, Boolean> criteria)
    {
        this.sortProperties = (criteria != null) ? new LinkedHashMap<>(criteria) : null;
    }


    public void addSortCriterion(PropertyDescriptor sortProperty, boolean asc)
    {
        if(this.sortProperties == null)
        {
            this.sortProperties = new LinkedHashMap<>();
        }
        this.sortProperties.put(sortProperty, Boolean.valueOf(asc));
    }


    public void removeSortCriterion(PropertyDescriptor sortProperty)
    {
        if(this.sortProperties != null)
        {
            this.sortProperties.remove(sortProperty);
        }
    }


    public boolean isOrMode()
    {
        return this.orMode;
    }


    public void setOrMode(boolean orMode)
    {
        this.orMode = orMode;
    }


    public Set<SearchType> getSelectedTypes()
    {
        return (this.selectedTypes != null) ? this.selectedTypes : Collections.EMPTY_SET;
    }


    public Object getContextParameter(String key)
    {
        return (this.context != null) ? this.context.get(key) : null;
    }


    public void setContextParameter(String key, Object value)
    {
        if(this.context == null)
        {
            this.context = new HashMap<>();
        }
        this.context.put(key, value);
    }


    public Object clone() throws CloneNotSupportedException
    {
        Query ret = new Query(getSelectedTypes(), getSimpleText(), getStart(), getCount());
        if(this.context != null)
        {
            for(Map.Entry<String, Object> entry : this.context.entrySet())
            {
                ret.setContextParameter(entry.getKey(), entry.getValue());
            }
        }
        ret.setExcludeSubTypes(isExcludeSubTypes());
        ret.setOrMode(isOrMode());
        ret.setSelectedFacetValues(new HashSet<>(getSelectedFacetValues()));
        ret.setParameterValues(new ArrayList<>(getParameterValues()));
        ret.setParameterOrValues(new ArrayList<>(getParameterOrValues()));
        ret.setSortCriteria(new HashMap<>(getSortCriteria()));
        ret.setSelectedTypes(new HashSet<>(getSelectedTypes()));
        ret.setExcludedItems(new HashSet<>(getExcludedItems()));
        ret.setNeedTotalCount(isNeedTotalCount());
        return ret;
    }


    public boolean isExcludeSubTypes()
    {
        return this.excludeSubTypes;
    }


    public void setExcludeSubTypes(boolean excludeSubTypes)
    {
        this.excludeSubTypes = excludeSubTypes;
    }


    public boolean isNeedTotalCount()
    {
        return this.needTotalCount;
    }


    public void setNeedTotalCount(boolean needTotalCount)
    {
        this.needTotalCount = needTotalCount;
    }


    public void setSimpleSearch(boolean simpleSearch)
    {
        this.simpleSearch = simpleSearch;
    }


    public boolean isSimpleSearch()
    {
        return this.simpleSearch;
    }
}
