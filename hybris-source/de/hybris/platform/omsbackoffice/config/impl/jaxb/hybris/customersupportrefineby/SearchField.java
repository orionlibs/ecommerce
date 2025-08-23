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
@XmlType(name = "SearchField", propOrder = {"searchValue"})
public class SearchField
{
    @Mergeable(key = {"label"})
    @XmlElement(name = "search-value", required = true)
    protected List<SearchValue> searchValue;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    public List<SearchValue> getSearchValue()
    {
        if(this.searchValue == null)
        {
            this.searchValue = new ArrayList<>();
        }
        return this.searchValue;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String value)
    {
        this.label = value;
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
