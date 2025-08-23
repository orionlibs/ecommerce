package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "atomictypesType", propOrder = {"atomictype"})
public class AtomictypesType
{
    protected List<AtomictypeType> atomictype;


    public List<AtomictypeType> getAtomictype()
    {
        if(this.atomictype == null)
        {
            this.atomictype = new ArrayList<>();
        }
        return this.atomictype;
    }
}
