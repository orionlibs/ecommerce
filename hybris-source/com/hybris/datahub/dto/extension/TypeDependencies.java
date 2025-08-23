package com.hybris.datahub.dto.extension;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dependsOn"})
public class TypeDependencies
{
    @XmlElement(name = "dependency")
    private List<String> dependsOn = new ArrayList<>();


    public List<String> getDependsOn()
    {
        return this.dependsOn;
    }


    public TypeDependencies addDependency(String dependency)
    {
        this.dependsOn.add(dependency);
        return this;
    }


    public boolean isNotEmpty()
    {
        return !this.dependsOn.isEmpty();
    }


    public String toString()
    {
        return "DependsOn" + this.dependsOn.toString();
    }
}
