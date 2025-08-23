package de.hybris.platform.solrfacetsearch.indexer.xml.add;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"doc"})
@XmlRootElement(name = "add")
public class Add
{
    protected List<DocType> doc;


    public List<DocType> getDoc()
    {
        if(this.doc == null)
        {
            this.doc = new ArrayList<>();
        }
        return this.doc;
    }
}
