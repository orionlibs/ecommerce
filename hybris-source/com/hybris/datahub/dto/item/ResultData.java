package com.hybris.datahub.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultData
{
    private int numberProcessed;


    public int getNumberProcessed()
    {
        return this.numberProcessed;
    }


    public void setNumberProcessed(int number)
    {
        this.numberProcessed = number;
    }
}
