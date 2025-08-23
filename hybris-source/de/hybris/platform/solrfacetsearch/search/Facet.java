package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.config.FacetType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Facet implements Serializable, Comparable<Facet>
{
    private static final long serialVersionUID = 1L;
    private static final String NAME_MUST_NOT_BE_NULL = "name must not be null";
    private final String name;
    private final String displayName;
    private List<FacetValue> facetValues;
    private List<FacetValue> topFacetValues;
    private List<FacetValue> selectedFacetValues;
    private List<FacetValue> allFacetValues;
    private FacetType facetType;
    private int priority;
    private boolean multiselect;
    private final Set<String> tags = new HashSet<>();


    public Facet(String name, List<FacetValue> facetValues)
    {
        if(name == null)
        {
            throw new IllegalArgumentException();
        }
        if(facetValues == null)
        {
            throw new IllegalArgumentException("facetValues must not be null");
        }
        this.name = name;
        this.displayName = name;
        this.facetValues = facetValues;
        this.topFacetValues = new ArrayList<>();
        this.selectedFacetValues = new ArrayList<>();
    }


    public Facet(String name, String displayName, List<FacetValue> facetValues, List<FacetValue> topFacetValues, FacetType facetType, int priority)
    {
        if(name == null)
        {
            throw new IllegalArgumentException("name must not be null");
        }
        if(facetValues == null)
        {
            throw new IllegalArgumentException("facetValues must not be null");
        }
        this.name = name;
        this.displayName = StringUtils.isBlank(displayName) ? name : displayName;
        this.facetValues = facetValues;
        this.topFacetValues = topFacetValues;
        this.selectedFacetValues = new ArrayList<>();
        this.facetType = facetType;
        this.priority = priority;
    }


    public Facet(String name, String displayName, List<FacetValue> facetValues, List<FacetValue> topFacetValues, List<FacetValue> selectedFacetValues, FacetType facetType, int priority)
    {
        if(name == null)
        {
            throw new IllegalArgumentException("name must not be null");
        }
        if(facetValues == null)
        {
            throw new IllegalArgumentException("facetValues must not be null");
        }
        this.name = name;
        this.displayName = StringUtils.isBlank(displayName) ? name : displayName;
        this.facetValues = facetValues;
        this.topFacetValues = topFacetValues;
        this.selectedFacetValues = selectedFacetValues;
        this.facetType = facetType;
        this.priority = priority;
    }


    public void setFacetValues(List<FacetValue> facetValues)
    {
        this.facetValues = facetValues;
    }


    public List<FacetValue> getFacetValues()
    {
        return this.facetValues;
    }


    public String getName()
    {
        return this.name;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }


    public FacetType getFacetType()
    {
        return this.facetType;
    }


    public void setFacetType(FacetType facetType)
    {
        this.facetType = facetType;
    }


    public int getPriority()
    {
        return this.priority;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public boolean isMultiselect()
    {
        return this.multiselect;
    }


    public void setMultiselect(boolean multiselect)
    {
        this.multiselect = multiselect;
    }


    public List<FacetValue> getTopFacetValues()
    {
        return this.topFacetValues;
    }


    public void setTopFacetValues(List<FacetValue> topFacetValues)
    {
        this.topFacetValues = topFacetValues;
    }


    public List<FacetValue> getSelectedFacetValues()
    {
        return this.selectedFacetValues;
    }


    public void setSelectedFacetValues(List<FacetValue> selectedFacetValues)
    {
        this.selectedFacetValues = selectedFacetValues;
    }


    public List<FacetValue> getAllFacetValues()
    {
        return this.allFacetValues;
    }


    public void setAllFacetValues(List<FacetValue> allFacetValues)
    {
        this.allFacetValues = allFacetValues;
    }


    public Set<String> getTags()
    {
        return this.tags;
    }


    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.name.hashCode();
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        Facet that = (Facet)obj;
        return (new EqualsBuilder()).append(this.name, that.name).isEquals();
    }


    public int compareTo(@Nonnull Facet other)
    {
        return this.name.compareTo(other.name);
    }
}
