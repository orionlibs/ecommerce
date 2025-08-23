package com.hybris.datahub.dto.extension;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dependencyList"})
public class Dependencies
{
    @XmlElement(name = "dependency")
    private List<Dependency> dependencyList;


    public List<Dependency> getDependencyList()
    {
        if(this.dependencyList == null)
        {
            this.dependencyList = new ArrayList<>();
        }
        return this.dependencyList;
    }


    public void addDependency(Dependency dependency)
    {
        getDependencyList().add(dependency);
    }


    public boolean isNotEmpty()
    {
        return (this.dependencyList != null && !this.dependencyList.isEmpty());
    }


    public String toString()
    {
        return "Dependencies{dependencyList=" + this.dependencyList + "}";
    }
}
