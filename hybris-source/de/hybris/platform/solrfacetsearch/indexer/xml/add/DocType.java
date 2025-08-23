package de.hybris.platform.solrfacetsearch.indexer.xml.add;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "docType", propOrder = {"field"})
public class DocType
{
    protected List<FieldType> field;


    public List<FieldType> getField()
    {
        if(this.field == null)
        {
            this.field = new ArrayList<>();
        }
        return this.field;
    }
}
