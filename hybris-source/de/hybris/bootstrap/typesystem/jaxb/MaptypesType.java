package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maptypesType", propOrder = {"maptype"})
public class MaptypesType
{
    protected List<MaptypeType> maptype;


    public List<MaptypeType> getMaptype()
    {
        if(this.maptype == null)
        {
            this.maptype = new ArrayList<>();
        }
        return this.maptype;
    }
}
