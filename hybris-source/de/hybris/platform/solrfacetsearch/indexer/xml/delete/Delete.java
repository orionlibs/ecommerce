package de.hybris.platform.solrfacetsearch.indexer.xml.delete;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "query"})
@XmlRootElement(name = "delete")
public class Delete
{
    protected List<String> id;
    protected List<String> query;


    public List<String> getId()
    {
        if(this.id == null)
        {
            this.id = new ArrayList<>();
        }
        return this.id;
    }


    public List<String> getQuery()
    {
        if(this.query == null)
        {
            this.query = new ArrayList<>();
        }
        return this.query;
    }
}
