package com.hybris.datahub.dto.extension;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"targetSystemList"})
public class TargetSystems
{
    @XmlElement(name = "targetSystem")
    private List<TargetSystem> targetSystemList;


    public boolean isEmpty()
    {
        return getTargetSystemList().isEmpty();
    }


    public List<TargetSystem> getTargetSystemList()
    {
        if(this.targetSystemList == null)
        {
            this.targetSystemList = new ArrayList<>();
        }
        return this.targetSystemList;
    }


    public TargetSystem getTargetSystem(String name)
    {
        assert name != null;
        TargetSystem returnTargetSystem = null;
        for(TargetSystem targetSystem : this.targetSystemList)
        {
            if(targetSystem.getName().equals(name))
            {
                returnTargetSystem = targetSystem;
            }
        }
        return returnTargetSystem;
    }


    public TargetSystems withSystem(TargetSystem system)
    {
        getTargetSystemList().add(system);
        return this;
    }


    public String toString()
    {
        return "TargetSystems{targetSystemList=" + this.targetSystemList + "}";
    }
}
