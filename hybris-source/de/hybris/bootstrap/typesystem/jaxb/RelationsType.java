package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relationsType", propOrder = {"relation"})
public class RelationsType
{
    protected List<RelationType> relation;


    public List<RelationType> getRelation()
    {
        if(this.relation == null)
        {
            this.relation = new ArrayList<>();
        }
        return this.relation;
    }
}
