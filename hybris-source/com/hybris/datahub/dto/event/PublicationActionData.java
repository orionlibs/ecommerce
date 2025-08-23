package com.hybris.datahub.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hybris.datahub.dto.publication.TargetSystemPublicationData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicationActionData extends PoolActionData
{
    private List<TargetSystemPublicationData> targetSystemPublications;


    public PublicationActionData()
    {
        setType(PoolActionData.ActionType.PUBLICATION.toString());
    }


    public List<TargetSystemPublicationData> getTargetSystemPublications()
    {
        return this.targetSystemPublications;
    }


    public void setTargetSystemPublications(List<TargetSystemPublicationData> targetSystemPublications)
    {
        this.targetSystemPublications = targetSystemPublications;
    }


    public void addTargetSystemPublication(TargetSystemPublicationData targetSystemPublicationData)
    {
        if(this.targetSystemPublications == null)
        {
            this.targetSystemPublications = new ArrayList<>();
        }
        this.targetSystemPublications.add(targetSystemPublicationData);
    }


    public PublicationActionData toSystem(String name, Optional<PoolActionData.ActionType> optionalActionType)
    {
        optionalActionType.ifPresent(type -> setType(type.toString()));
        addTargetSystemPublication((new TargetSystemPublicationData())
                        .fromPool(getPoolName()).toSystem(name));
        return this;
    }


    public PublicationActionData toSystems(Optional<PoolActionData.ActionType> optionalActionType, String... names)
    {
        return toSystems(Arrays.asList(names), optionalActionType);
    }


    public PublicationActionData toSystems(Collection<String> names, Optional<PoolActionData.ActionType> optionalActionType)
    {
        names.forEach(name -> toSystem(name, optionalActionType));
        return this;
    }


    public PublicationActionData fromPool(String poolName)
    {
        setPoolName(poolName);
        return this;
    }


    public String toString()
    {
        return "PublicationActionData{targetSystemPublications=" + this.targetSystemPublications + super
                        .toString() + "}";
    }
}
