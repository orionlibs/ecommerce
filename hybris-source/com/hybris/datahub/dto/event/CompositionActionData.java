package com.hybris.datahub.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompositionActionData extends PoolActionData
{
    private Long count;


    public Long getCount()
    {
        return this.count;
    }


    public void setCount(Long count)
    {
        this.count = count;
    }
}
