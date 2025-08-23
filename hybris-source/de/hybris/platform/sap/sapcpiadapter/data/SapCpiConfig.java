package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiConfig")
public class SapCpiConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SapCpiTargetSystem sapCpiTargetSystem;


    public void setSapCpiTargetSystem(SapCpiTargetSystem sapCpiTargetSystem)
    {
        this.sapCpiTargetSystem = sapCpiTargetSystem;
    }


    @XmlElement(name = "sapCpiTargetSystem")
    public SapCpiTargetSystem getSapCpiTargetSystem()
    {
        return this.sapCpiTargetSystem;
    }
}
