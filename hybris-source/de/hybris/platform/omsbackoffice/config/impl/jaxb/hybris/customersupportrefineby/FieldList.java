package de.hybris.platform.omsbackoffice.config.impl.jaxb.hybris.customersupportrefineby;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldList", propOrder = {"searchField"})
public class FieldList
{
    @Mergeable(key = {"name"})
    @XmlElement(name = "search-field")
    protected List<SearchField> searchField;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    public List<SearchField> getSearchField()
    {
        if(this.searchField == null)
        {
            this.searchField = new ArrayList<>();
        }
        return this.searchField;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public MergeMode getMergeMode()
    {
        return this.mergeMode;
    }


    public void setMergeMode(MergeMode value)
    {
        this.mergeMode = value;
    }
}
