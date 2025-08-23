package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class FacetValue implements Serializable, Comparable<FacetValue>
{
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String displayName;
    private final long count;
    private final boolean selected;
    private Set<String> tags;


    public FacetValue(String name, long count, boolean selected)
    {
        this(name, name, count, selected);
    }


    public FacetValue(String name, String displayName, long count, boolean selected)
    {
        this.name = name;
        this.displayName = displayName;
        this.count = count;
        this.selected = selected;
        this.tags = Collections.emptySet();
    }


    public String getName()
    {
        return this.name;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }


    public long getCount()
    {
        return this.count;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public void addTag(String tag)
    {
        if(CollectionUtils.isEmpty(this.tags))
        {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag);
    }


    public Set<String> getTags()
    {
        return this.tags;
    }


    public int compareTo(FacetValue anotherFacetValue)
    {
        return this.name.compareTo(anotherFacetValue.name);
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
        FacetValue that = (FacetValue)obj;
        return (new EqualsBuilder())
                        .append(this.name, that.name)
                        .append(this.displayName, that.displayName)
                        .append(this.count, that.count)
                        .append(this.selected, that.selected)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.name, this.displayName, Long.valueOf(this.count)});
    }


    public String toString()
    {
        return getClass().getName() + " [" + getClass().getName() + " (" + this.name + ")]";
    }
}
