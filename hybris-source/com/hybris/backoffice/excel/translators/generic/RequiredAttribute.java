package com.hybris.backoffice.excel.translators.generic;

import de.hybris.platform.core.model.type.TypeModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RequiredAttribute implements Comparable<RequiredAttribute>
{
    private final String enclosingType;
    private final String qualifier;
    private final TypeModel typeModel;
    private final boolean unique;
    private final boolean mandatory;
    private final boolean partOf;
    private RequiredAttribute parent;
    private final List<RequiredAttribute> children = new ArrayList<>();


    public RequiredAttribute(TypeModel typeModel, String enclosingType, String qualifier, boolean unique, boolean mandatory, boolean partOf)
    {
        this.typeModel = typeModel;
        this.enclosingType = enclosingType;
        this.qualifier = qualifier;
        this.unique = unique;
        this.mandatory = mandatory;
        this.partOf = partOf;
    }


    public TypeModel getTypeModel()
    {
        return this.typeModel;
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public boolean isMandatory()
    {
        return this.mandatory;
    }


    public void addChild(RequiredAttribute child)
    {
        child.parent = this;
        this.children.add(child);
        Collections.sort(this.children);
    }


    public String getEnclosingType()
    {
        return this.enclosingType;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public List<RequiredAttribute> getChildren()
    {
        return this.children;
    }


    public boolean isPartOf()
    {
        return this.partOf;
    }


    private int calculateBranchDepth()
    {
        int sum = this.children.size();
        for(RequiredAttribute child : this.children)
        {
            sum += child.calculateBranchDepth();
        }
        return sum;
    }


    public boolean isRoot()
    {
        return (this.parent == null);
    }


    public int compareTo(RequiredAttribute o)
    {
        return calculateBranchDepth() - o.calculateBranchDepth();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != getClass())
        {
            return false;
        }
        RequiredAttribute that = (RequiredAttribute)o;
        return (Objects.equals(getEnclosingType(), that.getEnclosingType()) && Objects.equals(getQualifier(), that.getQualifier()));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {getEnclosingType(), getQualifier()});
    }
}
